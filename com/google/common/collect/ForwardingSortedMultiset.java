/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ForwardingSortedMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*  54 */   public NavigableSet<E> elementSet() { return (NavigableSet)super.elementSet(); }
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
/*     */   protected class StandardElementSet
/*     */     extends SortedMultisets.NavigableElementSet<E>
/*     */   {
/*     */     public StandardElementSet() {
/*  71 */       super(ForwardingSortedMultiset.this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public Comparator<? super E> comparator() { return delegate().comparator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public SortedMultiset<E> descendingMultiset() { return delegate().descendingMultiset(); }
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
/*     */   protected abstract class StandardDescendingMultiset
/*     */     extends DescendingMultiset<E>
/*     */   {
/* 102 */     SortedMultiset<E> forwardMultiset() { return ForwardingSortedMultiset.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public Multiset.Entry<E> firstEntry() { return delegate().firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardFirstEntry() {
/* 118 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 119 */     if (!entryIterator.hasNext()) {
/* 120 */       return null;
/*     */     }
/* 122 */     Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/* 123 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public Multiset.Entry<E> lastEntry() { return delegate().lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardLastEntry() {
/* 139 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/*     */ 
/*     */     
/* 142 */     if (!entryIterator.hasNext()) {
/* 143 */       return null;
/*     */     }
/* 145 */     Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/* 146 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public Multiset.Entry<E> pollFirstEntry() { return delegate().pollFirstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollFirstEntry() {
/* 161 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 162 */     if (!entryIterator.hasNext()) {
/* 163 */       return null;
/*     */     }
/* 165 */     Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/* 166 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 167 */     entryIterator.remove();
/* 168 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public Multiset.Entry<E> pollLastEntry() { return delegate().pollLastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollLastEntry() {
/* 184 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/*     */ 
/*     */     
/* 187 */     if (!entryIterator.hasNext()) {
/* 188 */       return null;
/*     */     }
/* 190 */     Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/* 191 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 192 */     entryIterator.remove();
/* 193 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) { return delegate().headMultiset(upperBound, boundType); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) { return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType); }
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
/* 217 */   protected SortedMultiset<E> standardSubMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) { return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) { return delegate().tailMultiset(lowerBound, boundType); }
/*     */   
/*     */   protected abstract SortedMultiset<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */