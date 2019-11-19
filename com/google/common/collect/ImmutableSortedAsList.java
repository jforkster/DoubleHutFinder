/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.util.Comparator;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class ImmutableSortedAsList<E>
/*    */   extends RegularImmutableAsList<E>
/*    */   implements SortedIterable<E>
/*    */ {
/* 36 */   ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) { super(backingSet, backingList); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   ImmutableSortedSet<E> delegateCollection() { return (ImmutableSortedSet)super.delegateCollection(); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public Comparator<? super E> comparator() { return delegateCollection().comparator(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("ImmutableSortedSet.indexOf")
/*    */   public int indexOf(@Nullable Object target) {
/* 53 */     int index = delegateCollection().indexOf(target);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     return (index >= 0 && get(index).equals(target)) ? index : -1;
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible("ImmutableSortedSet.indexOf")
/* 65 */   public int lastIndexOf(@Nullable Object target) { return indexOf(target); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public boolean contains(Object target) { return (indexOf(target) >= 0); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("super.subListUnchecked does not exist; inherited subList is valid if slow")
/* 82 */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) { return (new RegularImmutableSortedSet(super.subListUnchecked(fromIndex, toIndex), comparator())).asList(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableSortedAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */