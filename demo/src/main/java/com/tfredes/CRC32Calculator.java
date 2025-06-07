package com.tfredes;

import java.util.zip.CRC32;

public class CRC32Calculator {
    public static long calculateCRC(String data) {
        CRC32 crc = new CRC32();
        crc.update(data.getBytes());
        return crc.getValue();
    }
}