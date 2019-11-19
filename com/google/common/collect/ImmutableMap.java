/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMap<K, V>
/*     */   extends Object
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*  70 */   public static <K, V> ImmutableMap<K, V> of() { return ImmutableBiMap.of(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) { return ImmutableBiMap.of(k1, v1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) { return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) { return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) { return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) { return new RegularImmutableMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableMapEntry.TerminalEntry<K, V> entryOf(K key, V value) {
/* 135 */     CollectPreconditions.checkEntryNotNull(key, value);
/* 136 */     return new ImmutableMapEntry.TerminalEntry(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static <K, V> Builder<K, V> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/*     */   static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
/* 149 */     if (!safe) {
/* 150 */       throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
/*     */     }
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
/*     */   public static class Builder<K, V>
/*     */     extends Object
/*     */   {
/*     */     ImmutableMapEntry.TerminalEntry<K, V>[] entries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     public Builder() { this(4); }
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(int initialCapacity) {
/* 189 */       this.entries = new ImmutableMapEntry.TerminalEntry[initialCapacity];
/* 190 */       this.size = 0;
/*     */     }
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 194 */       if (minCapacity > this.entries.length) {
/* 195 */         this.entries = (TerminalEntry[])ObjectArrays.arraysCopyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 205 */       ensureCapacity(this.size + 1);
/* 206 */       ImmutableMapEntry.TerminalEntry<K, V> entry = ImmutableMap.entryOf(key, value);
/*     */       
/* 208 */       this.entries[this.size++] = entry;
/* 209 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) { return put(entry.getKey(), entry.getValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 230 */       ensureCapacity(this.size + map.size());
/* 231 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 232 */         put(entry);
/*     */       }
/* 234 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMap<K, V> build() {
/* 248 */       switch (this.size) {
/*     */         case 0:
/* 250 */           return ImmutableMap.of();
/*     */         case 1:
/* 252 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 254 */       return new RegularImmutableMap(this.size, this.entries);
/*     */     }
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/*     */     Map.Entry<K, V> onlyEntry;
/* 273 */     if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 278 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/* 279 */       if (!kvMap.isPartialView()) {
/* 280 */         return kvMap;
/*     */       }
/* 282 */     } else if (map instanceof EnumMap) {
/* 283 */       return copyOfEnumMapUnsafe(map);
/*     */     } 
/* 285 */     Entry[] entries = (Entry[])map.entrySet().toArray(EMPTY_ENTRY_ARRAY);
/* 286 */     switch (entries.length) {
/*     */       case 0:
/* 288 */         return of();
/*     */       
/*     */       case 1:
/* 291 */         onlyEntry = entries[0];
/* 292 */         return of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */     } 
/* 294 */     return new RegularImmutableMap(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 301 */   private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> map) { return copyOfEnumMap((EnumMap)map); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(Map<K, ? extends V> original) {
/* 306 */     EnumMap<K, V> copy = new EnumMap<K, V>(original);
/* 307 */     for (Map.Entry<?, ?> entry : copy.entrySet()) {
/* 308 */       CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
/*     */     }
/* 310 */     return ImmutableEnumMap.asImmutable(copy);
/*     */   }
/*     */   
/* 313 */   private static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Map.Entry[0];
/*     */ 
/*     */   
/*     */   private ImmutableSet<Map.Entry<K, V>> entrySet;
/*     */   
/*     */   private ImmutableSet<K> keySet;
/*     */   
/*     */   private ImmutableCollection<V> values;
/*     */   
/*     */   private ImmutableSetMultimap<K, V> multimapView;
/*     */ 
/*     */   
/*     */   @Deprecated
/* 326 */   public final V put(K k, V v) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 338 */   public final V remove(Object o) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 350 */   public final void putAll(Map<? extends K, ? extends V> map) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 362 */   public final void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 367 */   public boolean isEmpty() { return (size() == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 372 */   public boolean containsKey(@Nullable Object key) { return (get(key) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 377 */   public boolean containsValue(@Nullable Object value) { return values().contains(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 392 */     ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
/* 393 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
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
/*     */   public ImmutableSet<K> keySet() {
/* 406 */     ImmutableSet<K> result = this.keySet;
/* 407 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */ 
/*     */   
/* 411 */   ImmutableSet<K> createKeySet() { return new ImmutableMapKeySet(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 422 */     ImmutableCollection<V> result = this.values;
/* 423 */     return (result == null) ? (this.values = new ImmutableMapValues(this)) : result;
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
/*     */   @Beta
/*     */   public ImmutableSetMultimap<K, V> asMultimap() {
/* 436 */     ImmutableSetMultimap<K, V> result = this.multimapView;
/* 437 */     return (result == null) ? (this.multimapView = createMultimapView()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableSetMultimap<K, V> createMultimapView() {
/* 441 */     ImmutableMap<K, ImmutableSet<V>> map = viewMapValuesAsSingletonSets();
/* 442 */     return new ImmutableSetMultimap(map, map.size(), null);
/*     */   }
/*     */ 
/*     */   
/* 446 */   private ImmutableMap<K, ImmutableSet<V>> viewMapValuesAsSingletonSets() { return new MapViewOfValuesAsSingletonSets(this); }
/*     */ 
/*     */   
/*     */   private static final class MapViewOfValuesAsSingletonSets<K, V>
/*     */     extends ImmutableMap<K, ImmutableSet<V>>
/*     */   {
/*     */     private final ImmutableMap<K, V> delegate;
/*     */     
/* 454 */     MapViewOfValuesAsSingletonSets(ImmutableMap<K, V> delegate) { this.delegate = (ImmutableMap)Preconditions.checkNotNull(delegate); }
/*     */ 
/*     */ 
/*     */     
/* 458 */     public int size() { return this.delegate.size(); }
/*     */ 
/*     */ 
/*     */     
/* 462 */     public boolean containsKey(@Nullable Object key) { return this.delegate.containsKey(key); }
/*     */ 
/*     */     
/*     */     public ImmutableSet<V> get(@Nullable Object key) {
/* 466 */       V outerValue = (V)this.delegate.get(key);
/* 467 */       return (outerValue == null) ? null : ImmutableSet.of(outerValue);
/*     */     }
/*     */ 
/*     */     
/* 471 */     boolean isPartialView() { return false; }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<K, ImmutableSet<V>>> createEntrySet() {
/* 475 */       return new ImmutableMapEntrySet<K, ImmutableSet<V>>()
/*     */         {
/* 477 */           ImmutableMap<K, ImmutableSet<V>> map() { return super.this$0; }
/*     */ 
/*     */ 
/*     */           
/*     */           public UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> iterator() {
/* 482 */             final Iterator<Map.Entry<K, V>> backingIterator = super.this$0.delegate.entrySet().iterator();
/* 483 */             return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>()
/*     */               {
/* 485 */                 public boolean hasNext() { return backingIterator.hasNext(); }
/*     */ 
/*     */                 
/*     */                 public Map.Entry<K, ImmutableSet<V>> next() {
/* 489 */                   final Map.Entry<K, V> backingEntry = (Map.Entry)backingIterator.next();
/* 490 */                   return new AbstractMapEntry<K, ImmutableSet<V>>()
/*     */                     {
/* 492 */                       public K getKey() { return (K)backingEntry.getKey(); }
/*     */ 
/*     */ 
/*     */                       
/* 496 */                       public ImmutableSet<V> getValue() { return ImmutableSet.of(backingEntry.getValue()); }
/*     */                     };
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 507 */   public boolean equals(@Nullable Object object) { return Maps.equalsImpl(this, object); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 515 */   public int hashCode() { return entrySet().hashCode(); }
/*     */ 
/*     */ 
/*     */   
/* 519 */   public String toString() { return Maps.toStringImpl(this); }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] keys;
/*     */     
/*     */     private final Object[] values;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, ?> map) {
/* 531 */       this.keys = new Object[map.size()];
/* 532 */       this.values = new Object[map.size()];
/* 533 */       int i = 0;
/* 534 */       for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 535 */         this.keys[i] = entry.getKey();
/* 536 */         this.values[i] = entry.getValue();
/* 537 */         i++;
/*     */       } 
/*     */     }
/*     */     Object readResolve() {
/* 541 */       ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<Object, Object>();
/* 542 */       return createMap(builder);
/*     */     }
/*     */     Object createMap(ImmutableMap.Builder<Object, Object> builder) {
/* 545 */       for (int i = 0; i < this.keys.length; i++) {
/* 546 */         builder.put(this.keys[i], this.values[i]);
/*     */       }
/* 548 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 554 */   Object writeReplace() { return new SerializedForm(this); }
/*     */   
/*     */   public abstract V get(@Nullable Object paramObject);
/*     */   
/*     */   abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
/*     */   
/*     */   abstract boolean isPartialView();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */