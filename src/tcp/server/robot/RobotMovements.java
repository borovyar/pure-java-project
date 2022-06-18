package tcp.server.robot;

import tcp.server.inputvalidation.ValidationType;
import tcp.server.messages.ServerMessages;

import java.io.IOException;

public class RobotMovements {

    public static Coordinate turnLeft(Robot robot) throws IOException {
        robot.communicator.sendMessage(ServerMessages.SERVER_TURN_LEFT.getMessage());
        return Coordinate.createCoordinateFromMessage(robot.communicator.receiveMessage(ValidationType.OK) );
    }

    public static void turnRight(Robot robot) throws IOException {
        robot.communicator.sendMessage(ServerMessages.SERVER_TURN_RIGHT.getMessage());

        robot.robotTemporaryOrientation = robot.robotTemporaryOrientation.nextDirection();

        Coordinate.createCoordinateFromMessage(robot.communicator.receiveMessage(ValidationType.OK) );
    }

    public static Coordinate move(Robot robot) throws IOException {
        robot.communicator.sendMessage(ServerMessages.SERVER_MOVE.getMessage());
        return Coordinate.createCoordinateFromMessage(robot.communicator.receiveMessage(ValidationType.OK) );
    }
}
