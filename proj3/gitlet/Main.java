package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Yi Zhang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        CommandCreator creator = new CommandCreator();
        Command cmnd = creator.create(args);
        if (cmnd.check(args)) {
            cmnd.execute();
        }
    }
}
