package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;

@GwtCompatible
interface SortedIterable<T> extends Iterable<T> {
  Comparator<? super T> comparator();
  
  Iterator<T> iterator();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SortedIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */