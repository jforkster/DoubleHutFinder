/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ForwardingNavigableMap<K, V>
/*     */   extends ForwardingSortedMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*  63 */   public Map.Entry<K, V> lowerEntry(K key) { return delegate().lowerEntry(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   protected Map.Entry<K, V> standardLowerEntry(K key) { return headMap(key, false).lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public K lowerKey(K key) { return (K)delegate().lowerKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected K standardLowerKey(K key) { return (K)Maps.keyOrNull(lowerEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Map.Entry<K, V> floorEntry(K key) { return delegate().floorEntry(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected Map.Entry<K, V> standardFloorEntry(K key) { return headMap(key, true).lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public K floorKey(K key) { return (K)delegate().floorKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   protected K standardFloorKey(K key) { return (K)Maps.keyOrNull(floorEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public Map.Entry<K, V> ceilingEntry(K key) { return delegate().ceilingEntry(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected Map.Entry<K, V> standardCeilingEntry(K key) { return tailMap(key, true).firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public K ceilingKey(K key) { return (K)delegate().ceilingKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected K standardCeilingKey(K key) { return (K)Maps.keyOrNull(ceilingEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public Map.Entry<K, V> higherEntry(K key) { return delegate().higherEntry(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   protected Map.Entry<K, V> standardHigherEntry(K key) { return tailMap(key, false).firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public K higherKey(K key) { return (K)delegate().higherKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   protected K standardHigherKey(K key) { return (K)Maps.keyOrNull(higherEntry(key)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public Map.Entry<K, V> firstEntry() { return delegate().firstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   protected Map.Entry<K, V> standardFirstEntry() { return (Map.Entry)Iterables.getFirst(entrySet(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardFirstKey() {
/* 193 */     Map.Entry<K, V> entry = firstEntry();
/* 194 */     if (entry == null) {
/* 195 */       throw new NoSuchElementException();
/*     */     }
/* 197 */     return (K)entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public Map.Entry<K, V> lastEntry() { return delegate().lastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   protected Map.Entry<K, V> standardLastEntry() { return (Map.Entry)Iterables.getFirst(descendingMap().entrySet(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected K standardLastKey() {
/* 220 */     Map.Entry<K, V> entry = lastEntry();
/* 221 */     if (entry == null) {
/* 222 */       throw new NoSuchElementException();
/*     */     }
/* 224 */     return (K)entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 230 */   public Map.Entry<K, V> pollFirstEntry() { return delegate().pollFirstEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   protected Map.Entry<K, V> standardPollFirstEntry() { return (Map.Entry)Iterators.pollNext(entrySet().iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 244 */   public Map.Entry<K, V> pollLastEntry() { return delegate().pollLastEntry(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   protected Map.Entry<K, V> standardPollLastEntry() { return (Map.Entry)Iterators.pollNext(descendingMap().entrySet().iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public NavigableMap<K, V> descendingMap() { return delegate().descendingMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   protected class StandardDescendingMap
/*     */     extends Maps.DescendingMap<K, V>
/*     */   {
/* 280 */     NavigableMap<K, V> forward() { return ForwardingNavigableMap.this; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Iterator<Map.Entry<K, V>> entryIterator() {
/* 285 */       return new Iterator<Map.Entry<K, V>>()
/*     */         {
/*     */           private Map.Entry<K, V> toRemove;
/*     */           
/*     */           private Map.Entry<K, V> nextOrNull;
/*     */           
/* 291 */           public boolean hasNext() { return (super.nextOrNull != null); }
/*     */ 
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 296 */             if (!super.hasNext()) {
/* 297 */               throw new NoSuchElementException();
/*     */             }
/*     */             try {
/* 300 */               return super.nextOrNull;
/*     */             } finally {
/* 302 */               super.toRemove = super.nextOrNull;
/* 303 */               super.nextOrNull = super.this$1.forward().lowerEntry(super.nextOrNull.getKey());
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 309 */             CollectPreconditions.checkRemove((super.toRemove != null));
/* 310 */             super.this$1.forward().remove(super.toRemove.getKey());
/* 311 */             super.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 319 */   public NavigableSet<K> navigableKeySet() { return delegate().navigableKeySet(); }
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
/*     */   protected class StandardNavigableKeySet
/*     */     extends Maps.NavigableKeySet<K, V>
/*     */   {
/*     */     public StandardNavigableKeySet() {
/* 334 */       super(ForwardingNavigableMap.this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 340 */   public NavigableSet<K> descendingKeySet() { return delegate().descendingKeySet(); }
/*     */ 
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
/* 352 */   protected NavigableSet<K> standardDescendingKeySet() { return descendingMap().navigableKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 363 */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 368 */   public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 373 */   public NavigableMap<K, V> headMap(K toKey, boolean inclusive) { return delegate().headMap(toKey, inclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 378 */   public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) { return delegate().tailMap(fromKey, inclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 387 */   protected SortedMap<K, V> standardHeadMap(K toKey) { return headMap(toKey, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 396 */   protected SortedMap<K, V> standardTailMap(K fromKey) { return tailMap(fromKey, true); }
/*     */   
/*     */   protected abstract NavigableMap<K, V> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingNavigableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */