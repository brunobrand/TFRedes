package com.tfredes;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length < 1) {
            System.err.println("Uso: java Main <arquivo_config>");
            System.exit(1);
        }
        
        ConfigLoader config = new ConfigLoader("config.txt");
        RingNode node = new RingNode(config);
        node.start();
        node.startTokenControl();
        
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("\nComandos:");
            System.out.println("1. Enviar mensagem");
            System.out.println("2. Ver fila");
            System.out.println("3. Ver estado do nó");
            System.out.println("4. Sair");
            System.out.print("Opção: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            
            switch(option) {
                case 1:
                    System.out.print("Destino: ");
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
    }
}