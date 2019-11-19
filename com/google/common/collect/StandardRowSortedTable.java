/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class StandardRowSortedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */   implements RowSortedTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  59 */   StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) { super(backingMap, factory); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   private SortedMap<R, Map<C, V>> sortedBackingMap() { return (SortedMap)this.backingMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public SortedSet<R> rowKeySet() { return (SortedSet)rowMap().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public SortedMap<R, Map<C, V>> rowMap() { return (SortedMap)super.rowMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   SortedMap<R, Map<C, V>> createRowMap() { return new RowSortedMap(null); }
/*     */   
/*     */   private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> { private RowSortedMap() {
/*  91 */       super(StandardRowSortedTable.this);
/*     */     }
/*     */     
/*  94 */     public SortedSet<R> keySet() { return (SortedSet)super.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     SortedSet<R> createKeySet() { return new Maps.SortedKeySet(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     public Comparator<? super R> comparator() { return StandardRowSortedTable.this.sortedBackingMap().comparator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     public R firstKey() { return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     public R lastKey() { return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> headMap(R toKey) {
/* 119 */       Preconditions.checkNotNull(toKey);
/* 120 */       return (new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory)).rowMap();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey) {
/* 126 */       Preconditions.checkNotNull(fromKey);
/* 127 */       Preconditions.checkNotNull(toKey);
/* 128 */       return (new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory)).rowMap();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> tailMap(R fromKey) {
/* 134 */       Preconditions.checkNotNull(fromKey);
/* 135 */       return (new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory)).rowMap();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/StandardRowSortedTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */