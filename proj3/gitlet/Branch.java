package gitlet;

import java.util.List;
import static gitlet.Utils.*;

/** Branch command class.
 *  @author Yi Zhang */
public class Branch extends Command {

    /** The class constructor. Pass ARGS to _args. */
    Branch(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String branchName = _args[1];
        String headID = headCommit().shaID();
        writeContents(join(".gitlet/refs", branchName), headID);
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
        if (branchName.contains(args[1])) {
            System.out.println("A branch with that name already exists.");
            return false;
        }
        return true;
    }

}
