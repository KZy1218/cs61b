package enigma;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlphabetTests {
    Alphabet normal = new Alphabet();
    Alphabet random = new Alphabet("DAYtomrwNEXh");

    @Test
    public void sizeTest() {
        assertEquals(26, normal.size());
        assertEquals(12, random.size());
    }

    @Test
    public void toCharTest() {
        assertEquals('Z', normal.toChar(25));
        assertEquals('B', normal.toChar(1));
        assertEquals('Y', random.toChar(2));
        assertEquals('h', random.toChar(11));
    }

    @Test
    public void toIntTest() {
        assertEquals(6, normal.toInt('G'));
        assertEquals(24, normal.toInt('Y'));
        assertEquals(4, random.toInt('o'));
        assertEquals(8, random.toInt('N'));
    }

    @Test
    public void containsTest() {
        assertTrue("Should contain L", normal.contains('L'));
        assertFalse("Should not contain L", normal.contains('l'));
        assertTrue("Should contain t", random.contains('t'));
        assertFalse("Should not contain X", random.contains('Z'));
    }

}
