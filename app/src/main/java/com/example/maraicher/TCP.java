package com.example.maraicher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCP {

    public static final int TAILLE_MAX_DATA = 1500;

    public Socket socket;

    // Modifiez le constructeur pour prendre une socket en paramètre
    public TCP(Socket socket) {
        this.socket = socket;
    }

    /**
     * Ferme la connexion du client.
     * Cette méthode doit être appelée lorsque la connexion n'est plus nécessaire.
     */
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie des données au serveur de manière synchrone.
     *
     * @param data Tableau de bytes représentant les données à envoyer.
     * @param size Taille des données à envoyer.
     * @return La taille des données envoyées ou -1 en cas d'échec.
     */
    public int send(byte[] data, int size) {
        if (size > TAILLE_MAX_DATA) {
            return -1;
        }

        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(data, 0, size);
            outputStream.write('#');
            outputStream.write(')');
            outputStream.flush();
            return size;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Méthode pour la logique de réception des données depuis le serveur.
     * À adapter en fonction des besoins spécifiques de réception de votre application.
     *
     * @param data Tableau de bytes pour stocker les données reçues.
     * @return La taille des données reçues.
     */
    public static int receive(Socket socket, byte[] data) {
        boolean finished = false;
        int bytesRead;
        int i = 0;

        try {
            InputStream inputStream = socket.getInputStream();

            while (!finished) {
                bytesRead = inputStream.read();

                if (bytesRead == -1) {
                    return i;
                }

                char currentChar = (char) bytesRead;

                if (currentChar == '#') {
                    bytesRead = inputStream.read();

                    if (bytesRead == -1) {
                        return i;
                    }

                    char nextChar = (char) bytesRead;

                    if (nextChar == ')') {
                        data[i] = '\0'; // Null-terminate the string
                        finished = true;
                    } else {
                        data[i] = (byte) currentChar;
                        data[i + 1] = (byte) nextChar;
                        i += 2;
                    }
                } else {
                    data[i] = (byte) currentChar;
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return i;
    }

}
