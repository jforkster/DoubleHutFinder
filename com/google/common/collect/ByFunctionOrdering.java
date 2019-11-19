/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ByFunctionOrdering<F, T>
/*    */   extends Ordering<F>
/*    */   implements Serializable
/*    */ {
/*    */   final Function<F, ? extends T> function;
/*    */   final Ordering<T> ordering;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering) {
/* 41 */     this.function = (Function)Preconditions.checkNotNull(function);
/* 42 */     this.ordering = (Ordering)Preconditions.checkNotNull(ordering);
/*    */   }
/*    */ 
/*    */   
/* 46 */   public int compare(F left, F right) { return this.ordering.compare(this.function.apply(left), this.function.apply(right)); }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 50 */     if (object == this) {
/* 51 */       return true;
/*    */     }
/* 53 */     if (object instanceof ByFunctionOrdering) {
/* 54 */       ByFunctionOrdering<?, ?> that = (ByFunctionOrdering)object;
/* 55 */       return (this.function.equals(that.function) && this.ordering.equals(that.ordering));
/*    */     } 
/*    */     
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 62 */   public int hashCode() { return Objects.hashCode(new Object[] { this.function, this.ordering }); }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public String toString() { return this.ordering + ".onResultOf(" + this.function + ")"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ByFunctionOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */