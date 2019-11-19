/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ public abstract class ForwardingNavigableSet<E>
/*     */   extends ForwardingSortedSet<E>
/*     */   implements NavigableSet<E>
/*     */ {
/*  58 */   public E lower(E e) { return (E)delegate().lower(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected E standardLower(E e) { return (E)Iterators.getNext(headSet(e, false).descendingIterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public E floor(E e) { return (E)delegate().floor(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   protected E standardFloor(E e) { return (E)Iterators.getNext(headSet(e, true).descendingIterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public E ceiling(E e) { return (E)delegate().ceiling(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected E standardCeiling(E e) { return (E)Iterators.getNext(tailSet(e, true).iterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public E higher(E e) { return (E)delegate().higher(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   protected E standardHigher(E e) { return (E)Iterators.getNext(tailSet(e, false).iterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public E pollFirst() { return (E)delegate().pollFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected E standardPollFirst() { return (E)Iterators.pollNext(iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public E pollLast() { return (E)delegate().pollLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   protected E standardPollLast() { return (E)Iterators.pollNext(descendingIterator()); }
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected E standardFirst() { return (E)iterator().next(); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   protected E standardLast() { return (E)descendingIterator().next(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public NavigableSet<E> descendingSet() { return delegate().descendingSet(); }
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
/*     */   @Beta
/*     */   protected class StandardDescendingSet
/*     */     extends Sets.DescendingSet<E>
/*     */   {
/*     */     public StandardDescendingSet() {
/* 166 */       super(ForwardingNavigableSet.this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public Iterator<E> descendingIterator() { return delegate().descendingIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive); }
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
/*     */   @Beta
/* 195 */   protected NavigableSet<E> standardSubSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive); }
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
/* 206 */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) { return subSet(fromElement, true, toElement, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public NavigableSet<E> headSet(E toElement, boolean inclusive) { return delegate().headSet(toElement, inclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   protected SortedSet<E> standardHeadSet(E toElement) { return headSet(toElement, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { return delegate().tailSet(fromElement, inclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   protected SortedSet<E> standardTailSet(E fromElement) { return tailSet(fromElement, true); }
/*     */   
/*     */   protected abstract NavigableSet<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingNavigableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */