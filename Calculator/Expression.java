package Calculator;

/**
 * Tracks the state of the expression.
 */
class Expression {

    private boolean numberPresent;
    private boolean operatorPresent;
    private boolean state;
    private int openParCount;
    private boolean stateChanged;
    private boolean parStateChanged;

    void addNumber() throws ExpressionFormatException {
        if (!numberPresent) {
            numberPresent = true;
        } else if (operatorPresent) {
            // if operator is present, make the entire expression a number.
            operatorPresent = false;
        } else {
            throw new ExpressionFormatException();
        }
        stateChanged = true;
    }

    void addOperator() throws ExpressionFormatException {
        if (numberPresent && !operatorPresent) {
            operatorPresent = true;
        } else {
            throw new ExpressionFormatException();
        }
    }

    /**
     * Modify open parenthesis count.
     */
    void modParCount(int n) {
        openParCount += n;
        parStateChanged = true;
        stateChanged = true;
    }

    boolean hasStateChanged() {
        return stateChanged;
    }

    /**
     * Get the current state of the expression.
     * @return a boolean value.
     */
    boolean getState() {
        if (!numberPresent && !operatorPresent) {
            // if parenthesis state is changed a number need to be present
            state = !parStateChanged;
        } else if (numberPresent && operatorPresent) {
            state = false;
        } else if (numberPresent) {
            state = true;
        }

        // return true only if the state is true and parentheses are balanced
        return state && openParCount == 0;
    }
}
