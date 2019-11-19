/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ public abstract class Converter<A, B>
/*     */   extends Object
/*     */   implements Function<A, B>
/*     */ {
/*     */   private final boolean handleNullAutomatically;
/*     */   private Converter<B, A> reverse;
/*     */   
/* 103 */   protected Converter() { this(true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   Converter(boolean handleNullAutomatically) { this.handleNullAutomatically = handleNullAutomatically; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract B doForward(A paramA);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract A doBackward(B paramB);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/* 147 */   public final B convert(@Nullable A a) { return (B)correctedDoForward(a); }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   B correctedDoForward(@Nullable A a) {
/* 152 */     if (this.handleNullAutomatically)
/*     */     {
/* 154 */       return (B)((a == null) ? null : Preconditions.checkNotNull(doForward(a)));
/*     */     }
/* 156 */     return (B)doForward(a);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   A correctedDoBackward(@Nullable B b) {
/* 162 */     if (this.handleNullAutomatically)
/*     */     {
/* 164 */       return (A)((b == null) ? null : Preconditions.checkNotNull(doBackward(b)));
/*     */     }
/* 166 */     return (A)doBackward(b);
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
/*     */   public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
/* 179 */     Preconditions.checkNotNull(fromIterable, "fromIterable");
/* 180 */     return new Iterable<B>() {
/*     */         public Iterator<B> iterator() {
/* 182 */           return new Iterator<B>()
/*     */             {
/*     */               private final Iterator<? extends A> fromIterator;
/*     */ 
/*     */               
/* 187 */               public boolean hasNext() { return super.fromIterator.hasNext(); }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 192 */               public B next() { return (B)Converter.null.this.this$0.convert(super.fromIterator.next()); }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 197 */               public void remove() { super.fromIterator.remove(); }
/*     */             };
/*     */         }
/*     */       };
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
/*     */   public Converter<B, A> reverse() {
/* 212 */     Converter<B, A> result = this.reverse;
/* 213 */     return (result == null) ? (this.reverse = new ReverseConverter(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class ReverseConverter<A, B>
/*     */     extends Converter<B, A> implements Serializable {
/*     */     final Converter<A, B> original;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 221 */     ReverseConverter(Converter<A, B> original) { this.original = original; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     protected A doForward(B b) { throw new AssertionError(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     protected B doBackward(A a) { throw new AssertionError(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 244 */     A correctedDoForward(@Nullable B b) { return (A)this.original.correctedDoBackward(b); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 250 */     B correctedDoBackward(@Nullable A a) { return (B)this.original.correctedDoForward(a); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     public Converter<A, B> reverse() { return this.original; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 260 */       if (object instanceof ReverseConverter) {
/* 261 */         ReverseConverter<?, ?> that = (ReverseConverter)object;
/* 262 */         return this.original.equals(that.original);
/*     */       } 
/* 264 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 269 */     public int hashCode() { return this.original.hashCode() ^ 0xFFFFFFFF; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     public String toString() { return this.original + ".reverse()"; }
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
/* 288 */   public <C> Converter<A, C> andThen(Converter<B, C> secondConverter) { return new ConverterComposition(this, (Converter)Preconditions.checkNotNull(secondConverter)); }
/*     */   
/*     */   private static final class ConverterComposition<A, B, C>
/*     */     extends Converter<A, C> implements Serializable {
/*     */     final Converter<A, B> first;
/*     */     final Converter<B, C> second;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
/* 297 */       this.first = first;
/* 298 */       this.second = second;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     protected C doForward(A a) { throw new AssertionError(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     protected A doBackward(C c) { throw new AssertionError(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 321 */     C correctedDoForward(@Nullable A a) { return (C)this.second.correctedDoForward(this.first.correctedDoForward(a)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/* 327 */     A correctedDoBackward(@Nullable C c) { return (A)this.first.correctedDoBackward(this.second.correctedDoBackward(c)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 332 */       if (object instanceof ConverterComposition) {
/* 333 */         ConverterComposition<?, ?, ?> that = (ConverterComposition)object;
/* 334 */         return (this.first.equals(that.first) && this.second.equals(that.second));
/*     */       } 
/*     */       
/* 337 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 342 */     public int hashCode() { return 31 * this.first.hashCode() + this.second.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     public String toString() { return this.first + ".andThen(" + this.second + ")"; }
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
/*     */   @Deprecated
/*     */   @Nullable
/* 360 */   public final B apply(@Nullable A a) { return (B)convert(a); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   public boolean equals(@Nullable Object object) { return super.equals(object); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 398 */   public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) { return new FunctionBasedConverter(forwardFunction, backwardFunction, null); }
/*     */ 
/*     */   
/*     */   private static final class FunctionBasedConverter<A, B>
/*     */     extends Converter<A, B>
/*     */     implements Serializable
/*     */   {
/*     */     private final Function<? super A, ? extends B> forwardFunction;
/*     */     private final Function<? super B, ? extends A> backwardFunction;
/*     */     
/*     */     private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 409 */       this.forwardFunction = (Function)Preconditions.checkNotNull(forwardFunction);
/* 410 */       this.backwardFunction = (Function)Preconditions.checkNotNull(backwardFunction);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 415 */     protected B doForward(A a) { return (B)this.forwardFunction.apply(a); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 420 */     protected A doBackward(B b) { return (A)this.backwardFunction.apply(b); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 425 */       if (object instanceof FunctionBasedConverter) {
/* 426 */         FunctionBasedConverter<?, ?> that = (FunctionBasedConverter)object;
/* 427 */         return (this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction.equals(that.backwardFunction));
/*     */       } 
/*     */       
/* 430 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 435 */     public int hashCode() { return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 440 */     public String toString() { return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")"; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 449 */   public static <T> Converter<T, T> identity() { return IdentityConverter.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class IdentityConverter<T>
/*     */     extends Converter<T, T>
/*     */     implements Serializable
/*     */   {
/* 457 */     static final IdentityConverter INSTANCE = new IdentityConverter();
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 461 */     protected T doForward(T t) { return t; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     protected T doBackward(T t) { return t; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 471 */     public IdentityConverter<T> reverse() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 476 */     public <S> Converter<T, S> andThen(Converter<T, S> otherConverter) { return (Converter)Preconditions.checkNotNull(otherConverter, "otherConverter"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 486 */     public String toString() { return "Converter.identity()"; }
/*     */ 
/*     */ 
/*     */     
/* 490 */     private Object readResolve() { return INSTANCE; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Converter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */