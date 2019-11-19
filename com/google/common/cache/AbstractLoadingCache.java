/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ @Beta
/*    */ public abstract class AbstractLoadingCache<K, V>
/*    */   extends AbstractCache<K, V>
/*    */   implements LoadingCache<K, V>
/*    */ {
/*    */   public V getUnchecked(K key) {
/*    */     try {
/* 53 */       return (V)get(key);
/* 54 */     } catch (ExecutionException e) {
/* 55 */       throw new UncheckedExecutionException(e.getCause());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 61 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 62 */     for (K key : keys) {
/* 63 */       if (!result.containsKey(key)) {
/* 64 */         result.put(key, get(key));
/*    */       }
/*    */     } 
/* 67 */     return ImmutableMap.copyOf(result);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public final V apply(K key) { return (V)getUnchecked(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public void refresh(K key) { throw new UnsupportedOperationException(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/AbstractLoadingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */