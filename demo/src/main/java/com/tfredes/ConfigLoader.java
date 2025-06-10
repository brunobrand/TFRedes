package com.tfredes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ConfigLoader {
    private String nextNodeIP;
    private int nextNodePort;
    private String nickname;
    private int listenPort; // This field remains
    private int tokenHoldTime;
    private boolean isTokenGenerator;

    // MODIFICATION: The constructor now takes the listenPort as an argument
    public ConfigLoader(Reader reader, int listenPort) throws IOException {
        this.listenPort = listenPort; // Assign the port from the argument
        try (BufferedReader br = new BufferedReader(reader)) {
            String[] ipPort = br.readLine().split(":");
            this.nextNodeIP = ipPort[0];
            this.nextNodePort = Integer.parseInt(ipPort[1]);
            this.nickname = br.readLine();
            // REMOVED: The line reading the listenPort from the file is gone.
            // this.listenPort = Integer.parseInt(br.readLine());
            this.tokenHoldTime = Integer.parseInt(br.readLine());
            // The fourth line in the file is now the token generator flag
            this.isTokenGenerator = Boolean.parseBoolean(br.readLine());
        }
    }

    // --- All getter methods remain the same ---
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