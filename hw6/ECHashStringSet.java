import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Yi Zhang
 */
class ECHashStringSet implements StringSet {

    /** Create empty ECHashStringSet with indicated number of bins. */
    @SuppressWarnings("unchecked")
    public ECHashStringSet(int bins) {
        _count = 0;
        _info = new LinkedList[bins];
        for (int i = 0; i < bins; i++) {
            _info[i] = new LinkedList<String> ();
        }
    }
    /** Create empty ECHashStringSet of minimum bins. */
    public ECHashStringSet() {
        _count = 0;
        _info = new LinkedList[(int) (1 / minLoad)];
        for (int i = 0; i < (1/minLoad); i++) {
            _info[i] = new LinkedList<String> ();
        }
    }

    @Override
    public void put(String s) {
        if (contains(s)) {
            return;
        }
        _count += 1;
        if (_count > _info.length * maxLoad) {
            resize();
        }
        int index = Math.abs(s.hashCode()) % _info.length;
        _info[index].add(s);
    }

    @Override
    public boolean contains(String s) {
        int index = Math.abs(s.hashCode()) % _info.length;
        return _info[index].contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> res = new ArrayList<>();
        for (LinkedList<String> strings : _info) {
            res.addAll(strings);
        }
        return res;
    }

    public void resize() {
        ECHashStringSet temp = new ECHashStringSet(2 * _info.length);
        for (LinkedList<String> strings : _info) {
            for (String s : strings) {
                temp.put(s);
            }
        }
        _info = temp._info;
    }

    /** The maximum load factor. */
    private static final double maxLoad = 5;

    /** The maximum load factor. */
    private static final double minLoad = 0.2;

    /** The information contained in the hash string set. */
    private LinkedList<String> [] _info;

    /** The number of total elements in the set. */
    private int _count;
}
