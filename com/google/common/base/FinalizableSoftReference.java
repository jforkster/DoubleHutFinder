/*    */ package com.google.common.base;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
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
/*    */ 
/*    */ 
/*    */ public abstract class FinalizableSoftReference<T>
/*    */   extends SoftReference<T>
/*    */   implements FinalizableReference
/*    */ {
/*    */   protected FinalizableSoftReference(T referent, FinalizableReferenceQueue queue) {
/* 39 */     super(referent, queue.queue);
/* 40 */     queue.cleanUp();
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/FinalizableSoftReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */