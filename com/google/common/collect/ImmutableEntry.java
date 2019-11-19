/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ class ImmutableEntry<K, V>
/*    */   extends AbstractMapEntry<K, V>
/*    */   implements Serializable
/*    */ {
/*    */   final K key;
/*    */   final V value;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ImmutableEntry(@Nullable K key, @Nullable V value) {
/* 35 */     this.key = key;
/* 36 */     this.value = value;
/*    */   }
/*    */   
/*    */   @Nullable
/* 40 */   public final K getKey() { return (K)this.key; }
/*    */ 
/*    */   
/*    */   @Nullable
/* 44 */   public final V getValue() { return (V)this.value; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public final V setValue(V value) { throw new UnsupportedOperationException(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */