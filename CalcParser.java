import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class IdentifierException extends Exception {}


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

    private static final String REG_EX = "(\\$[\\w]+)|([a-zA-Z]+[\\w]*)";
    private static final Pattern REG_PATTERN = Pattern.compile(REG_EX);
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
            try {
                createIdentifiers();
                return new ExpressionMessage(3, "");
            } catch (IdentifierException e) {
                return new ExpressionMessage(4, "Invalid Identifier.");
            }
        }

        try {
            insertIdentifiers();
            handleUnary();
        } catch (IdentifierException e) {
            return new ExpressionMessage(4, "Invalid Identifier.");
        }

        return new ExpressionMessage(0, expression);
    }

    void addIdentifier(String key, String value) {
        identifiers.put(key, value);
    }

    private void createIdentifiers() throws IdentifierException {
        String[] identifiersList = expression.split(",");
        String nameRegex = "(^[a-zA-Z]+[\\w]*)|(^\\$[\\w]+)";

        for (String token: identifiersList) {
            String[] tokenSplit = token.split("=");
            String name = tokenSplit[0].trim();
            String value = tokenSplit[1].trim();

            if (!(name.matches(nameRegex) && Calc.isNumber(value))) {
                throw new IdentifierException();
            }

            identifiers.put(name, value);
        }
    }

    private void insertIdentifiers() throws IdentifierException {
        StringBuilder localExpression = new StringBuilder();
        Matcher regMatcher = REG_PATTERN.matcher(expression);

        while (regMatcher.find()) {
            String prefixRep = "";
            String repString = identifiers.get(regMatcher.group());
            int matchIndex = regMatcher.start();

            if (repString != null) {
                if (matchIndex > 0) {
                    char preChar = expression.charAt(matchIndex - 1);
                    if (Character.isDigit(preChar)) { prefixRep = "*"; }
                }
                regMatcher.appendReplacement(localExpression, prefixRep + repString);
            } else {
                throw new IdentifierException();
            }
        }
        regMatcher.appendTail(localExpression);

        expression = localExpression.toString();
    }

    private void handleUnary() {
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replaceAll("-", "~");
        expression = expression.replaceAll("(\\d)~(\\d)", "$1-$2");
        // System.out.println("handleUnary2: " + expression);
    }
}