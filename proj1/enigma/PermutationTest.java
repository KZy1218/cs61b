package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Yi Zhang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);

        alpha = "ABCDEFXK";
        perm = new Permutation("(ACB)(D)(F)(KXE)", new Alphabet(alpha));
        checkPerm("null", "ABCDEFXK", "CABDKFEX");

        alpha = "UYSA87xckj";
        perm = new Permutation("(x7Y)  (c) (Uj)", new Alphabet(alpha));
        checkPerm("no message", "kU7xcASY8j", "kjY7cASx8U");
    }

    Permutation p = new Permutation("(ACB)(D)(F)(KXE)",
            new Alphabet("ABCDEFXK"));

    @Test
    public void permuteCharTest() {
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('F', p.permute('F'));
        assertEquals('X', p.permute('K'));
    }

    @Test
    public void permuteIntTest() {
        assertEquals(2, p.permute(0));
        assertEquals(3, p.permute(3));
        assertEquals(7, p.permute(4));
        assertEquals(4, p.permute(6));
    }

    @Test
    public void invCharTest() {
        assertEquals('E', p.invert('K'));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('D'));
        assertEquals('K', p.invert('X'));
    }

    @Test
    public void invIntTest() {
        assertEquals(2, p.invert(1));
        assertEquals(4, p.invert(7));
        assertEquals(7, p.invert(6));
    }

    @Test
    public void deArrangeTest() {
        Permutation p1 = new Permutation("",
                new Alphabet("ABCD"));
        Permutation p2 = new Permutation("(ACB)(D)(F)(KXE)",
                new Alphabet("ABCDEFXK"));
        Permutation p3 = new Permutation("(ACB)(DFXY)",
                new Alphabet("ABCDFXY"));
        Permutation p4 = new Permutation("(AB)(DFY)", new Alphabet("ABCDFXY"));

        assertFalse("Did not rearrange the alphabet", p1.derangement());
        assertFalse("Did not rearrange the alphabet", p2.derangement());
        assertTrue("Did rearrange the alphabet", p3.derangement());
        assertFalse("Did not rearrange the alphabet", p4.derangement());
    }

}
