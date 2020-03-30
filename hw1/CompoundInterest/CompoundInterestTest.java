import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(9, CompoundInterest.numYears(2029));
        assertEquals(7, CompoundInterest.numYears(2027));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(11.2, CompoundInterest.futureValue(10,12,2021), tolerance);
        assertEquals(121, CompoundInterest.futureValue(100,10,2022), tolerance);
        assertEquals(72.9, CompoundInterest.futureValue(100,-10,2023), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(1214767.76, CompoundInterest.futureValueReal(1000000,10,2023,3), tolerance);
        assertEquals(10852.67, CompoundInterest.futureValueReal(10000,7,2025,5), tolerance);

    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(464.1, CompoundInterest.totalSavings(100, 2023, 10), tolerance);
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2022, 10), tolerance);;
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(14936.37, CompoundInterest.totalSavingsReal(5000, 2022, 10, 5), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
