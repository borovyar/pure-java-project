package tcp.server.robot;

import tcp.server.Communicator;
import tcp.server.inputvalidation.ValidationType;
import tcp.server.messages.ServerMessages;

import java.io.IOException;

public class Robot {
    public Coordinate coordinate;
    private Integer health;
    public RobotOrientation robotMainOrientation;
    public RobotOrientation robotTemporaryOrientation;
    public RobotOrientation robotSecondOrientation;
    public Communicator communicator;

    public Robot(Communicator communicator) {
        this.coordinate = null;
        this.health = 20;
        this.robotMainOrientation = RobotOrientation.STATELESS;
        this.robotTemporaryOrientation = RobotOrientation.STATELESS;
        this.robotSecondOrientation = RobotOrientation.STATELESS;
        this.communicator = communicator;
    }

    public void findTargetPosition() throws IOException {
        coordinate = RobotMovements.turnLeft(this);
        findTemporary();
        while (!coordinate.atTargetLocation()) {
            if(health == 0)
                throw new IOException();
            determineMainOrientation();
            nextMove();
        }
        pickUp();
    }

    private void pickUp() throws IOException{
        communicator.sendMessage(ServerMessages.SERVER_PICK_UP.getMessage());
        communicator.receiveMessage(ValidationType.SECRET_MESSAGE);
        communicator.sendMessage(ServerMessages.SERVER_LOGOUT.getMessage());
    }

    public void findTemporary() throws IOException {
        var tmpCoordinate = RobotMovements.move(this);

        if(coordinate.equals(tmpCoordinate)){
            health--;
            RobotMovements.turnLeft(this);
            tmpCoordinate = RobotMovements.move(this);
        }

        robotTemporaryOrientation = coordinate.determineTemporaryDirection(tmpCoordinate);
        coordinate = tmpCoordinate;
    }

    private void turnOnOrientation(RobotOrientation correctOrientation) throws IOException {
        while(robotTemporaryOrientation != correctOrientation)
            RobotMovements.turnRight(this);
    }

    private void determineMainOrientation(){
        if(coordinate.mainAxe() == 0) {
            robotSecondOrientation = (coordinate.getY() > 0) ?  RobotOrientation.LOWER_DIRECTION : RobotOrientation.UPPER_DIRECTION;
            robotMainOrientation = (coordinate.getX() > 0) ? RobotOrientation.LEFT_DIRECTION : RobotOrientation.RIGHT_DIRECTION;
        }
        else {
            robotSecondOrientation = (coordinate.getX() > 0) ? RobotOrientation.LEFT_DIRECTION : RobotOrientation.RIGHT_DIRECTION;
            robotMainOrientation = (coordinate.getY() > 0) ? RobotOrientation.LOWER_DIRECTION : RobotOrientation.UPPER_DIRECTION;
        }
    }

    private void nextMove() throws IOException {
        turnOnOrientation(robotMainOrientation);
        var newCoordinate = RobotMovements.move(this);

        if(coordinate.equals(newCoordinate)){
            health--;
            turnOnOrientation(robotSecondOrientation);
            newCoordinate = RobotMovements.move(this);
        }

        coordinate = newCoordinate;
    }
}
