use std::collections::HashMap;

use anyhow::bail;
use fehler::throws;

use crate::token::Token;

#[throws(anyhow::Error)]
pub fn parse_idents(expr: &str, idents: &mut HashMap<String, f64>) {
    let assignments = expr.split(',');

    for assignment in assignments {
        let (ident, num) = parse_assignment(assignment)?;
        idents.insert(ident.to_owned(), num);
    }
}

#[throws(anyhow::Error)]
pub fn replace_idents(tokens: &mut [Token], idents: &HashMap<String, f64>) {
    let mut i = 0;

    while i < tokens.len() {
        if let Token::Identifier(ident) = &tokens[i] {
            let num = idents.get(ident);

            let token = match num {
                Some(v) => Token::Number(*v),
                _ => bail!("Invalid identifier."),
            };

            tokens[i] = token;
        }

        i += 1;
    }
}

#[throws(anyhow::Error)]
fn parse_assignment(assignment: &str) -> (&str, f64) {
    let split = assignment.split('=');
    let tokens: Vec<&str> = split.collect();

    if tokens.len() != 2 {
        bail!("Invalid use of assignment");
    }

    let ident = tokens[0];

    for (i, ch) in ident.chars().enumerate() {
        if i == 0 {
            if !ch.is_alphabetic() {
                bail!("Ident should start with a letter.")
            }
        } else if !ch.is_alphanumeric() {
            bail!("Invalid identifier.")
        }
    }

    let num_res = tokens[1].parse::<f64>();

    let num = match num_res {
        Ok(n) => n,
        _ => bail!("Invalid use of assignment"),
    };

    (ident, num)
}
