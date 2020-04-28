package gitlet;
import static gitlet.Utils.*;
import java.io.File;
import java.util.HashMap;

/** Add command class.
 *  @author Yi Zhang */
public class Add extends Command {

    /** Constructor.
     *  @param args argument for the add command. */
    Add(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        Stage add = readObject(join(".gitlet/add"), Stage.class);
        Stage rm = readObject(join(".gitlet/remove"), Stage.class);
        String name = _args[1];
        File f = new File(name);
        String sha = sha1("blob", readContentsAsString(f));

        if (!tracked(name)
                || stagedThenChanged(name)
                || changed(name)) {
            add.put(name, sha);
        }

        if (!this.changed(name) && this.staged(name)) {
            add.remove(name, sha);
        }

        HashMap<String, String> rmMap = rm.map();
        if (rmMap.containsKey(name)) {
            rm.remove(name, sha);
        }

        writeObject(join(".gitlet/add"), add);
        writeObject(join(".gitlet/remove"), rm);
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
        File f = new File(args[1]);
        if (!f.exists()) {
            System.out.println("File does not exist.");
            return false;
        }
        return true;
    }

}
