package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Yi Zhang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    void advance() {
        set((setting() + 1) % size());
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        if (_notches.length() == 1) {
            return false;
        }
        boolean flag = false;
        for (int i = 1; i < _notches.length(); i++) {
            char x = _notches.charAt(i);
            int n = permutation().alphabet().toInt(x);
            if (setting() == n) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /** The notch of the rotor. */
    private String _notches;

}
