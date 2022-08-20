import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author pilaf
 * @description
 * @date 2022-08-20 10:37
 **/
public class MainClass {


    /**
     * 本地缓存，如果不设置.weakKeys().weakValues()，在持续不断往cache中放入新对象的时候，会导致老年代占用越来越大，最终导致FullGC
     * 如果设置.weakKeys().weakValues()，老年代不会占用越来越大
     */
    private static Cache<Long, DemoObject> cache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .expireAfterWrite(180, TimeUnit.SECONDS)
            .weakKeys()
            .weakValues()
            .initialCapacity(8)
            .maximumSize(50000)
            .build();

    /**
     * 写入缓存限流
     */
    private static RateLimiter writeLimiter = RateLimiter.create(20000);


    public static void main(String[] args) {
        loopWrite();
    }


    /**
     * 启动一个线程，往guava cache中写入大量对象
     */
    private static void loopWrite() {
        new Thread(() -> {
            Long id = 0L;
            //限制一共写入500万个对象
            while (id < 500_0000) {
                writeLimiter.acquire();
                cache.put(id, new DemoObject());
                id++;
            }
        }).start();
    }


}
