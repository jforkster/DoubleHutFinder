/*    */ package com.google.common.collect;
/*    */ 
/*    */ import java.util.NavigableSet;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ final class DescendingImmutableSortedMultiset<E>
/*    */   extends ImmutableSortedMultiset<E>
/*    */ {
/*    */   private final ImmutableSortedMultiset<E> forward;
/*    */   
/* 29 */   DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> forward) { this.forward = forward; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public int count(@Nullable Object element) { return this.forward.count(element); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Multiset.Entry<E> firstEntry() { return this.forward.lastEntry(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Multiset.Entry<E> lastEntry() { return this.forward.firstEntry(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public int size() { return this.forward.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public ImmutableSortedSet<E> elementSet() { return this.forward.elementSet().descendingSet(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   Multiset.Entry<E> getEntry(int index) { return (Multiset.Entry)this.forward.entrySet().asList().reverse().get(index); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public ImmutableSortedMultiset<E> descendingMultiset() { return this.forward; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) { return this.forward.tailMultiset(upperBound, boundType).descendingMultiset(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) { return this.forward.headMultiset(lowerBound, boundType).descendingMultiset(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   boolean isPartialView() { return this.forward.isPartialView(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/DescendingImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */