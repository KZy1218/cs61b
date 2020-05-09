package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import static gitlet.Utils.*;

/** Checkout command class.
 *  @author Yi Zhang */
public class Checkout extends Command {

    /** Constructor.
     *  @param args argument for log command*/
    public Checkout(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        if (_args.length == 2) {
            String givenID = readContentsAsString(
                    join(".gitlet/refs", _args[1]));
            Commit givenC = readObject(
                    join(".gitlet/commits", givenID), Commit.class);
            HashMap<String, String> givenMap = givenC.map();
            for (String name : givenMap.keySet()) {
                String fileSHA = givenMap.get(name);
                byte[] content = readContents(join(".gitlet/objects", fileSHA));
                writeContents(join(name), content);
            }
            for (String name : headCommit().map().keySet()) {
                if (!givenMap.containsKey(name)) {
                    join(name).delete();
                }
            }
            writeContents(join(".gitlet/refs/HEAD"), _args[1]);
            writeObject(join(".gitlet/add"), new Stage());
            writeObject(join(".gitlet/remove"), new Stage());
        } else if (_args.length == 3) {
            String fileSHA = headCommit().map().get(_args[2]);
            byte[] content = readContents(join(".gitlet/objects", fileSHA));
            writeContents(join(_args[2]), content);
        } else if (_args.length == 4) {
            String id = _args[1];
            String fileName = _args[3];
            List<String> allID = plainFilenamesIn(
                    join(".gitlet/commits"));
            for (String sha : allID) {
                if (sha.equals(id)
                        || id.regionMatches(true, 0, sha, 0, id.length())) {
                    Commit c = readObject(join(".gitlet/commits", sha),
                            Commit.class);
                    File targetVersion = join(".gitlet/objects",
                            c.map().get(fileName));
                    byte[] content = readContents(targetVersion);
                    writeContents(join(fileName), content);
                    break;
                }
            }
        }
    }

    @Override
    public boolean check(String... args) {
        if (!initialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return false;
        } else if (args.length != 2
                && args.length != 3
                && args.length != 4) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        }
        if (args.length == 2 && !typeThree(args)) {
            return false;
        }
        if (args.length == 3 && !typeOne(args)) {
            return false;
        }
        if (args.length == 4 && !typeTwo(args)) {
            return false;
        }
        return true;
    }

    /** Return true if the type 1 checkout command with argument ARGS
     *  is valid. Otherwise return false. */
    private boolean typeOne(String... args) {
        Commit currentHead = headCommit();
        String fileName = args[2];
        if (!args[1].equals("--")) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        }
        if (!currentHead.map().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return false;
        }
        return true;
    }

    /** Return true if the type 2 checkout command with argument ARGS
     *  is valid. Otherwise return false. */
    private boolean typeTwo(String... args) {
        String id = args[1];
        String fileName = args[3];
        List<String> allID = plainFilenamesIn(
                join(".gitlet/commits"));
        if (!args[2].equals("--")) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        }
        boolean flag = false;
        for (String sha : allID) {
            if (sha.equals(id)
                    || id.regionMatches(true, 0, sha, 0, id.length())) {
                flag = true;
                id = sha;
                break;
            }
        }
        if (!flag) {
            System.out.println("No commit with that id exists.");
            return false;
        } else {
            Commit c = readObject(join(".gitlet/commits", id), Commit.class);
            if (!c.map().containsKey(fileName)) {
                System.out.println("File does not exist in that commit.");
                return false;
            }
        }
        return true;
    }

    /** Return true if the type 3 checkout command with argument ARGS
     *  is valid. Otherwise return false. */
    private boolean typeThree(String... args) {
        Commit currentHead = headCommit();
        List<String> branchName = plainFilenamesIn(join(".gitlet/refs"));
        String currentBranch = readContentsAsString(
                join(".gitlet/refs/HEAD/"));
        if (!branchName.contains(args[1])) {
            if (args[1].contains("/")) {
                return true;
            }
            System.out.println("No such branch exists.");
            return false;
        }
        if (currentBranch.equals(args[1])) {
            System.out.println("No need to checkout the current branch.");
            return false;
        }
        String givenID = readContentsAsString(
                join(".gitlet/refs", args[1]));
        Commit given = readObject(
                join(".gitlet/commits", givenID), Commit.class);
        for (String name : plainFilenamesIn(
                System.getProperty("user.dir"))) {
            String shaID = sha1("blob", readContentsAsString(join(name)));
            if ((!currentHead.map().containsKey(name)
                    && given.map().containsKey(name))
                    && (!given.map().get(name).equals(shaID))) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
                return false;
            }
        }
        return true;
    }

}
