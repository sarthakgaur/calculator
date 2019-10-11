package Calculator;


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
            operatorPresent = false;
        } else {
            throw new ExpressionFormatException();
        }
        stateChanged = true;
    }

    void addOperator(String aOperator) throws ExpressionFormatException {
        if (numberPresent && !operatorPresent) {
            operatorPresent = true;
        } else {
            throw new ExpressionFormatException();
        }
    }

    void modParCount(int n) {
        openParCount += n;
        parStateChanged = true;
        stateChanged = true;
    }

    boolean hasStateChanged() {
        return stateChanged;
    }

    boolean getState() {
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
