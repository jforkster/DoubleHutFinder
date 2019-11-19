/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class RegularContiguousSet<C extends Comparable>
/*     */   extends ContiguousSet<C>
/*     */ {
/*     */   private final Range<C> range;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain) {
/*  40 */     super(domain);
/*  41 */     this.range = range;
/*     */   }
/*     */ 
/*     */   
/*  45 */   private ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) { return this.range.isConnected(other) ? ContiguousSet.create(this.range.intersection(other), this.domain) : new EmptyContiguousSet(this.domain); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) { return intersectionInCurrentDomain(Range.upTo(toElement, BoundType.forBoolean(inclusive))); }
/*     */ 
/*     */ 
/*     */   
/*     */   ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
/*  56 */     if (fromElement.compareTo(toElement) == 0 && !fromInclusive && !toInclusive)
/*     */     {
/*  58 */       return new EmptyContiguousSet(this.domain);
/*     */     }
/*  60 */     return intersectionInCurrentDomain(Range.range(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) { return intersectionInCurrentDomain(Range.downTo(fromElement, BoundType.forBoolean(inclusive))); }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("not used by GWT emulation")
/*  71 */   int indexOf(Object target) { return contains(target) ? (int)this.domain.distance(first(), (Comparable)target) : -1; }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<C> iterator() {
/*  75 */     return new AbstractSequentialIterator<C>(first())
/*     */       {
/*     */         final C last;
/*     */ 
/*     */         
/*  80 */         protected C computeNext(C previous) { return (C)(RegularContiguousSet.equalsOrThrow(previous, super.last) ? null : RegularContiguousSet.this.domain.next(previous)); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public UnmodifiableIterator<C> descendingIterator() {
/*  87 */     return new AbstractSequentialIterator<C>(last())
/*     */       {
/*     */         final C first;
/*     */ 
/*     */         
/*  92 */         protected C computeNext(C previous) { return (C)(RegularContiguousSet.equalsOrThrow(previous, super.first) ? null : RegularContiguousSet.this.domain.previous(previous)); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static boolean equalsOrThrow(Comparable<?> left, @Nullable Comparable<?> right) { return (right != null && Range.compareOrThrow(left, right) == 0); }
/*     */ 
/*     */ 
/*     */   
/* 102 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public C first() { return (C)this.range.lowerBound.leastValueAbove(this.domain); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public C last() { return (C)this.range.upperBound.greatestValueBelow(this.domain); }
/*     */ 
/*     */   
/*     */   public int size() {
/* 114 */     long distance = this.domain.distance(first(), last());
/* 115 */     return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int)distance + 1);
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/* 119 */     if (object == null) {
/* 120 */       return false;
/*     */     }
/*     */     try {
/* 123 */       return this.range.contains((Comparable)object);
/* 124 */     } catch (ClassCastException e) {
/* 125 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 130 */   public boolean containsAll(Collection<?> targets) { return Collections2.containsAllImpl(this, targets); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public boolean isEmpty() { return false; }
/*     */ 
/*     */   
/*     */   public ContiguousSet<C> intersection(ContiguousSet<C> other) {
/* 138 */     Preconditions.checkNotNull(other);
/* 139 */     Preconditions.checkArgument(this.domain.equals(other.domain));
/* 140 */     if (other.isEmpty()) {
/* 141 */       return other;
/*     */     }
/* 143 */     C lowerEndpoint = (C)(Comparable)Ordering.natural().max(first(), other.first());
/* 144 */     C upperEndpoint = (C)(Comparable)Ordering.natural().min(last(), other.last());
/* 145 */     return (lowerEndpoint.compareTo(upperEndpoint) < 0) ? ContiguousSet.create(Range.closed(lowerEndpoint, upperEndpoint), this.domain) : new EmptyContiguousSet(this.domain);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public Range<C> range() { return range(BoundType.CLOSED, BoundType.CLOSED); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) { return Range.create(this.range.lowerBound.withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound.withUpperBoundType(upperBoundType, this.domain)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 161 */     if (object == this)
/* 162 */       return true; 
/* 163 */     if (object instanceof RegularContiguousSet) {
/* 164 */       RegularContiguousSet<?> that = (RegularContiguousSet)object;
/* 165 */       if (this.domain.equals(that.domain)) {
/* 166 */         return (first().equals(that.first()) && last().equals(that.last()));
/*     */       }
/*     */     } 
/*     */     
/* 170 */     return super.equals(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public int hashCode() { return Sets.hashCodeImpl(this); }
/*     */   
/*     */   @GwtIncompatible("serialization")
/*     */   private static final class SerializedForm<C extends Comparable>
/*     */     extends Object implements Serializable {
/*     */     final Range<C> range;
/*     */     final DiscreteDomain<C> domain;
/*     */     
/*     */     private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
/* 184 */       this.range = range;
/* 185 */       this.domain = domain;
/*     */     }
/*     */ 
/*     */     
/* 189 */     private Object readResolve() { return new RegularContiguousSet(this.range, this.domain); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("serialization")
/* 195 */   Object writeReplace() { return new SerializedForm(this.range, this.domain, null); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularContiguousSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */