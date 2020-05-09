package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static gitlet.Utils.*;

/** Push command class.
 *  @author Yi Zhang */
public class Push extends Command {

    /** Constructor.
     *  @param args argument for the push command. */
    Push(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String remoteName = _args[1];
        String branchName = _args[2];
        String dir = readContentsAsString(join(".gitlet/remotes",
                remoteName));
        Commit local = headCommit();
        List<String> localCommits = plainFilenamesIn(join(".gitlet/commits"));
        List<String> remoteCommits = plainFilenamesIn(join(dir, "commits"));
        for (String name : localCommits) {
            if (remoteCommits == null
                    || !remoteCommits.contains(name)) {
                try {
                    var source = join(".gitlet/commits", name);
                    var dest = join(dir, "commits", name);
                    Files.copy(source.toPath(), dest.toPath());
                } catch (IOException excp) {
                    throw new IllegalArgumentException(excp.getMessage());
                }
            }
        }
        writeObject(join(dir, "commits", local.shaID()), local);
        writeContents(join(dir, "refs/HEAD"), branchName);
        writeContents(join(dir, "refs", branchName), local.shaID());
    }


    @Override
    public boolean check(String... args) {
        String remoteName = args[1];
        String branchName = args[2];
        String dir = readContentsAsString(join(".gitlet/remotes",
                remoteName));
        ArrayList<String> history = new ArrayList<>();
        Commit pointerC = headCommit();
        while (!pointerC.msg().equals("initial commit")) {
            history.add(pointerC.shaID());
            pointerC = pointerC.par1();
        }
        history.add(pointerC.shaID());

        if (!join(dir).exists()) {
            System.out.println("Remote directory not found.");
            return false;
        }
        Commit remoteHead;
        File branchRef = join(dir, "refs", branchName);
        if (branchRef.exists()) {
            String headID = readContentsAsString(branchRef);
            remoteHead = readObject(join(dir, "commits", headID),
                    Commit.class);
            if (!history.contains(remoteHead.shaID())) {
                System.out.println("Please pull down remote changes "
                        + "before pushing.");
                return false;
            }
        } else {
            System.out.println("Please pull down remote changes "
                    + "before pushing.");
            return false;
        }
        return true;
    }

}
