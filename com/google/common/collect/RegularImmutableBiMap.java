/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private final ImmutableMapEntry<K, V>[] keyTable;
/*     */   private final ImmutableMapEntry<K, V>[] valueTable;
/*     */   private final ImmutableMapEntry<K, V>[] entries;
/*     */   private final int mask;
/*     */   private final int hashCode;
/*     */   private ImmutableBiMap<V, K> inverse;
/*     */   
/*  46 */   RegularImmutableBiMap(TerminalEntry... entriesToAdd) { this(entriesToAdd.length, entriesToAdd); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableBiMap(int n, TerminalEntry[] entriesToAdd) {
/*  56 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  57 */     this.mask = tableSize - 1;
/*  58 */     ImmutableMapEntry[] keyTable = createEntryArray(tableSize);
/*  59 */     ImmutableMapEntry[] valueTable = createEntryArray(tableSize);
/*  60 */     ImmutableMapEntry[] entries = createEntryArray(n);
/*  61 */     int hashCode = 0;
/*     */     
/*  63 */     for (int i = 0; i < n; i++) {
/*     */       
/*  65 */       ImmutableMapEntry.TerminalEntry<K, V> entry = entriesToAdd[i];
/*  66 */       K key = (K)entry.getKey();
/*  67 */       V value = (V)entry.getValue();
/*     */       
/*  69 */       int keyHash = key.hashCode();
/*  70 */       int valueHash = value.hashCode();
/*  71 */       int keyBucket = Hashing.smear(keyHash) & this.mask;
/*  72 */       int valueBucket = Hashing.smear(valueHash) & this.mask;
/*     */       
/*  74 */       ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
/*  75 */       for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket; keyEntry != null; 
/*  76 */         keyEntry = keyEntry.getNextInKeyBucket()) {
/*  77 */         checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
/*     */       }
/*  79 */       ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
/*  80 */       for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket; valueEntry != null; 
/*  81 */         valueEntry = valueEntry.getNextInValueBucket()) {
/*  82 */         checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
/*     */       }
/*  84 */       ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? entry : new NonTerminalBiMapEntry(entry, nextInKeyBucket, nextInValueBucket);
/*     */ 
/*     */ 
/*     */       
/*  88 */       keyTable[keyBucket] = newEntry;
/*  89 */       valueTable[valueBucket] = newEntry;
/*  90 */       entries[i] = newEntry;
/*  91 */       hashCode += (keyHash ^ valueHash);
/*     */     } 
/*     */     
/*  94 */     this.keyTable = keyTable;
/*  95 */     this.valueTable = valueTable;
/*  96 */     this.entries = entries;
/*  97 */     this.hashCode = hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegularImmutableBiMap(Entry[] entriesToAdd) {
/* 104 */     int n = entriesToAdd.length;
/* 105 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/* 106 */     this.mask = tableSize - 1;
/* 107 */     ImmutableMapEntry[] keyTable = createEntryArray(tableSize);
/* 108 */     ImmutableMapEntry[] valueTable = createEntryArray(tableSize);
/* 109 */     ImmutableMapEntry[] entries = createEntryArray(n);
/* 110 */     int hashCode = 0;
/*     */     
/* 112 */     for (int i = 0; i < n; i++) {
/*     */       
/* 114 */       Map.Entry<K, V> entry = entriesToAdd[i];
/* 115 */       K key = (K)entry.getKey();
/* 116 */       V value = (V)entry.getValue();
/* 117 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 118 */       int keyHash = key.hashCode();
/* 119 */       int valueHash = value.hashCode();
/* 120 */       int keyBucket = Hashing.smear(keyHash) & this.mask;
/* 121 */       int valueBucket = Hashing.smear(valueHash) & this.mask;
/*     */       
/* 123 */       ImmutableMapEntry<K, V> nextInKeyBucket = keyTable[keyBucket];
/* 124 */       for (ImmutableMapEntry<K, V> keyEntry = nextInKeyBucket; keyEntry != null; 
/* 125 */         keyEntry = keyEntry.getNextInKeyBucket()) {
/* 126 */         checkNoConflict(!key.equals(keyEntry.getKey()), "key", entry, keyEntry);
/*     */       }
/* 128 */       ImmutableMapEntry<K, V> nextInValueBucket = valueTable[valueBucket];
/* 129 */       for (ImmutableMapEntry<K, V> valueEntry = nextInValueBucket; valueEntry != null; 
/* 130 */         valueEntry = valueEntry.getNextInValueBucket()) {
/* 131 */         checkNoConflict(!value.equals(valueEntry.getValue()), "value", entry, valueEntry);
/*     */       }
/* 133 */       ImmutableMapEntry<K, V> newEntry = (nextInKeyBucket == null && nextInValueBucket == null) ? new ImmutableMapEntry.TerminalEntry(key, value) : new NonTerminalBiMapEntry(key, value, nextInKeyBucket, nextInValueBucket);
/*     */ 
/*     */ 
/*     */       
/* 137 */       keyTable[keyBucket] = newEntry;
/* 138 */       valueTable[valueBucket] = newEntry;
/* 139 */       entries[i] = newEntry;
/* 140 */       hashCode += (keyHash ^ valueHash);
/*     */     } 
/*     */     
/* 143 */     this.keyTable = keyTable;
/* 144 */     this.valueTable = valueTable;
/* 145 */     this.entries = entries;
/* 146 */     this.hashCode = hashCode;
/*     */   }
/*     */   
/*     */   private static final class NonTerminalBiMapEntry<K, V>
/*     */     extends ImmutableMapEntry<K, V> {
/*     */     @Nullable
/*     */     private final ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */     
/*     */     NonTerminalBiMapEntry(K key, V value, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
/* 155 */       super(key, value);
/* 156 */       this.nextInKeyBucket = nextInKeyBucket;
/* 157 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */     @Nullable
/*     */     private final ImmutableMapEntry<K, V> nextInValueBucket;
/*     */     
/*     */     NonTerminalBiMapEntry(ImmutableMapEntry<K, V> contents, @Nullable ImmutableMapEntry<K, V> nextInKeyBucket, @Nullable ImmutableMapEntry<K, V> nextInValueBucket) {
/* 163 */       super(contents);
/* 164 */       this.nextInKeyBucket = nextInKeyBucket;
/* 165 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 171 */     ImmutableMapEntry<K, V> getNextInKeyBucket() { return this.nextInKeyBucket; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 177 */     ImmutableMapEntry<K, V> getNextInValueBucket() { return this.nextInValueBucket; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   private static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int length) { return new ImmutableMapEntry[length]; }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(@Nullable Object key) {
/* 189 */     if (key == null) {
/* 190 */       return null;
/*     */     }
/* 192 */     int bucket = Hashing.smear(key.hashCode()) & this.mask;
/* 193 */     for (ImmutableMapEntry<K, V> entry = this.keyTable[bucket]; entry != null; 
/* 194 */       entry = entry.getNextInKeyBucket()) {
/* 195 */       if (key.equals(entry.getKey())) {
/* 196 */         return (V)entry.getValue();
/*     */       }
/*     */     } 
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 204 */     return new ImmutableMapEntrySet<K, V>()
/*     */       {
/*     */         ImmutableMap<K, V> map() {
/* 207 */           return super.this$0;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 212 */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() { return asList().iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 217 */         ImmutableList<Map.Entry<K, V>> createAsList() { return new RegularImmutableAsList(this, super.this$0.entries); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 222 */         boolean isHashCodeFast() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 227 */         public int hashCode() { return super.this$0.hashCode; }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public int size() { return this.entries.length; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 246 */     ImmutableBiMap<V, K> result = this.inverse;
/* 247 */     return (result == null) ? (this.inverse = new Inverse(null)) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse
/*     */     extends ImmutableBiMap<V, K> {
/*     */     private Inverse() {}
/*     */     
/* 254 */     public int size() { return inverse().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     public ImmutableBiMap<K, V> inverse() { return RegularImmutableBiMap.this; }
/*     */ 
/*     */ 
/*     */     
/*     */     public K get(@Nullable Object value) {
/* 264 */       if (value == null) {
/* 265 */         return null;
/*     */       }
/* 267 */       int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
/* 268 */       for (ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket]; entry != null; 
/* 269 */         entry = entry.getNextInValueBucket()) {
/* 270 */         if (value.equals(entry.getValue())) {
/* 271 */           return (K)entry.getKey();
/*     */         }
/*     */       } 
/* 274 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 279 */     ImmutableSet<Map.Entry<V, K>> createEntrySet() { return new InverseEntrySet(); }
/*     */ 
/*     */     
/*     */     final class InverseEntrySet
/*     */       extends ImmutableMapEntrySet<V, K>
/*     */     {
/* 285 */       ImmutableMap<V, K> map() { return super.this$1; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 290 */       boolean isHashCodeFast() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 295 */       public int hashCode() { return RegularImmutableBiMap.Inverse.this.this$0.hashCode; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 300 */       public UnmodifiableIterator<Map.Entry<V, K>> iterator() { return asList().iterator(); }
/*     */ 
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<V, K>> createAsList() {
/* 305 */         return new ImmutableAsList<Map.Entry<V, K>>()
/*     */           {
/*     */             public Map.Entry<V, K> get(int index) {
/* 308 */               Map.Entry<K, V> entry = RegularImmutableBiMap.Inverse.this.this$0.entries[index];
/* 309 */               return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 314 */             ImmutableCollection<Map.Entry<V, K>> delegateCollection() { return super.this$2; }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     Object writeReplace() { return new RegularImmutableBiMap.InverseSerializedForm(RegularImmutableBiMap.this); }
/*     */   }
/*     */   
/*     */   private static class InverseSerializedForm<K, V>
/*     */     extends Object implements Serializable {
/*     */     private final ImmutableBiMap<K, V> forward;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 335 */     InverseSerializedForm(ImmutableBiMap<K, V> forward) { this.forward = forward; }
/*     */ 
/*     */ 
/*     */     
/* 339 */     Object readResolve() { return this.forward.inverse(); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */