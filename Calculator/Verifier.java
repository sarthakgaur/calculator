package Calculator;

import java.util.*;


class Verifier {

    private static Set<String> validOperators = Set.of("+", "-", "*", "/", "^");

    /**
     * Passes tokens and the starting offset to isComputable to verify the expression
     *
     * @param tokens contains valid numbers and symbols.
     * @return a boolean value returned by isComputable.
     */
    static void verify(ArrayList<String> tokens) throws ExpressionFormatException {
        boolean isValid =  isComputable(tokens, new int[] {0});
        if (!isValid) {
            throw new ExpressionFormatException("");
        }
    }

    /**
     * Verifies the state of expression and its nested expressions recursively.
     *
     * @param tokens a list containing valid numbers and symbols.
     * @param offset keeps track of the current position in the list.
     * @return a boolean value denoting whether the expression is valid.
     */
    private static boolean isComputable(ArrayList<String> tokens, int[] offset) {
        var localExpression = new Expression();
        var subState = true;

        while(offset[0] < tokens.size()) {
            String token = tokens.get(offset[0]);

            try {
                if (Evaluator.isNumber(token)) {
                    localExpression.addNumber();
                } else if (validOperators.contains(token)) {
                    localExpression.addOperator();
                } else if (token.equals("(")) {
                    if (localExpression.hasStateChanged()) {
                        subState = isComputable(tokens, offset);
                        if (!subState) {
                            return false;
                        }
                        localExpression.addNumber();
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
