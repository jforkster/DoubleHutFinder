/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class UsingToStringOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 30 */   static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 33 */   public int compare(Object left, Object right) { return left.toString().compareTo(right.toString()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   private Object readResolve() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public String toString() { return "Ordering.usingToString()"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/UsingToStringOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */