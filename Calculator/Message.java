package Calculator;

import java.util.ArrayList;

class Message {

    private int status;
    private String message;
    private ArrayList<String> tokens;

    Message(int s) {
        status = s;
    }

    Message(int s, String m) {
        status = s;
        message = m;
    }

    Message(int s, ArrayList<String> t) {
        status = s;
        tokens = t;
    }

    int getStatus() {
        return status;
    }

    String getMessage() {
        return message;
    }

    ArrayList<String> getTokens() {
        return tokens;
    }
}
