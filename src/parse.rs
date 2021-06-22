use std::collections::HashMap;

use crate::token::{Operator, OperatorName, Token};

pub fn parse_expr(expr: &String) -> Vec<Token> {
    let mut tokens: Vec<Token> = Vec::new();
    let mut num_buffer: Vec<char> = Vec::new();
    let op_map = get_op_map();

    for c in expr.chars() {
        if c.is_digit(10) {
            num_buffer.push(c);
        } else if c == '.' {
            num_buffer.push(c);
        } else if op_map.contains_key(&c) {
            push_num(&mut tokens, &mut num_buffer);
            let op = op_map.get(&c).unwrap().clone();
            tokens.push(Token::Operator(op));
        } else {
            if c == ' ' {
                continue;
            } else {
                panic!("Unexpected token found.");
            }
        }
    }

    push_num(&mut tokens, &mut num_buffer);

    tokens
}

fn get_op_map() -> HashMap<char, Operator> {
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
    if num_buffer.len() > 0 {
        let num_str: String = num_buffer.iter().collect();
        let num: f64 = num_str
            .parse()
            .expect("Error occurred while parsing a number.");
        tokens.push(Token::Number(num));
        num_buffer.clear();
    }
}
