package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public interface SortedSetMultimap<K, V> extends SetMultimap<K, V> {
  SortedSet<V> get(@Nullable K paramK);
  
  SortedSet<V> removeAll(@Nullable Object paramObject);
  
  SortedSet<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  Map<K, Collection<V>> asMap();
  
  Comparator<? super V> valueComparator();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SortedSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */