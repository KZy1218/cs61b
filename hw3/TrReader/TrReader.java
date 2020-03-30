import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.io.*;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Yi Zhang
 */

public class TrReader extends Reader {
    /** Create fields for TrReader class */
    private Reader _str;
    private String _from;
    private String _to;

    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        this._str = str;
        this._from = from;
        this._to = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int ReadNum = this._str.read(cbuf, off, len);
        for (int i = off; i < off + len; i += 1) {
            int x = this._from.indexOf(cbuf[i]);
            cbuf[i] = (x == -1) ? cbuf[i] : this._to.charAt(x);
        }
        return Math.min(ReadNum, len);
    }

    @Override
    public void close() throws IOException{
        this._str.close();
    }

    public static void main(String[] args) {

        Reader r1 = new StringReader("ebcde");

        Reader r = new TrReader(r1, "ae", "XY");
//        char cbuf[] = new char[7];
        char cbuf[] = new char[] {'1','2','3','4','5','6','7'};

        try {
//
            System.out.println(r1.read(cbuf));
            System.out.println(cbuf);

//            System.out.println(r.read(cbuf, 2, 3));
//            System.out.println(cbuf);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
