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
        return String.format("%s;%s;%s;%s;%s;%s",
            DATA_HEADER, source, destination, status, crc, messageContent);
    }

    public static String[] parseDataPacket(String packet) {
        return packet.split(";", 6); // Divide em 6 partes
    }
}