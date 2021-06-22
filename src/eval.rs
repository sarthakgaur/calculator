use crate::expr::Expression;
use crate::token::{Operator, OperatorName, Token};

pub fn eval_expr(tokens: &[Token]) -> f64 {
    let mut index = 0;
    _eval_expr(tokens, &mut index, false)
}

fn _eval_expr(tokens: &[Token], index: &mut usize, inside_paren: bool) -> f64 {
    let mut expression = Expression::new();

    if inside_paren {
        expression.handle_paren(OperatorName::OpenParenthesis);
    }

    while *index < tokens.len() {
        match &tokens[*index] {
            Token::Number(n) => expression.add_num(n.to_owned()),
            Token::Operator(operator) => match operator.name {
                OperatorName::OpenParenthesis => {
                    *index += 1;
                    let num = _eval_expr(tokens, index, true);
                    expression.add_num(num);
                }
                OperatorName::CloseParenthesis => {
                    expression.handle_paren(OperatorName::CloseParenthesis);
                    return expression.evaluate();
                }
                _ => {
                    expression.add_operator(operator.clone());
                }
            },
        }

        *index += 1;
    }

    expression.evaluate()
}

pub fn eval_postfix(tokens: Vec<Token>) -> f64 {
    let mut eval_stack: Vec<Token> = Vec::new();

    for token in tokens {
        match token {
            Token::Number(_) => eval_stack.push(token),
            Token::Operator(operator) => {
                let n2 = eval_stack.pop().unwrap();
                let n1 = eval_stack.pop().unwrap();
                let res = calc(&n1, &n2, &operator);
                eval_stack.push(res);
            }
        }
    }

    match eval_stack.pop().unwrap() {
        Token::Number(n) => n,
        _ => panic!("Error occurred while evaluating."),
    }
}

fn calc(t1: &Token, t2: &Token, operator: &Operator) -> Token {
    let n1 = match t1 {
        Token::Number(n) => n,
        _ => panic!("Error occurred while calculating."),
    };

    let n2 = match t2 {
        Token::Number(n) => n,
        _ => panic!("Error occurred while calculating."),
    };

    let res = match operator.name {
        OperatorName::Add => n1 + n2,
        OperatorName::Subtract => n1 - n2,
        OperatorName::Multiply => n1 * n2,
        OperatorName::Divide => n1 / n2,
        _ => panic!("Invalid operator."),
    };

    Token::Number(res)
}
