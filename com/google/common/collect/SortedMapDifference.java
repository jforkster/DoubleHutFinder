package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;

@GwtCompatible
public interface SortedMapDifference<K, V> extends MapDifference<K, V> {
  SortedMap<K, V> entriesOnlyOnLeft();
  
  SortedMap<K, V> entriesOnlyOnRight();
  
  SortedMap<K, V> entriesInCommon();
  
  SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SortedMapDifference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */