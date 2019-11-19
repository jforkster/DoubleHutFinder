/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractTable<R, C, V>
/*     */   extends Object
/*     */   implements Table<R, C, V>
/*     */ {
/*     */   private Set<Table.Cell<R, C, V>> cellSet;
/*     */   private Collection<V> values;
/*     */   
/*  38 */   public boolean containsRow(@Nullable Object rowKey) { return Maps.safeContainsKey(rowMap(), rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public boolean containsColumn(@Nullable Object columnKey) { return Maps.safeContainsKey(columnMap(), columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public Set<R> rowKeySet() { return rowMap().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public Set<C> columnKeySet() { return columnMap().keySet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/*  58 */     for (Map<C, V> row : rowMap().values()) {
/*  59 */       if (row.containsValue(value)) {
/*  60 */         return true;
/*     */       }
/*     */     } 
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  68 */     Map<C, V> row = (Map)Maps.safeGet(rowMap(), rowKey);
/*  69 */     return (row != null && Maps.safeContainsKey(row, columnKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  74 */     Map<C, V> row = (Map)Maps.safeGet(rowMap(), rowKey);
/*  75 */     return (V)((row == null) ? null : Maps.safeGet(row, columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public boolean isEmpty() { return (size() == 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void clear() { Iterators.clear(cellSet().iterator()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  90 */     Map<C, V> row = (Map)Maps.safeGet(rowMap(), rowKey);
/*  91 */     return (V)((row == null) ? null : Maps.safeRemove(row, columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public V put(R rowKey, C columnKey, V value) { return (V)row(rowKey).put(columnKey, value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 101 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 102 */       put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 110 */     Set<Table.Cell<R, C, V>> result = this.cellSet;
/* 111 */     return (result == null) ? (this.cellSet = createCellSet()) : result;
/*     */   }
/*     */ 
/*     */   
/* 115 */   Set<Table.Cell<R, C, V>> createCellSet() { return new CellSet(); }
/*     */   
/*     */   abstract Iterator<Table.Cell<R, C, V>> cellIterator();
/*     */   
/*     */   class CellSet
/*     */     extends AbstractSet<Table.Cell<R, C, V>>
/*     */   {
/*     */     public boolean contains(Object o) {
/* 123 */       if (o instanceof Table.Cell) {
/* 124 */         Table.Cell<?, ?, ?> cell = (Table.Cell)o;
/* 125 */         Map<C, V> row = (Map)Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
/* 126 */         return (row != null && Collections2.safeContains(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())));
/*     */       } 
/*     */       
/* 129 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@Nullable Object o) {
/* 134 */       if (o instanceof Table.Cell) {
/* 135 */         Table.Cell<?, ?, ?> cell = (Table.Cell)o;
/* 136 */         Map<C, V> row = (Map)Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
/* 137 */         return (row != null && Collections2.safeRemove(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())));
/*     */       } 
/*     */       
/* 140 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 145 */     public void clear() { AbstractTable.this.clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     public Iterator<Table.Cell<R, C, V>> iterator() { return AbstractTable.this.cellIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     public int size() { return AbstractTable.this.size(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 163 */     Collection<V> result = this.values;
/* 164 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */ 
/*     */   
/* 168 */   Collection<V> createValues() { return new Values(); }
/*     */ 
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 172 */     return new TransformedIterator<Table.Cell<R, C, V>, V>(cellSet().iterator())
/*     */       {
/*     */         V transform(Table.Cell<R, C, V> cell) {
/* 175 */           return (V)cell.getValue();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/* 183 */     public Iterator<V> iterator() { return AbstractTable.this.valuesIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     public boolean contains(Object o) { return AbstractTable.this.containsValue(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     public void clear() { AbstractTable.this.clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     public int size() { return AbstractTable.this.size(); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public boolean equals(@Nullable Object obj) { return Tables.equalsImpl(this, obj); }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public int hashCode() { return cellSet().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 214 */   public String toString() { return rowMap().toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */