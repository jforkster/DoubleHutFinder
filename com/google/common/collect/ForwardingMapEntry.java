/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMapEntry<K, V>
/*     */   extends ForwardingObject
/*     */   implements Map.Entry<K, V>
/*     */ {
/*  66 */   public K getKey() { return (K)delegate().getKey(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public V getValue() { return (V)delegate().getValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public V setValue(V value) { return (V)delegate().setValue(value); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public boolean equals(@Nullable Object object) { return delegate().equals(object); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public int hashCode() { return delegate().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardEquals(@Nullable Object object) {
/*  96 */     if (object instanceof Map.Entry) {
/*  97 */       Map.Entry<?, ?> that = (Map.Entry)object;
/*  98 */       return (Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue()));
/*     */     } 
/*     */     
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 112 */     K k = (K)getKey();
/* 113 */     V v = (V)getValue();
/* 114 */     return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/* 126 */   protected String standardToString() { return getKey() + "=" + getValue(); }
/*     */   
/*     */   protected abstract Map.Entry<K, V> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */