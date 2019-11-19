/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ final class CompoundOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final ImmutableList<Comparator<? super T>> comparators;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 31 */   CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) { this.comparators = ImmutableList.of(primary, secondary); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) { this.comparators = ImmutableList.copyOf(comparators); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 41 */     int size = this.comparators.size();
/* 42 */     for (int i = 0; i < size; i++) {
/* 43 */       int result = ((Comparator)this.comparators.get(i)).compare(left, right);
/* 44 */       if (result != 0) {
/* 45 */         return result;
/*    */       }
/*    */     } 
/* 48 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean equals(Object object) {
/* 52 */     if (object == this) {
/* 53 */       return true;
/*    */     }
/* 55 */     if (object instanceof CompoundOrdering) {
/* 56 */       CompoundOrdering<?> that = (CompoundOrdering)object;
/* 57 */       return this.comparators.equals(that.comparators);
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 63 */   public int hashCode() { return this.comparators.hashCode(); }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public String toString() { return "Ordering.compound(" + this.comparators + ")"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/CompoundOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */