import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Yi Zhang
 */
public class BSTStringSetTest  {

    @Test
    public void testPut() {
        BSTStringSet bs = new BSTStringSet();
        bs.put("hello");
        bs.put("world");
        bs.put("hey");
        bs.put("you");

    }

    @Test
    public void testContain() {
        BSTStringSet bs = new BSTStringSet();
        bs.put("hey");
        bs.put("haha");
        bs.put("hi");

        assertTrue(bs.contains("hey"));
        assertFalse(bs.contains("ahah"));
    }



}
