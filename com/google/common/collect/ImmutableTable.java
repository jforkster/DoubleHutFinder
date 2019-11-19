/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */ {
/*  49 */   private static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static <R, C, V> ImmutableTable<R, C, V> of() { return EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) { return new SingletonImmutableTable(rowKey, columnKey, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
/*     */     Table.Cell<? extends R, ? extends C, ? extends V> onlyCell;
/*  82 */     if (table instanceof ImmutableTable)
/*     */     {
/*  84 */       return (ImmutableTable)table;
/*     */     }
/*     */ 
/*     */     
/*  88 */     int size = table.size();
/*  89 */     switch (size) {
/*     */       case 0:
/*  91 */         return of();
/*     */       case 1:
/*  93 */         onlyCell = (Table.Cell)Iterables.getOnlyElement(table.cellSet());
/*     */         
/*  95 */         return of(onlyCell.getRowKey(), onlyCell.getColumnKey(), onlyCell.getValue());
/*     */     } 
/*     */     
/*  98 */     ImmutableSet.Builder<Table.Cell<R, C, V>> cellSetBuilder = ImmutableSet.builder();
/*     */ 
/*     */     
/* 101 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet())
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 106 */       cellSetBuilder.add(cellOf(cell.getRowKey(), cell.getColumnKey(), cell.getValue()));
/*     */     }
/*     */     
/* 109 */     return RegularImmutableTable.forCells(cellSetBuilder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static <R, C, V> Builder<R, C, V> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) { return Tables.immutableCell(Preconditions.checkNotNull(rowKey), Preconditions.checkNotNull(columnKey), Preconditions.checkNotNull(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<R, C, V>
/*     */     extends Object
/*     */   {
/* 158 */     private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     private Comparator<? super R> rowComparator;
/*     */ 
/*     */ 
/*     */     
/*     */     private Comparator<? super C> columnComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
/* 172 */       this.rowComparator = (Comparator)Preconditions.checkNotNull(rowComparator);
/* 173 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
/* 181 */       this.columnComparator = (Comparator)Preconditions.checkNotNull(columnComparator);
/* 182 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
/* 191 */       this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
/* 192 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
/* 202 */       if (cell instanceof Tables.ImmutableCell) {
/* 203 */         Preconditions.checkNotNull(cell.getRowKey());
/* 204 */         Preconditions.checkNotNull(cell.getColumnKey());
/* 205 */         Preconditions.checkNotNull(cell.getValue());
/*     */         
/* 207 */         Table.Cell<R, C, V> immutableCell = cell;
/* 208 */         this.cells.add(immutableCell);
/*     */       } else {
/* 210 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */       } 
/* 212 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 224 */       for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 225 */         put(cell);
/*     */       }
/* 227 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableTable<R, C, V> build() {
/* 236 */       int size = this.cells.size();
/* 237 */       switch (size) {
/*     */         case 0:
/* 239 */           return ImmutableTable.of();
/*     */         case 1:
/* 241 */           return new SingletonImmutableTable((Table.Cell)Iterables.getOnlyElement(this.cells));
/*     */       } 
/*     */       
/* 244 */       return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   public ImmutableSet<Table.Cell<R, C, V>> cellSet() { return (ImmutableSet)super.cellSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 261 */   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 266 */   public ImmutableCollection<V> values() { return (ImmutableCollection)super.values(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 274 */   final Iterator<V> valuesIterator() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, V> column(C columnKey) {
/* 283 */     Preconditions.checkNotNull(columnKey);
/* 284 */     return (ImmutableMap)Objects.firstNonNull((ImmutableMap)columnMap().get(columnKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public ImmutableSet<C> columnKeySet() { return columnMap().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<C, V> row(R rowKey) {
/* 307 */     Preconditions.checkNotNull(rowKey);
/* 308 */     return (ImmutableMap)Objects.firstNonNull((ImmutableMap)rowMap().get(rowKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 314 */   public ImmutableSet<R> rowKeySet() { return rowMap().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) { return (get(rowKey, columnKey) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public boolean containsValue(@Nullable Object value) { return values().contains(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 342 */   public final void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 352 */   public final V put(R rowKey, C columnKey, V value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 363 */   public final void putAll(Table<? extends R, ? extends C, ? extends V> table) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 373 */   public final V remove(Object rowKey, Object columnKey) { throw new UnsupportedOperationException(); }
/*     */   
/*     */   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   public abstract ImmutableMap<C, Map<R, V>> columnMap();
/*     */   
/*     */   public abstract ImmutableMap<R, Map<C, V>> rowMap();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */