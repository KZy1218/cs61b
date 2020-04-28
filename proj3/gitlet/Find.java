package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Utils.*;

/** Find command class.
 *  @author Yi Zhang */
public class Find extends Command {

    /** Constructor.
     *  @param args argument for the find command. */
    public Find(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String commitMsg = _args[1];
        File commitFolder = new File(".gitlet/commits");
        List<String> commitIDs = plainFilenamesIn(commitFolder);
        boolean find = false;
        for (String id : commitIDs) {
            Commit c = readObject(join(commitFolder, id), Commit.class);
            if (c.msg().equals(commitMsg)) {
                find = true;
                System.out.println(id);
            }
        }
        if (!find) {
            System.out.println("Found no commit with that message.");
        }
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
        return true;
    }

}
