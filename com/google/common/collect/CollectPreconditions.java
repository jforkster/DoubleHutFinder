/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @GwtCompatible
/*    */ final class CollectPreconditions
/*    */ {
/*    */   static void checkEntryNotNull(Object key, Object value) {
/* 30 */     if (key == null)
/* 31 */       throw new NullPointerException("null key in entry: null=" + value); 
/* 32 */     if (value == null) {
/* 33 */       throw new NullPointerException("null value in entry: " + key + "=null");
/*    */     }
/*    */   }
/*    */   
/*    */   static int checkNonnegative(int value, String name) {
/* 38 */     if (value < 0) {
/* 39 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 41 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   static void checkRemove(boolean canRemove) { Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()"); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/CollectPreconditions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */