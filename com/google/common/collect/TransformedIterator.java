/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
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
/*    */ abstract class TransformedIterator<F, T>
/*    */   extends Object
/*    */   implements Iterator<T>
/*    */ {
/*    */   final Iterator<? extends F> backingIterator;
/*    */   
/* 36 */   TransformedIterator(Iterator<? extends F> backingIterator) { this.backingIterator = (Iterator)Preconditions.checkNotNull(backingIterator); }
/*    */ 
/*    */ 
/*    */   
/*    */   abstract T transform(F paramF);
/*    */ 
/*    */   
/* 43 */   public final boolean hasNext() { return this.backingIterator.hasNext(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public final T next() { return (T)transform(this.backingIterator.next()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public final void remove() { this.backingIterator.remove(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/TransformedIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */