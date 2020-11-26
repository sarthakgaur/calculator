package Calculator;

import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

/**
 * All the identifiers used in the expression are stored by an object of this class.
 * This class also provides methods like add(), clear(), create() to modify the identifiers
 * in the session.
 */
class Identifiers {

    private HashMap<String, String> identifiers = new HashMap<>();

    /**
     * Store identifiers in the hash map.
     * @param key Identifier that can be used to fetch the value.
     * @param value Value associated with the key.
     */
    void add(String key, String value) {
        identifiers.put(key, value);
    }

    /**
     * Creates a new hash map for identifiers.
     */
    void clear() {
        identifiers = new HashMap<String, String>();
    }

    /**
     * If the expression string contains "=", this method stores the identifier and the
     * associated value in the hash map.
     * @throws IdentifierException If the identifier doesn't match the identifier regex or
     * the value is not a number.
     */
    void create(String expression) throws IdentifierException, InvalidNumberException {
        String[] identifiersList = expression.split(",");
        String IdentifierRegex = "(?:^[a-zA-Z][$\\w]*)|(?:^\\$[\\w]+)";

        for (String token: identifiersList) {
            String[] tokenSplit = token.split("=");
            String key = tokenSplit[0].trim();
            String value = tokenSplit[1].trim();

            if (!key.matches(IdentifierRegex)) {
                throw new IdentifierException(key);
            } else if (!Evaluator.isNumber(value)) {
                throw new InvalidNumberException(value);
            }

            identifiers.put(key, value);
        }
    }

    /**
     * Replaces identifiers with the values in the expression. The method also handles
     * implicit multiplication.
     * @throws IdentifierException If the the hash map look up fails.
     */
    String replace(String expression) throws IdentifierException {
        String IdentifierRegex = "(?:\\$[$\\w]+)|(?:[a-zA-Z]+[$\\w]*)";
        Pattern IdentifierPattern = Pattern.compile(IdentifierRegex);
        Matcher IdentifierMatcher = IdentifierPattern.matcher(expression);
        StringBuilder localExpression = new StringBuilder();

        while (IdentifierMatcher.find()) {
            String key = IdentifierMatcher.group();
            String value = identifiers.get(key);
            int matchIndex = IdentifierMatcher.start();

            if (value != null) {
                String prefix = "";
                if (matchIndex > 0) {
                    char preChar = expression.charAt(matchIndex - 1);
                    if (Character.isDigit(preChar)) {
                        prefix = "*";
                    }
                }
                IdentifierMatcher.appendReplacement(localExpression, prefix + value);
            } else {
                throw new IdentifierException(key);
            }
        }
        IdentifierMatcher.appendTail(localExpression);

        String newExpression = localExpression.toString();
        Logger.getLogger("calcLogger").log(Level.FINE, "IdentifiersInsert -> " + newExpression);
        return newExpression;
    }
}
