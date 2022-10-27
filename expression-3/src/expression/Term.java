package expression;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Term implements Comparable<Term> {
    private BigInteger coefficient;
    private final ArrayList<Factor> factors;  // Expression or Factor
    private Expression complexTerm;

    public Term() {
        this.coefficient = BigInteger.ONE;
        this.factors = new ArrayList<>();
    }

    public Term(BigInteger coefficient) {
        this.coefficient = coefficient;
        this.factors = new ArrayList<>();
    }

    public Term(BigInteger coefficient, ArrayList<Factor> factors) {
        this.coefficient = coefficient;
        this.factors = factors;
    }

    public BigInteger getCoefficient() {
        return coefficient;
    }

    public ArrayList<Factor> getFactors() {  // greater power first
        return this.factors;
    }

    public Expression getComplexTerm() {
        return complexTerm;
    }

    public void setCoefficient(BigInteger coefficient) {
        this.coefficient = coefficient;
    }

    public void negate() {
        if (complexTerm == null) {
            coefficient = coefficient.negate();
        }
        else {
            for (Term term : complexTerm.getTerms()) {
                term.negate();
            }
        }
    }

    @Override
    public int compareTo(Term another) {
        int i;
        for (i = 0;i < factors.size() && i < another.factors.size();i++) {
            int cmp = factors.get(i).compareTo(another.factors.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        if (i < factors.size()) {
            return -1;
        }
        if (i < another.factors.size()) {
            return 1;
        }
        return 0;
    }

    public void addFactor(Factor factor) {
        if (factor.getComplexFactor() == null) {  // factor's type is Factor, not Expression
            SimpleBase simpleBase = factor.getSimpleBase();
            if (complexTerm == null) {  // with no expression before
                if (simpleBase.getClass() == Number.class) {  // factor is a number
                    coefficient = coefficient.multiply(
                            ((Number) simpleBase).getValue().pow((factor.getPower())));
                }
                else {  // factor is a variable or sin / cos
                    if (factor.getSign() == -1) {
                        negate();
                    }
                    factors.add(factor);
                    termAggregate();
                }
            }
            else {  // with expression before
                ArrayList<Term> now = new ArrayList<>();
                if (simpleBase.getClass() == Number.class) {  // factor is a number
                    now.add(new Term(((Number) simpleBase).getValue().pow((factor.getPower()))));
                }
                else {  // factor is a variable or sin / cos
                    ArrayList<Factor> factors1 = new ArrayList<>();
                    factors1.add(factor);
                    if (factor.getSign() == -1) {
                        now.add(new Term(BigInteger.valueOf(-1), factors1));
                    } else {
                        now.add(new Term(BigInteger.ONE, factors1));
                    }
                }
                complexTerm.expressionMult(new Expression(now));
            }
        }
        else {  //factor's type is Expression
            Expression expression = factor.getComplexFactor();
            if (complexTerm == null) {  // with no expression before
                complexTerm = expression;
                ArrayList<Term> prev = new ArrayList<>();
                prev.add(new Term(coefficient, factors));
                complexTerm.expressionMult(new Expression(prev));
                factors.clear();
            } else {  // with expression before
                complexTerm.expressionMult(expression);
            }
        }
    }

    public void termAggregate() {
        factors.sort(Comparator.naturalOrder());
        for (int i = 1;i < factors.size();i++) {
            while (i < factors.size() &&
                   factors.get(i - 1).getSimpleBase().compareTo(
                   factors.get(i).getSimpleBase()) == 0) {
                factors.get(i - 1).setPower(factors.get(i - 1).getPower() +
                                            factors.get(i).getPower());
                factors.remove(i);
            }
        }
        for (int i = 0;i < factors.size();i++) {
            if (factors.get(i).getPower() == 0) {
                factors.remove(i);
                i--;
            }
        }
    }

    @Override
    public String toString() {
        if (factors.size() == 0) {  // no variable
            return coefficient.toString();
        }
        // at least one variable
        Iterator<Factor> factorIterator = factors.iterator();
        if (coefficient.equals(BigInteger.ZERO)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (coefficient.equals(BigInteger.ONE)) {
            sb.append(factorIterator.next().toString());
        } else if (coefficient.equals(BigInteger.valueOf(-1))) {
            sb.append("-");
            sb.append(factorIterator.next().toString());
        } else {
            sb.append(coefficient);
        }

        while (factorIterator.hasNext()) {
            sb.append("*");
            sb.append(factorIterator.next().toString());
        }
        return sb.toString();
    }
}
