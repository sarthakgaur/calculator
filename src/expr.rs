use crate::eval;
use crate::token::{Operator, OperatorName, Token};
use crate::utils;

pub struct Expression {
    num: Option<f64>,
    operator: Option<Operator>,
    extra_operators: usize,
    paren_count: usize,
    tokens: Vec<Token>,
}

impl Expression {
    pub fn new() -> Expression {
        Expression {
            num: None,
            operator: None,
            extra_operators: 0,
            paren_count: 0,
            tokens: Vec::new(),
        }
    }

    pub fn add_num(&mut self, num: f64) {
        if self.num.is_none() || self.operator.is_some() {
            self.num = Some(num);
            let update_num = self.handle_extra_ops(num);
            self.tokens.push(Token::Number(update_num));
        } else {
            panic!("Invalid expression.")
        }
    }

    pub fn add_operator(&mut self, operator: Operator) {
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

    pub fn handle_extra_ops(&mut self, num: f64) -> f64 {
        let update_num = if self.extra_operators % 2 == 0 {
            num
        } else {
            num * -1.0
        };

        self.extra_operators = 0;

        update_num
    }

    pub fn handle_paren(&mut self, operator_name: OperatorName) {
        match operator_name {
            OperatorName::OpenParenthesis => self.paren_count += 1,
            OperatorName::CloseParenthesis => self.paren_count -= 1,
            _ => panic!("Not a parenthesis."),
        }
    }

    pub fn evaluate(&self) -> f64 {
        if self.num.is_none() {
            panic!("Invalid expression.");
        } else if self.paren_count != 0 {
            panic!("Unbalanced parentheses.")
        } else if self.extra_operators != 0 {
            panic!("Extra operators provided.")
        } else {
            let postfix_tokens = utils::get_postfix(&self.tokens);
            let res = eval::eval_postfix(postfix_tokens);
            return res;
        }
    }
}
