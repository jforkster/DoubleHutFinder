/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ final class SortedMultisets
/*     */ {
/*     */   static class ElementSet<E>
/*     */     extends Multisets.ElementSet<E>
/*     */     implements SortedSet<E>
/*     */   {
/*     */     private final SortedMultiset<E> multiset;
/*     */     
/*  53 */     ElementSet(SortedMultiset<E> multiset) { this.multiset = multiset; }
/*     */ 
/*     */ 
/*     */     
/*  57 */     final SortedMultiset<E> multiset() { return this.multiset; }
/*     */ 
/*     */ 
/*     */     
/*  61 */     public Comparator<? super E> comparator() { return multiset().comparator(); }
/*     */ 
/*     */ 
/*     */     
/*  65 */     public SortedSet<E> subSet(E fromElement, E toElement) { return multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet(); }
/*     */ 
/*     */ 
/*     */     
/*  69 */     public SortedSet<E> headSet(E toElement) { return multiset().headMultiset(toElement, BoundType.OPEN).elementSet(); }
/*     */ 
/*     */ 
/*     */     
/*  73 */     public SortedSet<E> tailSet(E fromElement) { return multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet(); }
/*     */ 
/*     */ 
/*     */     
/*  77 */     public E first() { return (E)SortedMultisets.getElementOrThrow(multiset().firstEntry()); }
/*     */ 
/*     */ 
/*     */     
/*  81 */     public E last() { return (E)SortedMultisets.getElementOrThrow(multiset().lastEntry()); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Navigable")
/*     */   static class NavigableElementSet<E>
/*     */     extends ElementSet<E>
/*     */     implements NavigableSet<E>
/*     */   {
/*  91 */     NavigableElementSet(SortedMultiset<E> multiset) { super(multiset); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     public E lower(E e) { return (E)SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.OPEN).lastEntry()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     public E floor(E e) { return (E)SortedMultisets.getElementOrNull(multiset().headMultiset(e, BoundType.CLOSED).lastEntry()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     public E ceiling(E e) { return (E)SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.CLOSED).firstEntry()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     public E higher(E e) { return (E)SortedMultisets.getElementOrNull(multiset().tailMultiset(e, BoundType.OPEN).firstEntry()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 116 */     public NavigableSet<E> descendingSet() { return new NavigableElementSet(multiset().descendingMultiset()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     public Iterator<E> descendingIterator() { return descendingSet().iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     public E pollFirst() { return (E)SortedMultisets.getElementOrNull(multiset().pollFirstEntry()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     public E pollLast() { return (E)SortedMultisets.getElementOrNull(multiset().pollLastEntry()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return new NavigableElementSet(multiset().subMultiset(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     public NavigableSet<E> headSet(E toElement, boolean inclusive) { return new NavigableElementSet(multiset().headMultiset(toElement, BoundType.forBoolean(inclusive))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { return new NavigableElementSet(multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive))); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> E getElementOrThrow(Multiset.Entry<E> entry) {
/* 156 */     if (entry == null) {
/* 157 */       throw new NoSuchElementException();
/*     */     }
/* 159 */     return (E)entry.getElement();
/*     */   }
/*     */ 
/*     */   
/* 163 */   private static <E> E getElementOrNull(@Nullable Multiset.Entry<E> entry) { return (E)((entry == null) ? null : entry.getElement()); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SortedMultisets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */