/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Immutable
/*     */ final class DenseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final ImmutableMap<R, Map<C, V>> rowMap;
/*     */   private final ImmutableMap<C, Map<R, V>> columnMap;
/*     */   private final int[] rowCounts;
/*     */   private final int[] columnCounts;
/*     */   private final V[][] values;
/*     */   private final int[] iterationOrderRow;
/*     */   private final int[] iterationOrderColumn;
/*     */   
/*     */   private static <E> ImmutableMap<E, Integer> makeIndex(ImmutableSet<E> set) {
/*  44 */     ImmutableMap.Builder<E, Integer> indexBuilder = ImmutableMap.builder();
/*  45 */     int i = 0;
/*  46 */     for (E key : set) {
/*  47 */       indexBuilder.put(key, Integer.valueOf(i));
/*  48 */       i++;
/*     */     } 
/*  50 */     return indexBuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  56 */     V[][] array = (V[][])(Object[][])new Object[rowSpace.size()][columnSpace.size()];
/*  57 */     this.values = array;
/*  58 */     this.rowKeyToIndex = makeIndex(rowSpace);
/*  59 */     this.columnKeyToIndex = makeIndex(columnSpace);
/*  60 */     this.rowCounts = new int[this.rowKeyToIndex.size()];
/*  61 */     this.columnCounts = new int[this.columnKeyToIndex.size()];
/*  62 */     int[] iterationOrderRow = new int[cellList.size()];
/*  63 */     int[] iterationOrderColumn = new int[cellList.size()];
/*  64 */     for (int i = 0; i < cellList.size(); i++) {
/*  65 */       Table.Cell<R, C, V> cell = (Table.Cell)cellList.get(i);
/*  66 */       R rowKey = (R)cell.getRowKey();
/*  67 */       C columnKey = (C)cell.getColumnKey();
/*  68 */       int rowIndex = ((Integer)this.rowKeyToIndex.get(rowKey)).intValue();
/*  69 */       int columnIndex = ((Integer)this.columnKeyToIndex.get(columnKey)).intValue();
/*  70 */       V existingValue = (V)this.values[rowIndex][columnIndex];
/*  71 */       Preconditions.checkArgument((existingValue == null), "duplicate key: (%s, %s)", new Object[] { rowKey, columnKey });
/*  72 */       this.values[rowIndex][columnIndex] = cell.getValue();
/*  73 */       this.rowCounts[rowIndex] = this.rowCounts[rowIndex] + 1;
/*  74 */       this.columnCounts[columnIndex] = this.columnCounts[columnIndex] + 1;
/*  75 */       iterationOrderRow[i] = rowIndex;
/*  76 */       iterationOrderColumn[i] = columnIndex;
/*     */     } 
/*  78 */     this.iterationOrderRow = iterationOrderRow;
/*  79 */     this.iterationOrderColumn = iterationOrderColumn;
/*  80 */     this.rowMap = new RowMap(null);
/*  81 */     this.columnMap = new ColumnMap(null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class ImmutableArrayMap<K, V>
/*     */     extends ImmutableMap<K, V>
/*     */   {
/*     */     private final int size;
/*     */ 
/*     */     
/*  91 */     ImmutableArrayMap(int size) { this.size = size; }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract ImmutableMap<K, Integer> keyToIndex();
/*     */ 
/*     */     
/*  98 */     private boolean isFull() { return (this.size == keyToIndex().size()); }
/*     */ 
/*     */ 
/*     */     
/* 102 */     K getKey(int index) { return (K)keyToIndex().keySet().asList().get(index); }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     abstract V getValue(int param1Int);
/*     */ 
/*     */     
/* 109 */     ImmutableSet<K> createKeySet() { return isFull() ? keyToIndex().keySet() : super.createKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     public int size() { return this.size; }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 119 */       Integer keyIndex = (Integer)keyToIndex().get(key);
/* 120 */       return (V)((keyIndex == null) ? null : getValue(keyIndex.intValue()));
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 125 */       return new ImmutableMapEntrySet<K, V>()
/*     */         {
/* 127 */           ImmutableMap<K, V> map() { return super.this$0; }
/*     */ 
/*     */ 
/*     */           
/*     */           public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 132 */             return new AbstractIterator<Map.Entry<K, V>>()
/*     */               {
/*     */                 private int index;
/*     */                 private final int maxIndex;
/*     */                 
/*     */                 protected Map.Entry<K, V> computeNext() {
/* 138 */                   super.index++; for (; super.index < super.maxIndex; super.index++) {
/* 139 */                     V value = (V)DenseImmutableTable.ImmutableArrayMap.null.this.this$0.getValue(super.index);
/* 140 */                     if (value != null) {
/* 141 */                       return Maps.immutableEntry(DenseImmutableTable.ImmutableArrayMap.null.this.this$0.getKey(super.index), value);
/*     */                     }
/*     */                   } 
/* 144 */                   return (Map.Entry)endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Row extends ImmutableArrayMap<C, V> {
/*     */     private final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 156 */       super(this$0.rowCounts[rowIndex]);
/* 157 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 162 */     ImmutableMap<C, Integer> keyToIndex() { return DenseImmutableTable.this.columnKeyToIndex; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     V getValue(int keyIndex) { return (V)DenseImmutableTable.this.values[this.rowIndex][keyIndex]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 172 */     boolean isPartialView() { return true; }
/*     */   }
/*     */   
/*     */   private final class Column
/*     */     extends ImmutableArrayMap<R, V> {
/*     */     private final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 180 */       super(this$0.columnCounts[columnIndex]);
/* 181 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 186 */     ImmutableMap<R, Integer> keyToIndex() { return DenseImmutableTable.this.rowKeyToIndex; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     V getValue(int keyIndex) { return (V)DenseImmutableTable.this.values[keyIndex][this.columnIndex]; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     boolean isPartialView() { return true; }
/*     */   }
/*     */   
/*     */   private final class RowMap
/*     */     extends ImmutableArrayMap<R, Map<C, V>>
/*     */   {
/* 202 */     private RowMap() { super(this$0.rowCounts.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     ImmutableMap<R, Integer> keyToIndex() { return DenseImmutableTable.this.rowKeyToIndex; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     Map<C, V> getValue(int keyIndex) { return new DenseImmutableTable.Row(DenseImmutableTable.this, keyIndex); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     boolean isPartialView() { return false; }
/*     */   }
/*     */   
/*     */   private final class ColumnMap
/*     */     extends ImmutableArrayMap<C, Map<R, V>>
/*     */   {
/* 223 */     private ColumnMap() { super(this$0.columnCounts.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     ImmutableMap<C, Integer> keyToIndex() { return DenseImmutableTable.this.columnKeyToIndex; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     Map<R, V> getValue(int keyIndex) { return new DenseImmutableTable.Column(DenseImmutableTable.this, keyIndex); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     boolean isPartialView() { return false; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 243 */   public ImmutableMap<C, Map<R, V>> columnMap() { return this.columnMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public ImmutableMap<R, Map<C, V>> rowMap() { return this.rowMap; }
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 253 */     Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
/* 254 */     Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
/* 255 */     return (V)((rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 261 */   public int size() { return this.iterationOrderRow.length; }
/*     */ 
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 266 */     int rowIndex = this.iterationOrderRow[index];
/* 267 */     int columnIndex = this.iterationOrderColumn[index];
/* 268 */     R rowKey = (R)rowKeySet().asList().get(rowIndex);
/* 269 */     C columnKey = (C)columnKeySet().asList().get(columnIndex);
/* 270 */     V value = (V)this.values[rowIndex][columnIndex];
/* 271 */     return cellOf(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 276 */   V getValue(int index) { return (V)this.values[this.iterationOrderRow[index]][this.iterationOrderColumn[index]]; }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/DenseImmutableTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */