package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface FilteredSetMultimap<K, V> extends FilteredMultimap<K, V>, SetMultimap<K, V> {
  SetMultimap<K, V> unfiltered();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */