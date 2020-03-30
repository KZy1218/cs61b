import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.*;

/** String translation.
 *  @author Yi Zhang
 *
 *
 */
public class Translate {
    /** This method should return the String S, but with all characters that
     *  occur in FROM changed to the corresponding characters in TO.
     *  FROM and TO must have the same length.
     *  NOTE: You must use your TrReader to achieve this. */
    static String translate(String S, String from, String to) {
        /* NOTE: The try {...} catch is a technicality to keep Java happy. */
        char[] buffer = new char[S.length()];
        try {
            TrReader tR = new TrReader(new StringReader(S), from, to);
            tR.read(buffer, 0, S.length());
            S = new String(buffer);
            return S;
        } catch (IOException e) {
            return null;
        }
    }

    @Test
    public void TranslateTest() {
        String s1 = "ABCdef";
        String res1 = "FUCdef";
        assertEquals(res1, translate(s1, "AB", "FU"));

        String s2 = "ababababab";
        String res2 = "dbdbdbdbdb";
        assertEquals(res2, translate(s2, "a", "d"));

        String s3 = "     ";
        String res3 = "XXXXX";
        assertEquals(res3, translate(s3, " ", "X"));
    }


    /*
       REMINDER: translate must
      a. Be non-recursive
      b. Contain only 'new' operations, and ONE other method call, and no
         other kinds of statement (other than return).
      c. Use only the library classes String, and any classes with names
         ending with "Reader" (see online java documentation).
    */
}
