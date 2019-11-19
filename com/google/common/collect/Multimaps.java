/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.base.Supplier;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Multimaps
/*      */ {
/*  113 */   public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) { return new CustomMultimap(map, factory); }
/*      */   
/*      */   private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
/*      */     Supplier<? extends Collection<V>> factory;
/*      */     @GwtIncompatible("java serialization not supported")
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
/*  121 */       super(map);
/*  122 */       this.factory = (Supplier)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*  126 */     protected Collection<V> createCollection() { return (Collection)this.factory.get(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectOutputStream")
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  135 */       stream.defaultWriteObject();
/*  136 */       stream.writeObject(this.factory);
/*  137 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectInputStream")
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  144 */       stream.defaultReadObject();
/*  145 */       this.factory = (Supplier)stream.readObject();
/*  146 */       Map<K, Collection<V>> map = (Map)stream.readObject();
/*  147 */       setMap(map);
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
/*  194 */   public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) { return new CustomListMultimap(map, factory); }
/*      */   
/*      */   private static class CustomListMultimap<K, V>
/*      */     extends AbstractListMultimap<K, V> {
/*      */     Supplier<? extends List<V>> factory;
/*      */     @GwtIncompatible("java serialization not supported")
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
/*  203 */       super(map);
/*  204 */       this.factory = (Supplier)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*  208 */     protected List<V> createCollection() { return (List)this.factory.get(); }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectOutputStream")
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  214 */       stream.defaultWriteObject();
/*  215 */       stream.writeObject(this.factory);
/*  216 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectInputStream")
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  223 */       stream.defaultReadObject();
/*  224 */       this.factory = (Supplier)stream.readObject();
/*  225 */       Map<K, Collection<V>> map = (Map)stream.readObject();
/*  226 */       setMap(map);
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
/*  272 */   public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) { return new CustomSetMultimap(map, factory); }
/*      */   
/*      */   private static class CustomSetMultimap<K, V>
/*      */     extends AbstractSetMultimap<K, V> {
/*      */     Supplier<? extends Set<V>> factory;
/*      */     @GwtIncompatible("not needed in emulated source")
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
/*  281 */       super(map);
/*  282 */       this.factory = (Supplier)Preconditions.checkNotNull(factory);
/*      */     }
/*      */ 
/*      */     
/*  286 */     protected Set<V> createCollection() { return (Set)this.factory.get(); }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectOutputStream")
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  292 */       stream.defaultWriteObject();
/*  293 */       stream.writeObject(this.factory);
/*  294 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectInputStream")
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  301 */       stream.defaultReadObject();
/*  302 */       this.factory = (Supplier)stream.readObject();
/*  303 */       Map<K, Collection<V>> map = (Map)stream.readObject();
/*  304 */       setMap(map);
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
/*  350 */   public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) { return new CustomSortedSetMultimap(map, factory); }
/*      */   
/*      */   private static class CustomSortedSetMultimap<K, V>
/*      */     extends AbstractSortedSetMultimap<K, V> {
/*      */     Supplier<? extends SortedSet<V>> factory;
/*      */     Comparator<? super V> valueComparator;
/*      */     @GwtIncompatible("not needed in emulated source")
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
/*  360 */       super(map);
/*  361 */       this.factory = (Supplier)Preconditions.checkNotNull(factory);
/*  362 */       this.valueComparator = ((SortedSet)factory.get()).comparator();
/*      */     }
/*      */ 
/*      */     
/*  366 */     protected SortedSet<V> createCollection() { return (SortedSet)this.factory.get(); }
/*      */ 
/*      */ 
/*      */     
/*  370 */     public Comparator<? super V> valueComparator() { return this.valueComparator; }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectOutputStream")
/*      */     private void writeObject(ObjectOutputStream stream) throws IOException {
/*  376 */       stream.defaultWriteObject();
/*  377 */       stream.writeObject(this.factory);
/*  378 */       stream.writeObject(backingMap());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @GwtIncompatible("java.io.ObjectInputStream")
/*      */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  385 */       stream.defaultReadObject();
/*  386 */       this.factory = (Supplier)stream.readObject();
/*  387 */       this.valueComparator = ((SortedSet)this.factory.get()).comparator();
/*  388 */       Map<K, Collection<V>> map = (Map)stream.readObject();
/*  389 */       setMap(map);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
/*  409 */     Preconditions.checkNotNull(dest);
/*  410 */     for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
/*  411 */       dest.put(entry.getValue(), entry.getKey());
/*      */     }
/*  413 */     return dest;
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
/*  451 */   public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) { return Synchronized.multimap(multimap, null); }
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
/*      */   public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
/*  473 */     if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap)
/*      */     {
/*  475 */       return delegate;
/*      */     }
/*  477 */     return new UnmodifiableMultimap(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  488 */   public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) { return (Multimap)Preconditions.checkNotNull(delegate); }
/*      */   
/*      */   private static class UnmodifiableMultimap<K, V>
/*      */     extends ForwardingMultimap<K, V>
/*      */     implements Serializable {
/*      */     final Multimap<K, V> delegate;
/*      */     Collection<Map.Entry<K, V>> entries;
/*      */     Multiset<K> keys;
/*      */     Set<K> keySet;
/*      */     Collection<V> values;
/*      */     Map<K, Collection<V>> map;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  501 */     UnmodifiableMultimap(Multimap<K, V> delegate) { this.delegate = (Multimap)Preconditions.checkNotNull(delegate); }
/*      */ 
/*      */ 
/*      */     
/*  505 */     protected Multimap<K, V> delegate() { return this.delegate; }
/*      */ 
/*      */ 
/*      */     
/*  509 */     public void clear() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */     
/*      */     public Map<K, Collection<V>> asMap() {
/*  513 */       Map<K, Collection<V>> result = this.map;
/*  514 */       if (result == null) {
/*  515 */         result = this.map = Collections.unmodifiableMap(Maps.transformValues(this.delegate.asMap(), new Function<Collection<V>, Collection<V>>()
/*      */               {
/*      */                 public Collection<V> apply(Collection<V> collection)
/*      */                 {
/*  519 */                   return Multimaps.unmodifiableValueCollection(collection);
/*      */                 }
/*      */               }));
/*      */       }
/*  523 */       return result;
/*      */     }
/*      */     
/*      */     public Collection<Map.Entry<K, V>> entries() {
/*  527 */       Collection<Map.Entry<K, V>> result = this.entries;
/*  528 */       if (result == null) {
/*  529 */         this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
/*      */       }
/*  531 */       return result;
/*      */     }
/*      */ 
/*      */     
/*  535 */     public Collection<V> get(K key) { return Multimaps.unmodifiableValueCollection(this.delegate.get(key)); }
/*      */ 
/*      */     
/*      */     public Multiset<K> keys() {
/*  539 */       Multiset<K> result = this.keys;
/*  540 */       if (result == null) {
/*  541 */         this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
/*      */       }
/*  543 */       return result;
/*      */     }
/*      */     
/*      */     public Set<K> keySet() {
/*  547 */       Set<K> result = this.keySet;
/*  548 */       if (result == null) {
/*  549 */         this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
/*      */       }
/*  551 */       return result;
/*      */     }
/*      */ 
/*      */     
/*  555 */     public boolean put(K key, V value) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  559 */     public boolean putAll(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  564 */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  568 */     public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  572 */     public Collection<V> removeAll(Object key) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  577 */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/*  581 */       Collection<V> result = this.values;
/*  582 */       if (result == null) {
/*  583 */         this.values = result = Collections.unmodifiableCollection(this.delegate.values());
/*      */       }
/*  585 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableListMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V>
/*      */     implements ListMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  594 */     UnmodifiableListMultimap(ListMultimap<K, V> delegate) { super(delegate); }
/*      */ 
/*      */     
/*  597 */     public ListMultimap<K, V> delegate() { return (ListMultimap)super.delegate(); }
/*      */ 
/*      */     
/*  600 */     public List<V> get(K key) { return Collections.unmodifiableList(delegate().get(key)); }
/*      */ 
/*      */     
/*  603 */     public List<V> removeAll(Object key) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  607 */     public List<V> replaceValues(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSetMultimap<K, V>
/*      */     extends UnmodifiableMultimap<K, V>
/*      */     implements SetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  615 */     UnmodifiableSetMultimap(SetMultimap<K, V> delegate) { super(delegate); }
/*      */ 
/*      */     
/*  618 */     public SetMultimap<K, V> delegate() { return (SetMultimap)super.delegate(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  625 */     public Set<V> get(K key) { return Collections.unmodifiableSet(delegate().get(key)); }
/*      */ 
/*      */     
/*  628 */     public Set<Map.Entry<K, V>> entries() { return Maps.unmodifiableEntrySet(delegate().entries()); }
/*      */ 
/*      */     
/*  631 */     public Set<V> removeAll(Object key) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  635 */     public Set<V> replaceValues(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */   }
/*      */   
/*      */   private static class UnmodifiableSortedSetMultimap<K, V>
/*      */     extends UnmodifiableSetMultimap<K, V>
/*      */     implements SortedSetMultimap<K, V> {
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  643 */     UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) { super(delegate); }
/*      */ 
/*      */     
/*  646 */     public SortedSetMultimap<K, V> delegate() { return (SortedSetMultimap)super.delegate(); }
/*      */ 
/*      */     
/*  649 */     public SortedSet<V> get(K key) { return Collections.unmodifiableSortedSet(delegate().get(key)); }
/*      */ 
/*      */     
/*  652 */     public SortedSet<V> removeAll(Object key) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  656 */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  660 */     public Comparator<? super V> valueComparator() { return delegate().valueComparator(); }
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
/*  679 */   public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) { return Synchronized.setMultimap(multimap, null); }
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
/*      */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
/*  702 */     if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap)
/*      */     {
/*  704 */       return delegate;
/*      */     }
/*  706 */     return new UnmodifiableSetMultimap(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  717 */   public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) { return (SetMultimap)Preconditions.checkNotNull(delegate); }
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
/*  734 */   public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) { return Synchronized.sortedSetMultimap(multimap, null); }
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
/*      */   public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
/*  757 */     if (delegate instanceof UnmodifiableSortedSetMultimap) {
/*  758 */       return delegate;
/*      */     }
/*  760 */     return new UnmodifiableSortedSetMultimap(delegate);
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
/*  774 */   public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) { return Synchronized.listMultimap(multimap, null); }
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
/*      */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
/*  797 */     if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap)
/*      */     {
/*  799 */       return delegate;
/*      */     }
/*  801 */     return new UnmodifiableListMultimap(delegate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  812 */   public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) { return (ListMultimap)Preconditions.checkNotNull(delegate); }
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
/*      */   private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
/*  825 */     if (collection instanceof SortedSet)
/*  826 */       return Collections.unmodifiableSortedSet((SortedSet)collection); 
/*  827 */     if (collection instanceof Set)
/*  828 */       return Collections.unmodifiableSet((Set)collection); 
/*  829 */     if (collection instanceof List) {
/*  830 */       return Collections.unmodifiableList((List)collection);
/*      */     }
/*  832 */     return Collections.unmodifiableCollection(collection);
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
/*      */   private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/*  846 */     if (entries instanceof Set) {
/*  847 */       return Maps.unmodifiableEntrySet((Set)entries);
/*      */     }
/*  849 */     return new Maps.UnmodifiableEntries(Collections.unmodifiableCollection(entries));
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
/*      */   @Beta
/*  863 */   public static <K, V> Map<K, List<V>> asMap(ListMultimap<K, V> multimap) { return multimap.asMap(); }
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
/*      */   @Beta
/*  876 */   public static <K, V> Map<K, Set<V>> asMap(SetMultimap<K, V> multimap) { return multimap.asMap(); }
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
/*      */   @Beta
/*  891 */   public static <K, V> Map<K, SortedSet<V>> asMap(SortedSetMultimap<K, V> multimap) { return multimap.asMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*  902 */   public static <K, V> Map<K, Collection<V>> asMap(Multimap<K, V> multimap) { return multimap.asMap(); }
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
/*  923 */   public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) { return new MapMultimap(map); }
/*      */   
/*      */   private static class MapMultimap<K, V>
/*      */     extends AbstractMultimap<K, V>
/*      */     implements SetMultimap<K, V>, Serializable
/*      */   {
/*      */     final Map<K, V> map;
/*      */     private static final long serialVersionUID = 7845222491160860175L;
/*      */     
/*  932 */     MapMultimap(Map<K, V> map) { this.map = (Map)Preconditions.checkNotNull(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  937 */     public int size() { return this.map.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  942 */     public boolean containsKey(Object key) { return this.map.containsKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  947 */     public boolean containsValue(Object value) { return this.map.containsValue(value); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     public boolean containsEntry(Object key, Object value) { return this.map.entrySet().contains(Maps.immutableEntry(key, value)); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> get(final K key) {
/*  957 */       return new Sets.ImprovedAbstractSet<V>() {
/*      */           public Iterator<V> iterator() {
/*  959 */             return new Iterator<V>()
/*      */               {
/*      */                 int i;
/*      */ 
/*      */                 
/*  964 */                 public boolean hasNext() { return (super.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key)); }
/*      */ 
/*      */ 
/*      */                 
/*      */                 public V next() {
/*  969 */                   if (!super.hasNext()) {
/*  970 */                     throw new NoSuchElementException();
/*      */                   }
/*  972 */                   super.i++;
/*  973 */                   return (V)Multimaps.MapMultimap.this.map.get(key);
/*      */                 }
/*      */ 
/*      */                 
/*      */                 public void remove() {
/*  978 */                   CollectPreconditions.checkRemove((super.i == 1));
/*  979 */                   super.i = -1;
/*  980 */                   Multimaps.MapMultimap.this.map.remove(key);
/*      */                 }
/*      */               };
/*      */           }
/*      */ 
/*      */           
/*  986 */           public int size() { return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0; }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  993 */     public boolean put(K key, V value) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  998 */     public boolean putAll(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1003 */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1008 */     public Set<V> replaceValues(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1013 */     public boolean remove(Object key, Object value) { return this.map.entrySet().remove(Maps.immutableEntry(key, value)); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> removeAll(Object key) {
/* 1018 */       Set<V> values = new HashSet<V>(2);
/* 1019 */       if (!this.map.containsKey(key)) {
/* 1020 */         return values;
/*      */       }
/* 1022 */       values.add(this.map.remove(key));
/* 1023 */       return values;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1028 */     public void clear() { this.map.clear(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1033 */     public Set<K> keySet() { return this.map.keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1038 */     public Collection<V> values() { return this.map.values(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     public Set<Map.Entry<K, V>> entries() { return this.map.entrySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1048 */     Iterator<Map.Entry<K, V>> entryIterator() { return this.map.entrySet().iterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1053 */     Map<K, Collection<V>> createAsMap() { return new Multimaps.AsMap(this); }
/*      */ 
/*      */ 
/*      */     
/* 1057 */     public int hashCode() { return this.map.hashCode(); }
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
/*      */   public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1109 */     Preconditions.checkNotNull(function);
/* 1110 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1111 */     return transformEntries(fromMultimap, transformer);
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
/* 1172 */   public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) { return new TransformedEntriesMultimap(fromMap, transformer); }
/*      */ 
/*      */   
/*      */   private static class TransformedEntriesMultimap<K, V1, V2>
/*      */     extends AbstractMultimap<K, V2>
/*      */   {
/*      */     final Multimap<K, V1> fromMultimap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1182 */       this.fromMultimap = (Multimap)Preconditions.checkNotNull(fromMultimap);
/* 1183 */       this.transformer = (Maps.EntryTransformer)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */     
/*      */     Collection<V2> transform(K key, Collection<V1> values) {
/* 1187 */       Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
/*      */       
/* 1189 */       if (values instanceof List) {
/* 1190 */         return Lists.transform((List)values, function);
/*      */       }
/* 1192 */       return Collections2.transform(values, function);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Map<K, Collection<V2>> createAsMap() {
/* 1198 */       return Maps.transformEntries(this.fromMultimap.asMap(), new Maps.EntryTransformer<K, Collection<V1>, Collection<V2>>()
/*      */           {
/*      */             public Collection<V2> transformEntry(K key, Collection<V1> value)
/*      */             {
/* 1202 */               return super.this$0.transform(key, value);
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/* 1208 */     public void clear() { this.fromMultimap.clear(); }
/*      */ 
/*      */ 
/*      */     
/* 1212 */     public boolean containsKey(Object key) { return this.fromMultimap.containsKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1217 */     Iterator<Map.Entry<K, V2>> entryIterator() { return Iterators.transform(this.fromMultimap.entries().iterator(), Maps.asEntryToEntryFunction(this.transformer)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1222 */     public Collection<V2> get(K key) { return transform(key, this.fromMultimap.get(key)); }
/*      */ 
/*      */ 
/*      */     
/* 1226 */     public boolean isEmpty() { return this.fromMultimap.isEmpty(); }
/*      */ 
/*      */ 
/*      */     
/* 1230 */     public Set<K> keySet() { return this.fromMultimap.keySet(); }
/*      */ 
/*      */ 
/*      */     
/* 1234 */     public Multiset<K> keys() { return this.fromMultimap.keys(); }
/*      */ 
/*      */ 
/*      */     
/* 1238 */     public boolean put(K key, V2 value) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/* 1242 */     public boolean putAll(K key, Iterable<? extends V2> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1247 */     public boolean putAll(Multimap<? extends K, ? extends V2> multimap) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1252 */     public boolean remove(Object key, Object value) { return get(key).remove(value); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1257 */     public Collection<V2> removeAll(Object key) { return transform(key, this.fromMultimap.removeAll(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1262 */     public Collection<V2> replaceValues(K key, Iterable<? extends V2> values) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/* 1266 */     public int size() { return this.fromMultimap.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1271 */     Collection<V2> createValues() { return Collections2.transform(this.fromMultimap.entries(), Maps.asEntryToValueFunction(this.transformer)); }
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
/*      */   public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, Function<? super V1, V2> function) {
/* 1320 */     Preconditions.checkNotNull(function);
/* 1321 */     Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
/* 1322 */     return transformEntries(fromMultimap, transformer);
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
/* 1380 */   public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) { return new TransformedEntriesListMultimap(fromMap, transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class TransformedEntriesListMultimap<K, V1, V2>
/*      */     extends TransformedEntriesMultimap<K, V1, V2>
/*      */     implements ListMultimap<K, V2>
/*      */   {
/* 1389 */     TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) { super(fromMultimap, transformer); }
/*      */ 
/*      */ 
/*      */     
/* 1393 */     List<V2> transform(K key, Collection<V1> values) { return Lists.transform((List)values, Maps.asValueToValueFunction(this.transformer, key)); }
/*      */ 
/*      */ 
/*      */     
/* 1397 */     public List<V2> get(K key) { return transform(key, this.fromMultimap.get(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1402 */     public List<V2> removeAll(Object key) { return transform(key, this.fromMultimap.removeAll(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1407 */     public List<V2> replaceValues(K key, Iterable<? extends V2> values) { throw new UnsupportedOperationException(); }
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
/* 1455 */   public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) { return index(values.iterator(), keyFunction); }
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
/*      */   public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1503 */     Preconditions.checkNotNull(keyFunction);
/* 1504 */     ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
/*      */     
/* 1506 */     while (values.hasNext()) {
/* 1507 */       V value = (V)values.next();
/* 1508 */       Preconditions.checkNotNull(value, values);
/* 1509 */       builder.put(keyFunction.apply(value), value);
/*      */     } 
/* 1511 */     return builder.build();
/*      */   }
/*      */   
/*      */   static class Keys<K, V>
/*      */     extends AbstractMultiset<K> {
/*      */     final Multimap<K, V> multimap;
/*      */     
/* 1518 */     Keys(Multimap<K, V> multimap) { this.multimap = multimap; }
/*      */ 
/*      */     
/*      */     Iterator<Multiset.Entry<K>> entryIterator() {
/* 1522 */       return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this.multimap.asMap().entrySet().iterator())
/*      */         {
/*      */           
/*      */           Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry)
/*      */           {
/* 1527 */             return new Multisets.AbstractEntry<K>()
/*      */               {
/*      */                 public K getElement() {
/* 1530 */                   return (K)backingEntry.getKey();
/*      */                 }
/*      */ 
/*      */ 
/*      */                 
/* 1535 */                 public int getCount() { return ((Collection)backingEntry.getValue()).size(); }
/*      */               };
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1543 */     int distinctElements() { return this.multimap.asMap().size(); }
/*      */ 
/*      */ 
/*      */     
/* 1547 */     Set<Multiset.Entry<K>> createEntrySet() { return new KeysEntrySet(); }
/*      */     
/*      */     class KeysEntrySet
/*      */       extends Multisets.EntrySet<K>
/*      */     {
/* 1552 */       Multiset<K> multiset() { return Multimaps.Keys.this; }
/*      */ 
/*      */ 
/*      */       
/* 1556 */       public Iterator<Multiset.Entry<K>> iterator() { return Multimaps.Keys.this.entryIterator(); }
/*      */ 
/*      */ 
/*      */       
/* 1560 */       public int size() { return Multimaps.Keys.this.distinctElements(); }
/*      */ 
/*      */ 
/*      */       
/* 1564 */       public boolean isEmpty() { return Multimaps.Keys.this.multimap.isEmpty(); }
/*      */ 
/*      */       
/*      */       public boolean contains(@Nullable Object o) {
/* 1568 */         if (o instanceof Multiset.Entry) {
/* 1569 */           Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1570 */           Collection<V> collection = (Collection)Multimaps.Keys.this.multimap.asMap().get(entry.getElement());
/* 1571 */           return (collection != null && collection.size() == entry.getCount());
/*      */         } 
/* 1573 */         return false;
/*      */       }
/*      */       
/*      */       public boolean remove(@Nullable Object o) {
/* 1577 */         if (o instanceof Multiset.Entry) {
/* 1578 */           Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 1579 */           Collection<V> collection = (Collection)Multimaps.Keys.this.multimap.asMap().get(entry.getElement());
/* 1580 */           if (collection != null && collection.size() == entry.getCount()) {
/* 1581 */             collection.clear();
/* 1582 */             return true;
/*      */           } 
/*      */         } 
/* 1585 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1590 */     public boolean contains(@Nullable Object element) { return this.multimap.containsKey(element); }
/*      */ 
/*      */ 
/*      */     
/* 1594 */     public Iterator<K> iterator() { return Maps.keyIterator(this.multimap.entries().iterator()); }
/*      */ 
/*      */     
/*      */     public int count(@Nullable Object element) {
/* 1598 */       Collection<V> values = (Collection)Maps.safeGet(this.multimap.asMap(), element);
/* 1599 */       return (values == null) ? 0 : values.size();
/*      */     }
/*      */     
/*      */     public int remove(@Nullable Object element, int occurrences) {
/* 1603 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 1604 */       if (occurrences == 0) {
/* 1605 */         return count(element);
/*      */       }
/*      */       
/* 1608 */       Collection<V> values = (Collection)Maps.safeGet(this.multimap.asMap(), element);
/*      */       
/* 1610 */       if (values == null) {
/* 1611 */         return 0;
/*      */       }
/*      */       
/* 1614 */       int oldCount = values.size();
/* 1615 */       if (occurrences >= oldCount) {
/* 1616 */         values.clear();
/*      */       } else {
/* 1618 */         Iterator<V> iterator = values.iterator();
/* 1619 */         for (int i = 0; i < occurrences; i++) {
/* 1620 */           iterator.next();
/* 1621 */           iterator.remove();
/*      */         } 
/*      */       } 
/* 1624 */       return oldCount;
/*      */     }
/*      */ 
/*      */     
/* 1628 */     public void clear() { this.multimap.clear(); }
/*      */ 
/*      */ 
/*      */     
/* 1632 */     public Set<K> elementSet() { return this.multimap.keySet(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class Entries<K, V>
/*      */     extends AbstractCollection<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Multimap<K, V> multimap();
/*      */ 
/*      */ 
/*      */     
/* 1644 */     public int size() { return multimap().size(); }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/* 1648 */       if (o instanceof Map.Entry) {
/* 1649 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 1650 */         return multimap().containsEntry(entry.getKey(), entry.getValue());
/*      */       } 
/* 1652 */       return false;
/*      */     }
/*      */     
/*      */     public boolean remove(@Nullable Object o) {
/* 1656 */       if (o instanceof Map.Entry) {
/* 1657 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 1658 */         return multimap().remove(entry.getKey(), entry.getValue());
/*      */       } 
/* 1660 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1664 */     public void clear() { multimap().clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class AsMap<K, V>
/*      */     extends Maps.ImprovedAbstractMap<K, Collection<V>>
/*      */   {
/*      */     private final Multimap<K, V> multimap;
/*      */ 
/*      */ 
/*      */     
/* 1676 */     AsMap(Multimap<K, V> multimap) { this.multimap = (Multimap)Preconditions.checkNotNull(multimap); }
/*      */ 
/*      */ 
/*      */     
/* 1680 */     public int size() { return this.multimap.keySet().size(); }
/*      */ 
/*      */ 
/*      */     
/* 1684 */     protected Set<Map.Entry<K, Collection<V>>> createEntrySet() { return new EntrySet(); }
/*      */ 
/*      */ 
/*      */     
/* 1688 */     void removeValuesForKey(Object key) { this.multimap.keySet().remove(key); }
/*      */     
/*      */     class EntrySet
/*      */       extends Maps.EntrySet<K, Collection<V>>
/*      */     {
/* 1693 */       Map<K, Collection<V>> map() { return Multimaps.AsMap.this; }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 1697 */         return Maps.asMapEntryIterator(Multimaps.AsMap.this.multimap.keySet(), new Function<K, Collection<V>>()
/*      */             {
/*      */               public Collection<V> apply(K key) {
/* 1700 */                 return Multimaps.AsMap.EntrySet.this.this$0.multimap.get(key);
/*      */               }
/*      */             });
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 1706 */         if (!contains(o)) {
/* 1707 */           return false;
/*      */         }
/* 1709 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 1710 */         Multimaps.AsMap.this.removeValuesForKey(entry.getKey());
/* 1711 */         return true;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1717 */     public Collection<V> get(Object key) { return containsKey(key) ? this.multimap.get(key) : null; }
/*      */ 
/*      */ 
/*      */     
/* 1721 */     public Collection<V> remove(Object key) { return containsKey(key) ? this.multimap.removeAll(key) : null; }
/*      */ 
/*      */ 
/*      */     
/* 1725 */     public Set<K> keySet() { return this.multimap.keySet(); }
/*      */ 
/*      */ 
/*      */     
/* 1729 */     public boolean isEmpty() { return this.multimap.isEmpty(); }
/*      */ 
/*      */ 
/*      */     
/* 1733 */     public boolean containsKey(Object key) { return this.multimap.containsKey(key); }
/*      */ 
/*      */ 
/*      */     
/* 1737 */     public void clear() { this.multimap.clear(); }
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
/*      */   public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1773 */     if (unfiltered instanceof SetMultimap)
/* 1774 */       return filterKeys((SetMultimap)unfiltered, keyPredicate); 
/* 1775 */     if (unfiltered instanceof ListMultimap)
/* 1776 */       return filterKeys((ListMultimap)unfiltered, keyPredicate); 
/* 1777 */     if (unfiltered instanceof FilteredKeyMultimap) {
/* 1778 */       FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap)unfiltered;
/* 1779 */       return new FilteredKeyMultimap(prev.unfiltered, Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/* 1781 */     if (unfiltered instanceof FilteredMultimap) {
/* 1782 */       FilteredMultimap<K, V> prev = (FilteredMultimap)unfiltered;
/* 1783 */       return filterFiltered(prev, Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1785 */     return new FilteredKeyMultimap(unfiltered, keyPredicate);
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
/*      */   public static <K, V> SetMultimap<K, V> filterKeys(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1821 */     if (unfiltered instanceof FilteredKeySetMultimap) {
/* 1822 */       FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap)unfiltered;
/* 1823 */       return new FilteredKeySetMultimap(prev.unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/* 1825 */     if (unfiltered instanceof FilteredSetMultimap) {
/* 1826 */       FilteredSetMultimap<K, V> prev = (FilteredSetMultimap)unfiltered;
/* 1827 */       return filterFiltered(prev, Maps.keyPredicateOnEntries(keyPredicate));
/*      */     } 
/* 1829 */     return new FilteredKeySetMultimap(unfiltered, keyPredicate);
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
/*      */   public static <K, V> ListMultimap<K, V> filterKeys(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 1865 */     if (unfiltered instanceof FilteredKeyListMultimap) {
/* 1866 */       FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap)unfiltered;
/* 1867 */       return new FilteredKeyListMultimap(prev.unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
/*      */     } 
/*      */     
/* 1870 */     return new FilteredKeyListMultimap(unfiltered, keyPredicate);
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
/* 1906 */   public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> unfiltered, Predicate<? super V> valuePredicate) { return filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate)); }
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
/* 1941 */   public static <K, V> SetMultimap<K, V> filterValues(SetMultimap<K, V> unfiltered, Predicate<? super V> valuePredicate) { return filterEntries(unfiltered, Maps.valuePredicateOnEntries(valuePredicate)); }
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
/*      */   public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 1974 */     Preconditions.checkNotNull(entryPredicate);
/* 1975 */     if (unfiltered instanceof SetMultimap) {
/* 1976 */       return filterEntries((SetMultimap)unfiltered, entryPredicate);
/*      */     }
/* 1978 */     return (unfiltered instanceof FilteredMultimap) ? filterFiltered((FilteredMultimap)unfiltered, entryPredicate) : new FilteredEntryMultimap((Multimap)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   public static <K, V> SetMultimap<K, V> filterEntries(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2013 */     Preconditions.checkNotNull(entryPredicate);
/* 2014 */     return (unfiltered instanceof FilteredSetMultimap) ? filterFiltered((FilteredSetMultimap)unfiltered, entryPredicate) : new FilteredEntrySetMultimap((SetMultimap)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2028 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/*      */     
/* 2030 */     return new FilteredEntryMultimap(multimap.unfiltered(), predicate);
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
/*      */   private static <K, V> SetMultimap<K, V> filterFiltered(FilteredSetMultimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2042 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
/*      */     
/* 2044 */     return new FilteredEntrySetMultimap(multimap.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static boolean equalsImpl(Multimap<?, ?> multimap, @Nullable Object object) {
/* 2048 */     if (object == multimap) {
/* 2049 */       return true;
/*      */     }
/* 2051 */     if (object instanceof Multimap) {
/* 2052 */       Multimap<?, ?> that = (Multimap)object;
/* 2053 */       return multimap.asMap().equals(that.asMap());
/*      */     } 
/* 2055 */     return false;
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Multimaps.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */