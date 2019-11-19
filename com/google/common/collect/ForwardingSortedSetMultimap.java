/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSortedSetMultimap<K, V>
/*    */   extends ForwardingSetMultimap<K, V>
/*    */   implements SortedSetMultimap<K, V>
/*    */ {
/* 45 */   public SortedSet<V> get(@Nullable K key) { return delegate().get(key); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public SortedSet<V> removeAll(@Nullable Object key) { return delegate().removeAll(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) { return delegate().replaceValues(key, values); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public Comparator<? super V> valueComparator() { return delegate().valueComparator(); }
/*    */   
/*    */   protected abstract SortedSetMultimap<K, V> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingSortedSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */