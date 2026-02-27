package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    public void setUp() {
        calc = new Calculator();
    }

    // --- Tests for Square Root (√x) ---
    @Test
    public void testSquareRootPositive() {
        assertEquals(4.0, calc.squareRoot(16.0), 0.001);
        assertEquals(5.0, calc.squareRoot(25.0), 0.001);
    }

    @Test
    public void testSquareRootNegative() {
        assertThrows(IllegalArgumentException.class, () -> calc.squareRoot(-4.0));
    }

    // --- Tests for Factorial (x!) ---
    @Test
    public void testFactorialPositive() {
        assertEquals(120, calc.factorial(5));
        assertEquals(1, calc.factorial(0)); // 0! is 1
    }

    @Test
    public void testFactorialNegative() {
        assertThrows(IllegalArgumentException.class, () -> calc.factorial(-5));
    }

    // --- Tests for Natural Logarithm (ln(x)) ---
    @Test
    public void testNaturalLogPositive() {
        assertEquals(1.0, calc.naturalLog(Math.E), 0.001); // ln(e) = 1
        assertEquals(0.0, calc.naturalLog(1.0), 0.001);   // ln(1) = 0
    }

    @Test
    public void testNaturalLogZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> calc.naturalLog(0));
        assertThrows(IllegalArgumentException.class, () -> calc.naturalLog(-10.0));
    }

    // --- Tests for Power Function (x^b) ---
    @Test
    public void testPower() {
        assertEquals(8.0, calc.power(2.0, 3.0), 0.001); // 2^3 = 8
        assertEquals(1.0, calc.power(5.0, 0.0), 0.001); // 5^0 = 1
        assertEquals(0.25, calc.power(2.0, -2.0), 0.001); // 2^-2 = 0.25
    }
}