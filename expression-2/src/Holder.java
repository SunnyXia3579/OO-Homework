public class Holder {
    private String expression;
    private String curToken;
    private int ptr;

    public Holder(String expression) {
        this.expression = expression.replaceAll("[ \\t]", "")  // remove blank
                     .replaceAll("-\\+|\\+-", "-")  // between terms
                     .replaceAll("--|\\+\\+", "+")  // between terms
                     .replaceAll("-\\+|\\+-", "-")  // between terms
                     .replaceAll("\\*\\+", "*");  // between factors | inside a factor
        for (int i = this.expression.indexOf('i');i != -1;
             i = this.expression.indexOf('i', i)) {
            if (!this.expression.substring(i - 1, i + 2).equals("sin")) {
                this.expression = this.expression.substring(0, i) +
                                  "loopVar" +
                                  this.expression.substring(i + 1);
            } else {
                i++;
            }
        }
        this.next();
    }

    public String getCurToken() {
        return curToken;
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (ptr < expression.length() && Character.isDigit(expression.charAt(ptr))) {
            sb.append(expression.charAt(ptr));
            ptr++;
        }
        return sb.toString();
    }

    public void next() {
        if (ptr == expression.length()) {
            return;
        }

        char c = expression.charAt(ptr);
        if (Character.isDigit(c)) {  // read a number
            curToken = getNumber();
        } else if ("()+-*xfgh,".indexOf(c) != -1) {  // read ()+-*x
            curToken = String.valueOf(c);
            ptr++;
            if (c == '*' && expression.charAt(ptr) == '*') {  // power
                curToken = "**";
                ptr++;
            }
        } else {
            if (c == 's' && expression.charAt(ptr + 1) == 'i') {
                curToken = "sin";
            } else if (c == 'c') {
                curToken = "cos";
            } else {
                curToken = "sum";
            }
            ptr += 3;
        }
    }

    public void nextParam() {
        int leftBracket = 0;
        int rightBracket = 0;
        char c = '\0';
        for (curToken = "";
             ptr < expression.length() && c != ',' && leftBracket >= rightBracket;
             ptr++) {
            c = expression.charAt(ptr);
            curToken = curToken.concat(String.valueOf(c));
            if ("csfgh".indexOf(c) != -1) {
                for (ptr++;c != '(';ptr++) {
                    c = expression.charAt(ptr);
                    curToken = curToken.concat(String.valueOf(c));
                }
                int leftBracket1 = 0;
                int rightBracket1 = 0;
                for ( ;ptr < expression.length() && leftBracket1 >= rightBracket1;ptr++) {
                    c = expression.charAt(ptr);
                    curToken = curToken.concat(String.valueOf(c));
                    if (c == '(') {
                        leftBracket1++;
                    } else if (c == ')') {
                        rightBracket1++;
                    }
                }
                c = expression.charAt(ptr);
                curToken = curToken.concat(String.valueOf(c));
            }
            if (c == '(') {
                leftBracket++;
            } else if (c == ')') {
                rightBracket++;
            }
        }
        curToken = curToken.substring(0, curToken.length() - 1);
    }
}
