mod eval;
mod expr;
mod parse;
mod token;
mod utils;

// TODO Get the expresssion from stdin. Done.
// TODO Parse the expression. Done.
// TODO Get the post fix expression. Done.
// TODO Evaluate the expression. Done.
// TODO Add parantheses. Done.
// TODO Refactor the code. Done.
// TODO Handle unary operators. Done.
// TODO Split code into different files. Done.
// TODO Fix clippy warnings. Done.
// TODO Improve error handling.

fn main() {
    loop {
        let expr = match utils::get_expr() {
            Ok(expr) => expr,
            Err(error) => {
                eprintln!("Error occurred: {}", error);
                continue;
            }
        };

        let tokens = match parse::parse_expr(&expr) {
            Ok(tokens) => tokens,
            Err(error) => {
                eprintln!("Error occurred: {}", error);
                continue;
            }
        };

        println!("tokens, {:?}", tokens);

        let res = match eval::eval_expr(&tokens) {
            Ok(res) => res,
            Err(error) => {
                eprintln!("Error occurred: {}", error);
                continue;
            }
        };

        println!("res, {:?}", res);
    }
}
