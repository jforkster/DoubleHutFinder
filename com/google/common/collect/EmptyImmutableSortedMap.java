/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ final class EmptyImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMap<K, V>
/*     */ {
/*     */   private final ImmutableSortedSet<K> keySet;
/*     */   
/*  37 */   EmptyImmutableSortedMap(Comparator<? super K> comparator) { this.keySet = ImmutableSortedSet.emptySet(comparator); }
/*     */ 
/*     */ 
/*     */   
/*     */   EmptyImmutableSortedMap(Comparator<? super K> comparator, ImmutableSortedMap<K, V> descendingMap) {
/*  42 */     super(descendingMap);
/*  43 */     this.keySet = ImmutableSortedSet.emptySet(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public V get(@Nullable Object key) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public ImmutableSortedSet<K> keySet() { return this.keySet; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public int size() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public boolean isEmpty() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public ImmutableCollection<V> values() { return ImmutableList.of(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public String toString() { return "{}"; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public ImmutableSet<Map.Entry<K, V>> entrySet() { return ImmutableSet.of(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   ImmutableSet<Map.Entry<K, V>> createEntrySet() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public ImmutableSetMultimap<K, V> asMultimap() { return ImmutableSetMultimap.of(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/*  98 */     Preconditions.checkNotNull(toKey);
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 104 */     Preconditions.checkNotNull(fromKey);
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   ImmutableSortedMap<K, V> createDescendingMap() { return new EmptyImmutableSortedMap(Ordering.from(comparator()).reverse(), this); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */