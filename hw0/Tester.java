import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author Yi Zhang
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(14, hw0.max_for_loop(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(9,hw0.max_for_loop(new int[] {1,3,5,7,9}));

        assertEquals(14, hw0.max_while_loop(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(9,hw0.max_while_loop(new int[] {1,3,5,7,9}));

        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(hw0.threeSum(new int[] { -6, 3, 10, 200 }));
        assertTrue(hw0.threeSum(new int [] {-1,1,2}));
        assertTrue(hw0.threeSum(new int [] {0,1,12,-31}));

        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertFalse(hw0.threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        assertTrue(hw0.threeSumDistinct(new int[] {8,-4,-4}));
        assertFalse(hw0.threeSumDistinct(new int[] {6,-3,-2}));

        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
