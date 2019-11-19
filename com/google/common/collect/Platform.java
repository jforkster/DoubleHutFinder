/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Predicate;
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.NavigableMap;
/*    */ import java.util.NavigableSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ final class Platform
/*    */ {
/*    */   static <T> T[] newArray(T[] reference, int length) {
/* 48 */     Class<?> type = reference.getClass().getComponentType();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     return (T[])(Object[])Array.newInstance(type, length);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   static <E> Set<E> newSetFromMap(Map<E, Boolean> map) { return Collections.newSetFromMap(map); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   static MapMaker tryWeakKeys(MapMaker mapMaker) { return mapMaker.weakKeys(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   static <K, V1, V2> SortedMap<K, V2> mapsTransformEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) { return (fromMap instanceof NavigableMap) ? Maps.transformEntries((NavigableMap)fromMap, transformer) : Maps.transformEntriesIgnoreNavigable(fromMap, transformer); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 81 */   static <K, V> SortedMap<K, V> mapsAsMapSortedSet(SortedSet<K> set, Function<? super K, V> function) { return (set instanceof NavigableSet) ? Maps.asMap((NavigableSet)set, function) : Maps.asMapSortedIgnoreNavigable(set, function); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   static <E> SortedSet<E> setsFilterSortedSet(SortedSet<E> set, Predicate<? super E> predicate) { return (set instanceof NavigableSet) ? Sets.filter((NavigableSet)set, predicate) : Sets.filterSortedIgnoreNavigable(set, predicate); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 95 */   static <K, V> SortedMap<K, V> mapsFilterSortedMap(SortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> predicate) { return (map instanceof NavigableMap) ? Maps.filterEntries((NavigableMap)map, predicate) : Maps.filterSortedIgnoreNavigable(map, predicate); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */