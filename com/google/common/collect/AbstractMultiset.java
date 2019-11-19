/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultiset<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   private Set<E> elementSet;
/*     */   private Set<Multiset.Entry<E>> entrySet;
/*     */   
/*  52 */   public int size() { return Multisets.sizeImpl(this); }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public boolean isEmpty() { return entrySet().isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public boolean contains(@Nullable Object element) { return (count(element) > 0); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public Iterator<E> iterator() { return Multisets.iteratorImpl(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/*  69 */     for (Multiset.Entry<E> entry : entrySet()) {
/*  70 */       if (Objects.equal(entry.getElement(), element)) {
/*  71 */         return entry.getCount();
/*     */       }
/*     */     } 
/*  74 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(@Nullable E element) {
/*  80 */     add(element, 1);
/*  81 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public int add(@Nullable E element, int occurrences) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public boolean remove(@Nullable Object element) { return (remove(element, 1) > 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public int remove(@Nullable Object element, int occurrences) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public int setCount(@Nullable E element, int count) { return Multisets.setCountImpl(this, element, count); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean setCount(@Nullable E element, int oldCount, int newCount) { return Multisets.setCountImpl(this, element, oldCount, newCount); }
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
/* 117 */   public boolean addAll(Collection<? extends E> elementsToAdd) { return Multisets.addAllImpl(this, elementsToAdd); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public boolean removeAll(Collection<?> elementsToRemove) { return Multisets.removeAllImpl(this, elementsToRemove); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public boolean retainAll(Collection<?> elementsToRetain) { return Multisets.retainAllImpl(this, elementsToRetain); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public void clear() { Iterators.clear(entryIterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/* 138 */     Set<E> result = this.elementSet;
/* 139 */     if (result == null) {
/* 140 */       this.elementSet = result = createElementSet();
/*     */     }
/* 142 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ 
/*     */   
/* 150 */   Set<E> createElementSet() { return new ElementSet(); }
/*     */   
/*     */   abstract int distinctElements();
/*     */   
/*     */   class ElementSet
/*     */     extends Multisets.ElementSet<E> {
/* 156 */     Multiset<E> multiset() { return AbstractMultiset.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 167 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 168 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*     */   }
/*     */   
/*     */   class EntrySet
/*     */     extends Multisets.EntrySet<E> {
/* 173 */     Multiset<E> multiset() { return AbstractMultiset.this; }
/*     */ 
/*     */ 
/*     */     
/* 177 */     public Iterator<Multiset.Entry<E>> iterator() { return AbstractMultiset.this.entryIterator(); }
/*     */ 
/*     */ 
/*     */     
/* 181 */     public int size() { return AbstractMultiset.this.distinctElements(); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 186 */   Set<Multiset.Entry<E>> createEntrySet() { return new EntrySet(); }
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
/* 199 */   public boolean equals(@Nullable Object object) { return Multisets.equalsImpl(this, object); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   public int hashCode() { return entrySet().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   public String toString() { return entrySet().toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */