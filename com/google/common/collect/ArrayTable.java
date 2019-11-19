/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ArrayTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private final ImmutableList<R> rowList;
/*     */   private final ImmutableList<C> columnList;
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final V[][] array;
/*     */   private ColumnMap columnMap;
/*     */   private RowMap rowMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  99 */   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) { return new ArrayTable(rowKeys, columnKeys); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) { return (table instanceof ArrayTable) ? new ArrayTable((ArrayTable)table) : new ArrayTable(table); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 146 */     this.rowList = ImmutableList.copyOf(rowKeys);
/* 147 */     this.columnList = ImmutableList.copyOf(columnKeys);
/* 148 */     Preconditions.checkArgument(!this.rowList.isEmpty());
/* 149 */     Preconditions.checkArgument(!this.columnList.isEmpty());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     this.rowKeyToIndex = index(this.rowList);
/* 157 */     this.columnKeyToIndex = index(this.columnList);
/*     */ 
/*     */     
/* 160 */     V[][] tmpArray = (V[][])(Object[][])new Object[this.rowList.size()][this.columnList.size()];
/*     */     
/* 162 */     this.array = tmpArray;
/*     */     
/* 164 */     eraseAll();
/*     */   }
/*     */   
/*     */   private static <E> ImmutableMap<E, Integer> index(List<E> list) {
/* 168 */     ImmutableMap.Builder<E, Integer> columnBuilder = ImmutableMap.builder();
/* 169 */     for (int i = 0; i < list.size(); i++) {
/* 170 */       columnBuilder.put(list.get(i), Integer.valueOf(i));
/*     */     }
/* 172 */     return columnBuilder.build();
/*     */   }
/*     */   
/*     */   private ArrayTable(Table<R, C, V> table) {
/* 176 */     this(table.rowKeySet(), table.columnKeySet());
/* 177 */     putAll(table);
/*     */   }
/*     */   
/*     */   private ArrayTable(ArrayTable<R, C, V> table) {
/* 181 */     this.rowList = table.rowList;
/* 182 */     this.columnList = table.columnList;
/* 183 */     this.rowKeyToIndex = table.rowKeyToIndex;
/* 184 */     this.columnKeyToIndex = table.columnKeyToIndex;
/*     */     
/* 186 */     V[][] copy = (V[][])(Object[][])new Object[this.rowList.size()][this.columnList.size()];
/* 187 */     this.array = copy;
/*     */     
/* 189 */     eraseAll();
/* 190 */     for (int i = 0; i < this.rowList.size(); i++)
/* 191 */       System.arraycopy(table.array[i], 0, copy[i], 0, table.array[i].length); 
/*     */   }
/*     */   
/*     */   private static abstract class ArrayMap<K, V>
/*     */     extends Maps.ImprovedAbstractMap<K, V>
/*     */   {
/*     */     private final ImmutableMap<K, Integer> keyIndex;
/*     */     
/* 199 */     private ArrayMap(ImmutableMap<K, Integer> keyIndex) { this.keyIndex = keyIndex; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 204 */     public Set<K> keySet() { return this.keyIndex.keySet(); }
/*     */ 
/*     */ 
/*     */     
/* 208 */     K getKey(int index) { return (K)this.keyIndex.keySet().asList().get(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     public int size() { return this.keyIndex.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     public boolean isEmpty() { return this.keyIndex.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 229 */       return new Maps.EntrySet<K, V>()
/*     */         {
/*     */           Map<K, V> map() {
/* 232 */             return super.this$0;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Map.Entry<K, V>> iterator() {
/* 237 */             return new AbstractIndexedListIterator<Map.Entry<K, V>>(size())
/*     */               {
/*     */                 protected Map.Entry<K, V> get(final int index) {
/* 240 */                   return new AbstractMapEntry<K, V>()
/*     */                     {
/*     */                       public K getKey() {
/* 243 */                         return (K)ArrayTable.ArrayMap.null.this.this$0.getKey(index);
/*     */                       }
/*     */ 
/*     */ 
/*     */                       
/* 248 */                       public V getValue() { return (V)ArrayTable.ArrayMap.null.this.this$0.getValue(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/* 253 */                       public V setValue(V value) { return (V)ArrayTable.ArrayMap.null.this.this$0.setValue(index, value); }
/*     */                     };
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     public boolean containsKey(@Nullable Object key) { return this.keyIndex.containsKey(key); }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 271 */       Integer index = (Integer)this.keyIndex.get(key);
/* 272 */       if (index == null) {
/* 273 */         return null;
/*     */       }
/* 275 */       return (V)getValue(index.intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(K key, V value) {
/* 281 */       Integer index = (Integer)this.keyIndex.get(key);
/* 282 */       if (index == null) {
/* 283 */         throw new IllegalArgumentException(getKeyRole() + " " + key + " not in " + this.keyIndex.keySet());
/*     */       }
/*     */       
/* 286 */       return (V)setValue(index.intValue(), value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 291 */     public V remove(Object key) { throw new UnsupportedOperationException(); }
/*     */     abstract String getKeyRole();
/*     */     @Nullable
/*     */     abstract V getValue(int param1Int);
/*     */     
/* 296 */     public void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     abstract V setValue(int param1Int, V param1V);
/*     */   }
/*     */ 
/*     */   
/* 305 */   public ImmutableList<R> rowKeyList() { return this.rowList; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   public ImmutableList<C> columnKeyList() { return this.columnList; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V at(int rowIndex, int columnIndex) {
/* 332 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 333 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 334 */     return (V)this.array[rowIndex][columnIndex];
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
/*     */   public V set(int rowIndex, int columnIndex, @Nullable V value) {
/* 354 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 355 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 356 */     V oldValue = (V)this.array[rowIndex][columnIndex];
/* 357 */     this.array[rowIndex][columnIndex] = value;
/* 358 */     return oldValue;
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
/*     */   @GwtIncompatible("reflection")
/*     */   public V[][] toArray(Class<V> valueClass) {
/* 375 */     V[][] copy = (V[][])(Object[][])Array.newInstance(valueClass, new int[] { this.rowList.size(), this.columnList.size() });
/*     */     
/* 377 */     for (int i = 0; i < this.rowList.size(); i++) {
/* 378 */       System.arraycopy(this.array[i], 0, copy[i], 0, this.array[i].length);
/*     */     }
/* 380 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 391 */   public void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void eraseAll() {
/*     */     Object[][] arr$;
/*     */     int len$;
/*     */     int i$;
/* 399 */     for (arr$ = this.array, len$ = arr$.length, i$ = 0; i$ < len$; ) { V[] row = (V[])arr$[i$];
/* 400 */       Arrays.fill(row, null);
/*     */       i$++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 410 */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) { return (containsRow(rowKey) && containsColumn(columnKey)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 419 */   public boolean containsColumn(@Nullable Object columnKey) { return this.columnKeyToIndex.containsKey(columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 428 */   public boolean containsRow(@Nullable Object rowKey) { return this.rowKeyToIndex.containsKey(rowKey); }
/*     */   public boolean containsValue(@Nullable Object value) {
/*     */     Object[][] arr$;
/*     */     int len$;
/*     */     int i$;
/* 433 */     for (arr$ = this.array, len$ = arr$.length, i$ = 0; i$ < len$; ) { V[] row = (V[])arr$[i$];
/* 434 */       for (V element : row) {
/* 435 */         if (Objects.equal(value, element))
/* 436 */           return true; 
/*     */       } 
/*     */       i$++; }
/*     */     
/* 440 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 445 */     Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
/* 446 */     Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
/* 447 */     return (V)((rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 456 */   public boolean isEmpty() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(R rowKey, C columnKey, @Nullable V value) {
/* 467 */     Preconditions.checkNotNull(rowKey);
/* 468 */     Preconditions.checkNotNull(columnKey);
/* 469 */     Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
/* 470 */     Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", new Object[] { rowKey, this.rowList });
/* 471 */     Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
/* 472 */     Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", new Object[] { columnKey, this.columnList });
/*     */     
/* 474 */     return (V)set(rowIndex.intValue(), columnIndex.intValue(), value);
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
/* 495 */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) { super.putAll(table); }
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
/* 506 */   public V remove(Object rowKey, Object columnKey) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V erase(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 523 */     Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
/* 524 */     Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
/* 525 */     if (rowIndex == null || columnIndex == null) {
/* 526 */       return null;
/*     */     }
/* 528 */     return (V)set(rowIndex.intValue(), columnIndex.intValue(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 535 */   public int size() { return this.rowList.size() * this.columnList.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 553 */   public Set<Table.Cell<R, C, V>> cellSet() { return super.cellSet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 558 */     return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size()) {
/*     */         protected Table.Cell<R, C, V> get(final int index) {
/* 560 */           return new Tables.AbstractCell<R, C, V>()
/*     */             {
/*     */               final int rowIndex;
/*     */               final int columnIndex;
/*     */               
/* 565 */               public R getRowKey() { return (R)ArrayTable.null.this.this$0.rowList.get(super.rowIndex); }
/*     */ 
/*     */ 
/*     */               
/* 569 */               public C getColumnKey() { return (C)ArrayTable.null.this.this$0.columnList.get(super.columnIndex); }
/*     */ 
/*     */ 
/*     */               
/* 573 */               public V getValue() { return (V)ArrayTable.null.this.this$0.at(super.rowIndex, super.columnIndex); }
/*     */             };
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
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/* 594 */     Preconditions.checkNotNull(columnKey);
/* 595 */     Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
/* 596 */     return (columnIndex == null) ? ImmutableMap.of() : new Column(columnIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Column
/*     */     extends ArrayMap<R, V> {
/*     */     final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 604 */       super(this$0.rowKeyToIndex, null);
/* 605 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 610 */     String getKeyRole() { return "Row"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 615 */     V getValue(int index) { return (V)ArrayTable.this.at(index, this.columnIndex); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 620 */     V setValue(int index, V newValue) { return (V)ArrayTable.this.set(index, this.columnIndex, newValue); }
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
/* 632 */   public ImmutableSet<C> columnKeySet() { return this.columnKeyToIndex.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 639 */     ColumnMap map = this.columnMap;
/* 640 */     return (map == null) ? (this.columnMap = new ColumnMap(null)) : map;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends ArrayMap<C, Map<R, V>> {
/* 645 */     private ColumnMap() { super(this$0.columnKeyToIndex, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 650 */     String getKeyRole() { return "Column"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 655 */     Map<R, V> getValue(int index) { return new ArrayTable.Column(ArrayTable.this, index); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 660 */     Map<R, V> setValue(int index, Map<R, V> newValue) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 665 */     public Map<R, V> put(C key, Map<R, V> value) { throw new UnsupportedOperationException(); }
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
/*     */   public Map<C, V> row(R rowKey) {
/* 684 */     Preconditions.checkNotNull(rowKey);
/* 685 */     Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
/* 686 */     return (rowIndex == null) ? ImmutableMap.of() : new Row(rowIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Row extends ArrayMap<C, V> {
/*     */     final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 693 */       super(this$0.columnKeyToIndex, null);
/* 694 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 699 */     String getKeyRole() { return "Column"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 704 */     V getValue(int index) { return (V)ArrayTable.this.at(this.rowIndex, index); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 709 */     V setValue(int index, V newValue) { return (V)ArrayTable.this.set(this.rowIndex, index, newValue); }
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
/* 721 */   public ImmutableSet<R> rowKeySet() { return this.rowKeyToIndex.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 728 */     RowMap map = this.rowMap;
/* 729 */     return (map == null) ? (this.rowMap = new RowMap(null)) : map;
/*     */   }
/*     */   
/*     */   private class RowMap
/*     */     extends ArrayMap<R, Map<C, V>> {
/* 734 */     private RowMap() { super(this$0.rowKeyToIndex, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 739 */     String getKeyRole() { return "Row"; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 744 */     Map<C, V> getValue(int index) { return new ArrayTable.Row(ArrayTable.this, index); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 749 */     Map<C, V> setValue(int index, Map<C, V> newValue) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 754 */     public Map<C, V> put(R key, Map<C, V> value) { throw new UnsupportedOperationException(); }
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
/* 769 */   public Collection<V> values() { return super.values(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ArrayTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */