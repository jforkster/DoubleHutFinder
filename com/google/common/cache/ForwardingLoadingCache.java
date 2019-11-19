/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public abstract class ForwardingLoadingCache<K, V>
/*    */   extends ForwardingCache<K, V>
/*    */   implements LoadingCache<K, V>
/*    */ {
/* 48 */   public V get(K key) throws ExecutionException { return (V)delegate().get(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public V getUnchecked(K key) throws ExecutionException { return (V)delegate().getUnchecked(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException { return delegate().getAll(keys); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public V apply(K key) throws ExecutionException { return (V)delegate().apply(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public void refresh(K key) { delegate().refresh(key); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract LoadingCache<K, V> delegate();
/*    */ 
/*    */ 
/*    */   
/*    */   @Beta
/*    */   public static abstract class SimpleForwardingLoadingCache<K, V>
/*    */     extends ForwardingLoadingCache<K, V>
/*    */   {
/*    */     private final LoadingCache<K, V> delegate;
/*    */ 
/*    */     
/* 83 */     protected SimpleForwardingLoadingCache(LoadingCache<K, V> delegate) { this.delegate = (LoadingCache)Preconditions.checkNotNull(delegate); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 88 */     protected final LoadingCache<K, V> delegate() { return this.delegate; }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/ForwardingLoadingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */