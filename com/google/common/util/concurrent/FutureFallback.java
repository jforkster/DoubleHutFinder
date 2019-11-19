package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;

@Beta
public interface FutureFallback<V> {
  ListenableFuture<V> create(Throwable paramThrowable) throws Exception;
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/FutureFallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */