package gitlet;

import java.util.List;
import static gitlet.Utils.*;

/** Add remote command class.
 *  @author Yi Zhang */
public class AddRemote extends Command {

    /** Constructor.
     *  @param args argument for the add-remote command. */
    AddRemote(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String remoteName = _args[1];
        String dir = _args[2];
        writeContents(join(".gitlet/remotes", remoteName), dir);
    }

    @Override
    public boolean check(String... args) {
        String remoteName = args[1];
        List<String> remoteNames = plainFilenamesIn(join(".gitlet/remotes"));
        if (remoteNames.contains(remoteName)) {
            System.out.println("A remote with that name already exists.");
            return false;
        }
        return true;
    }

}
