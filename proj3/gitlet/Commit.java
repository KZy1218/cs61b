package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import static gitlet.Utils.*;

/** Commit command class.
 *  @author Yi Zhang */
public class Commit extends Command implements Serializable {

    /** Constructor 1.
     *  @param logMsg log message of the commmand
     *  @param d the time this commit was made
     *  @param par1 the sha ID of parent commit 1
     *  @param par2 the sha ID of parent commit 2
     *  @param blobMap the HashMap maps file name to its sha ID
     *  @param branch the branch name. */
    public Commit(String logMsg, Date d, String par1, String par2,
           HashMap<String, String> blobMap, String branch) {
        super(_args);
        _logMsg = logMsg;
        _time = d;
        _par1 = par1;
        _par2 = par2;
        _blobMap = blobMap;
        _branch = branch;
        _shaID = Utils.sha1("commit", _logMsg,
                _par1, _time.toString(), _blobMap.toString());
    }

    /** Constructor 2.
     *  @param args the argument of this command. */
    public Commit(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String branch = readContentsAsString(
                join(".gitlet/refs/HEAD"));
        Commit head = headCommit();
        _logMsg = _args[1];
        _par1 = head.shaID();
        if (_logMsg.split(" ")[0].equals("Merged")) {
            String givenBranch = _logMsg.split(" ")[1];
            String givenID = readContentsAsString(join(
                    ".gitlet/refs", givenBranch));
            Commit p2 = readObject(join(".gitlet/commits", givenID),
                    Commit.class);
            _par2 = p2.shaID();
        } else {
            _par2 = null;
        }
        _branch = branch;
        _time = new java.util.Date();
        HashMap<String, String> parMap = head.map();
        for (String key : parMap.keySet()) {
            put(key, parMap.get(key));
        }
        HashMap<String, String> addMap =
                readObject(join(".gitlet/add"), Stage.class).map();
        HashMap<String, String> rmMap =
                readObject(join(".gitlet/remove"), Stage.class).map();
        for (String name : addMap.keySet()) {
            put(name, addMap.get(name));
            File f = new File(name);
            writeContents(join(".gitlet/objects", addMap.get(name)),
                    readContents(f));
        }
        for (String name : rmMap.keySet()) {
            remove(name, rmMap.get(name));
        }
        _shaID = Utils.sha1("commit", _logMsg,
                _par1, _time.toString(), _blobMap.toString());

        writeObject(join(".gitlet/add"), new Stage());
        writeObject(join(".gitlet/remove"), new Stage());

        writeObject(join(".gitlet/commits", shaID()), this);
        writeContents(join(".gitlet/refs", branch), shaID());
    }

    @Override
    public boolean check(String... args) {
        if (!initialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return false;
        }
        if (args.length == 1 || args[1].length() == 0) {
            System.out.println("Please enter a commit message.");
            return false;
        }
        if (args.length > 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        }
        Stage add = readObject(join(".gitlet/add"), Stage.class);
        Stage rm = readObject(join(".gitlet/remove"), Stage.class);
        if (add.map().isEmpty() && rm.map().isEmpty()) {
            System.out.println("No changes added to the commit.");
            return false;
        }
        return true;
    }

    /** Set the _par1 as PARID. */
    void setPar1(String parID) {
        _par1 = parID;
    }

    /** Put a mapping from FILENAME to its ID into the HashMap. */
    void put(String fileName, String id) {
        _blobMap.put(fileName, id);
    }

    /** Remove a mapping from FILENAME to its ID. */
    void remove(String fileName, String id) {
        _blobMap.remove(fileName, id);
    }

    /** Return the hash value for this commit. */
    String shaID() {
        return _shaID;
    }

    /** Return the HashMap of file to its name. */
    HashMap<String, String> map() {
        return _blobMap;
    }

    /** Return the log message of the commit. */
    String msg() {
        return _logMsg;
    }

    /** Return the branch name of the commit. */
    String branch() {
        return _branch;
    }

    /** Return the date of the commit. */
    Date date() {
        return _time;
    }

    /** Return the parent 1 commit. */
    Commit par1() {
        return readObject(join(".gitlet/commits", _par1),
                Commit.class);
    }

    /** Return the parent 2 commit. */
    Commit par2() {
        if (_par2 != null) {
            return readObject(join(".gitlet/commits", _par2),
                    Commit.class);
        } else {
            return null;
        }
    }

    /** Log message. */
    private String _logMsg;

    /** Timestamp. */
    private Date _time;

    /** Parent commit 1's SHA-1 ID. */
    private String _par1;

    /** Parent commit 2's SHA-1 ID. */
    private String _par2;

    /** The HashMap from a file to its reference. */
    private HashMap<String, String> _blobMap = new HashMap<>();

    /** The Hash value of this commit. */
    private String _shaID;

    /** The branch name of this commit. */
    private String _branch;

}
