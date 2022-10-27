package expression;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {
    private String name;
    private final ArrayList<String> paramList;
    private String definition;
    private final int paramNum;

    public Function(String definition) {
        paramList = new ArrayList<>();
        this.definition = definition.replaceAll("[ \\t]", "");
        parseName();
        parseParam();
        paramNum = paramList.size();
        encodeParam();
        int end = this.definition.indexOf('=');
        this.definition = this.definition.substring(end + 1);
    }

    public String getName() {
        return name;
    }

    public int getParamNum() {
        return paramNum;
    }

    private void parseName() {
        Pattern pattern = Pattern.compile("([fhg])");
        Matcher matcher = pattern.matcher(definition);
        if (matcher.find()) {
            name = matcher.group();
        }
    }

    private void parseParam() {
        int pos = 0;
        String curToken = "";
        while (!curToken.equals(")")) {
            curToken = String.valueOf(definition.charAt(pos));
            if (curToken.equals("x") || curToken.equals("y") || curToken.equals("z")) {
                paramList.add(curToken);
            }
            pos++;
        }
    }

    public void encodeParam() {
        for (int i = 0;i < paramNum;i++) {
            definition = definition.replaceAll(paramList.get(i), "parameter" + i);
        }
    }

    public String instantiate(ArrayList<String> argList) {
        String expString = definition;
        for (int i = 0;i < paramNum;i++) {
            expString = expString.replaceAll("parameter" + i,
                                 "(" + argList.get(i) + ")");
        }
        return expString;
    }
}
