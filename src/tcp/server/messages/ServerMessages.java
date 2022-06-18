package tcp.server.messages;

import tcp.server.Main;

public enum ServerMessages {
    SERVER_MOVE("102 MOVE" + Main.endCharacters),
    SERVER_TURN_LEFT("103 TURN LEFT"  + Main.endCharacters),
    SERVER_TURN_RIGHT("104 TURN RIGHT"  + Main.endCharacters),
    SERVER_PICK_UP("105 GET MESSAGE"  + Main.endCharacters),
    SERVER_LOGOUT("106 LOGOUT"  + Main.endCharacters),
    SERVER_KEY_REQUEST("107 KEY REQUEST" + Main.endCharacters),
    SERVER_OK("200 OK"  + Main.endCharacters),
    SERVER_LOGIN_FAILED("300 LOGIN FAILED"  + Main.endCharacters),
    SERVER_SYNTAX_ERROR ("301 SYNTAX ERROR"  + Main.endCharacters),
    SERVER_LOGIC_ERROR ("302 LOGIC ERROR"  + Main.endCharacters),
    SERVER_KEY_OUT_RANGE ("303 KEY OUT OF RANGE"  + Main.endCharacters);

    private final String message;

    ServerMessages(String message) {
        this.message = message;
    }

    public String getMessage (){
        return this.message;
    }
}
