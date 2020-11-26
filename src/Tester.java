package Calculator;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import static junit.framework.TestCase.assertEquals;

public class Tester {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Tester.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }

    public void test1() {
        // assertEquals(true, Verifier.verify("2+1"));
        // assertEquals(true, Verifier.verify("2+2*(5+6)"));
        // assertEquals(true, Verifier.verify("2+3+4+5+6"));
        // assertEquals(true, Verifier.verify("2!+3^5"));
        // assertEquals(true, Verifier.verify("~5*3+~1"));
        // assertEquals(true, Verifier.verify("4+2*(6+2)/(4-2)/(2*~1)"));
        // assertEquals(true, Verifier.verify("4+2*(6+2)/(4-2)/(2*(65+(3*4)))"));
        // assertEquals(true, Verifier.verify("((1+1)+(2+3))"));
        // assertEquals(true, Verifier.verify("((1+1)+(2+3))"));
        // assertEquals(true, Verifier.verify("(1+3)"));
        // assertEquals(true, Verifier.verify("(3+(3+2))"));
        // assertEquals(true, Verifier.verify("3"));
        //
        // assertEquals(false, Verifier.verify("(2+2"));
        // assertEquals(false, Verifier.verify("2!(3)"));
        // assertEquals(false, Verifier.verify("2**2"));
        // assertEquals(false, Verifier.verify("5+()"));
        // assertEquals(false, Verifier.verify("~~1"));
        // assertEquals(false, Verifier.verify("+3"));
        // assertEquals(false, Verifier.verify(")"));
    }

    @Test
    public void test2() {
        Engine e = new Engine();

        assertEquals("1", e.test("1"));
        assertEquals("122", e.test("5!+2"));
        assertEquals("1091", e.test("5!*(2+7)+11"));
        assertEquals("124", e.test("(1+2)*4+56(2)"));
        assertEquals("99", e.test("1+2+3(2^5)"));
        assertEquals("NA", e.test("9890***11"));
        assertEquals("-71", e.test("2^3+1-3*6-11*6+2^2"));
        // assertEquals("NA", e.run("1 2 3"));
    }
}