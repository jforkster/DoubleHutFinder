/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible
/*     */ public abstract class Equivalence<T>
/*     */   extends Object
/*     */ {
/*     */   public final boolean equivalent(@Nullable T a, @Nullable T b) {
/*  65 */     if (a == b) {
/*  66 */       return true;
/*     */     }
/*  68 */     if (a == null || b == null) {
/*  69 */       return false;
/*     */     }
/*  71 */     return doEquivalent(a, b);
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
/*     */   protected abstract boolean doEquivalent(T paramT1, T paramT2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hash(@Nullable T t) {
/* 101 */     if (t == null) {
/* 102 */       return 0;
/*     */     }
/* 104 */     return doHash(t);
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
/*     */   protected abstract int doHash(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) { return new FunctionalEquivalence(function, this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public final <S extends T> Wrapper<S> wrap(@Nullable S reference) { return new Wrapper(this, reference, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Wrapper<T>
/*     */     extends Object
/*     */     implements Serializable
/*     */   {
/*     */     private final Equivalence<? super T> equivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final T reference;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Wrapper(Equivalence<? super T> equivalence, @Nullable T reference) {
/* 177 */       this.equivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
/* 178 */       this.reference = reference;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/* 183 */     public T get() { return (T)this.reference; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 192 */       if (obj == this) {
/* 193 */         return true;
/*     */       }
/* 195 */       if (obj instanceof Wrapper) {
/* 196 */         Wrapper<?> that = (Wrapper)obj;
/*     */         
/* 198 */         if (this.equivalence.equals(that.equivalence)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 204 */           Equivalence<Object> equivalence = this.equivalence;
/* 205 */           return equivalence.equivalent(this.reference, that.reference);
/*     */         } 
/*     */       } 
/* 208 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     public int hashCode() { return this.equivalence.hash(this.reference); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     public String toString() { return this.equivalence + ".wrap(" + this.reference + ")"; }
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
/*     */   @GwtCompatible(serializable = true)
/* 244 */   public final <S extends T> Equivalence<Iterable<S>> pairwise() { return new PairwiseEquivalence(this); }
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
/* 255 */   public final Predicate<T> equivalentTo(@Nullable T target) { return new EquivalentToPredicate(this, target); }
/*     */   
/*     */   private static final class EquivalentToPredicate<T> extends Object implements Predicate<T>, Serializable {
/*     */     private final Equivalence<T> equivalence;
/*     */     @Nullable
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T target) {
/* 264 */       this.equivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
/* 265 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/* 269 */     public boolean apply(@Nullable T input) { return this.equivalence.equivalent(input, this.target); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 273 */       if (this == obj) {
/* 274 */         return true;
/*     */       }
/* 276 */       if (obj instanceof EquivalentToPredicate) {
/* 277 */         EquivalentToPredicate<?> that = (EquivalentToPredicate)obj;
/* 278 */         return (this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target));
/*     */       } 
/*     */       
/* 281 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 285 */     public int hashCode() { return Objects.hashCode(new Object[] { this.equivalence, this.target }); }
/*     */ 
/*     */ 
/*     */     
/* 289 */     public String toString() { return this.equivalence + ".equivalentTo(" + this.target + ")"; }
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
/* 306 */   public static Equivalence<Object> equals() { return Equals.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 318 */   public static Equivalence<Object> identity() { return Identity.INSTANCE; }
/*     */   
/*     */   static final class Equals
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 324 */     static final Equals INSTANCE = new Equals();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 327 */     protected boolean doEquivalent(Object a, Object b) { return a.equals(b); }
/*     */ 
/*     */     
/* 330 */     public int doHash(Object o) { return o.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 334 */     private Object readResolve() { return INSTANCE; }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Identity
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 342 */     static final Identity INSTANCE = new Identity();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/* 345 */     protected boolean doEquivalent(Object a, Object b) { return false; }
/*     */ 
/*     */ 
/*     */     
/* 349 */     protected int doHash(Object o) { return System.identityHashCode(o); }
/*     */ 
/*     */ 
/*     */     
/* 353 */     private Object readResolve() { return INSTANCE; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Equivalence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */