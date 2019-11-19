/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ public abstract class ForwardingIterator<T>
/*    */   extends ForwardingObject
/*    */   implements Iterator<T>
/*    */ {
/* 43 */   public boolean hasNext() { return delegate().hasNext(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public T next() { return (T)delegate().next(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void remove() { delegate().remove(); }
/*    */   
/*    */   protected abstract Iterator<T> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */