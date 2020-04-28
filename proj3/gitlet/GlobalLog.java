package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Utils.*;

/** Global log command class.
 *  @author Yi Zhang */
public class GlobalLog extends Log {

    /** Constructor 1.
     *  @param args the argument of this command. */
    GlobalLog(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        File commitFolder = new File(".gitlet/commits");
        List<String> commitIDs = plainFilenamesIn(commitFolder);
        assert commitIDs != null;
        for (String id : commitIDs) {
            Commit c = readObject(join(commitFolder, id), Commit.class);
            logPrint(c);
        }
    }


}
