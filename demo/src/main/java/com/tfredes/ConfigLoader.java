package com.tfredes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigLoader {
    private String nextNodeIP;
    private int nextNodePort;
    private String nickname;
    private int tokenHoldTime;
    private boolean isTokenGenerator;

    public ConfigLoader(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String[] ipPort = reader.readLine().split(":");
            this.nextNodeIP = ipPort[0];
            this.nextNodePort = Integer.parseInt(ipPort[1]);
            this.nickname = reader.readLine();
            this.tokenHoldTime = Integer.parseInt(reader.readLine());
            this.isTokenGenerator = Boolean.parseBoolean(reader.readLine());
        }
    }

    public String getNextNodeIP() {
        return nextNodeIP;
    }

    public int getNextNodePort() {
        return nextNodePort;
    }    

    public String getNickname() {
        return nickname;
    }

    public int getTokenHoldTime() {
        return tokenHoldTime;
    }

    public boolean isTokenGenerator() {
        return isTokenGenerator;
    }   
}