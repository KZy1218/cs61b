/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {
    private String _colName;
    private String _subStr;

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
       int i = headerList().indexOf(_colName);
       return _next.getValue(i).contains(_subStr);
    }

    // FIXME: Add instance variables?
}
