package gitlet;

import java.io.File;

import static gitlet.Utils.*;
import static gitlet.Utils.join;

/** Remove command class.
 *  @author Yi Zhang */
public class Remove extends Command {

    /** Constructor.
     *  @param args argument for remove command*/
    Remove(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String fileName = _args[1];
        File f = new File(fileName);
        Commit head = this.headCommit();
        String shaID = sha1("blob", readContentsAsString(f));
        Stage rm = readObject(join(".gitlet/remove"), Stage.class);
        Stage add = readObject(join(".gitlet/add"), Stage.class);
        if (head.map().containsKey(fileName)) {
            rm.put(fileName, shaID);
            writeObject(join(".gitlet/remove"), rm);
            if (f.exists()) {
                f.delete();
            }
        }
        if (add.map().containsKey(fileName)) {
            add.remove(fileName, shaID);
            writeObject(join(".gitlet/add"), add);
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
        Stage add = readObject(join(".gitlet/add"), Stage.class);
        Commit head = headCommit();
        if (!add.map().containsKey(args[1])
                && !head.map().containsKey(args[1])) {
            System.out.println("No reason to remove the file.");
            return false;
        }
        return true;
    }
}
