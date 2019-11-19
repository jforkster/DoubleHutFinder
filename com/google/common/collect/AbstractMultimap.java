/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultimap<K, V>
/*     */   extends Object
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   private Collection<Map.Entry<K, V>> entries;
/*     */   private Set<K> keySet;
/*     */   private Multiset<K> keys;
/*     */   private Collection<V> values;
/*     */   private Map<K, Collection<V>> asMap;
/*     */   
/*  41 */   public boolean isEmpty() { return (size() == 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/*  46 */     for (Collection<V> collection : asMap().values()) {
/*  47 */       if (collection.contains(value)) {
/*  48 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  52 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/*  57 */     Collection<V> collection = (Collection)asMap().get(key);
/*  58 */     return (collection != null && collection.contains(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/*  63 */     Collection<V> collection = (Collection)asMap().get(key);
/*  64 */     return (collection != null && collection.remove(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public boolean put(@Nullable K key, @Nullable V value) { return get(key).add(value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
/*  74 */     Preconditions.checkNotNull(values);
/*     */ 
/*     */     
/*  77 */     if (values instanceof Collection) {
/*  78 */       Collection<? extends V> valueCollection = (Collection)values;
/*  79 */       return (!valueCollection.isEmpty() && get(key).addAll(valueCollection));
/*     */     } 
/*  81 */     Iterator<? extends V> valueItr = values.iterator();
/*  82 */     return (valueItr.hasNext() && Iterators.addAll(get(key), valueItr));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  88 */     boolean changed = false;
/*  89 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/*  90 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/*  92 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/*  97 */     Preconditions.checkNotNull(values);
/*  98 */     Collection<V> result = removeAll(key);
/*  99 */     putAll(key, values);
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 107 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 108 */     return (result == null) ? (this.entries = createEntries()) : result;
/*     */   }
/*     */   
/*     */   Collection<Map.Entry<K, V>> createEntries() {
/* 112 */     if (this instanceof SetMultimap) {
/* 113 */       return new EntrySet(null);
/*     */     }
/* 115 */     return new Entries(null);
/*     */   }
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> entryIterator();
/*     */   
/*     */   private class Entries
/*     */     extends Multimaps.Entries<K, V> {
/* 122 */     Multimap<K, V> multimap() { return AbstractMultimap.this; }
/*     */ 
/*     */     
/*     */     private Entries() {}
/*     */     
/* 127 */     public Iterator<Map.Entry<K, V>> iterator() { return AbstractMultimap.this.entryIterator(); }
/*     */   }
/*     */   
/*     */   private class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
/* 131 */     private EntrySet() { super(AbstractMultimap.this, null); }
/*     */ 
/*     */     
/* 134 */     public int hashCode() { return Sets.hashCodeImpl(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     public boolean equals(@Nullable Object obj) { return Sets.equalsImpl(this, obj); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 149 */     Set<K> result = this.keySet;
/* 150 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */ 
/*     */   
/* 154 */   Set<K> createKeySet() { return new Maps.KeySet(asMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/* 161 */     Multiset<K> result = this.keys;
/* 162 */     return (result == null) ? (this.keys = createKeys()) : result;
/*     */   }
/*     */ 
/*     */   
/* 166 */   Multiset<K> createKeys() { return new Multimaps.Keys(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 173 */     Collection<V> result = this.values;
/* 174 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */ 
/*     */   
/* 178 */   Collection<V> createValues() { return new Values(); }
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/* 183 */     public Iterator<V> iterator() { return AbstractMultimap.this.valueIterator(); }
/*     */ 
/*     */ 
/*     */     
/* 187 */     public int size() { return AbstractMultimap.this.size(); }
/*     */ 
/*     */ 
/*     */     
/* 191 */     public boolean contains(@Nullable Object o) { return AbstractMultimap.this.containsValue(o); }
/*     */ 
/*     */ 
/*     */     
/* 195 */     public void clear() { AbstractMultimap.this.clear(); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 200 */   Iterator<V> valueIterator() { return Maps.valueIterator(entries().iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 207 */     Map<K, Collection<V>> result = this.asMap;
/* 208 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Map<K, Collection<V>> createAsMap();
/*     */ 
/*     */   
/* 216 */   public boolean equals(@Nullable Object object) { return Multimaps.equalsImpl(this, object); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public int hashCode() { return asMap().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public String toString() { return asMap().toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */