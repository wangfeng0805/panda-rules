package org.wangfeng.panda.app.cache;

import java.io.Closeable;

/**
 * 使用redis实现的分布式锁
 *
 *
 */

public class Lock implements Closeable {

//    private  static  R2mClusterClient redisClient =SpringUtil.getBean(R2mClusterClient.class);;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(Lock.class);
//
//    private static final long ONE_MILLI_NANOS = 1000000L;
//
//    private static final long DEFAULT_WAIT_TIME = 10000;// 默认超时时间（毫秒）
//
//    private static final int EXPIRE = 30;//
//
//    private static Random r = new Random();
//
//
//
//
//    private String key;
//    private String rand;
//
//    private Lock(String key, String rand) {
//        this.rand = rand;
//        this.key = key;
//    }
//
//    /**
//     * 给key设置值value，并添加超时时间，当key存在时，通过ttl判断此key是否为持久化的，持久的则重新设置key超时时间，这防止set 和
//     * expire非原子操作产生持久化key
//     *
//     * @param key
//     * @param value
//     * @param seconds
//     * @return
//     */
//    private static boolean setnxAndExpire(String key, String value, int seconds) {
//        long result = redisClient.setnx(key, value);
//        if (result == 1) {
//            redisClient.expire(key, seconds);
//        } else if (redisClient.ttl(key) < 0) {
//            LOGGER.warn("expire key {} is persistence or none exist, will expire the key", key);
//            redisClient.expire(key, seconds);
//        }
//        return result == 1;
//    }
//
//    /**
//     * 循环获取分布式锁
//     *
//     * @param key
//     * @param waitTime
//     * @return
//     */
//    public static Lock lock(String key, long waitTime) {
//        LOGGER.info("get lock {}", key);
//        long startTime = System.nanoTime();
//        long waitTimeNaNo = waitTime * ONE_MILLI_NANOS;
//        String rand = String.valueOf(r.nextDouble());
//        while (true) {
//            if (setnxAndExpire(key, rand, EXPIRE)) {
//                break;
//            }
//            try {
//                Thread.sleep(100, r.nextInt(500));
//                if (System.nanoTime() - startTime > waitTimeNaNo) {
//                    throw new TimeoutException("get lock " + key + " timeout");
//                }
//            } catch (TimeoutException e) {
//                throw new RuntimeException(e);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//        return new Lock(key, rand);
//    }
//
//    /**
//     * 尝试获取锁，若锁已被占用，立即返回null
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static Lock tryLock(String key) {
//        return tryLock(key, EXPIRE);
//    }
//
//    /**
//     * 尝试获取锁，若锁已被占用，立即返回null
//     *
//     * @param key
//     * @return
//     * @throws Exception
//     */
//    public static Lock tryLock(String key, int expireTime) {
//        LOGGER.info("try get lock {}", key);
//        String rand = String.valueOf(r.nextInt());
//        return setnxAndExpire(key, rand, expireTime) ? new Lock(key, rand) : null;
//    }
//
//    public static Lock lock(String key) {
//        return lock(key, DEFAULT_WAIT_TIME);
//    }
//
//    @Override
    public void close() {
//        LOGGER.info("release lock {}", key);
//        try {
//            String value = redisClient.get(key);
//            if (rand.equals(value)) {
//                redisClient.del(key);// 说明当前的锁是自己锁定的
//            } else {
//                LOGGER.warn("del the lock {key: {}, rand: {}} fail, redis value is {}", key, rand, value);
//            }
//        } catch (Exception e) {
//            LOGGER.error("release lock error", e);
//        }
    }


}
