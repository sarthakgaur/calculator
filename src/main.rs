use std::collections::HashMap;
use std::io::{self, Write};

// TODO Get the expresssion from stdin. Done.
// TODO Parse the expression. Done.
// TODO Get the post fix expression. Done.
// TODO Evaluate the expression. Done.
// TODO Add parantheses. Done.
// TODO Refactor the code. Done.

#[derive(PartialEq, Debug)]
enum Token {
    Number(f64),
    Operator(Operator),
}

#[derive(Clone, PartialEq, Debug)]
struct Operator {
    name: OperatorName,
    precedence: usize,
}

#[derive(Clone, PartialEq, Debug)]
enum OperatorName {
    Add,
    Subtract,
    Multiply,
    Divide,
    Parentheses(Parentheses),
}

#[derive(Clone, PartialEq, Debug)]
enum Parentheses {
    Open,
    Close,
}

fn main() {
    loop {
        let expr = get_expr();
        println!("You entered the expression, {:?}", expr);
        let tokens = parse_expr(&expr);
        println!("tokens, {:?}", tokens);
        let postfix_tokens = get_postfix(tokens);
        println!("postfix tokens, {:?}", postfix_tokens);
        let res = eval_postfix(postfix_tokens);

        println!("res, {:?}", res);
    }
}

fn get_expr() -> String {
    let mut stdout = io::stdout();
    write!(&mut stdout, "> ").unwrap();
    stdout.flush().unwrap();

    let mut input = String::new();
    std::io::stdin().read_line(&mut input).unwrap();
    input.trim().to_string()
}

fn parse_expr(expr: &String) -> Vec<Token> {
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
            name: OperatorName::Parentheses(Parentheses::Open),
            precedence: 0,
        },
    );
    op_map.insert(
        ')',
        Operator {
            name: OperatorName::Parentheses(Parentheses::Close),
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

fn get_postfix(tokens: Vec<Token>) -> Vec<Token> {
    let mut output_stack: Vec<Token> = Vec::new();
    let mut operator_stack: Vec<Operator> = Vec::new();

    for token in tokens {
        match token {
            Token::Number(_) => output_stack.push(token),
            Token::Operator(operator) => match &operator.name {
                OperatorName::Parentheses(paren) => match paren {
                    Parentheses::Open => operator_stack.push(operator),
                    Parentheses::Close => {
                        handle_close_paren(&mut operator_stack, &mut output_stack)
                    }
                },
                _ => handle_rest_ops(&mut operator_stack, &mut output_stack, operator),
            },
        }
    }

    while !operator_stack.is_empty() {
        let operator = operator_stack.pop().unwrap();
        output_stack.push(Token::Operator(operator));
    }

    output_stack
}

fn handle_close_paren(operator_stack: &mut Vec<Operator>, output_stack: &mut Vec<Token>) {
    let mut last = operator_stack.pop().unwrap();
    let is_open = last.name == OperatorName::Parentheses(Parentheses::Open);
    while !operator_stack.is_empty() && !is_open {
        output_stack.push(Token::Operator(last));
        last = operator_stack.pop().unwrap();
    }
}

fn handle_rest_ops(
    operator_stack: &mut Vec<Operator>,
    output_stack: &mut Vec<Token>,
    operator: Operator,
) {
    let mut last = operator_stack.last();
    let current_prec = operator.precedence;

    while last.is_some() && last.unwrap().precedence >= current_prec {
        let last_op = operator_stack.pop().unwrap();
        output_stack.push(Token::Operator(last_op));
        last = operator_stack.last();
    }

    operator_stack.push(operator);
}

fn eval_postfix(tokens: Vec<Token>) -> f64 {
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
