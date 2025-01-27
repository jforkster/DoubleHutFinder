/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public final class ArrayListMultimap<K, V>
/*     */   extends AbstractListMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 3;
/*     */   @VisibleForTesting
/*     */   int expectedValuesPerKey;
/*     */   @GwtIncompatible("Not needed in emulated source.")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  78 */   public static <K, V> ArrayListMultimap<K, V> create() { return new ArrayListMultimap(); }
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
/*  92 */   public static <K, V> ArrayListMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) { return new ArrayListMultimap(expectedKeys, expectedValuesPerKey); }
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
/* 103 */   public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) { return new ArrayListMultimap(multimap); }
/*     */ 
/*     */   
/*     */   private ArrayListMultimap() {
/* 107 */     super(new HashMap());
/* 108 */     this.expectedValuesPerKey = 3;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
/* 112 */     super(Maps.newHashMapWithExpectedSize(expectedKeys));
/* 113 */     CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 114 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 118 */     this(multimap.keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap)multimap).expectedValuesPerKey : 3);
/*     */ 
/*     */ 
/*     */     
/* 122 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   List<V> createCollection() { return new ArrayList(this.expectedValuesPerKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimToSize() {
/* 137 */     for (Collection<V> collection : backingMap().values()) {
/* 138 */       ArrayList<V> arrayList = (ArrayList)collection;
/* 139 */       arrayList.trimToSize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 150 */     stream.defaultWriteObject();
/* 151 */     stream.writeInt(this.expectedValuesPerKey);
/* 152 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 158 */     stream.defaultReadObject();
/* 159 */     this.expectedValuesPerKey = stream.readInt();
/* 160 */     int distinctKeys = Serialization.readCount(stream);
/* 161 */     Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
/* 162 */     setMap(map);
/* 163 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ArrayListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */