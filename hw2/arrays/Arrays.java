package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Yi Zhang
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        if ((A == null) && (B == null)) {
            return null;
        }
        else if (A == null) {
            return B;
        }
        else if (B == null) {
            return A;
        }
        else {
            int[] C = new int[A.length + B.length];
            for (int i = 0; i < A.length + B.length; i +=1) {
                if (i < A.length) {
                    C[i] = A[i];
                }
                else {
                    C[i] = B[i - A.length];
                }
            }
            return C;
        }
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        if (len == 0) {
            return A;
        }

        if (start == 0) {
            if (len < A.length) {
                int[] C = new int[A.length-len];
                for (int i = 0; i < A.length - len; i += 1) {
                    C[i] = A[len + i];
                }
                return C;
            }
            else {
                return new int[] {};
            }
        }

        else {
            if (len >= A.length - start) {
                int[] C = new int[start];
                for (int i = 0; i < start; i += 1) {
                    C[i] = A[i];
                }
                return C;
            }
            else {
                int[] C = new int[A.length-len];
                for (int i = 0; i < start; i += 1) {
                    C[i] = A[i];
                }
                for (int i = start; i < A.length-len; i += 1) {
                    C[i] = A[i+len];
                }
                return C;
            }
        }
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int breakpoint =  0;
        for (int i = 0; i < A.length - 1; i += 1) {
            if (A[i+1] <= A[i]) {
                breakpoint += 1;
            }
        }

        if (breakpoint == 0) {
            return new int[][] {A};
        }

        int[] index = new int[breakpoint];
        int x = 0;
        for (int i = 0; i < A.length - 1; i += 1) {
            if (A[i+1] <= A[i]) {
                index[x] = i;
                x += 1;
            }
        }
        int[][] res = new int[breakpoint + 1][];
        for (int i = 0; i < breakpoint + 1; i +=1 ){
            if (i == 0) {
                res[i] = select(A,0,index[i]);
            }
            else if (i == breakpoint) {
                res[i] = select(A, index[breakpoint-1] + 1, A.length - 1);
            }
            else {
                res[i] = select(A,index[i-1] + 1, index[i]);
            }
        }

        return res;
    }

    static int[] select(int[] A, int start, int end) {
        if (start == end) {
            return new int[] {A[start]};
        }
        else {
            int[] res = new int[end - start + 1];
            for (int i = 0; i < end - start + 1; i += 1) {
                res[i] = A[i + start];
            }
            return res;
        }
    }

}
