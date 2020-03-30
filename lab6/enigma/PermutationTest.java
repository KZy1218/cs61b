package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Yi Zhang
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void checkSize() {
        Alphabet a1 = getNewAlphabet(UPPER_STRING);
        assertEquals(26, a1.size());

        Alphabet a2 = getNewAlphabet("");
        assertEquals(0, a2.size());

        Alphabet a3 = getNewAlphabet("aoyCXASk");
        assertEquals(8, a3.size());
    }

    @Test
    public void permuteCharTest() {
        Alphabet a1 = getNewAlphabet("AXUIMN");
        Permutation p1 = getNewPermutation("(A)(XUM)(NI)", a1);

        assertEquals('A', p1.permute('A'));
        assertEquals('U', p1.permute('X'));
        assertEquals('N', p1.permute('I'));

        Alphabet a2 = getNewAlphabet("xy*&MNO");
        Permutation p2 = getNewPermutation("(*yx) (NM)    (&)", a2);

        assertEquals('y', p2.permute('*'));
        assertEquals('M', p2.permute('N'));
        assertEquals('&', p2.permute('&'));

    }

    @Test
    public void permuteIntTest() {
        Alphabet a1 = getNewAlphabet("AXUIMN");
        Permutation p1 = getNewPermutation("(A)     (XUM) (NI)", a1);

        assertEquals(0, p1.permute(0));
        assertEquals(2, p1.permute(1));
        assertEquals(5, p1.permute(3));

        Alphabet a2 = getNewAlphabet("xy&MNO");
        Permutation p2 = getNewPermutation("(yx) (NM)    (&)", a2);

        assertEquals(0, p2.permute(1));
        assertEquals(3, p2.permute(4));
        assertEquals(2, p2.permute(2));

        Alphabet a3 = getNewAlphabet();
        Permutation p3 = getNewPermutation("", a3);

        assertEquals(25, p3.permute(25));
        assertEquals(24, p3.permute(24));
    }

    @Test
    public void invertInt() {
        Alphabet a1 = getNewAlphabet("Y");
        Permutation p1 = getNewPermutation("(Y)", a1);

        assertEquals(0, p1.invert(0));

        Alphabet a2 = getNewAlphabet("weY");
        Permutation p2 = getNewPermutation("(Ye) (w)", a2);

        assertEquals(0, p2.invert(0));
        assertEquals(2, p2.invert(1));
        assertEquals(1, p2.invert(2));

        Alphabet a3 = getNewAlphabet();
        Permutation p3 = getNewPermutation("", a3);

        assertEquals(25, p3.invert(25));
        assertEquals(24, p3.invert(24));
    }

    @Test
    public void invertChar() {
        Alphabet a1 = getNewAlphabet("UPE_LOwAr");
        Permutation p1 = getNewPermutation(("(E_A)   (wrP) (UOL)"), a1);
        assertEquals('r', p1.invert('P'));
        assertEquals('O', p1.invert('L'));
        assertEquals('_', p1.invert('A'));
        assertEquals('A', p1.invert('E'));
        assertEquals('O', p1.invert('L'));

        Alphabet a2 = getNewAlphabet();
        Permutation p2 = getNewPermutation("", a2);
        assertEquals('Z', p2.invert('Z'));
        assertEquals('Y', p2.invert('Y'));

    }

    @Test(expected = EnigmaException.class)
    public void specialTest() {
        Alphabet a = getNewAlphabet();
        Permutation p = getNewPermutation("", a);
        assertEquals('Z', p.invert('z'));
    }

    @Test
    public void deArrangeTest() {
        Alphabet a1 = getNewAlphabet();
        Permutation p1 = getNewPermutation("", a1);
        assertFalse("The permutation is not dearranged", p1.derangement());

        Alphabet a2 = getNewAlphabet("ACBD");
        Permutation p2 = getNewPermutation("(DA)(CB)", a2);
        assertTrue("The permutation is dearranged", p2.derangement());
    }
}
