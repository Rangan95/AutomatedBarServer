package server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Main class of the server
 */
public class Server {

    private ServerSocket serverSocket;

    /**
     * Constructor of the Server class
     *
     * @param port the port to listen
     */
    public Server (int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch the server for listen new client
     */
    public void run () {
        while (true) {
            try {
                new ClientProcessor(serverSocket.accept()).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
