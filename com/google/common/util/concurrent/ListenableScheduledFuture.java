package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.ScheduledFuture;

@Beta
public interface ListenableScheduledFuture<V> extends ScheduledFuture<V>, ListenableFuture<V> {}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ListenableScheduledFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */