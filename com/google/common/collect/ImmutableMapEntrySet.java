/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated = true)
/*    */ abstract class ImmutableMapEntrySet<K, V>
/*    */   extends ImmutableSet<Map.Entry<K, V>>
/*    */ {
/*    */   abstract ImmutableMap<K, V> map();
/*    */   
/* 41 */   public int size() { return map().size(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(@Nullable Object object) {
/* 46 */     if (object instanceof Map.Entry) {
/* 47 */       Map.Entry<?, ?> entry = (Map.Entry)object;
/* 48 */       V value = (V)map().get(entry.getKey());
/* 49 */       return (value != null && value.equals(entry.getValue()));
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 56 */   boolean isPartialView() { return map().isPartialView(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/* 62 */   Object writeReplace() { return new EntrySetSerializedForm(map()); }
/*    */   
/*    */   @GwtIncompatible("serialization")
/*    */   private static class EntrySetSerializedForm<K, V> extends Object implements Serializable {
/*    */     final ImmutableMap<K, V> map;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/* 69 */     EntrySetSerializedForm(ImmutableMap<K, V> map) { this.map = map; }
/*    */ 
/*    */     
/* 72 */     Object readResolve() { return this.map.entrySet(); }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMapEntrySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */