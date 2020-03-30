import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.util.Collection;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.PrintStream;

public class ScannerExplore {
    public static void main(String[] args) {
        String s = "adsf";
//        ArrayList<String> s2 = new ArrayList<>();
//        s2.add("A");
//        s2.add("B");
//        s2.toArray();
//        System.out.println();


//        Scanner scan = new Scanner(s);
//
//        System.out.println(s);
//
//        while (scan.hasNext()) {
//            String component = scan.next();
//            System.out.println(component);
//            System.out.println(checkValid(component));
//            System.out.println();
//            System.out.println(t);
//        }
//
//        String test = "";
//        System.out.println(test.equals(""));

//        StringBuilder sb = new StringBuilder();
//        sb.append("A");
//        sb.append(System.lineSeparator());
////        sb.append(System.lineSeparator());
//        sb.append("B");
//        System.out.println(sb.toString());
        System.out.println(6/5);
//        System.out.println(s.substring(1,3));
        System.out.println(insertSpace(s));

    }

    public static String insertSpace(String s) {
        String[] pieces = new String[(s.length() / 5) + 1];
        if (s.length() <= 5) {
            return s;
        } else {
            for (int i = 1; i <= (s.length() / 5 + 1); i++) {
                if (5 * i - 1 > s.length()) {
                    pieces[i - 1] = s.substring(5 * (i - 1));
                } else {
                    pieces[i - 1] = s.substring(5 * (i - 1), 5 * i);
                }
            }
        }
        StringBuilder res = new StringBuilder();
        for (String subs: pieces) {
            res.append(subs).append(" ");
        }
        return res.toString();
    }


    public static boolean checkValid(String s) {
        t = 1;
        Pattern Pa = Pattern.compile("^([(][^()*,]+[)])+$");
        Matcher Ma = Pa.matcher(s);
        return Ma.matches();
    }

    private static int t = 0;

}


