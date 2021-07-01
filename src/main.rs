use fehler::throws;
use rustyline::error::ReadlineError;
use rustyline::Editor;

mod eval;
mod expr;
mod parse;
mod token;
mod utils;

#[throws(anyhow::Error)]
fn main() {
    let mut rl = Editor::<()>::new();
    utils::build_local_calc_dir()?;
    let history_file_path = utils::get_local_calc_dir()?.join("history.txt");
    let _ = rl.load_history(&history_file_path);

    loop {
        let readline = rl.readline("> ");

        match readline {
            Ok(expr) => {
                rl.add_history_entry(&expr);

                let tokens = match parse::parse_expr(&expr) {
                    Ok(tokens) => tokens,
                    Err(error) => {
                        eprintln!("Error occurred: {}", error);
                        continue;
                    }
                };

                match eval::eval_expr(&tokens) {
                    Ok(res) => println!("{}", res),
                    Err(error) => {
                        eprintln!("Error occurred: {}", error);
                        continue;
                    }
                };
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
