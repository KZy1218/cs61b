public class hw0{
    /**  Run some tests in main method */
    public static void main(String[] args) {
        System.out.println(max_for_loop(new int[] {-2,-23,214,599,13}));
        System.out.println(max_while_loop(new int[] {-9,0,12,43}));

        System.out.println(threeSum(new int [] {-1,1,2}));
        System.out.println(threeSum(new int [] {0,1,12,-31}));
        System.out.println(threeSum(new int [] {-6,2,5}));

        System.out.println(threeSumDistinct(new int [] {8,-1,-1,2,15}));
        System.out.println(threeSumDistinct(new int[] {5,1,0,3,6}));
    }

    /** max method using for loop */
    public static int max_for_loop(int[] a) {
        if (a.length == 0){
            return 0;
        }
        int m = a[0];
        for (int i=1; i < a.length; i += 1) {
            if (a[i] > m) {
                m = a[i];
            }
        }
        return m;
    }

    /** max method using while loop */  
    public static int max_while_loop(int[] a) {
        if (a.length == 0){
            return 0;
        } 
        int i = 0;
        int m = a[0];
        while (i < a.length){
            if (a[i] > m){
                m = a[i];
            }
            i += 1;
        }
        return m;
    }

    /** 3SUM that allows repeatedly choosing a number from a list */
    public static boolean threeSum(int[] a) {
        for (int i = 0; i < a.length; i += 1){
            for (int j = i; j < a.length; j += 1){
                for (int k = j; k < a.length; k += 1){
                    if (a[i] + a[j] + a[k] == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** 3SUM that requires to choose numbers distinctively */
    public static boolean threeSumDistinct(int[] a){
        for (int i = 0; i < a.length; i += 1){
            for (int j = i+1; j < a.length; j += 1){
                for (int k = j+1; k < a.length; k += 1){
                    if (a[i] + a[j] + a[k] == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}