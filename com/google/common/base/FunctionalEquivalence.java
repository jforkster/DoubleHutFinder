/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ final class FunctionalEquivalence<F, T>
/*    */   extends Equivalence<F>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   private final Function<F, ? extends T> function;
/*    */   private final Equivalence<T> resultEquivalence;
/*    */   
/*    */   FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
/* 46 */     this.function = (Function)Preconditions.checkNotNull(function);
/* 47 */     this.resultEquivalence = (Equivalence)Preconditions.checkNotNull(resultEquivalence);
/*    */   }
/*    */ 
/*    */   
/* 51 */   protected boolean doEquivalent(F a, F b) { return this.resultEquivalence.equivalent(this.function.apply(a), this.function.apply(b)); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected int doHash(F a) { return this.resultEquivalence.hash(this.function.apply(a)); }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object obj) {
/* 59 */     if (obj == this) {
/* 60 */       return true;
/*    */     }
/* 62 */     if (obj instanceof FunctionalEquivalence) {
/* 63 */       FunctionalEquivalence<?, ?> that = (FunctionalEquivalence)obj;
/* 64 */       return (this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence));
/*    */     } 
/*    */     
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 71 */   public int hashCode() { return Objects.hashCode(new Object[] { this.function, this.resultEquivalence }); }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public String toString() { return this.resultEquivalence + ".onResultOf(" + this.function + ")"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/FunctionalEquivalence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */