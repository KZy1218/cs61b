package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Yi Zhang
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _ring = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return the ring. */
    int getRing() {
        return _ring;
    }

    /** Set ring to POSN.  */
    void setRing(char posn) {
        _ring = _permutation.alphabet().toInt(posn);
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = _permutation.alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        if ((p < 0) && (p >= size())) {
            throw new EnigmaException("Index out of bound");
        }

        int ringSet;
        if (setting() - getRing() >= 0) {
            ringSet = setting() - getRing();
        } else {
            ringSet = size() + setting() - getRing();
        }

        int contact = p + ringSet;
        int next = _permutation.permute(contact % size());
        if (next - ringSet > 0) {
            return (next - ringSet) % size();
        } else {
            return (size() + next - ringSet) % size();
        }
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        if ((e < 0) && (e >= size())) {
            throw new EnigmaException("Index out of bound");
        }

        int ringSet;
        if (setting() - getRing() >= 0) {
            ringSet = setting() - getRing();
        } else {
            ringSet = size() + setting() - getRing();
        }

        int contact = e + ringSet;
        int next = _permutation.invert(contact % size());
        if (next - ringSet > 0) {
            return (next - ringSet) % size();
        } else {
            return (size() + next - ringSet) % size();
        }
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }


    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current setting of the rotor. */
    private int _setting;

    /** The ring of the rotor. */
    private int _ring;

}
