import com.google.common.base.Strings;

import java.awt.*;
import java.util.Arrays;

/** Multidimensional array
 *  @author Yi Zhang
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        System.out.println("Rows: " + arr.length);
        int column = 0;
        for (int[] i : arr) {
            if (i.length >= column) {
                column = i.length;
            }
        }
        System.out.println("Columns: " + column);
    }

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int max = arr[0][0];
        for (int[] ls : arr){
            Arrays.sort(ls);
            if (ls[ls.length-1] > max){
                max = ls[ls.length-1];
            }
        }
        return max;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length ; i += 1) {
            int sum = 0;
            for (int j : arr[i]) {
                sum += j;
            }
            result[i] = sum;
        }
        return result;
    }

    public static void main(String[] args){
        printRowAndCol(new int[][] {{1,3,4},{1},{5,6,7,8},{7,9}});
//        printRowAndCol(new int[][] {{1,3,4},{1},{5,6,10,102,7,8},{7,9}});
//        printRowAndCol(new String[][] {{"1","3","4"}});

        System.out.println(maxValue(new int[][] {{1,3,4},{1},{5,6,7,8},{7,9}}));

    }



}