/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class EmptyImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*  38 */   EmptyImmutableSortedSet(Comparator<? super E> comparator) { super(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public int size() { return 0; }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public boolean isEmpty() { return true; }
/*     */ 
/*     */ 
/*     */   
/*  51 */   public boolean contains(@Nullable Object target) { return false; }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public boolean containsAll(Collection<?> targets) { return targets.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public UnmodifiableIterator<E> iterator() { return Iterators.emptyIterator(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*  64 */   public UnmodifiableIterator<E> descendingIterator() { return Iterators.emptyIterator(); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public ImmutableList<E> asList() { return ImmutableList.of(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   int copyIntoArray(Object[] dst, int offset) { return offset; }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  81 */     if (object instanceof Set) {
/*  82 */       Set<?> that = (Set)object;
/*  83 */       return that.isEmpty();
/*     */     } 
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  89 */   public int hashCode() { return 0; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public String toString() { return "[]"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public E first() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public E last() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) { return this; }
/*     */ 
/*     */ 
/*     */   
/* 123 */   int indexOf(@Nullable Object target) { return -1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   ImmutableSortedSet<E> createDescendingSet() { return new EmptyImmutableSortedSet(Ordering.from(this.comparator).reverse()); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */