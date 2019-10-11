package Calculator;

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
// TODO add method comments.
// TODO Refactor Evaluator class and parser class.


class Engine {

    private Evaluator evaluator = new Evaluator();
    private CalcParser cParse = new CalcParser();
    private int resultsSaved = 0;
    private String expression;

    private void start() {
        Scanner sc = new Scanner(System.in);

        final String CURRENT_SESSION = "c: ";
        final String NEW_SESSION = "n: ";
        String prompt = NEW_SESSION;

        while (true) {
            System.out.print(prompt);
            expression = sc.nextLine();

            ExpressionMessage statusM = cParse.checkExpression(expression);
            int status = statusM.getStatus();
            expression = statusM.getExpression();
            // System.out.println("Parsed expression: " + expression);

            switch (status) {
                case 0:
                    resultHandler();
                    prompt = CURRENT_SESSION;
                    break;
                case 1:
                    System.exit(0);
                    break;
                case 2:
                    resultsSaved = 0;
                    prompt = NEW_SESSION;
                    break;
                case 3:
                    prompt = CURRENT_SESSION;
                    break;
                case 4:
                    System.out.println(expression);
                    break;
            }

            System.out.println();
        }
    }

    private void resultHandler() {
        String result = evaluator.calcExpr(expression);
        resultsSaved++;
        String resultPrompt = "$" + resultsSaved;
        cParse.addIdentifier(resultPrompt, result);
        System.out.println(resultPrompt + " -> " + result);
    }

    public static void main(String[] args) {
        Engine cEngine = new Engine();
        cEngine.start();
    }
}