package Calculator;

import java.math.BigDecimal;
import java.util.*;


class Evaluator {

    private static final Set<String> validOperators = Set.of("+", "-", "*", "/", "^", "!");

    static String calculate(ArrayList<String> tokens) {
        ArrayList<String> postFixList = getPostFix(tokens);
        return evaluate(postFixList);
    }

    private static ArrayList<String> getPostFix(ArrayList<String> tokens) {
        HashMap<String, Integer> prec = new HashMap<>();
        ArrayList<String> postFixList = new ArrayList<>();
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

    private static String evaluate(ArrayList<String> postFixTokens) {
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

    private static double factorial(double n) {
        double fact = 1;
        while (n > 1) {
            fact *= n;
            n--;
        }
        return fact;
    }

    private static String resultCleaner(String result) {
        BigDecimal bigResult = new BigDecimal(result);
        return bigResult.stripTrailingZeros().toPlainString();
    }

    static boolean isNumber(String num) {
        try {
            Double.valueOf(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}