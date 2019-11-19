/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ final class NaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 30 */   static final NaturalOrdering INSTANCE = new NaturalOrdering();
/*    */   
/*    */   public int compare(Comparable left, Comparable right) {
/* 33 */     Preconditions.checkNotNull(left);
/* 34 */     Preconditions.checkNotNull(right);
/* 35 */     return left.compareTo(right);
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 39 */   public <S extends Comparable> Ordering<S> reverse() { return ReverseNaturalOrdering.INSTANCE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   private Object readResolve() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public String toString() { return "Ordering.natural()"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/NaturalOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */