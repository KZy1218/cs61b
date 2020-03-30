package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Yi Zhang
 */

public class ListsTest {

    @Test
    public void NaturalRunsTest() {
        IntList L1 = IntList.list(new int[] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11});
        IntListList res1 = Lists.naturalRuns(L1);

        IntList L2 = IntList.list(new int[] {1});
        IntListList res2 = Lists.naturalRuns(L2);

        IntList L3 = IntList.list(new int[] {3,3,3});
        IntListList res3 = Lists.naturalRuns(L3);

        IntList L4 = IntList.list(new int[] {11, 9, 4, 6, 9, 10, 10, 1, 0});
        IntListList res4 = Lists.naturalRuns(L4);

        assertEquals(res1, IntListList.list(new int[][] {{1,3,7}, {5}, {4,6,9,10}, {10,11}}));
        assertEquals(res2, IntListList.list(new int[][] {{1}}));
        assertEquals(res3, IntListList.list(new int[][] {{3}, {3}, {3}}));
        assertEquals(res4, IntListList.list(new int[][] {{11}, {9}, {4,6,9,10}, {10}, {1}, {0}}));
    }

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
