/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   private final ImmutableMapEntry<K, V>[] entries;
/*     */   private final ImmutableMapEntry<K, V>[] table;
/*     */   private final int mask;
/*     */   private static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  44 */   RegularImmutableMap(TerminalEntry... theEntries) { this(theEntries.length, theEntries); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableMap(int size, TerminalEntry[] theEntries) {
/*  54 */     this.entries = createEntryArray(size);
/*  55 */     int tableSize = Hashing.closedTableSize(size, 1.2D);
/*  56 */     this.table = createEntryArray(tableSize);
/*  57 */     this.mask = tableSize - 1;
/*  58 */     for (int entryIndex = 0; entryIndex < size; entryIndex++) {
/*     */       
/*  60 */       ImmutableMapEntry.TerminalEntry<K, V> entry = theEntries[entryIndex];
/*  61 */       K key = (K)entry.getKey();
/*  62 */       int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
/*  63 */       ImmutableMapEntry<K, V> existing = this.table[tableIndex];
/*     */       
/*  65 */       ImmutableMapEntry<K, V> newEntry = (existing == null) ? entry : new NonTerminalMapEntry(entry, existing);
/*     */ 
/*     */       
/*  68 */       this.table[tableIndex] = newEntry;
/*  69 */       this.entries[entryIndex] = newEntry;
/*  70 */       checkNoConflictInBucket(key, newEntry, existing);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableMap(Entry[] theEntries) {
/*  78 */     int size = theEntries.length;
/*  79 */     this.entries = createEntryArray(size);
/*  80 */     int tableSize = Hashing.closedTableSize(size, 1.2D);
/*  81 */     this.table = createEntryArray(tableSize);
/*  82 */     this.mask = tableSize - 1;
/*  83 */     for (int entryIndex = 0; entryIndex < size; entryIndex++) {
/*     */       
/*  85 */       Map.Entry<K, V> entry = theEntries[entryIndex];
/*  86 */       K key = (K)entry.getKey();
/*  87 */       V value = (V)entry.getValue();
/*  88 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  89 */       int tableIndex = Hashing.smear(key.hashCode()) & this.mask;
/*  90 */       ImmutableMapEntry<K, V> existing = this.table[tableIndex];
/*     */       
/*  92 */       ImmutableMapEntry<K, V> newEntry = (existing == null) ? new ImmutableMapEntry.TerminalEntry(key, value) : new NonTerminalMapEntry(key, value, existing);
/*     */ 
/*     */       
/*  95 */       this.table[tableIndex] = newEntry;
/*  96 */       this.entries[entryIndex] = newEntry;
/*  97 */       checkNoConflictInBucket(key, newEntry, existing);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkNoConflictInBucket(K key, ImmutableMapEntry<K, V> entry, ImmutableMapEntry<K, V> bucketHead) {
/* 103 */     for (; bucketHead != null; bucketHead = bucketHead.getNextInKeyBucket())
/* 104 */       checkNoConflict(!key.equals(bucketHead.getKey()), "key", entry, bucketHead); 
/*     */   }
/*     */   
/*     */   private static final class NonTerminalMapEntry<K, V>
/*     */     extends ImmutableMapEntry<K, V> {
/*     */     private final ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */     
/*     */     NonTerminalMapEntry(K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket) {
/* 112 */       super(key, value);
/* 113 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */     
/*     */     NonTerminalMapEntry(ImmutableMapEntry<K, V> contents, ImmutableMapEntry<K, V> nextInKeyBucket) {
/* 117 */       super(contents);
/* 118 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 123 */     ImmutableMapEntry<K, V> getNextInKeyBucket() { return this.nextInKeyBucket; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 129 */     ImmutableMapEntry<K, V> getNextInValueBucket() { return null; }
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
/* 148 */   private ImmutableMapEntry<K, V>[] createEntryArray(int size) { return new ImmutableMapEntry[size]; }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 152 */     if (key == null) {
/* 153 */       return null;
/*     */     }
/* 155 */     int index = Hashing.smear(key.hashCode()) & this.mask;
/* 156 */     ImmutableMapEntry<K, V> entry = this.table[index];
/* 157 */     for (; entry != null; 
/* 158 */       entry = entry.getNextInKeyBucket()) {
/* 159 */       K candidateKey = (K)entry.getKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       if (key.equals(candidateKey)) {
/* 168 */         return (V)entry.getValue();
/*     */       }
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int size() { return this.entries.length; }
/*     */ 
/*     */ 
/*     */   
/* 180 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   ImmutableSet<Map.Entry<K, V>> createEntrySet() { return new EntrySet(null); }
/*     */   
/*     */   private class EntrySet
/*     */     extends ImmutableMapEntrySet<K, V> {
/*     */     private EntrySet() {}
/*     */     
/* 191 */     ImmutableMap<K, V> map() { return RegularImmutableMap.this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() { return asList().iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     ImmutableList<Map.Entry<K, V>> createAsList() { return new RegularImmutableAsList(this, RegularImmutableMap.this.entries); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */