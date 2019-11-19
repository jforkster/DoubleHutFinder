/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
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
/*    */ final class FilteredMultimapValues<K, V>
/*    */   extends AbstractCollection<V>
/*    */ {
/*    */   private final FilteredMultimap<K, V> multimap;
/*    */   
/* 42 */   FilteredMultimapValues(FilteredMultimap<K, V> multimap) { this.multimap = (FilteredMultimap)Preconditions.checkNotNull(multimap); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Iterator<V> iterator() { return Maps.valueIterator(this.multimap.entries().iterator()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean contains(@Nullable Object o) { return this.multimap.containsValue(o); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int size() { return this.multimap.size(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean remove(@Nullable Object o) {
/* 62 */     Predicate<? super Map.Entry<K, V>> entryPredicate = this.multimap.entryPredicate();
/* 63 */     Iterator<Map.Entry<K, V>> unfilteredItr = this.multimap.unfiltered().entries().iterator();
/* 64 */     while (unfilteredItr.hasNext()) {
/* 65 */       Map.Entry<K, V> entry = (Map.Entry)unfilteredItr.next();
/* 66 */       if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 67 */         unfilteredItr.remove();
/* 68 */         return true;
/*    */       } 
/*    */     } 
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public boolean removeAll(Collection<?> c) { return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c)))); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   public boolean retainAll(Collection<?> c) { return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))))); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 92 */   public void clear() { this.multimap.clear(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FilteredMultimapValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */