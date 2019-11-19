package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
public interface Function<F, T> {
  @Nullable
  T apply(@Nullable F paramF);
  
  boolean equals(@Nullable Object paramObject);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */