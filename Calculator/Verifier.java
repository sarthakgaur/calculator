package Calculator;

import java.util.*;


class Verifier {

    private static Set<Character> validSymbols = Set.of('+', '-', '*', '/', '^', '!', '(', ')');
    private static Set<String> validOperators = Set.of("+", "-", "*", "/", "^");

    static boolean verify(String expression) {
        boolean res = false;
        try {
            ArrayList<String> tokens = generateTokens(expression);
            // System.out.println(tokens);
            res = isComputable(tokens);
        } catch (NumberFormatException | InvalidSymbolException e) {
            // System.out.println("");
        }
        return res;
    }

    static private ArrayList<String> generateTokens(String expression)
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
                throw new InvalidSymbolException();
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

    static private boolean isComputable(ArrayList<String> tokens) {
        return isComputable(tokens, new int[] {0});
    }

    static private boolean isComputable(ArrayList<String> tokens, int[] offset) {
        var localExpression = new Expression();
        var subState = true;

        while(offset[0] < tokens.size()) {
            String token = tokens.get(offset[0]);

            try {
                if (Evaluator.isNumber(token)) {
                    localExpression.addNumber();
                } else if (validOperators.contains(token)) {
                    localExpression.addOperator(token);
                } else if (token.equals("(")) {
                    if (localExpression.hasStateChanged()) {
                        subState = isComputable(tokens, offset);
                        if (!subState) {
                            return false;
                        } else {
                            localExpression.addNumber();
                        }
                    } else {
                        localExpression.modParCount(1);
                    }
                } else if (token.equals(")")) {
                    localExpression.modParCount(-1);
                    return localExpression.getState();
                }
            } catch (ExpressionFormatException e) {
                return false;
            }

            offset[0]++;
        }

        return localExpression.getState();
    }
}
