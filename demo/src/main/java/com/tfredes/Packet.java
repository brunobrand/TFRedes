package com.tfredes;

public class Packet {
    public static final String TOKEN = "1000";
    public static final String DATA_HEADER = "2000";
    
    public static String createDataPacket(
        String source, 
        String destination, 
        String status, 
        String crc, 
        String messageContent
    ) {
        // CORREÇÃO: Usa o formato HEADER;<payload_com_dois_pontos>
        String payload = String.join(":", source, destination, status, crc, messageContent);
        return DATA_HEADER + ";" + payload;
    }

    public static String[] parseDataPacket(String packet) {
        // CORREÇÃO: Divide primeiro o cabeçalho, depois o payload
        String[] parts = packet.split(";", 2); // Divide em 2 partes: HEADER e PAYLOAD
        if (parts.length < 2) {
            // Retorna um array inválido para indicar erro no formato
            return new String[0]; 
        }
        String[] payloadParts = parts[1].split(":", 5); // Divide o payload em 5 partes
        
        // Monta um array final compatível com o resto do código
        String[] finalParts = new String[6];
        finalParts[0] = parts[0]; // Header (2000)
        System.arraycopy(payloadParts, 0, finalParts, 1, payloadParts.length);
        
        return finalParts;
    }
}