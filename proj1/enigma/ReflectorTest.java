package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.HashMap;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Reflector class.
 *  @author Yi Zhang
 */

public class ReflectorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors) {
        rotor = new Reflector(name, new Permutation(rotors.get(name), UPPER));
    }

    /* ***** TESTS ***** */
    @Test
    public void testB() {
        setRotor("B", NAVALA);
        checkRotor("Reflector B", UPPER_STRING, NAVALA_MAP.get("B"));
    }

    @Test
    public void generalTest() {
        Permutation p1 = new Permutation("(SU)  (Ae) (1@) ",
                new Alphabet("Ae1@US"));
        rotor = new Reflector("Random", p1);
        alpha = "Ae1@US";
        checkRotor("Random Reflector", "Ue@1SA", "SA1@Ue");
    }

    @Test
    public void fixedSettingTest() {
        setRotor("C", NAVALB);
        assertFalse("Reflector can't move", rotor.rotates());
        rotor.advance();
        assertEquals("Reflector only has 0 setting",
                0, rotor.setting());
    }

    @Test (expected = EnigmaException.class)
    public void buggyTest() {
        Permutation p2 = new Permutation("(ABC)(EF)", new Alphabet("ABCEF"));
        rotor = new Reflector("Buggy", p2);
        alpha = "ABCEF";
        checkRotor("Buggy Reflector", "CFABE", "AEBCF");
    }

    @Test (expected = EnigmaException.class)
    public void buggyTest2() {
        Permutation p3 = new Permutation("(A) (BC)", new Alphabet("BCA"));
        rotor = new Reflector("Buggy2", p3);
        alpha = "BCA";
        checkRotor("Buggy Reflector", "CBA", "BCA");
    }

}











