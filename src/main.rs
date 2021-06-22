mod parse;
mod token;
mod expr;
mod eval;
mod utils;

// TODO Get the expresssion from stdin. Done.
// TODO Parse the expression. Done.
// TODO Get the post fix expression. Done.
// TODO Evaluate the expression. Done.
// TODO Add parantheses. Done.
// TODO Refactor the code. Done.
// TODO Handle unary operators. Done.
// TODO Split code into different files. Done.
// TODO Fix clippy warnings.

fn main() {
    loop {
        let expr = utils::get_expr();
        println!("You entered the expression, {:?}", expr);
        let tokens = parse::parse_expr(&expr);
        println!("tokens, {:?}", tokens);
        let res = eval::eval_expr(&tokens);

        println!("res, {:?}", res);
    }
}
