package tcp.server.authorization;

public class KeyPair {
    public Integer serverKey;
    public Integer clientKey;

    public KeyPair(Integer serverKey, Integer clientKey) {
        this.serverKey = serverKey;
        this.clientKey = clientKey;
    }
}
