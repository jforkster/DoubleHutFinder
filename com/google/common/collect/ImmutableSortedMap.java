/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMapFauxverideShim<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*  65 */   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
/*     */   
/*  67 */   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap(NATURAL_ORDER);
/*     */   private ImmutableSortedMap<K, V> descendingMap;
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
/*  71 */     if (Ordering.natural().equals(comparator)) {
/*  72 */       return of();
/*     */     }
/*  74 */     return new EmptyImmutableSortedMap(comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(Comparator<? super K> comparator, int size, Entry[] entries) {
/*  82 */     if (size == 0) {
/*  83 */       return emptyMap(comparator);
/*     */     }
/*     */     
/*  86 */     ImmutableList.Builder<K> keyBuilder = ImmutableList.builder();
/*  87 */     ImmutableList.Builder<V> valueBuilder = ImmutableList.builder();
/*  88 */     for (int i = 0; i < size; i++) {
/*  89 */       Map.Entry<K, V> entry = entries[i];
/*  90 */       keyBuilder.add(entry.getKey());
/*  91 */       valueBuilder.add(entry.getValue());
/*     */     } 
/*     */     
/*  94 */     return new RegularImmutableSortedMap(new RegularImmutableSortedSet(keyBuilder.build(), comparator), valueBuilder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> from(ImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/* 101 */     if (keySet.isEmpty()) {
/* 102 */       return emptyMap(keySet.comparator());
/*     */     }
/* 104 */     return new RegularImmutableSortedMap((RegularImmutableSortedSet)keySet, valueList);
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
/* 117 */   public static <K, V> ImmutableSortedMap<K, V> of() { return NATURAL_EMPTY_MAP; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) { return from(ImmutableSortedSet.of(k1), ImmutableList.of(v1)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) { return fromEntries(Ordering.natural(), false, 2, new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) { return fromEntries(Ordering.natural(), false, 3, new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) { return fromEntries(Ordering.natural(), false, 4, new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) { return fromEntries(Ordering.natural(), false, 5, new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 205 */     Ordering<K> naturalOrder = Ordering.natural();
/* 206 */     return copyOfInternal(map, naturalOrder);
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
/* 223 */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) { return copyOfInternal(map, (Comparator)Preconditions.checkNotNull(comparator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
/* 239 */     Comparator<? super K> comparator = map.comparator();
/* 240 */     if (comparator == null)
/*     */     {
/*     */       
/* 243 */       comparator = NATURAL_ORDER;
/*     */     }
/* 245 */     return copyOfInternal(map, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 250 */     boolean sameComparator = false;
/* 251 */     if (map instanceof SortedMap) {
/* 252 */       SortedMap<?, ?> sortedMap = (SortedMap)map;
/* 253 */       Comparator<?> comparator2 = sortedMap.comparator();
/* 254 */       sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 259 */     if (sameComparator && map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 263 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 264 */       if (!kvMap.isPartialView()) {
/* 265 */         return kvMap;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     Entry[] entries = (Entry[])map.entrySet().toArray(new Map.Entry[0]);
/*     */     
/* 275 */     return fromEntries(comparator, sameComparator, entries.length, entries);
/*     */   }
/*     */ 
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, int size, Entry... entries) {
/* 280 */     for (int i = 0; i < size; i++) {
/* 281 */       Map.Entry<K, V> entry = entries[i];
/* 282 */       entries[i] = entryOf(entry.getKey(), entry.getValue());
/*     */     } 
/* 284 */     if (!sameComparator) {
/* 285 */       sortEntries(comparator, size, entries);
/* 286 */       validateEntries(size, entries, comparator);
/*     */     } 
/*     */     
/* 289 */     return fromSortedEntries(comparator, size, entries);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 294 */   private static <K, V> void sortEntries(Comparator<? super K> comparator, int size, Entry[] entries) { Arrays.sort(entries, 0, size, Ordering.from(comparator).onKeys()); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> void validateEntries(int size, Entry[] entries, Comparator<? super K> comparator) {
/* 299 */     for (int i = 1; i < size; i++) {
/* 300 */       checkNoConflict((comparator.compare(entries[i - 1].getKey(), entries[i].getKey()) != 0), "key", entries[i - 1], entries[i]);
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
/* 311 */   public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() { return new Builder(Ordering.natural()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 323 */   public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) { return new Builder(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() { return new Builder(Ordering.natural().reverse()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     private final Comparator<? super K> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 363 */     public Builder(Comparator<? super K> comparator) { this.comparator = (Comparator)Preconditions.checkNotNull(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 372 */       super.put(key, value);
/* 373 */       return this;
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
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 385 */       super.put(entry);
/* 386 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 397 */       super.putAll(map);
/* 398 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 408 */     public ImmutableSortedMap<K, V> build() { return ImmutableSortedMap.fromEntries(this.comparator, false, this.size, this.entries); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap() {}
/*     */ 
/*     */   
/* 416 */   ImmutableSortedMap(ImmutableSortedMap<K, V> descendingMap) { this.descendingMap = descendingMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 421 */   public int size() { return values().size(); }
/*     */ 
/*     */ 
/*     */   
/* 425 */   public boolean containsValue(@Nullable Object value) { return values().contains(value); }
/*     */ 
/*     */ 
/*     */   
/* 429 */   boolean isPartialView() { return (keySet().isPartialView() || values().isPartialView()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 437 */   public ImmutableSet<Map.Entry<K, V>> entrySet() { return super.entrySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 459 */   public Comparator<? super K> comparator() { return keySet().comparator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 464 */   public K firstKey() { return (K)keySet().first(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 469 */   public K lastKey() { return (K)keySet().last(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 484 */   public ImmutableSortedMap<K, V> headMap(K toKey) { return headMap(toKey, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 517 */   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 538 */     Preconditions.checkNotNull(fromKey);
/* 539 */     Preconditions.checkNotNull(toKey);
/* 540 */     Preconditions.checkArgument((comparator().compare(fromKey, toKey) <= 0), "expected fromKey <= toKey but %s > %s", new Object[] { fromKey, toKey });
/*     */     
/* 542 */     return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
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
/* 557 */   public ImmutableSortedMap<K, V> tailMap(K fromKey) { return tailMap(fromKey, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 578 */   public Map.Entry<K, V> lowerEntry(K key) { return headMap(key, false).lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 583 */   public K lowerKey(K key) { return (K)Maps.keyOrNull(lowerEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 588 */   public Map.Entry<K, V> floorEntry(K key) { return headMap(key, true).lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 593 */   public K floorKey(K key) { return (K)Maps.keyOrNull(floorEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 598 */   public Map.Entry<K, V> ceilingEntry(K key) { return tailMap(key, true).firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 603 */   public K ceilingKey(K key) { return (K)Maps.keyOrNull(ceilingEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 608 */   public Map.Entry<K, V> higherEntry(K key) { return tailMap(key, false).firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 613 */   public K higherKey(K key) { return (K)Maps.keyOrNull(higherEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 618 */   public Map.Entry<K, V> firstEntry() { return isEmpty() ? null : (Map.Entry)entrySet().asList().get(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 623 */   public Map.Entry<K, V> lastEntry() { return isEmpty() ? null : (Map.Entry)entrySet().asList().get(size() - 1); }
/*     */ 
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
/* 635 */   public final Map.Entry<K, V> pollFirstEntry() { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 647 */   public final Map.Entry<K, V> pollLastEntry() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> descendingMap() {
/* 654 */     ImmutableSortedMap<K, V> result = this.descendingMap;
/* 655 */     if (result == null) {
/* 656 */       result = this.descendingMap = createDescendingMap();
/*     */     }
/* 658 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 665 */   public ImmutableSortedSet<K> navigableKeySet() { return keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 670 */   public ImmutableSortedSet<K> descendingKeySet() { return keySet().descendingSet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private final Comparator<Object> comparator;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
/* 683 */       super(sortedMap);
/* 684 */       this.comparator = sortedMap.comparator();
/*     */     }
/*     */     Object readResolve() {
/* 687 */       ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<Object, Object>(this.comparator);
/* 688 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 694 */   Object writeReplace() { return new SerializedForm(this); }
/*     */   
/*     */   public abstract ImmutableSortedSet<K> keySet();
/*     */   
/*     */   public abstract ImmutableCollection<V> values();
/*     */   
/*     */   public abstract ImmutableSortedMap<K, V> headMap(K paramK, boolean paramBoolean);
/*     */   
/*     */   public abstract ImmutableSortedMap<K, V> tailMap(K paramK, boolean paramBoolean);
/*     */   
/*     */   abstract ImmutableSortedMap<K, V> createDescendingMap();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */