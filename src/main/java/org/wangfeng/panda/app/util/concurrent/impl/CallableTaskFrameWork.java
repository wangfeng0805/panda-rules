package org.wangfeng.panda.app.util.concurrent.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wangfeng.panda.app.util.concurrent.CallableTemplate;
import org.wangfeng.panda.app.util.concurrent.ICallableTaskFrameWork;
import org.wangfeng.panda.app.util.concurrent.IConcurrentThreadPool;

import java.util.List;

@Component
public class CallableTaskFrameWork implements ICallableTaskFrameWork {

    @Autowired
    private IConcurrentThreadPool concurrentThreadPool;

    @Override
    public <V> List<V> submitsAll(List<? extends CallableTemplate<V>> tasks)
            throws InterruptedException {

        concurrentThreadPool.getConcurrentThreadPool();

        return concurrentThreadPool.invokeAll(tasks);
    }

}
