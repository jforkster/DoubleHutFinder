/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
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
/*     */ public abstract class ForwardingMultiset<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*  62 */   public int count(Object element) { return delegate().count(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public int add(E element, int occurrences) { return delegate().add(element, occurrences); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public int remove(Object element, int occurrences) { return delegate().remove(element, occurrences); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public Set<E> elementSet() { return delegate().elementSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public Set<Multiset.Entry<E>> entrySet() { return delegate().entrySet(); }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public boolean equals(@Nullable Object object) { return (object == this || delegate().equals(object)); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public int hashCode() { return delegate().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public int setCount(E element, int count) { return delegate().setCount(element, count); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public boolean setCount(E element, int oldCount, int newCount) { return delegate().setCount(element, oldCount, newCount); }
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
/* 111 */   protected boolean standardContains(@Nullable Object object) { return (count(object) > 0); }
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
/* 122 */   protected void standardClear() { Iterators.clear(entrySet().iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected int standardCount(@Nullable Object object) {
/* 133 */     for (Multiset.Entry<?> entry : entrySet()) {
/* 134 */       if (Objects.equal(entry.getElement(), object)) {
/* 135 */         return entry.getCount();
/*     */       }
/*     */     } 
/* 138 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAdd(E element) {
/* 149 */     add(element, 1);
/* 150 */     return true;
/*     */   }
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
/* 163 */   protected boolean standardAddAll(Collection<? extends E> elementsToAdd) { return Multisets.addAllImpl(this, elementsToAdd); }
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
/* 175 */   protected boolean standardRemove(Object element) { return (remove(element, 1) > 0); }
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
/* 188 */   protected boolean standardRemoveAll(Collection<?> elementsToRemove) { return Multisets.removeAllImpl(this, elementsToRemove); }
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
/* 201 */   protected boolean standardRetainAll(Collection<?> elementsToRetain) { return Multisets.retainAllImpl(this, elementsToRetain); }
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
/* 214 */   protected int standardSetCount(E element, int count) { return Multisets.setCountImpl(this, element, count); }
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
/* 226 */   protected boolean standardSetCount(E element, int oldCount, int newCount) { return Multisets.setCountImpl(this, element, oldCount, newCount); }
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
/*     */   @Beta
/*     */   protected class StandardElementSet
/*     */     extends Multisets.ElementSet<E>
/*     */   {
/* 249 */     Multiset<E> multiset() { return ForwardingMultiset.this; }
/*     */   }
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
/* 261 */   protected Iterator<E> standardIterator() { return Multisets.iteratorImpl(this); }
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
/* 272 */   protected int standardSize() { return Multisets.sizeImpl(this); }
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
/* 284 */   protected boolean standardEquals(@Nullable Object object) { return Multisets.equalsImpl(this, object); }
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
/* 295 */   protected int standardHashCode() { return entrySet().hashCode(); }
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
/* 306 */   protected String standardToString() { return entrySet().toString(); }
/*     */   
/*     */   protected abstract Multiset<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */