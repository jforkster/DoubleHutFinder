/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
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
/*     */ class DescendingImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*     */   private final ImmutableSortedSet<E> forward;
/*     */   
/*     */   DescendingImmutableSortedSet(ImmutableSortedSet<E> forward) {
/*  32 */     super(Ordering.from(forward.comparator()).reverse());
/*  33 */     this.forward = forward;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public int size() { return this.forward.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public UnmodifiableIterator<E> iterator() { return this.forward.descendingIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) { return this.forward.tailSet(toElement, inclusive).descendingSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) { return this.forward.headSet(fromElement, inclusive).descendingSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*  65 */   public ImmutableSortedSet<E> descendingSet() { return this.forward; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*  71 */   public UnmodifiableIterator<E> descendingIterator() { return this.forward.iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*  77 */   ImmutableSortedSet<E> createDescendingSet() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public E lower(E element) { return (E)this.forward.higher(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public E floor(E element) { return (E)this.forward.ceiling(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public E ceiling(E element) { return (E)this.forward.floor(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public E higher(E element) { return (E)this.forward.lower(element); }
/*     */ 
/*     */ 
/*     */   
/*     */   int indexOf(@Nullable Object target) {
/* 102 */     int index = this.forward.indexOf(target);
/* 103 */     if (index == -1) {
/* 104 */       return index;
/*     */     }
/* 106 */     return size() - 1 - index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   boolean isPartialView() { return this.forward.isPartialView(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/DescendingImmutableSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */