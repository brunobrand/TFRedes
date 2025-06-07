package com.tfredes;

import java.net.*;
import java.util.concurrent.*;

public class RingNode {
    private static final int MAX_QUEUE_SIZE = 10;
    private BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    private ConfigLoader config;
    private DatagramSocket socket;
    private boolean hasToken = false;
    private ScheduledExecutorService tokenMonitor;
    private final ErrorSimulator errorSimulator = new ErrorSimulator();
    private long lastTokenTime = 0;
    private ScheduledExecutorService scheduler;

    public RingNode(ConfigLoader config) throws SocketException {
        this.config = config;
        this.socket = new DatagramSocket(config.getNextNodePort());
    }

    public void printStatus() {
        System.out.println("\n--- ESTADO DO NÓ ---");
        System.out.println("Apelido: " + config.getNickname());
        System.out.println("Próximo: " + config.getNextNodeIP() + ":" + config.getNextNodePort());
        System.out.println("Tem token: " + hasToken);
        System.out.println("Mensagens na fila: " + messageQueue.size() + "/10");
    }

    public void startTokenControl() {
        if(config.isTokenGenerator()) {
            tokenMonitor = Executors.newSingleThreadScheduledExecutor();
            tokenMonitor.scheduleAtFixedRate(this::checkTokenStatus, 30, 30, TimeUnit.SECONDS);
        }
    }

    private void checkTokenStatus() {
        if(!hasToken) {
            System.out.println("[ALERTA] Token perdido! Gerando novo...");
            sendToken();
        }
    }

    public void start() {
        // Thread para receber pacotes
        new Thread(this::receivePackets).start();
        
        startTokenControl();

        // Se for gerador inicial, cria primeiro token
        if(config.isTokenGenerator()) {
            sendToken();
        }
    }

    // === NOVOS MÉTODOS ADICIONADOS ===
    public void addMessage(String destination, String content) {
        if(messageQueue.size() < MAX_QUEUE_SIZE) {
            messageQueue.offer(new Message(destination, content));
            System.out.println("Mensagem adicionada à fila: " + content);
        } else {
            System.out.println("ERRO: Fila cheia! Mensagem descartada.");
        }
    }

    public void printQueue() {
        System.out.println("\n--- FILA DE MENSAGENS (" + config.getNickname() + ") ---");
        if(messageQueue.isEmpty()) {
            System.out.println("(vazia)");
            return;
        }

        int index = 0;
        for(Message msg : messageQueue) {
            System.out.printf("%d. Destino: %s | Conteúdo: %s\n", 
                             index++, msg.destination, msg.content);
        }
    }

    private void receivePackets() {
        byte[] buffer = new byte[1024];
        while(true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                
                if(received.equals(Packet.TOKEN)) {
                    handleToken();
                } else if(received.startsWith(Packet.DATA_HEADER)) {
                    handleDataPacket(received);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleToken() {
        hasToken = true;
        lastTokenTime = System.currentTimeMillis();
        System.out.println("[TOKEN] Recebido em " + config.getNickname());
        
        // Simula tempo de espera configurado
        try {
            Thread.sleep(config.getTokenHoldTime() * 1000);
        } catch (InterruptedException e) {}
        
        processWithToken();
    }

    private void processWithToken() {
        if(!messageQueue.isEmpty()) {
            sendMessage();
        } else {
            forwardToken();
        }
    }

    private void forwardToken() {
        sendToken();
        hasToken = false;
    }

    private void sendToken() {
        try {
            InetAddress address = InetAddress.getByName(config.getNextNodeIP());
            byte[] data = Packet.TOKEN.getBytes();
            DatagramPacket packet = new DatagramPacket(
                data, data.length, address, config.getNextNodePort()
            );
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
    Message message = messageQueue.poll();
    if(message != null) {
        String crc = String.valueOf(CRC32Calculator.calculateCRC(message.content));
        String packet = Packet.createDataPacket(
            config.getNickname(),
            message.destination,
            "maquinanaoexiste", // Status inicial
            crc,
            message.content
        );
        
        packet = ErrorSimulator.maybeCorrupt(packet); // Aplicar corrupção
        forwardDataPacket(packet);
    }
        forwardToken();
    }

    

    private String corruptPacket(String packet) {
        // Corrompe o CRC para simular erro
        String[] parts = packet.split(";");
        parts[4] = "00000000"; // CRC inválido
        return String.join(";", parts);
    }

    private void forwardDataPacket(String packetData) {
        try {
            InetAddress address = InetAddress.getByName(config.getNextNodeIP());
            byte[] data = packetData.getBytes();
            DatagramPacket packet = new DatagramPacket(
                data, data.length, address, config.getNextNodePort()
            );
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDataPacket(String packetData) {
        String[] parts = Packet.parseDataPacket(packetData);
        String source = parts[1];
        String destination = parts[2];
        String status = parts[3];
        String receivedCRC = parts[4];
        String message = parts[5];
        
        // Se for destinado a esta máquina
        if(destination.equals(config.getNickname()) || destination.equals("TODOS")) {
            long calculatedCRC = CRC32Calculator.calculateCRC(message);
            
            if(Long.parseLong(receivedCRC) == calculatedCRC) {
                status = "ACK";
                System.out.println("[MSG] De " + source + ": " + message);
            } else {
                status = "NAK";
                System.out.println("[ERRO] CRC inválido de " + source);
            }
        }
        
        // Se voltou para a origem
        if(source.equals(config.getNickname())) {
            handleReturnedPacket(status, message);
        } else {
            // Repassa o pacote atualizado
            String newPacket = Packet.createDataPacket(
                source, destination, status, receivedCRC, message
            );
            forwardDataPacket(newPacket);
        }
    }

    private void handleReturnedPacket(String status, String message) {
        switch(status) {
            case "maquinanaoexiste":
                System.out.println("[ERRO] Máquina destino não existe");
                break;
            case "NAK":
                System.out.println("[RETRANSMISSÃO] Solicitada");
                // Adiciona novamente na fila (1 retentativa)
                messageQueue.offer(new Message(message.destination, message.content));
                break;
            case "ACK":
                System.out.println("[SUCESSO] Mensagem entregue");
                break;
        }
    }
}