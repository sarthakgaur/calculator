package Calculator;

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
