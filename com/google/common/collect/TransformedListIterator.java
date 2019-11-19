/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.ListIterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ abstract class TransformedListIterator<F, T>
/*    */   extends TransformedIterator<F, T>
/*    */   implements ListIterator<T>
/*    */ {
/* 35 */   TransformedListIterator(ListIterator<? extends F> backingIterator) { super(backingIterator); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   private ListIterator<? extends F> backingIterator() { return Iterators.cast(this.backingIterator); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public final boolean hasPrevious() { return backingIterator().hasPrevious(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public final T previous() { return (T)transform(backingIterator().previous()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public final int nextIndex() { return backingIterator().nextIndex(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public final int previousIndex() { return backingIterator().previousIndex(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public void set(T element) { throw new UnsupportedOperationException(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public void add(T element) { throw new UnsupportedOperationException(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/TransformedListIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */