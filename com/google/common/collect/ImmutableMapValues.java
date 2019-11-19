/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class ImmutableMapValues<K, V>
/*    */   extends ImmutableCollection<V>
/*    */ {
/*    */   private final ImmutableMap<K, V> map;
/*    */   
/* 38 */   ImmutableMapValues(ImmutableMap<K, V> map) { this.map = map; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int size() { return this.map.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public UnmodifiableIterator<V> iterator() { return Maps.valueIterator(this.map.entrySet().iterator()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean contains(@Nullable Object object) { return (object != null && Iterators.contains(iterator(), object)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   boolean isPartialView() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   ImmutableList<V> createAsList() {
/* 63 */     final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
/* 64 */     return new ImmutableAsList<V>()
/*    */       {
/*    */         public V get(int index) {
/* 67 */           return (V)((Map.Entry)entryList.get(index)).getValue();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 72 */         ImmutableCollection<V> delegateCollection() { return super.this$0; }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/* 79 */   Object writeReplace() { return new SerializedForm(this.map); }
/*    */   
/*    */   @GwtIncompatible("serialization")
/*    */   private static class SerializedForm<V> extends Object implements Serializable {
/*    */     final ImmutableMap<?, V> map;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/* 86 */     SerializedForm(ImmutableMap<?, V> map) { this.map = map; }
/*    */ 
/*    */     
/* 89 */     Object readResolve() { return this.map.values(); }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMapValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */