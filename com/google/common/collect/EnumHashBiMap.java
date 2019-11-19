/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumHashBiMap<K extends Enum<K>, V>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private Class<K> keyType;
/*     */   @GwtIncompatible("only needed in emulated source.")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  58 */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> keyType) { return new EnumHashBiMap(keyType); }
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
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> map) {
/*  73 */     EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyType(map));
/*  74 */     bimap.putAll(map);
/*  75 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumHashBiMap(Class<K> keyType) {
/*  79 */     super(WellBehavedMap.wrap(new EnumMap(keyType)), Maps.newHashMapWithExpectedSize((Enum[])keyType.getEnumConstants().length));
/*     */ 
/*     */ 
/*     */     
/*  83 */     this.keyType = keyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   K checkKey(K key) { return (K)(Enum)Preconditions.checkNotNull(key); }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public V put(K key, @Nullable V value) { return (V)super.put(key, value); }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public V forcePut(K key, @Nullable V value) { return (V)super.forcePut(key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public Class<K> keyType() { return this.keyType; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 112 */     stream.defaultWriteObject();
/* 113 */     stream.writeObject(this.keyType);
/* 114 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 121 */     stream.defaultReadObject();
/* 122 */     this.keyType = (Class)stream.readObject();
/* 123 */     setDelegates(WellBehavedMap.wrap(new EnumMap(this.keyType)), new HashMap((Enum[])this.keyType.getEnumConstants().length * 3 / 2));
/*     */     
/* 125 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EnumHashBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */