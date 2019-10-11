package Calculator;

class CalculatorException extends Exception {}

class InvalidSymbolException extends CalculatorException {}

class ExpressionFormatException extends CalculatorException {}

class IdentifierException extends CalculatorException {}
