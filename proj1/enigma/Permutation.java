package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Yi Zhang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        cycles = cycles.replaceAll("\\s+", "");
        for (int i = 0; i < cycles.length(); i++) {
            char ch = cycles.charAt(i);
            if (!alphabet.contains(ch)
                    && (ch != '(')
                    && (ch != ')')) {
                throw new EnigmaException("Invalid cycle. Input cycle"
                        + " contains character that is not in alphabet");
            }
        }
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i++) {
            if (!_alphabet.contains(cycle.charAt(i))) {
                throw new EnigmaException("Cycle contains invalid characters");
            }
        }
        String answer = "(" + cycle + ")";
        _cycles += answer;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Return the cycle of this permutation. */
    String cycles() {
        return _cycles;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(nextPer(p, false));
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(nextPer(c, true));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return nextPer(_alphabet.toInt(p), false);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return nextPer(_alphabet.toInt(c), true);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        boolean flag = true;
        for (int i = 0; i < _alphabet.size(); i++) {
            char ch = _alphabet.toChar(i);
            if (permute(ch) == ch) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /** Find the next character according to the permutation, X is the
     *  index of the given character in alphabet and INV is true if
     *  we are permute from left to right. Return the next character. */
    char nextPer(int x, boolean inv) {
        assert ((0 <= x) && (x < _alphabet.size()));
        char pAlpha = _alphabet.toChar(x);
        int indexP = _cycles.indexOf(pAlpha);
        if (indexP == -1)  {
            return pAlpha;
        }

        char next = _cycles.charAt(indexP + 1);
        char prev = _cycles.charAt(indexP - 1);
        if ((next == ')') && (prev == '(')) {
            return pAlpha;
        } else if ((prev == '(') && (inv)) {
            int bracket = _cycles.indexOf(')', indexP);
            return _cycles.charAt(bracket - 1);
        } else if ((next == ')') && (!inv)) {
            int bracket = _cycles.lastIndexOf('(', indexP);
            return _cycles.charAt(bracket + 1);
        } else {
            if (inv) {
                return prev;
            } else {
                return next;
            }
        }
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycle of this permutation. */
    private String _cycles;

}
