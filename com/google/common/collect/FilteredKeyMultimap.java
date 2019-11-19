/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
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
/*     */ @GwtCompatible
/*     */ class FilteredKeyMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements FilteredMultimap<K, V>
/*     */ {
/*     */   final Multimap<K, V> unfiltered;
/*     */   final Predicate<? super K> keyPredicate;
/*     */   
/*     */   FilteredKeyMultimap(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/*  44 */     this.unfiltered = (Multimap)Preconditions.checkNotNull(unfiltered);
/*  45 */     this.keyPredicate = (Predicate)Preconditions.checkNotNull(keyPredicate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public Multimap<K, V> unfiltered() { return this.unfiltered; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public Predicate<? super Map.Entry<K, V>> entryPredicate() { return Maps.keyPredicateOnEntries(this.keyPredicate); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  60 */     int size = 0;
/*  61 */     for (Collection<V> collection : asMap().values()) {
/*  62 */       size += collection.size();
/*     */     }
/*  64 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/*  69 */     if (this.unfiltered.containsKey(key)) {
/*     */       
/*  71 */       K k = (K)key;
/*  72 */       return this.keyPredicate.apply(k);
/*     */     } 
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public Collection<V> removeAll(Object key) { return containsKey(key) ? this.unfiltered.removeAll(key) : unmodifiableEmptyCollection(); }
/*     */ 
/*     */   
/*     */   Collection<V> unmodifiableEmptyCollection() {
/*  83 */     if (this.unfiltered instanceof SetMultimap) {
/*  84 */       return ImmutableSet.of();
/*     */     }
/*  86 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void clear() { keySet().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   Set<K> createKeySet() { return Sets.filter(this.unfiltered.keySet(), this.keyPredicate); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> get(K key) {
/* 102 */     if (this.keyPredicate.apply(key))
/* 103 */       return this.unfiltered.get(key); 
/* 104 */     if (this.unfiltered instanceof SetMultimap) {
/* 105 */       return new AddRejectingSet(key);
/*     */     }
/* 107 */     return new AddRejectingList(key);
/*     */   }
/*     */   
/*     */   static class AddRejectingSet<K, V>
/*     */     extends ForwardingSet<V>
/*     */   {
/*     */     final K key;
/*     */     
/* 115 */     AddRejectingSet(K key) { this.key = key; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     public boolean add(V element) { throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends V> collection) {
/* 125 */       Preconditions.checkNotNull(collection);
/* 126 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 131 */     protected Set<V> delegate() { return Collections.emptySet(); }
/*     */   }
/*     */   
/*     */   static class AddRejectingList<K, V>
/*     */     extends ForwardingList<V>
/*     */   {
/*     */     final K key;
/*     */     
/* 139 */     AddRejectingList(K key) { this.key = key; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(V v) {
/* 144 */       add(0, v);
/* 145 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends V> collection) {
/* 150 */       addAll(0, collection);
/* 151 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, V element) {
/* 156 */       Preconditions.checkPositionIndex(index, 0);
/* 157 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends V> elements) {
/* 162 */       Preconditions.checkNotNull(elements);
/* 163 */       Preconditions.checkPositionIndex(index, 0);
/* 164 */       throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 169 */     protected List<V> delegate() { return Collections.emptyList(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   Iterator<Map.Entry<K, V>> entryIterator() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   Collection<Map.Entry<K, V>> createEntries() { return new Entries(); }
/*     */ 
/*     */   
/*     */   class Entries
/*     */     extends ForwardingCollection<Map.Entry<K, V>>
/*     */   {
/* 186 */     protected Collection<Map.Entry<K, V>> delegate() { return Collections2.filter(FilteredKeyMultimap.this.unfiltered.entries(), FilteredKeyMultimap.this.entryPredicate()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(@Nullable Object o) {
/* 192 */       if (o instanceof Map.Entry) {
/* 193 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 194 */         if (FilteredKeyMultimap.this.unfiltered.containsKey(entry.getKey()) && FilteredKeyMultimap.this.keyPredicate.apply(entry.getKey()))
/*     */         {
/*     */           
/* 197 */           return FilteredKeyMultimap.this.unfiltered.remove(entry.getKey(), entry.getValue());
/*     */         }
/*     */       } 
/* 200 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 206 */   Collection<V> createValues() { return new FilteredMultimapValues(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   Map<K, Collection<V>> createAsMap() { return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   Multiset<K> createKeys() { return Multisets.filter(this.unfiltered.keys(), this.keyPredicate); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredKeyMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */