package server;

import server.ClientMessageTreatment.SequentialWaitPumpsMessageTreatment;
import server.ClientMessageTreatment.ThreadPumpsMessageTreatment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Describe the processing for a new connected client
 */
public class ClientProcessor {

    private Socket clientSocket;

    /**
     * The constructor of the class
     *
     * @param clientSocket the client socket open when a new client connect to the server
     */
    public ClientProcessor (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Launch the processing for the client
     */
    public void run () {
        PrintWriter writer = null;
        BufferedInputStream reader = null;

        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            boolean stop = false;

            do {
                String clientMessage = read(reader);
                String toSend;

                String[] clientMessageTab = clientMessage.split(" ");
                String command = clientMessageTab.length > 1 ? clientMessageTab[0].replaceAll("[\n\r]", "").toUpperCase() : clientMessage.replaceAll("[\n\r]", "").toUpperCase();

                switch (command) {
                    case "PUMPS_WITH_THREAD":
                        toSend = new ThreadPumpsMessageTreatment(clientMessage).run();
                        break;

                    case "PUMPS_SEQUENTIAL_WITH_WAIT":
                        toSend = new SequentialWaitPumpsMessageTreatment(clientMessage).run();
                        break;

                    case "STOP":
                        stop = true;
                        toSend = "200 STOP - OK";
                        break;

                    default:
                        toSend = "500 UNKNOWN COMMAND";
                        stop = true;
                        break;
                }

                writer.println(toSend);
            } while (!stop);
        } catch (IOException e) {
            e.printStackTrace();
            writer.println("500 Internal server error : " + e.getMessage());
        } finally {
            try {
                writer.close();
                reader.close();
                clientSocket.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read the message from the client
     *
     * @param reader the bufferedInputStream to read message from the client
     * @return the message from the client
     * @throws IOException when the method read the input from the client
     */
    private String read (BufferedInputStream reader) throws IOException {
        byte[] b = new byte[4096];
        return new String(b, 0, reader.read(b));
    }

}
