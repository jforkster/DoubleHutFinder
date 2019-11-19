/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private Class<K> keyType;
/*     */   private Class<V> valueType;
/*     */   @GwtIncompatible("not needed in emulated source.")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  58 */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType) { return new EnumBiMap(keyType, valueType); }
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
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map) {
/*  73 */     EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
/*  74 */     bimap.putAll(map);
/*  75 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumBiMap(Class<K> keyType, Class<V> valueType) {
/*  79 */     super(WellBehavedMap.wrap(new EnumMap(keyType)), WellBehavedMap.wrap(new EnumMap(valueType)));
/*     */     
/*  81 */     this.keyType = keyType;
/*  82 */     this.valueType = valueType;
/*     */   }
/*     */   
/*     */   static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
/*  86 */     if (map instanceof EnumBiMap) {
/*  87 */       return ((EnumBiMap)map).keyType();
/*     */     }
/*  89 */     if (map instanceof EnumHashBiMap) {
/*  90 */       return ((EnumHashBiMap)map).keyType();
/*     */     }
/*  92 */     Preconditions.checkArgument(!map.isEmpty());
/*  93 */     return ((Enum)map.keySet().iterator().next()).getDeclaringClass();
/*     */   }
/*     */   
/*     */   private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
/*  97 */     if (map instanceof EnumBiMap) {
/*  98 */       return ((EnumBiMap)map).valueType;
/*     */     }
/* 100 */     Preconditions.checkArgument(!map.isEmpty());
/* 101 */     return ((Enum)map.values().iterator().next()).getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public Class<K> keyType() { return this.keyType; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public Class<V> valueType() { return this.valueType; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   K checkKey(K key) { return (K)(Enum)Preconditions.checkNotNull(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   V checkValue(V value) { return (V)(Enum)Preconditions.checkNotNull(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 130 */     stream.defaultWriteObject();
/* 131 */     stream.writeObject(this.keyType);
/* 132 */     stream.writeObject(this.valueType);
/* 133 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 140 */     stream.defaultReadObject();
/* 141 */     this.keyType = (Class)stream.readObject();
/* 142 */     this.valueType = (Class)stream.readObject();
/* 143 */     setDelegates(WellBehavedMap.wrap(new EnumMap(this.keyType)), WellBehavedMap.wrap(new EnumMap(this.valueType)));
/*     */ 
/*     */     
/* 146 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EnumBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */