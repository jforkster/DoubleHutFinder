package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
public interface RangeMap<K extends Comparable, V> {
  @Nullable
  V get(K paramK);
  
  @Nullable
  Map.Entry<Range<K>, V> getEntry(K paramK);
  
  Range<K> span();
  
  void put(Range<K> paramRange, V paramV);
  
  void putAll(RangeMap<K, V> paramRangeMap);
  
  void clear();
  
  void remove(Range<K> paramRange);
  
  Map<Range<K>, V> asMapOfRanges();
  
  RangeMap<K, V> subRangeMap(Range<K> paramRange);
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
  
  String toString();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RangeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */