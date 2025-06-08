package com.tfredes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ConfigLoader {
    private String nextNodeIP;
    private int nextNodePort;
    private String nickname;
    private int listenPort;
    private int tokenHoldTime;
    private boolean isTokenGenerator;

    public ConfigLoader(Reader reader) throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            String[] ipPort = br.readLine().split(":");
            this.nextNodeIP = ipPort[0];
            this.nextNodePort = Integer.parseInt(ipPort[1]);
            this.nickname = br.readLine();
            this.listenPort = Integer.parseInt(br.readLine());  // NOVO
            this.tokenHoldTime = Integer.parseInt(br.readLine());
            this.isTokenGenerator = Boolean.parseBoolean(br.readLine());
        }
    }

    public int getListenPort() {
        return listenPort;
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