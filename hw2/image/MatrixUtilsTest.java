package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class MatrixUtilsTest {
    /** Test for functions in MatrixUtils
     */

    @Test
    public void accumulateVerticalTest() {
        double[][] matrix = new double[][] {
                {1000000,1000000,1000000,1000000},
                {1000000,75990,30003,1000000},
                {1000000,30002,103046,1000000},
                {1000000,29515,38273,1000000},
                {1000000,73403,35399,1000000},
                {1000000,1000000,1000000,1000000}
        };
        double[][] am1 = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        assertEquals(am1,MatrixUtils.accumulateVertical(matrix));
    }

    @Test
    public void accumulateTest() {
        double[][] matrix = new double[][] {
                {1,2,3,4},
                {2,3,4,5},
                {3,4,5,6},
                {4,5,6,7},
        };
        double[][] am2 = new double[][] {
                {1, 3, 6, 10},
                {2, 4, 7, 11},
                {3, 6, 9, 13},
                {4, 8, 12, 16}
        };
        assertEquals(am2,MatrixUtils.accumulate(matrix, MatrixUtils.Orientation.HORIZONTAL));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
