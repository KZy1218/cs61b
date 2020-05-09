package gitlet;

import java.util.List;
import static gitlet.Utils.join;
import static gitlet.Utils.plainFilenamesIn;

/** Remove remote command class.
 *  @author Yi Zhang */
public class RmRemote extends Command {

    /** Constructor.
     *  @param args argument for the rm-remote command. */
    RmRemote(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        join(".gitlet/remotes", _args[1]).delete();
    }

    @Override
    public boolean check(String... args) {
        String remoteName = args[1];
        List<String> remoteNames = plainFilenamesIn(join(".gitlet/remotes"));
        if (!remoteNames.contains(remoteName)) {
            System.out.println("A remote with that name does not exist.");
            return false;
        }
        return true;
    }


}
