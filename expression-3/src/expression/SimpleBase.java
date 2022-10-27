package expression;

public interface SimpleBase extends Base, Comparable<SimpleBase> {
    int compareTo(SimpleBase another);
}
