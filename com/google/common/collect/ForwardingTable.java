/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingTable<R, C, V>
/*     */   extends ForwardingObject
/*     */   implements Table<R, C, V>
/*     */ {
/*  44 */   public Set<Table.Cell<R, C, V>> cellSet() { return delegate().cellSet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public void clear() { delegate().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public Map<R, V> column(C columnKey) { return delegate().column(columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public Set<C> columnKeySet() { return delegate().columnKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public Map<C, Map<R, V>> columnMap() { return delegate().columnMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public boolean contains(Object rowKey, Object columnKey) { return delegate().contains(rowKey, columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public boolean containsColumn(Object columnKey) { return delegate().containsColumn(columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public boolean containsRow(Object rowKey) { return delegate().containsRow(rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean containsValue(Object value) { return delegate().containsValue(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public V get(Object rowKey, Object columnKey) { return (V)delegate().get(rowKey, columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public boolean isEmpty() { return delegate().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public V put(R rowKey, C columnKey, V value) { return (V)delegate().put(rowKey, columnKey, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) { delegate().putAll(table); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public V remove(Object rowKey, Object columnKey) { return (V)delegate().remove(rowKey, columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public Map<C, V> row(R rowKey) { return delegate().row(rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public Set<R> rowKeySet() { return delegate().rowKeySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public Map<R, Map<C, V>> rowMap() { return delegate().rowMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public int size() { return delegate().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public Collection<V> values() { return delegate().values(); }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public boolean equals(Object obj) { return (obj == this || delegate().equals(obj)); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public int hashCode() { return delegate().hashCode(); }
/*     */   
/*     */   protected abstract Table<R, C, V> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */