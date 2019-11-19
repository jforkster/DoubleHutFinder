/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingConcurrentMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */   implements ConcurrentMap<K, V>
/*    */ {
/* 43 */   public V putIfAbsent(K key, V value) { return (V)delegate().putIfAbsent(key, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public boolean remove(Object key, Object value) { return delegate().remove(key, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public V replace(K key, V value) { return (V)delegate().replace(key, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public boolean replace(K key, V oldValue, V newValue) { return delegate().replace(key, oldValue, newValue); }
/*    */   
/*    */   protected abstract ConcurrentMap<K, V> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingConcurrentMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */