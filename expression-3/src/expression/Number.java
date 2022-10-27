package expression;

import java.math.BigInteger;

public class Number implements SimpleBase {
    private final BigInteger value;

    public Number(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return this.value;
    }

    @Override
    public int compareTo(SimpleBase another) {
        return 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
