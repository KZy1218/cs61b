package enigma;

/** The method for checking duplicated characters or rotors.
 *  @author Yi Zhang
 */

public class CheckDuplicate {

    /** Check whether string CH has duplicated characters. If every
     *  character is unique, return true. */
    public static boolean checkUnique(String ch) {
        for (int l = 0; l < ch.length(); l++) {
            int count = 0;
            char target = ch.charAt(l);
            for (int i = 0; i < ch.length(); i++) {
                if (ch.charAt(i) == target) {
                    count += 1;
                }
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Check whether string[] R contains duplicated rotor name. If every
     *  rotor name is unique, return true. */
    public static boolean checkRotor(String[] r) {
        for (String value : r) {
            int count = 0;
            for (String s : r) {
                if (s.equals(value)) {
                    count += 1;
                }
                if (count > 1) {
                    return false;
                }
            }
        }
        return true;
    }

}
