/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class AtomicLongMap<K>
/*     */   extends Object
/*     */ {
/*     */   private final ConcurrentHashMap<K, AtomicLong> map;
/*     */   private Map<K, Long> asMap;
/*     */   
/*  58 */   private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> map) { this.map = (ConcurrentHashMap)Preconditions.checkNotNull(map); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static <K> AtomicLongMap<K> create() { return new AtomicLongMap(new ConcurrentHashMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
/*  72 */     AtomicLongMap<K> result = create();
/*  73 */     result.putAll(m);
/*  74 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long get(K key) {
/*  82 */     AtomicLong atomic = (AtomicLong)this.map.get(key);
/*  83 */     return (atomic == null) ? 0L : atomic.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public long incrementAndGet(K key) { return addAndGet(key, 1L); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public long decrementAndGet(K key) { return addAndGet(key, -1L); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long addAndGet(K key, long delta) {
/*     */     long newValue;
/*     */     label17: while (true) {
/*     */       long oldValue;
/* 106 */       AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 107 */       if (atomic == null) {
/* 108 */         atomic = (AtomicLong)this.map.putIfAbsent(key, new AtomicLong(delta));
/* 109 */         if (atomic == null) {
/* 110 */           return delta;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       do {
/* 116 */         oldValue = atomic.get();
/* 117 */         if (oldValue == 0L) {
/*     */           
/* 119 */           if (this.map.replace(key, atomic, new AtomicLong(delta))) {
/* 120 */             return delta;
/*     */           }
/*     */           
/*     */           continue label17;
/*     */         } 
/*     */         
/* 126 */         newValue = oldValue + delta;
/* 127 */       } while (!atomic.compareAndSet(oldValue, newValue)); break;
/* 128 */     }  return newValue;
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
/* 139 */   public long getAndIncrement(K key) { return getAndAdd(key, 1L); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public long getAndDecrement(K key) { return getAndAdd(key, -1L); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAndAdd(K key, long delta) {
/*     */     long oldValue;
/*     */     label17: while (true) {
/*     */       long newValue;
/* 155 */       AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 156 */       if (atomic == null) {
/* 157 */         atomic = (AtomicLong)this.map.putIfAbsent(key, new AtomicLong(delta));
/* 158 */         if (atomic == null) {
/* 159 */           return 0L;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       do {
/* 165 */         oldValue = atomic.get();
/* 166 */         if (oldValue == 0L) {
/*     */           
/* 168 */           if (this.map.replace(key, atomic, new AtomicLong(delta))) {
/* 169 */             return 0L;
/*     */           }
/*     */           
/*     */           continue label17;
/*     */         } 
/*     */         
/* 175 */         newValue = oldValue + delta;
/* 176 */       } while (!atomic.compareAndSet(oldValue, newValue)); break;
/* 177 */     }  return oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long put(K key, long newValue) {
/*     */     long oldValue;
/*     */     label16: while (true) {
/* 190 */       AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 191 */       if (atomic == null) {
/* 192 */         atomic = (AtomicLong)this.map.putIfAbsent(key, new AtomicLong(newValue));
/* 193 */         if (atomic == null) {
/* 194 */           return 0L;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       do {
/* 200 */         oldValue = atomic.get();
/* 201 */         if (oldValue == 0L) {
/*     */           
/* 203 */           if (this.map.replace(key, atomic, new AtomicLong(newValue))) {
/* 204 */             return 0L;
/*     */           }
/*     */ 
/*     */           
/*     */           continue label16;
/*     */         } 
/* 210 */       } while (!atomic.compareAndSet(oldValue, newValue)); break;
/* 211 */     }  return oldValue;
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
/*     */   public void putAll(Map<? extends K, ? extends Long> m) {
/* 225 */     for (Map.Entry<? extends K, ? extends Long> entry : m.entrySet()) {
/* 226 */       put(entry.getKey(), ((Long)entry.getValue()).longValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long remove(K key) {
/*     */     long oldValue;
/* 235 */     AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 236 */     if (atomic == null) {
/* 237 */       return 0L;
/*     */     }
/*     */     
/*     */     do {
/* 241 */       oldValue = atomic.get();
/* 242 */     } while (oldValue != 0L && !atomic.compareAndSet(oldValue, 0L));
/*     */     
/* 244 */     this.map.remove(key, atomic);
/*     */     
/* 246 */     return oldValue;
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
/*     */   public void removeAllZeros() {
/* 258 */     for (K key : this.map.keySet()) {
/* 259 */       AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 260 */       if (atomic != null && atomic.get() == 0L) {
/* 261 */         this.map.remove(key, atomic);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sum() {
/* 272 */     long sum = 0L;
/* 273 */     for (AtomicLong value : this.map.values()) {
/* 274 */       sum += value.get();
/*     */     }
/* 276 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Long> asMap() {
/* 285 */     Map<K, Long> result = this.asMap;
/* 286 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */   
/*     */   private Map<K, Long> createAsMap() {
/* 290 */     return Collections.unmodifiableMap(Maps.transformValues(this.map, new Function<AtomicLong, Long>()
/*     */           {
/*     */             public Long apply(AtomicLong atomic)
/*     */             {
/* 294 */               return Long.valueOf(atomic.get());
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 303 */   public boolean containsKey(Object key) { return this.map.containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   public int size() { return this.map.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 318 */   public boolean isEmpty() { return this.map.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 328 */   public void clear() { this.map.clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 333 */   public String toString() { return this.map.toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long putIfAbsent(K key, long newValue) {
/*     */     long oldValue;
/*     */     while (true) {
/* 366 */       AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 367 */       if (atomic == null) {
/* 368 */         atomic = (AtomicLong)this.map.putIfAbsent(key, new AtomicLong(newValue));
/* 369 */         if (atomic == null) {
/* 370 */           return 0L;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 375 */       oldValue = atomic.get();
/* 376 */       if (oldValue == 0L) {
/*     */         
/* 378 */         if (this.map.replace(key, atomic, new AtomicLong(newValue))) {
/* 379 */           return 0L;
/*     */         }
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 385 */     return oldValue;
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
/*     */   boolean replace(K key, long expectedOldValue, long newValue) {
/* 398 */     if (expectedOldValue == 0L) {
/* 399 */       return (putIfAbsent(key, newValue) == 0L);
/*     */     }
/* 401 */     AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 402 */     return (atomic == null) ? false : atomic.compareAndSet(expectedOldValue, newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean remove(K key, long value) {
/* 411 */     AtomicLong atomic = (AtomicLong)this.map.get(key);
/* 412 */     if (atomic == null) {
/* 413 */       return false;
/*     */     }
/*     */     
/* 416 */     long oldValue = atomic.get();
/* 417 */     if (oldValue != value) {
/* 418 */       return false;
/*     */     }
/*     */     
/* 421 */     if (oldValue == 0L || atomic.compareAndSet(oldValue, 0L)) {
/*     */       
/* 423 */       this.map.remove(key, atomic);
/*     */       
/* 425 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 429 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AtomicLongMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */