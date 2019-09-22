import java.util.Scanner;


// Done add identifiers support
// Done bind the result to 'r'
// Done add full identifier support. Where words can be used.
// Done Instead of binding the result with 'r' do it jShell style.
// Done Add git.
// TODO Verify the user input.
// TODO Add more operator support, like '^', '!'.
// TODO add method comments.


public class CalcEngine {

    private Calc calc = new Calc();
    private CalcParser cParse = new CalcParser();
    private int resultsSaved = 0;
    private String expression;

    private void start() {
        Scanner sc = new Scanner(System.in);

        final String currentSession = "c: ";
        final String newSession = "n: ";
        String prompt = newSession;

        int status;

        while (true) {
            System.out.print(prompt);
            expression = sc.nextLine();

            ExpressionMessage statusM = cParse.checkExpression(expression);
            status = statusM.getStatus();
            expression = statusM.getExpression();

            switch (status) {
                case 0:
                    resultHandler();
                    prompt = currentSession;
                    break;
                case 1:
                    System.exit(0);
                    break;
                case 2:
                    resultsSaved = 0;
                    prompt = newSession;
                    break;
                case 3:
                    prompt = currentSession;
                    break;
            }

            System.out.println();
        }
    }

    private void resultHandler() {
        String result = calc.calcExpr(expression);
        resultsSaved++;
        String resultPrompt = "$" + resultsSaved;
        cParse.addIdentifier(resultPrompt, result);
        System.out.println(resultPrompt + " -> " + result);
    }

    public static void main(String[] args) {
        CalcEngine cEngine = new CalcEngine();
        cEngine.start();
    }
}