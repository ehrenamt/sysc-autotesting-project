/*
 * QuadraticTest.java
*/
package sysc;


// builtins for redirecting stdout and stdin for the Quadratic main() method
// and for solveQuadratic()
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;


public class QuadraticTest {

    // threshold from Quadratic.java
    private static final double ERROR = 0.00000001;

    // -------------------------------------------------------------------------
    //                          Combinatorial Tests
    // -------------------------------------------------------------------------


    // TODO Imaginary roots
    @ParameterizedTest
    @JsonClasspathSource("solveQuadratic-combinatorial.json")
    public void testCombinatorialSolveQuadratic(
        @org.junitpioneer.jupiter.json.Property("a") double a,
        @org.junitpioneer.jupiter.json.Property("b") double b,
        @org.junitpioneer.jupiter.json.Property("c") double c
    ) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
            PrintStream old = System.out;

            System.setOut(printStream);

            // reason for calling this first is that it will throw for
            // bad values, and we don't need to eror-check the oracle for this
            Quadratic.solveQuadratic(a, b, c);

            double discrmin = b*b - (4 * a * c);

            if (discrmin < 0) {

                assertTrue(true);

            } else {
                double sqrt = Math.sqrt(discrmin);
                double q = (-0.5) * (b + Math.signum(b)*sqrt);
                double expectedx1 = (Double) q / a;
                double expectedx2 = (Double) c / q;

                String formattedx1 = String.valueOf(expectedx1);
                String formattedx2 = String.valueOf(expectedx2);

                double actualx1 = Double.NaN;
                double actualx2 = Double.NaN;

                for (String line : baos.toString().split("\\R")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        double value = Double.parseDouble(parts[1].trim());
                        if (parts[0].trim().equals("x1")) {
                            actualx1 = value;
                        } else if (parts[0].trim().equals("x2")) {
                            actualx2 = value;
                        }
                    }
                }

                if (!Double.isNaN(actualx1)) {
                    boolean x1WithinError =
                        Math.abs(actualx1 - expectedx1) < ERROR ? true : false;

                    // assertTrue(baos.toString().contains(formattedx1));
                    // assertTrue(baos.toString().contains(formattedx2));
                    assertTrue(x1WithinError);
                }

                if (!Double.isNaN(actualx2)) {
                    boolean x2WithinError =
                        Math.abs(actualx2 - expectedx2) < ERROR ? true : false;

                    assertTrue(x2WithinError);
                }
            }

            System.out.flush();
            System.setOut(old);

        } catch (Exception thrownException) {
            assertEquals(
                ArithmeticException.class,
                thrownException.getClass(),
                "Threw incorrect exception or otherwise."
            );
        }

    }
}
