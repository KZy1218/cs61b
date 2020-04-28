package gitlet;

import java.util.List;

import static gitlet.Utils.*;

/** Merge command class.
 *  @author Yi Zhang */
public class Merge extends Command {

    /** The class constructor. Pass ARGS to _args. */
    Merge(String... args) {
        _args = args;
    }

    @Override
    public void execute() {

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
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        String currentBranch = readContentsAsString(
                join(".gitlet/refs/HEAD/"));
        if (currentBranch.equals(args[1])) {
            System.out.println("Cannot merge a branch with itself.");
            return false;
        }




        return true;

    }

}
