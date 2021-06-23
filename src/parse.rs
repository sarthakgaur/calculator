use crate::token::{Operator, OperatorName, Token};
use anyhow::bail;
use fehler::throws;
use std::collections::HashMap;

#[throws(anyhow::Error)]
pub fn parse_expr(expr: &str) -> Vec<Token> {
    let mut tokens: Vec<Token> = Vec::new();
    let mut num_buffer: Vec<char> = Vec::new();
    let oper_map = get_oper_map();

    for ch in expr.chars() {
        if ch.is_digit(10) || ch == '.' {
            num_buffer.push(ch);
        } else if oper_map.contains_key(&ch) {
            push_num(&mut tokens, &mut num_buffer);
            let op = oper_map.get(&ch).unwrap().clone();
            tokens.push(Token::Operator(op));
        } else if ch == ' ' {
            continue;
        } else {
            bail!("Unexpected token found.");
        }
    }

    push_num(&mut tokens, &mut num_buffer);

    tokens
}

fn get_oper_map() -> HashMap<char, Operator> {
    let mut op_map: HashMap<char, Operator> = HashMap::new();
    op_map.insert(
        '+',
        Operator {
            name: OperatorName::Add,
            precedence: 1,
        },
    );
    op_map.insert(
        '-',
        Operator {
            name: OperatorName::Subtract,
            precedence: 1,
        },
    );
    op_map.insert(
        '*',
        Operator {
            name: OperatorName::Multiply,
            precedence: 2,
        },
    );
    op_map.insert(
        '/',
        Operator {
            name: OperatorName::Divide,
            precedence: 2,
        },
    );
    op_map.insert(
        '(',
        Operator {
            name: OperatorName::OpenParenthesis,
            precedence: 0,
        },
    );
    op_map.insert(
        ')',
        Operator {
            name: OperatorName::CloseParenthesis,
            precedence: 0,
        },
    );

    op_map
}

fn push_num(tokens: &mut Vec<Token>, num_buffer: &mut Vec<char>) {
    if !num_buffer.is_empty() {
        let num_str: String = num_buffer.iter().collect();
        let num: f64 = num_str
            .parse()
            .expect("Error occurred while parsing a number.");
        tokens.push(Token::Number(num));
        num_buffer.clear();
    }
}
