use anyhow::bail;
use fehler::throws;

use crate::eval;
use crate::token::{Operator, OperatorName, Token};
use crate::utils;

pub struct Expression {
    is_num_set: bool,
    is_oper_set: bool,
    extra_opers: usize,
    paren_count: isize,
    tokens: Vec<Token>,
}

impl Expression {
    pub fn new() -> Expression {
        Expression {
            is_num_set: false,
            is_oper_set: false,
            extra_opers: 0,
            paren_count: 0,
            tokens: Vec::new(),
        }
    }

    #[throws(anyhow::Error)]
    pub fn add_num(&mut self, num: f64) {
        if !self.is_num_set {
            self.set_num(num);
        } else if self.is_oper_set {
            self.set_num(num);
            self.is_oper_set = false;
        } else {
            bail!("Invalid expression.")
        }
    }

    #[throws(anyhow::Error)]
    pub fn add_oper(&mut self, oper: Operator) {
        if !self.is_num_set || self.is_oper_set {
            match oper.name {
                OperatorName::Add => (),
                OperatorName::Subtract => self.extra_opers += 1,
                _ => bail!("Invalid expression."),
            }
        } else {
            self.is_oper_set = true;
            self.tokens.push(Token::Operator(oper));
        }
    }

    #[throws(anyhow::Error)]
    pub fn add_paren(&mut self, oper_name: OperatorName) {
        match oper_name {
            OperatorName::OpenParenthesis => self.paren_count += 1,
            OperatorName::CloseParenthesis => self.paren_count -= 1,
            _ => bail!("Not a parenthesis."),
        }
    }

    #[throws(anyhow::Error)]
    pub fn eval(&self) -> f64 {
        if !self.is_num_set {
            bail!("Number not found in expression.");
        } else if self.paren_count != 0 {
            bail!("Unbalanced parentheses.")
        } else if self.extra_opers != 0 {
            bail!("Extra operators provided.")
        } else {
            let postfix_tokens = utils::get_postfix(&self.tokens)?;
            eval::eval_postfix(postfix_tokens)?
        }
    }

    fn set_num(&mut self, num: f64) {
        self.is_num_set = true;
        let update_num = self.handle_extra_opers(num);
        self.tokens.push(Token::Number(update_num));
    }

    fn handle_extra_opers(&mut self, num: f64) -> f64 {
        let update_num = if self.extra_opers % 2 == 0 {
            num
        } else {
            num * -1.0
        };

        self.extra_opers = 0;

        update_num
    }
}
