/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ @Beta
/*    */ public final class FakeTimeLimiter
/*    */   implements TimeLimiter
/*    */ {
/*    */   public <T> T newProxy(T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit) {
/* 41 */     Preconditions.checkNotNull(target);
/* 42 */     Preconditions.checkNotNull(interfaceType);
/* 43 */     Preconditions.checkNotNull(timeoutUnit);
/* 44 */     return target;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
/* 50 */     Preconditions.checkNotNull(timeoutUnit);
/* 51 */     return (T)callable.call();
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/FakeTimeLimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */