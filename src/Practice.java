package Calculator;

import java.math.BigDecimal;

public class Practice {

    public static void main(String[] args) {
        System.out.println(resultCleaner(args[0]));
    }


    private static String resultCleaner(String result) {
        BigDecimal bigResult = new BigDecimal(result);
        return bigResult.stripTrailingZeros().toPlainString();
    }
}
