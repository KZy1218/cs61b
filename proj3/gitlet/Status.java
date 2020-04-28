package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gitlet.Utils.*;

/** Status command class.
 *  @author Yi Zhang */
public class Status extends Command {

    /** Constructor .
     *  @param args the argument of this command. */
    public Status(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        branchSection();

        File addStage = join(".gitlet/add");
        stageSection(addStage, "Staged");
        File rmStage = join(".gitlet/remove");
        stageSection(rmStage, "Removed");

        modifySection();
        untrackedSection();

    }

    /** print branch information in format. */
    void branchSection() {
        String head = readContentsAsString(
                new File(".gitlet/refs/HEAD"));
        List<String> branchName = plainFilenamesIn(join(".gitlet/refs"));
        Collections.sort(branchName);

        System.out.println("=== Branches ===");
        for (String name : branchName) {
            if (name.equals("HEAD")) {
                continue;
            }
            if (name.equals(head)) {
                System.out.println("*" + name);
            } else {
                System.out.println(name);
            }
        }
        System.out.println();
    }

    /** Print stage information contained in file F in format. KEY
     *  is the string that indicates whether this stage is add stage
     *  or remove stage. */
    void stageSection(File f, String key) {
        Stage s = readObject(f, Stage.class);
        List<String> fileName = new ArrayList<>(s.map().keySet());
        Collections.sort(fileName);
        System.out.println("=== " + key + " Files ===");
        for (String id : fileName) {
            System.out.println(id);
        }
        System.out.println();
    }

    /** Print modification but not staged section. */
    void modifySection() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        List<String> fileName =
                plainFilenamesIn(System.getProperty("user.dir"));
        Collections.sort(fileName);
        for (String name : fileName) {
            if ((changed(name) && !staged(name))
                    || stagedThenChanged(name)) {
                System.out.println(name + " (modified)");
            }
        }
        Commit head = headCommit();
        for (String name : head.map().keySet()) {
            boolean deleted = !join(name).exists();
            boolean stageAdd = readObject(join(".gitlet/add"),
                    Stage.class).map().containsKey(name);
            boolean stageRm = readObject(join(".gitlet/remove"),
                    Stage.class).map().containsKey(name);
            if ((stageAdd && deleted)
                    || (!stageRm && trackedDeleted(name))) {
                System.out.println(name + " (deleted)");
            }
        }
        System.out.println();
    }

    /** Print untracked section. */
    void untrackedSection() {
        System.out.println("=== Untracked Files ===");
        List<String> fileName =
                plainFilenamesIn(System.getProperty("user.dir"));
        Collections.sort(fileName);
        for (String name : fileName) {
            boolean stageAdd = readObject(join(".gitlet/add"),
                    Stage.class).map().containsKey(name);
            if (!tracked(name) && !stageAdd) {
                System.out.println(name);
            }
        }
        System.out.println();
    }
}
