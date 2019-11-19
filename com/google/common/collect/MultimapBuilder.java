/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public abstract class MultimapBuilder<K0, V0>
/*     */   extends Object
/*     */ {
/*     */   private static final int DEFAULT_EXPECTED_KEYS = 8;
/*     */   
/*     */   private MultimapBuilder() {}
/*     */   
/*  85 */   public static MultimapBuilderWithKeys<Object> hashKeys() { return hashKeys(8); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys(final int expectedKeys) {
/*  95 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/*  96 */     return new MultimapBuilderWithKeys<Object>()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/*  99 */           return new HashMap(expectedKeys);
/*     */         }
/*     */       };
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
/*     */   
/* 113 */   public static MultimapBuilderWithKeys<Object> linkedHashKeys() { return linkedHashKeys(8); }
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys(final int expectedKeys) {
/* 126 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/* 127 */     return new MultimapBuilderWithKeys<Object>()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/* 130 */           return new LinkedHashMap(expectedKeys);
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static MultimapBuilderWithKeys<Comparable> treeKeys() { return treeKeys(Ordering.natural()); }
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
/*     */   public static <K0> MultimapBuilderWithKeys<K0> treeKeys(final Comparator<K0> comparator) {
/* 164 */     Preconditions.checkNotNull(comparator);
/* 165 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 168 */           return new TreeMap(comparator);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(final Class<K0> keyClass) {
/* 178 */     Preconditions.checkNotNull(keyClass);
/* 179 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */ 
/*     */         
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap()
/*     */         {
/* 185 */           return new EnumMap(keyClass);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class ArrayListSupplier<V>
/*     */     extends Object implements Supplier<List<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/* 194 */     ArrayListSupplier(int expectedValuesPerKey) { this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     public List<V> get() { return new ArrayList(this.expectedValuesPerKey); }
/*     */   }
/*     */   
/*     */   private enum LinkedListSupplier
/*     */     implements Supplier<List<Object>> {
/* 204 */     INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     public static <V> Supplier<List<V>> instance() { return INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     public List<Object> get() { return new LinkedList(); }
/*     */   }
/*     */   
/*     */   private static final class HashSetSupplier<V>
/*     */     extends Object
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/* 223 */     HashSetSupplier(int expectedValuesPerKey) { this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     public Set<V> get() { return new HashSet(this.expectedValuesPerKey); }
/*     */   }
/*     */   
/*     */   private static final class LinkedHashSetSupplier<V>
/*     */     extends Object
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/* 236 */     LinkedHashSetSupplier(int expectedValuesPerKey) { this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey"); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     public Set<V> get() { return new LinkedHashSet(this.expectedValuesPerKey); }
/*     */   }
/*     */   
/*     */   private static final class TreeSetSupplier<V>
/*     */     extends Object
/*     */     implements Supplier<SortedSet<V>>, Serializable {
/*     */     private final Comparator<? super V> comparator;
/*     */     
/* 249 */     TreeSetSupplier(Comparator<? super V> comparator) { this.comparator = (Comparator)Preconditions.checkNotNull(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     public SortedSet<V> get() { return new TreeSet(this.comparator); }
/*     */   }
/*     */   
/*     */   private static final class EnumSetSupplier<V extends Enum<V>>
/*     */     extends Object
/*     */     implements Supplier<Set<V>>, Serializable
/*     */   {
/*     */     private final Class<V> clazz;
/*     */     
/* 263 */     EnumSetSupplier(Class<V> clazz) { this.clazz = (Class)Preconditions.checkNotNull(clazz); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     public Set<V> get() { return EnumSet.noneOf(this.clazz); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class MultimapBuilderWithKeys<K0>
/*     */     extends Object
/*     */   {
/*     */     private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <K extends K0, V> Map<K, Collection<V>> createMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues() { return arrayListValues(2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey) {
/* 300 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 301 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 304 */             return Multimaps.newListMultimap(super.this$0.createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues() {
/* 315 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 318 */             return Multimaps.newListMultimap(super.this$0.createMap(), MultimapBuilder.LinkedListSupplier.instance());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues() { return hashSetValues(2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey) {
/* 339 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 340 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 343 */             return Multimaps.newSetMultimap(super.this$0.createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 354 */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues() { return linkedHashSetValues(2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey) {
/* 364 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 365 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 368 */             return Multimaps.newSetMultimap(super.this$0.createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 380 */     public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues() { return treeSetValues(Ordering.natural()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator) {
/* 390 */       Preconditions.checkNotNull(comparator, "comparator");
/* 391 */       return new MultimapBuilder.SortedSetMultimapBuilder<K0, V0>()
/*     */         {
/*     */           public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
/* 394 */             return Multimaps.newSortedSetMultimap(super.this$0.createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass) {
/* 406 */       Preconditions.checkNotNull(valueClass, "valueClass");
/* 407 */       return new MultimapBuilder.SetMultimapBuilder<K0, V0>()
/*     */         {
/*     */ 
/*     */           
/*     */           public <K extends K0, V extends V0> SetMultimap<K, V> build()
/*     */           {
/* 413 */             Supplier<Set<V>> factory = new MultimapBuilder.EnumSetSupplier<Set<V>>(valueClass);
/* 414 */             return Multimaps.newSetMultimap(super.this$0.createMap(), factory);
/*     */           }
/*     */         };
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 433 */     Multimap<K, V> result = build();
/* 434 */     result.putAll(multimap);
/* 435 */     return result;
/*     */   }
/*     */   
/*     */   public abstract <K extends K0, V extends V0> Multimap<K, V> build();
/*     */   
/*     */   public static abstract class ListMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0> {
/* 442 */     ListMultimapBuilder() { super(null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 450 */     public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) { return (ListMultimap)super.build(multimap); }
/*     */     
/*     */     public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
/*     */   }
/*     */   
/*     */   public static abstract class SetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/* 458 */     SetMultimapBuilder() { super(null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) { return (SetMultimap)super.build(multimap); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SortedSetMultimapBuilder<K0, V0>
/*     */     extends SetMultimapBuilder<K0, V0>
/*     */   {
/* 482 */     public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) { return (SortedSetMultimap)super.build(multimap); }
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/MultimapBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */