/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMap<K, V>
/*     */ {
/*     */   private final RegularImmutableSortedSet<K> keySet;
/*     */   private final ImmutableList<V> valueList;
/*     */   
/*     */   RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/*  36 */     this.keySet = keySet;
/*  37 */     this.valueList = valueList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
/*  44 */     super(descendingMap);
/*  45 */     this.keySet = keySet;
/*  46 */     this.valueList = valueList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  51 */   ImmutableSet<Map.Entry<K, V>> createEntrySet() { return new EntrySet(null); }
/*     */   
/*     */   private class EntrySet
/*     */     extends ImmutableMapEntrySet<K, V> {
/*     */     private EntrySet() {}
/*     */     
/*  57 */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() { return asList().iterator(); }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/*  62 */       return new ImmutableAsList<Map.Entry<K, V>>()
/*     */         {
/*     */           private final ImmutableList<K> keyList;
/*     */ 
/*     */ 
/*     */           
/*  68 */           public Map.Entry<K, V> get(int index) { return Maps.immutableEntry(super.keyList.get(index), RegularImmutableSortedMap.EntrySet.this.this$0.valueList.get(index)); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  73 */           ImmutableCollection<Map.Entry<K, V>> delegateCollection() { return super.this$1; }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     ImmutableMap<K, V> map() { return RegularImmutableSortedMap.this; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public ImmutableSortedSet<K> keySet() { return this.keySet; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public ImmutableCollection<V> values() { return this.valueList; }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/*  96 */     int index = this.keySet.indexOf(key);
/*  97 */     return (V)((index == -1) ? null : this.valueList.get(index));
/*     */   }
/*     */   
/*     */   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
/* 101 */     if (fromIndex == 0 && toIndex == size())
/* 102 */       return this; 
/* 103 */     if (fromIndex == toIndex) {
/* 104 */       return emptyMap(comparator());
/*     */     }
/* 106 */     return from(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) { return getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) { return getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), size()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   ImmutableSortedMap<K, V> createDescendingMap() { return new RegularImmutableSortedMap((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */