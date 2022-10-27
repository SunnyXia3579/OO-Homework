// 需要先将官方包中用到的工具类import进来
import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import expression.Function;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 获取自定义函数个数
        int cnt = scanner.getCount();

        ArrayList<Function> functions = new ArrayList<>();

        // 读入自定义函数
        for (int i = 0; i < cnt; i++) {
            String func = scanner.readLine();
            // 存储或者解析逻辑
            functions.add(new Function(func));
        }

        // 读入最后一行表达式
        String expression = scanner.readLine();

        // 表达式括号展开相关的逻辑
        Holder holder = new Holder(expression);
        Parser parser = new Parser(functions, holder);
        System.out.println(parser.parseExpression().toString());
    }
}