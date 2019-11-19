/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
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
/*     */ @Beta
/*     */ @GwtIncompatible("NavigableMap")
/*     */ public final class TreeRangeMap<K extends Comparable, V>
/*     */   extends Object
/*     */   implements RangeMap<K, V>
/*     */ {
/*  61 */   public static <K extends Comparable, V> TreeRangeMap<K, V> create() { return new TreeRangeMap(); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound = Maps.newTreeMap();
/*     */ 
/*     */   
/*     */   private static final class RangeMapEntry<K extends Comparable, V>
/*     */     extends AbstractMapEntry<Range<K>, V>
/*     */   {
/*     */     private final Range<K> range;
/*     */     private final V value;
/*     */     
/*  74 */     RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) { this(Range.create(lowerBound, upperBound), value); }
/*     */ 
/*     */     
/*     */     RangeMapEntry(Range<K> range, V value) {
/*  78 */       this.range = range;
/*  79 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     public Range<K> getKey() { return this.range; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     public V getValue() { return (V)this.value; }
/*     */ 
/*     */ 
/*     */     
/*  93 */     public boolean contains(K value) { return this.range.contains(value); }
/*     */ 
/*     */ 
/*     */     
/*  97 */     Cut<K> getLowerBound() { return this.range.lowerBound; }
/*     */ 
/*     */ 
/*     */     
/* 101 */     Cut<K> getUpperBound() { return this.range.upperBound; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(K key) {
/* 108 */     Map.Entry<Range<K>, V> entry = getEntry(key);
/* 109 */     return (V)((entry == null) ? null : entry.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 115 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
/*     */     
/* 117 */     if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key)) {
/* 118 */       return (Map.Entry)mapEntry.getValue();
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Range<K> range, V value) {
/* 126 */     if (!range.isEmpty()) {
/* 127 */       Preconditions.checkNotNull(value);
/* 128 */       remove(range);
/* 129 */       this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry(range, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 135 */     for (Map.Entry<Range<K>, V> entry : rangeMap.asMapOfRanges().entrySet()) {
/* 136 */       put((Range)entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public void clear() { this.entriesByLowerBound.clear(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 147 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
/* 148 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
/* 149 */     if (firstEntry == null) {
/* 150 */       throw new NoSuchElementException();
/*     */     }
/* 152 */     return Range.create((((RangeMapEntry)firstEntry.getValue()).getKey()).lowerBound, (((RangeMapEntry)lastEntry.getValue()).getKey()).upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) { this.entriesByLowerBound.put(lowerBound, new RangeMapEntry(lowerBound, upperBound, value)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(Range<K> rangeToRemove) {
/* 163 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/*     */     
/* 173 */     if (mapEntryBelowToTruncate != null) {
/*     */       
/* 175 */       RangeMapEntry<K, V> rangeMapEntry = (RangeMapEntry)mapEntryBelowToTruncate.getValue();
/* 176 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
/*     */         
/* 178 */         if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */         {
/*     */           
/* 181 */           putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), ((RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
/*     */         }
/*     */ 
/*     */         
/* 185 */         putRangeMapEntry(rangeMapEntry.getLowerBound(), rangeToRemove.lowerBound, ((RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 190 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
/*     */     
/* 192 */     if (mapEntryAboveToTruncate != null) {
/*     */       
/* 194 */       RangeMapEntry<K, V> rangeMapEntry = (RangeMapEntry)mapEntryAboveToTruncate.getValue();
/* 195 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
/*     */ 
/*     */         
/* 198 */         putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), ((RangeMapEntry)mapEntryAboveToTruncate.getValue()).getValue());
/*     */         
/* 200 */         this.entriesByLowerBound.remove(rangeToRemove.lowerBound);
/*     */       } 
/*     */     } 
/* 203 */     this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public Map<Range<K>, V> asMapOfRanges() { return new AsMapOfRanges(null); }
/*     */   
/*     */   private final class AsMapOfRanges
/*     */     extends AbstractMap<Range<K>, V>
/*     */   {
/*     */     private AsMapOfRanges() {}
/*     */     
/* 215 */     public boolean containsKey(@Nullable Object key) { return (get(key) != null); }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 220 */       if (key instanceof Range) {
/* 221 */         Range<?> range = (Range)key;
/* 222 */         TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
/* 223 */         if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
/* 224 */           return (V)rangeMapEntry.getValue();
/*     */         }
/*     */       } 
/* 227 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<Range<K>, V>> entrySet() {
/* 232 */       return new AbstractSet<Map.Entry<Range<K>, V>>()
/*     */         {
/*     */           
/*     */           public Iterator<Map.Entry<Range<K>, V>> iterator()
/*     */           {
/* 237 */             return TreeRangeMap.AsMapOfRanges.this.this$0.entriesByLowerBound.values().iterator();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 242 */           public int size() { return TreeRangeMap.AsMapOfRanges.this.this$0.entriesByLowerBound.size(); }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 250 */     if (subRange.equals(Range.all())) {
/* 251 */       return this;
/*     */     }
/* 253 */     return new SubRangeMap(subRange);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 259 */   private RangeMap<K, V> emptySubRangeMap() { return EMPTY_SUB_RANGE_MAP; }
/*     */ 
/*     */   
/* 262 */   private static final RangeMap EMPTY_SUB_RANGE_MAP = new RangeMap()
/*     */     {
/*     */       @Nullable
/*     */       public Object get(Comparable key)
/*     */       {
/* 267 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       @Nullable
/* 273 */       public Map.Entry<Range, Object> getEntry(Comparable key) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 278 */       public Range span() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */       
/*     */       public void put(Range range, Object value) {
/* 283 */         Preconditions.checkNotNull(range);
/* 284 */         throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putAll(RangeMap rangeMap) {
/* 290 */         if (!rangeMap.asMapOfRanges().isEmpty()) {
/* 291 */           throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {}
/*     */ 
/*     */ 
/*     */       
/* 301 */       public void remove(Range range) { Preconditions.checkNotNull(range); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 306 */       public Map<Range, Object> asMapOfRanges() { return Collections.emptyMap(); }
/*     */ 
/*     */ 
/*     */       
/*     */       public RangeMap subRangeMap(Range range) {
/* 311 */         Preconditions.checkNotNull(range);
/* 312 */         return this;
/*     */       }
/*     */     };
/*     */   
/*     */   private class SubRangeMap
/*     */     extends Object
/*     */     implements RangeMap<K, V> {
/*     */     private final Range<K> subRange;
/*     */     
/* 321 */     SubRangeMap(Range<K> subRange) { this.subRange = subRange; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 327 */     public V get(K key) { return (V)(this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Map.Entry<Range<K>, V> getEntry(K key) {
/* 335 */       if (this.subRange.contains(key)) {
/* 336 */         Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
/* 337 */         if (entry != null) {
/* 338 */           return Maps.immutableEntry(((Range)entry.getKey()).intersection(this.subRange), entry.getValue());
/*     */         }
/*     */       } 
/* 341 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<K> span() {
/*     */       Cut<K> upperBound, lowerBound;
/* 347 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
/*     */       
/* 349 */       if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
/*     */         
/* 351 */         lowerBound = this.subRange.lowerBound;
/*     */       } else {
/* 353 */         lowerBound = (Cut)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
/* 354 */         if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
/* 355 */           throw new NoSuchElementException();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 360 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
/*     */       
/* 362 */       if (upperEntry == null)
/* 363 */         throw new NoSuchElementException(); 
/* 364 */       if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
/* 365 */         upperBound = this.subRange.upperBound;
/*     */       } else {
/* 367 */         upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
/*     */       } 
/* 369 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(Range<K> range, V value) {
/* 374 */       Preconditions.checkArgument(this.subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", new Object[] { range, this.subRange });
/*     */       
/* 376 */       TreeRangeMap.this.put(range, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(RangeMap<K, V> rangeMap) {
/* 381 */       if (rangeMap.asMapOfRanges().isEmpty()) {
/*     */         return;
/*     */       }
/* 384 */       Range<K> span = rangeMap.span();
/* 385 */       Preconditions.checkArgument(this.subRange.encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", new Object[] { span, this.subRange });
/*     */       
/* 387 */       TreeRangeMap.this.putAll(rangeMap);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 392 */     public void clear() { TreeRangeMap.this.remove(this.subRange); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove(Range<K> range) {
/* 397 */       if (range.isConnected(this.subRange)) {
/* 398 */         TreeRangeMap.this.remove(range.intersection(this.subRange));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeMap<K, V> subRangeMap(Range<K> range) {
/* 404 */       if (!range.isConnected(this.subRange)) {
/* 405 */         return TreeRangeMap.this.emptySubRangeMap();
/*     */       }
/* 407 */       return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 413 */     public Map<Range<K>, V> asMapOfRanges() { return new SubRangeMapAsMap(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 418 */       if (o instanceof RangeMap) {
/* 419 */         RangeMap<?, ?> rangeMap = (RangeMap)o;
/* 420 */         return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */       } 
/* 422 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 427 */     public int hashCode() { return asMapOfRanges().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 432 */     public String toString() { return asMapOfRanges().toString(); }
/*     */ 
/*     */ 
/*     */     
/*     */     class SubRangeMapAsMap
/*     */       extends AbstractMap<Range<K>, V>
/*     */     {
/* 439 */       public boolean containsKey(Object key) { return (super.get(key) != null); }
/*     */ 
/*     */ 
/*     */       
/*     */       public V get(Object key) {
/*     */         try {
/* 445 */           if (key instanceof Range) {
/*     */             
/* 447 */             Range<K> r = (Range)key;
/* 448 */             if (!super.this$1.subRange.encloses(r) || r.isEmpty()) {
/* 449 */               return null;
/*     */             }
/* 451 */             TreeRangeMap.RangeMapEntry<K, V> candidate = null;
/* 452 */             if (r.lowerBound.compareTo(super.this$1.subRange.lowerBound) == 0) {
/*     */               
/* 454 */               Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.SubRangeMap.this.this$0.entriesByLowerBound.floorEntry(r.lowerBound);
/*     */               
/* 456 */               if (entry != null) {
/* 457 */                 candidate = (TreeRangeMap.RangeMapEntry)entry.getValue();
/*     */               }
/*     */             } else {
/* 460 */               candidate = (TreeRangeMap.RangeMapEntry)TreeRangeMap.SubRangeMap.this.this$0.entriesByLowerBound.get(r.lowerBound);
/*     */             } 
/*     */             
/* 463 */             if (candidate != null && candidate.getKey().isConnected(super.this$1.subRange) && candidate.getKey().intersection(super.this$1.subRange).equals(r))
/*     */             {
/* 465 */               return (V)candidate.getValue();
/*     */             }
/*     */           } 
/* 468 */         } catch (ClassCastException e) {
/* 469 */           return null;
/*     */         } 
/* 471 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public V remove(Object key) {
/* 476 */         V value = (V)super.get(key);
/* 477 */         if (value != null) {
/*     */           
/* 479 */           Range<K> range = (Range)key;
/* 480 */           TreeRangeMap.SubRangeMap.this.this$0.remove(range);
/* 481 */           return value;
/*     */         } 
/* 483 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 488 */       public void clear() { super.this$1.clear(); }
/*     */ 
/*     */       
/*     */       private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> predicate) {
/* 492 */         List<Range<K>> toRemove = Lists.newArrayList();
/* 493 */         for (Map.Entry<Range<K>, V> entry : super.entrySet()) {
/* 494 */           if (predicate.apply(entry)) {
/* 495 */             toRemove.add(entry.getKey());
/*     */           }
/*     */         } 
/* 498 */         for (Range<K> range : toRemove) {
/* 499 */           TreeRangeMap.SubRangeMap.this.this$0.remove(range);
/*     */         }
/* 501 */         return !toRemove.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Range<K>> keySet() {
/* 506 */         return new Maps.KeySet<Range<K>, V>(this)
/*     */           {
/*     */             public boolean remove(@Nullable Object o) {
/* 509 */               return (super.this$2.remove(o) != null);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 514 */             public boolean retainAll(Collection<?> c) { return super.this$2.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction())); }
/*     */           };
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Set<Map.Entry<Range<K>, V>> entrySet() {
/* 521 */         return new Maps.EntrySet<Range<K>, V>()
/*     */           {
/*     */             Map<Range<K>, V> map() {
/* 524 */               return super.this$2;
/*     */             }
/*     */ 
/*     */             
/*     */             public Iterator<Map.Entry<Range<K>, V>> iterator() {
/* 529 */               if (TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.this$1.subRange.isEmpty()) {
/* 530 */                 return Iterators.emptyIterator();
/*     */               }
/* 532 */               Cut<K> cutToStart = (Cut)Objects.firstNonNull(TreeRangeMap.SubRangeMap.this.this$0.entriesByLowerBound.floorKey(super.this$2.this$1.subRange.lowerBound), super.this$2.this$1.subRange.lowerBound);
/*     */ 
/*     */               
/* 535 */               final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.SubRangeMap.this.this$0.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
/*     */               
/* 537 */               return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */                 {
/*     */                   protected Map.Entry<Range<K>, V> computeNext()
/*     */                   {
/* 541 */                     while (backingItr.hasNext()) {
/* 542 */                       TreeRangeMap.RangeMapEntry<K, V> entry = (TreeRangeMap.RangeMapEntry)backingItr.next();
/* 543 */                       if (entry.getLowerBound().compareTo(super.this$3.this$2.this$1.subRange.upperBound) >= 0)
/*     */                         break; 
/* 545 */                       if (entry.getUpperBound().compareTo(super.this$3.this$2.this$1.subRange.lowerBound) > 0)
/*     */                       {
/* 547 */                         return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.this$1.subRange), entry.getValue());
/*     */                       }
/*     */                     } 
/*     */                     
/* 551 */                     return (Map.Entry)endOfData();
/*     */                   }
/*     */                 };
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 558 */             public boolean retainAll(Collection<?> c) { return super.this$2.removeEntryIf(Predicates.not(Predicates.in(c))); }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 563 */             public int size() { return Iterators.size(super.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 568 */             public boolean isEmpty() { return !super.iterator().hasNext(); }
/*     */           };
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Collection<V> values() {
/* 575 */         return new Maps.Values<Range<K>, V>(this)
/*     */           {
/*     */             public boolean removeAll(Collection<?> c) {
/* 578 */               return super.this$2.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 583 */             public boolean retainAll(Collection<?> c) { return super.this$2.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction())); }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object o) {
/* 592 */     if (o instanceof RangeMap) {
/* 593 */       RangeMap<?, ?> rangeMap = (RangeMap)o;
/* 594 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 596 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 601 */   public int hashCode() { return asMapOfRanges().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 606 */   public String toString() { return this.entriesByLowerBound.values().toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/TreeRangeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */