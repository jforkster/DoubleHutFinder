/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingCollection<E>
/*     */   extends ForwardingObject
/*     */   implements Collection<E>
/*     */ {
/*  59 */   public Iterator<E> iterator() { return delegate().iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public int size() { return delegate().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public boolean removeAll(Collection<?> collection) { return delegate().removeAll(collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public boolean isEmpty() { return delegate().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean contains(Object object) { return delegate().contains(object); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean add(E element) { return delegate().add(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public boolean remove(Object object) { return delegate().remove(object); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public boolean containsAll(Collection<?> collection) { return delegate().containsAll(collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean addAll(Collection<? extends E> collection) { return delegate().addAll(collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean retainAll(Collection<?> collection) { return delegate().retainAll(collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public void clear() { delegate().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public Object[] toArray() { return delegate().toArray(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public <T> T[] toArray(T[] array) { return (T[])delegate().toArray(array); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   protected boolean standardContains(@Nullable Object object) { return Iterators.contains(iterator(), object); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected boolean standardContainsAll(Collection<?> collection) { return Collections2.containsAllImpl(this, collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   protected boolean standardAddAll(Collection<? extends E> collection) { return Iterators.addAll(this, collection.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardRemove(@Nullable Object object) {
/* 164 */     Iterator<E> iterator = iterator();
/* 165 */     while (iterator.hasNext()) {
/* 166 */       if (Objects.equal(iterator.next(), object)) {
/* 167 */         iterator.remove();
/* 168 */         return true;
/*     */       } 
/*     */     } 
/* 171 */     return false;
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
/* 183 */   protected boolean standardRemoveAll(Collection<?> collection) { return Iterators.removeAll(iterator(), collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   protected boolean standardRetainAll(Collection<?> collection) { return Iterators.retainAll(iterator(), collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   protected void standardClear() { Iterators.clear(iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 219 */   protected boolean standardIsEmpty() { return !iterator().hasNext(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 230 */   protected String standardToString() { return Collections2.toStringImpl(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object[] standardToArray() {
/* 241 */     Object[] newArray = new Object[size()];
/* 242 */     return toArray(newArray);
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
/* 253 */   protected <T> T[] standardToArray(T[] array) { return (T[])ObjectArrays.toArrayImpl(this, array); }
/*     */   
/*     */   protected abstract Collection<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */