/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ class RegularImmutableMultiset<E>
/*    */   extends ImmutableMultiset<E>
/*    */ {
/*    */   private final ImmutableMap<E, Integer> map;
/*    */   private final int size;
/*    */   
/*    */   RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
/* 39 */     this.map = map;
/* 40 */     this.size = size;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   boolean isPartialView() { return this.map.isPartialView(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int count(@Nullable Object element) {
/* 50 */     Integer value = (Integer)this.map.get(element);
/* 51 */     return (value == null) ? 0 : value.intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public int size() { return this.size; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public boolean contains(@Nullable Object element) { return this.map.containsKey(element); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public ImmutableSet<E> elementSet() { return this.map.keySet(); }
/*    */ 
/*    */ 
/*    */   
/*    */   Multiset.Entry<E> getEntry(int index) {
/* 71 */     Map.Entry<E, Integer> mapEntry = (Map.Entry)this.map.entrySet().asList().get(index);
/* 72 */     return Multisets.immutableEntry(mapEntry.getKey(), ((Integer)mapEntry.getValue()).intValue());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public int hashCode() { return this.map.hashCode(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */