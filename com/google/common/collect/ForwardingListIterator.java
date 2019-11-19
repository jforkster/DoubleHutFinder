/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingListIterator<E>
/*    */   extends ForwardingIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/* 43 */   public void add(E element) { delegate().add(element); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public boolean hasPrevious() { return delegate().hasPrevious(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public int nextIndex() { return delegate().nextIndex(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public E previous() { return (E)delegate().previous(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public int previousIndex() { return delegate().previousIndex(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public void set(E element) { delegate().set(element); }
/*    */   
/*    */   protected abstract ListIterator<E> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingListIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */