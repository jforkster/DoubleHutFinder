/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ final class EmptyImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultiset<E>
/*     */ {
/*     */   private final ImmutableSortedSet<E> elementSet;
/*     */   
/*  34 */   EmptyImmutableSortedMultiset(Comparator<? super E> comparator) { this.elementSet = ImmutableSortedSet.emptySet(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public Multiset.Entry<E> firstEntry() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public Multiset.Entry<E> lastEntry() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public int count(@Nullable Object element) { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public boolean containsAll(Collection<?> targets) { return targets.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public int size() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public ImmutableSortedSet<E> elementSet() { return this.elementSet; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   Multiset.Entry<E> getEntry(int index) { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/*  74 */     Preconditions.checkNotNull(upperBound);
/*  75 */     Preconditions.checkNotNull(boundType);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/*  81 */     Preconditions.checkNotNull(lowerBound);
/*  82 */     Preconditions.checkNotNull(boundType);
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public UnmodifiableIterator<E> iterator() { return Iterators.emptyIterator(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  93 */     if (object instanceof Multiset) {
/*  94 */       Multiset<?> other = (Multiset)object;
/*  95 */       return other.isEmpty();
/*     */     } 
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 102 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   int copyIntoArray(Object[] dst, int offset) { return offset; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public ImmutableList<E> asList() { return ImmutableList.of(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */