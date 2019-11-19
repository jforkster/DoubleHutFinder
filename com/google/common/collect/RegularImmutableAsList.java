/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ class RegularImmutableAsList<E>
/*    */   extends ImmutableAsList<E>
/*    */ {
/*    */   private final ImmutableCollection<E> delegate;
/*    */   private final ImmutableList<? extends E> delegateList;
/*    */   
/*    */   RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList) {
/* 35 */     this.delegate = delegate;
/* 36 */     this.delegateList = delegateList;
/*    */   }
/*    */ 
/*    */   
/* 40 */   RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) { this(delegate, ImmutableList.asImmutableList(array)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   ImmutableCollection<E> delegateCollection() { return this.delegate; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   ImmutableList<? extends E> delegateList() { return this.delegateList; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public UnmodifiableListIterator<E> listIterator(int index) { return this.delegateList.listIterator(index); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("not present in emulated superclass")
/* 61 */   int copyIntoArray(Object[] dst, int offset) { return this.delegateList.copyIntoArray(dst, offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public E get(int index) { return (E)this.delegateList.get(index); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */