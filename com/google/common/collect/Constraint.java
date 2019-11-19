package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface Constraint<E> {
  E checkElement(E paramE);
  
  String toString();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Constraint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */