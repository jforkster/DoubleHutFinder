/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class EmptyContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*  34 */   EmptyContiguousSet(DiscreteDomain<C> domain) { super(domain); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public C first() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public C last() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public int size() { return 0; }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public ContiguousSet<C> intersection(ContiguousSet<C> other) { return this; }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public Range<C> range() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) { return this; }
/*     */ 
/*     */ 
/*     */   
/*  71 */   ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive) { return this; }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("not used by GWT emulation")
/*  76 */   int indexOf(Object target) { return -1; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public UnmodifiableIterator<C> iterator() { return Iterators.emptyIterator(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*  85 */   public UnmodifiableIterator<C> descendingIterator() { return Iterators.emptyIterator(); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public boolean isEmpty() { return true; }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public ImmutableList<C> asList() { return ImmutableList.of(); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public String toString() { return "[]"; }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 105 */     if (object instanceof Set) {
/* 106 */       Set<?> that = (Set)object;
/* 107 */       return that.isEmpty();
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 113 */   public int hashCode() { return 0; }
/*     */   
/*     */   @GwtIncompatible("serialization")
/*     */   private static final class SerializedForm<C extends Comparable>
/*     */     extends Object implements Serializable {
/*     */     private final DiscreteDomain<C> domain;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 121 */     private SerializedForm(DiscreteDomain<C> domain) { this.domain = domain; }
/*     */ 
/*     */ 
/*     */     
/* 125 */     private Object readResolve() { return new EmptyContiguousSet(this.domain); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("serialization")
/* 134 */   Object writeReplace() { return new SerializedForm(this.domain, null); }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 139 */   ImmutableSortedSet<C> createDescendingSet() { return new EmptyImmutableSortedSet(Ordering.natural().reverse()); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */