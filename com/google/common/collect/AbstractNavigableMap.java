/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
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
/*     */ abstract class AbstractNavigableMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   @Nullable
/*     */   public abstract V get(@Nullable Object paramObject);
/*     */   
/*     */   @Nullable
/*  44 */   public Map.Entry<K, V> firstEntry() { return (Map.Entry)Iterators.getNext(entryIterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  50 */   public Map.Entry<K, V> lastEntry() { return (Map.Entry)Iterators.getNext(descendingEntryIterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  56 */   public Map.Entry<K, V> pollFirstEntry() { return (Map.Entry)Iterators.pollNext(entryIterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  62 */   public Map.Entry<K, V> pollLastEntry() { return (Map.Entry)Iterators.pollNext(descendingEntryIterator()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public K firstKey() {
/*  67 */     Map.Entry<K, V> entry = firstEntry();
/*  68 */     if (entry == null) {
/*  69 */       throw new NoSuchElementException();
/*     */     }
/*  71 */     return (K)entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/*  77 */     Map.Entry<K, V> entry = lastEntry();
/*  78 */     if (entry == null) {
/*  79 */       throw new NoSuchElementException();
/*     */     }
/*  81 */     return (K)entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  88 */   public Map.Entry<K, V> lowerEntry(K key) { return headMap(key, false).lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  94 */   public Map.Entry<K, V> floorEntry(K key) { return headMap(key, true).lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/* 100 */   public Map.Entry<K, V> ceilingEntry(K key) { return tailMap(key, true).firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/* 106 */   public Map.Entry<K, V> higherEntry(K key) { return tailMap(key, false).firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public K lowerKey(K key) { return (K)Maps.keyOrNull(lowerEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public K floorKey(K key) { return (K)Maps.keyOrNull(floorEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public K ceilingKey(K key) { return (K)Maps.keyOrNull(ceilingEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public K higherKey(K key) { return (K)Maps.keyOrNull(higherEntry(key)); }
/*     */ 
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> entryIterator();
/*     */ 
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
/*     */ 
/*     */   
/* 135 */   public SortedMap<K, V> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public SortedMap<K, V> headMap(K toKey) { return headMap(toKey, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public SortedMap<K, V> tailMap(K fromKey) { return tailMap(fromKey, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   public NavigableSet<K> navigableKeySet() { return new Maps.NavigableKeySet(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public Set<K> keySet() { return navigableKeySet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int size();
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 163 */     return new Maps.EntrySet<K, V>()
/*     */       {
/*     */         Map<K, V> map() {
/* 166 */           return super.this$0;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 171 */         public Iterator<Map.Entry<K, V>> iterator() { return super.this$0.entryIterator(); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public NavigableSet<K> descendingKeySet() { return descendingMap().navigableKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public NavigableMap<K, V> descendingMap() { return new DescendingMap(null); }
/*     */   
/*     */   private final class DescendingMap
/*     */     extends Maps.DescendingMap<K, V> {
/*     */     private DescendingMap() {}
/*     */     
/* 189 */     NavigableMap<K, V> forward() { return AbstractNavigableMap.this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     Iterator<Map.Entry<K, V>> entryIterator() { return AbstractNavigableMap.this.descendingEntryIterator(); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractNavigableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */