/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
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
/*    */ final class SynchronizedEventSubscriber
/*    */   extends EventSubscriber
/*    */ {
/* 40 */   public SynchronizedEventSubscriber(Object target, Method method) { super(target, method); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleEvent(Object event) throws InvocationTargetException {
/* 46 */     synchronized (this) {
/* 47 */       super.handleEvent(event);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/eventbus/SynchronizedEventSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */