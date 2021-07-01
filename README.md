# calculator
A command line calculator written in Rust.

## Installation
1. `git clone https://github.com/sarthakgaur/calc`
2. `cd calc`
3. `cargo build --release`
4. The executable is located in `target/release`

## Features
1. `+`, `-`, `*`, `/` and `^` operators can be used.
2. Follows [PEMDAS/BODMAS](https://en.wikipedia.org/wiki/Order_of_operations#Mnemonics) order of operations.
3. `(` and `)` can be used to change the order of operations.
4. Users can cycle through their history using "Up" and "Down" arrow keys.
5. Identifiers can be used in expressions.
6. Multiple assignments can be performed in a single line.

## Important
1. The program creates the `calc` directory in `~/.local` to save history.
2. Assignment expression should be written in a seperate line.
3. The result of an expression can be accessed from the identifier specified.
4. An identifier should start with a letter and can be followed with any alphanumeric character.
5. Result identifiers can be overwritten.
6. To exit the program press `ctrl + c` or `ctrl + d`.

## Example Usage
The following session shows the use of assignments, identifiers and different operators:

    $ calc
    > a=1,b=2,c=3
    > a+b+c
    r1 => 6
    > r1-3
    r2 => 3
    > r2*30
    r3 => 90
    > r3/5
    r4 => 18
    > r4^2
    r5 => 324
    > (1+2)*3
    r6 => 9
    >

The program was exited by pressing `ctrl + c`.

