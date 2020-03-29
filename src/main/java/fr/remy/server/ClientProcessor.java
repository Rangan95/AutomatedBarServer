package fr.remy.server;

import fr.remy.server.ClientMessageTreatment.SequentialWaitPumpsMessageTreatment;
import fr.remy.server.ClientMessageTreatment.ThreadPumpsMessageTreatment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcessor implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedInputStream reader;

    public ClientProcessor (Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run () {
        while (!socket.isClosed()) {
            try {
                writer = new PrintWriter(socket.getOutputStream());
                reader = new BufferedInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Error when open flow to client : " + e.getMessage());
            }

            String clientMessage = "";

            try {
                clientMessage = read(reader);
            } catch (IOException e) {
                System.out.println("Error when read client message : " + e.getMessage());
            }

            String[] clientMessageTab = clientMessage.split(" ");
            String command = clientMessageTab.length > 1
                    ? clientMessageTab[0].replaceAll("[\n\r]", "").toUpperCase()
                    : clientMessage.replaceAll("[\n\r]", "").toUpperCase();

            String toSend;
            boolean stop = false;

            if ("PUMPS_WITH_THREAD".equals(command)) {
                toSend = new ThreadPumpsMessageTreatment(clientMessage).run();
            } else if ("PUMPS_SEQUENTIAL_WITH_WAIT".equals(command)) {
                toSend = new SequentialWaitPumpsMessageTreatment(clientMessage).run();
            } else if ("STOP".equals(command)) {
                stop = true;
                toSend = "200 STOP - OK";
            } else {
                toSend = "500 UNKNOWN COMMAND";
                stop = true;
            }

            writer.write(toSend);
            writer.flush();

            if(stop){
                writer = null;
                reader = null;

                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Erreur lors de la fermeture de la socket client : " + e.getMessage());
                }

                break;
            }

        }
    }

    private String read (BufferedInputStream reader) throws IOException {
        byte[] byteTab = new byte[4096];
        return new String(byteTab, 0, reader.read(byteTab));
    }

}
