/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ComparatorOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<T> comparator;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 34 */   ComparatorOrdering(Comparator<T> comparator) { this.comparator = (Comparator)Preconditions.checkNotNull(comparator); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public int compare(T a, T b) { return this.comparator.compare(a, b); }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 42 */     if (object == this) {
/* 43 */       return true;
/*    */     }
/* 45 */     if (object instanceof ComparatorOrdering) {
/* 46 */       ComparatorOrdering<?> that = (ComparatorOrdering)object;
/* 47 */       return this.comparator.equals(that.comparator);
/*    */     } 
/* 49 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 53 */   public int hashCode() { return this.comparator.hashCode(); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String toString() { return this.comparator.toString(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ComparatorOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */