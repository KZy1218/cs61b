package gitlet;

import java.io.Serializable;
import java.util.HashMap;

/** Stage class. A class that contains the information of files
 *  that are going to be added or deleted.
 *  @author Yi Zhang */
public class Stage implements Serializable {

    /** Constructor for the class that stores files to be staged. */
    Stage() {
        _stageMap = new HashMap<>();
    }

    /** Put FILENAME into the map based on the SHA1 value. */
    void put(String filename, String sha1) {
        _stageMap.put(filename, sha1);
    }

    /** Remove FILENAME from the map based on the SHA1 value. */
    void remove(String filename, String sha1) {
        _stageMap.remove(filename, sha1);
    }

    /** Return the hashmap of the stage. */
    HashMap<String, String> map() {
        return _stageMap;
    }

    /** HashMap for files to be add. */
    private HashMap<String, String> _stageMap;
}
