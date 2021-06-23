use crate::expr::Expression;
use crate::token::{Operator, OperatorName, Token};
use anyhow::{anyhow, bail};
use fehler::throws;

#[throws(anyhow::Error)]
pub fn eval_expr(tokens: &[Token]) -> f64 {
    let mut index = 0;
    _eval_expr(tokens, &mut index, false)?
}

#[throws(anyhow::Error)]
fn _eval_expr(tokens: &[Token], index: &mut usize, inside_paren: bool) -> f64 {
    let mut expr = Expression::new();

    if inside_paren {
        expr.handle_paren(OperatorName::OpenParenthesis)?;
    }

    while *index < tokens.len() {
        match &tokens[*index] {
            Token::Number(n) => expr.add_num(n.to_owned())?,
            Token::Operator(oper) => match oper.name {
                OperatorName::OpenParenthesis => {
                    *index += 1;
                    let num = _eval_expr(tokens, index, true)?;
                    expr.add_num(num)?;
                }
                OperatorName::CloseParenthesis => {
                    expr.handle_paren(OperatorName::CloseParenthesis)?;
                    return expr.eval()?;
                }
                _ => {
                    expr.add_operator(oper.clone())?;
                }
            },
        }

        *index += 1;
    }

    expr.eval()?
}

#[throws(anyhow::Error)]
pub fn eval_postfix(tokens: Vec<Token>) -> f64 {
    let mut eval_stack: Vec<Token> = Vec::new();

    for token in tokens {
        match token {
            Token::Number(_) => eval_stack.push(token),
            Token::Operator(oper) => {
                let n2 = eval_stack
                    .pop()
                    .ok_or_else(|| anyhow!("eval_stack empty."))?;
                let n1 = eval_stack
                    .pop()
                    .ok_or_else(|| anyhow!("eval_stack empty."))?;
                let res = calc(&n1, &n2, &oper);
                eval_stack.push(res?);
            }
        }
    }

    match eval_stack
        .pop()
        .ok_or_else(|| anyhow!("eval_stack empty."))?
    {
        Token::Number(n) => n,
        _ => bail!("Error occurred while evaluating."),
    }
}

#[throws(anyhow::Error)]
fn calc(t1: &Token, t2: &Token, oper: &Operator) -> Token {
    let n1 = match t1 {
        Token::Number(n) => n,
        _ => bail!("Token is not a number."),
    };

    let n2 = match t2 {
        Token::Number(n) => n,
        _ => bail!("Token is not a number."),
    };

    let res = match oper.name {
        OperatorName::Add => n1 + n2,
        OperatorName::Subtract => n1 - n2,
        OperatorName::Multiply => n1 * n2,
        OperatorName::Divide => n1 / n2,
        _ => bail!("Invalid operator."),
    };

    Token::Number(res)
}
