// 需要先将官方包中用到的工具类import进来
//import com.oocourse.spec1.ExprInput;
//import com.oocourse.spec1.ExprInputMode;

import expression.Function;

import java.util.ArrayList;
import java.util.Scanner;

public class MyMainClass {
    public static void main(String[] args) {
        // 实例化一个ExprInput类型的对象scanner
        // 由于是一般读入模式，所以我们实例化时传递的参数为ExprInputMode.NormalMode
        // ExprInput scanner = new ExprInput(ExprInputMode.NormalMode);

        // 一般读入模式下，读入一行字符串时使用readLine()方法，在这里我们使用其读入表达式
        // String expression = scanner.readLine();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            ArrayList<Function> functions = new ArrayList<>();
            int functionNum = Integer.parseInt(scanner.nextLine());
            for (int i = 0;i < functionNum;i++) {
                String fun = scanner.nextLine();
                // System.out.println(fun);
                functions.add(new Function(fun));
            }
            String expression = scanner.nextLine();

            // 表达式括号展开相关的逻辑
            Holder holder = new Holder(expression);
            Parser parser = new Parser(functions, holder);
            System.out.println(parser.parseExpression().toString());
        }
    }
}
//(x*x*x-x**3)
//x*x*x*x
//1
//f(x)=1
//x**2

/*
1
f(x)=1
x*x*x*x
 */

/*
1
f(x)=1
(x+1)**2
 */

/*
1
f(x)=1
cos(x)
 */

/*
1
f(x)=1
sin((x)**2)
 */

/*
1
f(x)=1
sin(sin((x+1)**2))
 */

/*
1
f(x)=x
sin(f(x)*f(x+1))
 */

/*
2
f(x)=x
g(x)=x**2
f(x)*g(x+1)
 */

/*
1
g(x)=x**2
g(x+1)
 */

/*
0
x*x*x*x
 */

/*
0
sin(x)*sin(x)
 */

/*
1
f(x,y)=x+y
f(x,x)
 */

/*
1
f(x,y)=x+y
sum(i,1,2,f(x,x))
 */

/*
1
h(x,y,z)=x+z+y
sum(i,1,2,h(i,i+1,i+2))
 */

/*
1
f(x,y,z)=x**2+sin(0)+cos(0)
f(0,0,0)
 */

/*
2
f(x,y,z)=x**2
g(x,z)=z**3
g(sum(i,1,2,f(i,i**2,i**3)),f(sin(x),x,x**2))
 */

/*
1
f(x,y,z)=x**2
sum(i,1,2,f(i,i**2,i**3))
 */

/*
1
f(x,y,z)=x**2
f(sin(x),x,x**2)
 */

/*
2
f(x,y)=sin(x)*cos(y)
g(x)=x**2+2*x+1
f(x,x)-g(x)
 */

/*
1
f(x,y)=sin(x)*cos(y)
f(x,x)
 */

/*
1
g(x)=(x+1)**2
-(g(cos(x))+sum(i,1,10,(sin(x)*i)))
 */

/*
1
g(x)=(x+1)**2
-(g(cos(x))+sum(i,1,10,sin(x)*i))
 */

/*
0
sum(i,1,10,sin(x)*i)
 */

/*
0
-sin(-x)
 */

/*
0
sum(i,1,10,sin(x))
 */

/*
2
f(x,y,z)=x**3+cos(y)+sin(z)**3
h(x,y,z)=sin(x)**3*cos(2*x+2*-2)+cos(y)*sin(x)+sin((z+1)**2)**1
x*cos(+g(+x-x**+0,-x*x**+1+x*x,+x)+x**+1*x*x)
 */

/*
1
h(x,y,z)=sin(x)**3*cos(2*x+2*-2)+cos(y)*sin(x)+sin((z+1)**2)**1
h(+x**0,-x*sin(+x**0)**+1*x**0,+x*x*x**+0)
 */

/*
1
h(x,y,z)=sin(x)**3*cos(2*x+2*-2)+cos(y)*sin(x)+sin((z+1)**2)**1
h(1,-x*sin(x),x*x)
 */

/*
0
sin((1))**3*cos(2*(1)+2*-2)+cos((-x*sin(x)))*sin((1))+sin(((x*x)+1)**2)**1
 */

/*
0
sin((1))**3*cos(2*(1)+2*-2)
 */



// b:
/*
0
cos(x)*(+3)**2

x*x-49*x*x*cos(x)
 */


// c
/*
0
+(-(-x**+1+cos(x)**0*x)*x**2+sin(x)**1*(-x*x*x)++4*(-x*x--4*sin(x)**0*sin(x)**2-sin(x)**0*x))*(-(-x-x)*(+x-x+x)*(+x))*(+x**1)+x**2+(+(-cos(x)**+0*x-cos(x)**+1*x**+1+x*x*x**0)*(+cos(x)**+0+x))*sin(x)**1
 */

// c
/*
0
- (	-+1*cos(x)**2+cos(x)**+2	)	--6
 */

// c
/*
-	+ (- x**+4-x	*x )*-7 	 +(	+	x**3	 * cos(x) **0	+	x** 3 )	*	cos(x) **+0
 */
