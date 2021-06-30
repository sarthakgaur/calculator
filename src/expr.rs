use crate::eval;
use crate::token::{Operator, OperatorName, Token};
use crate::utils;
use anyhow::bail;
use fehler::throws;

pub struct Expression {
    num_set: bool,
    oper_set: bool,
    extra_opers: usize,
    paren_count: usize,
    tokens: Vec<Token>,
}

impl Expression {
    pub fn new() -> Expression {
        Expression {
            num_set: false,
            oper_set: false,
            extra_opers: 0,
            paren_count: 0,
            tokens: Vec::new(),
        }
    }

    #[throws(anyhow::Error)]
    pub fn add_num(&mut self, num: f64) {
        if !self.num_set {
            self.set_num(num);
        } else if self.oper_set {
            self.set_num(num);
            self.oper_set = false;
        } else {
            bail!("Invalid expression.")
        }
    }

    #[throws(anyhow::Error)]
    pub fn add_operator(&mut self, oper: Operator) {
        if !self.num_set || self.oper_set {
            match oper.name {
                OperatorName::Add => (),
                OperatorName::Subtract => self.extra_opers += 1,
                _ => bail!("Invalid operator position."),
            }
        } else {
            self.oper_set = true;
            self.tokens.push(Token::Operator(oper));
        }
    }

    pub fn handle_extra_ops(&mut self, num: f64) -> f64 {
        let update_num = if self.extra_opers % 2 == 0 {
            num
        } else {
            num * -1.0
        };

        self.extra_opers = 0;

        update_num
    }

    #[throws(anyhow::Error)]
    pub fn handle_paren(&mut self, oper_name: OperatorName) {
        match oper_name {
            OperatorName::OpenParenthesis => self.paren_count += 1,
            OperatorName::CloseParenthesis => self.paren_count -= 1,
            _ => bail!("Not a parenthesis."),
        }
    }

    #[throws(anyhow::Error)]
    pub fn eval(&self) -> f64 {
        if !self.num_set {
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
        self.num_set = true;
        let update_num = self.handle_extra_ops(num);
        self.tokens.push(Token::Number(update_num));
    }
}
