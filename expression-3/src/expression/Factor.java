package expression;

import java.math.BigInteger;

public class Factor implements Comparable<Factor> {
    private int sign = 1;
    private int power = 1;
    private SimpleBase simpleBase;
    private Expression complexFactor;

    public Factor() {
        this.simpleBase = new Number(BigInteger.ZERO);
    }

    public Factor(int power, SimpleBase base) {
        this.power = power;
        this.simpleBase = base;
    }

    public int getPower() {
        return power;
    }

    public SimpleBase getSimpleBase() {
        return simpleBase;
    }

    public Expression getComplexFactor() {
        return complexFactor;
    }

    public int getSign() {
        return sign;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public int compareTo(Factor another) {
        int cmp = simpleBase.compareTo(another.simpleBase);
        if (cmp == 0) {
            return -(power - another.power);
        }
        return cmp;
    }

    public void addBase(Base base) {
        if (base.getClass() == Expression.class) {
            complexFactor = (Expression) base;
        }
        else {  // Number or Variable or sin / cos
            simpleBase = (SimpleBase) base;
            if (simpleBase.getClass() == Circular.class) {
                Circular circular = (Circular) simpleBase;
                if (circular.getConstant() == 0 || circular.getConstant() == 1) {
                    simpleBase = new Number(BigInteger.valueOf(circular.getConstant()));
                } else if (circular.getSign() == -1) {
                    sign = -1;
                }
            }
        }
    }

    public void addPower(int power) {
        if ((power & 1) == 0 && sign == -1) {
            sign = 1;
        }
        if (power == 0) {
            this.power = 1;
            simpleBase = new Number(BigInteger.ONE);
            complexFactor = null;
        }
        else {
            if (complexFactor == null) {
                this.power = power;
            }
            else {  // unfold complexFactor
                this.power = 1;
                Expression expression = new Expression(BigInteger.ONE);
                // Expression expression = complexFactor;
                for (int i = 0;i < power;i++) {
                    expression.expressionMult(complexFactor);
                }
                complexFactor = expression;
            }
        }
    }

    @Override
    public String toString() {
        if (power == 1) {
            return simpleBase.toString();
        }
        if (power == 2 && simpleBase.getClass() == Variable.class) {
            return simpleBase + "*" + simpleBase;
        }
        return simpleBase.toString() + "**" + power;
    }
}
