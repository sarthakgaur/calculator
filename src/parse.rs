use std::collections::HashMap;

use anyhow::bail;
use fehler::throws;

use crate::token::{Operator, OperatorName, Token};

#[throws(anyhow::Error)]
pub fn parse_expr(expr: &str) -> Vec<Token> {
    let mut tokens: Vec<Token> = Vec::new();
    let mut num_buffer: Vec<char> = Vec::new();
    let mut ident_buffer: Vec<char> = Vec::new();
    let oper_map = get_oper_map();

    for ch in expr.chars() {
        if ch.is_alphabetic() && num_buffer.len() == 0 {
            ident_buffer.push(ch);
            push_num(&mut tokens, &mut num_buffer);
        } else if ch.is_alphanumeric() && ident_buffer.len() > 0 {
            ident_buffer.push(ch);
        } else if ch.is_digit(10) || ch == '.' {
            num_buffer.push(ch);
        } else if oper_map.contains_key(&ch) {
            push_ident(&mut tokens, &mut ident_buffer);
            push_num(&mut tokens, &mut num_buffer);
            let op = oper_map.get(&ch).unwrap().clone();
            tokens.push(Token::Operator(op));
        } else if ch == ' ' {
            push_ident(&mut tokens, &mut ident_buffer);
            push_num(&mut tokens, &mut num_buffer);
        } else {
            bail!("Unexpected token found.");
        }
    }

    push_ident(&mut tokens, &mut ident_buffer);
    push_num(&mut tokens, &mut num_buffer);

    tokens
}

fn get_oper_map() -> HashMap<char, Operator> {
    let mut op_map: HashMap<char, Operator> = HashMap::new();

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
        '^',
        Operator {
            name: OperatorName::Exponentiation,
            precedence: 3,
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

fn push_ident(tokens: &mut Vec<Token>, ident_buffer: &mut Vec<char>) {
    if !ident_buffer.is_empty() {
        let ident = ident_buffer.iter().collect();
        tokens.push(Token::Identifier(ident));
        ident_buffer.clear();
    }
}
