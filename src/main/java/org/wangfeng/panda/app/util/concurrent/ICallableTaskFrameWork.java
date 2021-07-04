package org.wangfeng.panda.app.util.concurrent;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ICallableTaskFrameWork {
    <V> List<V> submitsAll(List<? extends CallableTemplate<V>> tasks)
            throws InterruptedException, ExecutionException;
}
