#[derive(PartialEq, Debug, Clone)]
pub enum Token {
    Number(f64),
    Operator(Operator),
}

#[derive(Clone, PartialEq, Debug)]
pub struct Operator {
    pub name: OperatorName,
    pub precedence: usize,
}

#[derive(Clone, PartialEq, Debug)]
pub enum OperatorName {
    OpenParenthesis,
    CloseParenthesis,
    Add,
    Subtract,
    Multiply,
    Divide,
    Exponentiation,
}
