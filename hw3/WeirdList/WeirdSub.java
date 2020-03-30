public class WeirdSub extends WeirdList implements IntUnaryFunction {
    public WeirdSub(int head, WeirdList tail) {
        super(head, tail);
    }

    public int apply(int x) {
        return x;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public WeirdList map(IntUnaryFunction func) {
        return EMPTY;
    }
}
