/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ public class HashBasedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     extends Object
/*     */     implements Supplier<Map<C, V>>, Serializable
/*     */   {
/*     */     final int expectedSize;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*  61 */     Factory(int expectedSize) { this.expectedSize = expectedSize; }
/*     */ 
/*     */ 
/*     */     
/*  65 */     public Map<C, V> get() { return Maps.newHashMapWithExpectedSize(this.expectedSize); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static <R, C, V> HashBasedTable<R, C, V> create() { return new HashBasedTable(new HashMap(), new Factory(0)); }
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
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create(int expectedRows, int expectedCellsPerRow) {
/*  89 */     CollectPreconditions.checkNonnegative(expectedCellsPerRow, "expectedCellsPerRow");
/*  90 */     Map<R, Map<C, V>> backingMap = Maps.newHashMapWithExpectedSize(expectedRows);
/*     */     
/*  92 */     return new HashBasedTable(backingMap, new Factory(expectedCellsPerRow));
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
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> table) {
/* 106 */     HashBasedTable<R, C, V> result = create();
/* 107 */     result.putAll(table);
/* 108 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 112 */   HashBasedTable(Map<R, Map<C, V>> backingMap, Factory<C, V> factory) { super(backingMap, factory); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) { return super.contains(rowKey, columnKey); }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public boolean containsColumn(@Nullable Object columnKey) { return super.containsColumn(columnKey); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public boolean containsRow(@Nullable Object rowKey) { return super.containsRow(rowKey); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public boolean containsValue(@Nullable Object value) { return super.containsValue(value); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) { return (V)super.get(rowKey, columnKey); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public boolean equals(@Nullable Object obj) { return super.equals(obj); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) { return (V)super.remove(rowKey, columnKey); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/HashBasedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */