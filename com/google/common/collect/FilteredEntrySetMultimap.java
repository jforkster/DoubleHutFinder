/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ final class FilteredEntrySetMultimap<K, V>
/*    */   extends FilteredEntryMultimap<K, V>
/*    */   implements FilteredSetMultimap<K, V>
/*    */ {
/* 35 */   FilteredEntrySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) { super(unfiltered, predicate); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public SetMultimap<K, V> unfiltered() { return (SetMultimap)this.unfiltered; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public Set<V> get(K key) { return (Set)super.get(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public Set<V> removeAll(Object key) { return (Set)super.removeAll(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public Set<V> replaceValues(K key, Iterable<? extends V> values) { return (Set)super.replaceValues(key, values); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   Set<Map.Entry<K, V>> createEntries() { return Sets.filter(unfiltered().entries(), entryPredicate()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public Set<Map.Entry<K, V>> entries() { return (Set)super.entries(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredEntrySetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */