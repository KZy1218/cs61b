package enigma;

import static enigma.EnigmaException.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Class that represents a reflector in the enigma.
 *  @author Yi Zhang
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("The reflector did not "
                    + "rearrange the characters");
        }
        if (!checkValid(perm)) {
            throw new EnigmaException("Invalid cycle format for reflector");
        }
        _setting = 0;
    }

    /** Check whether the permutation P has the correct cycle format.
     *  Return true if it is correct. */
    public boolean checkValid(Permutation p) {
        Pattern pa = Pattern.compile("^([(][^()*,]{2}[)])+$");
        Matcher ma = pa.matcher(p.cycles());
        return ma.matches();
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    boolean reflecting() {
        return true;
    }

    /** The setting of reflector. */
    private int _setting;

}
