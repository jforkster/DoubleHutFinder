/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ComputingConcurrentHashMap<K, V>
/*     */   extends MapMakerInternalMap<K, V>
/*     */ {
/*     */   final Function<? super K, ? extends V> computingFunction;
/*     */   private static final long serialVersionUID = 4L;
/*     */   
/*     */   ComputingConcurrentHashMap(MapMaker builder, Function<? super K, ? extends V> computingFunction) {
/*  51 */     super(builder);
/*  52 */     this.computingFunction = (Function)Preconditions.checkNotNull(computingFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   MapMakerInternalMap.Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) { return new ComputingSegment(this, initialCapacity, maxSegmentSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   ComputingSegment<K, V> segmentFor(int hash) { return (ComputingSegment)super.segmentFor(hash); }
/*     */ 
/*     */   
/*     */   V getOrCompute(K key) throws ExecutionException {
/*  66 */     int hash = hash(Preconditions.checkNotNull(key));
/*  67 */     return (V)segmentFor(hash).getOrCompute(key, hash, this.computingFunction);
/*     */   }
/*     */   
/*     */   static final class ComputingSegment<K, V>
/*     */     extends MapMakerInternalMap.Segment<K, V>
/*     */   {
/*  73 */     ComputingSegment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) { super(map, initialCapacity, maxSegmentSize); }
/*     */ 
/*     */     
/*     */     V getOrCompute(K key, int hash, Function<? super K, ? extends V> computingFunction) throws ExecutionException {
/*     */       
/*     */       try { V value;
/*     */         MapMakerInternalMap.ReferenceEntry<K, V> e;
/*     */         
/*  81 */         do { e = getEntry(key, hash);
/*  82 */           if (e != null) {
/*  83 */             V value = (V)getLiveValue(e);
/*  84 */             if (value != null) {
/*  85 */               recordRead(e);
/*  86 */               return value;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/*  92 */           if (e == null || !e.getValueReference().isComputingReference())
/*  93 */           { boolean createNewEntry = true;
/*  94 */             ComputingConcurrentHashMap.ComputingValueReference<K, V> computingValueReference = null;
/*  95 */             lock();
/*     */             try {
/*  97 */               preWriteCleanup();
/*     */               
/*  99 */               int newCount = this.count - 1;
/* 100 */               AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 101 */               int index = hash & table.length() - 1;
/* 102 */               MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*     */               
/* 104 */               for (e = first; e != null; e = e.getNext()) {
/* 105 */                 K entryKey = (K)e.getKey();
/* 106 */                 if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
/*     */                   
/* 108 */                   MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 109 */                   if (valueReference.isComputingReference()) {
/* 110 */                     createNewEntry = false; break;
/*     */                   } 
/* 112 */                   V value = (V)e.getValueReference().get();
/* 113 */                   if (value == null) {
/* 114 */                     enqueueNotification(entryKey, hash, value, MapMaker.RemovalCause.COLLECTED);
/* 115 */                   } else if (this.map.expires() && this.map.isExpired(e)) {
/*     */ 
/*     */                     
/* 118 */                     enqueueNotification(entryKey, hash, value, MapMaker.RemovalCause.EXPIRED);
/*     */                   } else {
/* 120 */                     recordLockedRead(e);
/* 121 */                     return value;
/*     */                   } 
/*     */ 
/*     */                   
/* 125 */                   this.evictionQueue.remove(e);
/* 126 */                   this.expirationQueue.remove(e);
/* 127 */                   this.count = newCount;
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */               
/* 133 */               if (createNewEntry) {
/* 134 */                 computingValueReference = new ComputingConcurrentHashMap.ComputingValueReference<K, V>(computingFunction);
/*     */                 
/* 136 */                 if (e == null) {
/* 137 */                   e = newEntry(key, hash, first);
/* 138 */                   e.setValueReference(computingValueReference);
/* 139 */                   table.set(index, e);
/*     */                 } else {
/* 141 */                   e.setValueReference(computingValueReference);
/*     */                 } 
/*     */               } 
/*     */             } finally {
/* 145 */               unlock();
/* 146 */               postWriteCleanup();
/*     */             } 
/*     */             
/* 149 */             if (createNewEntry)
/*     */             
/* 151 */             { object = compute(key, hash, e, computingValueReference);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 167 */               return (V)object; }  }  Preconditions.checkState(!Thread.holdsLock(e), "Recursive computation"); value = (V)e.getValueReference().waitForValue(); } while (value == null); recordRead(e); return value; } finally { postReadCleanup(); }
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     V compute(K key, int hash, MapMakerInternalMap.ReferenceEntry<K, V> e, ComputingConcurrentHashMap.ComputingValueReference<K, V> computingValueReference) throws ExecutionException {
/* 174 */       value = null;
/* 175 */       long start = System.nanoTime();
/* 176 */       end = 0L;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 181 */         synchronized (e) {
/* 182 */           value = (V)computingValueReference.compute(key, hash);
/* 183 */           end = System.nanoTime();
/*     */         } 
/* 185 */         if (value != null) {
/*     */           
/* 187 */           V oldValue = (V)put(key, hash, value, true);
/* 188 */           if (oldValue != null)
/*     */           {
/* 190 */             enqueueNotification(key, hash, value, MapMaker.RemovalCause.REPLACED);
/*     */           }
/*     */         } 
/* 193 */         return value;
/*     */       } finally {
/* 195 */         if (end == 0L) {
/* 196 */           end = System.nanoTime();
/*     */         }
/* 198 */         if (value == null) {
/* 199 */           clearValue(key, hash, computingValueReference);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ComputationExceptionReference<K, V>
/*     */     extends Object
/*     */     implements MapMakerInternalMap.ValueReference<K, V>
/*     */   {
/*     */     final Throwable t;
/*     */     
/* 212 */     ComputationExceptionReference(Throwable t) { this.t = t; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     public V get() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     public boolean isComputingReference() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     public V waitForValue() { throw new ExecutionException(this.t); }
/*     */ 
/*     */     
/*     */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ComputedReference<K, V>
/*     */     extends Object
/*     */     implements MapMakerInternalMap.ValueReference<K, V>
/*     */   {
/*     */     final V value;
/*     */ 
/*     */     
/* 252 */     ComputedReference(@Nullable V value) { this.value = value; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 257 */     public V get() { return (V)this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     public boolean isComputingReference() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     public V waitForValue() { return (V)get(); }
/*     */     
/*     */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
/*     */   }
/*     */   
/*     */   private static final class ComputingValueReference<K, V>
/*     */     extends Object implements MapMakerInternalMap.ValueReference<K, V> {
/*     */     final Function<? super K, ? extends V> computingFunction;
/*     */     
/*     */     public ComputingValueReference(Function<? super K, ? extends V> computingFunction) {
/* 288 */       this.computedReference = MapMakerInternalMap.unset();
/*     */ 
/*     */ 
/*     */       
/* 292 */       this.computingFunction = computingFunction;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     public V get() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 304 */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @Nullable V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     public boolean isComputingReference() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V waitForValue() {
/* 323 */       if (this.computedReference == MapMakerInternalMap.UNSET) {
/* 324 */         interrupted = false;
/*     */         try {
/* 326 */           synchronized (this) {
/* 327 */             while (this.computedReference == MapMakerInternalMap.UNSET) {
/*     */               try {
/* 329 */                 wait();
/* 330 */               } catch (InterruptedException ie) {
/* 331 */                 interrupted = true;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } finally {
/* 336 */           if (interrupted) {
/* 337 */             Thread.currentThread().interrupt();
/*     */           }
/*     */         } 
/*     */       } 
/* 341 */       return (V)this.computedReference.waitForValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 348 */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) { setValueReference(newValue); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     V compute(K key, int hash) throws ExecutionException {
/*     */       V value;
/*     */       try {
/* 356 */         value = (V)this.computingFunction.apply(key);
/* 357 */       } catch (Throwable t) {
/* 358 */         setValueReference(new ComputingConcurrentHashMap.ComputationExceptionReference(t));
/* 359 */         throw new ExecutionException(t);
/*     */       } 
/*     */       
/* 362 */       setValueReference(new ComputingConcurrentHashMap.ComputedReference(value));
/* 363 */       return value;
/*     */     }
/*     */     
/*     */     void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 367 */       synchronized (this) {
/* 368 */         if (this.computedReference == MapMakerInternalMap.UNSET) {
/* 369 */           this.computedReference = valueReference;
/* 370 */           notifyAll();
/*     */         } 
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
/* 382 */   Object writeReplace() { return new ComputingSerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this, this.computingFunction); }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ComputingSerializationProxy<K, V>
/*     */     extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
/*     */   {
/*     */     final Function<? super K, ? extends V> computingFunction;
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 4L;
/*     */ 
/*     */     
/*     */     ComputingSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate, Function<? super K, ? extends V> computingFunction) {
/* 396 */       super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate);
/*     */       
/* 398 */       this.computingFunction = computingFunction;
/*     */     }
/*     */     
/*     */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 402 */       out.defaultWriteObject();
/* 403 */       writeMapTo(out);
/*     */     }
/*     */ 
/*     */     
/*     */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 408 */       in.defaultReadObject();
/* 409 */       MapMaker mapMaker = readMapMaker(in);
/* 410 */       this.delegate = mapMaker.makeComputingMap(this.computingFunction);
/* 411 */       readEntries(in);
/*     */     }
/*     */ 
/*     */     
/* 415 */     Object readResolve() { return this.delegate; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ComputingConcurrentHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */