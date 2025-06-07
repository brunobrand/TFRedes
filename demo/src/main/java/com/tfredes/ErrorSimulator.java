package com.tfredes;

public class ErrorSimulator {
    private static final double ERROR_PROBABILITY = 0.1; // 10% chance de erro
    
    public static String maybeCorrupt(String packet) {
        if(Math.random() < ERROR_PROBABILITY) {
            return corruptPacket(packet);
        }
        return packet;
    }
    
    private static String corruptPacket(String packet) {
        char[] chars = packet.toCharArray();
        int pos = (int) (Math.random() * (chars.length - 10)) + 5; // Evita corromper cabeçalho
        chars[pos] = (char) (chars[pos] + 1);
        return new String(chars);
    }
}