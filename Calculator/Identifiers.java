package Calculator;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifiers {

    private HashMap<String, String> identifiers = new HashMap<>();

    /**
     * Store identifiers in the hash map.
     *
     * @param key Identifier that can be used to fetch the value.
     * @param value Value associated with the key.
     */
    void add(String key, String value) {
        identifiers.put(key, value);
    }

    void clear() {
        identifiers = new HashMap<String, String>();
    }

    /**
     * If the expression string contains "=", this method stores the identifier and the
     * associated value in the hash map.
     *
     * @throws IdentifierException If the identifier doesn't match the identifier regex or
     * the value is not a number.
     */
    void create(String expression) throws IdentifierException {
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
     * implicit multiplication.
     *
     * @throws IdentifierException If the the hash map look up fails.
     */
    String replace(String expression) throws IdentifierException {
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
        return expression;
    }
}
