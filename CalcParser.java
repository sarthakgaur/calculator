import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MissingIdentifier extends Exception {}


class ExpressionMessage {

    private int status;
    private String expression;

    ExpressionMessage (int status, String expression) {
        this.status = status;
        this.expression = expression;
    }

    int getStatus() {
        return status;
    }

    String getExpression() {
        return expression;
    }
}


class CalcParser {

    private HashMap<String, String> identifiers = new HashMap<>();
    private String expression;

    ExpressionMessage checkExpression(String userExpression) {
        expression = userExpression;

        if (userExpression.equals("q")) {
            return new ExpressionMessage(1, "");
        } else if (userExpression.equals("n")) {
            identifiers = new HashMap<String, String>();
            return new ExpressionMessage(2, "");
        } else if (userExpression.contains("=")) {
            createIdentifiers();
            return new ExpressionMessage(3, "");
        }

        try {
            insertIdentifiers();
        } catch (MissingIdentifier e) {
            System.out.println("Identifier used but not declared.");
            return new ExpressionMessage(4, "");
        }

        return new ExpressionMessage(0, expression);
    }

    void addIdentifier(String key, String value) {
        identifiers.put(key, value);
    }

    private void createIdentifiers() {
        String[] identifiersList = expression.split(",");

        for (String token: identifiersList) {
            String[] tokenSplit = token.split("=");
            identifiers.put(tokenSplit[0], tokenSplit[1]);
        }
    }

    private void insertIdentifiers() throws MissingIdentifier {
        StringBuilder localExpression = new StringBuilder();
//        System.out.println("original expression: " + expression);

        String rx = "(\\$[0-9]+)|([a-z]+)";
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(expression);

        while (m.find()) {
            String repString = identifiers.get(m.group());
            if (repString != null) {
                m.appendReplacement(localExpression, repString);
            } else {
                throw new MissingIdentifier();
            }
        }

        m.appendTail(localExpression);
        expression = localExpression.toString();
//        System.out.println("new expression: " + expression);
    }
}