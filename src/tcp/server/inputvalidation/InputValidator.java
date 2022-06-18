package tcp.server.inputvalidation;

import tcp.server.messages.ClientMessages;
import tcp.server.exceptions.SyntaxErrorException;



public class InputValidator {
    public static void validateMessage(String clientMessage, ValidationType validationType,
                                       Boolean checkPartOfMessage) throws SyntaxErrorException {

        if (checkPartOfMessage && (ClientMessages.CLIENT_RECHARGING.getMessage().startsWith(clientMessage) ||
                ClientMessages.CLIENT_FULL_POWER.getMessage().startsWith(clientMessage)))
            return;

        switch (validationType){
            case USERNAME -> {
                if (clientMessage.length() > 18)
                    throw new SyntaxErrorException();
            }
            case KEY_ID -> {
                if(clientMessage.length() > 3 || !clientMessage.matches("\\d{1,3}"))
                    throw new SyntaxErrorException();
            }
            case CONFIRMATION -> {
                if(clientMessage.length() > 5 || !clientMessage.matches("\\d{1,5}"))
                    throw new SyntaxErrorException();
            }
            case OK -> {
                if(clientMessage.length() > 10)
                    throw new SyntaxErrorException();
            }
            case SECRET_MESSAGE -> {
                if(clientMessage.length() > 98)
                    throw new SyntaxErrorException();
            }
            case NO_VALIDATION -> {}
        }
    }
}
