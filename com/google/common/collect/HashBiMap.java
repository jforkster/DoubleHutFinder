/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class HashBiMap<K, V>
/*     */   extends AbstractMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private static final double LOAD_FACTOR = 1.0D;
/*     */   private BiEntry<K, V>[] hashTableKToV;
/*     */   private BiEntry<K, V>[] hashTableVToK;
/*     */   private int size;
/*     */   private int mask;
/*     */   private int modCount;
/*     */   private BiMap<V, K> inverse;
/*     */   @GwtIncompatible("Not needed in emulated source")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  58 */   public static <K, V> HashBiMap<K, V> create() { return create(16); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static <K, V> HashBiMap<K, V> create(int expectedSize) { return new HashBiMap(expectedSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  76 */     HashBiMap<K, V> bimap = create(map.size());
/*  77 */     bimap.putAll(map);
/*  78 */     return bimap;
/*     */   }
/*     */   
/*     */   private static final class BiEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */   {
/*     */     final int keyHash;
/*     */     final int valueHash;
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInKToVBucket;
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInVToKBucket;
/*     */     
/*     */     BiEntry(K key, int keyHash, V value, int valueHash) {
/*  92 */       super(key, value);
/*  93 */       this.keyHash = keyHash;
/*  94 */       this.valueHash = valueHash;
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
/* 107 */   private HashBiMap(int expectedSize) { init(expectedSize); }
/*     */ 
/*     */   
/*     */   private void init(int expectedSize) {
/* 111 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 112 */     int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
/* 113 */     this.hashTableKToV = createTable(tableSize);
/* 114 */     this.hashTableVToK = createTable(tableSize);
/* 115 */     this.mask = tableSize - 1;
/* 116 */     this.modCount = 0;
/* 117 */     this.size = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(BiEntry<K, V> entry) {
/* 125 */     int keyBucket = entry.keyHash & this.mask;
/* 126 */     BiEntry<K, V> prevBucketEntry = null;
/* 127 */     BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
/* 128 */     for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
/* 129 */       if (bucketEntry == entry) {
/* 130 */         if (prevBucketEntry == null) {
/* 131 */           this.hashTableKToV[keyBucket] = entry.nextInKToVBucket; break;
/*     */         } 
/* 133 */         prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 137 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 140 */     int valueBucket = entry.valueHash & this.mask;
/* 141 */     prevBucketEntry = null;
/* 142 */     BiEntry<K, V> bucketEntry = this.hashTableVToK[valueBucket];
/* 143 */     for (;; bucketEntry = bucketEntry.nextInVToKBucket) {
/* 144 */       if (bucketEntry == entry) {
/* 145 */         if (prevBucketEntry == null) {
/* 146 */           this.hashTableVToK[valueBucket] = entry.nextInVToKBucket; break;
/*     */         } 
/* 148 */         prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 152 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 155 */     this.size--;
/* 156 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private void insert(BiEntry<K, V> entry) {
/* 160 */     int keyBucket = entry.keyHash & this.mask;
/* 161 */     entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
/* 162 */     this.hashTableKToV[keyBucket] = entry;
/*     */     
/* 164 */     int valueBucket = entry.valueHash & this.mask;
/* 165 */     entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
/* 166 */     this.hashTableVToK[valueBucket] = entry;
/*     */     
/* 168 */     this.size++;
/* 169 */     this.modCount++;
/*     */   }
/*     */ 
/*     */   
/* 173 */   private static int hash(@Nullable Object o) { return Hashing.smear((o == null) ? 0 : o.hashCode()); }
/*     */ 
/*     */   
/*     */   private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
/* 177 */     for (BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask]; entry != null; 
/* 178 */       entry = entry.nextInKToVBucket) {
/* 179 */       if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
/* 180 */         return entry;
/*     */       }
/*     */     } 
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
/* 187 */     for (BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask]; entry != null; 
/* 188 */       entry = entry.nextInVToKBucket) {
/* 189 */       if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
/* 190 */         return entry;
/*     */       }
/*     */     } 
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public boolean containsKey(@Nullable Object key) { return (seekByKey(key, hash(key)) != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public boolean containsValue(@Nullable Object value) { return (seekByValue(value, hash(value)) != null); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(@Nullable Object key) {
/* 209 */     BiEntry<K, V> entry = seekByKey(key, hash(key));
/* 210 */     return (V)((entry == null) ? null : entry.value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   public V put(@Nullable K key, @Nullable V value) { return (V)put(key, value, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   public V forcePut(@Nullable K key, @Nullable V value) { return (V)put(key, value, true); }
/*     */ 
/*     */   
/*     */   private V put(@Nullable K key, @Nullable V value, boolean force) {
/* 224 */     int keyHash = hash(key);
/* 225 */     int valueHash = hash(value);
/*     */     
/* 227 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 228 */     if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value))
/*     */     {
/* 230 */       return value;
/*     */     }
/*     */     
/* 233 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 234 */     if (oldEntryForValue != null) {
/* 235 */       if (force) {
/* 236 */         delete(oldEntryForValue);
/*     */       } else {
/* 238 */         throw new IllegalArgumentException("value already present: " + value);
/*     */       } 
/*     */     }
/*     */     
/* 242 */     if (oldEntryForKey != null) {
/* 243 */       delete(oldEntryForKey);
/*     */     }
/* 245 */     BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
/* 246 */     insert(newEntry);
/* 247 */     rehashIfNecessary();
/* 248 */     return (V)((oldEntryForKey == null) ? null : oldEntryForKey.value);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private K putInverse(@Nullable V value, @Nullable K key, boolean force) {
/* 253 */     int valueHash = hash(value);
/* 254 */     int keyHash = hash(key);
/*     */     
/* 256 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 257 */     if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key))
/*     */     {
/* 259 */       return key;
/*     */     }
/*     */     
/* 262 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 263 */     if (oldEntryForKey != null) {
/* 264 */       if (force) {
/* 265 */         delete(oldEntryForKey);
/*     */       } else {
/* 267 */         throw new IllegalArgumentException("value already present: " + key);
/*     */       } 
/*     */     }
/*     */     
/* 271 */     if (oldEntryForValue != null) {
/* 272 */       delete(oldEntryForValue);
/*     */     }
/* 274 */     BiEntry<K, V> newEntry = new BiEntry<K, V>(key, keyHash, value, valueHash);
/* 275 */     insert(newEntry);
/* 276 */     rehashIfNecessary();
/* 277 */     return (K)((oldEntryForValue == null) ? null : oldEntryForValue.key);
/*     */   }
/*     */   
/*     */   private void rehashIfNecessary() {
/* 281 */     BiEntry[] arrayOfBiEntry = this.hashTableKToV;
/* 282 */     if (Hashing.needsResizing(this.size, arrayOfBiEntry.length, 1.0D)) {
/* 283 */       int newTableSize = arrayOfBiEntry.length * 2;
/*     */       
/* 285 */       this.hashTableKToV = createTable(newTableSize);
/* 286 */       this.hashTableVToK = createTable(newTableSize);
/* 287 */       this.mask = newTableSize - 1;
/* 288 */       this.size = 0;
/*     */       
/* 290 */       for (int bucket = 0; bucket < arrayOfBiEntry.length; bucket++) {
/* 291 */         BiEntry<K, V> entry = arrayOfBiEntry[bucket];
/* 292 */         while (entry != null) {
/* 293 */           BiEntry<K, V> nextEntry = entry.nextInKToVBucket;
/* 294 */           insert(entry);
/* 295 */           entry = nextEntry;
/*     */         } 
/*     */       } 
/* 298 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 304 */   private BiEntry<K, V>[] createTable(int length) { return new BiEntry[length]; }
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(@Nullable Object key) {
/* 309 */     BiEntry<K, V> entry = seekByKey(key, hash(key));
/* 310 */     if (entry == null) {
/* 311 */       return null;
/*     */     }
/* 313 */     delete(entry);
/* 314 */     return (V)entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 320 */     this.size = 0;
/* 321 */     Arrays.fill(this.hashTableKToV, null);
/* 322 */     Arrays.fill(this.hashTableVToK, null);
/* 323 */     this.modCount++;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 328 */   public int size() { return this.size; }
/*     */   
/*     */   abstract class Itr<T>
/*     */     extends Object implements Iterator<T> {
/* 332 */     int nextBucket = 0;
/* 333 */     HashBiMap.BiEntry<K, V> next = null;
/* 334 */     HashBiMap.BiEntry<K, V> toRemove = null;
/* 335 */     int expectedModCount = HashBiMap.this.modCount;
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 338 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 339 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 345 */       checkForConcurrentModification();
/* 346 */       if (this.next != null) {
/* 347 */         return true;
/*     */       }
/* 349 */       while (this.nextBucket < HashBiMap.this.hashTableKToV.length) {
/* 350 */         if (HashBiMap.this.hashTableKToV[this.nextBucket] != null) {
/* 351 */           this.next = HashBiMap.this.hashTableKToV[this.nextBucket++];
/* 352 */           return true;
/*     */         } 
/* 354 */         this.nextBucket++;
/*     */       } 
/* 356 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 361 */       checkForConcurrentModification();
/* 362 */       if (!hasNext()) {
/* 363 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 366 */       HashBiMap.BiEntry<K, V> entry = this.next;
/* 367 */       this.next = entry.nextInKToVBucket;
/* 368 */       this.toRemove = entry;
/* 369 */       return (T)output(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 374 */       checkForConcurrentModification();
/* 375 */       CollectPreconditions.checkRemove((this.toRemove != null));
/* 376 */       HashBiMap.this.delete(this.toRemove);
/* 377 */       this.expectedModCount = HashBiMap.this.modCount;
/* 378 */       this.toRemove = null;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry);
/*     */   }
/*     */ 
/*     */   
/* 386 */   public Set<K> keySet() { return new KeySet(); }
/*     */   
/*     */   private final class KeySet
/*     */     extends Maps.KeySet<K, V> {
/*     */     KeySet() {
/* 391 */       super(HashBiMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 396 */       return new HashBiMap<K, V>.Itr<K>()
/*     */         {
/*     */           K output(HashBiMap.BiEntry<K, V> entry) {
/* 399 */             return (K)entry.key;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@Nullable Object o) {
/* 406 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, HashBiMap.hash(o));
/* 407 */       if (entry == null) {
/* 408 */         return false;
/*     */       }
/* 410 */       HashBiMap.this.delete(entry);
/* 411 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 418 */   public Set<V> values() { return inverse().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 423 */   public Set<Map.Entry<K, V>> entrySet() { return new EntrySet(null); }
/*     */   
/*     */   private final class EntrySet
/*     */     extends Maps.EntrySet<K, V> {
/*     */     private EntrySet() {}
/*     */     
/* 429 */     Map<K, V> map() { return HashBiMap.this; }
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 434 */       return new HashBiMap<K, V>.Itr<Map.Entry<K, V>>()
/*     */         {
/*     */           Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
/* 437 */             return new HashBiMap.EntrySet.null.MapEntry(entry);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           class MapEntry
/*     */             extends AbstractMapEntry<K, V>
/*     */           {
/*     */             HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */             
/* 448 */             public K getKey() { return (K)super.delegate.key; }
/*     */ 
/*     */ 
/*     */             
/* 452 */             public V getValue() { return (V)super.delegate.value; }
/*     */ 
/*     */             
/*     */             public V setValue(V value) {
/* 456 */               V oldValue = (V)super.delegate.value;
/* 457 */               int valueHash = HashBiMap.hash(value);
/* 458 */               if (valueHash == super.delegate.valueHash && Objects.equal(value, oldValue)) {
/* 459 */                 return value;
/*     */               }
/* 461 */               Preconditions.checkArgument((super.this$2.this$1.this$0.seekByValue(value, valueHash) == null), "value already present: %s", new Object[] { value });
/*     */               
/* 463 */               super.this$2.this$1.this$0.delete(super.delegate);
/* 464 */               HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<K, V>(super.delegate.key, super.delegate.keyHash, value, valueHash);
/*     */               
/* 466 */               super.this$2.this$1.this$0.insert(newEntry);
/* 467 */               HashBiMap.EntrySet.null.this.expectedModCount = super.this$2.this$1.this$0.modCount;
/* 468 */               if (HashBiMap.EntrySet.null.this.toRemove == super.delegate) {
/* 469 */                 HashBiMap.EntrySet.null.this.toRemove = newEntry;
/*     */               }
/* 471 */               super.delegate = newEntry;
/* 472 */               return oldValue;
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 483 */   public BiMap<V, K> inverse() { return (this.inverse == null) ? (this.inverse = new Inverse(null)) : this.inverse; }
/*     */   
/*     */   private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
/*     */     private Inverse() {}
/*     */     
/* 488 */     BiMap<K, V> forward() { return HashBiMap.this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 493 */     public int size() { return HashBiMap.this.size; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 498 */     public void clear() { forward().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 503 */     public boolean containsKey(@Nullable Object value) { return forward().containsValue(value); }
/*     */ 
/*     */ 
/*     */     
/*     */     public K get(@Nullable Object value) {
/* 508 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
/* 509 */       return (K)((entry == null) ? null : entry.key);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 514 */     public K put(@Nullable V value, @Nullable K key) { return (K)HashBiMap.this.putInverse(value, key, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 519 */     public K forcePut(@Nullable V value, @Nullable K key) { return (K)HashBiMap.this.putInverse(value, key, true); }
/*     */ 
/*     */ 
/*     */     
/*     */     public K remove(@Nullable Object value) {
/* 524 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
/* 525 */       if (entry == null) {
/* 526 */         return null;
/*     */       }
/* 528 */       HashBiMap.this.delete(entry);
/* 529 */       return (K)entry.key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 535 */     public BiMap<K, V> inverse() { return forward(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 540 */     public Set<V> keySet() { return new InverseKeySet(); }
/*     */     
/*     */     private final class InverseKeySet
/*     */       extends Maps.KeySet<V, K> {
/*     */       InverseKeySet() {
/* 545 */         super(HashBiMap.Inverse.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(@Nullable Object o) {
/* 550 */         HashBiMap.BiEntry<K, V> entry = HashBiMap.Inverse.this.this$0.seekByValue(o, HashBiMap.hash(o));
/* 551 */         if (entry == null) {
/* 552 */           return false;
/*     */         }
/* 554 */         HashBiMap.Inverse.this.this$0.delete(entry);
/* 555 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Iterator<V> iterator() {
/* 561 */         return new HashBiMap<K, V>.Itr<V>()
/*     */           {
/* 563 */             V output(HashBiMap.BiEntry<K, V> entry) { return (V)entry.value; }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 571 */     public Set<K> values() { return forward().keySet(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<V, K>> entrySet() {
/* 576 */       return new Maps.EntrySet<V, K>()
/*     */         {
/*     */           Map<V, K> map()
/*     */           {
/* 580 */             return super.this$1;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Map.Entry<V, K>> iterator() {
/* 585 */             return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>()
/*     */               {
/*     */                 Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
/* 588 */                   return new HashBiMap.Inverse.null.null.InverseEntry(entry);
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 class InverseEntry
/*     */                   extends AbstractMapEntry<V, K>
/*     */                 {
/*     */                   HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */ 
/*     */                   
/* 600 */                   public V getKey() { return (V)super.delegate.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 605 */                   public K getValue() { return (K)super.delegate.key; }
/*     */ 
/*     */ 
/*     */                   
/*     */                   public K setValue(K key) {
/* 610 */                     K oldKey = (K)super.delegate.key;
/* 611 */                     int keyHash = HashBiMap.hash(key);
/* 612 */                     if (keyHash == super.delegate.keyHash && Objects.equal(key, oldKey)) {
/* 613 */                       return key;
/*     */                     }
/* 615 */                     Preconditions.checkArgument((super.this$3.this$2.this$1.this$0.seekByKey(key, keyHash) == null), "value already present: %s", new Object[] { key });
/* 616 */                     super.this$3.this$2.this$1.this$0.delete(super.delegate);
/* 617 */                     HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<K, V>(key, keyHash, super.delegate.value, super.delegate.valueHash);
/*     */                     
/* 619 */                     super.this$3.this$2.this$1.this$0.insert(newEntry);
/* 620 */                     HashBiMap.Inverse.null.null.this.expectedModCount = super.this$3.this$2.this$1.this$0.modCount;
/*     */ 
/*     */                     
/* 623 */                     return oldKey;
/*     */                   }
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/* 632 */     Object writeReplace() { return new HashBiMap.InverseSerializedForm(HashBiMap.this); }
/*     */   }
/*     */   
/*     */   private static final class InverseSerializedForm<K, V>
/*     */     extends Object
/*     */     implements Serializable {
/*     */     private final HashBiMap<K, V> bimap;
/*     */     
/* 640 */     InverseSerializedForm(HashBiMap<K, V> bimap) { this.bimap = bimap; }
/*     */ 
/*     */ 
/*     */     
/* 644 */     Object readResolve() { return this.bimap.inverse(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 653 */     stream.defaultWriteObject();
/* 654 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 659 */     stream.defaultReadObject();
/* 660 */     int size = Serialization.readCount(stream);
/* 661 */     init(size);
/* 662 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/HashBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */