package gitlet;

import java.text.SimpleDateFormat;
import java.util.Locale;

/** Log command class.
 *  @author Yi Zhang */
public class Log extends Command {

    /** Constructor.
     *  @param args argument for log command*/
    public Log(String... args) {
        _args = args;
    }

    @Override
    public void execute() {
        Commit pointer = headCommit();
        while (true) {
            logPrint(pointer);
            if (pointer.shaID().equals(pointer.par1().shaID())) {
                break;
            } else {
                pointer = pointer.par1();
            }
        }
    }

    /** Print the commit C's information as the indicated format. */
    void logPrint(Commit c) {
        System.out.println("===");
        System.out.format("commit %s\n", c.shaID());
        SimpleDateFormat date = new
                SimpleDateFormat("E MMM dd HH:mm:ss yyyy -0800", Locale.US);
        String time = date.format(c.date());
        System.out.format("Date: %s\n", time);
        System.out.println(c.msg());
        System.out.println();
    }
}
