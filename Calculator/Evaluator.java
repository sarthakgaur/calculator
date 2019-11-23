package Calculator;

import java.math.BigDecimal;
import java.util.*;

/**
 * Converts the list of token to post-fix form and then evaluates it.
 */
class Evaluator {

    private static final Set<String> validOperators = Set.of("+", "-", "*", "/", "^", "!");

    /**
     * Passes the tokens to getPostFix and then to evaluate.
     * @param tokens list of verified symbols and numbers
     * @return the result of the expression
     */
    static String calculate(List<String> tokens) {
        List<String> postFixList = getPostFix(tokens);
        return evaluate(postFixList);
    }

    /**
     * Converts the list of tokens into the postfix form.
     * @param tokens verified tokens
     * @return list of post-fix tokens
     */
    private static List<String> getPostFix(List<String> tokens) {
        Map<String, Integer> prec = new HashMap<>();
        List<String> postFixList = new ArrayList<>();
        Stack<String> opStack = new Stack<>();

        prec.put("(", 0);
        prec.put("+", 1);
        prec.put("-", 1);
        prec.put("*", 2);
        prec.put("/", 2);
        prec.put("^", 3);
        prec.put("!", 4);

        for (String token : tokens) {

            if (isNumber(token)) {
                postFixList.add(token);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                String topOfStack = opStack.pop();
                while (!opStack.isEmpty() && !topOfStack.equals("(")) {
                    postFixList.add(topOfStack);
                    topOfStack = opStack.pop();
                }
            } else if (prec.containsKey(token)) {
                while (!opStack.isEmpty() && prec.get(opStack.peek()) >= prec.get(token)) {
                    String topOfStack = opStack.pop();
                    postFixList.add(topOfStack);
                }
                opStack.push(token);
            }
        }

        while (!opStack.isEmpty()) {
            String topOfStack = opStack.pop();
            postFixList.add(topOfStack);
        }

        // System.out.println("postFix: " + postFixList.toString());
        return postFixList;
    }

    /**
     * Evaluates the postfix tokens.
     * @param postFixTokens list of tokens in the post-fix form
     * @return the result after evaluating the tokens
     */
    private static String evaluate(List<String> postFixTokens) {
        Stack<String> evalStack = new Stack<>();

        for (String token : postFixTokens) {
            if (isNumber(token)) {
                evalStack.push(token);
            } else if (validOperators.contains(token)) {
                String res;
                if (token.equals("!")) {
                    String n1 = evalStack.pop();
                    res = calculate(n1, token, "0");
                } else {
                    String n2 = evalStack.pop();
                    String n1 = evalStack.pop();
                    res = calculate(n1, token, n2);
                }
                evalStack.push(res);
            }
        }

        String result = evalStack.pop();
        return resultCleaner(result);
    }

    /**
     * Performs the operation on the given numbers.
     * @param x first number
     * @param operator the operator
     * @param y second number
     * @return result of the expression
     */
    private static String calculate(String x, String operator, String y) {
        Double a = Double.valueOf(x);
        Double b = Double.valueOf(y);;

        switch (operator) {
            case "+":
                a += b;
                break;
            case "-":
                a -= b;
                break;
            case "*":
                a *= b;
                break;
            case "/":
                a /= b;
                break;
            case "^":
                a = Math.pow(a, b);
                break;
            case "!":
                a = factorial(a);
                break;
        }

        return a.toString();
    }

    /**
     * Calculates the factorial of a number.
     * @param n a number
     * @return a double result
     */
    private static double factorial(double n) {
        double fact = 1;
        while (n > 1) {
            fact *= n;
            n--;
        }
        return fact;
    }

    /**
     * Strips out all the trailing zeros from the result.
     * @param result the result of the expression
     * @return a String result
     */
    private static String resultCleaner(String result) {
        BigDecimal bigResult = new BigDecimal(result);
        return bigResult.stripTrailingZeros().toPlainString();
    }

    /**
     * Checks if the String is a number.
     * @param num a String which might contain a number
     * @return true if the String is a number, false otherwise
     */
    static boolean isNumber(String num) {
        try {
            Double.valueOf(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}