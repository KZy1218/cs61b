package gitlet;


/** Pull command class.
 *  @author Yi Zhang */
public class Pull extends Command {

    /** Constructor.
     *  @param args argument for the pull command. */
    Pull(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        String remoteName = _args[1];
        String branchName = _args[2];
        String localName = remoteName + "/" + branchName;
        new Fetch("fetch", remoteName, branchName).execute();
        new Merge("merge", localName).execute();
    }

    @Override
    public boolean check(String... args) {
        Command m = new Merge("merge", args[1], args[2]);
        return m.check("merge", args[1] + "/" + args[2]);
    }

}
