package com.google.common.util.concurrent;

public interface AsyncFunction<I, O> {
  ListenableFuture<O> apply(I paramI) throws Exception;
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AsyncFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */