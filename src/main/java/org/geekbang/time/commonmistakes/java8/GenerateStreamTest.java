package org.geekbang.time.commonmistakes.java8;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Stream 相关API操作
 * @author xialihui
 */
public class GenerateStreamTest {


    /**
     * 通过Stream.of方法直接传入多个元素构成一个流
     */
    @Test
    public void of() {
        String[] arr = {"a", "b", "c"};
        Stream.of(arr).forEach(System.out::println);
        Stream.of("a", "b", "c").forEach(System.out::println);
        Stream.of(1, 2, "a").map(item -> item.getClass().getName()).forEach(System.out::println);
    }

    /**
     * 通过Stream.iterate方法使用迭代的方式构造一个无限流，然后使用limit限制流元素个数
     */
    @Test
    public void iterate() {
        Stream.iterate(2, item -> item * 2).limit(10).forEach(System.out::println);
        Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.TEN)).limit(10).forEach(System.out::println);
    }

    /**
     * 通过Stream.generate方法从外部传入一个提供元素的Supplier来构造无限流，然后使用limit限制流元素个数
     */
    @Test
    public void generate() {
        Stream.generate(() -> "test").limit(3).forEach(System.out::println);
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }

    /**
     * 通过stream方法把List或数组转换为流
     */
    @Test
    public void stream() {
        Arrays.asList("a1", "a2", "a3").stream().forEach(System.out::println);
        Arrays.stream(new int[]{1, 2, 3}).forEach(System.out::println);
    }

    /**
     * 通过IntStream或DoubleStream构造基本类型的流
     */
    @Test
    public void primitive() {
        //演示IntStream和DoubleStream
        IntStream.range(1, 3).forEach(System.out::println);
        IntStream.range(0, 3).mapToObj(i -> "x").forEach(System.out::println);

        IntStream.rangeClosed(1, 3).forEach(System.out::println);
        DoubleStream.of(1.1, 2.2, 3.3).forEach(System.out::println);


        //各种转换
        System.out.println(IntStream.of(1, 2).toArray().getClass()); //class [I
        System.out.println(Stream.of(1, 2).mapToInt(Integer::intValue).toArray().getClass()); //class [I
        System.out.println(IntStream.of(1, 2).boxed().toArray().getClass()); //class [Ljava.lang.Object;
        System.out.println(IntStream.of(1, 2).asDoubleStream().toArray().getClass()); //class [D
        System.out.println(IntStream.of(1, 2).asLongStream().toArray().getClass()); //class [J


        Arrays.asList("a", "b", "c").stream()   // Stream<String>
                .mapToInt(String::length)       // IntStream
                .asLongStream()                 // LongStream
                .mapToDouble(x -> x / 10.0)     // DoubleStream
                .boxed()                        // Stream<Double>
                .mapToLong(x -> 1L)             // LongStream
                .mapToObj(x -> "")              // Stream<String>
                .collect(toList());
    }

}
