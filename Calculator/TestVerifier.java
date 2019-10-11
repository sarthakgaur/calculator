package Calculator;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.jupiter.api.Assertions.*;

public class TestVerifier {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestVerifier.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }

    @Test
    public void tests() {
        // $("()", true),
        // $("3", true),
        // $("3 + 2", true),
        // $("(3 + (2))", true),
        // $(")", false),
        // $("+3", false),
        // $("3+-2", false),
        // $("(3 + (3 + 2)", false),
        // $("(x + 3)", false)

        assertEquals(true, Verifier.verify("2+1"));
        assertEquals(true, Verifier.verify("2+2*(5+6)"));
        assertEquals(true, Verifier.verify("2+3+4+5+6"));
        assertEquals(true, Verifier.verify("2!+3^5"));
        assertEquals(true, Verifier.verify("~5*3+~1"));
        assertEquals(true, Verifier.verify("4+2*(6+2)/(4-2)/(2*~1)"));
        assertEquals(true, Verifier.verify("4+2*(6+2)/(4-2)/(2*(65+(3*4)))"));
        assertEquals(true, Verifier.verify("((1+1)+(2+3))"));
        assertEquals(true, Verifier.verify("((1+1)+(2+3))"));
        assertEquals(true, Verifier.verify("(1+3)"));
        assertEquals(true, Verifier.verify("(3+(3+2))"));
        assertEquals(true, Verifier.verify("3"));

        assertEquals(false, Verifier.verify("(2+2"));
        assertEquals(false, Verifier.verify("2!(3)"));
        assertEquals(false, Verifier.verify("2**2"));
        assertEquals(false, Verifier.verify("5+()"));
        assertEquals(false, Verifier.verify("~~1"));
        assertEquals(false, Verifier.verify("+3"));
        assertEquals(false, Verifier.verify(")"));
    }
}