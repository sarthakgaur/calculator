package Calculator;

import java.util.List;

/**
 * Used by the Checker class to send messages to the Engine class. The message class
 * stores status, message, and tokens. Every message needs a status, message and tokens are
 * optional.
 */
class Message {

    private int status;
    private String message;
    private List<String> tokens;

    Message(int s) {
        status = s;
    }

    Message(int s, String m) {
        status = s;
        message = m;
    }

    Message(int s, List<String> t) {
        status = s;
        tokens = t;
    }

    int getStatus() {
        return status;
    }

    String getMessage() {
        return message;
    }

    List<String> getTokens() {
        return tokens;
    }
}
