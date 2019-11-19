/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible
/*    */ class SingletonImmutableTable<R, C, V>
/*    */   extends ImmutableTable<R, C, V>
/*    */ {
/*    */   final R singleRowKey;
/*    */   final C singleColumnKey;
/*    */   final V singleValue;
/*    */   
/*    */   SingletonImmutableTable(R rowKey, C columnKey, V value) {
/* 37 */     this.singleRowKey = Preconditions.checkNotNull(rowKey);
/* 38 */     this.singleColumnKey = Preconditions.checkNotNull(columnKey);
/* 39 */     this.singleValue = Preconditions.checkNotNull(value);
/*    */   }
/*    */ 
/*    */   
/* 43 */   SingletonImmutableTable(Table.Cell<R, C, V> cell) { this(cell.getRowKey(), cell.getColumnKey(), cell.getValue()); }
/*    */ 
/*    */   
/*    */   public ImmutableMap<R, V> column(C columnKey) {
/* 47 */     Preconditions.checkNotNull(columnKey);
/* 48 */     return containsColumn(columnKey) ? ImmutableMap.of(this.singleRowKey, this.singleValue) : ImmutableMap.of();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public ImmutableMap<C, Map<R, V>> columnMap() { return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public ImmutableMap<R, Map<C, V>> rowMap() { return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public int size() { return 1; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   ImmutableSet<Table.Cell<R, C, V>> createCellSet() { return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   ImmutableCollection<V> createValues() { return ImmutableSet.of(this.singleValue); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SingletonImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */