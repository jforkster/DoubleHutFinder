/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @Beta
/*     */ public abstract class ForwardingCache<K, V>
/*     */   extends ForwardingObject
/*     */   implements Cache<K, V>
/*     */ {
/*     */   @Nullable
/*  54 */   public V getIfPresent(Object key) { return (V)delegate().getIfPresent(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException { return (V)delegate().get(key, valueLoader); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) { return delegate().getAllPresent(keys); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public void put(K key, V value) { delegate().put(key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public void putAll(Map<? extends K, ? extends V> m) { delegate().putAll(m); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void invalidate(Object key) { delegate().invalidate(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public void invalidateAll(Iterable<?> keys) { delegate().invalidateAll(keys); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public void invalidateAll() { delegate().invalidateAll(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public long size() { return delegate().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public CacheStats stats() { return delegate().stats(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public ConcurrentMap<K, V> asMap() { return delegate().asMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public void cleanUp() { delegate().cleanUp(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Cache<K, V> delegate();
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static abstract class SimpleForwardingCache<K, V>
/*     */     extends ForwardingCache<K, V>
/*     */   {
/*     */     private final Cache<K, V> delegate;
/*     */ 
/*     */     
/* 138 */     protected SimpleForwardingCache(Cache<K, V> delegate) { this.delegate = (Cache)Preconditions.checkNotNull(delegate); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     protected final Cache<K, V> delegate() { return this.delegate; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/ForwardingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */