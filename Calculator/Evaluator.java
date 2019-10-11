package Calculator;

import java.math.BigDecimal;
import java.util.*;


class Evaluator {

    String calcExpr(String expression) {
        ArrayList<String> postFixList = getPostFix(expression);
        return evaluate(postFixList);
    }

    private ArrayList<String> getPostFix(String expression) {
        HashMap<Character, Integer> prec = new HashMap<>();
        ArrayList<String> postFixList = new ArrayList<>();
        Stack<Character> opStack = new Stack<>();

        prec.put('(', 0);
        prec.put('+', 1);
        prec.put('-', 1);
        prec.put('*', 2);
        prec.put('/', 2);
        prec.put('^', 3);
        prec.put('!', 4);
        prec.put('~', 5);

        StringBuilder digits = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            Character currentChar = expression.charAt(i);
            // System.out.println("currentChar " + currentChar);

            if (Character.isDigit(currentChar) || currentChar.equals('.')) {
                digits.append(currentChar);
            } else if (!digits.toString().equals("")) {
                postFixList.add(digits.toString());
                digits = new StringBuilder();
            }

            if (currentChar.equals('(')) {
                opStack.push(currentChar);
            } else if (currentChar.equals(')')) {
                Character topOfStack = opStack.pop();

                while (!opStack.isEmpty() && !topOfStack.equals('(')) {
                    postFixList.add(Character.toString(topOfStack));
                    topOfStack = opStack.pop();
                }
            } else if (prec.containsKey(currentChar)) {

                while (!opStack.isEmpty() && prec.get(opStack.peek()) >= prec.get(currentChar)) {
                    Character topOfStack = opStack.pop();
                    postFixList.add(Character.toString(topOfStack));
                }
                opStack.push(currentChar);
            }
        }

        if (digits.length() != 0) {
            postFixList.add(digits.toString());
        }

        while (!opStack.isEmpty()) {
            Character topOfStack = opStack.pop();
            postFixList.add(Character.toString(topOfStack));
        }

        // System.out.println(Arrays.toString(postFixList.toArray()));
        return postFixList;
    }

    private String evaluate(ArrayList<String> postFixList) {
        Stack<String> evalStack = new Stack<>();
        String[] operators = {"+", "-", "*", "/", "^", "!"};
        String res;

        for (String token: postFixList) {
            if (isNumber(token)) {
                evalStack.push(token);
            } else if (Arrays.asList(operators).contains(token)) {
                if (token.equals("!") || token.equals("~")) {
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

    static boolean isNumber(String num) {
        try {
            new BigDecimal(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String resultCleaner(String result) {
        BigDecimal bigResult = new BigDecimal(result);
        return bigResult.stripTrailingZeros().toPlainString();
    }

    private String calculate(String x, String operator, String y) {
        BigDecimal a = new BigDecimal(x);
        BigDecimal b = new BigDecimal(y);

        switch (operator) {
            case "+":
                a = a.add(b);
                break;
            case "-":
                a = a.subtract(b);
                break;
            case "*":
                a = a.multiply(b);
                break;
            case "/":
                a = a.divide(b);
                break;
            case "^":
                a = a.pow(b.intValue());
                break;
            case "!":
                a = factorial(a);
                break;
            case "~":
                a = a.multiply(BigDecimal.valueOf(-1));
                break;
        }

        return a.toString();
    }

    private static BigDecimal factorial(BigDecimal n) {
        BigDecimal fact = BigDecimal.valueOf(1);

        while (n.compareTo(BigDecimal.valueOf(1)) > 0) {
            fact = fact.multiply(n);
            n = n.subtract(BigDecimal.valueOf(1));
        }

        return fact;
    }
}