package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
public interface Funnel<T> extends Serializable {
  void funnel(T paramT, PrimitiveSink paramPrimitiveSink);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/Funnel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */