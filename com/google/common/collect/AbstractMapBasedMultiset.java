/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ConcurrentModificationException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractMapBasedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private Map<E, Count> backingMap;
/*     */   private long size;
/*     */   @GwtIncompatible("not needed in emulated source.")
/*     */   private static final long serialVersionUID = -2250766705698539974L;
/*     */   
/*     */   protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
/*  62 */     this.backingMap = (Map)Preconditions.checkNotNull(backingMap);
/*  63 */     this.size = super.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  68 */   void setBackingMap(Map<E, Count> backingMap) { this.backingMap = backingMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public Set<Multiset.Entry<E>> entrySet() { return super.entrySet(); }
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/*  87 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/*     */     
/*  89 */     return new Iterator<Multiset.Entry<E>>()
/*     */       {
/*     */         Map.Entry<E, Count> toRemove;
/*     */ 
/*     */         
/*  94 */         public boolean hasNext() { return backingEntries.hasNext(); }
/*     */ 
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/*  99 */           final Map.Entry<E, Count> mapEntry = (Map.Entry)backingEntries.next();
/* 100 */           super.toRemove = mapEntry;
/* 101 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               public E getElement() {
/* 104 */                 return (E)mapEntry.getKey();
/*     */               }
/*     */               
/*     */               public int getCount() {
/* 108 */                 Count count = (Count)mapEntry.getValue();
/* 109 */                 if (count == null || count.get() == 0) {
/* 110 */                   Count frequency = (Count)AbstractMapBasedMultiset.null.this.this$0.backingMap.get(super.getElement());
/* 111 */                   if (frequency != null) {
/* 112 */                     return frequency.get();
/*     */                   }
/*     */                 } 
/* 115 */                 return (count == null) ? 0 : count.get();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 122 */           CollectPreconditions.checkRemove((super.toRemove != null));
/* 123 */           super.this$0.size -= ((Count)super.toRemove.getValue()).getAndSet(0);
/* 124 */           backingEntries.remove();
/* 125 */           super.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 132 */     for (Count frequency : this.backingMap.values()) {
/* 133 */       frequency.set(0);
/*     */     }
/* 135 */     this.backingMap.clear();
/* 136 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   int distinctElements() { return this.backingMap.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public int size() { return Ints.saturatedCast(this.size); }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public Iterator<E> iterator() { return new MapBasedMultisetIterator(); }
/*     */ 
/*     */   
/*     */   private class MapBasedMultisetIterator
/*     */     extends Object
/*     */     implements Iterator<E>
/*     */   {
/*     */     final Iterator<Map.Entry<E, Count>> entryIterator;
/*     */     
/*     */     Map.Entry<E, Count> currentEntry;
/*     */     
/*     */     int occurrencesLeft;
/*     */     
/*     */     boolean canRemove;
/*     */     
/* 166 */     MapBasedMultisetIterator() { this.entryIterator = this$0.backingMap.entrySet().iterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     public boolean hasNext() { return (this.occurrencesLeft > 0 || this.entryIterator.hasNext()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public E next() {
/* 176 */       if (this.occurrencesLeft == 0) {
/* 177 */         this.currentEntry = (Map.Entry)this.entryIterator.next();
/* 178 */         this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
/*     */       } 
/* 180 */       this.occurrencesLeft--;
/* 181 */       this.canRemove = true;
/* 182 */       return (E)this.currentEntry.getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 187 */       CollectPreconditions.checkRemove(this.canRemove);
/* 188 */       int frequency = ((Count)this.currentEntry.getValue()).get();
/* 189 */       if (frequency <= 0) {
/* 190 */         throw new ConcurrentModificationException();
/*     */       }
/* 192 */       if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
/* 193 */         this.entryIterator.remove();
/*     */       }
/* 195 */       AbstractMapBasedMultiset.this.size--;
/* 196 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public int count(@Nullable Object element) {
/* 201 */     Count frequency = (Count)Maps.safeGet(this.backingMap, element);
/* 202 */     return (frequency == null) ? 0 : frequency.get();
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
/*     */   public int add(@Nullable E element, int occurrences) {
/*     */     int oldCount;
/* 215 */     if (occurrences == 0) {
/* 216 */       return count(element);
/*     */     }
/* 218 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/* 220 */     Count frequency = (Count)this.backingMap.get(element);
/*     */     
/* 222 */     if (frequency == null) {
/* 223 */       oldCount = 0;
/* 224 */       this.backingMap.put(element, new Count(occurrences));
/*     */     } else {
/* 226 */       oldCount = frequency.get();
/* 227 */       long newCount = oldCount + occurrences;
/* 228 */       Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", new Object[] { Long.valueOf(newCount) });
/*     */       
/* 230 */       frequency.getAndAdd(occurrences);
/*     */     } 
/* 232 */     this.size += occurrences;
/* 233 */     return oldCount;
/*     */   }
/*     */   public int remove(@Nullable Object element, int occurrences) {
/*     */     int numberRemoved;
/* 237 */     if (occurrences == 0) {
/* 238 */       return count(element);
/*     */     }
/* 240 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", new Object[] { Integer.valueOf(occurrences) });
/*     */     
/* 242 */     Count frequency = (Count)this.backingMap.get(element);
/* 243 */     if (frequency == null) {
/* 244 */       return 0;
/*     */     }
/*     */     
/* 247 */     int oldCount = frequency.get();
/*     */ 
/*     */     
/* 250 */     if (oldCount > occurrences) {
/* 251 */       numberRemoved = occurrences;
/*     */     } else {
/* 253 */       numberRemoved = oldCount;
/* 254 */       this.backingMap.remove(element);
/*     */     } 
/*     */     
/* 257 */     frequency.addAndGet(-numberRemoved);
/* 258 */     this.size -= numberRemoved;
/* 259 */     return oldCount;
/*     */   }
/*     */   
/*     */   public int setCount(@Nullable E element, int count) {
/*     */     int oldCount;
/* 264 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */ 
/*     */ 
/*     */     
/* 268 */     if (count == 0) {
/* 269 */       Count existingCounter = (Count)this.backingMap.remove(element);
/* 270 */       oldCount = getAndSet(existingCounter, count);
/*     */     } else {
/* 272 */       Count existingCounter = (Count)this.backingMap.get(element);
/* 273 */       oldCount = getAndSet(existingCounter, count);
/*     */       
/* 275 */       if (existingCounter == null) {
/* 276 */         this.backingMap.put(element, new Count(count));
/*     */       }
/*     */     } 
/*     */     
/* 280 */     this.size += (count - oldCount);
/* 281 */     return oldCount;
/*     */   }
/*     */   
/*     */   private static int getAndSet(Count i, int count) {
/* 285 */     if (i == null) {
/* 286 */       return 0;
/*     */     }
/*     */     
/* 289 */     return i.getAndSet(count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectStreamException")
/* 296 */   private void readObjectNoData() { throw new InvalidObjectException("Stream data required"); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractMapBasedMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */