package gitlet;

/** CommandCreator class. Helps to create specific type of command, if such
 *  command does not exist, print error message.
 * @author Yi Zhang */
public class CommandCreator {

    /** Return command with argument ARGS. */
    public Command create(String... args) {
        switch (args[0]) {
        case "init":
            return new Init(args);
        case "add":
            return new Add(args);
        case "commit":
            return new Commit(args);
        case "rm":
            return new Remove(args);
        case "log":
            return new Log(args);
        case "global-log":
            return new GlobalLog(args);
        case "find":
            return new Find(args);
        case "status":
            return new Status(args);
        case "checkout":
            return new Checkout(args);
        case "branch":
            return new Branch(args);
        case "rm-branch":
            return new RmBranch(args);
        case "reset":
            return new Reset(args);
        case "merge":
            return new Merge(args);
        case "add-remote":
            return new AddRemote(args);
        case "rm-remote":
            return new RmRemote(args);
        case "push":
            return new Push(args);
        case "fetch":
            return new Fetch(args);
        case "pull":
            return new Pull(args);
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
            break;
        }
        return null;
    }
}
