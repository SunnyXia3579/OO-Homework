import expression.Base;
import expression.Number;
import expression.Expression;
import expression.Factor;
import expression.Circular;
import expression.Function;
import expression.Term;
import expression.Variable;

import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {
    private final Holder holder;
    private final ArrayList<Function> functions;

    public Parser(ArrayList<Function> functions, Holder holder) {
        this.functions = functions;
        this.holder = holder;
    }

/*    public Expression parseExpression() {  // only return expression
        Expression expression = new Expression();
        int firstSign = 1;// the first "-" needs special tackling
        while (holder.getCurToken().equals("-") ||
               holder.getCurToken().equals("+")) {
            if (holder.getCurToken().equals("-")) {
                firstSign = -firstSign;
            }
            holder.next();
        }
        Term firstTerm = parseTerm();
        if (firstSign == -1) {
            firstTerm.negate();
        }
        expression.addTerms(firstTerm);

        while (holder.getCurToken().equals("+") ||
               holder.getCurToken().equals("-")) {
            int sign = 1;
            if (holder.getCurToken().equals("-")) {
                sign = -1;
            }
            holder.next();
            Term term = parseTerm();
            if (sign == -1) {
                term.negate();
            }
            expression.addTerms(term);
        }
        return expression;
    }*/

    public Expression parseExpression() {  // only return expression
        Expression expression = new Expression();
        int firstSign = parseSign();
        Term firstTerm = parseTerm();
        if (firstSign == -1) {
            firstTerm.negate();
        }
        expression.addTerms(firstTerm);

        while (holder.getCurToken().equals("+") ||
                holder.getCurToken().equals("-")) {
            int sign = parseSign();
            Term term = parseTerm();
            if (sign == -1) {
                term.negate();
            }
            expression.addTerms(term);
        }
        return expression;
    }

    private int parseSign() {
        int sign = 1;
        while (holder.getCurToken().equals("+") || holder.getCurToken().equals("-")) {
            if (holder.getCurToken().equals("-")) {
                sign = -sign;
            }
            holder.next();
        }
        return sign;
    }

    public Term parseTerm() {  // only return Term
        Term term = new Term();
        term.addFactor(parseFactor());
        while (holder.getCurToken().equals("*")) {
            holder.next();
            if (holder.getCurToken().equals("-")) {
                term.negate();
                holder.next();
            }
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {  // only return Factor
        Factor factor = new Factor();
        factor.addBase(parseBase());
        factor.addPower(parsePower());
        return factor;
    }

    public Base parseBase() {  // return ( Number | Variable ) | Expression
        if (holder.getCurToken().equals("(")) {  // complexBase
            holder.next();
            Expression expression = parseExpression();
            holder.next();
            return expression;
        } else if (Character.isDigit(holder.getCurToken().charAt(0))) {  // Number
            String number = holder.getCurToken();
            holder.next();
            return new Number(new BigInteger(number));
        } else if (holder.getCurToken().equals("x")) {  // x
            holder.next();
            return new Variable();
        } else if (holder.getCurToken().equals("cos") ||
                   holder.getCurToken().equals("sin")) {  // sin / cos
            final String name = holder.getCurToken();
            holder.next();
            holder.nextParam();
            Holder holder1 = new Holder(holder.getCurToken());
            Parser parser = new Parser(functions, holder1);
            Expression expression = parser.parseExpression();
            holder.next();
            return new Circular(name, expression);
        } else if (holder.getCurToken().equals("sum")) {  // sum
            holder.next();
            holder.next();
            Expression expression = parseSum();
            holder.next();
            return expression;
        } else {  // fgh
            Expression expression = parseFunction();
            holder.next();
            return expression;
        }
    }

    public Expression parseFunction() {
        String functionName = holder.getCurToken();
        holder.next();
        ArrayList<String> argList = new ArrayList<>();

        Function function = null;
        for (Function function1 : functions) {
            if (function1.getName().equals(functionName)) {
                function = function1;
            }
        }
        for (int i = 0;i < function.getParamNum();i++) {
            holder.nextParam();
            argList.add(holder.getCurToken());
        }
        Holder holder1 = new Holder(function.instantiate(argList));
        Parser parser = new Parser(functions, holder1);
        return parser.parseExpression();
    }

    public Expression parseSum() {
        holder.nextParam();
        holder.nextParam();
        int begin = Integer.parseInt(holder.getCurToken());
        holder.nextParam();
        int end = Integer.parseInt(holder.getCurToken());
        holder.nextParam();
        String expString = "".concat(holder.getCurToken().replaceAll("loopVar",
                                                                     String.valueOf(begin)));
        for (int i = begin + 1;i <= end;i++) {
            expString = expString.concat("+")
                                 .concat(holder.getCurToken().replaceAll("loopVar",
                                                                         String.valueOf(i)));
        }
        Holder holder1 = new Holder(expString);
        Parser parser = new Parser(functions, holder1);
        return parser.parseExpression();
    }

    public int parsePower() {  // return only int
        if (holder.getCurToken().equals("**")) {
            holder.next();
            String power = holder.getCurToken();
            holder.next();
            return Integer.parseInt(power);
        }
        return 1;
    }
}
