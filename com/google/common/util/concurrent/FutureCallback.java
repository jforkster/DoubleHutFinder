package com.google.common.util.concurrent;

import javax.annotation.Nullable;

public interface FutureCallback<V> {
  void onSuccess(@Nullable V paramV);
  
  void onFailure(Throwable paramThrowable);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/FutureCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */