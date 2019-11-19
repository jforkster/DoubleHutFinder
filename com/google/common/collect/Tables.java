/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
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
/*     */ public final class Tables
/*     */ {
/*  67 */   public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) { return new ImmutableCell(rowKey, columnKey, value); }
/*     */   
/*     */   static final class ImmutableCell<R, C, V>
/*     */     extends AbstractCell<R, C, V>
/*     */     implements Serializable {
/*     */     private final R rowKey;
/*     */     private final C columnKey;
/*     */     private final V value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ImmutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/*  78 */       this.rowKey = rowKey;
/*  79 */       this.columnKey = columnKey;
/*  80 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     public R getRowKey() { return (R)this.rowKey; }
/*     */ 
/*     */ 
/*     */     
/*  89 */     public C getColumnKey() { return (C)this.columnKey; }
/*     */ 
/*     */ 
/*     */     
/*  93 */     public V getValue() { return (V)this.value; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractCell<R, C, V>
/*     */     extends Object
/*     */     implements Table.Cell<R, C, V>
/*     */   {
/*     */     public boolean equals(Object obj) {
/* 104 */       if (obj == this) {
/* 105 */         return true;
/*     */       }
/* 107 */       if (obj instanceof Table.Cell) {
/* 108 */         Table.Cell<?, ?, ?> other = (Table.Cell)obj;
/* 109 */         return (Objects.equal(getRowKey(), other.getRowKey()) && Objects.equal(getColumnKey(), other.getColumnKey()) && Objects.equal(getValue(), other.getValue()));
/*     */       } 
/*     */ 
/*     */       
/* 113 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 117 */     public int hashCode() { return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() }); }
/*     */ 
/*     */ 
/*     */     
/* 121 */     public String toString() { return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue(); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) { return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable(table); }
/*     */ 
/*     */   
/*     */   private static class TransposeTable<C, R, V>
/*     */     extends AbstractTable<C, R, V>
/*     */   {
/*     */     final Table<R, C, V> original;
/*     */ 
/*     */     
/* 149 */     TransposeTable(Table<R, C, V> original) { this.original = (Table)Preconditions.checkNotNull(original); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 154 */     public void clear() { this.original.clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     public Map<C, V> column(R columnKey) { return this.original.row(columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     public Set<R> columnKeySet() { return this.original.rowKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     public Map<R, Map<C, V>> columnMap() { return this.original.rowMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) { return this.original.contains(columnKey, rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     public boolean containsColumn(@Nullable Object columnKey) { return this.original.containsRow(columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     public boolean containsRow(@Nullable Object rowKey) { return this.original.containsColumn(rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     public boolean containsValue(@Nullable Object value) { return this.original.containsValue(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     public V get(@Nullable Object rowKey, @Nullable Object columnKey) { return (V)this.original.get(columnKey, rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     public V put(C rowKey, R columnKey, V value) { return (V)this.original.put(columnKey, rowKey, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     public void putAll(Table<? extends C, ? extends R, ? extends V> table) { this.original.putAll(Tables.transpose(table)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     public V remove(@Nullable Object rowKey, @Nullable Object columnKey) { return (V)this.original.remove(columnKey, rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     public Map<R, V> row(C rowKey) { return this.original.column(rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     public Set<C> rowKeySet() { return this.original.columnKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     public Map<C, Map<R, V>> rowMap() { return this.original.columnMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     public int size() { return this.original.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     public Collection<V> values() { return this.original.values(); }
/*     */ 
/*     */ 
/*     */     
/* 239 */     private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>()
/*     */       {
/*     */         public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell)
/*     */         {
/* 243 */           return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     Iterator<Table.Cell<C, R, V>> cellIterator() { return Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/* 299 */     Preconditions.checkArgument(backingMap.isEmpty());
/* 300 */     Preconditions.checkNotNull(factory);
/*     */     
/* 302 */     return new StandardTable(backingMap, factory);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/* 334 */   public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) { return new TransformedTable(fromTable, function); }
/*     */ 
/*     */   
/*     */   private static class TransformedTable<R, C, V1, V2>
/*     */     extends AbstractTable<R, C, V2>
/*     */   {
/*     */     final Table<R, C, V1> fromTable;
/*     */     final Function<? super V1, V2> function;
/*     */     
/*     */     TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 344 */       this.fromTable = (Table)Preconditions.checkNotNull(fromTable);
/* 345 */       this.function = (Function)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/* 349 */     public boolean contains(Object rowKey, Object columnKey) { return this.fromTable.contains(rowKey, columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     public V2 get(Object rowKey, Object columnKey) { return (V2)(contains(rowKey, columnKey) ? this.function.apply(this.fromTable.get(rowKey, columnKey)) : null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     public int size() { return this.fromTable.size(); }
/*     */ 
/*     */ 
/*     */     
/* 364 */     public void clear() { this.fromTable.clear(); }
/*     */ 
/*     */ 
/*     */     
/* 368 */     public V2 put(R rowKey, C columnKey, V2 value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     public void putAll(Table<? extends R, ? extends C, ? extends V2> table) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */     
/* 377 */     public V2 remove(Object rowKey, Object columnKey) { return (V2)(contains(rowKey, columnKey) ? this.function.apply(this.fromTable.remove(rowKey, columnKey)) : null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     public Map<C, V2> row(R rowKey) { return Maps.transformValues(this.fromTable.row(rowKey), this.function); }
/*     */ 
/*     */ 
/*     */     
/* 386 */     public Map<R, V2> column(C columnKey) { return Maps.transformValues(this.fromTable.column(columnKey), this.function); }
/*     */ 
/*     */     
/*     */     Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
/* 390 */       return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>()
/*     */         {
/* 392 */           public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) { return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), Tables.TransformedTable.this.function.apply(cell.getValue())); }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 401 */     Iterator<Table.Cell<R, C, V2>> cellIterator() { return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction()); }
/*     */ 
/*     */ 
/*     */     
/* 405 */     public Set<R> rowKeySet() { return this.fromTable.rowKeySet(); }
/*     */ 
/*     */ 
/*     */     
/* 409 */     public Set<C> columnKeySet() { return this.fromTable.columnKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     Collection<V2> createValues() { return Collections2.transform(this.fromTable.values(), this.function); }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V2>> rowMap() {
/* 418 */       Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>()
/*     */         {
/*     */           public Map<C, V2> apply(Map<C, V1> row) {
/* 421 */             return Maps.transformValues(row, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 424 */       return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
/*     */     }
/*     */     
/*     */     public Map<C, Map<R, V2>> columnMap() {
/* 428 */       Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>()
/*     */         {
/*     */           public Map<R, V2> apply(Map<R, V1> column) {
/* 431 */             return Maps.transformValues(column, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 434 */       return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 455 */   public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) { return new UnmodifiableTable(table); }
/*     */   
/*     */   private static class UnmodifiableTable<R, C, V>
/*     */     extends ForwardingTable<R, C, V>
/*     */     implements Serializable {
/*     */     final Table<? extends R, ? extends C, ? extends V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 463 */     UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) { this.delegate = (Table)Preconditions.checkNotNull(delegate); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 469 */     protected Table<R, C, V> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     public Set<Table.Cell<R, C, V>> cellSet() { return Collections.unmodifiableSet(super.cellSet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 479 */     public void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 484 */     public Map<R, V> column(@Nullable C columnKey) { return Collections.unmodifiableMap(super.column(columnKey)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 489 */     public Set<C> columnKeySet() { return Collections.unmodifiableSet(super.columnKeySet()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> columnMap() {
/* 494 */       Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
/* 495 */       return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 500 */     public V put(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 505 */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 510 */     public V remove(@Nullable Object rowKey, @Nullable Object columnKey) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 515 */     public Map<C, V> row(@Nullable R rowKey) { return Collections.unmodifiableMap(super.row(rowKey)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 520 */     public Set<R> rowKeySet() { return Collections.unmodifiableSet(super.rowKeySet()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> rowMap() {
/* 525 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 526 */       return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 531 */     public Collection<V> values() { return Collections.unmodifiableCollection(super.values()); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/* 557 */   public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) { return new UnmodifiableRowSortedMap(table); }
/*     */   
/*     */   static final class UnmodifiableRowSortedMap<R, C, V>
/*     */     extends UnmodifiableTable<R, C, V>
/*     */     implements RowSortedTable<R, C, V> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 564 */     public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) { super(delegate); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 569 */     protected RowSortedTable<R, C, V> delegate() { return (RowSortedTable)super.delegate(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> rowMap() {
/* 574 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 575 */       return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 580 */     public SortedSet<R> rowKeySet() { return Collections.unmodifiableSortedSet(delegate().rowKeySet()); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 588 */   private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() { return UNMODIFIABLE_WRAPPER; }
/*     */ 
/*     */   
/* 591 */   private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>()
/*     */     {
/*     */       public Map<Object, Object> apply(Map<Object, Object> input)
/*     */       {
/* 595 */         return Collections.unmodifiableMap(input);
/*     */       }
/*     */     };
/*     */   
/*     */   static boolean equalsImpl(Table<?, ?, ?> table, @Nullable Object obj) {
/* 600 */     if (obj == table)
/* 601 */       return true; 
/* 602 */     if (obj instanceof Table) {
/* 603 */       Table<?, ?, ?> that = (Table)obj;
/* 604 */       return table.cellSet().equals(that.cellSet());
/*     */     } 
/* 606 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Tables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */