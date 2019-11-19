package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public interface Weigher<K, V> {
  int weigh(K paramK, V paramV);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/Weigher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */