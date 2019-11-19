package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public interface PeekingIterator<E> extends Iterator<E> {
  E peek();
  
  E next();
  
  void remove();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/PeekingIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */