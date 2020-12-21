package org.geekbang.time.commonmistakes.springpart1.aopmetrics;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author xialh
 * @Date 2020/10/25 10:52 AM
 */
@Slf4j
public class Test {
    List<String> data = new ArrayList<>();
    public static void main(String[] args) {

        Test s = new Test();
        s.say();


   }


    public void say() {
        data.add(IntStream.rangeClosed(1, 10)
                .mapToObj(_$ -> "a")
                .collect(Collectors.joining("|")) + UUID.randomUUID().toString());
        log.info("I'm {} size:{}", this, data);
    }
}
