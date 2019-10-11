package Calculator;

import java.util.HashMap;
import java.util.regex.*;


/**
 * Helps in parsing the expression provided to it. Supports the full use of identifiers.
 */
class CalcParser {

    private HashMap<String, String> identifiers = new HashMap<>();
    private String expression;

    /**
     * Checks the received expression and sends ExpressionMessage to the caller accordingly.
     * The following list explains all the status codes.
     * 1. 1 -> close the program
     * 2. 2 -> delete all identifiers
     * 3. 3 -> successful creation of identifiers
     * 4. 4 -> identifier creation failed
     *
     *
     * @param userExpression A string containing a mathematical expression or identifier
     *                       expression.
     *
     * @return A ExpressionMessage object containing the required status and message.
     */
    ExpressionMessage checkExpression(String userExpression) {
        expression = userExpression.replaceAll("\\s+", "");

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
            replaceIdentifiers();
            handleUnary();
        } catch (IdentifierException e) {
            return new ExpressionMessage(4, "Invalid Identifier.");
        }

        return new ExpressionMessage(0, expression);
    }

    /**
     * Store identifiers in the hash map.
     *
     * @param key Identifier that can be used to fetch the value.
     * @param value Value associated with the key.
     */
    void addIdentifier(String key, String value) {
        identifiers.put(key, value);
    }

    /**
     * If the expression string contains "=", this method stores the identifier and the
     * associated value in the hash map.
     *
     * @throws IdentifierException If the identifier doesn't match the identifier regex or
     * the value is not a number.
     */
    private void createIdentifiers() throws IdentifierException {
        String[] identifiersList = expression.split(",");
        String IdentifierRegex = "(?:^[a-zA-Z][$\\w]*)|(?:^\\$[\\w]+)";

        for (String token: identifiersList) {
            String[] tokenSplit = token.split("=");
            String name = tokenSplit[0].trim();
            String value = tokenSplit[1].trim();

            if (!(name.matches(IdentifierRegex) && Evaluator.isNumber(value))) {
                throw new IdentifierException();
            }

            identifiers.put(name, value);
        }
    }

    /**
     * Replaces identifiers with the values in the expression. The method also handles
     * implicit multiplication when are grouped.
     *
     * @throws IdentifierException If the the hash map look up fails.
     */
    private void replaceIdentifiers() throws IdentifierException {
        String IdentifierRegex = "(?:\\$[$\\w]+)|(?:[a-zA-Z]+[$\\w]*)";
        Pattern IdentifierPattern = Pattern.compile(IdentifierRegex);
        Matcher IdentifierMatcher = IdentifierPattern.matcher(expression);
        StringBuilder localExpression = new StringBuilder();

        while (IdentifierMatcher.find()) {
            String preRep = "";
            String repString = identifiers.get(IdentifierMatcher.group());
            int matchIndex = IdentifierMatcher.start();

            if (repString != null) {
                if (matchIndex > 0) {
                    char preChar = expression.charAt(matchIndex - 1);
                    if (Character.isDigit(preChar)) {
                        preRep = "*";
                    }
                }
                IdentifierMatcher.appendReplacement(localExpression, preRep + repString);
            } else {
                throw new IdentifierException();
            }
        }
        IdentifierMatcher.appendTail(localExpression);

        expression = localExpression.toString();
        expression = expression.replaceAll("(\\d)(\\()", "$1*$2");
    }

    /**
     * Handles the unary minus operator.
     */
    private void handleUnary() {
        expression = expression.replaceAll("-", "~");
        expression = expression.replaceAll("(\\d)~(\\d)", "$1-$2");
        // System.out.println("handleUnary2: " + expression);
    }
}