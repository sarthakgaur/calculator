use std::collections::HashMap;
use std::collections::HashSet;
use std::io::{self, Write};

// TODO Get the expresssion from stdin. Done.
// TODO Parse the expression. Done.
// TODO Get the post fix expression. Done.
// TODO Evaluate the expression. Done.

fn main() {
    loop {
        let expr = get_expr();
        let tokens = parse_expr(&expr);
        let postfix_tokens = get_postfix(&tokens);
        let res = eval_postfix(&postfix_tokens);

        println!("You entered the expression, {:?}", expr);
        println!("tokens, {:?}", tokens);
        println!("postfix tokens, {:?}", postfix_tokens);
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

fn parse_expr(expr: &String) -> Vec<String> {
    let mut tokens: Vec<String> = Vec::new();
    let mut num_buffer: Vec<char> = Vec::new();
    let mut op_set: HashSet<char> = HashSet::new();

    op_set.insert('+');
    op_set.insert('-');
    op_set.insert('*');
    op_set.insert('/');

    for c in expr.chars() {
        if c.is_digit(10) {
            num_buffer.push(c);
        } else if c == '.' {
            num_buffer.push(c);
        } else if op_set.contains(&c) {
            push_num(&mut num_buffer, &mut tokens);
            tokens.push(c.to_string());
        } else {
            if c == ' ' {
                continue;
            } else {
                panic!("Unexpected token found.");
            }
        }
    }

    push_num(&mut num_buffer, &mut tokens);

    tokens
}

fn push_num(num_buffer: &mut Vec<char>, tokens: &mut Vec<String>) {
    if num_buffer.len() > 0 {
        let num_str: String = num_buffer.iter().collect();
        let num: f64 = num_str
            .parse()
            .expect("Error occurred while parsing a number.");
        tokens.push(num.to_string());
        num_buffer.clear();
    }
}

fn get_postfix(tokens: &Vec<String>) -> Vec<String> {
    let mut output_stack: Vec<String> = Vec::new();
    let mut operator_stack: Vec<String> = Vec::new();
    let mut op_set: HashMap<&str, usize> = HashMap::new();

    op_set.insert("+", 1);
    op_set.insert("-", 1);
    op_set.insert("*", 2);
    op_set.insert("/", 3);

    for token in tokens {
        if token.parse::<f64>().is_ok() {
            output_stack.push(token.to_string());
        } else if op_set.contains_key(token.as_str()) {
            if operator_stack.is_empty() {
                operator_stack.push(token.to_string());
            } else {
                let mut last = operator_stack.last();
                let current_prec = op_set.get(token.as_str());

                while last.is_some() && op_set.get(last.unwrap().as_str()) >= current_prec {
                    let operator = operator_stack.pop().unwrap();
                    output_stack.push(operator);
                    last = operator_stack.last();
                }

                operator_stack.push(token.to_string());
            }
        } else {
            panic!("Unexpected token found.");
        }
    }

    while !operator_stack.is_empty() {
        let operator = operator_stack.pop().unwrap();
        output_stack.push(operator.to_string());
    }

    output_stack
}

fn eval_postfix(tokens: &Vec<String>) -> String {
    let mut eval_stack: Vec<String> = Vec::new();
    let mut op_set: HashSet<String> = HashSet::new();

    op_set.insert("+".to_string());
    op_set.insert("-".to_string());
    op_set.insert("*".to_string());
    op_set.insert("/".to_string());

    for token in tokens {
        if token.parse::<f64>().is_ok() {
            eval_stack.push(token.to_string());
        } else if op_set.contains(token) {
            let n2 = eval_stack.pop().unwrap();
            let n1 = eval_stack.pop().unwrap();
            let res = calc(&n1, &n2, token);
            eval_stack.push(res.to_string());
        }
    }

    eval_stack.pop().unwrap()
}

fn calc(n1: &String, n2: &String, operator: &String) -> f64 {
    let n1 = n1.parse::<f64>().unwrap();
    let n2 = n2.parse::<f64>().unwrap();

    match operator.as_str() {
        "+" => n1 + n2,
        "-" => n1 - n2,
        "*" => n1 * n2,
        "/" => n1 / n2,
        _ => panic!("Invalid operator."),
    }
}
