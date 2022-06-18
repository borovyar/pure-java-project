package tcp.server.robot;

import tcp.server.exceptions.SyntaxErrorException;

import java.io.IOException;
import java.util.regex.Pattern;

public record Coordinate(Integer X, Integer Y) {

    public RobotOrientation determineTemporaryDirection(Coordinate other) {
        if (X > other.getX())
            return RobotOrientation.LEFT_DIRECTION;
        if (X < other.getX())
            return RobotOrientation.RIGHT_DIRECTION;
        if (Y > other.getY())
            return RobotOrientation.LOWER_DIRECTION;

        return RobotOrientation.UPPER_DIRECTION;
    }

    public Integer mainAxe() {
        return (Math.abs(X) >= Math.abs(Y)) ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (getX() != null ? !getX().equals(that.getX()) : that.getX() != null) return false;
        return getY() != null ? getY().equals(that.getY()) : that.getY() == null;
    }

    public Integer getX() {
        return X;
    }

    public Integer getY() {
        return Y;
    }

    public Boolean atTargetLocation() {
        return X == 0 && Y == 0;
    }

    public static Coordinate createCoordinateFromMessage(String clientMessage) throws IOException {
        var pattern = Pattern.compile("OK\\s(-?\\d+)\\s(-?\\d+)$");
        var matcher = pattern.matcher(clientMessage);

        if (!matcher.matches())
            throw new SyntaxErrorException();

        return new Coordinate(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
    }
}
