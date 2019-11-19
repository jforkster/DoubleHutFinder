/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class MapConstraints
/*     */ {
/*  54 */   public static MapConstraint<Object, Object> notNull() { return NotNullMapConstraint.INSTANCE; }
/*     */   
/*     */   private enum NotNullMapConstraint
/*     */     implements MapConstraint<Object, Object>
/*     */   {
/*  59 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void checkKeyValue(Object key, Object value) {
/*  63 */       Preconditions.checkNotNull(key);
/*  64 */       Preconditions.checkNotNull(value);
/*     */     }
/*     */ 
/*     */     
/*  68 */     public String toString() { return "Not null"; }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static <K, V> Map<K, V> constrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedMap(map, constraint); }
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
/* 107 */   public static <K, V> Multimap<K, V> constrainedMultimap(Multimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedMultimap(multimap, constraint); }
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
/* 129 */   public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedListMultimap(multimap, constraint); }
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
/* 150 */   public static <K, V> SetMultimap<K, V> constrainedSetMultimap(SetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedSetMultimap(multimap, constraint); }
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
/* 171 */   public static <K, V> SortedSetMultimap<K, V> constrainedSortedSetMultimap(SortedSetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedSortedSetMultimap(multimap, constraint); }
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
/*     */   private static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
/* 186 */     Preconditions.checkNotNull(entry);
/* 187 */     Preconditions.checkNotNull(constraint);
/* 188 */     return new ForwardingMapEntry<K, V>()
/*     */       {
/* 190 */         protected Map.Entry<K, V> delegate() { return entry; }
/*     */         
/*     */         public V setValue(V value) {
/* 193 */           constraint.checkKeyValue(getKey(), value);
/* 194 */           return (V)entry.setValue(value);
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Map.Entry<K, Collection<V>> constrainedAsMapEntry(final Map.Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> constraint) {
/* 212 */     Preconditions.checkNotNull(entry);
/* 213 */     Preconditions.checkNotNull(constraint);
/* 214 */     return new ForwardingMapEntry<K, Collection<V>>()
/*     */       {
/* 216 */         protected Map.Entry<K, Collection<V>> delegate() { return entry; }
/*     */         
/*     */         public Collection<V> getValue() {
/* 219 */           return Constraints.constrainedTypePreservingCollection((Collection)entry.getValue(), new Constraint<V>()
/*     */               {
/*     */                 public V checkElement(V value)
/*     */                 {
/* 223 */                   constraint.checkKeyValue(MapConstraints.null.this.getKey(), value);
/* 224 */                   return value;
/*     */                 }
/*     */               });
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 246 */   private static <K, V> Set<Map.Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedAsMapEntries(entries, constraint); }
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
/*     */   private static <K, V> Collection<Map.Entry<K, V>> constrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 264 */     if (entries instanceof Set) {
/* 265 */       return constrainedEntrySet((Set)entries, constraint);
/*     */     }
/* 267 */     return new ConstrainedEntries(entries, constraint);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 287 */   private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedEntrySet(entries, constraint); }
/*     */ 
/*     */   
/*     */   static class ConstrainedMap<K, V>
/*     */     extends ForwardingMap<K, V>
/*     */   {
/*     */     private final Map<K, V> delegate;
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     private Set<Map.Entry<K, V>> entrySet;
/*     */     
/*     */     ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 298 */       this.delegate = (Map)Preconditions.checkNotNull(delegate);
/* 299 */       this.constraint = (MapConstraint)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/* 302 */     protected Map<K, V> delegate() { return this.delegate; }
/*     */     
/*     */     public Set<Map.Entry<K, V>> entrySet() {
/* 305 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 306 */       if (result == null) {
/* 307 */         this.entrySet = result = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
/*     */       }
/*     */       
/* 310 */       return result;
/*     */     }
/*     */     public V put(K key, V value) {
/* 313 */       this.constraint.checkKeyValue(key, value);
/* 314 */       return (V)this.delegate.put(key, value);
/*     */     }
/*     */     
/* 317 */     public void putAll(Map<? extends K, ? extends V> map) { this.delegate.putAll(MapConstraints.checkMap(map, this.constraint)); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 334 */   public static <K, V> BiMap<K, V> constrainedBiMap(BiMap<K, V> map, MapConstraint<? super K, ? super V> constraint) { return new ConstrainedBiMap(map, null, constraint); }
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
/*     */   private static class ConstrainedBiMap<K, V>
/*     */     extends ConstrainedMap<K, V>
/*     */     implements BiMap<K, V>
/*     */   {
/*     */     ConstrainedBiMap(BiMap<K, V> delegate, @Nullable BiMap<V, K> inverse, MapConstraint<? super K, ? super V> constraint) {
/* 356 */       super(delegate, constraint);
/* 357 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/* 361 */     protected BiMap<K, V> delegate() { return (BiMap)super.delegate(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public V forcePut(K key, V value) {
/* 366 */       this.constraint.checkKeyValue(key, value);
/* 367 */       return (V)delegate().forcePut(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public BiMap<V, K> inverse() {
/* 372 */       if (this.inverse == null) {
/* 373 */         this.inverse = new ConstrainedBiMap(delegate().inverse(), this, new MapConstraints.InverseConstraint(this.constraint));
/*     */       }
/*     */       
/* 376 */       return this.inverse;
/*     */     }
/*     */ 
/*     */     
/* 380 */     public Set<V> values() { return delegate().values(); }
/*     */   }
/*     */   
/*     */   private static class InverseConstraint<K, V>
/*     */     extends Object
/*     */     implements MapConstraint<K, V>
/*     */   {
/*     */     final MapConstraint<? super V, ? super K> constraint;
/*     */     
/* 389 */     public InverseConstraint(MapConstraint<? super V, ? super K> constraint) { this.constraint = (MapConstraint)Preconditions.checkNotNull(constraint); }
/*     */ 
/*     */ 
/*     */     
/* 393 */     public void checkKeyValue(K key, V value) { this.constraint.checkKeyValue(value, key); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedMultimap<K, V>
/*     */     extends ForwardingMultimap<K, V>
/*     */     implements Serializable
/*     */   {
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     final Multimap<K, V> delegate;
/*     */     Collection<Map.Entry<K, V>> entries;
/*     */     Map<K, Collection<V>> asMap;
/*     */     
/*     */     public ConstrainedMultimap(Multimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
/* 407 */       this.delegate = (Multimap)Preconditions.checkNotNull(delegate);
/* 408 */       this.constraint = (MapConstraint)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */ 
/*     */     
/* 412 */     protected Multimap<K, V> delegate() { return this.delegate; }
/*     */ 
/*     */     
/*     */     public Map<K, Collection<V>> asMap() {
/* 416 */       Map<K, Collection<V>> result = this.asMap;
/* 417 */       if (result == null) {
/* 418 */         final Map<K, Collection<V>> asMapDelegate = this.delegate.asMap();
/*     */         
/* 420 */         this.asMap = result = new ForwardingMap<K, Collection<V>>()
/*     */           {
/*     */             Set<Map.Entry<K, Collection<V>>> entrySet;
/*     */             Collection<Collection<V>> values;
/*     */             
/* 425 */             protected Map<K, Collection<V>> delegate() { return asMapDelegate; }
/*     */ 
/*     */             
/*     */             public Set<Map.Entry<K, Collection<V>>> entrySet() {
/* 429 */               Set<Map.Entry<K, Collection<V>>> result = super.entrySet;
/* 430 */               if (result == null) {
/* 431 */                 super.entrySet = result = MapConstraints.constrainedAsMapEntries(asMapDelegate.entrySet(), MapConstraints.ConstrainedMultimap.this.constraint);
/*     */               }
/*     */               
/* 434 */               return result;
/*     */             }
/*     */ 
/*     */             
/*     */             public Collection<V> get(Object key) {
/*     */               try {
/* 440 */                 Collection<V> collection = super.this$0.get(key);
/* 441 */                 return collection.isEmpty() ? null : collection;
/* 442 */               } catch (ClassCastException e) {
/* 443 */                 return null;
/*     */               } 
/*     */             }
/*     */             
/*     */             public Collection<Collection<V>> values() {
/* 448 */               Collection<Collection<V>> result = super.values;
/* 449 */               if (result == null) {
/* 450 */                 super.values = result = new MapConstraints.ConstrainedAsMapValues<Collection<V>>(super.delegate().values(), super.entrySet());
/*     */               }
/*     */               
/* 453 */               return result;
/*     */             }
/*     */             
/*     */             public boolean containsValue(Object o) {
/* 457 */               return super.values().contains(o);
/*     */             }
/*     */           };
/*     */       } 
/* 461 */       return result;
/*     */     }
/*     */     
/*     */     public Collection<Map.Entry<K, V>> entries() {
/* 465 */       Collection<Map.Entry<K, V>> result = this.entries;
/* 466 */       if (result == null) {
/* 467 */         this.entries = result = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
/*     */       }
/* 469 */       return result;
/*     */     }
/*     */     
/*     */     public Collection<V> get(final K key) {
/* 473 */       return Constraints.constrainedTypePreservingCollection(this.delegate.get(key), new Constraint<V>()
/*     */           {
/*     */             public V checkElement(V value)
/*     */             {
/* 477 */               MapConstraints.ConstrainedMultimap.this.constraint.checkKeyValue(key, value);
/* 478 */               return value;
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public boolean put(K key, V value) {
/* 484 */       this.constraint.checkKeyValue(key, value);
/* 485 */       return this.delegate.put(key, value);
/*     */     }
/*     */ 
/*     */     
/* 489 */     public boolean putAll(K key, Iterable<? extends V> values) { return this.delegate.putAll(key, MapConstraints.checkValues(key, values, this.constraint)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 494 */       boolean changed = false;
/* 495 */       for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/* 496 */         changed |= put(entry.getKey(), entry.getValue());
/*     */       }
/* 498 */       return changed;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 503 */     public Collection<V> replaceValues(K key, Iterable<? extends V> values) { return this.delegate.replaceValues(key, MapConstraints.checkValues(key, values, this.constraint)); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ConstrainedAsMapValues<K, V>
/*     */     extends ForwardingCollection<Collection<V>>
/*     */   {
/*     */     final Collection<Collection<V>> delegate;
/*     */ 
/*     */     
/*     */     final Set<Map.Entry<K, Collection<V>>> entrySet;
/*     */ 
/*     */ 
/*     */     
/*     */     ConstrainedAsMapValues(Collection<Collection<V>> delegate, Set<Map.Entry<K, Collection<V>>> entrySet) {
/* 519 */       this.delegate = delegate;
/* 520 */       this.entrySet = entrySet;
/*     */     }
/*     */     
/* 523 */     protected Collection<Collection<V>> delegate() { return this.delegate; }
/*     */ 
/*     */     
/*     */     public Iterator<Collection<V>> iterator() {
/* 527 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entrySet.iterator();
/* 528 */       return new Iterator<Collection<V>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 531 */             return iterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/* 535 */           public Collection<V> next() { return (Collection)((Map.Entry)iterator.next()).getValue(); }
/*     */ 
/*     */ 
/*     */           
/* 539 */           public void remove() { iterator.remove(); }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 545 */     public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */     
/* 548 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */ 
/*     */     
/* 551 */     public boolean contains(Object o) { return standardContains(o); }
/*     */ 
/*     */     
/* 554 */     public boolean containsAll(Collection<?> c) { return standardContainsAll(c); }
/*     */ 
/*     */     
/* 557 */     public boolean remove(Object o) { return standardRemove(o); }
/*     */ 
/*     */     
/* 560 */     public boolean removeAll(Collection<?> c) { return standardRemoveAll(c); }
/*     */ 
/*     */     
/* 563 */     public boolean retainAll(Collection<?> c) { return standardRetainAll(c); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedEntries<K, V>
/*     */     extends ForwardingCollection<Map.Entry<K, V>>
/*     */   {
/*     */     final MapConstraint<? super K, ? super V> constraint;
/*     */     
/*     */     final Collection<Map.Entry<K, V>> entries;
/*     */     
/*     */     ConstrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 575 */       this.entries = entries;
/* 576 */       this.constraint = constraint;
/*     */     }
/*     */     
/* 579 */     protected Collection<Map.Entry<K, V>> delegate() { return this.entries; }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 583 */       final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
/* 584 */       return new ForwardingIterator<Map.Entry<K, V>>()
/*     */         {
/* 586 */           public Map.Entry<K, V> next() { return MapConstraints.constrainedEntry((Map.Entry)iterator.next(), MapConstraints.ConstrainedEntries.this.constraint); }
/*     */ 
/*     */           
/* 589 */           protected Iterator<Map.Entry<K, V>> delegate() { return iterator; }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 597 */     public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */     
/* 600 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */ 
/*     */     
/* 603 */     public boolean contains(Object o) { return Maps.containsEntryImpl(delegate(), o); }
/*     */ 
/*     */     
/* 606 */     public boolean containsAll(Collection<?> c) { return standardContainsAll(c); }
/*     */ 
/*     */     
/* 609 */     public boolean remove(Object o) { return Maps.removeEntryImpl(delegate(), o); }
/*     */ 
/*     */     
/* 612 */     public boolean removeAll(Collection<?> c) { return standardRemoveAll(c); }
/*     */ 
/*     */     
/* 615 */     public boolean retainAll(Collection<?> c) { return standardRetainAll(c); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConstrainedEntrySet<K, V>
/*     */     extends ConstrainedEntries<K, V>
/*     */     implements Set<Map.Entry<K, V>>
/*     */   {
/* 624 */     ConstrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) { super(entries, constraint); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 630 */     public boolean equals(@Nullable Object object) { return Sets.equalsImpl(this, object); }
/*     */ 
/*     */ 
/*     */     
/* 634 */     public int hashCode() { return Sets.hashCodeImpl(this); }
/*     */   }
/*     */ 
/*     */   
/*     */   static class ConstrainedAsMapEntries<K, V>
/*     */     extends ForwardingSet<Map.Entry<K, Collection<V>>>
/*     */   {
/*     */     private final MapConstraint<? super K, ? super V> constraint;
/*     */     
/*     */     private final Set<Map.Entry<K, Collection<V>>> entries;
/*     */     
/*     */     ConstrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
/* 646 */       this.entries = entries;
/* 647 */       this.constraint = constraint;
/*     */     }
/*     */ 
/*     */     
/* 651 */     protected Set<Map.Entry<K, Collection<V>>> delegate() { return this.entries; }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 655 */       final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entries.iterator();
/* 656 */       return new ForwardingIterator<Map.Entry<K, Collection<V>>>()
/*     */         {
/* 658 */           public Map.Entry<K, Collection<V>> next() { return MapConstraints.constrainedAsMapEntry((Map.Entry)iterator.next(), super.this$0.constraint); }
/*     */ 
/*     */           
/* 661 */           protected Iterator<Map.Entry<K, Collection<V>>> delegate() { return iterator; }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 669 */     public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */ 
/*     */     
/* 673 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */ 
/*     */ 
/*     */     
/* 677 */     public boolean contains(Object o) { return Maps.containsEntryImpl(delegate(), o); }
/*     */ 
/*     */ 
/*     */     
/* 681 */     public boolean containsAll(Collection<?> c) { return standardContainsAll(c); }
/*     */ 
/*     */ 
/*     */     
/* 685 */     public boolean equals(@Nullable Object object) { return standardEquals(object); }
/*     */ 
/*     */ 
/*     */     
/* 689 */     public int hashCode() { return standardHashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 693 */     public boolean remove(Object o) { return Maps.removeEntryImpl(delegate(), o); }
/*     */ 
/*     */ 
/*     */     
/* 697 */     public boolean removeAll(Collection<?> c) { return standardRemoveAll(c); }
/*     */ 
/*     */ 
/*     */     
/* 701 */     public boolean retainAll(Collection<?> c) { return standardRetainAll(c); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedListMultimap<K, V>
/*     */     extends ConstrainedMultimap<K, V>
/*     */     implements ListMultimap<K, V>
/*     */   {
/* 709 */     ConstrainedListMultimap(ListMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) { super(delegate, constraint); }
/*     */ 
/*     */     
/* 712 */     public List<V> get(K key) { return (List)super.get(key); }
/*     */ 
/*     */     
/* 715 */     public List<V> removeAll(Object key) { return (List)super.removeAll(key); }
/*     */ 
/*     */ 
/*     */     
/* 719 */     public List<V> replaceValues(K key, Iterable<? extends V> values) { return (List)super.replaceValues(key, values); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedSetMultimap<K, V>
/*     */     extends ConstrainedMultimap<K, V>
/*     */     implements SetMultimap<K, V>
/*     */   {
/* 727 */     ConstrainedSetMultimap(SetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) { super(delegate, constraint); }
/*     */ 
/*     */     
/* 730 */     public Set<V> get(K key) { return (Set)super.get(key); }
/*     */ 
/*     */     
/* 733 */     public Set<Map.Entry<K, V>> entries() { return (Set)super.entries(); }
/*     */ 
/*     */     
/* 736 */     public Set<V> removeAll(Object key) { return (Set)super.removeAll(key); }
/*     */ 
/*     */ 
/*     */     
/* 740 */     public Set<V> replaceValues(K key, Iterable<? extends V> values) { return (Set)super.replaceValues(key, values); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConstrainedSortedSetMultimap<K, V>
/*     */     extends ConstrainedSetMultimap<K, V>
/*     */     implements SortedSetMultimap<K, V>
/*     */   {
/* 748 */     ConstrainedSortedSetMultimap(SortedSetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) { super(delegate, constraint); }
/*     */ 
/*     */     
/* 751 */     public SortedSet<V> get(K key) { return (SortedSet)super.get(key); }
/*     */ 
/*     */     
/* 754 */     public SortedSet<V> removeAll(Object key) { return (SortedSet)super.removeAll(key); }
/*     */ 
/*     */ 
/*     */     
/* 758 */     public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) { return (SortedSet)super.replaceValues(key, values); }
/*     */ 
/*     */ 
/*     */     
/* 762 */     public Comparator<? super V> valueComparator() { return ((SortedSetMultimap)delegate()).valueComparator(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Collection<V> checkValues(K key, Iterable<? extends V> values, MapConstraint<? super K, ? super V> constraint) {
/* 769 */     Collection<V> copy = Lists.newArrayList(values);
/* 770 */     for (V value : copy) {
/* 771 */       constraint.checkKeyValue(key, value);
/*     */     }
/* 773 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> constraint) {
/* 778 */     Map<K, V> copy = new LinkedHashMap<K, V>(map);
/* 779 */     for (Map.Entry<K, V> entry : copy.entrySet()) {
/* 780 */       constraint.checkKeyValue(entry.getKey(), entry.getValue());
/*     */     }
/* 782 */     return copy;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/MapConstraints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */