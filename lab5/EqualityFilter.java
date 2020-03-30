/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {
    private String _colName;
    private String _match;

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colName = colName;
        _match = match;
    }

    @Override
    protected boolean keep() {
        int i = headerList().indexOf(_colName);
        return _next.getValue(i).equals(_match);
    }

}
