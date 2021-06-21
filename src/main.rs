use std::collections::HashMap;
use std::io::{self, Write};

// TODO Get the expresssion from stdin. Done.
// TODO Parse the expression. Done.
// TODO Get the post fix expression. Done.
// TODO Evaluate the expression. Done.
// TODO Add parantheses. Done.
// TODO Refactor the code. Done.
// TODO Handle unary operators. Done.
// TODO Refactor the code.

#[derive(PartialEq, Debug, Clone)]
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
    OpenParenthesis,
    CloseParenthesis,
}

struct Expression {
    num: Option<f64>,
    operator: Option<Operator>,
    extra_operators: usize,
    paren_count: usize,
    tokens: Vec<Token>,
}

impl Expression {
    fn new() -> Expression {
        Expression {
            num: None,
            operator: None,
            extra_operators: 0,
            paren_count: 0,
            tokens: Vec::new(),
        }
    }

    fn add_num(&mut self, num: f64) {
        if self.num.is_none() || self.operator.is_some() {
            self.num = Some(num);
            let update_num = self.handle_extra_ops(num);
            self.tokens.push(Token::Number(update_num));
        } else {
            panic!("Invalid expression.")
        }
    }

    fn add_operator(&mut self, operator: Operator) {
        if self.num.is_none() {
            match operator.name {
                OperatorName::Add => (),
                OperatorName::Subtract => self.extra_operators += 1,
                _ => panic!("Invalid operator position."),
            }
        } else {
            self.operator = Some(operator.clone());
            self.tokens.push(Token::Operator(operator));
        }
    }

    fn handle_extra_ops(&mut self, num: f64) -> f64 {
        let update_num = if self.extra_operators % 2 == 0 {
            num
        } else {
            num * -1.0
        };

        self.extra_operators = 0;

        update_num
    }

    fn handle_paren(&mut self, operator_name: OperatorName) {
        match operator_name {
            OperatorName::OpenParenthesis => self.paren_count += 1,
            OperatorName::CloseParenthesis => self.paren_count -= 1,
            _ => panic!("Not a parenthesis."),
        }
    }

    fn evaluate(&self) -> f64 {
        if self.num.is_none() {
            panic!("Invalid expression.");
        } else if self.paren_count != 0 {
            panic!("Unbalanced parentheses.")
        } else if self.extra_operators != 0 {
            panic!("Extra operators provided.")
        } else {
            let postfix_tokens = get_postfix(&self.tokens);
            let res = eval_postfix(postfix_tokens);
            return res;
        }
    }
}

fn main() {
    loop {
        let expr = get_expr();
        println!("You entered the expression, {:?}", expr);
        let tokens = parse_expr(&expr);
        println!("tokens, {:?}", tokens);
        let res = eval_expr(&tokens);

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

fn eval_expr(tokens: &Vec<Token>) -> f64 {
    let mut index = 0;
    return _eval_expr(tokens, &mut index, false);
}

fn _eval_expr(tokens: &Vec<Token>, index: &mut usize, inside_paren: bool) -> f64 {
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

fn get_postfix(tokens: &Vec<Token>) -> Vec<Token> {
    let cloned_tokens = tokens.clone();
    let mut output_stack: Vec<Token> = Vec::new();
    let mut operator_stack: Vec<Operator> = Vec::new();

    for token in cloned_tokens {
        match token {
            Token::Number(_) => output_stack.push(token),
            Token::Operator(operator) => match &operator.name {
                OperatorName::OpenParenthesis => operator_stack.push(operator),
                OperatorName::CloseParenthesis => {
                    let mut last = operator_stack.pop().unwrap();
                    let is_open = last.name == OperatorName::OpenParenthesis;

                    while !operator_stack.is_empty() && !is_open {
                        output_stack.push(Token::Operator(last));
                        last = operator_stack.pop().unwrap();
                    }
                }
                _ => {
                    let mut last = operator_stack.last();
                    let current_prec = operator.precedence;

                    while last.is_some() && last.unwrap().precedence >= current_prec {
                        let last_op = operator_stack.pop().unwrap();
                        output_stack.push(Token::Operator(last_op));
                        last = operator_stack.last();
                    }

                    operator_stack.push(operator);
                }
            },
        }
    }

    while !operator_stack.is_empty() {
        let operator = operator_stack.pop().unwrap();
        output_stack.push(Token::Operator(operator));
    }

    output_stack
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
