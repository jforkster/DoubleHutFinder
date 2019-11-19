/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Predicate;
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
/*    */ @GwtCompatible
/*    */ final class FilteredKeySetMultimap<K, V>
/*    */   extends FilteredKeyMultimap<K, V>
/*    */   implements FilteredSetMultimap<K, V>
/*    */ {
/* 37 */   FilteredKeySetMultimap(SetMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate) { super(unfiltered, keyPredicate); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public SetMultimap<K, V> unfiltered() { return (SetMultimap)this.unfiltered; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Set<V> get(K key) { return (Set)super.get(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public Set<V> removeAll(Object key) { return (Set)super.removeAll(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public Set<V> replaceValues(K key, Iterable<? extends V> values) { return (Set)super.replaceValues(key, values); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public Set<Map.Entry<K, V>> entries() { return (Set)super.entries(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   Set<Map.Entry<K, V>> createEntries() { return new EntrySet(); }
/*    */   
/*    */   class EntrySet extends FilteredKeyMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> { EntrySet() {
/* 70 */       super(FilteredKeySetMultimap.this);
/*    */     }
/*    */     
/* 73 */     public int hashCode() { return Sets.hashCodeImpl(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 78 */     public boolean equals(@Nullable Object o) { return Sets.equalsImpl(this, o); } }
/*    */ 
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredKeySetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */