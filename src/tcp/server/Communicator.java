package tcp.server;

import tcp.server.authorization.Authorization;
import tcp.server.messages.ClientMessages;
import tcp.server.messages.ServerMessages;
import tcp.server.messages.TimeConstants;
import tcp.server.inputvalidation.ValidationType;
import tcp.server.exceptions.KeyOutOfRangeException;
import tcp.server.exceptions.LogicalErrorException;
import tcp.server.exceptions.LoginFailedException;
import tcp.server.exceptions.SyntaxErrorException;
import tcp.server.inputvalidation.InputValidator;
import tcp.server.robot.Robot;

import java.io.*;
import java.net.Socket;

public class Communicator implements Runnable{
    private final BufferedReader inputStream;
    private final DataOutputStream outputStream;
    private final Socket clientSocket;
    private final StringBuilder messageBuilder;
    private final Authorization authorization;
    private final Robot robot;

    public Communicator(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.messageBuilder = new StringBuilder();
        this.authorization = new Authorization(this);
        this.robot = new Robot(this);
    }

    @Override
    public void run() {
        try {
            authorization.authorize();
            robot.findTargetPosition();
        } catch (SyntaxErrorException e) {
            try {sendMessage(ServerMessages.SERVER_SYNTAX_ERROR.getMessage()); } catch (IOException ignored){}
        } catch (LogicalErrorException e){
            try {sendMessage(ServerMessages.SERVER_LOGIC_ERROR.getMessage());} catch (IOException ignored) {}
        } catch (LoginFailedException e){
            try {sendMessage(ServerMessages.SERVER_LOGIN_FAILED.getMessage()); } catch (IOException ignored) {}
        } catch (KeyOutOfRangeException e){
            try {sendMessage(ServerMessages.SERVER_KEY_OUT_RANGE.getMessage()); } catch (IOException ignored) {}
        } catch (Exception ignored) {}
         finally { try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    public String receiveMessage(ValidationType validationType) throws IOException{
        var clientMessage = parseMessage(validationType);
        clientMessage = validCharge(clientMessage);

        InputValidator.validateMessage(clientMessage, validationType, false);

        return clientMessage;
    }

    public String parseMessage (ValidationType validationType) throws IOException {
        do {
            int firstChar  = inputStream.read();

            if(!messageBuilder.isEmpty())
                InputValidator.validateMessage(messageBuilder.toString(), validationType, true);

            if(firstChar != 7)
                messageBuilder.append((char) firstChar);
            else {
                int secondChar = inputStream.read();
                if (secondChar == 8)
                    break;
                messageBuilder.append((char) firstChar);
                messageBuilder.append((char) secondChar);
            }

            InputValidator.validateMessage(messageBuilder.toString(), validationType, true);
        } while (true);
        var message = messageBuilder.toString();
        messageBuilder.delete(0, messageBuilder.length() + 1);
        return message;
    }

    public void sendMessage(String serverMessage) throws IOException {
        serverMessage.chars().forEach((c) -> {
            try {
                outputStream.write(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        outputStream.flush();
    }

    public String validCharge(String clientMessage) throws IOException {
        if(clientMessage.equals(ClientMessages.CLIENT_FULL_POWER.getMessage()))
            throw new LogicalErrorException();

        if(!clientMessage.equals(ClientMessages.CLIENT_RECHARGING.getMessage()))
            return clientMessage;

        clientSocket.setSoTimeout(TimeConstants.TIMEOUT_RECHARGING.getValue());

        clientMessage = parseMessage(ValidationType.NO_VALIDATION);

        if(!clientMessage.equals(ClientMessages.CLIENT_FULL_POWER.getMessage()))
            throw new LogicalErrorException();

        clientSocket.setSoTimeout(TimeConstants.TIMEOUT.getValue());
        return parseMessage(ValidationType.NO_VALIDATION);
    }
}
