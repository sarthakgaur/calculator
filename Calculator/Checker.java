package Calculator;


import java.util.ArrayList;
import java.util.Set;

/**
 * Helps in checking the expression provided to it.
 */
class Checker {

    private static Set<Character> validSymbols = Set.of('+', '-', '*', '/', '^', '!', '(', ')');
    private Identifiers identifiers;

    Checker(Identifiers i) {
        identifiers = i;
    }

    /**
     * Checks the received expression and sends Message to the caller accordingly.
     * The following list explains all the status codes.
     * 1. 1 -> close the program
     * 2. 2 -> delete all identifiers
     * 3. 3 -> successful creation of identifiers
     * 4. 4 -> identifier creation failed
     *
     *
     * @param expression A string containing a mathematical expression or identifier
     *                       expression.
     *
     * @return A Message object containing the required status and message.
     */
    Message check(String expression) {
        if (expression.equals("q")) {
            return new Message(1, "");
        } else if (expression.equals("n")) {
            identifiers.clear();
            return new Message(2, "");
        } else if (expression.contains("=")) {
            try {
                identifiers.create(expression);
                return new Message(3, "");
            } catch (IdentifierException e) {
                return new Message(4, "Invalid Identifier.");
            }
        }

        ArrayList<String> tokens;
        boolean validExpression;

        try {
            expression = identifiers.replace(expression);
            expression = handleUnary(expression);
            tokens = generateTokens(expression);
            validExpression = Verifier.verify(tokens);
        } catch (IdentifierException e) {
            return new Message(4, "Invalid Identifier.");
        } catch (NumberFormatException e) {
            return new Message(4, "Invalid Number.");
        } catch (InvalidSymbolException e) {
            return new Message(4, "Invalid Symbol.");
        }

        if (!validExpression) {
            return new Message(4, "Invalid Expression");
        }

        return new Message(0, tokens);
    }

    /**
     * Handles the unary minus operator.
     */
    private static String handleUnary(String expression) {
        expression = expression.replaceAll("-", "~");
        expression = expression.replaceAll("(\\d|!)~(\\d)", "$1-$2");
        return expression;
    }

    private static ArrayList<String> generateTokens(String expression)
            throws NumberFormatException, InvalidSymbolException {
        var tokens = new ArrayList<String>();
        var digits = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            Character token = expression.charAt(i);

            if (Character.isDigit(token) || token.equals('.') || token.equals('~')) {
                if (token.equals('~')) {
                    digits.append('-');
                } else {
                    digits.append(token.toString());
                }
            } else if (validSymbols.contains(token)) {
                if (digits.length() > 0) {
                    if (Evaluator.isNumber(digits.toString())) {
                        tokens.add(digits.toString());
                        digits = new StringBuilder();
                    } else {
                        throw new NumberFormatException();
                    }
                }
                tokens.add(token.toString());
            } else {
                if (!token.equals(" ")) {
                    throw new InvalidSymbolException();
                }
            }
        }

        if (digits.length() > 0) {
            if (Evaluator.isNumber(digits.toString())) {
                tokens.add(digits.toString());
            } else {
                throw new NumberFormatException();
            }
        }

        return tokens;
    }
}