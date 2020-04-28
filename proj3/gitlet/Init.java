package gitlet;

import java.io.File;
import java.util.Date;
import java.time.Instant;
import java.util.HashMap;
import static gitlet.Utils.*;

/** Init command class.
 *  @author Yi Zhang */
public class Init extends Command {

    /** Constructor.
     *  @param args argument for log command*/
    public Init(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        File gitletDir = new File(".gitlet");
        File commits = new File(".gitlet/commits");
        File add = new File(".gitlet/add");
        File remove = new File(".gitlet/remove");
        File refs = new File(".gitlet/refs");
        File objects = new File(".gitlet/objects");
        gitletDir.mkdir();
        commits.mkdir();
        refs.mkdir();
        objects.mkdir();

        String msg = "initial commit";
        Date d = new Date(Instant.EPOCH.getEpochSecond());
        Commit initC = new Commit(msg, d, "null",
                "null", new HashMap<>(), "master");
        initC.setPar1(initC.shaID());

        writeObject(join(commits, initC.shaID()), initC);
        writeContents(join(refs, "master"), initC.shaID());
        writeContents(join(refs, "HEAD"), "master");

        Stage addStage = new Stage();
        Stage removeStage = new Stage();
        writeObject(add, addStage);
        writeObject(remove, removeStage);
    }

    @Override
    public boolean check(String... args) {
        File workDir = new File(".gitlet");
        if (args.length != 1) {
            System.out.println("Incorrect operands.");
            System.exit(0);
            return false;
        } else if (workDir.exists()) {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
            return false;
        } else {
            return true;
        }
    }

}
