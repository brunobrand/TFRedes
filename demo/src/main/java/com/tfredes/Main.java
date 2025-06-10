// Main.java

package com.tfredes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // MODIFICATION: Check for two arguments now
        if(args.length < 2) {
            System.err.println("Uso: java com.tfredes.Main <arquivo_config> <porta_local>");
            System.exit(1);
        }
        
        try {
            String configFile = args[0];
            // MODIFICATION: Parse the listen port from the second argument
            int listenPort = Integer.parseInt(args[1]);

            InputStream is = Main.class.getResourceAsStream("/" + configFile);
            if(is == null) {
                throw new IOException("Arquivo de configuração não encontrado: " + configFile);
            }
            
            // MODIFICATION: Pass the listenPort to the ConfigLoader constructor
            ConfigLoader config = new ConfigLoader(new InputStreamReader(is), listenPort);
            RingNode node = new RingNode(config);
            node.start();
            
            Scanner scanner = new Scanner(System.in);
            while(true) {
                // ... (The rest of your menu loop remains the same)
                synchronized(RingNode.class) {
                    System.out.println("\n[" + config.getNickname() + "] Comandos:");
                    System.out.println("1. Enviar mensagem");
                    System.out.println("2. Ver fila de mensagens");
                    System.out.println("3. Ver estado do nó");
                    System.out.println("4. Sair");
                    System.out.print("Opção: ");
                }
            
                String input = scanner.nextLine().trim();
                int option;
                try {
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Digite um número entre 1 e 4.");
                    continue;
                }
                    
                switch(option) {
                    case 1:
                        System.out.print("Destino (nome ou TODOS): ");
                        String dest = scanner.nextLine();
                        System.out.print("Mensagem: ");
                        String msg = scanner.nextLine();
                        node.addMessage(dest, msg);
                        break;
                    case 2:
                        node.printQueue();
                        break;
                    case 3:
                        node.printStatus();
                        break;
                    case 4:
                        System.exit(0);
                }
            }
        } catch (IOException e) { 
            System.err.println("Erro de I/O: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NumberFormatException e) { // Good practice to catch this for the port
            System.err.println("Erro: A porta informada não é um número válido.");
            System.exit(1);
        }
    }
}