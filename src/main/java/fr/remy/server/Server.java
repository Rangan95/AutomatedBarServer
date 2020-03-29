package fr.remy.server;

import fr.remy.exception.ServerException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server (int port) throws ServerException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new ServerException("Erreur lors de la création de la socket serveur", e.getMessage());
        }
    }

    public void open () {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        Thread clientThread = new Thread(new ClientProcessor(socket));
                        clientThread.start();
                    } catch (IOException e) {
                        System.out.println("Error when receive a client" + e.getMessage());
                    }
                }
            }
        });

        thread.start();
    }

}
