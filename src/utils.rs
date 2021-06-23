use crate::token::{Operator, OperatorName, Token};
use anyhow::anyhow;
use fehler::throws;
use std::io::{self, Write};

#[throws(anyhow::Error)]
pub fn get_expr() -> String {
    let mut stdout = io::stdout();
    write!(&mut stdout, "> ")?;
    stdout.flush()?;

    let mut input = String::new();
    std::io::stdin().read_line(&mut input)?;
    input.trim().to_string()
}

#[throws(anyhow::Error)]
pub fn get_postfix(tokens: &[Token]) -> Vec<Token> {
    let local_tokens = tokens.to_owned();
    let mut output_stack: Vec<Token> = Vec::new();
    let mut operator_stack: Vec<Operator> = Vec::new();

    for token in local_tokens {
        match token {
            Token::Number(_) => output_stack.push(token),
            Token::Operator(operator) => match &operator.name {
                OperatorName::OpenParenthesis => operator_stack.push(operator),
                OperatorName::CloseParenthesis => {
                    let mut last = operator_stack
                        .pop()
                        .ok_or_else(|| anyhow!("operator_stack empty."))?;
                    let is_open = last.name == OperatorName::OpenParenthesis;

                    while !operator_stack.is_empty() && !is_open {
                        output_stack.push(Token::Operator(last));
                        last = operator_stack
                            .pop()
                            .ok_or_else(|| anyhow!("operator_stack empty."))?;
                    }
                }
                _ => {
                    let mut last = operator_stack.last();
                    let current_prec = operator.precedence;

                    while last.is_some() && last.unwrap().precedence >= current_prec {
                        let last_op = operator_stack
                            .pop()
                            .ok_or_else(|| anyhow!("operator_stack empty."))?;
                        output_stack.push(Token::Operator(last_op));
                        last = operator_stack.last();
                    }

                    operator_stack.push(operator);
                }
            },
        }
    }

    while !operator_stack.is_empty() {
        let operator = operator_stack
            .pop()
            .ok_or_else(|| anyhow!("operator_stack empty."))?;
        output_stack.push(Token::Operator(operator));
    }

    output_stack
}
