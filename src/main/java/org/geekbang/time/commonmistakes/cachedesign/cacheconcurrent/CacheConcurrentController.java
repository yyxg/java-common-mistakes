package org.geekbang.time.commonmistakes.cachedesign.cacheconcurrent;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequestMapping("cacheconcurrent")
@RestController
public class CacheConcurrentController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private AtomicInteger atomicInteger = new AtomicInteger();
    @Autowired
    private RedissonClient redissonClient;


    @PostConstruct
    public void init() {
        stringRedisTemplate.opsForValue().set("hotsopt", getExpensiveData(), 5, TimeUnit.SECONDS);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            log.info("DB QPS : {}", atomicInteger.getAndSet(0));
        }, 0, 1, TimeUnit.SECONDS);
    }

    @GetMapping("wrong")
    public String wrong() {
        String data = stringRedisTemplate.opsForValue().get("hotsopt");
        if (StringUtils.isEmpty(data)) {
            data = getExpensiveData();
            stringRedisTemplate.opsForValue().set("hotsopt", data, 5, TimeUnit.SECONDS);
        }
        return data;
    }

    @GetMapping("right")
    public String right() {
        //第一次判断先从缓存取一次数据
        String data = stringRedisTemplate.opsForValue().get("hotsopt");
        if (StringUtils.isEmpty(data)) {
            RLock locker = redissonClient.getLock("locker");
            if (locker.tryLock()) {
                try {
                    data = stringRedisTemplate.opsForValue().get("hotsopt");
                    //双重检查，因为可能已经有一个B线程过了第一次判断，在等锁，然后A线程已经把数据写入了Redis中
                    //线程AB 同时过了第一次判断，A 拿到锁并从数据库拿到数据更新了redis,这时线程B 等到锁的时候就不需要再去数据库取一次数据
                    if (StringUtils.isEmpty(data)) {
                        data = getExpensiveData();
                        stringRedisTemplate.opsForValue().set("hotsopt", data, 5, TimeUnit.SECONDS);
                    }
                } finally {
                    locker.unlock();
                }
            }
        }
        return data;
    }

    private String getExpensiveData() {
        atomicInteger.incrementAndGet();
        return "important data";
    }
}
