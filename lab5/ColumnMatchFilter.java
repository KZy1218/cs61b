import java.util.Iterator;


/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {
    private String _str1;
    private String _str2;

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _str1 = colName1;
        _str2 = colName2;
    }

    /**
     * Returns true iff the value of _next should be delivered by next().
     */
    @Override
    protected boolean keep() {
        int index1 = headerList().indexOf(_str1);
        int index2 = headerList().indexOf(_str2);
        return _next.getValue(index1).equals(_next.getValue(index2));
    }
}
