package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import static gitlet.Utils.*;

/** Command class. A general version of a command. This class contains some
 *  basic useful method that every specific type of command can utilize.
 *  @author Yi Zhang */
public class Command implements Serializable {

    /** The class constructor. Pass ARGS to _args. */
    Command(String... args) {
        _args = args;
    }

    /** Method used to execute the command. */
    public void execute() { }

    /** Method used to check whether a command with
     *  arguments ARGS is valid. If so, return true,
     *  otherwise return false. */
    public boolean check(String... args) {
        if (!initialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return false;
        }
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        }
        return true;
    }

    /** Return true if a gitlet control version system already
     *  exists. Otherwise return false. */
    public boolean initialized() {
        File workDir = new File(".gitlet");
        return workDir.exists();
    }

    /** Return the active head commit. */
    public Commit headCommit() {
        String branch = readContentsAsString(
                join(".gitlet/refs/HEAD/"));
        String headID = readContentsAsString(
                join(".gitlet/refs", branch));
        return readObject(
                join(".gitlet/commits", headID), Commit.class);
    }

    /** Return true if the file NAME is tracked. Otherwise return false. */
    public boolean tracked(String name) {
        Commit head = headCommit();
        HashMap<String, String> blobMap = head.map();
        return blobMap.containsKey(name);
    }

    /** Return true if the file NAME is staged. Otherwise return false. */
    public boolean staged(String name) {
        Stage add = readObject(join(".gitlet/add"),
                Stage.class);
        Stage rm = readObject(join(".gitlet/remove"),
                Stage.class);
        HashMap<String, String> addMap = add.map();
        HashMap<String, String> rmMap = rm.map();
        return (addMap.containsKey(name)
                || rmMap.containsKey(name));
    }

    /** Return true if the file NAME is tracked but changed.
     *  Otherwise return false. */
    public boolean changed(String name) {
        Commit head = headCommit();
        String thisSHA = sha1("blob",
                readContentsAsString(join(name)));
        HashMap<String, String> blobMap = head.map();
        return (blobMap.containsKey(name)
                && !blobMap.get(name).equals(thisSHA));
    }

    /** Return true if the file NAME is tracked but deleted.
     *  Otherwise return false. */
    public boolean trackedDeleted(String name) {
        Commit head = headCommit();
        HashMap<String, String> blopMap = head.map();
        return (blopMap.containsKey(name)
                && !plainFilenamesIn(System.getProperty("user.dir"))
                .contains(name));
    }

    /** Return true if the file NAME is changed but not staged again.
     *  Otherwise return false. This is the case where the file
     *  has been tracked before. */
    public boolean stagedThenChanged(String name) {
        Stage s = readObject(join(".gitlet/add"), Stage.class);
        HashMap<String, String> addMap = s.map();
        String thisSHA = sha1("blob",
                readContentsAsString(join(name)));
        return (addMap.get(name) != null
                && !thisSHA.equals(addMap.get(name)));
    }

    /** The input argument. */
    protected static String[] _args;
}
