/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ final class GeneralRange<T>
/*     */   extends Object
/*     */   implements Serializable
/*     */ {
/*     */   private final Comparator<? super T> comparator;
/*     */   private final boolean hasLowerBound;
/*     */   @Nullable
/*     */   private final T lowerEndpoint;
/*     */   private final BoundType lowerBoundType;
/*     */   private final boolean hasUpperBound;
/*     */   @Nullable
/*     */   private final T upperEndpoint;
/*     */   private final BoundType upperBoundType;
/*     */   private GeneralRange<T> reverse;
/*     */   
/*     */   static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
/*  46 */     T lowerEndpoint = (T)(range.hasLowerBound() ? range.lowerEndpoint() : null);
/*  47 */     BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
/*     */ 
/*     */     
/*  50 */     T upperEndpoint = (T)(range.hasUpperBound() ? range.upperEndpoint() : null);
/*  51 */     BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
/*  52 */     return new GeneralRange(Ordering.natural(), range.hasLowerBound(), lowerEndpoint, lowerBoundType, range.hasUpperBound(), upperEndpoint, upperBoundType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   static <T> GeneralRange<T> all(Comparator<? super T> comparator) { return new GeneralRange(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) { return new GeneralRange(comparator, true, endpoint, boundType, false, null, BoundType.OPEN); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType) { return new GeneralRange(comparator, false, null, BoundType.OPEN, true, endpoint, boundType); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T lower, BoundType lowerType, @Nullable T upper, BoundType upperType) { return new GeneralRange(comparator, true, lower, lowerType, true, upper, upperType); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @Nullable T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @Nullable T upperEndpoint, BoundType upperBoundType) {
/* 103 */     this.comparator = (Comparator)Preconditions.checkNotNull(comparator);
/* 104 */     this.hasLowerBound = hasLowerBound;
/* 105 */     this.hasUpperBound = hasUpperBound;
/* 106 */     this.lowerEndpoint = lowerEndpoint;
/* 107 */     this.lowerBoundType = (BoundType)Preconditions.checkNotNull(lowerBoundType);
/* 108 */     this.upperEndpoint = upperEndpoint;
/* 109 */     this.upperBoundType = (BoundType)Preconditions.checkNotNull(upperBoundType);
/*     */     
/* 111 */     if (hasLowerBound) {
/* 112 */       comparator.compare(lowerEndpoint, lowerEndpoint);
/*     */     }
/* 114 */     if (hasUpperBound) {
/* 115 */       comparator.compare(upperEndpoint, upperEndpoint);
/*     */     }
/* 117 */     if (hasLowerBound && hasUpperBound) {
/* 118 */       int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
/*     */       
/* 120 */       Preconditions.checkArgument((cmp <= 0), "lowerEndpoint (%s) > upperEndpoint (%s)", new Object[] { lowerEndpoint, upperEndpoint });
/*     */       
/* 122 */       if (cmp == 0) {
/* 123 */         Preconditions.checkArgument(((lowerBoundType != BoundType.OPEN) ? 1 : 0) | ((upperBoundType != BoundType.OPEN) ? 1 : 0));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 129 */   Comparator<? super T> comparator() { return this.comparator; }
/*     */ 
/*     */ 
/*     */   
/* 133 */   boolean hasLowerBound() { return this.hasLowerBound; }
/*     */ 
/*     */ 
/*     */   
/* 137 */   boolean hasUpperBound() { return this.hasUpperBound; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   boolean isEmpty() { return ((hasUpperBound() && tooLow(getUpperEndpoint())) || (hasLowerBound() && tooHigh(getLowerEndpoint()))); }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean tooLow(@Nullable T t) {
/* 146 */     if (!hasLowerBound()) {
/* 147 */       return false;
/*     */     }
/* 149 */     T lbound = (T)getLowerEndpoint();
/* 150 */     int cmp = this.comparator.compare(t, lbound);
/* 151 */     return ((cmp < 0) ? 1 : 0) | ((cmp == 0) ? 1 : 0) & ((getLowerBoundType() == BoundType.OPEN) ? 1 : 0);
/*     */   }
/*     */   
/*     */   boolean tooHigh(@Nullable T t) {
/* 155 */     if (!hasUpperBound()) {
/* 156 */       return false;
/*     */     }
/* 158 */     T ubound = (T)getUpperEndpoint();
/* 159 */     int cmp = this.comparator.compare(t, ubound);
/* 160 */     return ((cmp > 0) ? 1 : 0) | ((cmp == 0) ? 1 : 0) & ((getUpperBoundType() == BoundType.OPEN) ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/* 164 */   boolean contains(@Nullable T t) { return (!tooLow(t) && !tooHigh(t)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> intersect(GeneralRange<T> other) {
/* 171 */     Preconditions.checkNotNull(other);
/* 172 */     Preconditions.checkArgument(this.comparator.equals(other.comparator));
/*     */     
/* 174 */     boolean hasLowBound = this.hasLowerBound;
/*     */     
/* 176 */     T lowEnd = (T)getLowerEndpoint();
/* 177 */     BoundType lowType = getLowerBoundType();
/* 178 */     if (!hasLowerBound()) {
/* 179 */       hasLowBound = other.hasLowerBound;
/* 180 */       lowEnd = (T)other.getLowerEndpoint();
/* 181 */       lowType = other.getLowerBoundType();
/* 182 */     } else if (other.hasLowerBound()) {
/* 183 */       int cmp = this.comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
/* 184 */       if (cmp < 0 || (cmp == 0 && other.getLowerBoundType() == BoundType.OPEN)) {
/* 185 */         lowEnd = (T)other.getLowerEndpoint();
/* 186 */         lowType = other.getLowerBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 190 */     boolean hasUpBound = this.hasUpperBound;
/*     */     
/* 192 */     T upEnd = (T)getUpperEndpoint();
/* 193 */     BoundType upType = getUpperBoundType();
/* 194 */     if (!hasUpperBound()) {
/* 195 */       hasUpBound = other.hasUpperBound;
/* 196 */       upEnd = (T)other.getUpperEndpoint();
/* 197 */       upType = other.getUpperBoundType();
/* 198 */     } else if (other.hasUpperBound()) {
/* 199 */       int cmp = this.comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
/* 200 */       if (cmp > 0 || (cmp == 0 && other.getUpperBoundType() == BoundType.OPEN)) {
/* 201 */         upEnd = (T)other.getUpperEndpoint();
/* 202 */         upType = other.getUpperBoundType();
/*     */       } 
/*     */     } 
/*     */     
/* 206 */     if (hasLowBound && hasUpBound) {
/* 207 */       int cmp = this.comparator.compare(lowEnd, upEnd);
/* 208 */       if (cmp > 0 || (cmp == 0 && lowType == BoundType.OPEN && upType == BoundType.OPEN)) {
/*     */         
/* 210 */         lowEnd = upEnd;
/* 211 */         lowType = BoundType.OPEN;
/* 212 */         upType = BoundType.CLOSED;
/*     */       } 
/*     */     } 
/*     */     
/* 216 */     return new GeneralRange(this.comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 221 */     if (obj instanceof GeneralRange) {
/* 222 */       GeneralRange<?> r = (GeneralRange)obj;
/* 223 */       return (this.comparator.equals(r.comparator) && this.hasLowerBound == r.hasLowerBound && this.hasUpperBound == r.hasUpperBound && getLowerBoundType().equals(r.getLowerBoundType()) && getUpperBoundType().equals(r.getUpperBoundType()) && Objects.equal(getLowerEndpoint(), r.getLowerEndpoint()) && Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 234 */   public int hashCode() { return Objects.hashCode(new Object[] { this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType() }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralRange<T> reverse() {
/* 244 */     GeneralRange<T> result = this.reverse;
/* 245 */     if (result == null) {
/* 246 */       result = new GeneralRange<T>(Ordering.from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
/*     */ 
/*     */       
/* 249 */       result.reverse = this;
/* 250 */       return this.reverse = result;
/*     */     } 
/* 252 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 257 */   public String toString() { return this.comparator + ":" + ((this.lowerBoundType == BoundType.CLOSED) ? 91 : 40) + (this.hasLowerBound ? this.lowerEndpoint : "-∞") + ',' + (this.hasUpperBound ? this.upperEndpoint : "∞") + ((this.upperBoundType == BoundType.CLOSED) ? 93 : 41); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   T getLowerEndpoint() { return (T)this.lowerEndpoint; }
/*     */ 
/*     */ 
/*     */   
/* 273 */   BoundType getLowerBoundType() { return this.lowerBoundType; }
/*     */ 
/*     */ 
/*     */   
/* 277 */   T getUpperEndpoint() { return (T)this.upperEndpoint; }
/*     */ 
/*     */ 
/*     */   
/* 281 */   BoundType getUpperBoundType() { return this.upperBoundType; }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/GeneralRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */