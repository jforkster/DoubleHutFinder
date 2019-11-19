/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class RemovalNotification<K, V>
/*    */   extends Object
/*    */   implements Map.Entry<K, V>
/*    */ {
/*    */   @Nullable
/*    */   private final K key;
/*    */   @Nullable
/*    */   private final V value;
/*    */   private final RemovalCause cause;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
/* 48 */     this.key = key;
/* 49 */     this.value = value;
/* 50 */     this.cause = (RemovalCause)Preconditions.checkNotNull(cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public RemovalCause getCause() { return this.cause; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public boolean wasEvicted() { return this.cause.wasEvicted(); }
/*    */ 
/*    */   
/*    */   @Nullable
/* 69 */   public K getKey() { return (K)this.key; }
/*    */ 
/*    */   
/*    */   @Nullable
/* 73 */   public V getValue() { return (V)this.value; }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public final V setValue(V value) { throw new UnsupportedOperationException(); }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 81 */     if (object instanceof Map.Entry) {
/* 82 */       Map.Entry<?, ?> that = (Map.Entry)object;
/* 83 */       return (Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue()));
/*    */     } 
/*    */     
/* 86 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 90 */     K k = (K)getKey();
/* 91 */     V v = (V)getValue();
/* 92 */     return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 99 */   public String toString() { return getKey() + "=" + getValue(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/RemovalNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */