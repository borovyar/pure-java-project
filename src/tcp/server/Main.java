package tcp.server;

import tcp.server.messages.TimeConstants;
import java.net.ServerSocket;

public class Main {
    public static String endCharacters = String.valueOf((char)7) + (char)8;

    public static void main(String[] args) {
        try(var serverSocket = new ServerSocket(6666)) {
            while (true) {
                var clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(TimeConstants.TIMEOUT.getValue());

                var communicator = new Communicator(clientSocket);

                new Thread(communicator).start();
            }
        } catch (Exception ignored){}
    }
}
