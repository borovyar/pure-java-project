package tcp.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TcpServer {
}

//public class Main {
//    public static String endCharacters = String.valueOf((char)7) + (char)8;
//
//    public static void main(String[] args) {
//        try(var serverSocket = new ServerSocket(6666)) {
//            while (true) {
//                var clientSocket = serverSocket.accept();
//                clientSocket.setSoTimeout(TimeConstants.TIMEOUT.getValue());
//
//                var communicator = new Communicator(clientSocket);
//
//                new Thread(communicator).start();
//            }
//        } catch (Exception ignored){}
//    }
//}
//
//public class Communicator implements Runnable{
//    private final BufferedReader inputStream;
//    private final DataOutputStream outputStream;
//    private final Socket clientSocket;
//    private final StringBuilder messageBuilder;
//    private final Authorization authorization;
//    private final Robot robot;
//
//    public Communicator(Socket clientSocket) throws IOException {
//        this.clientSocket = clientSocket;
//        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
//        this.messageBuilder = new StringBuilder();
//        this.authorization = new Authorization(this);
//        this.robot = new Robot(this);
//    }
//
//    @Override
//    public void run() {
//        try {
//            authorization.authorize();
//            robot.findTargetPosition();
//        } catch (SyntaxErrorException e) {
//            try {sendMessage(ServerMessages.SERVER_SYNTAX_ERROR.getMessage()); } catch (IOException ignored){}
//        } catch (LogicalErrorException e){
//            try {sendMessage(ServerMessages.SERVER_LOGIC_ERROR.getMessage());} catch (IOException ignored) {}
//        } catch (LoginFailedException e){
//            try {sendMessage(ServerMessages.SERVER_LOGIN_FAILED.getMessage()); } catch (IOException ignored) {}
//        } catch (KeyOutOfRangeException e){
//            try {sendMessage(ServerMessages.SERVER_KEY_OUT_RANGE.getMessage()); } catch (IOException ignored) {}
//        } catch (Exception ignored) {}
//        finally { try {
//            inputStream.close();
//            outputStream.close();
//            clientSocket.close();
//        } catch (IOException ignored) {}
//        }
//    }
//
//    public String receiveMessage(ValidationType validationType) throws IOException{
//        var clientMessage = parseMessage(validationType);
//        clientMessage = validCharge(clientMessage);
//
//        InputValidator.validateMessage(clientMessage, validationType, false);
//
//        return clientMessage;
//    }
//
//    public String parseMessage (ValidationType validationType) throws IOException {
//        do {
//            int firstChar  = inputStream.read();
//
//            if(!messageBuilder.isEmpty())
//                InputValidator.validateMessage(messageBuilder.toString(), validationType, true);
//
//            if(firstChar != 7)
//                messageBuilder.append((char) firstChar);
//            else {
//                int secondChar = inputStream.read();
//                if (secondChar == 8)
//                    break;
//                messageBuilder.append((char) firstChar);
//                messageBuilder.append((char) secondChar);
//            }
//
//            InputValidator.validateMessage(messageBuilder.toString(), validationType, true);
//        } while (true);
//        var message = messageBuilder.toString();
//        messageBuilder.delete(0, messageBuilder.length() + 1);
//        return message;
//    }
//
//    public void sendMessage(String serverMessage) throws IOException {
//        serverMessage.chars().forEach((c) -> {
//            try {
//                outputStream.write(c);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        outputStream.flush();
//    }
//
//    public String validCharge(String clientMessage) throws IOException {
//        if(clientMessage.equals(ClientMessages.CLIENT_FULL_POWER.getMessage()))
//            throw new LogicalErrorException();
//
//        if(!clientMessage.equals(ClientMessages.CLIENT_RECHARGING.getMessage()))
//            return clientMessage;
//
//        clientSocket.setSoTimeout(TimeConstants.TIMEOUT_RECHARGING.getValue());
//
//        clientMessage = parseMessage(ValidationType.NO_VALIDATION);
//
//        if(!clientMessage.equals(ClientMessages.CLIENT_FULL_POWER.getMessage()))
//            throw new LogicalErrorException();
//
//        clientSocket.setSoTimeout(TimeConstants.TIMEOUT.getValue());
//        return parseMessage(ValidationType.NO_VALIDATION);
//    }
//}
//
//public enum RobotOrientation {
//    STATELESS{
//        @Override
//        public tcp.server.robot.RobotOrientation nextDirection(){
//            return LEFT_DIRECTION;
//        }
//    },
//    LEFT_DIRECTION {
//        @Override
//        public tcp.server.robot.RobotOrientation nextDirection() {
//            return UPPER_DIRECTION;
//        }
//    },
//    RIGHT_DIRECTION {
//        @Override
//        public tcp.server.robot.RobotOrientation nextDirection() {
//            return LOWER_DIRECTION;
//        }
//    },
//    UPPER_DIRECTION {
//        @Override
//        public tcp.server.robot.RobotOrientation nextDirection() {
//            return RIGHT_DIRECTION;
//        }
//    },
//    LOWER_DIRECTION {
//        @Override
//        public tcp.server.robot.RobotOrientation nextDirection() {
//            return LEFT_DIRECTION;
//        }
//    };
//
//    public abstract tcp.server.robot.RobotOrientation nextDirection();
//}
//
//public class RobotMovements {
//
//    public static Coordinate turnLeft(Robot robot) throws IOException {
//        robot.communicator.sendMessage(ServerMessages.SERVER_TURN_LEFT.getMessage());
//        return Coordinate.createCoordinateFromMessage(robot.communicator.receiveMessage(ValidationType.OK) );
//    }
//
//    public static void turnRight(Robot robot) throws IOException {
//        robot.communicator.sendMessage(ServerMessages.SERVER_TURN_RIGHT.getMessage());
//
//        robot.robotTemporaryOrientation = robot.robotTemporaryOrientation.nextDirection();
//
//        Coordinate.createCoordinateFromMessage(robot.communicator.receiveMessage(ValidationType.OK) );
//    }
//
//    public static Coordinate move(Robot robot) throws IOException {
//        robot.communicator.sendMessage(ServerMessages.SERVER_MOVE.getMessage());
//        return Coordinate.createCoordinateFromMessage(robot.communicator.receiveMessage(ValidationType.OK) );
//    }
//}
//
//public class Robot {
//    public Coordinate coordinate;
//    private Integer health;
//    public tcp.server.robot.RobotOrientation robotMainOrientation;
//    public tcp.server.robot.RobotOrientation robotTemporaryOrientation;
//    public tcp.server.robot.RobotOrientation robotSecondOrientation;
//    public Communicator communicator;
//
//    public Robot(Communicator communicator) {
//        this.coordinate = null;
//        this.health = 20;
//        this.robotMainOrientation = tcp.server.robot.RobotOrientation.STATELESS;
//        this.robotTemporaryOrientation = tcp.server.robot.RobotOrientation.STATELESS;
//        this.robotSecondOrientation = tcp.server.robot.RobotOrientation.STATELESS;
//        this.communicator = communicator;
//    }
//
//    public void findTargetPosition() throws IOException {
//        coordinate = tcp.server.robot.RobotMovements.turnLeft(this);
//        findTemporary();
//        while (!coordinate.atTargetLocation()) {
//            if(health == 0)
//                throw new IOException();
//            determineMainOrientation();
//            nextMove();
//        }
//        pickUp();
//    }
//
//    private void pickUp() throws IOException{
//        communicator.sendMessage(ServerMessages.SERVER_PICK_UP.getMessage());
//        communicator.receiveMessage(ValidationType.SECRET_MESSAGE);
//        communicator.sendMessage(ServerMessages.SERVER_LOGOUT.getMessage());
//    }
//
//    public void findTemporary() throws IOException {
//        var tmpCoordinate = tcp.server.robot.RobotMovements.move(this);
//
//        if(coordinate.equals(tmpCoordinate)){
//            health--;
//            tcp.server.robot.RobotMovements.turnLeft(this);
//            tmpCoordinate = tcp.server.robot.RobotMovements.move(this);
//        }
//
//        robotTemporaryOrientation = coordinate.determineTemporaryDirection(tmpCoordinate);
//        coordinate = tmpCoordinate;
//    }
//
//    private void turnOnOrientation(tcp.server.robot.RobotOrientation correctOrientation) throws IOException {
//        while(robotTemporaryOrientation != correctOrientation)
//            tcp.server.robot.RobotMovements.turnRight(this);
//    }
//
//    private void determineMainOrientation(){
//        if(coordinate.mainAxe() == 0) {
//            robotSecondOrientation = (coordinate.getY() > 0) ?  tcp.server.robot.RobotOrientation.LOWER_DIRECTION : tcp.server.robot.RobotOrientation.UPPER_DIRECTION;
//            robotMainOrientation = (coordinate.getX() > 0) ? tcp.server.robot.RobotOrientation.LEFT_DIRECTION : tcp.server.robot.RobotOrientation.RIGHT_DIRECTION;
//        }
//        else {
//            robotSecondOrientation = (coordinate.getX() > 0) ? tcp.server.robot.RobotOrientation.LEFT_DIRECTION : tcp.server.robot.RobotOrientation.RIGHT_DIRECTION;
//            robotMainOrientation = (coordinate.getY() > 0) ? tcp.server.robot.RobotOrientation.LOWER_DIRECTION : tcp.server.robot.RobotOrientation.UPPER_DIRECTION;
//        }
//    }
//
//    private void nextMove() throws IOException {
//        turnOnOrientation(robotMainOrientation);
//        var newCoordinate = tcp.server.robot.RobotMovements.move(this);
//
//        if(coordinate.equals(newCoordinate)){
//            health--;
//            turnOnOrientation(robotSecondOrientation);
//            newCoordinate = tcp.server.robot.RobotMovements.move(this);
//        }
//
//        coordinate = newCoordinate;
//    }
//}
//
//public record Coordinate(Integer X, Integer Y) {
//
//    public tcp.server.robot.RobotOrientation determineTemporaryDirection(tcp.server.robot.Coordinate other) {
//        if (X > other.getX())
//            return tcp.server.robot.RobotOrientation.LEFT_DIRECTION;
//        if (X < other.getX())
//            return tcp.server.robot.RobotOrientation.RIGHT_DIRECTION;
//        if (Y > other.getY())
//            return tcp.server.robot.RobotOrientation.LOWER_DIRECTION;
//
//        return tcp.server.robot.RobotOrientation.UPPER_DIRECTION;
//    }
//
//    public Integer mainAxe() {
//        return (Math.abs(X) >= Math.abs(Y)) ? 0 : 1;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        tcp.server.robot.Coordinate that = (tcp.server.robot.Coordinate) o;
//
//        if (getX() != null ? !getX().equals(that.getX()) : that.getX() != null) return false;
//        return getY() != null ? getY().equals(that.getY()) : that.getY() == null;
//    }
//
//    public Integer getX() {
//        return X;
//    }
//
//    public Integer getY() {
//        return Y;
//    }
//
//    public Boolean atTargetLocation() {
//        return X == 0 && Y == 0;
//    }
//
//    public static tcp.server.robot.Coordinate createCoordinateFromMessage(String clientMessage) throws IOException {
//        var pattern = Pattern.compile("OK\\s(-?\\d+)\\s(-?\\d+)$");
//        var matcher = pattern.matcher(clientMessage);
//
//        if (!matcher.matches())
//            throw new SyntaxErrorException();
//
//        return new tcp.server.robot.Coordinate(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
//    }
//}
//
//public enum TimeConstants {
//    TIMEOUT(1000),
//    TIMEOUT_RECHARGING(5000);
//
//    private final Integer value;
//
//    public Integer getValue() {
//        return value;
//    }
//
//    TimeConstants(Integer value) {
//        this.value = value;
//    }
//}
//
//public enum ServerMessages {
//    SERVER_MOVE("102 MOVE" + Main.endCharacters),
//    SERVER_TURN_LEFT("103 TURN LEFT"  + Main.endCharacters),
//    SERVER_TURN_RIGHT("104 TURN RIGHT"  + Main.endCharacters),
//    SERVER_PICK_UP("105 GET MESSAGE"  + Main.endCharacters),
//    SERVER_LOGOUT("106 LOGOUT"  + Main.endCharacters),
//    SERVER_KEY_REQUEST("107 KEY REQUEST" + Main.endCharacters),
//    SERVER_OK("200 OK"  + Main.endCharacters),
//    SERVER_LOGIN_FAILED("300 LOGIN FAILED"  + Main.endCharacters),
//    SERVER_SYNTAX_ERROR ("301 SYNTAX ERROR"  + Main.endCharacters),
//    SERVER_LOGIC_ERROR ("302 LOGIC ERROR"  + Main.endCharacters),
//    SERVER_KEY_OUT_RANGE ("303 KEY OUT OF RANGE"  + Main.endCharacters);
//
//    private final String message;
//
//    ServerMessages(String message) {
//        this.message = message;
//    }
//
//    public String getMessage (){
//        return this.message;
//    }
//}
//
//public enum ClientMessages {
//    CLIENT_RECHARGING("RECHARGING"),
//    CLIENT_FULL_POWER("FULL POWER");
//
//    private final String message;
//
//    ClientMessages(String message) {
//        this.message = message;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//}
//
//public enum ValidationType {
//    USERNAME,
//    KEY_ID,
//    CONFIRMATION,
//    OK,
//    RECHARGE,
//    FULL_POWER,
//    SECRET_MESSAGE,
//    NO_VALIDATION
//}
//
//public class InputValidator {
//    public static void validateMessage(String clientMessage, tcp.server.inputvalidation.ValidationType validationType,
//                                       Boolean checkPartOfMessage) throws SyntaxErrorException {
//
//        if (checkPartOfMessage && (tcp.server.messages.ClientMessages.CLIENT_RECHARGING.getMessage().startsWith(clientMessage) ||
//                tcp.server.messages.ClientMessages.CLIENT_FULL_POWER.getMessage().startsWith(clientMessage)))
//            return;
//
//        switch (validationType){
//            case USERNAME -> {
//                if (clientMessage.length() > 18)
//                    throw new SyntaxErrorException();
//            }
//            case KEY_ID -> {
//                if(clientMessage.length() > 3 || !clientMessage.matches("\\d{1,3}"))
//                    throw new SyntaxErrorException();
//            }
//            case CONFIRMATION -> {
//                if(clientMessage.length() > 5 || !clientMessage.matches("\\d{1,5}"))
//                    throw new SyntaxErrorException();
//            }
//            case OK -> {
//                if(clientMessage.length() > 10)
//                    throw new SyntaxErrorException();
//            }
//            case SECRET_MESSAGE -> {
//                if(clientMessage.length() > 98)
//                    throw new SyntaxErrorException();
//            }
//            case NO_VALIDATION -> {}
//        }
//    }
//}
//public class SyntaxErrorException extends IOException {
//}
//public class LoginFailedException extends IOException {
//}
//public class LogicalErrorException extends IOException {
//}
//public class KeyOutOfRangeException extends IOException {
//}
//public class KeyPair {
//    public Integer serverKey;
//    public Integer clientKey;
//
//    public KeyPair(Integer serverKey, Integer clientKey) {
//        this.serverKey = serverKey;
//        this.clientKey = clientKey;
//    }
//}
//public class Authorization {
//    private final Map<Integer, tcp.server.authorization.KeyPair> keyMap = new HashMap<>(
//            Map.of(0, new tcp.server.authorization.KeyPair(23019, 32037),
//                    1, new tcp.server.authorization.KeyPair(32037, 29295),
//                    2, new tcp.server.authorization.KeyPair(18789, 13603),
//                    3, new tcp.server.authorization.KeyPair(16443, 29533),
//                    4, new tcp.server.authorization.KeyPair(18189, 21952)));
//
//    private Integer usernameHash, keyId, clientHash;
//    private String username;
//    private final Communicator communicator;
//
//    public Authorization(Communicator communicator) {
//        this.username = "";
//        this.usernameHash = -1;
//        this.keyId = -1;
//        this.clientHash = -1;
//        this.communicator = communicator;
//    }
//
//    public void authorize() throws IOException {
//        communicator.sendMessage(parseUsername(communicator.receiveMessage(tcp.server.inputvalidation.ValidationType.USERNAME)));
//        communicator.sendMessage(parseKey(communicator.receiveMessage(tcp.server.inputvalidation.ValidationType.KEY_ID)));
//        communicator.sendMessage(parseClientHash(communicator.receiveMessage(tcp.server.inputvalidation.ValidationType.CONFIRMATION)));
//    }
//
//    private String parseClientHash(String clientMessage) throws tcp.server.exceptions.LoginFailedException {
//        clientHash = Integer.parseInt(clientMessage);
//
//        if((usernameHash + keyMap.get(keyId).clientKey) % 65536 != clientHash)
//            throw new tcp.server.exceptions.LoginFailedException();
//
//        return tcp.server.messages.ServerMessages.SERVER_OK.getMessage();
//    }
//
//    private String parseKey(String clientMessage) throws tcp.server.exceptions.KeyOutOfRangeException {
//        keyId = Integer.parseInt(clientMessage);
//
//        if( keyId < 0 || keyId > 4 )
//            throw new tcp.server.exceptions.KeyOutOfRangeException();
//
//        return ((usernameHash + keyMap.get(keyId).serverKey) % 65536) + Main.endCharacters;
//    }
//
//    public String parseUsername(String clientMessage){
//        username = clientMessage;
//        usernameHash = username.chars().sum();
//        usernameHash = (usernameHash * 1000) % 65536;
//        return tcp.server.messages.ServerMessages.SERVER_KEY_REQUEST.getMessage();
//    }
//}