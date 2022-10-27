package expression;

import java.math.BigInteger;

public class Circular implements SimpleBase {
    private final String name;
    private final Expression parameter;
    private int sign = 1;
    private int constant = -100;

    public Circular(String name, Expression parameter) {
        this.name = name;
        this.parameter = parameter;
        if (name.equals("sin") &&
            this.parameter.compareTo(new Expression(BigInteger.ZERO)) == 0) {
            constant = 0;
        } else if (name.equals("cos") &&
                   this.parameter.compareTo(new Expression(BigInteger.ZERO)) == 0) {
            constant = 1;
        } else if (this.parameter.getTerms().get(0).
                   getCoefficient().compareTo(BigInteger.ZERO) < 0) {
            this.parameter.negate();
            if (this.name.equals("sin")) {
                sign = -sign;
            }
        }
    }

    public Expression getParameter() {
        return parameter;
    }

    public int getSign() {
        return sign;
    }

    public int getConstant() {
        return constant;
    }

    @Override
    public int compareTo(SimpleBase another) {
        if (another.getClass() == Variable.class) {
            return 1;
        }
        if (name.equals("sin")) {
            if (((Circular) another).name.equals("cos")) {
                return -1;
            }
            return parameter.compareTo(((Circular) another).getParameter());
        }
        if (name.equals("cos")) {
            if (((Circular) another).name.equals("sin")) {
                return 1;
            }
            return parameter.compareTo(((Circular) another).getParameter());
        }
        return -100;
    }

    @Override
    public String toString() {
        return name + "(" + parameter.toString() + ")";
    }
}
