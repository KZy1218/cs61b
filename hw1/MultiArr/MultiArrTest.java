import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(9, MultiArr.maxValue(new int[][] {{1,2,3}, {2,3}, {4,6,9}}));
        assertEquals(-1, MultiArr.maxValue(new int[][] {{-1,-2,-3}, {-4,-6,-9}}));
    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int [] {8,1,26,16}, MultiArr.allRowSums(new int[][] {{1,3,4},{1},{5,6,7,8},{7,9}}));
        assertArrayEquals(new int [] {8,47,19,107}, MultiArr.allRowSums(new int[][] {{1,3,4},{19,28},{5,6,8},{7,100}}));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
