package tcp.server.robot;

public enum RobotOrientation {
    STATELESS{
        @Override
        public RobotOrientation nextDirection(){
            return LEFT_DIRECTION;
        }
    },
    LEFT_DIRECTION {
        @Override
        public RobotOrientation nextDirection() {
            return UPPER_DIRECTION;
        }
    },
    RIGHT_DIRECTION {
        @Override
        public RobotOrientation nextDirection() {
            return LOWER_DIRECTION;
        }
    },
    UPPER_DIRECTION {
        @Override
        public RobotOrientation nextDirection() {
            return RIGHT_DIRECTION;
        }
    },
    LOWER_DIRECTION {
        @Override
        public RobotOrientation nextDirection() {
            return LEFT_DIRECTION;
        }
    };

    public abstract RobotOrientation nextDirection();
}
