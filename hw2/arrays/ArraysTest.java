package arrays;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** Test the function in Array
     */

    @Test
    public void catenateTest(){
        assertArrayEquals(new int[] {1,2,3,4,5,6}, Arrays.catenate(new int[] {1,2,3}, new int[] {4,5,6}));
        assertArrayEquals(new int[] {4,5,6}, Arrays.catenate(null, new int[] {4,5,6}));
        assertArrayEquals(new int[] {1,2,3}, Arrays.catenate(new int[] {1,2,3}, null));
    }

    @Test
    public void removeTest() {
        int[] A = {1,2,3,4,5,6};
        assertArrayEquals(new int[] {}, Arrays.remove(A,0,10));
        assertArrayEquals(new int[] {3,4,5,6}, Arrays.remove(A,0,2));
        assertArrayEquals(new int[] {1,2,3}, Arrays.remove(A,3,3));
        assertArrayEquals(new int[] {1,2,5,6}, Arrays.remove(A,2,2));
        assertArrayEquals(new int[] {1,2,3,4,5,6}, Arrays.remove(A,3,0));
    }

    @Test
    public void naturalRunTest() {
        int[] A = new int[] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        assertEquals(new int[][] {{1,3,7}, {5}, {4,6,9,10}, {10,11}}, Arrays.naturalRuns(A));

        int[] B = new int[] {1, 3, 7, 9, 10, 11};
        assertEquals(new int[][] {{1,3,7,9,10,11}}, Arrays.naturalRuns(B));

        int[] C = new int[] {1,1,1,1,1};
        assertEquals(new int[][] {{1},{1},{1},{1},{1}}, Arrays.naturalRuns(C));

        int[] D = new int[] {};
        assertEquals(new int[][] {{}}, Arrays.naturalRuns(D));
    }



    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
