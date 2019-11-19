/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RegularImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultiset<E>
/*     */ {
/*     */   private final RegularImmutableSortedSet<E> elementSet;
/*     */   private final int[] counts;
/*     */   private final long[] cumulativeCounts;
/*     */   private final int offset;
/*     */   private final int length;
/*     */   
/*     */   RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, int[] counts, long[] cumulativeCounts, int offset, int length) {
/*  44 */     this.elementSet = elementSet;
/*  45 */     this.counts = counts;
/*  46 */     this.cumulativeCounts = cumulativeCounts;
/*  47 */     this.offset = offset;
/*  48 */     this.length = length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  53 */   Multiset.Entry<E> getEntry(int index) { return Multisets.immutableEntry(this.elementSet.asList().get(index), this.counts[this.offset + index]); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public Multiset.Entry<E> firstEntry() { return getEntry(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public Multiset.Entry<E> lastEntry() { return getEntry(this.length - 1); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/*  70 */     int index = this.elementSet.indexOf(element);
/*  71 */     return (index == -1) ? 0 : this.counts[index + this.offset];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  76 */     long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
/*  77 */     return Ints.saturatedCast(size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public ImmutableSortedSet<E> elementSet() { return this.elementSet; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) { return getSubMultiset(0, this.elementSet.headIndex(upperBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) { return getSubMultiset(this.elementSet.tailIndex(lowerBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)), this.length); }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
/*  97 */     Preconditions.checkPositionIndexes(from, to, this.length);
/*  98 */     if (from == to)
/*  99 */       return emptyMultiset(comparator()); 
/* 100 */     if (from == 0 && to == this.length) {
/* 101 */       return this;
/*     */     }
/* 103 */     RegularImmutableSortedSet<E> subElementSet = (RegularImmutableSortedSet)this.elementSet.getSubSet(from, to);
/*     */     
/* 105 */     return new RegularImmutableSortedMultiset(subElementSet, this.counts, this.cumulativeCounts, this.offset + from, to - from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   boolean isPartialView() { return (this.offset > 0 || this.length < this.counts.length); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */