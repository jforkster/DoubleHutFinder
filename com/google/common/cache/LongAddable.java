package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface LongAddable {
  void increment();
  
  void add(long paramLong);
  
  long sum();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/LongAddable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */