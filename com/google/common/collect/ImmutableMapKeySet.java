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
/*    */ final class ImmutableMapKeySet<K, V>
/*    */   extends ImmutableSet<K>
/*    */ {
/*    */   private final ImmutableMap<K, V> map;
/*    */   
/* 38 */   ImmutableMapKeySet(ImmutableMap<K, V> map) { this.map = map; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public int size() { return this.map.size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public UnmodifiableIterator<K> iterator() { return asList().iterator(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean contains(@Nullable Object object) { return this.map.containsKey(object); }
/*    */ 
/*    */ 
/*    */   
/*    */   ImmutableList<K> createAsList() {
/* 58 */     final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
/* 59 */     return new ImmutableAsList<K>()
/*    */       {
/*    */         public K get(int index)
/*    */         {
/* 63 */           return (K)((Map.Entry)entryList.get(index)).getKey();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/* 68 */         ImmutableCollection<K> delegateCollection() { return super.this$0; }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   boolean isPartialView() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/* 81 */   Object writeReplace() { return new KeySetSerializedForm(this.map); }
/*    */   
/*    */   @GwtIncompatible("serialization")
/*    */   private static class KeySetSerializedForm<K> extends Object implements Serializable {
/*    */     final ImmutableMap<K, ?> map;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/* 88 */     KeySetSerializedForm(ImmutableMap<K, ?> map) { this.map = map; }
/*    */ 
/*    */     
/* 91 */     Object readResolve() { return this.map.keySet(); }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMapKeySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */