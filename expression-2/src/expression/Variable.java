package expression;

public class Variable implements SimpleBase {
    private final String name = "x";

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(SimpleBase another) {
        if (another.getClass() == Circular.class) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
