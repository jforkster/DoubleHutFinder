/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
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
/*    */ @GwtCompatible
/*    */ final class FilteredKeyListMultimap<K, V>
/*    */   extends FilteredKeyMultimap<K, V>
/*    */   implements ListMultimap<K, V>
/*    */ {
/* 35 */   FilteredKeyListMultimap(ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) { super(unfiltered, keyPredicate); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public ListMultimap<K, V> unfiltered() { return (ListMultimap)super.unfiltered(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public List<V> get(K key) { return (List)super.get(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public List<V> removeAll(@Nullable Object key) { return (List)super.removeAll(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public List<V> replaceValues(K key, Iterable<? extends V> values) { return (List)super.replaceValues(key, values); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredKeyListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */