package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public interface MapConstraint<K, V> {
  void checkKeyValue(@Nullable K paramK, @Nullable V paramV);
  
  String toString();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/MapConstraint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */