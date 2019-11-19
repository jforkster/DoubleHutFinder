/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class UnmodifiableSortedMultiset<E>
/*     */   extends Multisets.UnmodifiableMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private UnmodifiableSortedMultiset<E> descendingMultiset;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  36 */   UnmodifiableSortedMultiset(SortedMultiset<E> delegate) { super(delegate); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   protected SortedMultiset<E> delegate() { return (SortedMultiset)super.delegate(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public Comparator<? super E> comparator() { return delegate().comparator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   NavigableSet<E> createElementSet() { return Sets.unmodifiableNavigableSet(delegate().elementSet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public NavigableSet<E> elementSet() { return (NavigableSet)super.elementSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  63 */     UnmodifiableSortedMultiset<E> result = this.descendingMultiset;
/*  64 */     if (result == null) {
/*  65 */       result = new UnmodifiableSortedMultiset<E>(delegate().descendingMultiset());
/*     */       
/*  67 */       result.descendingMultiset = this;
/*  68 */       return this.descendingMultiset = result;
/*     */     } 
/*  70 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public Multiset.Entry<E> firstEntry() { return delegate().firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public Multiset.Entry<E> lastEntry() { return delegate().lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public Multiset.Entry<E> pollFirstEntry() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public Multiset.Entry<E> pollLastEntry() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) { return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(upperBound, boundType)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) { return Multisets.unmodifiableSortedMultiset(delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) { return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(lowerBound, boundType)); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/UnmodifiableSortedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */