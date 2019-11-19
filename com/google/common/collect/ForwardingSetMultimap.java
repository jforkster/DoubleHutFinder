/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
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
/*    */ public abstract class ForwardingSetMultimap<K, V>
/*    */   extends ForwardingMultimap<K, V>
/*    */   implements SetMultimap<K, V>
/*    */ {
/* 42 */   public Set<Map.Entry<K, V>> entries() { return delegate().entries(); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public Set<V> get(@Nullable K key) { return delegate().get(key); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public Set<V> removeAll(@Nullable Object key) { return delegate().removeAll(key); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Set<V> replaceValues(K key, Iterable<? extends V> values) { return delegate().replaceValues(key, values); }
/*    */   
/*    */   protected abstract SetMultimap<K, V> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */