public class Sum implements IntUnaryFunction {
    int _count = 0;
    public int apply(int n) {
        return _count = _count + n;
    }
}
