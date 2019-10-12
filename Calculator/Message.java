package Calculator;

import java.util.ArrayList;

class Message {

    private int status;
    private String text;
    private ArrayList<String> tokens;

    Message(int s, String t) {
        status = s;
        text = t;
    }

    Message(int s, ArrayList<String> t) {
        status = s;
        tokens = t;
    }

    int getStatus() {
        return status;
    }

    String getText() {
        return text;
    }

    ArrayList<String> getTokens() {
        return tokens;
    }
}
