/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Range<C extends Comparable>
/*     */   extends Object
/*     */   implements Predicate<C>, Serializable
/*     */ {
/* 117 */   private static final Function<Range, Cut> LOWER_BOUND_FN = new Function<Range, Cut>()
/*     */     {
/*     */       public Cut apply(Range range) {
/* 120 */         return range.lowerBound;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 126 */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() { return LOWER_BOUND_FN; }
/*     */ 
/*     */   
/* 129 */   private static final Function<Range, Cut> UPPER_BOUND_FN = new Function<Range, Cut>()
/*     */     {
/*     */       public Cut apply(Range range) {
/* 132 */         return range.upperBound;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 138 */   static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() { return UPPER_BOUND_FN; }
/*     */ 
/*     */   
/* 141 */   static final Ordering<Range<?>> RANGE_LEX_ORDERING = new Ordering<Range<?>>()
/*     */     {
/*     */       public int compare(Range<?> left, Range<?> right) {
/* 144 */         return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) { return new Range(lowerBound, upperBound); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public static <C extends Comparable<?>> Range<C> open(C lower, C upper) { return create(Cut.aboveValue(lower), Cut.belowValue(upper)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) { return create(Cut.belowValue(lower), Cut.aboveValue(upper)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) { return create(Cut.belowValue(lower), Cut.belowValue(upper)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) { return create(Cut.aboveValue(lower), Cut.aboveValue(upper)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
/* 217 */     Preconditions.checkNotNull(lowerType);
/* 218 */     Preconditions.checkNotNull(upperType);
/*     */     
/* 220 */     Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? Cut.aboveValue(lower) : Cut.belowValue(lower);
/*     */ 
/*     */     
/* 223 */     Cut<C> upperBound = (upperType == BoundType.OPEN) ? Cut.belowValue(upper) : Cut.aboveValue(upper);
/*     */ 
/*     */     
/* 226 */     return create(lowerBound, upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) { return create(Cut.belowAll(), Cut.belowValue(endpoint)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 246 */   public static <C extends Comparable<?>> Range<C> atMost(C endpoint) { return create(Cut.belowAll(), Cut.aboveValue(endpoint)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
/* 257 */     switch (boundType) {
/*     */       case OPEN:
/* 259 */         return lessThan(endpoint);
/*     */       case CLOSED:
/* 261 */         return atMost(endpoint);
/*     */     } 
/* 263 */     throw new AssertionError();
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
/* 274 */   public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) { return create(Cut.aboveValue(endpoint), Cut.aboveAll()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) { return create(Cut.belowValue(endpoint), Cut.aboveAll()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
/* 295 */     switch (boundType) {
/*     */       case OPEN:
/* 297 */         return greaterThan(endpoint);
/*     */       case CLOSED:
/* 299 */         return atLeast(endpoint);
/*     */     } 
/* 301 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   
/* 305 */   private static final Range<Comparable> ALL = new Range(Cut.belowAll(), Cut.aboveAll());
/*     */ 
/*     */   
/*     */   final Cut<C> lowerBound;
/*     */   
/*     */   final Cut<C> upperBound;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/* 315 */   public static <C extends Comparable<?>> Range<C> all() { return ALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   public static <C extends Comparable<?>> Range<C> singleton(C value) { return closed(value, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
/* 342 */     Preconditions.checkNotNull(values);
/* 343 */     if (values instanceof ContiguousSet) {
/* 344 */       return ((ContiguousSet)values).range();
/*     */     }
/* 346 */     Iterator<C> valueIterator = values.iterator();
/* 347 */     C min = (C)(Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 348 */     C max = min;
/* 349 */     while (valueIterator.hasNext()) {
/* 350 */       C value = (C)(Comparable)Preconditions.checkNotNull(valueIterator.next());
/* 351 */       min = (C)(Comparable)Ordering.natural().min(min, value);
/* 352 */       max = (C)(Comparable)Ordering.natural().max(max, value);
/*     */     } 
/* 354 */     return closed(min, max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Range(Cut<C> lowerBound, Cut<C> upperBound) {
/* 361 */     if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.aboveAll() || upperBound == Cut.belowAll())
/*     */     {
/* 363 */       throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
/*     */     }
/* 365 */     this.lowerBound = (Cut)Preconditions.checkNotNull(lowerBound);
/* 366 */     this.upperBound = (Cut)Preconditions.checkNotNull(upperBound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 373 */   public boolean hasLowerBound() { return (this.lowerBound != Cut.belowAll()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 383 */   public C lowerEndpoint() { return (C)this.lowerBound.endpoint(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 394 */   public BoundType lowerBoundType() { return this.lowerBound.typeAsLowerBound(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 401 */   public boolean hasUpperBound() { return (this.upperBound != Cut.aboveAll()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 411 */   public C upperEndpoint() { return (C)this.upperBound.endpoint(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 422 */   public BoundType upperBoundType() { return this.upperBound.typeAsUpperBound(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 435 */   public boolean isEmpty() { return this.lowerBound.equals(this.upperBound); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(C value) {
/* 444 */     Preconditions.checkNotNull(value);
/*     */     
/* 446 */     return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 456 */   public boolean apply(C input) { return contains(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Iterable<? extends C> values) {
/* 464 */     if (Iterables.isEmpty(values)) {
/* 465 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 469 */     if (values instanceof SortedSet) {
/* 470 */       SortedSet<? extends C> set = cast(values);
/* 471 */       Comparator<?> comparator = set.comparator();
/* 472 */       if (Ordering.natural().equals(comparator) || comparator == null) {
/* 473 */         return (contains((Comparable)set.first()) && contains((Comparable)set.last()));
/*     */       }
/*     */     } 
/*     */     
/* 477 */     for (Iterator i$ = values.iterator(); i$.hasNext(); ) { C value = (C)(Comparable)i$.next();
/* 478 */       if (!contains(value)) {
/* 479 */         return false;
/*     */       } }
/*     */     
/* 482 */     return true;
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
/* 510 */   public boolean encloses(Range<C> other) { return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 539 */   public boolean isConnected(Range<C> other) { return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<C> intersection(Range<C> connectedRange) {
/* 560 */     int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
/* 561 */     int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
/* 562 */     if (lowerCmp >= 0 && upperCmp <= 0)
/* 563 */       return this; 
/* 564 */     if (lowerCmp <= 0 && upperCmp >= 0) {
/* 565 */       return connectedRange;
/*     */     }
/* 567 */     Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
/* 568 */     Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
/* 569 */     return create(newLower, newUpper);
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
/*     */   public Range<C> span(Range<C> other) {
/* 585 */     int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
/* 586 */     int upperCmp = this.upperBound.compareTo(other.upperBound);
/* 587 */     if (lowerCmp <= 0 && upperCmp >= 0)
/* 588 */       return this; 
/* 589 */     if (lowerCmp >= 0 && upperCmp <= 0) {
/* 590 */       return other;
/*     */     }
/* 592 */     Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
/* 593 */     Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
/* 594 */     return create(newLower, newUpper);
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
/*     */   public Range<C> canonical(DiscreteDomain<C> domain) {
/* 623 */     Preconditions.checkNotNull(domain);
/* 624 */     Cut<C> lower = this.lowerBound.canonical(domain);
/* 625 */     Cut<C> upper = this.upperBound.canonical(domain);
/* 626 */     return (lower == this.lowerBound && upper == this.upperBound) ? this : create(lower, upper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 637 */     if (object instanceof Range) {
/* 638 */       Range<?> other = (Range)object;
/* 639 */       return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
/*     */     } 
/*     */     
/* 642 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 647 */   public int hashCode() { return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 655 */   public String toString() { return toString(this.lowerBound, this.upperBound); }
/*     */ 
/*     */   
/*     */   private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
/* 659 */     StringBuilder sb = new StringBuilder(16);
/* 660 */     lowerBound.describeAsLowerBound(sb);
/* 661 */     sb.append('‥');
/* 662 */     upperBound.describeAsUpperBound(sb);
/* 663 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 670 */   private static <T> SortedSet<T> cast(Iterable<T> iterable) { return (SortedSet)iterable; }
/*     */ 
/*     */   
/*     */   Object readResolve() {
/* 674 */     if (equals(ALL)) {
/* 675 */       return all();
/*     */     }
/* 677 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 683 */   static int compareOrThrow(Comparable left, Comparable right) { return left.compareTo(right); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Range.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */