/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class LinkedHashMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_KEY_CAPACITY = 16;
/*     */   private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
/*     */   @VisibleForTesting
/*     */   static final double VALUE_SET_LOAD_FACTOR = 1.0D;
/*     */   
/*  89 */   public static <K, V> LinkedHashMultimap<K, V> create() { return new LinkedHashMultimap(16, 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) { return new LinkedHashMultimap(Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 119 */     LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
/* 120 */     result.putAll(multimap);
/* 121 */     return result;
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
/*     */   private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> pred, ValueSetLink<K, V> succ) {
/* 133 */     pred.setSuccessorInValueSet(succ);
/* 134 */     succ.setPredecessorInValueSet(pred);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> void succeedsInMultimap(ValueEntry<K, V> pred, ValueEntry<K, V> succ) {
/* 139 */     pred.setSuccessorInMultimap(succ);
/* 140 */     succ.setPredecessorInMultimap(pred);
/*     */   }
/*     */ 
/*     */   
/* 144 */   private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> entry) { succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet()); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   private static <K, V> void deleteFromMultimap(ValueEntry<K, V> entry) { succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap()); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class ValueEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     final int smearedValueHash;
/*     */     
/*     */     @Nullable
/*     */     ValueEntry<K, V> nextInValueBucket;
/*     */     
/*     */     LinkedHashMultimap.ValueSetLink<K, V> predecessorInValueSet;
/*     */     
/*     */     LinkedHashMultimap.ValueSetLink<K, V> successorInValueSet;
/*     */     
/*     */     ValueEntry<K, V> predecessorInMultimap;
/*     */     
/*     */     ValueEntry<K, V> successorInMultimap;
/*     */ 
/*     */     
/*     */     ValueEntry(@Nullable K key, @Nullable V value, int smearedValueHash, @Nullable ValueEntry<K, V> nextInValueBucket) {
/* 172 */       super(key, value);
/* 173 */       this.smearedValueHash = smearedValueHash;
/* 174 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */ 
/*     */     
/* 178 */     boolean matchesValue(@Nullable Object v, int smearedVHash) { return (this.smearedValueHash == smearedVHash && Objects.equal(getValue(), v)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() { return this.predecessorInValueSet; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() { return this.successorInValueSet; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) { this.predecessorInValueSet = entry; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) { this.successorInValueSet = entry; }
/*     */ 
/*     */ 
/*     */     
/* 202 */     public ValueEntry<K, V> getPredecessorInMultimap() { return this.predecessorInMultimap; }
/*     */ 
/*     */ 
/*     */     
/* 206 */     public ValueEntry<K, V> getSuccessorInMultimap() { return this.successorInMultimap; }
/*     */ 
/*     */ 
/*     */     
/* 210 */     public void setSuccessorInMultimap(ValueEntry<K, V> multimapSuccessor) { this.successorInMultimap = multimapSuccessor; }
/*     */ 
/*     */ 
/*     */     
/* 214 */     public void setPredecessorInMultimap(ValueEntry<K, V> multimapPredecessor) { this.predecessorInMultimap = multimapPredecessor; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 222 */   int valueSetCapacity = 2;
/*     */   private ValueEntry<K, V> multimapHeaderEntry;
/*     */   
/*     */   private LinkedHashMultimap(int keyCapacity, int valueSetCapacity) {
/* 226 */     super(new LinkedHashMap(keyCapacity));
/* 227 */     CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
/*     */     
/* 229 */     this.valueSetCapacity = valueSetCapacity;
/* 230 */     this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
/* 231 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java serialization not supported")
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   Set<V> createCollection() { return new LinkedHashSet(this.valueSetCapacity); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 259 */   Collection<V> createCollection(K key) { return new ValueSet(key, this.valueSetCapacity); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) { return super.replaceValues(key, values); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 288 */   public Set<Map.Entry<K, V>> entries() { return super.entries(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 299 */   public Collection<V> values() { return super.values(); }
/*     */   
/*     */   @VisibleForTesting
/*     */   final class ValueSet
/*     */     extends Sets.ImprovedAbstractSet<V>
/*     */     implements ValueSetLink<K, V> {
/*     */     private final K key;
/*     */     @VisibleForTesting
/*     */     LinkedHashMultimap.ValueEntry<K, V>[] hashTable;
/*     */     private int size;
/*     */     
/*     */     ValueSet(K key, int expectedValues) {
/* 311 */       this.size = 0;
/* 312 */       this.modCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 320 */       this.key = key;
/* 321 */       this.firstEntry = this;
/* 322 */       this.lastEntry = this;
/*     */       
/* 324 */       int tableSize = Hashing.closedTableSize(expectedValues, 1.0D);
/*     */ 
/*     */       
/* 327 */       LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[tableSize];
/* 328 */       this.hashTable = arrayOfValueEntry;
/*     */     }
/*     */     private int modCount; private LinkedHashMultimap.ValueSetLink<K, V> firstEntry; private LinkedHashMultimap.ValueSetLink<K, V> lastEntry;
/*     */     
/* 332 */     private int mask() { return this.hashTable.length - 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() { return this.lastEntry; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() { return this.firstEntry; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) { this.lastEntry = entry; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 352 */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) { this.firstEntry = entry; }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 357 */       return new Iterator<V>() {
/*     */           LinkedHashMultimap.ValueSetLink<K, V> nextEntry;
/*     */           LinkedHashMultimap.ValueEntry<K, V> toRemove;
/*     */           int expectedModCount;
/*     */           
/*     */           private void checkForComodification() {
/* 363 */             if (super.this$1.modCount != super.expectedModCount) {
/* 364 */               throw new ConcurrentModificationException();
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 370 */             super.checkForComodification();
/* 371 */             return (super.nextEntry != super.this$1);
/*     */           }
/*     */ 
/*     */           
/*     */           public V next() {
/* 376 */             if (!super.hasNext()) {
/* 377 */               throw new NoSuchElementException();
/*     */             }
/* 379 */             LinkedHashMultimap.ValueEntry<K, V> entry = (LinkedHashMultimap.ValueEntry)super.nextEntry;
/* 380 */             V result = (V)entry.getValue();
/* 381 */             super.toRemove = entry;
/* 382 */             super.nextEntry = entry.getSuccessorInValueSet();
/* 383 */             return result;
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 388 */             super.checkForComodification();
/* 389 */             CollectPreconditions.checkRemove((super.toRemove != null));
/* 390 */             super.this$1.remove(super.toRemove.getValue());
/* 391 */             super.expectedModCount = super.this$1.modCount;
/* 392 */             super.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 399 */     public int size() { return this.size; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object o) {
/* 404 */       int smearedHash = Hashing.smearedHash(o);
/* 405 */       for (LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[smearedHash & mask()]; entry != null; 
/* 406 */         entry = entry.nextInValueBucket) {
/* 407 */         if (entry.matchesValue(o, smearedHash)) {
/* 408 */           return true;
/*     */         }
/*     */       } 
/* 411 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(@Nullable V value) {
/* 416 */       int smearedHash = Hashing.smearedHash(value);
/* 417 */       int bucket = smearedHash & mask();
/* 418 */       LinkedHashMultimap.ValueEntry<K, V> rowHead = this.hashTable[bucket];
/* 419 */       for (LinkedHashMultimap.ValueEntry<K, V> entry = rowHead; entry != null; 
/* 420 */         entry = entry.nextInValueBucket) {
/* 421 */         if (entry.matchesValue(value, smearedHash)) {
/* 422 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 426 */       LinkedHashMultimap.ValueEntry<K, V> newEntry = new LinkedHashMultimap.ValueEntry<K, V>(this.key, value, smearedHash, rowHead);
/* 427 */       LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
/* 428 */       LinkedHashMultimap.succeedsInValueSet(newEntry, this);
/* 429 */       LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
/* 430 */       LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
/* 431 */       this.hashTable[bucket] = newEntry;
/* 432 */       this.size++;
/* 433 */       this.modCount++;
/* 434 */       rehashIfNecessary();
/* 435 */       return true;
/*     */     }
/*     */     
/*     */     private void rehashIfNecessary() {
/* 439 */       if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
/*     */         
/* 441 */         LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
/* 442 */         this.hashTable = arrayOfValueEntry;
/* 443 */         int mask = arrayOfValueEntry.length - 1;
/* 444 */         LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 445 */         for (; entry != this; entry = entry.getSuccessorInValueSet()) {
/* 446 */           LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry)entry;
/* 447 */           int bucket = valueEntry.smearedValueHash & mask;
/* 448 */           valueEntry.nextInValueBucket = arrayOfValueEntry[bucket];
/* 449 */           arrayOfValueEntry[bucket] = valueEntry;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@Nullable Object o) {
/* 456 */       int smearedHash = Hashing.smearedHash(o);
/* 457 */       int bucket = smearedHash & mask();
/* 458 */       LinkedHashMultimap.ValueEntry<K, V> prev = null;
/* 459 */       for (LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[bucket]; entry != null; 
/* 460 */         prev = entry, entry = entry.nextInValueBucket) {
/* 461 */         if (entry.matchesValue(o, smearedHash)) {
/* 462 */           if (prev == null) {
/*     */             
/* 464 */             this.hashTable[bucket] = entry.nextInValueBucket;
/*     */           } else {
/* 466 */             prev.nextInValueBucket = entry.nextInValueBucket;
/*     */           } 
/* 468 */           LinkedHashMultimap.deleteFromValueSet(entry);
/* 469 */           LinkedHashMultimap.deleteFromMultimap(entry);
/* 470 */           this.size--;
/* 471 */           this.modCount++;
/* 472 */           return true;
/*     */         } 
/*     */       } 
/* 475 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 480 */       Arrays.fill(this.hashTable, null);
/* 481 */       this.size = 0;
/* 482 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 483 */       for (; entry != this; entry = entry.getSuccessorInValueSet()) {
/* 484 */         LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry)entry;
/* 485 */         LinkedHashMultimap.deleteFromMultimap(valueEntry);
/*     */       } 
/* 487 */       LinkedHashMultimap.succeedsInValueSet(this, this);
/* 488 */       this.modCount++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 494 */     return new Iterator<Map.Entry<K, V>>()
/*     */       {
/*     */         LinkedHashMultimap.ValueEntry<K, V> nextEntry;
/*     */         
/*     */         LinkedHashMultimap.ValueEntry<K, V> toRemove;
/*     */         
/* 500 */         public boolean hasNext() { return (super.nextEntry != super.this$0.multimapHeaderEntry); }
/*     */ 
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 505 */           if (!super.hasNext()) {
/* 506 */             throw new NoSuchElementException();
/*     */           }
/* 508 */           LinkedHashMultimap.ValueEntry<K, V> result = super.nextEntry;
/* 509 */           super.toRemove = result;
/* 510 */           super.nextEntry = super.nextEntry.successorInMultimap;
/* 511 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 516 */           CollectPreconditions.checkRemove((super.toRemove != null));
/* 517 */           super.this$0.remove(super.toRemove.getKey(), super.toRemove.getValue());
/* 518 */           super.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 525 */   Iterator<V> valueIterator() { return Maps.valueIterator(entryIterator()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 530 */     super.clear();
/* 531 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 540 */     stream.defaultWriteObject();
/* 541 */     stream.writeInt(this.valueSetCapacity);
/* 542 */     stream.writeInt(keySet().size());
/* 543 */     for (K key : keySet()) {
/* 544 */       stream.writeObject(key);
/*     */     }
/* 546 */     stream.writeInt(size());
/* 547 */     for (Map.Entry<K, V> entry : entries()) {
/* 548 */       stream.writeObject(entry.getKey());
/* 549 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 556 */     stream.defaultReadObject();
/* 557 */     this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
/* 558 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/* 559 */     this.valueSetCapacity = stream.readInt();
/* 560 */     int distinctKeys = stream.readInt();
/* 561 */     Map<K, Collection<V>> map = new LinkedHashMap<K, Collection<V>>(Maps.capacity(distinctKeys));
/*     */     
/* 563 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 565 */       K key = (K)stream.readObject();
/* 566 */       map.put(key, createCollection(key));
/*     */     } 
/* 568 */     int entries = stream.readInt();
/* 569 */     for (int i = 0; i < entries; i++) {
/*     */       
/* 571 */       K key = (K)stream.readObject();
/*     */       
/* 573 */       V value = (V)stream.readObject();
/* 574 */       ((Collection)map.get(key)).add(value);
/*     */     } 
/* 576 */     setMap(map);
/*     */   }
/*     */   
/*     */   private static interface ValueSetLink<K, V> {
/*     */     ValueSetLink<K, V> getPredecessorInValueSet();
/*     */     
/*     */     ValueSetLink<K, V> getSuccessorInValueSet();
/*     */     
/*     */     void setPredecessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */     
/*     */     void setSuccessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/LinkedHashMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */