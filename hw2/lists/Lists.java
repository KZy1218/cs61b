package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

import com.sun.codemodel.internal.JArray;

/** List problem.
 *  @author Yi Zhang
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        if (L.tail == null) {
            return new IntListList(L, null);
        }
        else {
            IntList store = L;
            while (L.tail.head > L.head) {
                L = L.tail;
                if (L.tail == null) {
                    return new IntListList(store, null);
                }
            }
            IntList help = L;
            L = L.tail;
            help.tail = null;
            return new IntListList(store, naturalRuns(L));
        }
    }

/** some sanity check */
    public static void main(String[] args) {
        IntList L1 = IntList.list(new int[] {1, 3, 7, 5, 4, 6, 9, 10, 10, 11});
        IntListList res1 = naturalRuns(L1);
        System.out.println(res1.equals(new int[][] {{1,3,7}, {5}, {4,6,9,10}, {10,11}}));

        IntList L2 = IntList.list(new int[] {1});
        IntListList res2 = naturalRuns(L2);
        System.out.println(res2.equals(new int[][] {{1}}));

        IntList L3 = IntList.list(new int[] {3,3,3});
        IntListList res3 = naturalRuns(L3);
        System.out.println(res3.equals(new int[][] {{3}, {3}, {3}}));

        IntList L4 = IntList.list(new int[] {11, 9, 4, 6, 9, 10, 10, 1, 0});
        IntListList res4 = naturalRuns(L4);
        System.out.println(res4.equals(new int[][] {{11}, {9}, {4,6,9,10}, {10}, {1}, {0}}));

    }

}
