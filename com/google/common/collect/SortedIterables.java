/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
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
/*    */ final class SortedIterables
/*    */ {
/*    */   public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements) {
/*    */     Comparator<?> comparator2;
/* 38 */     Preconditions.checkNotNull(comparator);
/* 39 */     Preconditions.checkNotNull(elements);
/*    */     
/* 41 */     if (elements instanceof SortedSet) {
/* 42 */       comparator2 = comparator((SortedSet)elements);
/* 43 */     } else if (elements instanceof SortedIterable) {
/* 44 */       comparator2 = ((SortedIterable)elements).comparator();
/*    */     } else {
/* 46 */       return false;
/*    */     } 
/* 48 */     return comparator.equals(comparator2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet) {
/* 54 */     Comparator<? super E> result = sortedSet.comparator();
/* 55 */     if (result == null) {
/* 56 */       result = Ordering.natural();
/*    */     }
/* 58 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SortedIterables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */