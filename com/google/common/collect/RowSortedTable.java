package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
@Beta
public interface RowSortedTable<R, C, V> extends Table<R, C, V> {
  SortedSet<R> rowKeySet();
  
  SortedMap<R, Map<C, V>> rowMap();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RowSortedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */