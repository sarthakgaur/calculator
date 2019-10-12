package Calculator;

import java.util.ArrayList;
import java.util.Scanner;


// Done add identifiers support
// Done bind the result to 'r'
// Done add full identifier support. Where words can be used.
// Done Instead of binding the result with 'r' do it jShell style.
// Done Add git.
// Done Add more operator support, like '^', '!'.
// Done Use improved regex (\$[\w]+)|([a-zA-Z]+[\w]*)
// Done add parentheses checker
// Done Verify the user input.
// Done Refactor Evaluator class and parser class.
// TODO add method comments.


class Engine {

    private Identifiers identifiers = new Identifiers();
    private Checker checker = new Checker(identifiers);
    private int resultCount = 0;
    private String expression;

    private void start() {
        Scanner sc = new Scanner(System.in);

        final String CURRENT_SESSION = "c: ";
        final String NEW_SESSION = "n: ";
        String prompt = NEW_SESSION;

        while (true) {
            System.out.print(prompt);
            expression = sc.nextLine();

            Message message = checker.check(expression);
            int status = message.getStatus();
            String text = message.getText();
            ArrayList<String> tokens = message.getTokens();
            // System.out.println("tokens: " + tokens.toString());

            switch (status) {
                case 0:
                    resultHandler(tokens);
                    prompt = CURRENT_SESSION;
                    break;
                case 1:
                    System.exit(0);
                    break;
                case 2:
                    resultCount = 0;
                    prompt = NEW_SESSION;
                    break;
                case 3:
                    prompt = CURRENT_SESSION;
                    break;
                case 4:
                    System.out.println(text);
                    break;
            }

            System.out.println();
        }
    }

    private void resultHandler(ArrayList<String> tokens) {
        String result = Evaluator.calculate(tokens);
        resultCount++;
        String resultPrompt = "$" + resultCount;
        identifiers.add(resultPrompt, result);
        System.out.println(resultPrompt + " -> " + result);
    }

    String run(String expression) {
        Message message = checker.check(expression);
        ArrayList<String> tokens = message.getTokens();
        if (tokens == null) {
            return "NA";
        }
        return Evaluator.calculate(tokens);
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();
    }
}