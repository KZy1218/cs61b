package enigma;
import static org.junit.Assert.*;
import org.junit.Test;

public class DuplicateTest {

    @Test
    public void checkTest() {
        String s1 = "ABCSIOU";
        assertTrue("The characters are not duplicated",
                CheckDuplicate.checkUnique(s1));

        String s2 = "Afldksjak";
        assertFalse("Some characters are duplicated",
                CheckDuplicate.checkUnique(s2));
    }

    @Test
    public void checkRotorTest() {
        String[] s1 = new String[] {"III", "II", "V"};
        assertTrue("Rotors are not used for multiple times",
                CheckDuplicate.checkRotor(s1));

        String[] s2 = new String[] {"VIII", "II", "I", "II", "IV"};
        assertFalse("Some rotors are used for multiple times",
                CheckDuplicate.checkRotor(s2));
    }

}
