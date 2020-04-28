package gitlet;

import java.util.List;

import static gitlet.Utils.*;

/** RmBranch command class.
 *  @author Yi Zhang */
public class RmBranch extends Command {

    /** The class constructor. Pass ARGS to _args. */
    RmBranch(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        join(".gitlet/refs", _args[1]).delete();
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
        List<String> branchName = plainFilenamesIn(join(".gitlet/refs"));
        if (!branchName.contains(args[1])) {
            System.out.println("A branch with that name does not exist.");
            return false;
        }
        String current = readContentsAsString(
                join(".gitlet/refs/HEAD/"));
        if (current.equals(args[1])) {
            System.out.println("Cannot remove the current branch.");
            return false;
        }
        return true;
    }

}
