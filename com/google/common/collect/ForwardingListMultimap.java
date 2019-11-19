/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingListMultimap<K, V>
/*    */   extends ForwardingMultimap<K, V>
/*    */   implements ListMultimap<K, V>
/*    */ {
/* 44 */   public List<V> get(@Nullable K key) { return delegate().get(key); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public List<V> removeAll(@Nullable Object key) { return delegate().removeAll(key); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public List<V> replaceValues(K key, Iterable<? extends V> values) { return delegate().replaceValues(key, values); }
/*    */   
/*    */   protected abstract ListMultimap<K, V> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */