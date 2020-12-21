package org.geekbang.time.commonmistakes.java8;

import org.junit.Test;

import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class CoolOptionalTest {

    @Test(expected = IllegalArgumentException.class)
    public void optional() {

        //1、通过get方法获取Optional中的实际值
        System.out.println("1=="+Optional.of(1).get());
        assertThat(Optional.of(1).get(), is(1));

        //2、通过ofNullable来初始化一个null，通过orElse方法实现Optional中无数据的时候返回一个默认值
        System.out.println("2=="+Optional.ofNullable(null).orElse("A"));
        assertThat(Optional.ofNullable(null).orElse("A"), is("A"));

        //3、OptionalDouble是基本类型double的Optional对象，isPresent判断有无数据
        System.out.println("3=="+OptionalDouble.empty().isPresent());
        assertFalse(OptionalDouble.empty().isPresent());

        //4、通过map方法可以对Optional对象进行级联转换，不会出现空指针，转换后还是一个Optional
        System.out.println("4=="+Optional.of(1).map(Math::incrementExact).get());
        assertThat(Optional.of(1).map(Math::incrementExact).get(), is(2));

        //5、通过filter实现Optional中数据的过滤，得到一个Optional，然后级联使用orElse提供默认值
        System.out.println("5=="+Optional.of(1).filter(integer -> integer % 2 == 0).orElse(null));
        assertThat(Optional.of(1).filter(integer -> integer % 2 == 0).orElse(null), is(nullValue()));

        //6、通过orElseThrow实现无数据时抛出异常
        System.out.println("6=="+Optional.empty().orElseThrow(IllegalArgumentException::new));
        Optional.empty().orElseThrow(IllegalArgumentException::new);
    }
}
