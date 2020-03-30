package enigma;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.HashMap;
import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the FixedRotor class.
 *  @author Yi Zhang
 */

public class FixedRotorTest {

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
     *  specified by the NAME entry in ROTORS. */
    private void setRotor(String name, HashMap<String, String> rotors) {
        rotor = new FixedRotor(name, new Permutation(rotors.get(name), UPPER));

    }

    /* ***** TESTS ***** */
    @Test
    public void testBeta() {
        setRotor("Beta", NAVALA);
        checkRotor("Rotor Beta (A)", UPPER_STRING, NAVALA_MAP.get("Beta"));
    }

    @Test
    public void testGamma() {
        setRotor("Gamma", NAVALB);
        checkRotor("Rotor Beta (A)", UPPER_STRING, NAVALB_MAP.get("Gamma"));
    }

    @Test
    public void fixedTest() {
        setRotor("Beta", NAVALA);
        rotor.set(10);
        rotor.advance();
        assertEquals("Fixed rotor should have only one setting",
                rotor.setting(), 10);

        setRotor("Gamma", NAVALB);
        assertEquals("Default setting of fixed rotor is 0", rotor.setting(), 0);
    }

}
