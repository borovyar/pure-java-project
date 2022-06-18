package tcp.server.messages;

public enum ClientMessages {
    CLIENT_RECHARGING("RECHARGING"),
    CLIENT_FULL_POWER("FULL POWER");

    private final String message;

    ClientMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
