package enigma;

import java.util.ArrayList;
import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Yi Zhang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            ArrayList<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _activeRotor = new Rotor[numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _allRotors) {
                if (r.name().equals(rotors[i])) {
                    if ((!r.reflecting()) && (i == 0)) {
                        throw new EnigmaException("The "
                                + "first rotor must be reflector");
                    }
                    if ((r.rotates())
                            && (0 < i)
                            && (i < _numRotors - _pawls)) {
                        throw new EnigmaException("The "
                                + "rotor between reflector and "
                                + "moving rotors must be fixed");
                    }
                    _activeRotor[i] = r;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("The setting is illegal "
                        + "(contains characters"
                        + "that are not in alphabet)");
            } else {
                _activeRotor[i + 1].set(setting.charAt(i));
            }
        }
    }

    /** Set my rotors's rings according to RINGSET, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor's ring (not counting the reflector).  */
    void setRings(String ringset) {
        for (int i = 0; i < ringset.length(); i++) {
            if (!_alphabet.contains(ringset.charAt(i))) {
                throw new EnigmaException("The ring is illegal "
                        + "(contains characters"
                        + "that are not in alphabet)");
            } else {
                _activeRotor[i + 1].setRing(ringset.charAt(i));
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        machineAdvance();
        int n = _numRotors - 1;
        int res = _plugboard.permute(c);
        while (n > -_numRotors) {
            if (n > 0) {
                res = _activeRotor[n].convertForward(res);
            } else if (n == 0) {
                res = _activeRotor[n].permutation().permute(res);
            } else {
                res = _activeRotor[-n].convertBackward(res);
            }
            n -= 1;
        }
        res = _plugboard.permute(res);
        return res;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            int location = _alphabet.toInt(msg.charAt(i));
            res.append(_alphabet.toChar(convert(location)));
        }
        return res.toString();
    }

    /** Advance the rotors in the machine before encrypting. */
    void machineAdvance() {
        int[] rotateId = new int[_pawls];
        for (int i = _numRotors - 1; i >= _numRotors - _pawls; i--) {
            if (_activeRotor[i].atNotch()
                    && (_activeRotor[i - 1].rotates())) {
                rotateId[i - _numRotors + _pawls] += 1;
                rotateId[i - _numRotors + _pawls - 1] += 1;
            } else {
                if (i == _numRotors - 1) {
                    rotateId[i - _numRotors + _pawls] += 1;
                }
            }
        }

        for (int i = 0; i < rotateId.length; i++) {
            if (rotateId[i] > 0) {
                _activeRotor[i + _numRotors - _pawls].advance();
            }
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of all rotors. */
    private int _numRotors;

    /** The number of pawls. i.e. The number of moving rotors. */
    private int _pawls;

    /** The collection of rotors in this machine. */
    private ArrayList<Rotor> _allRotors;

    /** The plugboard of this machine. */
    private Permutation _plugboard;

    /** The set of active rotors. */
    private Rotor[] _activeRotor;


}
