package gitlet;

import java.util.List;

import static gitlet.Utils.*;
import static gitlet.Utils.sha1;

/** Reset command class.
 *  @author Yi Zhang */
public class Reset extends Command {

    /** Constructor.
     *  @param args argument for log command*/
    public Reset(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String commitID = _args[1];
        Commit givenC = readObject(join(".gitlet/commits", commitID),
                Commit.class);
        for (String name : givenC.map().keySet()) {
            if (givenC.map().containsKey(name)) {
                Command checkout = new Checkout(
                        "checkout", commitID, "--", name);
                checkout.execute();
            }
        }
        for (String name : plainFilenamesIn(System.getProperty
                ("user.dir"))) {
            if (!givenC.map().containsKey(name)) {
                join(name).delete();
            }
        }
        String branch = readContentsAsString(
                join(".gitlet/refs/HEAD/"));
        writeContents(join(".gitlet/refs", branch), commitID);
        writeObject(join(".gitlet/add"), new Stage());
        writeObject(join(".gitlet/remove"), new Stage());
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
        String id = args[1];
        List<String> allID = plainFilenamesIn(
                join(".gitlet/commits"));
        if (!allID.contains(id)) {
            System.out.println("No commit with that id exists.");
            return false;
        } else {
            Commit givenC = readObject(join(".gitlet/commits", id),
                    Commit.class);
            Commit currentHead = headCommit();
            for (String name : plainFilenamesIn(
                    System.getProperty("user.dir"))) {
                String shaID = sha1("blob", readContentsAsString(join(name)));
                if ((!currentHead.map().containsKey(name)
                        && givenC.map().containsKey(name))
                        && (!givenC.map().get(name).equals(shaID))) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    System.exit(0);
                    return false;
                }
            }
        }
        return true;
    }
}
