/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible
/*     */ abstract class Cut<C extends Comparable>
/*     */   extends Object
/*     */   implements Comparable<Cut<C>>, Serializable
/*     */ {
/*     */   final C endpoint;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  41 */   Cut(@Nullable C endpoint) { this.endpoint = endpoint; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   Cut<C> canonical(DiscreteDomain<C> domain) { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Cut<C> that) {
/*  69 */     if (that == belowAll()) {
/*  70 */       return 1;
/*     */     }
/*  72 */     if (that == aboveAll()) {
/*  73 */       return -1;
/*     */     }
/*  75 */     int result = Range.compareOrThrow(this.endpoint, that.endpoint);
/*  76 */     if (result != 0) {
/*  77 */       return result;
/*     */     }
/*     */     
/*  80 */     return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  85 */   C endpoint() { return (C)this.endpoint; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  90 */     if (obj instanceof Cut) {
/*     */       
/*  92 */       Cut<C> that = (Cut)obj;
/*     */       try {
/*  94 */         int compareResult = compareTo(that);
/*  95 */         return (compareResult == 0);
/*  96 */       } catch (ClassCastException ignored) {}
/*     */     } 
/*     */     
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   static <C extends Comparable> Cut<C> belowAll() { return 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 113 */       INSTANCE; }
/* 114 */   private static final class BelowAll extends Cut<Comparable<?>> { private static final BelowAll INSTANCE = new BelowAll();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 117 */     private BelowAll() { super(null); }
/*     */ 
/*     */     
/* 120 */     Comparable<?> endpoint() { throw new IllegalStateException("range unbounded on this side"); }
/*     */ 
/*     */     
/* 123 */     boolean isLessThan(Comparable<?> value) { return true; }
/*     */ 
/*     */     
/* 126 */     BoundType typeAsLowerBound() { throw new IllegalStateException(); }
/*     */ 
/*     */     
/* 129 */     BoundType typeAsUpperBound() { throw new AssertionError("this statement should be unreachable"); }
/*     */ 
/*     */ 
/*     */     
/* 133 */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) { throw new IllegalStateException(); }
/*     */ 
/*     */ 
/*     */     
/* 137 */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) { throw new AssertionError("this statement should be unreachable"); }
/*     */ 
/*     */     
/* 140 */     void describeAsLowerBound(StringBuilder sb) { sb.append("(-∞"); }
/*     */ 
/*     */     
/* 143 */     void describeAsUpperBound(StringBuilder sb) { throw new AssertionError(); }
/*     */ 
/*     */ 
/*     */     
/* 147 */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) { return domain.minValue(); }
/*     */ 
/*     */ 
/*     */     
/* 151 */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) { throw new AssertionError(); }
/*     */ 
/*     */     
/*     */     Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> domain) {
/*     */       try {
/* 156 */         return Cut.belowValue(domain.minValue());
/* 157 */       } catch (NoSuchElementException e) {
/* 158 */         return this;
/*     */       } 
/*     */     }
/*     */     
/* 162 */     public int compareTo(Cut<Comparable<?>> o) { return (o == this) ? 0 : -1; }
/*     */ 
/*     */     
/* 165 */     public String toString() { return "-∞"; }
/*     */ 
/*     */     
/* 168 */     private Object readResolve() { return INSTANCE; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   static <C extends Comparable> Cut<C> aboveAll() { return 
/*     */ 
/*     */       
/* 182 */       INSTANCE; }
/* 183 */   private static final class AboveAll extends Cut<Comparable<?>> { private static final AboveAll INSTANCE = new AboveAll();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 186 */     private AboveAll() { super(null); }
/*     */ 
/*     */     
/* 189 */     Comparable<?> endpoint() { throw new IllegalStateException("range unbounded on this side"); }
/*     */ 
/*     */     
/* 192 */     boolean isLessThan(Comparable<?> value) { return false; }
/*     */ 
/*     */     
/* 195 */     BoundType typeAsLowerBound() { throw new AssertionError("this statement should be unreachable"); }
/*     */ 
/*     */     
/* 198 */     BoundType typeAsUpperBound() { throw new IllegalStateException(); }
/*     */ 
/*     */ 
/*     */     
/* 202 */     Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) { throw new AssertionError("this statement should be unreachable"); }
/*     */ 
/*     */ 
/*     */     
/* 206 */     Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> domain) { throw new IllegalStateException(); }
/*     */ 
/*     */     
/* 209 */     void describeAsLowerBound(StringBuilder sb) { throw new AssertionError(); }
/*     */ 
/*     */     
/* 212 */     void describeAsUpperBound(StringBuilder sb) { sb.append("+∞)"); }
/*     */ 
/*     */ 
/*     */     
/* 216 */     Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> domain) { throw new AssertionError(); }
/*     */ 
/*     */ 
/*     */     
/* 220 */     Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> domain) { return domain.maxValue(); }
/*     */ 
/*     */     
/* 223 */     public int compareTo(Cut<Comparable<?>> o) { return (o == this) ? 0 : 1; }
/*     */ 
/*     */     
/* 226 */     public String toString() { return "+∞"; }
/*     */ 
/*     */     
/* 229 */     private Object readResolve() { return INSTANCE; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 235 */   static <C extends Comparable> Cut<C> belowValue(C endpoint) { return new BelowValue(endpoint); }
/*     */   
/*     */   private static final class BelowValue<C extends Comparable> extends Cut<C> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 240 */     BelowValue(C endpoint) { super((Comparable)Preconditions.checkNotNull(endpoint)); }
/*     */ 
/*     */ 
/*     */     
/* 244 */     boolean isLessThan(C value) { return (Range.compareOrThrow(this.endpoint, value) <= 0); }
/*     */ 
/*     */     
/* 247 */     BoundType typeAsLowerBound() { return BoundType.CLOSED; }
/*     */ 
/*     */     
/* 250 */     BoundType typeAsUpperBound() { return BoundType.OPEN; }
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 253 */       switch (Cut.null.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */         case 1:
/* 255 */           return this;
/*     */         case 2:
/* 257 */           previous = (C)domain.previous(this.endpoint);
/* 258 */           return (previous == null) ? Cut.belowAll() : new Cut.AboveValue(previous);
/*     */       } 
/* 260 */       throw new AssertionError();
/*     */     }
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C previous;
/* 264 */       switch (Cut.null.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */         case 1:
/* 266 */           previous = (C)domain.previous(this.endpoint);
/* 267 */           return (previous == null) ? Cut.aboveAll() : new Cut.AboveValue(previous);
/*     */         case 2:
/* 269 */           return this;
/*     */       } 
/* 271 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/* 275 */     void describeAsLowerBound(StringBuilder sb) { sb.append('[').append(this.endpoint); }
/*     */ 
/*     */     
/* 278 */     void describeAsUpperBound(StringBuilder sb) { sb.append(this.endpoint).append(')'); }
/*     */ 
/*     */     
/* 281 */     C leastValueAbove(DiscreteDomain<C> domain) { return (C)this.endpoint; }
/*     */ 
/*     */     
/* 284 */     C greatestValueBelow(DiscreteDomain<C> domain) { return (C)domain.previous(this.endpoint); }
/*     */ 
/*     */     
/* 287 */     public int hashCode() { return this.endpoint.hashCode(); }
/*     */ 
/*     */     
/* 290 */     public String toString() { return "\\" + this.endpoint + "/"; } }
/*     */   
/*     */   abstract boolean isLessThan(C paramC);
/*     */   
/*     */   abstract BoundType typeAsLowerBound();
/*     */   
/* 296 */   static <C extends Comparable> Cut<C> aboveValue(C endpoint) { return new AboveValue(endpoint); } abstract BoundType typeAsUpperBound(); abstract Cut<C> withLowerBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain); abstract Cut<C> withUpperBoundType(BoundType paramBoundType, DiscreteDomain<C> paramDiscreteDomain);
/*     */   abstract void describeAsLowerBound(StringBuilder paramStringBuilder);
/*     */   abstract void describeAsUpperBound(StringBuilder paramStringBuilder);
/*     */   abstract C leastValueAbove(DiscreteDomain<C> paramDiscreteDomain);
/*     */   abstract C greatestValueBelow(DiscreteDomain<C> paramDiscreteDomain);
/* 301 */   private static final class AboveValue<C extends Comparable> extends Cut<C> { AboveValue(C endpoint) { super((Comparable)Preconditions.checkNotNull(endpoint)); }
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 305 */     boolean isLessThan(C value) { return (Range.compareOrThrow(this.endpoint, value) < 0); }
/*     */ 
/*     */     
/* 308 */     BoundType typeAsLowerBound() { return BoundType.OPEN; }
/*     */ 
/*     */     
/* 311 */     BoundType typeAsUpperBound() { return BoundType.CLOSED; }
/*     */     Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 314 */       switch (Cut.null.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */         case 2:
/* 316 */           return this;
/*     */         case 1:
/* 318 */           next = (C)domain.next(this.endpoint);
/* 319 */           return (next == null) ? Cut.belowAll() : belowValue(next);
/*     */       } 
/* 321 */       throw new AssertionError();
/*     */     }
/*     */     Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> domain) {
/*     */       C next;
/* 325 */       switch (Cut.null.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
/*     */         case 2:
/* 327 */           next = (C)domain.next(this.endpoint);
/* 328 */           return (next == null) ? Cut.aboveAll() : belowValue(next);
/*     */         case 1:
/* 330 */           return this;
/*     */       } 
/* 332 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/* 336 */     void describeAsLowerBound(StringBuilder sb) { sb.append('(').append(this.endpoint); }
/*     */ 
/*     */     
/* 339 */     void describeAsUpperBound(StringBuilder sb) { sb.append(this.endpoint).append(']'); }
/*     */ 
/*     */     
/* 342 */     C leastValueAbove(DiscreteDomain<C> domain) { return (C)domain.next(this.endpoint); }
/*     */ 
/*     */     
/* 345 */     C greatestValueBelow(DiscreteDomain<C> domain) { return (C)this.endpoint; }
/*     */     
/*     */     Cut<C> canonical(DiscreteDomain<C> domain) {
/* 348 */       C next = (C)leastValueAbove(domain);
/* 349 */       return (next != null) ? belowValue(next) : Cut.aboveAll();
/*     */     }
/*     */     
/* 352 */     public int hashCode() { return this.endpoint.hashCode() ^ 0xFFFFFFFF; }
/*     */ 
/*     */     
/* 355 */     public String toString() { return "/" + this.endpoint + "\\"; } }
/*     */ 
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Cut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */