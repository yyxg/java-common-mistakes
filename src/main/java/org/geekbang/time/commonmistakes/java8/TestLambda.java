package org.geekbang.time.commonmistakes.java8;

import java.util.stream.IntStream;

/**
 * https://colobu.com/2014/11/06/secrets-of-java-8-lambda/
 * https://github.com/y645194203/geektime-java-100/blob/master/TestLambda.java
 * lambda与普通的匿名内部类的实现方式区别
 * 编译时：
 *  - Lambda 表达式会生成一个方法， 方法实现了表达式的代码逻辑
 *  - 生成invokedynamic指令， 调用bootstrap方法， 由java.lang.invoke.LambdaMetafactory.metafactory方法实现
 *  运行时：
 *  - invokedynamic指令调用metafactory方法。 它会返回一个CallSite, 此CallSite返回目标类型的一个匿名实现类，
 *  此类关联编译时产生的方法
 *  - lambda表达式调用时会调用匿名实现类关联的方法。
 * @Author xialh
 * @Date 2020/12/21 11:59 AM
 */
public class TestLambda {

    public static void main(String[] args) {
        int x = 2;
        IntStream.of(1, 2, 3).map(i -> i * 2).map(i -> i * x);
    }
}
