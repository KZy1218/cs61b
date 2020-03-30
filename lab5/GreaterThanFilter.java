/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {
    private String _colName;
    private String _ref;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        int i = headerList().indexOf(_colName);
        return (_next.getValue(i).compareTo(_ref) >= 0);
    }

}
