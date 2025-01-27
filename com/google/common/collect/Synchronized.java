/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Queue;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ final class Synchronized
/*      */ {
/*      */   static class SynchronizedObject
/*      */     implements Serializable
/*      */   {
/*      */     final Object delegate;
/*      */     final Object mutex;
/*      */     @GwtIncompatible("not needed in emulated source")
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     SynchronizedObject(Object delegate, @Nullable Object mutex) {
/*   68 */       this.delegate = Preconditions.checkNotNull(delegate);
/*   69 */       this.mutex = (mutex == null) ? this : mutex;
/*      */     }
/*      */ 
/*      */     
/*   73 */     Object delegate() { return this.delegate; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   79 */       synchronized (this.mutex) {
/*   80 */         return this.delegate.toString();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectOutputStream")
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*   91 */       synchronized (this.mutex) {
/*   92 */         stream.defaultWriteObject();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  102 */   private static <E> Collection<E> collection(Collection<E> collection, @Nullable Object mutex) { return new SynchronizedCollection(collection, mutex, null); }
/*      */   
/*      */   @VisibleForTesting
/*      */   static class SynchronizedCollection<E>
/*      */     extends SynchronizedObject implements Collection<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  109 */     private SynchronizedCollection(Collection<E> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  114 */     Collection<E> delegate() { return (Collection)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean add(E e) {
/*  119 */       synchronized (this.mutex) {
/*  120 */         return delegate().add(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends E> c) {
/*  126 */       synchronized (this.mutex) {
/*  127 */         return delegate().addAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  133 */       synchronized (this.mutex) {
/*  134 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  140 */       synchronized (this.mutex) {
/*  141 */         return delegate().contains(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  147 */       synchronized (this.mutex) {
/*  148 */         return delegate().containsAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  154 */       synchronized (this.mutex) {
/*  155 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  161 */     public Iterator<E> iterator() { return delegate().iterator(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  166 */       synchronized (this.mutex) {
/*  167 */         return delegate().remove(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  173 */       synchronized (this.mutex) {
/*  174 */         return delegate().removeAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  180 */       synchronized (this.mutex) {
/*  181 */         return delegate().retainAll(c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  187 */       synchronized (this.mutex) {
/*  188 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  194 */       synchronized (this.mutex) {
/*  195 */         return delegate().toArray();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  201 */       synchronized (this.mutex) {
/*  202 */         return (T[])delegate().toArray(a);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  210 */   static <E> Set<E> set(Set<E> set, @Nullable Object mutex) { return new SynchronizedSet(set, mutex); }
/*      */   
/*      */   static class SynchronizedSet<E>
/*      */     extends SynchronizedCollection<E>
/*      */     implements Set<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  217 */     SynchronizedSet(Set<E> delegate, @Nullable Object mutex) { super(delegate, mutex, null); }
/*      */ 
/*      */ 
/*      */     
/*  221 */     Set<E> delegate() { return (Set)super.delegate(); }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  225 */       if (o == this) {
/*  226 */         return true;
/*      */       }
/*  228 */       synchronized (this.mutex) {
/*  229 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  234 */       synchronized (this.mutex) {
/*  235 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  244 */   private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @Nullable Object mutex) { return new SynchronizedSortedSet(set, mutex); }
/*      */   
/*      */   static class SynchronizedSortedSet<E>
/*      */     extends SynchronizedSet<E> implements SortedSet<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  250 */     SynchronizedSortedSet(SortedSet<E> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/*  254 */     SortedSet<E> delegate() { return (SortedSet)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/*  259 */       synchronized (this.mutex) {
/*  260 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> subSet(E fromElement, E toElement) {
/*  266 */       synchronized (this.mutex) {
/*  267 */         return Synchronized.sortedSet(delegate().subSet(fromElement, toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> headSet(E toElement) {
/*  273 */       synchronized (this.mutex) {
/*  274 */         return Synchronized.sortedSet(delegate().headSet(toElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<E> tailSet(E fromElement) {
/*  280 */       synchronized (this.mutex) {
/*  281 */         return Synchronized.sortedSet(delegate().tailSet(fromElement), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E first() {
/*  287 */       synchronized (this.mutex) {
/*  288 */         return (E)delegate().first();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E last() {
/*  294 */       synchronized (this.mutex) {
/*  295 */         return (E)delegate().last();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  303 */   private static <E> List<E> list(List<E> list, @Nullable Object mutex) { return (list instanceof RandomAccess) ? new SynchronizedRandomAccessList(list, mutex) : new SynchronizedList(list, mutex); }
/*      */   
/*      */   private static class SynchronizedList<E>
/*      */     extends SynchronizedCollection<E>
/*      */     implements List<E>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  311 */     SynchronizedList(List<E> delegate, @Nullable Object mutex) { super(delegate, mutex, null); }
/*      */ 
/*      */ 
/*      */     
/*  315 */     List<E> delegate() { return (List)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/*  320 */       synchronized (this.mutex) {
/*  321 */         delegate().add(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/*  327 */       synchronized (this.mutex) {
/*  328 */         return delegate().addAll(index, c);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  334 */       synchronized (this.mutex) {
/*  335 */         return (E)delegate().get(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  341 */       synchronized (this.mutex) {
/*  342 */         return delegate().indexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  348 */       synchronized (this.mutex) {
/*  349 */         return delegate().lastIndexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  355 */     public ListIterator<E> listIterator() { return delegate().listIterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  360 */     public ListIterator<E> listIterator(int index) { return delegate().listIterator(index); }
/*      */ 
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/*  365 */       synchronized (this.mutex) {
/*  366 */         return (E)delegate().remove(index);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/*  372 */       synchronized (this.mutex) {
/*  373 */         return (E)delegate().set(index, element);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public List<E> subList(int fromIndex, int toIndex) {
/*  379 */       synchronized (this.mutex) {
/*  380 */         return Synchronized.list(delegate().subList(fromIndex, toIndex), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  385 */       if (o == this) {
/*  386 */         return true;
/*      */       }
/*  388 */       synchronized (this.mutex) {
/*  389 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  394 */       synchronized (this.mutex) {
/*  395 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SynchronizedRandomAccessList<E>
/*      */     extends SynchronizedList<E>
/*      */     implements RandomAccess {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  405 */     SynchronizedRandomAccessList(List<E> list, @Nullable Object mutex) { super(list, mutex); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object mutex) {
/*  412 */     if (multiset instanceof SynchronizedMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*  414 */       return multiset;
/*      */     }
/*  416 */     return new SynchronizedMultiset(multiset, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultiset<E>
/*      */     extends SynchronizedCollection<E> implements Multiset<E> {
/*      */     Set<E> elementSet;
/*      */     Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  425 */     SynchronizedMultiset(Multiset<E> delegate, @Nullable Object mutex) { super(delegate, mutex, null); }
/*      */ 
/*      */ 
/*      */     
/*  429 */     Multiset<E> delegate() { return (Multiset)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public int count(Object o) {
/*  434 */       synchronized (this.mutex) {
/*  435 */         return delegate().count(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(E e, int n) {
/*  441 */       synchronized (this.mutex) {
/*  442 */         return delegate().add(e, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(Object o, int n) {
/*  448 */       synchronized (this.mutex) {
/*  449 */         return delegate().remove(o, n);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int setCount(E element, int count) {
/*  455 */       synchronized (this.mutex) {
/*  456 */         return delegate().setCount(element, count);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean setCount(E element, int oldCount, int newCount) {
/*  462 */       synchronized (this.mutex) {
/*  463 */         return delegate().setCount(element, oldCount, newCount);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  469 */       synchronized (this.mutex) {
/*  470 */         if (this.elementSet == null) {
/*  471 */           this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
/*      */         }
/*  473 */         return this.elementSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  479 */       synchronized (this.mutex) {
/*  480 */         if (this.entrySet == null) {
/*  481 */           this.entrySet = Synchronized.typePreservingSet(delegate().entrySet(), this.mutex);
/*      */         }
/*  483 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  488 */       if (o == this) {
/*  489 */         return true;
/*      */       }
/*  491 */       synchronized (this.mutex) {
/*  492 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  497 */       synchronized (this.mutex) {
/*  498 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object mutex) {
/*  507 */     if (multimap instanceof SynchronizedMultimap || multimap instanceof ImmutableMultimap)
/*      */     {
/*  509 */       return multimap;
/*      */     }
/*  511 */     return new SynchronizedMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedMultimap<K, V>
/*      */     extends SynchronizedObject
/*      */     implements Multimap<K, V> {
/*      */     Set<K> keySet;
/*      */     Collection<V> valuesCollection;
/*      */     Collection<Map.Entry<K, V>> entries;
/*      */     Map<K, Collection<V>> asMap;
/*      */     Multiset<K> keys;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  524 */     Multimap<K, V> delegate() { return (Multimap)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*  528 */     SynchronizedMultimap(Multimap<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/*  533 */       synchronized (this.mutex) {
/*  534 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  540 */       synchronized (this.mutex) {
/*  541 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  547 */       synchronized (this.mutex) {
/*  548 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/*  554 */       synchronized (this.mutex) {
/*  555 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsEntry(Object key, Object value) {
/*  561 */       synchronized (this.mutex) {
/*  562 */         return delegate().containsEntry(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> get(K key) {
/*  568 */       synchronized (this.mutex) {
/*  569 */         return Synchronized.typePreservingCollection(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean put(K key, V value) {
/*  575 */       synchronized (this.mutex) {
/*  576 */         return delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(K key, Iterable<? extends V> values) {
/*  582 */       synchronized (this.mutex) {
/*  583 */         return delegate().putAll(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  589 */       synchronized (this.mutex) {
/*  590 */         return delegate().putAll(multimap);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/*  596 */       synchronized (this.mutex) {
/*  597 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object key, Object value) {
/*  603 */       synchronized (this.mutex) {
/*  604 */         return delegate().remove(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> removeAll(Object key) {
/*  610 */       synchronized (this.mutex) {
/*  611 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  617 */       synchronized (this.mutex) {
/*  618 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  624 */       synchronized (this.mutex) {
/*  625 */         if (this.keySet == null) {
/*  626 */           this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
/*      */         }
/*  628 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  634 */       synchronized (this.mutex) {
/*  635 */         if (this.valuesCollection == null) {
/*  636 */           this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/*  638 */         return this.valuesCollection;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  644 */       synchronized (this.mutex) {
/*  645 */         if (this.entries == null) {
/*  646 */           this.entries = Synchronized.typePreservingCollection(delegate().entries(), this.mutex);
/*      */         }
/*  648 */         return this.entries;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  654 */       synchronized (this.mutex) {
/*  655 */         if (this.asMap == null) {
/*  656 */           this.asMap = new Synchronized.SynchronizedAsMap(delegate().asMap(), this.mutex);
/*      */         }
/*  658 */         return this.asMap;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  664 */       synchronized (this.mutex) {
/*  665 */         if (this.keys == null) {
/*  666 */           this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
/*      */         }
/*  668 */         return this.keys;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  673 */       if (o == this) {
/*  674 */         return true;
/*      */       }
/*  676 */       synchronized (this.mutex) {
/*  677 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  682 */       synchronized (this.mutex) {
/*  683 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @Nullable Object mutex) {
/*  692 */     if (multimap instanceof SynchronizedListMultimap || multimap instanceof ImmutableListMultimap)
/*      */     {
/*  694 */       return multimap;
/*      */     }
/*  696 */     return new SynchronizedListMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedListMultimap<K, V>
/*      */     extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  703 */     SynchronizedListMultimap(ListMultimap<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */     
/*  706 */     ListMultimap<K, V> delegate() { return (ListMultimap)super.delegate(); }
/*      */     
/*      */     public List<V> get(K key) {
/*  709 */       synchronized (this.mutex) {
/*  710 */         return Synchronized.list(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     public List<V> removeAll(Object key) {
/*  714 */       synchronized (this.mutex) {
/*  715 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public List<V> replaceValues(K key, Iterable<? extends V> values) {
/*  720 */       synchronized (this.mutex) {
/*  721 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @Nullable Object mutex) {
/*  729 */     if (multimap instanceof SynchronizedSetMultimap || multimap instanceof ImmutableSetMultimap)
/*      */     {
/*  731 */       return multimap;
/*      */     }
/*  733 */     return new SynchronizedSetMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSetMultimap<K, V>
/*      */     extends SynchronizedMultimap<K, V>
/*      */     implements SetMultimap<K, V> {
/*      */     Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  742 */     SynchronizedSetMultimap(SetMultimap<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */     
/*  745 */     SetMultimap<K, V> delegate() { return (SetMultimap)super.delegate(); }
/*      */     
/*      */     public Set<V> get(K key) {
/*  748 */       synchronized (this.mutex) {
/*  749 */         return Synchronized.set(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     public Set<V> removeAll(Object key) {
/*  753 */       synchronized (this.mutex) {
/*  754 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/*  759 */       synchronized (this.mutex) {
/*  760 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */     public Set<Map.Entry<K, V>> entries() {
/*  764 */       synchronized (this.mutex) {
/*  765 */         if (this.entrySet == null) {
/*  766 */           this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
/*      */         }
/*  768 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @Nullable Object mutex) {
/*  776 */     if (multimap instanceof SynchronizedSortedSetMultimap) {
/*  777 */       return multimap;
/*      */     }
/*  779 */     return new SynchronizedSortedSetMultimap(multimap, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedSortedSetMultimap<K, V>
/*      */     extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  786 */     SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */     
/*  789 */     SortedSetMultimap<K, V> delegate() { return (SortedSetMultimap)super.delegate(); }
/*      */     
/*      */     public SortedSet<V> get(K key) {
/*  792 */       synchronized (this.mutex) {
/*  793 */         return Synchronized.sortedSet(delegate().get(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     public SortedSet<V> removeAll(Object key) {
/*  797 */       synchronized (this.mutex) {
/*  798 */         return delegate().removeAll(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/*  803 */       synchronized (this.mutex) {
/*  804 */         return delegate().replaceValues(key, values);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Comparator<? super V> valueComparator() {
/*  809 */       synchronized (this.mutex) {
/*  810 */         return delegate().valueComparator();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object mutex) {
/*  818 */     if (collection instanceof SortedSet) {
/*  819 */       return sortedSet((SortedSet)collection, mutex);
/*      */     }
/*  821 */     if (collection instanceof Set) {
/*  822 */       return set((Set)collection, mutex);
/*      */     }
/*  824 */     if (collection instanceof List) {
/*  825 */       return list((List)collection, mutex);
/*      */     }
/*  827 */     return collection(collection, mutex);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object mutex) {
/*  832 */     if (set instanceof SortedSet) {
/*  833 */       return sortedSet((SortedSet)set, mutex);
/*      */     }
/*  835 */     return set(set, mutex);
/*      */   }
/*      */   
/*      */   private static class SynchronizedAsMapEntries<K, V>
/*      */     extends SynchronizedSet<Map.Entry<K, Collection<V>>>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  843 */     SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/*  848 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = super.iterator();
/*  849 */       return new ForwardingIterator<Map.Entry<K, Collection<V>>>()
/*      */         {
/*  851 */           protected Iterator<Map.Entry<K, Collection<V>>> delegate() { return iterator; }
/*      */ 
/*      */           
/*      */           public Map.Entry<K, Collection<V>> next() {
/*  855 */             final Map.Entry<K, Collection<V>> entry = (Map.Entry)super.next();
/*  856 */             return new ForwardingMapEntry<K, Collection<V>>()
/*      */               {
/*  858 */                 protected Map.Entry<K, Collection<V>> delegate() { return entry; }
/*      */ 
/*      */                 
/*  861 */                 public Collection<V> getValue() { return Synchronized.typePreservingCollection((Collection)entry.getValue(), Synchronized.SynchronizedAsMapEntries.this.mutex); }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/*  871 */       synchronized (this.mutex) {
/*  872 */         return ObjectArrays.toArrayImpl(delegate());
/*      */       } 
/*      */     }
/*      */     public <T> T[] toArray(T[] array) {
/*  876 */       synchronized (this.mutex) {
/*  877 */         return (T[])ObjectArrays.toArrayImpl(delegate(), array);
/*      */       } 
/*      */     }
/*      */     public boolean contains(Object o) {
/*  881 */       synchronized (this.mutex) {
/*  882 */         return Maps.containsEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */     public boolean containsAll(Collection<?> c) {
/*  886 */       synchronized (this.mutex) {
/*  887 */         return Collections2.containsAllImpl(delegate(), c);
/*      */       } 
/*      */     }
/*      */     public boolean equals(Object o) {
/*  891 */       if (o == this) {
/*  892 */         return true;
/*      */       }
/*  894 */       synchronized (this.mutex) {
/*  895 */         return Sets.equalsImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */     public boolean remove(Object o) {
/*  899 */       synchronized (this.mutex) {
/*  900 */         return Maps.removeEntryImpl(delegate(), o);
/*      */       } 
/*      */     }
/*      */     public boolean removeAll(Collection<?> c) {
/*  904 */       synchronized (this.mutex) {
/*  905 */         return Iterators.removeAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */     public boolean retainAll(Collection<?> c) {
/*  909 */       synchronized (this.mutex) {
/*  910 */         return Iterators.retainAll(delegate().iterator(), c);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*  919 */   static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object mutex) { return new SynchronizedMap(map, mutex); }
/*      */   
/*      */   private static class SynchronizedMap<K, V>
/*      */     extends SynchronizedObject
/*      */     implements Map<K, V> {
/*      */     Set<K> keySet;
/*      */     Collection<V> values;
/*      */     Set<Map.Entry<K, V>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  929 */     SynchronizedMap(Map<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  934 */     Map<K, V> delegate() { return (Map)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  939 */       synchronized (this.mutex) {
/*  940 */         delegate().clear();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  946 */       synchronized (this.mutex) {
/*  947 */         return delegate().containsKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/*  953 */       synchronized (this.mutex) {
/*  954 */         return delegate().containsValue(value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/*  960 */       synchronized (this.mutex) {
/*  961 */         if (this.entrySet == null) {
/*  962 */           this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
/*      */         }
/*  964 */         return this.entrySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/*  970 */       synchronized (this.mutex) {
/*  971 */         return (V)delegate().get(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  977 */       synchronized (this.mutex) {
/*  978 */         return delegate().isEmpty();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  984 */       synchronized (this.mutex) {
/*  985 */         if (this.keySet == null) {
/*  986 */           this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
/*      */         }
/*  988 */         return this.keySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/*  994 */       synchronized (this.mutex) {
/*  995 */         return (V)delegate().put(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 1001 */       synchronized (this.mutex) {
/* 1002 */         delegate().putAll(map);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 1008 */       synchronized (this.mutex) {
/* 1009 */         return (V)delegate().remove(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1015 */       synchronized (this.mutex) {
/* 1016 */         return delegate().size();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 1022 */       synchronized (this.mutex) {
/* 1023 */         if (this.values == null) {
/* 1024 */           this.values = Synchronized.collection(delegate().values(), this.mutex);
/*      */         }
/* 1026 */         return this.values;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/* 1031 */       if (o == this) {
/* 1032 */         return true;
/*      */       }
/* 1034 */       synchronized (this.mutex) {
/* 1035 */         return delegate().equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1040 */       synchronized (this.mutex) {
/* 1041 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1050 */   static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @Nullable Object mutex) { return new SynchronizedSortedMap(sortedMap, mutex); }
/*      */   
/*      */   static class SynchronizedSortedMap<K, V>
/*      */     extends SynchronizedMap<K, V>
/*      */     implements SortedMap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1057 */     SynchronizedSortedMap(SortedMap<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/* 1061 */     SortedMap<K, V> delegate() { return (SortedMap)super.delegate(); }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1065 */       synchronized (this.mutex) {
/* 1066 */         return delegate().comparator();
/*      */       } 
/*      */     }
/*      */     
/*      */     public K firstKey() {
/* 1071 */       synchronized (this.mutex) {
/* 1072 */         return (K)delegate().firstKey();
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1077 */       synchronized (this.mutex) {
/* 1078 */         return Synchronized.sortedMap(delegate().headMap(toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public K lastKey() {
/* 1083 */       synchronized (this.mutex) {
/* 1084 */         return (K)delegate().lastKey();
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1089 */       synchronized (this.mutex) {
/* 1090 */         return Synchronized.sortedMap(delegate().subMap(fromKey, toKey), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1095 */       synchronized (this.mutex) {
/* 1096 */         return Synchronized.sortedMap(delegate().tailMap(fromKey), this.mutex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @Nullable Object mutex) {
/* 1104 */     if (bimap instanceof SynchronizedBiMap || bimap instanceof ImmutableBiMap)
/*      */     {
/* 1106 */       return bimap;
/*      */     }
/* 1108 */     return new SynchronizedBiMap(bimap, mutex, null, null);
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
/*      */     private Set<V> valueSet;
/*      */     private BiMap<V, K> inverse;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private SynchronizedBiMap(BiMap<K, V> delegate, @Nullable Object mutex, @Nullable BiMap<V, K> inverse) {
/* 1118 */       super(delegate, mutex);
/* 1119 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/* 1123 */     BiMap<K, V> delegate() { return (BiMap)super.delegate(); }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1127 */       synchronized (this.mutex) {
/* 1128 */         if (this.valueSet == null) {
/* 1129 */           this.valueSet = Synchronized.set(delegate().values(), this.mutex);
/*      */         }
/* 1131 */         return this.valueSet;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1137 */       synchronized (this.mutex) {
/* 1138 */         return (V)delegate().forcePut(key, value);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1144 */       synchronized (this.mutex) {
/* 1145 */         if (this.inverse == null) {
/* 1146 */           this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
/*      */         }
/*      */         
/* 1149 */         return this.inverse;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class SynchronizedAsMap<K, V>
/*      */     extends SynchronizedMap<K, Collection<V>>
/*      */   {
/*      */     Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
/*      */     Collection<Collection<V>> asMapValues;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1162 */     SynchronizedAsMap(Map<K, Collection<V>> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1166 */       synchronized (this.mutex) {
/* 1167 */         Collection<V> collection = (Collection)super.get(key);
/* 1168 */         return (collection == null) ? null : Synchronized.typePreservingCollection(collection, this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 1174 */       synchronized (this.mutex) {
/* 1175 */         if (this.asMapEntrySet == null) {
/* 1176 */           this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries(delegate().entrySet(), this.mutex);
/*      */         }
/*      */         
/* 1179 */         return this.asMapEntrySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Collection<Collection<V>> values() {
/* 1184 */       synchronized (this.mutex) {
/* 1185 */         if (this.asMapValues == null) {
/* 1186 */           this.asMapValues = new Synchronized.SynchronizedAsMapValues(delegate().values(), this.mutex);
/*      */         }
/*      */         
/* 1189 */         return this.asMapValues;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1195 */     public boolean containsValue(Object o) { return values().contains(o); }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class SynchronizedAsMapValues<V>
/*      */     extends SynchronizedCollection<Collection<V>>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/* 1205 */     SynchronizedAsMapValues(Collection<Collection<V>> delegate, @Nullable Object mutex) { super(delegate, mutex, null); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Collection<V>> iterator() {
/* 1210 */       final Iterator<Collection<V>> iterator = super.iterator();
/* 1211 */       return new ForwardingIterator<Collection<V>>()
/*      */         {
/* 1213 */           protected Iterator<Collection<V>> delegate() { return iterator; }
/*      */ 
/*      */           
/* 1216 */           public Collection<V> next() { return Synchronized.typePreservingCollection((Collection)super.next(), Synchronized.SynchronizedAsMapValues.this.mutex); }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   @VisibleForTesting
/*      */   static class SynchronizedNavigableSet<E>
/*      */     extends SynchronizedSortedSet<E>
/*      */     implements NavigableSet<E> {
/*      */     NavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1229 */     SynchronizedNavigableSet(NavigableSet<E> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/* 1233 */     NavigableSet<E> delegate() { return (NavigableSet)super.delegate(); }
/*      */ 
/*      */     
/*      */     public E ceiling(E e) {
/* 1237 */       synchronized (this.mutex) {
/* 1238 */         return (E)delegate().ceiling(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1243 */     public Iterator<E> descendingIterator() { return delegate().descendingIterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1249 */       synchronized (this.mutex) {
/* 1250 */         if (this.descendingSet == null) {
/* 1251 */           NavigableSet<E> dS = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
/*      */           
/* 1253 */           this.descendingSet = dS;
/* 1254 */           return dS;
/*      */         } 
/* 1256 */         return this.descendingSet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public E floor(E e) {
/* 1261 */       synchronized (this.mutex) {
/* 1262 */         return (E)delegate().floor(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1267 */       synchronized (this.mutex) {
/* 1268 */         return Synchronized.navigableSet(delegate().headSet(toElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E higher(E e) {
/* 1274 */       synchronized (this.mutex) {
/* 1275 */         return (E)delegate().higher(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public E lower(E e) {
/* 1280 */       synchronized (this.mutex) {
/* 1281 */         return (E)delegate().lower(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public E pollFirst() {
/* 1286 */       synchronized (this.mutex) {
/* 1287 */         return (E)delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */     
/*      */     public E pollLast() {
/* 1292 */       synchronized (this.mutex) {
/* 1293 */         return (E)delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1299 */       synchronized (this.mutex) {
/* 1300 */         return Synchronized.navigableSet(delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1306 */       synchronized (this.mutex) {
/* 1307 */         return Synchronized.navigableSet(delegate().tailSet(fromElement, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1313 */     public SortedSet<E> headSet(E toElement) { return headSet(toElement, false); }
/*      */ 
/*      */ 
/*      */     
/* 1317 */     public SortedSet<E> subSet(E fromElement, E toElement) { return subSet(fromElement, true, toElement, false); }
/*      */ 
/*      */ 
/*      */     
/* 1321 */     public SortedSet<E> tailSet(E fromElement) { return tailSet(fromElement, true); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/* 1330 */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @Nullable Object mutex) { return new SynchronizedNavigableSet(navigableSet, mutex); }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/* 1335 */   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) { return navigableSet(navigableSet, null); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 1341 */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) { return navigableMap(navigableMap, null); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 1347 */   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @Nullable Object mutex) { return new SynchronizedNavigableMap(navigableMap, mutex); }
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   @VisibleForTesting
/*      */   static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> { NavigableSet<K> descendingKeySet;
/*      */     NavigableMap<K, V> descendingMap;
/*      */     NavigableSet<K> navigableKeySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1356 */     SynchronizedNavigableMap(NavigableMap<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/* 1360 */     NavigableMap<K, V> delegate() { return (NavigableMap)super.delegate(); }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 1364 */       synchronized (this.mutex) {
/* 1365 */         return Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public K ceilingKey(K key) {
/* 1370 */       synchronized (this.mutex) {
/* 1371 */         return (K)delegate().ceilingKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 1378 */       synchronized (this.mutex) {
/* 1379 */         if (this.descendingKeySet == null) {
/* 1380 */           return this.descendingKeySet = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
/*      */         }
/*      */         
/* 1383 */         return this.descendingKeySet;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1390 */       synchronized (this.mutex) {
/* 1391 */         if (this.descendingMap == null) {
/* 1392 */           return this.descendingMap = Synchronized.navigableMap(delegate().descendingMap(), this.mutex);
/*      */         }
/*      */         
/* 1395 */         return this.descendingMap;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 1400 */       synchronized (this.mutex) {
/* 1401 */         return Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 1406 */       synchronized (this.mutex) {
/* 1407 */         return Synchronized.nullableSynchronizedEntry(delegate().floorEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public K floorKey(K key) {
/* 1412 */       synchronized (this.mutex) {
/* 1413 */         return (K)delegate().floorKey(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 1418 */       synchronized (this.mutex) {
/* 1419 */         return Synchronized.navigableMap(delegate().headMap(toKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 1425 */       synchronized (this.mutex) {
/* 1426 */         return Synchronized.nullableSynchronizedEntry(delegate().higherEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public K higherKey(K key) {
/* 1431 */       synchronized (this.mutex) {
/* 1432 */         return (K)delegate().higherKey(key);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 1437 */       synchronized (this.mutex) {
/* 1438 */         return Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 1443 */       synchronized (this.mutex) {
/* 1444 */         return Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(key), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public K lowerKey(K key) {
/* 1449 */       synchronized (this.mutex) {
/* 1450 */         return (K)delegate().lowerKey(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 1455 */     public Set<K> keySet() { return navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1461 */       synchronized (this.mutex) {
/* 1462 */         if (this.navigableKeySet == null) {
/* 1463 */           return this.navigableKeySet = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
/*      */         }
/*      */         
/* 1466 */         return this.navigableKeySet;
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 1471 */       synchronized (this.mutex) {
/* 1472 */         return Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 1477 */       synchronized (this.mutex) {
/* 1478 */         return Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1484 */       synchronized (this.mutex) {
/* 1485 */         return Synchronized.navigableMap(delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1492 */       synchronized (this.mutex) {
/* 1493 */         return Synchronized.navigableMap(delegate().tailMap(fromKey, inclusive), this.mutex);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1499 */     public SortedMap<K, V> headMap(K toKey) { return headMap(toKey, false); }
/*      */ 
/*      */ 
/*      */     
/* 1503 */     public SortedMap<K, V> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*      */ 
/*      */ 
/*      */     
/* 1507 */     public SortedMap<K, V> tailMap(K fromKey) { return tailMap(fromKey, true); } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("works but is needed only for NavigableMap")
/*      */   private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@Nullable Map.Entry<K, V> entry, @Nullable Object mutex) {
/* 1516 */     if (entry == null) {
/* 1517 */       return null;
/*      */     }
/* 1519 */     return new SynchronizedEntry(entry, mutex);
/*      */   }
/*      */   
/*      */   @GwtIncompatible("works but is needed only for NavigableMap")
/*      */   private static class SynchronizedEntry<K, V>
/*      */     extends SynchronizedObject implements Map.Entry<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1527 */     SynchronizedEntry(Map.Entry<K, V> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1532 */     Map.Entry<K, V> delegate() { return (Map.Entry)super.delegate(); }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1536 */       synchronized (this.mutex) {
/* 1537 */         return delegate().equals(obj);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1542 */       synchronized (this.mutex) {
/* 1543 */         return delegate().hashCode();
/*      */       } 
/*      */     }
/*      */     
/*      */     public K getKey() {
/* 1548 */       synchronized (this.mutex) {
/* 1549 */         return (K)delegate().getKey();
/*      */       } 
/*      */     }
/*      */     
/*      */     public V getValue() {
/* 1554 */       synchronized (this.mutex) {
/* 1555 */         return (V)delegate().getValue();
/*      */       } 
/*      */     }
/*      */     
/*      */     public V setValue(V value) {
/* 1560 */       synchronized (this.mutex) {
/* 1561 */         return (V)delegate().setValue(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1569 */   static <E> Queue<E> queue(Queue<E> queue, @Nullable Object mutex) { return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue(queue, mutex); }
/*      */ 
/*      */   
/*      */   private static class SynchronizedQueue<E>
/*      */     extends SynchronizedCollection<E>
/*      */     implements Queue<E>
/*      */   {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1578 */     SynchronizedQueue(Queue<E> delegate, @Nullable Object mutex) { super(delegate, mutex, null); }
/*      */ 
/*      */ 
/*      */     
/* 1582 */     Queue<E> delegate() { return (Queue)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public E element() {
/* 1587 */       synchronized (this.mutex) {
/* 1588 */         return (E)delegate().element();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offer(E e) {
/* 1594 */       synchronized (this.mutex) {
/* 1595 */         return delegate().offer(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1601 */       synchronized (this.mutex) {
/* 1602 */         return (E)delegate().peek();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E poll() {
/* 1608 */       synchronized (this.mutex) {
/* 1609 */         return (E)delegate().poll();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove() {
/* 1615 */       synchronized (this.mutex) {
/* 1616 */         return (E)delegate().remove();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("Deque")
/* 1625 */   static <E> Deque<E> deque(Deque<E> deque, @Nullable Object mutex) { return new SynchronizedDeque(deque, mutex); }
/*      */   
/*      */   @GwtIncompatible("Deque")
/*      */   private static final class SynchronizedDeque<E>
/*      */     extends SynchronizedQueue<E>
/*      */     implements Deque<E> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1633 */     SynchronizedDeque(Deque<E> delegate, @Nullable Object mutex) { super(delegate, mutex); }
/*      */ 
/*      */ 
/*      */     
/* 1637 */     Deque<E> delegate() { return (Deque)super.delegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void addFirst(E e) {
/* 1642 */       synchronized (this.mutex) {
/* 1643 */         delegate().addFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void addLast(E e) {
/* 1649 */       synchronized (this.mutex) {
/* 1650 */         delegate().addLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerFirst(E e) {
/* 1656 */       synchronized (this.mutex) {
/* 1657 */         return delegate().offerFirst(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean offerLast(E e) {
/* 1663 */       synchronized (this.mutex) {
/* 1664 */         return delegate().offerLast(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeFirst() {
/* 1670 */       synchronized (this.mutex) {
/* 1671 */         return (E)delegate().removeFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E removeLast() {
/* 1677 */       synchronized (this.mutex) {
/* 1678 */         return (E)delegate().removeLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollFirst() {
/* 1684 */       synchronized (this.mutex) {
/* 1685 */         return (E)delegate().pollFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pollLast() {
/* 1691 */       synchronized (this.mutex) {
/* 1692 */         return (E)delegate().pollLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getFirst() {
/* 1698 */       synchronized (this.mutex) {
/* 1699 */         return (E)delegate().getFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E getLast() {
/* 1705 */       synchronized (this.mutex) {
/* 1706 */         return (E)delegate().getLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peekFirst() {
/* 1712 */       synchronized (this.mutex) {
/* 1713 */         return (E)delegate().peekFirst();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E peekLast() {
/* 1719 */       synchronized (this.mutex) {
/* 1720 */         return (E)delegate().peekLast();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeFirstOccurrence(Object o) {
/* 1726 */       synchronized (this.mutex) {
/* 1727 */         return delegate().removeFirstOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeLastOccurrence(Object o) {
/* 1733 */       synchronized (this.mutex) {
/* 1734 */         return delegate().removeLastOccurrence(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void push(E e) {
/* 1740 */       synchronized (this.mutex) {
/* 1741 */         delegate().push(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public E pop() {
/* 1747 */       synchronized (this.mutex) {
/* 1748 */         return (E)delegate().pop();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> descendingIterator() {
/* 1754 */       synchronized (this.mutex) {
/* 1755 */         return delegate().descendingIterator();
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Synchronized.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */