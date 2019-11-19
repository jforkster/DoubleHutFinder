package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface ListenableFuture<V> extends Future<V> {
  void addListener(Runnable paramRunnable, Executor paramExecutor);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ListenableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */