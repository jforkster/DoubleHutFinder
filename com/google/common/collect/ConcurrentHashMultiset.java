/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ConcurrentHashMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final ConcurrentMap<E, AtomicInteger> countMap;
/*     */   private EntrySet entrySet;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static class FieldSettersHolder
/*     */   {
/*  75 */     static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
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
/*  87 */   public static <E> ConcurrentHashMultiset<E> create() { return new ConcurrentHashMultiset(new ConcurrentHashMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
/*  99 */     ConcurrentHashMultiset<E> multiset = create();
/* 100 */     Iterables.addAll(multiset, elements);
/* 101 */     return multiset;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/* 127 */   public static <E> ConcurrentHashMultiset<E> create(MapMaker mapMaker) { return new ConcurrentHashMultiset(mapMaker.makeMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
/* 141 */     Preconditions.checkArgument(countMap.isEmpty());
/* 142 */     this.countMap = countMap;
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
/*     */   public int count(@Nullable Object element) {
/* 154 */     AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(this.countMap, element);
/* 155 */     return (existingCounter == null) ? 0 : existingCounter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 165 */     long sum = 0L;
/* 166 */     for (AtomicInteger value : this.countMap.values()) {
/* 167 */       sum += value.get();
/*     */     }
/* 169 */     return Ints.saturatedCast(sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public Object[] toArray() { return snapshot().toArray(); }
/*     */ 
/*     */ 
/*     */   
/* 182 */   public <T> T[] toArray(T[] array) { return (T[])snapshot().toArray(array); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<E> snapshot() {
/* 190 */     List<E> list = Lists.newArrayListWithExpectedSize(size());
/* 191 */     for (Multiset.Entry<E> entry : entrySet()) {
/* 192 */       E element = (E)entry.getElement();
/* 193 */       for (int i = entry.getCount(); i > 0; i--) {
/* 194 */         list.add(element);
/*     */       }
/*     */     } 
/* 197 */     return list;
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
/*     */   public int add(E element, int occurrences) {
/*     */     AtomicInteger newCounter, existingCounter;
/* 212 */     Preconditions.checkNotNull(element);
/* 213 */     if (occurrences == 0) {
/* 214 */       return count(element);
/*     */     }
/* 216 */     Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/*     */     do {
/* 219 */       existingCounter = (AtomicInteger)Maps.safeGet(this.countMap, element);
/* 220 */       if (existingCounter == null) {
/* 221 */         existingCounter = (AtomicInteger)this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
/* 222 */         if (existingCounter == null) {
/* 223 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       while (true) {
/* 229 */         int oldValue = existingCounter.get();
/* 230 */         if (oldValue != 0) {
/*     */           try {
/* 232 */             int newValue = IntMath.checkedAdd(oldValue, occurrences);
/* 233 */             if (existingCounter.compareAndSet(oldValue, newValue))
/*     */             {
/* 235 */               return oldValue; } 
/*     */             continue;
/* 237 */           } catch (ArithmeticException overflow) {
/* 238 */             throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
/*     */           } 
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 245 */       newCounter = new AtomicInteger(occurrences);
/* 246 */     } while (this.countMap.putIfAbsent(element, newCounter) != null && !this.countMap.replace(element, existingCounter, newCounter));
/*     */     
/* 248 */     return 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int remove(@Nullable Object element, int occurrences) {
/* 277 */     if (occurrences == 0) {
/* 278 */       return count(element);
/*     */     }
/* 280 */     Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/* 282 */     AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(this.countMap, element);
/* 283 */     if (existingCounter == null) {
/* 284 */       return 0;
/*     */     }
/*     */     while (true) {
/* 287 */       int oldValue = existingCounter.get();
/* 288 */       if (oldValue != 0) {
/* 289 */         int newValue = Math.max(0, oldValue - occurrences);
/* 290 */         if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 291 */           if (newValue == 0)
/*     */           {
/*     */             
/* 294 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 296 */           return oldValue;
/*     */         }  continue;
/*     */       }  break;
/* 299 */     }  return 0;
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
/*     */   public boolean removeExactly(@Nullable Object element, int occurrences) {
/*     */     int newValue, oldValue;
/* 316 */     if (occurrences == 0) {
/* 317 */       return true;
/*     */     }
/* 319 */     Preconditions.checkArgument((occurrences > 0), "Invalid occurrences: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/* 321 */     AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(this.countMap, element);
/* 322 */     if (existingCounter == null) {
/* 323 */       return false;
/*     */     }
/*     */     do {
/* 326 */       oldValue = existingCounter.get();
/* 327 */       if (oldValue < occurrences) {
/* 328 */         return false;
/*     */       }
/* 330 */       newValue = oldValue - occurrences;
/* 331 */     } while (!existingCounter.compareAndSet(oldValue, newValue));
/* 332 */     if (newValue == 0)
/*     */     {
/*     */       
/* 335 */       this.countMap.remove(element, existingCounter);
/*     */     }
/* 337 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setCount(E element, int count) {
/*     */     int oldValue;
/*     */     AtomicInteger existingCounter;
/* 350 */     Preconditions.checkNotNull(element);
/* 351 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */     label26: while (true) {
/* 353 */       existingCounter = (AtomicInteger)Maps.safeGet(this.countMap, element);
/* 354 */       if (existingCounter == null) {
/* 355 */         if (count == 0) {
/* 356 */           return 0;
/*     */         }
/* 358 */         existingCounter = (AtomicInteger)this.countMap.putIfAbsent(element, new AtomicInteger(count));
/* 359 */         if (existingCounter == null) {
/* 360 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 367 */         oldValue = existingCounter.get();
/* 368 */         if (oldValue == 0) {
/* 369 */           if (count == 0) {
/* 370 */             return 0;
/*     */           }
/* 372 */           AtomicInteger newCounter = new AtomicInteger(count);
/* 373 */           if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter))
/*     */           {
/* 375 */             return 0;
/*     */           }
/*     */           
/*     */           continue label26;
/*     */         } 
/* 380 */       } while (!existingCounter.compareAndSet(oldValue, count)); break;
/* 381 */     }  if (count == 0)
/*     */     {
/*     */       
/* 384 */       this.countMap.remove(element, existingCounter);
/*     */     }
/* 386 */     return oldValue;
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
/*     */   public boolean setCount(E element, int expectedOldCount, int newCount) {
/* 405 */     Preconditions.checkNotNull(element);
/* 406 */     CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
/* 407 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*     */     
/* 409 */     AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(this.countMap, element);
/* 410 */     if (existingCounter == null) {
/* 411 */       if (expectedOldCount != 0)
/* 412 */         return false; 
/* 413 */       if (newCount == 0) {
/* 414 */         return true;
/*     */       }
/*     */       
/* 417 */       return (this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null);
/*     */     } 
/*     */     
/* 420 */     int oldValue = existingCounter.get();
/* 421 */     if (oldValue == expectedOldCount) {
/* 422 */       if (oldValue == 0) {
/* 423 */         if (newCount == 0) {
/*     */           
/* 425 */           this.countMap.remove(element, existingCounter);
/* 426 */           return true;
/*     */         } 
/* 428 */         AtomicInteger newCounter = new AtomicInteger(newCount);
/* 429 */         return (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap.replace(element, existingCounter, newCounter));
/*     */       } 
/*     */ 
/*     */       
/* 433 */       if (existingCounter.compareAndSet(oldValue, newCount)) {
/* 434 */         if (newCount == 0)
/*     */         {
/*     */           
/* 437 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 439 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 443 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 449 */     final Set<E> delegate = this.countMap.keySet();
/* 450 */     return new ForwardingSet<E>()
/*     */       {
/* 452 */         protected Set<E> delegate() { return delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 457 */         public boolean contains(@Nullable Object object) { return (object != null && Collections2.safeContains(delegate, object)); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 462 */         public boolean containsAll(Collection<?> collection) { return standardContainsAll(collection); }
/*     */ 
/*     */ 
/*     */         
/* 466 */         public boolean remove(Object object) { return (object != null && Collections2.safeRemove(delegate, object)); }
/*     */ 
/*     */ 
/*     */         
/* 470 */         public boolean removeAll(Collection<?> c) { return standardRemoveAll(c); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 478 */     EntrySet result = this.entrySet;
/* 479 */     if (result == null) {
/* 480 */       this.entrySet = result = new EntrySet(null);
/*     */     }
/* 482 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 486 */   int distinctElements() { return this.countMap.size(); }
/*     */ 
/*     */ 
/*     */   
/* 490 */   public boolean isEmpty() { return this.countMap.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 496 */     final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>() { private Iterator<Map.Entry<E, AtomicInteger>> mapEntries;
/*     */         
/*     */         protected Multiset.Entry<E> computeNext() {
/*     */           int count;
/*     */           Map.Entry<E, AtomicInteger> mapEntry;
/*     */           do {
/* 502 */             if (!super.mapEntries.hasNext()) {
/* 503 */               return (Multiset.Entry)endOfData();
/*     */             }
/* 505 */             mapEntry = (Map.Entry)super.mapEntries.next();
/* 506 */             count = ((AtomicInteger)mapEntry.getValue()).get();
/* 507 */           } while (count == 0);
/* 508 */           return Multisets.immutableEntry(mapEntry.getKey(), count);
/*     */         } }
/*     */       ;
/*     */ 
/*     */ 
/*     */     
/* 514 */     return new ForwardingIterator<Multiset.Entry<E>>()
/*     */       {
/*     */         private Multiset.Entry<E> last;
/*     */         
/* 518 */         protected Iterator<Multiset.Entry<E>> delegate() { return readOnlyIterator; }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 522 */           super.last = (Multiset.Entry)super.next();
/* 523 */           return super.last;
/*     */         }
/*     */         
/*     */         public void remove() {
/* 527 */           CollectPreconditions.checkRemove((super.last != null));
/* 528 */           super.this$0.setCount(super.last.getElement(), 0);
/* 529 */           super.last = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 535 */   public void clear() { this.countMap.clear(); }
/*     */   
/*     */   private class EntrySet extends AbstractMultiset<E>.EntrySet { private EntrySet() {
/* 538 */       super(ConcurrentHashMultiset.this);
/*     */     }
/* 540 */     ConcurrentHashMultiset<E> multiset() { return ConcurrentHashMultiset.this; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 549 */     public Object[] toArray() { return snapshot().toArray(); }
/*     */ 
/*     */ 
/*     */     
/* 553 */     public <T> T[] toArray(T[] array) { return (T[])snapshot().toArray(array); }
/*     */ 
/*     */     
/*     */     private List<Multiset.Entry<E>> snapshot() {
/* 557 */       List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
/*     */       
/* 559 */       Iterators.addAll(list, iterator());
/* 560 */       return list;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 568 */     stream.defaultWriteObject();
/* 569 */     stream.writeObject(this.countMap);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 573 */     stream.defaultReadObject();
/*     */     
/* 575 */     ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap)stream.readObject();
/*     */     
/* 577 */     FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ConcurrentHashMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */