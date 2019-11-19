/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingSortedMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*  67 */   public Comparator<? super K> comparator() { return delegate().comparator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public K firstKey() { return (K)delegate().firstKey(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public SortedMap<K, V> headMap(K toKey) { return delegate().headMap(toKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public K lastKey() { return (K)delegate().lastKey(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public SortedMap<K, V> subMap(K fromKey, K toKey) { return delegate().subMap(fromKey, toKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public SortedMap<K, V> tailMap(K fromKey) { return delegate().tailMap(fromKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.SortedKeySet<K, V>
/*     */   {
/*     */     public StandardKeySet() {
/* 106 */       super(ForwardingSortedMap.this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int unsafeCompare(Object k1, Object k2) {
/* 113 */     Comparator<? super K> comparator = comparator();
/* 114 */     if (comparator == null) {
/* 115 */       return ((Comparable)k1).compareTo(k2);
/*     */     }
/* 117 */     return comparator.compare(k1, k2);
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
/*     */   @Beta
/*     */   protected boolean standardContainsKey(@Nullable Object key) {
/*     */     try {
/* 133 */       SortedMap<Object, V> self = this;
/* 134 */       Object ceilingKey = self.tailMap(key).firstKey();
/* 135 */       return (unsafeCompare(ceilingKey, key) == 0);
/* 136 */     } catch (ClassCastException e) {
/* 137 */       return false;
/* 138 */     } catch (NoSuchElementException e) {
/* 139 */       return false;
/* 140 */     } catch (NullPointerException e) {
/* 141 */       return false;
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
/*     */   @Beta
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
/* 154 */     Preconditions.checkArgument((unsafeCompare(fromKey, toKey) <= 0), "fromKey must be <= toKey");
/* 155 */     return tailMap(fromKey).headMap(toKey);
/*     */   }
/*     */   
/*     */   protected abstract SortedMap<K, V> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */