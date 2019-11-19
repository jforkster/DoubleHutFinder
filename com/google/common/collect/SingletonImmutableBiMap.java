/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class SingletonImmutableBiMap<K, V>
/*    */   extends ImmutableBiMap<K, V>
/*    */ {
/*    */   final K singleKey;
/*    */   final V singleValue;
/*    */   ImmutableBiMap<V, K> inverse;
/*    */   
/*    */   SingletonImmutableBiMap(K singleKey, V singleValue) {
/* 39 */     CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
/* 40 */     this.singleKey = singleKey;
/* 41 */     this.singleValue = singleValue;
/*    */   }
/*    */ 
/*    */   
/*    */   private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
/* 46 */     this.singleKey = singleKey;
/* 47 */     this.singleValue = singleValue;
/* 48 */     this.inverse = inverse;
/*    */   }
/*    */ 
/*    */   
/* 52 */   SingletonImmutableBiMap(Map.Entry<? extends K, ? extends V> entry) { this(entry.getKey(), entry.getValue()); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public V get(@Nullable Object key) { return (V)(this.singleKey.equals(key) ? this.singleValue : null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public int size() { return 1; }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public boolean containsKey(@Nullable Object key) { return this.singleKey.equals(key); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public boolean containsValue(@Nullable Object value) { return this.singleValue.equals(value); }
/*    */ 
/*    */ 
/*    */   
/* 73 */   boolean isPartialView() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   ImmutableSet<Map.Entry<K, V>> createEntrySet() { return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   ImmutableSet<K> createKeySet() { return ImmutableSet.of(this.singleKey); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImmutableBiMap<V, K> inverse() {
/* 91 */     ImmutableBiMap<V, K> result = this.inverse;
/* 92 */     if (result == null) {
/* 93 */       return this.inverse = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this);
/*    */     }
/*    */     
/* 96 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SingletonImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */