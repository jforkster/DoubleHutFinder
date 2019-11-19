/*     */ package com.google.common.collect;
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class DescendingMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private Comparator<? super E> comparator;
/*     */   private NavigableSet<E> elementSet;
/*     */   private Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  40 */     Comparator<? super E> result = this.comparator;
/*  41 */     if (result == null) {
/*  42 */       return this.comparator = Ordering.from(forwardMultiset().comparator()).reverse();
/*     */     }
/*     */     
/*  45 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  51 */     NavigableSet<E> result = this.elementSet;
/*  52 */     if (result == null) {
/*  53 */       return this.elementSet = new SortedMultisets.NavigableElementSet(this);
/*     */     }
/*  55 */     return result;
/*     */   }
/*     */ 
/*     */   
/*  59 */   public Multiset.Entry<E> pollFirstEntry() { return forwardMultiset().pollLastEntry(); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public Multiset.Entry<E> pollLastEntry() { return forwardMultiset().pollFirstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public SortedMultiset<E> headMultiset(E toElement, BoundType boundType) { return forwardMultiset().tailMultiset(toElement, boundType).descendingMultiset(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public SortedMultiset<E> subMultiset(E fromElement, BoundType fromBoundType, E toElement, BoundType toBoundType) { return forwardMultiset().subMultiset(toElement, toBoundType, fromElement, fromBoundType).descendingMultiset(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public SortedMultiset<E> tailMultiset(E fromElement, BoundType boundType) { return forwardMultiset().headMultiset(fromElement, boundType).descendingMultiset(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   protected Multiset<E> delegate() { return forwardMultiset(); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public SortedMultiset<E> descendingMultiset() { return forwardMultiset(); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public Multiset.Entry<E> firstEntry() { return forwardMultiset().lastEntry(); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public Multiset.Entry<E> lastEntry() { return forwardMultiset().firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 105 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 106 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/* 110 */     return new Multisets.EntrySet<E>()
/*     */       {
/* 112 */         Multiset<E> multiset() { return super.this$0; }
/*     */ 
/*     */ 
/*     */         
/* 116 */         public Iterator<Multiset.Entry<E>> iterator() { return super.this$0.entryIterator(); }
/*     */ 
/*     */ 
/*     */         
/* 120 */         public int size() { return super.this$0.forwardMultiset().entrySet().size(); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public Iterator<E> iterator() { return Multisets.iteratorImpl(this); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public String toString() { return entrySet().toString(); }
/*     */   
/*     */   abstract SortedMultiset<E> forwardMultiset();
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/DescendingMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */