/*    */ package com.google.common.base;
/*    */ 
/*    */ import java.lang.ref.PhantomReference;
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
/*    */ 
/*    */ 
/*    */ public abstract class FinalizablePhantomReference<T>
/*    */   extends PhantomReference<T>
/*    */   implements FinalizableReference
/*    */ {
/*    */   protected FinalizablePhantomReference(T referent, FinalizableReferenceQueue queue) {
/* 41 */     super(referent, queue.queue);
/* 42 */     queue.cleanUp();
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/FinalizablePhantomReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */