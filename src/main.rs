use std::collections::HashMap;

use fehler::throws;
use rustyline::error::ReadlineError;
use rustyline::Editor;

mod eval;
mod expr;
mod ident;
mod parse;
mod token;
mod utils;

#[throws(anyhow::Error)]
fn main() {
    let mut rl = Editor::<()>::new();
    utils::build_local_calc_dir()?;
    let history_file_path = utils::get_local_calc_dir()?.join("history.txt");
    let _ = rl.load_history(&history_file_path);
    let mut idents: HashMap<String, f64> = HashMap::new();
    let mut res_count = 1;

    loop {
        let readline = rl.readline("> ");

        match readline {
            Ok(expr) => {
                rl.add_history_entry(&expr);

                if expr.contains("=") {
                    match ident::parse_idents(&expr, &mut idents) {
                        Ok(_) => (),
                        Err(error) => eprintln!("Error occurred: {}", error),
                    }
                } else {
                    let mut tokens = match parse::parse_expr(&expr) {
                        Ok(tokens) => tokens,
                        Err(error) => {
                            eprintln!("Error occurred: {}", error);
                            continue;
                        }
                    };

                    match ident::replace_idents(&mut tokens, &idents) {
                        Ok(_) => (),
                        Err(error) => {
                            eprintln!("Error occurred: {}", error);
                            continue;
                        }
                    }

                    match eval::eval_expr(&tokens) {
                        Ok(res) => {
                            let ident = format!("r{}", res_count);
                            idents.insert(ident.clone(), res);
                            res_count += 1;
                            println!("{} => {}", ident, res)
                        }
                        Err(error) => {
                            eprintln!("Error occurred: {}", error);
                            continue;
                        }
                    };
                }
            }

            Err(ReadlineError::Interrupted) => {
                break;
            }

            Err(ReadlineError::Eof) => {
                break;
            }

            Err(err) => {
                println!("Error: {:?}", err);
                break;
            }
        }

        rl.save_history(&history_file_path)?;
    }
}
