/*     */ package com.google.common.collect;
/*     */ 
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
/*     */ abstract class AbstractRangeSet<C extends Comparable>
/*     */   extends Object
/*     */   implements RangeSet<C>
/*     */ {
/*  29 */   public boolean contains(C value) { return (rangeContaining(value) != null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Range<C> rangeContaining(C paramC);
/*     */ 
/*     */ 
/*     */   
/*  37 */   public boolean isEmpty() { return asRanges().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public void add(Range<C> range) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public void remove(Range<C> range) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public void clear() { remove(Range.all()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean enclosesAll(RangeSet<C> other) {
/*  57 */     for (Range<C> range : other.asRanges()) {
/*  58 */       if (!encloses(range)) {
/*  59 */         return false;
/*     */       }
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(RangeSet<C> other) {
/*  67 */     for (Range<C> range : other.asRanges()) {
/*  68 */       add(range);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll(RangeSet<C> other) {
/*  74 */     for (Range<C> range : other.asRanges()) {
/*  75 */       remove(range);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean encloses(Range<C> paramRange);
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/*  84 */     if (obj == this)
/*  85 */       return true; 
/*  86 */     if (obj instanceof RangeSet) {
/*  87 */       RangeSet<?> other = (RangeSet)obj;
/*  88 */       return asRanges().equals(other.asRanges());
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public final int hashCode() { return asRanges().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public final String toString() { return asRanges().toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractRangeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */