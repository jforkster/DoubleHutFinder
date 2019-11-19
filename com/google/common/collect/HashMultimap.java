/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class HashMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 2;
/*     */   @VisibleForTesting
/*  53 */   int expectedValuesPerKey = 2;
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Not needed in emulated source")
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static <K, V> HashMultimap<K, V> create() { return new HashMultimap(); }
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
/*  75 */   public static <K, V> HashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) { return new HashMultimap(expectedKeys, expectedValuesPerKey); }
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
/*  87 */   public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) { return new HashMultimap(multimap); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   private HashMultimap() { super(new HashMap()); }
/*     */ 
/*     */   
/*     */   private HashMultimap(int expectedKeys, int expectedValuesPerKey) {
/*  95 */     super(Maps.newHashMapWithExpectedSize(expectedKeys));
/*  96 */     Preconditions.checkArgument((expectedValuesPerKey >= 0));
/*  97 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private HashMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 101 */     super(Maps.newHashMapWithExpectedSize(multimap.keySet().size()));
/*     */     
/* 103 */     putAll(multimap);
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
/* 114 */   Set<V> createCollection() { return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 124 */     stream.defaultWriteObject();
/* 125 */     stream.writeInt(this.expectedValuesPerKey);
/* 126 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 132 */     stream.defaultReadObject();
/* 133 */     this.expectedValuesPerKey = stream.readInt();
/* 134 */     int distinctKeys = Serialization.readCount(stream);
/* 135 */     Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
/* 136 */     setMap(map);
/* 137 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/HashMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */