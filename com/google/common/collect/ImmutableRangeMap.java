/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @Beta
/*     */ @GwtIncompatible("NavigableMap")
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   extends Object
/*     */   implements RangeMap<K, V>
/*     */ {
/*  44 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(ImmutableList.of(), ImmutableList.of());
/*     */ 
/*     */   
/*     */   private final ImmutableList<Range<K>> ranges;
/*     */ 
/*     */   
/*     */   private final ImmutableList<V> values;
/*     */ 
/*     */   
/*  53 */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() { return EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) { return new ImmutableRangeMap(ImmutableList.of(range), ImmutableList.of(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
/*  67 */     if (rangeMap instanceof ImmutableRangeMap) {
/*  68 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  70 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  71 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<Range<K>>(map.size());
/*  72 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<V>(map.size());
/*  73 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  74 */       rangesBuilder.add(entry.getKey());
/*  75 */       valuesBuilder.add(entry.getValue());
/*     */     } 
/*  77 */     return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static <K extends Comparable<?>, V> Builder<K, V> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */     extends Object
/*     */   {
/*  95 */     private final RangeSet<K> keyRanges = TreeRangeSet.create();
/*  96 */     private final RangeMap<K, V> rangeMap = TreeRangeMap.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(Range<K> range, V value) {
/* 106 */       Preconditions.checkNotNull(range);
/* 107 */       Preconditions.checkNotNull(value);
/* 108 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", new Object[] { range });
/* 109 */       if (!this.keyRanges.complement().encloses(range))
/*     */       {
/* 111 */         for (Map.Entry<Range<K>, V> entry : this.rangeMap.asMapOfRanges().entrySet()) {
/* 112 */           Range<K> key = (Range)entry.getKey();
/* 113 */           if (key.isConnected(range) && !key.intersection(range).isEmpty()) {
/* 114 */             throw new IllegalArgumentException("Overlapping ranges: range " + range + " overlaps with entry " + entry);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 119 */       this.keyRanges.add(range);
/* 120 */       this.rangeMap.put(range, value);
/* 121 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
/* 131 */       for (Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
/* 132 */         put((Range)entry.getKey(), entry.getValue());
/*     */       }
/* 134 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeMap<K, V> build() {
/* 142 */       Map<Range<K>, V> map = this.rangeMap.asMapOfRanges();
/* 143 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<Range<K>>(map.size());
/*     */       
/* 145 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<V>(map.size());
/* 146 */       for (Map.Entry<Range<K>, V> entry : map.entrySet()) {
/* 147 */         rangesBuilder.add(entry.getKey());
/* 148 */         valuesBuilder.add(entry.getValue());
/*     */       } 
/* 150 */       return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
/* 158 */     this.ranges = ranges;
/* 159 */     this.values = values;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(K key) {
/* 165 */     int index = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/* 167 */     if (index == -1) {
/* 168 */       return null;
/*     */     }
/* 170 */     Range<K> range = (Range)this.ranges.get(index);
/* 171 */     return (V)(range.contains(key) ? this.values.get(index) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 178 */     int index = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */     
/* 180 */     if (index == -1) {
/* 181 */       return null;
/*     */     }
/* 183 */     Range<K> range = (Range)this.ranges.get(index);
/* 184 */     return range.contains(key) ? Maps.immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 190 */     if (this.ranges.isEmpty()) {
/* 191 */       throw new NoSuchElementException();
/*     */     }
/* 193 */     Range<K> firstRange = (Range)this.ranges.get(0);
/* 194 */     Range<K> lastRange = (Range)this.ranges.get(this.ranges.size() - 1);
/* 195 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public void put(Range<K> range, V value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   public void putAll(RangeMap<K, V> rangeMap) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   public void remove(Range<K> range) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges() {
/* 220 */     if (this.ranges.isEmpty()) {
/* 221 */       return ImmutableMap.of();
/*     */     }
/* 223 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<Range<K>>(this.ranges, Range.RANGE_LEX_ORDERING);
/*     */     
/* 225 */     return new RegularImmutableSortedMap(rangeSet, this.values);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
/* 230 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 231 */       return of(); 
/* 232 */     if (this.ranges.isEmpty() || range.encloses(span())) {
/* 233 */       return this;
/*     */     }
/* 235 */     int lowerIndex = SortedLists.binarySearch(this.ranges, Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     
/* 238 */     int upperIndex = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */     
/* 241 */     if (lowerIndex >= upperIndex) {
/* 242 */       return of();
/*     */     }
/* 244 */     final int off = lowerIndex;
/* 245 */     final int len = upperIndex - lowerIndex;
/* 246 */     ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>()
/*     */       {
/*     */         public int size() {
/* 249 */           return len;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<K> get(int index) {
/* 254 */           Preconditions.checkElementIndex(index, len);
/* 255 */           if (index == 0 || index == len - 1) {
/* 256 */             return ((Range)super.this$0.ranges.get(index + off)).intersection(range);
/*     */           }
/* 258 */           return (Range)super.this$0.ranges.get(index + off);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 264 */           return true;
/*     */         }
/*     */       };
/* 267 */     final ImmutableRangeMap<K, V> outer = this;
/* 268 */     return new ImmutableRangeMap<K, V>(subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */       {
/*     */         public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange)
/*     */         {
/* 272 */           if (range.isConnected(subRange)) {
/* 273 */             return outer.subRangeMap(subRange.intersection(range));
/*     */           }
/* 275 */           return of();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 283 */   public int hashCode() { return asMapOfRanges().hashCode(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object o) {
/* 288 */     if (o instanceof RangeMap) {
/* 289 */       RangeMap<?, ?> rangeMap = (RangeMap)o;
/* 290 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 297 */   public String toString() { return asMapOfRanges().toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableRangeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */