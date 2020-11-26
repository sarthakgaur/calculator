package Calculator;


class CalculatorException extends Exception {

    CalculatorException() {}

    CalculatorException(String s) {
        super(s);
    }
}


class IdentifierException extends CalculatorException {

    IdentifierException() {}

    IdentifierException(String s) {
        super("Invalid Identifier: " + s);
    }

}


class InvalidSymbolException extends CalculatorException {

    InvalidSymbolException() {}

    InvalidSymbolException(char s) {
        super("Invalid Symbol: " + s);
    }
}


class InvalidNumberException extends CalculatorException {

    InvalidNumberException() {}

    InvalidNumberException(String s) {
        super("Invalid Number: " + s);
    }
}


class ExpressionFormatException extends CalculatorException {

    ExpressionFormatException() {}

    ExpressionFormatException(String s) {
        super("Invalid Expression");
    }
}
