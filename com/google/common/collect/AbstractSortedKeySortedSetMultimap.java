/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.SortedMap;
/*    */ import java.util.SortedSet;
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
/*    */ abstract class AbstractSortedKeySortedSetMultimap<K, V>
/*    */   extends AbstractSortedSetMultimap<K, V>
/*    */ {
/* 38 */   AbstractSortedKeySortedSetMultimap(SortedMap<K, Collection<V>> map) { super(map); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public SortedMap<K, Collection<V>> asMap() { return (SortedMap)super.asMap(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   SortedMap<K, Collection<V>> backingMap() { return (SortedMap)super.backingMap(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public SortedSet<K> keySet() { return (SortedSet)super.keySet(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractSortedKeySortedSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */