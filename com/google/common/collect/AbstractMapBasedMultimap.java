/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
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
/*      */ abstract class AbstractMapBasedMultimap<K, V>
/*      */   extends AbstractMultimap<K, V>
/*      */   implements Serializable
/*      */ {
/*      */   private Map<K, Collection<V>> map;
/*      */   private int totalSize;
/*      */   private static final long serialVersionUID = 2447537837011683357L;
/*      */   
/*      */   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
/*  123 */     Preconditions.checkArgument(map.isEmpty());
/*  124 */     this.map = map;
/*      */   }
/*      */ 
/*      */   
/*      */   final void setMap(Map<K, Collection<V>> map) {
/*  129 */     this.map = map;
/*  130 */     this.totalSize = 0;
/*  131 */     for (Collection<V> values : map.values()) {
/*  132 */       Preconditions.checkArgument(!values.isEmpty());
/*  133 */       this.totalSize += values.size();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  143 */   Collection<V> createUnmodifiableEmptyCollection() { return unmodifiableCollectionSubclass(createCollection()); }
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
/*  169 */   Collection<V> createCollection(@Nullable K key) { return createCollection(); }
/*      */ 
/*      */ 
/*      */   
/*  173 */   Map<K, Collection<V>> backingMap() { return this.map; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   public int size() { return this.totalSize; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  185 */   public boolean containsKey(@Nullable Object key) { return this.map.containsKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean put(@Nullable K key, @Nullable V value) {
/*  192 */     Collection<V> collection = (Collection)this.map.get(key);
/*  193 */     if (collection == null) {
/*  194 */       collection = createCollection(key);
/*  195 */       if (collection.add(value)) {
/*  196 */         this.totalSize++;
/*  197 */         this.map.put(key, collection);
/*  198 */         return true;
/*      */       } 
/*  200 */       throw new AssertionError("New Collection violated the Collection spec");
/*      */     } 
/*  202 */     if (collection.add(value)) {
/*  203 */       this.totalSize++;
/*  204 */       return true;
/*      */     } 
/*  206 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<V> getOrCreateCollection(@Nullable K key) {
/*  211 */     Collection<V> collection = (Collection)this.map.get(key);
/*  212 */     if (collection == null) {
/*  213 */       collection = createCollection(key);
/*  214 */       this.map.put(key, collection);
/*      */     } 
/*  216 */     return collection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/*  228 */     Iterator<? extends V> iterator = values.iterator();
/*  229 */     if (!iterator.hasNext()) {
/*  230 */       return removeAll(key);
/*      */     }
/*      */ 
/*      */     
/*  234 */     Collection<V> collection = getOrCreateCollection(key);
/*  235 */     Collection<V> oldValues = createCollection();
/*  236 */     oldValues.addAll(collection);
/*      */     
/*  238 */     this.totalSize -= collection.size();
/*  239 */     collection.clear();
/*      */     
/*  241 */     while (iterator.hasNext()) {
/*  242 */       if (collection.add(iterator.next())) {
/*  243 */         this.totalSize++;
/*      */       }
/*      */     } 
/*      */     
/*  247 */     return unmodifiableCollectionSubclass(oldValues);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> removeAll(@Nullable Object key) {
/*  257 */     Collection<V> collection = (Collection)this.map.remove(key);
/*      */     
/*  259 */     if (collection == null) {
/*  260 */       return createUnmodifiableEmptyCollection();
/*      */     }
/*      */     
/*  263 */     Collection<V> output = createCollection();
/*  264 */     output.addAll(collection);
/*  265 */     this.totalSize -= collection.size();
/*  266 */     collection.clear();
/*      */     
/*  268 */     return unmodifiableCollectionSubclass(output);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
/*  274 */     if (collection instanceof SortedSet)
/*  275 */       return Collections.unmodifiableSortedSet((SortedSet)collection); 
/*  276 */     if (collection instanceof Set)
/*  277 */       return Collections.unmodifiableSet((Set)collection); 
/*  278 */     if (collection instanceof List) {
/*  279 */       return Collections.unmodifiableList((List)collection);
/*      */     }
/*  281 */     return Collections.unmodifiableCollection(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  288 */     for (Collection<V> collection : this.map.values()) {
/*  289 */       collection.clear();
/*      */     }
/*  291 */     this.map.clear();
/*  292 */     this.totalSize = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> get(@Nullable K key) {
/*  304 */     Collection<V> collection = (Collection)this.map.get(key);
/*  305 */     if (collection == null) {
/*  306 */       collection = createCollection(key);
/*      */     }
/*  308 */     return wrapCollection(key, collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
/*  319 */     if (collection instanceof SortedSet)
/*  320 */       return new WrappedSortedSet(key, (SortedSet)collection, null); 
/*  321 */     if (collection instanceof Set)
/*  322 */       return new WrappedSet(key, (Set)collection); 
/*  323 */     if (collection instanceof List) {
/*  324 */       return wrapList(key, (List)collection, null);
/*      */     }
/*  326 */     return new WrappedCollection(key, collection, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  332 */   private List<V> wrapList(@Nullable K key, List<V> list, @Nullable WrappedCollection ancestor) { return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedCollection
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Collection<V> delegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final WrappedCollection ancestor;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Collection<V> ancestorDelegate;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedCollection(K key, @Nullable Collection<V> delegate, WrappedCollection ancestor) {
/*  362 */       this.key = key;
/*  363 */       this.delegate = delegate;
/*  364 */       this.ancestor = ancestor;
/*  365 */       this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void refreshIfEmpty() {
/*  377 */       if (this.ancestor != null) {
/*  378 */         this.ancestor.refreshIfEmpty();
/*  379 */         if (this.ancestor.getDelegate() != this.ancestorDelegate) {
/*  380 */           throw new ConcurrentModificationException();
/*      */         }
/*  382 */       } else if (this.delegate.isEmpty()) {
/*  383 */         Collection<V> newDelegate = (Collection)AbstractMapBasedMultimap.this.map.get(this.key);
/*  384 */         if (newDelegate != null) {
/*  385 */           this.delegate = newDelegate;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void removeIfEmpty() {
/*  395 */       if (this.ancestor != null) {
/*  396 */         this.ancestor.removeIfEmpty();
/*  397 */       } else if (this.delegate.isEmpty()) {
/*  398 */         AbstractMapBasedMultimap.this.map.remove(this.key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  403 */     K getKey() { return (K)this.key; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addToMap() {
/*  414 */       if (this.ancestor != null) {
/*  415 */         this.ancestor.addToMap();
/*      */       } else {
/*  417 */         AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  422 */       refreshIfEmpty();
/*  423 */       return this.delegate.size();
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  427 */       if (object == this) {
/*  428 */         return true;
/*      */       }
/*  430 */       refreshIfEmpty();
/*  431 */       return this.delegate.equals(object);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  435 */       refreshIfEmpty();
/*  436 */       return this.delegate.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*  440 */       refreshIfEmpty();
/*  441 */       return this.delegate.toString();
/*      */     }
/*      */ 
/*      */     
/*  445 */     Collection<V> getDelegate() { return this.delegate; }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/*  449 */       refreshIfEmpty();
/*  450 */       return new WrappedIterator();
/*      */     }
/*      */     
/*      */     class WrappedIterator extends Object implements Iterator<V> { final Iterator<V> delegateIterator;
/*      */       
/*      */       WrappedIterator() {
/*  456 */         super.originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*      */ 
/*      */         
/*  459 */         super.delegateIterator = AbstractMapBasedMultimap.WrappedCollection.this.this$0.iteratorOrListIterator(AbstractMapBasedMultimap.WrappedCollection.this.delegate);
/*      */       } final Collection<V> originalDelegate;
/*      */       WrappedIterator(Iterator<V> delegateIterator) {
/*      */         super.originalDelegate = AbstractMapBasedMultimap.WrappedCollection.this.delegate;
/*  463 */         super.delegateIterator = delegateIterator;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       void validateIterator() {
/*  471 */         super.this$1.refreshIfEmpty();
/*  472 */         if (AbstractMapBasedMultimap.WrappedCollection.this.delegate != super.originalDelegate) {
/*  473 */           throw new ConcurrentModificationException();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/*  479 */         super.validateIterator();
/*  480 */         return super.delegateIterator.hasNext();
/*      */       }
/*      */ 
/*      */       
/*      */       public V next() {
/*  485 */         super.validateIterator();
/*  486 */         return (V)super.delegateIterator.next();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  491 */         super.delegateIterator.remove();
/*  492 */         AbstractMapBasedMultimap.WrappedCollection.this.this$0.totalSize--;
/*  493 */         super.this$1.removeIfEmpty();
/*      */       }
/*      */       
/*      */       Iterator<V> getDelegateIterator() {
/*  497 */         super.validateIterator();
/*  498 */         return super.delegateIterator;
/*      */       } }
/*      */ 
/*      */     
/*      */     public boolean add(V value) {
/*  503 */       refreshIfEmpty();
/*  504 */       boolean wasEmpty = this.delegate.isEmpty();
/*  505 */       boolean changed = this.delegate.add(value);
/*  506 */       if (changed) {
/*  507 */         AbstractMapBasedMultimap.this.totalSize++;
/*  508 */         if (wasEmpty) {
/*  509 */           addToMap();
/*      */         }
/*      */       } 
/*  512 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*  516 */     WrappedCollection getAncestor() { return this.ancestor; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(Collection<? extends V> collection) {
/*  522 */       if (collection.isEmpty()) {
/*  523 */         return false;
/*      */       }
/*  525 */       int oldSize = size();
/*  526 */       boolean changed = this.delegate.addAll(collection);
/*  527 */       if (changed) {
/*  528 */         int newSize = this.delegate.size();
/*  529 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  530 */         if (oldSize == 0) {
/*  531 */           addToMap();
/*      */         }
/*      */       } 
/*  534 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean contains(Object o) {
/*  538 */       refreshIfEmpty();
/*  539 */       return this.delegate.contains(o);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  543 */       refreshIfEmpty();
/*  544 */       return this.delegate.containsAll(c);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  548 */       int oldSize = size();
/*  549 */       if (oldSize == 0) {
/*      */         return;
/*      */       }
/*  552 */       this.delegate.clear();
/*  553 */       AbstractMapBasedMultimap.this.totalSize -= oldSize;
/*  554 */       removeIfEmpty();
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  558 */       refreshIfEmpty();
/*  559 */       boolean changed = this.delegate.remove(o);
/*  560 */       if (changed) {
/*  561 */         AbstractMapBasedMultimap.this.totalSize--;
/*  562 */         removeIfEmpty();
/*      */       } 
/*  564 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  568 */       if (c.isEmpty()) {
/*  569 */         return false;
/*      */       }
/*  571 */       int oldSize = size();
/*  572 */       boolean changed = this.delegate.removeAll(c);
/*  573 */       if (changed) {
/*  574 */         int newSize = this.delegate.size();
/*  575 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  576 */         removeIfEmpty();
/*      */       } 
/*  578 */       return changed;
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  582 */       Preconditions.checkNotNull(c);
/*  583 */       int oldSize = size();
/*  584 */       boolean changed = this.delegate.retainAll(c);
/*  585 */       if (changed) {
/*  586 */         int newSize = this.delegate.size();
/*  587 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  588 */         removeIfEmpty();
/*      */       } 
/*  590 */       return changed;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  595 */   private Iterator<V> iteratorOrListIterator(Collection<V> collection) { return (collection instanceof List) ? ((List)collection).listIterator() : collection.iterator(); }
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedSet
/*      */     extends WrappedCollection
/*      */     implements Set<V>
/*      */   {
/*  603 */     WrappedSet(K key, Set<V> delegate) { super(AbstractMapBasedMultimap.this, key, delegate, null); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  608 */       if (c.isEmpty()) {
/*  609 */         return false;
/*      */       }
/*  611 */       int oldSize = size();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  616 */       boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
/*  617 */       if (changed) {
/*  618 */         int newSize = this.delegate.size();
/*  619 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  620 */         removeIfEmpty();
/*      */       } 
/*  622 */       return changed;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class WrappedSortedSet
/*      */     extends WrappedCollection
/*      */     implements SortedSet<V>
/*      */   {
/*  633 */     WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) { super(AbstractMapBasedMultimap.this, key, delegate, ancestor); }
/*      */ 
/*      */ 
/*      */     
/*  637 */     SortedSet<V> getSortedSetDelegate() { return (SortedSet)getDelegate(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  642 */     public Comparator<? super V> comparator() { return getSortedSetDelegate().comparator(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public V first() {
/*  647 */       refreshIfEmpty();
/*  648 */       return (V)getSortedSetDelegate().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public V last() {
/*  653 */       refreshIfEmpty();
/*  654 */       return (V)getSortedSetDelegate().last();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<V> headSet(V toElement) {
/*  659 */       refreshIfEmpty();
/*  660 */       return new WrappedSortedSet(AbstractMapBasedMultimap.this, getKey(), getSortedSetDelegate().headSet(toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> subSet(V fromElement, V toElement) {
/*  667 */       refreshIfEmpty();
/*  668 */       return new WrappedSortedSet(AbstractMapBasedMultimap.this, getKey(), getSortedSetDelegate().subSet(fromElement, toElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<V> tailSet(V fromElement) {
/*  675 */       refreshIfEmpty();
/*  676 */       return new WrappedSortedSet(AbstractMapBasedMultimap.this, getKey(), getSortedSetDelegate().tailSet(fromElement), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   class WrappedNavigableSet
/*      */     extends WrappedSortedSet
/*      */     implements NavigableSet<V>
/*      */   {
/*  686 */     WrappedNavigableSet(K key, @Nullable NavigableSet<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) { super(AbstractMapBasedMultimap.this, key, delegate, ancestor); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  691 */     NavigableSet<V> getSortedSetDelegate() { return (NavigableSet)super.getSortedSetDelegate(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  696 */     public V lower(V v) { return (V)getSortedSetDelegate().lower(v); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  701 */     public V floor(V v) { return (V)getSortedSetDelegate().floor(v); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  706 */     public V ceiling(V v) { return (V)getSortedSetDelegate().ceiling(v); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  711 */     public V higher(V v) { return (V)getSortedSetDelegate().higher(v); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  716 */     public V pollFirst() { return (V)Iterators.pollNext(iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  721 */     public V pollLast() { return (V)Iterators.pollNext(descendingIterator()); }
/*      */ 
/*      */ 
/*      */     
/*  725 */     private NavigableSet<V> wrap(NavigableSet<V> wrapped) { return new WrappedNavigableSet(AbstractMapBasedMultimap.this, this.key, wrapped, (getAncestor() == null) ? this : getAncestor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  731 */     public NavigableSet<V> descendingSet() { return wrap(getSortedSetDelegate().descendingSet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  736 */     public Iterator<V> descendingIterator() { return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this, getSortedSetDelegate().descendingIterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  742 */     public NavigableSet<V> subSet(V fromElement, boolean fromInclusive, V toElement, boolean toInclusive) { return wrap(getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  748 */     public NavigableSet<V> headSet(V toElement, boolean inclusive) { return wrap(getSortedSetDelegate().headSet(toElement, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  753 */     public NavigableSet<V> tailSet(V fromElement, boolean inclusive) { return wrap(getSortedSetDelegate().tailSet(fromElement, inclusive)); }
/*      */   }
/*      */ 
/*      */   
/*      */   private class WrappedList
/*      */     extends WrappedCollection
/*      */     implements List<V>
/*      */   {
/*  761 */     WrappedList(K key, @Nullable List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) { super(AbstractMapBasedMultimap.this, key, delegate, ancestor); }
/*      */ 
/*      */ 
/*      */     
/*  765 */     List<V> getListDelegate() { return (List)getDelegate(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends V> c) {
/*  770 */       if (c.isEmpty()) {
/*  771 */         return false;
/*      */       }
/*  773 */       int oldSize = size();
/*  774 */       boolean changed = getListDelegate().addAll(index, c);
/*  775 */       if (changed) {
/*  776 */         int newSize = getDelegate().size();
/*  777 */         AbstractMapBasedMultimap.this.totalSize += newSize - oldSize;
/*  778 */         if (oldSize == 0) {
/*  779 */           addToMap();
/*      */         }
/*      */       } 
/*  782 */       return changed;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(int index) {
/*  787 */       refreshIfEmpty();
/*  788 */       return (V)getListDelegate().get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public V set(int index, V element) {
/*  793 */       refreshIfEmpty();
/*  794 */       return (V)getListDelegate().set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, V element) {
/*  799 */       refreshIfEmpty();
/*  800 */       boolean wasEmpty = getDelegate().isEmpty();
/*  801 */       getListDelegate().add(index, element);
/*  802 */       AbstractMapBasedMultimap.this.totalSize++;
/*  803 */       if (wasEmpty) {
/*  804 */         addToMap();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(int index) {
/*  810 */       refreshIfEmpty();
/*  811 */       V value = (V)getListDelegate().remove(index);
/*  812 */       AbstractMapBasedMultimap.this.totalSize--;
/*  813 */       removeIfEmpty();
/*  814 */       return value;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(Object o) {
/*  819 */       refreshIfEmpty();
/*  820 */       return getListDelegate().indexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(Object o) {
/*  825 */       refreshIfEmpty();
/*  826 */       return getListDelegate().lastIndexOf(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator() {
/*  831 */       refreshIfEmpty();
/*  832 */       return new WrappedListIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<V> listIterator(int index) {
/*  837 */       refreshIfEmpty();
/*  838 */       return new WrappedListIterator(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public List<V> subList(int fromIndex, int toIndex) {
/*  843 */       refreshIfEmpty();
/*  844 */       return AbstractMapBasedMultimap.this.wrapList(getKey(), getListDelegate().subList(fromIndex, toIndex), (getAncestor() == null) ? this : getAncestor());
/*      */     }
/*      */     
/*      */     private class WrappedListIterator
/*      */       extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator
/*      */       implements ListIterator<V>
/*      */     {
/*      */       WrappedListIterator() {
/*  852 */         super(AbstractMapBasedMultimap.WrappedList.this);
/*      */       }
/*      */       
/*  855 */       public WrappedListIterator(int index) { super(AbstractMapBasedMultimap.WrappedList.this, this$0.getListDelegate().listIterator(index)); }
/*      */ 
/*      */ 
/*      */       
/*  859 */       private ListIterator<V> getDelegateListIterator() { return (ListIterator)getDelegateIterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  864 */       public boolean hasPrevious() { return super.getDelegateListIterator().hasPrevious(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  869 */       public V previous() { return (V)super.getDelegateListIterator().previous(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  874 */       public int nextIndex() { return super.getDelegateListIterator().nextIndex(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  879 */       public int previousIndex() { return super.getDelegateListIterator().previousIndex(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  884 */       public void set(V value) { super.getDelegateListIterator().set(value); }
/*      */ 
/*      */ 
/*      */       
/*      */       public void add(V value) {
/*  889 */         boolean wasEmpty = super.this$1.isEmpty();
/*  890 */         super.getDelegateListIterator().add(value);
/*  891 */         AbstractMapBasedMultimap.WrappedList.this.this$0.totalSize++;
/*  892 */         if (wasEmpty) {
/*  893 */           super.this$1.addToMap();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class RandomAccessWrappedList
/*      */     extends WrappedList
/*      */     implements RandomAccess
/*      */   {
/*  907 */     RandomAccessWrappedList(K key, @Nullable List<V> delegate, AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) { super(AbstractMapBasedMultimap.this, key, delegate, ancestor); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  915 */   Set<K> createKeySet() { return (this.map instanceof SortedMap) ? new SortedKeySet((SortedMap)this.map) : new KeySet(this.map); }
/*      */ 
/*      */   
/*      */   private class KeySet
/*      */     extends Maps.KeySet<K, Collection<V>>
/*      */   {
/*  921 */     KeySet(Map<K, Collection<V>> subMap) { super(subMap); }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/*  925 */       final Iterator<Map.Entry<K, Collection<V>>> entryIterator = map().entrySet().iterator();
/*      */       
/*  927 */       return new Iterator<K>()
/*      */         {
/*      */           Map.Entry<K, Collection<V>> entry;
/*      */ 
/*      */           
/*  932 */           public boolean hasNext() { return entryIterator.hasNext(); }
/*      */ 
/*      */           
/*      */           public K next() {
/*  936 */             super.entry = (Map.Entry)entryIterator.next();
/*  937 */             return (K)super.entry.getKey();
/*      */           }
/*      */           
/*      */           public void remove() {
/*  941 */             CollectPreconditions.checkRemove((super.entry != null));
/*  942 */             Collection<V> collection = (Collection)super.entry.getValue();
/*  943 */             entryIterator.remove();
/*  944 */             AbstractMapBasedMultimap.KeySet.this.this$0.totalSize -= collection.size();
/*  945 */             collection.clear();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object key) {
/*  953 */       int count = 0;
/*  954 */       Collection<V> collection = (Collection)map().remove(key);
/*  955 */       if (collection != null) {
/*  956 */         count = collection.size();
/*  957 */         collection.clear();
/*  958 */         AbstractMapBasedMultimap.this.totalSize -= count;
/*      */       } 
/*  960 */       return (count > 0);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  965 */     public void clear() { Iterators.clear(iterator()); }
/*      */ 
/*      */ 
/*      */     
/*  969 */     public boolean containsAll(Collection<?> c) { return map().keySet().containsAll(c); }
/*      */ 
/*      */ 
/*      */     
/*  973 */     public boolean equals(@Nullable Object object) { return (this == object || map().keySet().equals(object)); }
/*      */ 
/*      */ 
/*      */     
/*  977 */     public int hashCode() { return map().keySet().hashCode(); }
/*      */   }
/*      */   
/*      */   private class SortedKeySet
/*      */     extends KeySet
/*      */     implements SortedSet<K>
/*      */   {
/*  984 */     SortedKeySet(SortedMap<K, Collection<V>> subMap) { super(AbstractMapBasedMultimap.this, subMap); }
/*      */ 
/*      */ 
/*      */     
/*  988 */     SortedMap<K, Collection<V>> sortedMap() { return (SortedMap)map(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  993 */     public Comparator<? super K> comparator() { return sortedMap().comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  998 */     public K first() { return (K)sortedMap().firstKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1003 */     public SortedSet<K> headSet(K toElement) { return new SortedKeySet(AbstractMapBasedMultimap.this, sortedMap().headMap(toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1008 */     public K last() { return (K)sortedMap().lastKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1013 */     public SortedSet<K> subSet(K fromElement, K toElement) { return new SortedKeySet(AbstractMapBasedMultimap.this, sortedMap().subMap(fromElement, toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1018 */     public SortedSet<K> tailSet(K fromElement) { return new SortedKeySet(AbstractMapBasedMultimap.this, sortedMap().tailMap(fromElement)); }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   class NavigableKeySet
/*      */     extends SortedKeySet
/*      */     implements NavigableSet<K> {
/* 1025 */     NavigableKeySet(NavigableMap<K, Collection<V>> subMap) { super(AbstractMapBasedMultimap.this, subMap); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1030 */     NavigableMap<K, Collection<V>> sortedMap() { return (NavigableMap)super.sortedMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1035 */     public K lower(K k) { return (K)sortedMap().lowerKey(k); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1040 */     public K floor(K k) { return (K)sortedMap().floorKey(k); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1045 */     public K ceiling(K k) { return (K)sortedMap().ceilingKey(k); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1050 */     public K higher(K k) { return (K)sortedMap().higherKey(k); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1055 */     public K pollFirst() { return (K)Iterators.pollNext(iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1060 */     public K pollLast() { return (K)Iterators.pollNext(descendingIterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1065 */     public NavigableSet<K> descendingSet() { return new NavigableKeySet(AbstractMapBasedMultimap.this, sortedMap().descendingMap()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1070 */     public Iterator<K> descendingIterator() { return descendingSet().iterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1075 */     public NavigableSet<K> headSet(K toElement) { return headSet(toElement, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1080 */     public NavigableSet<K> headSet(K toElement, boolean inclusive) { return new NavigableKeySet(AbstractMapBasedMultimap.this, sortedMap().headMap(toElement, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1085 */     public NavigableSet<K> subSet(K fromElement, K toElement) { return subSet(fromElement, true, toElement, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1091 */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) { return new NavigableKeySet(AbstractMapBasedMultimap.this, sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1097 */     public NavigableSet<K> tailSet(K fromElement) { return tailSet(fromElement, true); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1102 */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) { return new NavigableKeySet(AbstractMapBasedMultimap.this, sortedMap().tailMap(fromElement, inclusive)); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int removeValuesForKey(Object key) {
/* 1111 */     Collection<V> collection = (Collection)Maps.safeRemove(this.map, key);
/*      */     
/* 1113 */     int count = 0;
/* 1114 */     if (collection != null) {
/* 1115 */       count = collection.size();
/* 1116 */       collection.clear();
/* 1117 */       this.totalSize -= count;
/*      */     } 
/* 1119 */     return count;
/*      */   }
/*      */   
/*      */   private abstract class Itr<T> extends Object implements Iterator<T> {
/*      */     final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
/*      */     K key;
/*      */     Collection<V> collection;
/*      */     Iterator<V> valueIterator;
/*      */     
/*      */     Itr() {
/* 1129 */       this.keyIterator = this$0.map.entrySet().iterator();
/* 1130 */       this.key = null;
/* 1131 */       this.collection = null;
/* 1132 */       this.valueIterator = Iterators.emptyModifiableIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     abstract T output(K param1K, V param1V);
/*      */ 
/*      */     
/* 1139 */     public boolean hasNext() { return (this.keyIterator.hasNext() || this.valueIterator.hasNext()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public T next() {
/* 1144 */       if (!this.valueIterator.hasNext()) {
/* 1145 */         Map.Entry<K, Collection<V>> mapEntry = (Map.Entry)this.keyIterator.next();
/* 1146 */         this.key = mapEntry.getKey();
/* 1147 */         this.collection = (Collection)mapEntry.getValue();
/* 1148 */         this.valueIterator = this.collection.iterator();
/*      */       } 
/* 1150 */       return (T)output(this.key, this.valueIterator.next());
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1155 */       this.valueIterator.remove();
/* 1156 */       if (this.collection.isEmpty()) {
/* 1157 */         this.keyIterator.remove();
/*      */       }
/* 1159 */       AbstractMapBasedMultimap.this.totalSize--;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1170 */   public Collection<V> values() { return super.values(); }
/*      */ 
/*      */ 
/*      */   
/*      */   Iterator<V> valueIterator() {
/* 1175 */     return new Itr<V>()
/*      */       {
/*      */         V output(K key, V value) {
/* 1178 */           return value;
/*      */         }
/*      */       };
/*      */   }
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
/* 1201 */   public Collection<Map.Entry<K, V>> entries() { return super.entries(); }
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
/*      */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 1214 */     return new Itr<Map.Entry<K, V>>()
/*      */       {
/*      */         Map.Entry<K, V> output(K key, V value) {
/* 1217 */           return Maps.immutableEntry(key, value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1226 */   Map<K, Collection<V>> createAsMap() { return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap)this.map) : new AsMap(this.map); }
/*      */ 
/*      */   
/*      */   abstract Collection<V> createCollection();
/*      */ 
/*      */   
/*      */   private class AsMap
/*      */     extends Maps.ImprovedAbstractMap<K, Collection<V>>
/*      */   {
/*      */     final Map<K, Collection<V>> submap;
/*      */ 
/*      */     
/* 1238 */     AsMap(Map<K, Collection<V>> submap) { this.submap = submap; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1243 */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() { return new AsMapEntries(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1249 */     public boolean containsKey(Object key) { return Maps.safeContainsKey(this.submap, key); }
/*      */ 
/*      */     
/*      */     public Collection<V> get(Object key) {
/* 1253 */       Collection<V> collection = (Collection)Maps.safeGet(this.submap, key);
/* 1254 */       if (collection == null) {
/* 1255 */         return null;
/*      */       }
/*      */       
/* 1258 */       K k = (K)key;
/* 1259 */       return AbstractMapBasedMultimap.this.wrapCollection(k, collection);
/*      */     }
/*      */ 
/*      */     
/* 1263 */     public Set<K> keySet() { return AbstractMapBasedMultimap.this.keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1268 */     public int size() { return this.submap.size(); }
/*      */ 
/*      */     
/*      */     public Collection<V> remove(Object key) {
/* 1272 */       Collection<V> collection = (Collection)this.submap.remove(key);
/* 1273 */       if (collection == null) {
/* 1274 */         return null;
/*      */       }
/*      */       
/* 1277 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1278 */       output.addAll(collection);
/* 1279 */       AbstractMapBasedMultimap.this.totalSize -= collection.size();
/* 1280 */       collection.clear();
/* 1281 */       return output;
/*      */     }
/*      */ 
/*      */     
/* 1285 */     public boolean equals(@Nullable Object object) { return (this == object || this.submap.equals(object)); }
/*      */ 
/*      */ 
/*      */     
/* 1289 */     public int hashCode() { return this.submap.hashCode(); }
/*      */ 
/*      */ 
/*      */     
/* 1293 */     public String toString() { return this.submap.toString(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1298 */       if (this.submap == AbstractMapBasedMultimap.this.map) {
/* 1299 */         AbstractMapBasedMultimap.this.clear();
/*      */       } else {
/* 1301 */         Iterators.clear(new AsMapIterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     Map.Entry<K, Collection<V>> wrapEntry(Map.Entry<K, Collection<V>> entry) {
/* 1306 */       K key = (K)entry.getKey();
/* 1307 */       return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, (Collection)entry.getValue()));
/*      */     }
/*      */     
/*      */     class AsMapEntries
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/* 1313 */       Map<K, Collection<V>> map() { return super.this$1; }
/*      */ 
/*      */ 
/*      */       
/* 1317 */       public Iterator<Map.Entry<K, Collection<V>>> iterator() { return new AbstractMapBasedMultimap.AsMap.AsMapIterator(super.this$1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1323 */       public boolean contains(Object o) { return Collections2.safeContains(AbstractMapBasedMultimap.AsMap.this.submap.entrySet(), o); }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 1327 */         if (!super.contains(o)) {
/* 1328 */           return false;
/*      */         }
/* 1330 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 1331 */         AbstractMapBasedMultimap.AsMap.this.this$0.removeValuesForKey(entry.getKey());
/* 1332 */         return true;
/*      */       } }
/*      */     
/*      */     class AsMapIterator extends Object implements Iterator<Map.Entry<K, Collection<V>>> { final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;
/*      */       Collection<V> collection;
/*      */       
/* 1338 */       AsMapIterator() { super.delegateIterator = AbstractMapBasedMultimap.AsMap.this.submap.entrySet().iterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1344 */       public boolean hasNext() { return super.delegateIterator.hasNext(); }
/*      */ 
/*      */ 
/*      */       
/*      */       public Map.Entry<K, Collection<V>> next() {
/* 1349 */         Map.Entry<K, Collection<V>> entry = (Map.Entry)super.delegateIterator.next();
/* 1350 */         super.collection = (Collection)entry.getValue();
/* 1351 */         return super.this$1.wrapEntry(entry);
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/* 1356 */         super.delegateIterator.remove();
/* 1357 */         AbstractMapBasedMultimap.AsMap.this.this$0.totalSize -= super.collection.size();
/* 1358 */         super.collection.clear();
/*      */       } }
/*      */   
/*      */   }
/*      */   
/*      */   private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
/*      */     SortedSet<K> sortedKeySet;
/*      */     
/* 1366 */     SortedAsMap(SortedMap<K, Collection<V>> submap) { super(AbstractMapBasedMultimap.this, submap); }
/*      */ 
/*      */ 
/*      */     
/* 1370 */     SortedMap<K, Collection<V>> sortedMap() { return (SortedMap)this.submap; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1375 */     public Comparator<? super K> comparator() { return sortedMap().comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1380 */     public K firstKey() { return (K)sortedMap().firstKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1385 */     public K lastKey() { return (K)sortedMap().lastKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1390 */     public SortedMap<K, Collection<V>> headMap(K toKey) { return new SortedAsMap(AbstractMapBasedMultimap.this, sortedMap().headMap(toKey)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1395 */     public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) { return new SortedAsMap(AbstractMapBasedMultimap.this, sortedMap().subMap(fromKey, toKey)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1400 */     public SortedMap<K, Collection<V>> tailMap(K fromKey) { return new SortedAsMap(AbstractMapBasedMultimap.this, sortedMap().tailMap(fromKey)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 1408 */       SortedSet<K> result = this.sortedKeySet;
/* 1409 */       return (result == null) ? (this.sortedKeySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1414 */     SortedSet<K> createKeySet() { return new AbstractMapBasedMultimap.SortedKeySet(AbstractMapBasedMultimap.this, sortedMap()); }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableAsMap")
/*      */   class NavigableAsMap
/*      */     extends SortedAsMap
/*      */     implements NavigableMap<K, Collection<V>>
/*      */   {
/* 1422 */     NavigableAsMap(NavigableMap<K, Collection<V>> submap) { super(AbstractMapBasedMultimap.this, submap); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1427 */     NavigableMap<K, Collection<V>> sortedMap() { return (NavigableMap)super.sortedMap(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lowerEntry(K key) {
/* 1432 */       Map.Entry<K, Collection<V>> entry = sortedMap().lowerEntry(key);
/* 1433 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1438 */     public K lowerKey(K key) { return (K)sortedMap().lowerKey(key); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> floorEntry(K key) {
/* 1443 */       Map.Entry<K, Collection<V>> entry = sortedMap().floorEntry(key);
/* 1444 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1449 */     public K floorKey(K key) { return (K)sortedMap().floorKey(key); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> ceilingEntry(K key) {
/* 1454 */       Map.Entry<K, Collection<V>> entry = sortedMap().ceilingEntry(key);
/* 1455 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1460 */     public K ceilingKey(K key) { return (K)sortedMap().ceilingKey(key); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> higherEntry(K key) {
/* 1465 */       Map.Entry<K, Collection<V>> entry = sortedMap().higherEntry(key);
/* 1466 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1471 */     public K higherKey(K key) { return (K)sortedMap().higherKey(key); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> firstEntry() {
/* 1476 */       Map.Entry<K, Collection<V>> entry = sortedMap().firstEntry();
/* 1477 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, Collection<V>> lastEntry() {
/* 1482 */       Map.Entry<K, Collection<V>> entry = sortedMap().lastEntry();
/* 1483 */       return (entry == null) ? null : wrapEntry(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1488 */     public Map.Entry<K, Collection<V>> pollFirstEntry() { return pollAsMapEntry(entrySet().iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1493 */     public Map.Entry<K, Collection<V>> pollLastEntry() { return pollAsMapEntry(descendingMap().entrySet().iterator()); }
/*      */ 
/*      */     
/*      */     Map.Entry<K, Collection<V>> pollAsMapEntry(Iterator<Map.Entry<K, Collection<V>>> entryIterator) {
/* 1497 */       if (!entryIterator.hasNext()) {
/* 1498 */         return null;
/*      */       }
/* 1500 */       Map.Entry<K, Collection<V>> entry = (Map.Entry)entryIterator.next();
/* 1501 */       Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
/* 1502 */       output.addAll((Collection)entry.getValue());
/* 1503 */       entryIterator.remove();
/* 1504 */       return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1509 */     public NavigableMap<K, Collection<V>> descendingMap() { return new NavigableAsMap(AbstractMapBasedMultimap.this, sortedMap().descendingMap()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1514 */     public NavigableSet<K> keySet() { return (NavigableSet)super.keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1519 */     NavigableSet<K> createKeySet() { return new AbstractMapBasedMultimap.NavigableKeySet(AbstractMapBasedMultimap.this, sortedMap()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1524 */     public NavigableSet<K> navigableKeySet() { return keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1529 */     public NavigableSet<K> descendingKeySet() { return descendingMap().navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1534 */     public NavigableMap<K, Collection<V>> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1540 */     public NavigableMap<K, Collection<V>> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return new NavigableAsMap(AbstractMapBasedMultimap.this, sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1545 */     public NavigableMap<K, Collection<V>> headMap(K toKey) { return headMap(toKey, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1550 */     public NavigableMap<K, Collection<V>> headMap(K toKey, boolean inclusive) { return new NavigableAsMap(AbstractMapBasedMultimap.this, sortedMap().headMap(toKey, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1555 */     public NavigableMap<K, Collection<V>> tailMap(K fromKey) { return tailMap(fromKey, true); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1560 */     public NavigableMap<K, Collection<V>> tailMap(K fromKey, boolean inclusive) { return new NavigableAsMap(AbstractMapBasedMultimap.this, sortedMap().tailMap(fromKey, inclusive)); }
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractMapBasedMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */