/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible
/*     */ class FilteredEntryMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements FilteredMultimap<K, V>
/*     */ {
/*     */   final Multimap<K, V> unfiltered;
/*     */   final Predicate<? super Map.Entry<K, V>> predicate;
/*     */   
/*     */   FilteredEntryMultimap(Multimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/*  51 */     this.unfiltered = (Multimap)Preconditions.checkNotNull(unfiltered);
/*  52 */     this.predicate = (Predicate)Preconditions.checkNotNull(predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public Multimap<K, V> unfiltered() { return this.unfiltered; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public Predicate<? super Map.Entry<K, V>> entryPredicate() { return this.predicate; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public int size() { return entries().size(); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   private boolean satisfies(K key, V value) { return this.predicate.apply(Maps.immutableEntry(key, value)); }
/*     */   
/*     */   final class ValuePredicate
/*     */     extends Object
/*     */     implements Predicate<V>
/*     */   {
/*     */     private final K key;
/*     */     
/*  79 */     ValuePredicate(K key) { this.key = key; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     public boolean apply(@Nullable V value) { return FilteredEntryMultimap.this.satisfies(this.key, value); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate) {
/*  90 */     if (collection instanceof Set) {
/*  91 */       return Sets.filter((Set)collection, predicate);
/*     */     }
/*  93 */     return Collections2.filter(collection, predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public boolean containsKey(@Nullable Object key) { return (asMap().get(key) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public Collection<V> removeAll(@Nullable Object key) { return (Collection)Objects.firstNonNull(asMap().remove(key), unmodifiableEmptyCollection()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   Collection<V> unmodifiableEmptyCollection() { return (this.unfiltered instanceof SetMultimap) ? Collections.emptySet() : Collections.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public void clear() { entries().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public Collection<V> get(K key) { return filterCollection(this.unfiltered.get(key), new ValuePredicate(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   Collection<Map.Entry<K, V>> createEntries() { return filterCollection(this.unfiltered.entries(), this.predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   Collection<V> createValues() { return new FilteredMultimapValues(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   Iterator<Map.Entry<K, V>> entryIterator() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   Map<K, Collection<V>> createAsMap() { return new AsMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public Set<K> keySet() { return asMap().keySet(); }
/*     */ 
/*     */   
/*     */   boolean removeEntriesIf(Predicate<? super Map.Entry<K, Collection<V>>> predicate) {
/* 150 */     Iterator<Map.Entry<K, Collection<V>>> entryIterator = this.unfiltered.asMap().entrySet().iterator();
/* 151 */     boolean changed = false;
/* 152 */     while (entryIterator.hasNext()) {
/* 153 */       Map.Entry<K, Collection<V>> entry = (Map.Entry)entryIterator.next();
/* 154 */       K key = (K)entry.getKey();
/* 155 */       Collection<V> collection = filterCollection((Collection)entry.getValue(), new ValuePredicate(key));
/* 156 */       if (!collection.isEmpty() && predicate.apply(Maps.immutableEntry(key, collection))) {
/* 157 */         if (collection.size() == ((Collection)entry.getValue()).size()) {
/* 158 */           entryIterator.remove();
/*     */         } else {
/* 160 */           collection.clear();
/*     */         } 
/* 162 */         changed = true;
/*     */       } 
/*     */     } 
/* 165 */     return changed;
/*     */   }
/*     */   
/*     */   class AsMap
/*     */     extends Maps.ImprovedAbstractMap<K, Collection<V>>
/*     */   {
/* 171 */     public boolean containsKey(@Nullable Object key) { return (get(key) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     public void clear() { FilteredEntryMultimap.this.clear(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Collection<V> get(@Nullable Object key) {
/* 181 */       Collection<V> result = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 182 */       if (result == null) {
/* 183 */         return null;
/*     */       }
/*     */       
/* 186 */       K k = (K)key;
/* 187 */       result = FilteredEntryMultimap.filterCollection(result, new FilteredEntryMultimap.ValuePredicate(FilteredEntryMultimap.this, k));
/* 188 */       return result.isEmpty() ? null : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> remove(@Nullable Object key) {
/* 193 */       Collection<V> collection = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 194 */       if (collection == null) {
/* 195 */         return null;
/*     */       }
/*     */       
/* 198 */       K k = (K)key;
/* 199 */       List<V> result = Lists.newArrayList();
/* 200 */       Iterator<V> itr = collection.iterator();
/* 201 */       while (itr.hasNext()) {
/* 202 */         V v = (V)itr.next();
/* 203 */         if (FilteredEntryMultimap.this.satisfies(k, v)) {
/* 204 */           itr.remove();
/* 205 */           result.add(v);
/*     */         } 
/*     */       } 
/* 208 */       if (result.isEmpty())
/* 209 */         return null; 
/* 210 */       if (FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) {
/* 211 */         return Collections.unmodifiableSet(Sets.newLinkedHashSet(result));
/*     */       }
/* 213 */       return Collections.unmodifiableList(result);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Set<K> createKeySet() {
/* 219 */       return new Maps.KeySet<K, Collection<V>>(this)
/*     */         {
/*     */           public boolean removeAll(Collection<?> c) {
/* 222 */             return FilteredEntryMultimap.AsMap.this.this$0.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 227 */           public boolean retainAll(Collection<?> c) { return FilteredEntryMultimap.AsMap.this.this$0.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c)))); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 232 */           public boolean remove(@Nullable Object o) { return (super.this$1.remove(o) != null); }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Set<Map.Entry<K, Collection<V>>> createEntrySet() {
/* 239 */       return new Maps.EntrySet<K, Collection<V>>()
/*     */         {
/*     */           Map<K, Collection<V>> map() {
/* 242 */             return super.this$1;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Map.Entry<K, Collection<V>>> iterator() {
/* 247 */             return new AbstractIterator<Map.Entry<K, Collection<V>>>()
/*     */               {
/*     */                 final Iterator<Map.Entry<K, Collection<V>>> backingIterator;
/*     */ 
/*     */                 
/*     */                 protected Map.Entry<K, Collection<V>> computeNext() {
/* 253 */                   while (super.backingIterator.hasNext()) {
/* 254 */                     Map.Entry<K, Collection<V>> entry = (Map.Entry)super.backingIterator.next();
/* 255 */                     K key = (K)entry.getKey();
/* 256 */                     Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), new FilteredEntryMultimap.ValuePredicate(FilteredEntryMultimap.AsMap.this.this$0, key));
/*     */                     
/* 258 */                     if (!collection.isEmpty()) {
/* 259 */                       return Maps.immutableEntry(key, collection);
/*     */                     }
/*     */                   } 
/* 262 */                   return (Map.Entry)endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 269 */           public boolean removeAll(Collection<?> c) { return FilteredEntryMultimap.AsMap.this.this$0.removeEntriesIf(Predicates.in(c)); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 274 */           public boolean retainAll(Collection<?> c) { return FilteredEntryMultimap.AsMap.this.this$0.removeEntriesIf(Predicates.not(Predicates.in(c))); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 279 */           public int size() { return Iterators.size(super.iterator()); }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Collection<Collection<V>> createValues() {
/* 286 */       return new Maps.Values<K, Collection<V>>(this)
/*     */         {
/*     */           public boolean remove(@Nullable Object o) {
/* 289 */             if (o instanceof Collection) {
/* 290 */               Collection<?> c = (Collection)o;
/* 291 */               Iterator<Map.Entry<K, Collection<V>>> entryIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
/*     */               
/* 293 */               while (entryIterator.hasNext()) {
/* 294 */                 Map.Entry<K, Collection<V>> entry = (Map.Entry)entryIterator.next();
/* 295 */                 K key = (K)entry.getKey();
/* 296 */                 Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), new FilteredEntryMultimap.ValuePredicate(FilteredEntryMultimap.AsMap.this.this$0, key));
/*     */                 
/* 298 */                 if (!collection.isEmpty() && c.equals(collection)) {
/* 299 */                   if (collection.size() == ((Collection)entry.getValue()).size()) {
/* 300 */                     entryIterator.remove();
/*     */                   } else {
/* 302 */                     collection.clear();
/*     */                   } 
/* 304 */                   return true;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 308 */             return false;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 313 */           public boolean removeAll(Collection<?> c) { return FilteredEntryMultimap.AsMap.this.this$0.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c))); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 318 */           public boolean retainAll(Collection<?> c) { return FilteredEntryMultimap.AsMap.this.this$0.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))); }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   Multiset<K> createKeys() { return new Keys(); }
/*     */   
/*     */   class Keys
/*     */     extends Multimaps.Keys<K, V> {
/*     */     Keys() {
/* 331 */       super(FilteredEntryMultimap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public int remove(@Nullable Object key, int occurrences) {
/* 336 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 337 */       if (occurrences == 0) {
/* 338 */         return count(key);
/*     */       }
/* 340 */       Collection<V> collection = (Collection)FilteredEntryMultimap.this.unfiltered.asMap().get(key);
/* 341 */       if (collection == null) {
/* 342 */         return 0;
/*     */       }
/*     */       
/* 345 */       K k = (K)key;
/* 346 */       int oldCount = 0;
/* 347 */       Iterator<V> itr = collection.iterator();
/* 348 */       while (itr.hasNext()) {
/* 349 */         V v = (V)itr.next();
/*     */         
/* 351 */         oldCount++;
/* 352 */         if (FilteredEntryMultimap.this.satisfies(k, v) && oldCount <= occurrences) {
/* 353 */           itr.remove();
/*     */         }
/*     */       } 
/*     */       
/* 357 */       return oldCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Multiset.Entry<K>> entrySet() {
/* 362 */       return new Multisets.EntrySet<K>()
/*     */         {
/*     */           Multiset<K> multiset()
/*     */           {
/* 366 */             return super.this$1;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 371 */           public Iterator<Multiset.Entry<K>> iterator() { return super.this$1.entryIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 376 */           public int size() { return FilteredEntryMultimap.Keys.this.this$0.keySet().size(); }
/*     */ 
/*     */           
/*     */           private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> predicate) {
/* 380 */             return FilteredEntryMultimap.Keys.this.this$0.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>()
/*     */                 {
/*     */                   public boolean apply(Map.Entry<K, Collection<V>> entry)
/*     */                   {
/* 384 */                     return predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size()));
/*     */                   }
/*     */                 });
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 392 */           public boolean removeAll(Collection<?> c) { return super.removeEntriesIf(Predicates.in(c)); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 397 */           public boolean retainAll(Collection<?> c) { return super.removeEntriesIf(Predicates.not(Predicates.in(c))); }
/*     */         };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredEntryMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */