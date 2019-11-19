/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
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
/*     */ @GwtCompatible
/*     */ class StandardTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   @GwtTransient
/*     */   final Map<R, Map<C, V>> backingMap;
/*     */   @GwtTransient
/*     */   final Supplier<? extends Map<C, V>> factory;
/*     */   private Set<C> columnKeySet;
/*     */   private Map<R, Map<C, V>> rowMap;
/*     */   private ColumnMap columnMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  73 */     this.backingMap = backingMap;
/*  74 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) { return (rowKey != null && columnKey != null && super.contains(rowKey, columnKey)); }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@Nullable Object columnKey) {
/*  85 */     if (columnKey == null) {
/*  86 */       return false;
/*     */     }
/*  88 */     for (Map<C, V> map : this.backingMap.values()) {
/*  89 */       if (Maps.safeContainsKey(map, columnKey)) {
/*  90 */         return true;
/*     */       }
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  97 */   public boolean containsRow(@Nullable Object rowKey) { return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey)); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean containsValue(@Nullable Object value) { return (value != null && super.containsValue(value)); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) { return (V)((rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public boolean isEmpty() { return this.backingMap.isEmpty(); }
/*     */ 
/*     */   
/*     */   public int size() {
/* 115 */     int size = 0;
/* 116 */     for (Map<C, V> map : this.backingMap.values()) {
/* 117 */       size += map.size();
/*     */     }
/* 119 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public void clear() { this.backingMap.clear(); }
/*     */ 
/*     */   
/*     */   private Map<C, V> getOrCreate(R rowKey) {
/* 129 */     Map<C, V> map = (Map)this.backingMap.get(rowKey);
/* 130 */     if (map == null) {
/* 131 */       map = (Map)this.factory.get();
/* 132 */       this.backingMap.put(rowKey, map);
/*     */     } 
/* 134 */     return map;
/*     */   }
/*     */   
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 138 */     Preconditions.checkNotNull(rowKey);
/* 139 */     Preconditions.checkNotNull(columnKey);
/* 140 */     Preconditions.checkNotNull(value);
/* 141 */     return (V)getOrCreate(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 146 */     if (rowKey == null || columnKey == null) {
/* 147 */       return null;
/*     */     }
/* 149 */     Map<C, V> map = (Map)Maps.safeGet(this.backingMap, rowKey);
/* 150 */     if (map == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     V value = (V)map.remove(columnKey);
/* 154 */     if (map.isEmpty()) {
/* 155 */       this.backingMap.remove(rowKey);
/*     */     }
/* 157 */     return value;
/*     */   }
/*     */   
/*     */   private Map<R, V> removeColumn(Object column) {
/* 161 */     Map<R, V> output = new LinkedHashMap<R, V>();
/* 162 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/*     */     
/* 164 */     while (iterator.hasNext()) {
/* 165 */       Map.Entry<R, Map<C, V>> entry = (Map.Entry)iterator.next();
/* 166 */       V value = (V)((Map)entry.getValue()).remove(column);
/* 167 */       if (value != null) {
/* 168 */         output.put(entry.getKey(), value);
/* 169 */         if (((Map)entry.getValue()).isEmpty()) {
/* 170 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/* 174 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 179 */   private boolean containsMapping(Object rowKey, Object columnKey, Object value) { return (value != null && value.equals(get(rowKey, columnKey))); }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
/* 184 */     if (containsMapping(rowKey, columnKey, value)) {
/* 185 */       remove(rowKey, columnKey);
/* 186 */       return true;
/*     */     } 
/* 188 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class TableSet<T>
/*     */     extends Sets.ImprovedAbstractSet<T>
/*     */   {
/*     */     private TableSet() {}
/*     */ 
/*     */     
/* 199 */     public boolean isEmpty() { return StandardTable.this.backingMap.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/* 203 */     public void clear() { StandardTable.this.backingMap.clear(); }
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
/* 218 */   public Set<Table.Cell<R, C, V>> cellSet() { return super.cellSet(); }
/*     */ 
/*     */ 
/*     */   
/* 222 */   Iterator<Table.Cell<R, C, V>> cellIterator() { return new CellIterator(null); }
/*     */   
/*     */   private class CellIterator
/*     */     extends Object implements Iterator<Table.Cell<R, C, V>> {
/* 226 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */     
/*     */     Map.Entry<R, Map<C, V>> rowEntry;
/* 229 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*     */ 
/*     */ 
/*     */     
/* 233 */     public boolean hasNext() { return (this.rowIterator.hasNext() || this.columnIterator.hasNext()); }
/*     */ 
/*     */     
/*     */     public Table.Cell<R, C, V> next() {
/* 237 */       if (!this.columnIterator.hasNext()) {
/* 238 */         this.rowEntry = (Map.Entry)this.rowIterator.next();
/* 239 */         this.columnIterator = ((Map)this.rowEntry.getValue()).entrySet().iterator();
/*     */       } 
/* 241 */       Map.Entry<C, V> columnEntry = (Map.Entry)this.columnIterator.next();
/* 242 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 247 */       this.columnIterator.remove();
/* 248 */       if (((Map)this.rowEntry.getValue()).isEmpty())
/* 249 */         this.rowIterator.remove(); 
/*     */     }
/*     */     
/*     */     private CellIterator() {}
/*     */   }
/*     */   
/* 255 */   public Map<C, V> row(R rowKey) { return new Row(rowKey); }
/*     */   
/*     */   class Row
/*     */     extends Maps.ImprovedAbstractMap<C, V> {
/*     */     final R rowKey;
/*     */     Map<C, V> backingRowMap;
/*     */     
/* 262 */     Row(R rowKey) { this.rowKey = Preconditions.checkNotNull(rowKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     Map<C, V> backingRowMap() { return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this.backingRowMap = computeBackingRowMap()) : this.backingRowMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     Map<C, V> computeBackingRowMap() { return (Map)StandardTable.this.backingMap.get(this.rowKey); }
/*     */ 
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 280 */       if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
/* 281 */         StandardTable.this.backingMap.remove(this.rowKey);
/* 282 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 288 */       Map<C, V> backingRowMap = backingRowMap();
/* 289 */       return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 295 */       Map<C, V> backingRowMap = backingRowMap();
/* 296 */       return (V)((key != null && backingRowMap != null) ? Maps.safeGet(backingRowMap, key) : null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 303 */       Preconditions.checkNotNull(key);
/* 304 */       Preconditions.checkNotNull(value);
/* 305 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/* 306 */         return (V)this.backingRowMap.put(key, value);
/*     */       }
/* 308 */       return (V)StandardTable.this.put(this.rowKey, key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 313 */       Map<C, V> backingRowMap = backingRowMap();
/* 314 */       if (backingRowMap == null) {
/* 315 */         return null;
/*     */       }
/* 317 */       V result = (V)Maps.safeRemove(backingRowMap, key);
/* 318 */       maintainEmptyInvariant();
/* 319 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 324 */       Map<C, V> backingRowMap = backingRowMap();
/* 325 */       if (backingRowMap != null) {
/* 326 */         backingRowMap.clear();
/*     */       }
/* 328 */       maintainEmptyInvariant();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 333 */     protected Set<Map.Entry<C, V>> createEntrySet() { return new RowEntrySet(null); }
/*     */     
/*     */     private final class RowEntrySet
/*     */       extends Maps.EntrySet<C, V> {
/*     */       private RowEntrySet() {}
/*     */       
/* 339 */       Map<C, V> map() { return super.this$1; }
/*     */ 
/*     */ 
/*     */       
/*     */       public int size() {
/* 344 */         Map<C, V> map = super.this$1.backingRowMap();
/* 345 */         return (map == null) ? 0 : map.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<Map.Entry<C, V>> iterator() {
/* 350 */         Map<C, V> map = super.this$1.backingRowMap();
/* 351 */         if (map == null) {
/* 352 */           return Iterators.emptyModifiableIterator();
/*     */         }
/* 354 */         final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
/* 355 */         return new Iterator<Map.Entry<C, V>>()
/*     */           {
/* 357 */             public boolean hasNext() { return iterator.hasNext(); }
/*     */             
/*     */             public Map.Entry<C, V> next() {
/* 360 */               final Map.Entry<C, V> entry = (Map.Entry)iterator.next();
/* 361 */               return new ForwardingMapEntry<C, V>()
/*     */                 {
/* 363 */                   protected Map.Entry<C, V> delegate() { return entry; }
/*     */ 
/*     */                   
/* 366 */                   public V setValue(V value) { return (V)super.setValue(Preconditions.checkNotNull(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 371 */                   public boolean equals(Object object) { return standardEquals(object); }
/*     */                 };
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void remove() {
/* 378 */               iterator.remove();
/* 379 */               StandardTable.Row.RowEntrySet.this.this$1.maintainEmptyInvariant();
/*     */             }
/*     */           };
/*     */       }
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
/* 393 */   public Map<R, V> column(C columnKey) { return new Column(columnKey); }
/*     */   
/*     */   private class Column
/*     */     extends Maps.ImprovedAbstractMap<R, V>
/*     */   {
/*     */     final C columnKey;
/*     */     
/* 400 */     Column(C columnKey) { this.columnKey = Preconditions.checkNotNull(columnKey); }
/*     */ 
/*     */ 
/*     */     
/* 404 */     public V put(R key, V value) { return (V)StandardTable.this.put(key, this.columnKey, value); }
/*     */ 
/*     */ 
/*     */     
/* 408 */     public V get(Object key) { return (V)StandardTable.this.get(key, this.columnKey); }
/*     */ 
/*     */ 
/*     */     
/* 412 */     public boolean containsKey(Object key) { return StandardTable.this.contains(key, this.columnKey); }
/*     */ 
/*     */ 
/*     */     
/* 416 */     public V remove(Object key) { return (V)StandardTable.this.remove(key, this.columnKey); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> predicate) {
/* 424 */       boolean changed = false;
/* 425 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */       
/* 427 */       while (iterator.hasNext()) {
/* 428 */         Map.Entry<R, Map<C, V>> entry = (Map.Entry)iterator.next();
/* 429 */         Map<C, V> map = (Map)entry.getValue();
/* 430 */         V value = (V)map.get(this.columnKey);
/* 431 */         if (value != null && predicate.apply(Maps.immutableEntry(entry.getKey(), value))) {
/*     */           
/* 433 */           map.remove(this.columnKey);
/* 434 */           changed = true;
/* 435 */           if (map.isEmpty()) {
/* 436 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 440 */       return changed;
/*     */     }
/*     */ 
/*     */     
/* 444 */     Set<Map.Entry<R, V>> createEntrySet() { return new EntrySet(null); }
/*     */     
/*     */     private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
/*     */       private EntrySet() {}
/*     */       
/* 449 */       public Iterator<Map.Entry<R, V>> iterator() { return new StandardTable.Column.EntrySetIterator(super.this$1, null); }
/*     */ 
/*     */       
/*     */       public int size() {
/* 453 */         int size = 0;
/* 454 */         for (Map<C, V> map : super.this$1.this$0.backingMap.values()) {
/* 455 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/* 456 */             size++;
/*     */           }
/*     */         } 
/* 459 */         return size;
/*     */       }
/*     */ 
/*     */       
/* 463 */       public boolean isEmpty() { return !StandardTable.Column.this.this$0.containsColumn(StandardTable.Column.this.columnKey); }
/*     */ 
/*     */ 
/*     */       
/* 467 */       public void clear() { super.this$1.removeFromColumnIf(Predicates.alwaysTrue()); }
/*     */ 
/*     */       
/*     */       public boolean contains(Object o) {
/* 471 */         if (o instanceof Map.Entry) {
/* 472 */           Map.Entry<?, ?> entry = (Map.Entry)o;
/* 473 */           return StandardTable.Column.this.this$0.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 475 */         return false;
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 479 */         if (obj instanceof Map.Entry) {
/* 480 */           Map.Entry<?, ?> entry = (Map.Entry)obj;
/* 481 */           return StandardTable.Column.this.this$0.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 483 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 487 */       public boolean retainAll(Collection<?> c) { return super.this$1.removeFromColumnIf(Predicates.not(Predicates.in(c))); } }
/*     */     
/*     */     private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/*     */       final Iterator<Map.Entry<R, Map<C, V>>> iterator;
/*     */       
/* 492 */       private EntrySetIterator() { super.iterator = super.this$1.this$0.backingMap.entrySet().iterator(); }
/*     */       
/*     */       protected Map.Entry<R, V> computeNext() {
/* 495 */         while (super.iterator.hasNext()) {
/* 496 */           final Map.Entry<R, Map<C, V>> entry = (Map.Entry)super.iterator.next();
/* 497 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey)) {
/* 498 */             return new AbstractMapEntry<R, V>()
/*     */               {
/* 500 */                 public R getKey() { return (R)entry.getKey(); }
/*     */ 
/*     */                 
/* 503 */                 public V getValue() { return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey); }
/*     */                 
/*     */                 public V setValue(V value) {
/* 506 */                   return (V)((Map)entry.getValue()).put(StandardTable.Column.this.columnKey, Preconditions.checkNotNull(value));
/*     */                 }
/*     */               };
/*     */           }
/*     */         } 
/* 511 */         return (Map.Entry)endOfData();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 516 */     Set<R> createKeySet() { return new KeySet(); }
/*     */     
/*     */     private class KeySet
/*     */       extends Maps.KeySet<R, V> {
/*     */       KeySet() {
/* 521 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/* 525 */       public boolean contains(Object obj) { return StandardTable.Column.this.this$0.contains(obj, StandardTable.Column.this.columnKey); }
/*     */ 
/*     */ 
/*     */       
/* 529 */       public boolean remove(Object obj) { return (StandardTable.Column.this.this$0.remove(obj, StandardTable.Column.this.columnKey) != null); }
/*     */ 
/*     */ 
/*     */       
/* 533 */       public boolean retainAll(Collection<?> c) { return super.this$1.removeFromColumnIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c)))); }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 539 */     Collection<V> createValues() { return new Values(); }
/*     */     
/*     */     private class Values
/*     */       extends Maps.Values<R, V> {
/*     */       Values() {
/* 544 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/* 548 */       public boolean remove(Object obj) { return (obj != null && super.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.equalTo(obj)))); }
/*     */ 
/*     */ 
/*     */       
/* 552 */       public boolean removeAll(Collection<?> c) { return super.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.in(c))); }
/*     */ 
/*     */ 
/*     */       
/* 556 */       public boolean retainAll(Collection<?> c) { return super.this$1.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))); }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 562 */   public Set<R> rowKeySet() { return rowMap().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<C> columnKeySet() {
/* 578 */     Set<C> result = this.columnKeySet;
/* 579 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet(null)) : result;
/*     */   }
/*     */   
/* 582 */   private class ColumnKeySet extends TableSet<C> { private ColumnKeySet() { super(StandardTable.this, null); }
/*     */     
/* 584 */     public Iterator<C> iterator() { return StandardTable.this.createColumnKeyIterator(); }
/*     */ 
/*     */ 
/*     */     
/* 588 */     public int size() { return Iterators.size(iterator()); }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 592 */       if (obj == null) {
/* 593 */         return false;
/*     */       }
/* 595 */       boolean changed = false;
/* 596 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 597 */       while (iterator.hasNext()) {
/* 598 */         Map<C, V> map = (Map)iterator.next();
/* 599 */         if (map.keySet().remove(obj)) {
/* 600 */           changed = true;
/* 601 */           if (map.isEmpty()) {
/* 602 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 606 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 610 */       Preconditions.checkNotNull(c);
/* 611 */       boolean changed = false;
/* 612 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 613 */       while (iterator.hasNext()) {
/* 614 */         Map<C, V> map = (Map)iterator.next();
/*     */ 
/*     */         
/* 617 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/* 618 */           changed = true;
/* 619 */           if (map.isEmpty()) {
/* 620 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 624 */       return changed;
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 628 */       Preconditions.checkNotNull(c);
/* 629 */       boolean changed = false;
/* 630 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 631 */       while (iterator.hasNext()) {
/* 632 */         Map<C, V> map = (Map)iterator.next();
/* 633 */         if (map.keySet().retainAll(c)) {
/* 634 */           changed = true;
/* 635 */           if (map.isEmpty()) {
/* 636 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 640 */       return changed;
/*     */     }
/*     */ 
/*     */     
/* 644 */     public boolean contains(Object obj) { return StandardTable.this.containsColumn(obj); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 653 */   Iterator<C> createColumnKeyIterator() { return new ColumnKeyIterator(null); }
/*     */ 
/*     */   
/*     */   private class ColumnKeyIterator
/*     */     extends AbstractIterator<C>
/*     */   {
/* 659 */     final Map<C, V> seen = (Map)StandardTable.this.factory.get();
/* 660 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/* 661 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*     */     
/*     */     protected C computeNext() {
/*     */       while (true) {
/* 665 */         while (this.entryIterator.hasNext()) {
/* 666 */           Map.Entry<C, V> entry = (Map.Entry)this.entryIterator.next();
/* 667 */           if (!this.seen.containsKey(entry.getKey())) {
/* 668 */             this.seen.put(entry.getKey(), entry.getValue());
/* 669 */             return (C)entry.getKey();
/*     */           } 
/* 671 */         }  if (this.mapIterator.hasNext()) {
/* 672 */           this.entryIterator = ((Map)this.mapIterator.next()).entrySet().iterator(); continue;
/*     */         }  break;
/* 674 */       }  return (C)endOfData();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ColumnKeyIterator() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 687 */   public Collection<V> values() { return super.values(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 693 */     Map<R, Map<C, V>> result = this.rowMap;
/* 694 */     return (result == null) ? (this.rowMap = createRowMap()) : result;
/*     */   }
/*     */ 
/*     */   
/* 698 */   Map<R, Map<C, V>> createRowMap() { return new RowMap(); }
/*     */   
/*     */   class RowMap
/*     */     extends Maps.ImprovedAbstractMap<R, Map<C, V>>
/*     */   {
/* 703 */     public boolean containsKey(Object key) { return StandardTable.this.containsRow(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 709 */     public Map<C, V> get(Object key) { return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null; }
/*     */ 
/*     */ 
/*     */     
/* 713 */     public Map<C, V> remove(Object key) { return (key == null) ? null : (Map)StandardTable.this.backingMap.remove(key); }
/*     */ 
/*     */ 
/*     */     
/* 717 */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() { return new EntrySet(); }
/*     */     
/*     */     class EntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>> {
/* 720 */       EntrySet() { super(StandardTable.RowMap.this.this$0, null); }
/*     */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/* 722 */         return Maps.asMapEntryIterator(super.this$1.this$0.backingMap.keySet(), new Function<R, Map<C, V>>()
/*     */             {
/*     */               public Map<C, V> apply(R rowKey) {
/* 725 */                 return StandardTable.RowMap.this.this$0.row(rowKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/* 731 */       public int size() { return super.this$1.this$0.backingMap.size(); }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 735 */         if (obj instanceof Map.Entry) {
/* 736 */           Map.Entry<?, ?> entry = (Map.Entry)obj;
/* 737 */           return (entry.getKey() != null && entry.getValue() instanceof Map && Collections2.safeContains(super.this$1.this$0.backingMap.entrySet(), entry));
/*     */         } 
/*     */ 
/*     */         
/* 741 */         return false;
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 745 */         if (obj instanceof Map.Entry) {
/* 746 */           Map.Entry<?, ?> entry = (Map.Entry)obj;
/* 747 */           return (entry.getKey() != null && entry.getValue() instanceof Map && super.this$1.this$0.backingMap.entrySet().remove(entry));
/*     */         } 
/*     */ 
/*     */         
/* 751 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 759 */     ColumnMap result = this.columnMap;
/* 760 */     return (result == null) ? (this.columnMap = new ColumnMap(null)) : result;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends Maps.ImprovedAbstractMap<C, Map<R, V>>
/*     */   {
/*     */     private ColumnMap() {}
/*     */     
/* 768 */     public Map<R, V> get(Object key) { return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null; }
/*     */ 
/*     */ 
/*     */     
/* 772 */     public boolean containsKey(Object key) { return StandardTable.this.containsColumn(key); }
/*     */ 
/*     */ 
/*     */     
/* 776 */     public Map<R, V> remove(Object key) { return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null; }
/*     */ 
/*     */ 
/*     */     
/* 780 */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() { return new ColumnMapEntrySet(); }
/*     */ 
/*     */ 
/*     */     
/* 784 */     public Set<C> keySet() { return StandardTable.this.columnKeySet(); }
/*     */ 
/*     */ 
/*     */     
/* 788 */     Collection<Map<R, V>> createValues() { return new ColumnMapValues(); }
/*     */     
/*     */     class ColumnMapEntrySet extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>> {
/* 791 */       ColumnMapEntrySet() { super(StandardTable.ColumnMap.this.this$0, null); }
/*     */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/* 793 */         return Maps.asMapEntryIterator(StandardTable.ColumnMap.this.this$0.columnKeySet(), new Function<C, Map<R, V>>()
/*     */             {
/*     */               public Map<R, V> apply(C columnKey) {
/* 796 */                 return StandardTable.ColumnMap.this.this$0.column(columnKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/* 802 */       public int size() { return StandardTable.ColumnMap.this.this$0.columnKeySet().size(); }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 806 */         if (obj instanceof Map.Entry) {
/* 807 */           Map.Entry<?, ?> entry = (Map.Entry)obj;
/* 808 */           if (StandardTable.ColumnMap.this.this$0.containsColumn(entry.getKey())) {
/*     */ 
/*     */ 
/*     */             
/* 812 */             C columnKey = (C)entry.getKey();
/* 813 */             return super.this$1.get(columnKey).equals(entry.getValue());
/*     */           } 
/*     */         } 
/* 816 */         return false;
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 820 */         if (super.contains(obj)) {
/* 821 */           Map.Entry<?, ?> entry = (Map.Entry)obj;
/* 822 */           StandardTable.ColumnMap.this.this$0.removeColumn(entry.getKey());
/* 823 */           return true;
/*     */         } 
/* 825 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 835 */         Preconditions.checkNotNull(c);
/* 836 */         return Sets.removeAllImpl(this, c.iterator());
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 840 */         Preconditions.checkNotNull(c);
/* 841 */         boolean changed = false;
/* 842 */         for (C columnKey : Lists.newArrayList(StandardTable.ColumnMap.this.this$0.columnKeySet().iterator())) {
/* 843 */           if (!c.contains(Maps.immutableEntry(columnKey, StandardTable.ColumnMap.this.this$0.column(columnKey)))) {
/* 844 */             StandardTable.ColumnMap.this.this$0.removeColumn(columnKey);
/* 845 */             changed = true;
/*     */           } 
/*     */         } 
/* 848 */         return changed;
/*     */       }
/*     */     }
/*     */     
/*     */     private class ColumnMapValues extends Maps.Values<C, Map<R, V>> {
/*     */       ColumnMapValues() {
/* 854 */         super(StandardTable.ColumnMap.this);
/*     */       }
/*     */       
/*     */       public boolean remove(Object obj) {
/* 858 */         for (Map.Entry<C, Map<R, V>> entry : super.this$1.entrySet()) {
/* 859 */           if (((Map)entry.getValue()).equals(obj)) {
/* 860 */             StandardTable.ColumnMap.this.this$0.removeColumn(entry.getKey());
/* 861 */             return true;
/*     */           } 
/*     */         } 
/* 864 */         return false;
/*     */       }
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 868 */         Preconditions.checkNotNull(c);
/* 869 */         boolean changed = false;
/* 870 */         for (C columnKey : Lists.newArrayList(StandardTable.ColumnMap.this.this$0.columnKeySet().iterator())) {
/* 871 */           if (c.contains(StandardTable.ColumnMap.this.this$0.column(columnKey))) {
/* 872 */             StandardTable.ColumnMap.this.this$0.removeColumn(columnKey);
/* 873 */             changed = true;
/*     */           } 
/*     */         } 
/* 876 */         return changed;
/*     */       }
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 880 */         Preconditions.checkNotNull(c);
/* 881 */         boolean changed = false;
/* 882 */         for (C columnKey : Lists.newArrayList(StandardTable.ColumnMap.this.this$0.columnKeySet().iterator())) {
/* 883 */           if (!c.contains(StandardTable.ColumnMap.this.this$0.column(columnKey))) {
/* 884 */             StandardTable.ColumnMap.this.this$0.removeColumn(columnKey);
/* 885 */             changed = true;
/*     */           } 
/*     */         } 
/* 888 */         return changed;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/StandardTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */