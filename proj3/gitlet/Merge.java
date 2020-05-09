package gitlet;

import static gitlet.Utils.*;
import java.util.Stack;
import java.util.List;

/** Merge command class.
 *  @author Yi Zhang */
public class Merge extends Command {

    /** The class constructor. Pass ARGS to _args. */
    Merge(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String givenName = _args[1]; Commit currentHead = headCommit();
        String givenID = readContentsAsString(join(".gitlet/refs", givenName));
        Commit givenC = readObject(
                join(".gitlet/commits", givenID), Commit.class);
        Commit split = splitPt(givenName);
        if (split.shaID().equals(currentHead.shaID())) {
            Command checkout = new Checkout("checkout", givenName);
            checkout.execute();
            System.out.println("Current branch fast-forwarded.");
            return;
        } else if (split.shaID().equals(givenC.shaID())) {
            System.out.println("Given branch is an "
                    + "ancestor of the current branch.");
            return;
        }
        normal(split, currentHead, givenC, givenName);
    }

    @Override
    public boolean check(String... args) {
        if (!initialized()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return false;
        }
        if (args.length != 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        }
        Stage add = readObject(join(".gitlet/add"), Stage.class);
        Stage rm = readObject(join(".gitlet/remove"), Stage.class);
        if (!add.map().isEmpty() || !rm.map().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
            return false;
        }
        List<String> branchName = plainFilenamesIn(join(".gitlet/refs"));
        if (!branchName.contains(args[1])) {
            if (args[1].contains("/")) {
                return true;
            }
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        String currentBranch = readContentsAsString(
                join(".gitlet/refs/HEAD/"));
        if (currentBranch.equals(args[1])) {
            System.out.println("Cannot merge a branch with itself.");
            return false;
        }
        Commit currentHead = headCommit();
        String givenID = readContentsAsString(
                join(".gitlet/refs", _args[1]));
        if (givenID.equals(currentHead.shaID())) {
            System.out.println("Nothing added to Commit.");
            return false;
        }
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

    /** Return the split point (the latest common ancestor) of current branch
     *  and the GIVENBRANCH. */
    private Commit splitPt(String givenBranch) {
        Commit currentHead = headCommit();
        String givenID = readContentsAsString(
                join(".gitlet/refs", givenBranch));
        Commit givenC = readObject(
                join(".gitlet/commits", givenID), Commit.class);
        Stack<String> currentPar = new Stack<>();
        Stack<String> givenPar = new Stack<>();
        while (!currentHead.msg().equals("initial commit")) {
            currentPar.push(currentHead.shaID());
            currentHead = currentHead.par1();
        }
        while (!givenC.msg().equals("initial commit")) {
            givenPar.push(givenC.shaID());
            givenC = givenC.par1();
        }
        String potential;
        if (currentPar.size() == 1 && givenPar.size() == 1
                && !currentPar.peek().equals(givenPar.peek())) {
            potential = currentHead.shaID();
        } else {
            potential = null;
            int l = Math.min(currentPar.size(), givenPar.size());
            int i = 0;
            while (i < l) {
                String cur = currentPar.pop();
                String giv = givenPar.pop();
                if (cur.equals(giv)) {
                    potential = cur;
                    i++;
                } else {
                    break;
                }
            }
        }
        return readObject(join(".gitlet/commits", potential),
                Commit.class);
    }

    /** Execute normal cases of merge based on split point SPLIT,
     *  current commit CURRENTHEAD and given commit GIVENC with
     *  given branch name GIVENNAME. */
    private void normal(Commit split, Commit currentHead,
                        Commit givenC, String givenName) {
        boolean conflict = false;
        for (String name : split.map().keySet()) {
            String splitVer = split.map().get(name);
            boolean currentRm = !currentHead.map().containsKey(name);
            boolean givenRm = !givenC.map().containsKey(name);
            if (currentRm && givenRm) {
                continue;
            }  else if (givenRm) {
                String currentVer = currentHead.map().get(name);
                if (currentVer.equals(splitVer)) {
                    new Remove("rm", name).execute();
                } else {
                    conflict = true;
                    overwrite(name, currentVer, "empty");
                }
            } else if (currentRm) {
                String givenVer = givenC.map().get(name);
                if (!givenVer.equals(splitVer)) {
                    conflict = true;
                    overwrite(name, "empty", givenVer);
                }
            } else {
                String currentVer = currentHead.map().get(name);
                String givenVer = givenC.map().get(name);
                if ((!splitVer.equals(givenVer))
                        && splitVer.equals(currentVer)) {
                    byte[] content = readContents(
                            join(".gitlet/objects", givenVer));
                    writeContents(join(name), content);
                    new Add("add", name).execute();
                }
                boolean bothchanged = !currentVer.equals(givenVer)
                        && !currentVer.equals(splitVer)
                        && !givenVer.equals(splitVer);
                if (bothchanged) {
                    conflict = true;
                    overwrite(name, currentVer, givenVer);
                }
            }
        }
        for (String name : givenC.map().keySet()) {
            if (!split.map().containsKey(name)
                    && !currentHead.map().containsKey(name)) {
                new Checkout("checkout", givenC.shaID(),
                        "--", name).execute();
                new Add("add", name).execute();
            }
        }
        String currentName = readContentsAsString(join(".gitlet/refs/HEAD/"));
        String logMsg = String.format("Merged %s into %s.",
                givenName, currentName);
        new Commit("commit", logMsg).execute();
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Overwrite the contents of file NAME when there is a conflict
     *  based on the version CURRENT and GIVEN. */
    private void overwrite(String name, String current, String given) {
        if (current.equals("empty")) {
            writeContents(join(System.getProperty("user.dir"), name),
                    "<<<<<<< HEAD\n", "=======\n",
                    readContents(join(".gitlet/objects", given)),
                    ">>>>>>>\n");
        } else if (given.equals("empty")) {
            writeContents(join(System.getProperty("user.dir"), name),
                    "<<<<<<< HEAD\n",
                    readContents(join(".gitlet/objects", current)),
                    "=======\n", ">>>>>>>\n");
        } else {
            writeContents(join(System.getProperty("user.dir"), name),
                    "<<<<<<< HEAD\n",
                    readContents(join(".gitlet/objects", current)),
                    "=======\n",
                    readContents(join(".gitlet/objects", given)),
                    ">>>>>>>\n");
        }
        new Add("add", name).execute();
    }


}
