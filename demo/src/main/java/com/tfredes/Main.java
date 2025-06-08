package com.tfredes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Uso: java com.tfredes.Main <arquivo_config>");
            System.exit(1);
        }
        
        try {
            // Carrega o arquivo de configuração do classpath
            InputStream is = Main.class.getResourceAsStream("/" + args[0]);
            if(is == null) {
                throw new IOException("Arquivo de configuração não encontrado: " + args[0]);
            }
            
            ConfigLoader config = new ConfigLoader(new InputStreamReader(is));
            RingNode node = new RingNode(config);
            node.start();
            
            Scanner scanner = new Scanner(System.in);
            while(true) {
            // Menu protegido contra interferências
            synchronized(RingNode.class) {
                System.out.println("\n[" + config.getNickname() + "] Comandos:");
                System.out.println("1. Enviar mensagem");
                System.out.println("2. Ver fila de mensagens");
                System.out.println("3. Ver estado do nó");
                System.out.println("4. Sair");
                System.out.print("Opção: ");
            }
            
            // Leitura mais segura da opção
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
        } catch (IOException e) { // Captura apenas IOException
            System.err.println("Erro fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}