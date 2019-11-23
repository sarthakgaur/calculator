package Calculator;

/**
 * Tracks the state of the expression.
 */
class Expression {

    private boolean numberPresent;
    private boolean operatorPresent;
    private int openParCount;
    private boolean stateChanged;
    private boolean parStateChanged;

    /**
     * Adds a number to the expression.
     * @throws ExpressionFormatException if this method is called consecutively
     */
    void addNumber() throws ExpressionFormatException {
        if (!numberPresent) {
            numberPresent = true;
        } else if (operatorPresent) {
            operatorPresent = false;
        } else {
            throw new ExpressionFormatException();
        }
        stateChanged = true;
    }

    /**
     * Adds an operator to the expression.
     * @throws ExpressionFormatException if no number is present or this method is
     * called consecutively
     */
    void addOperator() throws ExpressionFormatException {
        if (numberPresent && !operatorPresent) {
            operatorPresent = true;
        } else {
            throw new ExpressionFormatException();
        }
        stateChanged = true;
    }

    /**
     * Modify open parenthesis count.
     * @param n count by which open parenthesis needs to be increased.
     */
    void modParCount(int n) {
        openParCount += n;
        parStateChanged = true;
        stateChanged = true;
    }

    /**
     * Tells whether the state of the expression has changed.
     * @return a boolean value
     */
    boolean hasStateChanged() {
        return stateChanged;
    }

    /**
     * Get the current state of the expression.
     * @return a boolean value
     */
    boolean getState() {
        boolean state = false;
        if (!numberPresent && !operatorPresent) {
            state = !parStateChanged;
        } else if (numberPresent && operatorPresent) {
            state = false;
        } else if (numberPresent) {
            state = true;
        }

        return state && openParCount == 0;
    }
}
