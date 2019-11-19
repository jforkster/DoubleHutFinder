/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ abstract class RegularImmutableTable<R, C, V>
/*     */   extends ImmutableTable<R, C, V>
/*     */ {
/*  41 */   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() { return isEmpty() ? ImmutableSet.of() : new CellSet(null); }
/*     */   
/*     */   private final class CellSet
/*     */     extends ImmutableSet<Table.Cell<R, C, V>> {
/*     */     private CellSet() {}
/*     */     
/*  47 */     public int size() { return RegularImmutableTable.this.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     public UnmodifiableIterator<Table.Cell<R, C, V>> iterator() { return asList().iterator(); }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableList<Table.Cell<R, C, V>> createAsList() {
/*  57 */       return new ImmutableAsList<Table.Cell<R, C, V>>()
/*     */         {
/*     */           public Table.Cell<R, C, V> get(int index) {
/*  60 */             return RegularImmutableTable.CellSet.this.this$0.getCell(index);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*  65 */           ImmutableCollection<Table.Cell<R, C, V>> delegateCollection() { return super.this$1; }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/*  72 */       if (object instanceof Table.Cell) {
/*  73 */         Table.Cell<?, ?, ?> cell = (Table.Cell)object;
/*  74 */         Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
/*  75 */         return (value != null && value.equals(cell.getValue()));
/*     */       } 
/*  77 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  82 */     boolean isPartialView() { return false; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   final ImmutableCollection<V> createValues() { return isEmpty() ? ImmutableList.of() : new Values(null); }
/*     */   
/*     */   private final class Values
/*     */     extends ImmutableList<V> {
/*     */     private Values() {}
/*     */     
/*  96 */     public int size() { return RegularImmutableTable.this.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     public V get(int index) { return (V)RegularImmutableTable.this.getValue(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     boolean isPartialView() { return true; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
/* 114 */     Preconditions.checkNotNull(cells);
/* 115 */     if (rowComparator != null || columnComparator != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       Comparator<Table.Cell<R, C, V>> comparator = new Comparator<Table.Cell<R, C, V>>() {
/*     */           public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) {
/* 125 */             int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
/*     */             
/* 127 */             if (rowCompare != 0) {
/* 128 */               return rowCompare;
/*     */             }
/* 130 */             return (columnComparator == null) ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
/*     */           }
/*     */         };
/*     */       
/* 134 */       Collections.sort(cells, comparator);
/*     */     } 
/* 136 */     return forCellsInternal(cells, rowComparator, columnComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) { return forCellsInternal(cells, null, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
/* 152 */     ImmutableSet.Builder<R> rowSpaceBuilder = ImmutableSet.builder();
/* 153 */     ImmutableSet.Builder<C> columnSpaceBuilder = ImmutableSet.builder();
/* 154 */     ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
/* 155 */     for (Table.Cell<R, C, V> cell : cellList) {
/* 156 */       rowSpaceBuilder.add(cell.getRowKey());
/* 157 */       columnSpaceBuilder.add(cell.getColumnKey());
/*     */     } 
/*     */     
/* 160 */     ImmutableSet<R> rowSpace = rowSpaceBuilder.build();
/* 161 */     if (rowComparator != null) {
/* 162 */       List<R> rowList = Lists.newArrayList(rowSpace);
/* 163 */       Collections.sort(rowList, rowComparator);
/* 164 */       rowSpace = ImmutableSet.copyOf(rowList);
/*     */     } 
/* 166 */     ImmutableSet<C> columnSpace = columnSpaceBuilder.build();
/* 167 */     if (columnComparator != null) {
/* 168 */       List<C> columnList = Lists.newArrayList(columnSpace);
/* 169 */       Collections.sort(columnList, columnComparator);
/* 170 */       columnSpace = ImmutableSet.copyOf(columnList);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 175 */     return (cellList.size() > rowSpace.size() * columnSpace.size() / 2L) ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
/*     */   }
/*     */   
/*     */   abstract Table.Cell<R, C, V> getCell(int paramInt);
/*     */   
/*     */   abstract V getValue(int paramInt);
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */