package org.wangfeng.panda.app.util.concurrent.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.concurrent.CallableTemplate;
import org.wangfeng.panda.app.util.concurrent.IConcurrentThreadPool;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Slf4j
@Component
public class ConcurrentThreadPool implements IConcurrentThreadPool {
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 核心线程数
     */
    @Value("${thread.pool.executor.corePoolSize:10}")
    private int corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${thread.pool.executor.maximumPoolSize:20}")
    private int maximumPoolSize;

    /**
     * 超时时间
     */
    @Value("${thread.pool.executor.keepAliveTime:30}")
    private long keepAliveTime;

    @Override
    @PostConstruct
    public void initConcurrentThreadPool() {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
    }

    @Override
    public ThreadPoolExecutor getConcurrentThreadPool() {
        if(threadPoolExecutor == null){
            initConcurrentThreadPool();
        }
        return threadPoolExecutor;
    }

    @Override
    public <V> V submit(CallableTemplate<V> task) throws InterruptedException,
            ExecutionException {
        Future<V> result = threadPoolExecutor.submit(task);

        return result.get();
    }

    @Override
    public <V> List<V> invokeAll(List<? extends CallableTemplate<V>> tasks) throws InterruptedException {

        List<Future<V>> tasksResult = threadPoolExecutor.invokeAll(tasks);

        List<V> resultList = new ArrayList<V>();

        for (Future<V> future : tasksResult) {
            try {
                resultList.add(future.get());
            } catch (ExecutionException e) {
                String errorMessage = e.getMessage().substring(e.getMessage().indexOf("Exception")+10,e.getMessage().length());
                log.error("当前线程抛错了！"+errorMessage);
                throw new RuleRuntimeException(errorMessage);
            }
        }

        return resultList;
    }

}
