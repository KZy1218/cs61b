package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static gitlet.Utils.*;

/** Fetch command class.
 *  @author Yi Zhang */
public class Fetch extends Command {

    /** Constructor.
     *  @param args argument for the fetch command. */
    Fetch(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String dir = readContentsAsString(join(".gitlet/remotes",
                _args[1]));
        String remoteID = readContentsAsString(join(dir, "refs",
                _args[2]));
        new File(".gitlet/refs", _args[1]).mkdir();
        String localName = _args[1] + File.separator + _args[2];
        if (!join(".gitlet/refs", localName).exists()) {
            new Branch("branch", localName).execute();
        }
        List<String> remote = plainFilenamesIn(join(dir, "commits"));
        List<String> local = plainFilenamesIn(join(".gitlet/commits"));
        for (String name : remote) {
            if (!local.contains(name)) {
                try {
                    var source = join(dir, "commits", name);
                    var dest = join(".gitlet/commits", name);
                    Files.copy(source.toPath(), dest.toPath());
                } catch (IOException excp) {
                    throw new IllegalArgumentException(excp.getMessage());
                }
            }
        }

        List<String> remoteBlob = plainFilenamesIn(join(dir, "objects"));
        List<String> localBlob = plainFilenamesIn(join(".gitlet/objects"));
        for (String fileName : remoteBlob) {
            if (!localBlob.contains(fileName)) {
                try {
                    var source = join(dir, "objects", fileName);
                    var dest = join(".gitlet/objects", fileName);
                    Files.copy(source.toPath(), dest.toPath());
                } catch (IOException excp) {
                    throw new IllegalArgumentException(excp.getMessage());
                }
            }
        }

        Commit remoteHead = readObject(join(dir, "commits", remoteID),
                Commit.class);
        writeContents(join(".gitlet/refs", localName), remoteHead.shaID());
    }

    @Override
    public boolean check(String... args) {
        String remoteName = args[1];
        String branchName = args[2];
        String dir = readContentsAsString(join(".gitlet/remotes",
                remoteName));
        if (!join(dir).exists()) {
            System.out.println("Remote directory not found.");
            return false;
        } else if (!join(dir, "refs", branchName).exists()) {
            System.out.println("That remote does not have that branch.");
            return false;
        }
        return true;
    }


}
