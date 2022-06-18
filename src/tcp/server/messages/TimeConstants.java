package tcp.server.messages;

public enum TimeConstants {
    TIMEOUT(1000),
    TIMEOUT_RECHARGING(5000);

    private final Integer value;

    public Integer getValue() {
        return value;
    }

    TimeConstants(Integer value) {
        this.value = value;
    }
}
