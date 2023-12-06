package com.example.maraicher;

import java.net.Socket;

public class SocketSingleton {
    private static SocketSingleton instance;
    private Socket socket;

    private SocketSingleton() {
        // Constructeur privé pour éviter l'instanciation directe.
    }

    public static synchronized SocketSingleton getInstance() {
        if (instance == null) {
            instance = new SocketSingleton();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
