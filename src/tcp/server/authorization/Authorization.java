package tcp.server.authorization;

import tcp.server.Communicator;
import tcp.server.Main;
import tcp.server.messages.ServerMessages;
import tcp.server.inputvalidation.ValidationType;
import tcp.server.exceptions.KeyOutOfRangeException;
import tcp.server.exceptions.LoginFailedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Authorization {
    private final Map<Integer, KeyPair> keyMap = new HashMap<>(
            Map.of(0, new KeyPair(23019, 32037),
                    1, new KeyPair(32037, 29295),
                    2, new KeyPair(18789, 13603),
                    3, new KeyPair(16443, 29533),
                    4, new KeyPair(18189, 21952)));

    private Integer usernameHash, keyId, clientHash;
    private String username;
    private final Communicator communicator;

    public Authorization(Communicator communicator) {
        this.username = "";
        this.usernameHash = -1;
        this.keyId = -1;
        this.clientHash = -1;
        this.communicator = communicator;
    }

    public void authorize() throws IOException {
        communicator.sendMessage(parseUsername(communicator.receiveMessage(ValidationType.USERNAME)));
        communicator.sendMessage(parseKey(communicator.receiveMessage(ValidationType.KEY_ID)));
        communicator.sendMessage(parseClientHash(communicator.receiveMessage(ValidationType.CONFIRMATION)));
    }

    private String parseClientHash(String clientMessage) throws LoginFailedException {
        clientHash = Integer.parseInt(clientMessage);

        if((usernameHash + keyMap.get(keyId).clientKey) % 65536 != clientHash)
            throw new LoginFailedException();

        return ServerMessages.SERVER_OK.getMessage();
    }

    private String parseKey(String clientMessage) throws KeyOutOfRangeException {
        keyId = Integer.parseInt(clientMessage);

        if( keyId < 0 || keyId > 4 )
            throw new KeyOutOfRangeException();

        return ((usernameHash + keyMap.get(keyId).serverKey) % 65536) + Main.endCharacters;
    }

    public String parseUsername(String clientMessage){
        username = clientMessage;
        usernameHash = username.chars().sum();
        usernameHash = (usernameHash * 1000) % 65536;
        return ServerMessages.SERVER_KEY_REQUEST.getMessage();
    }
}
