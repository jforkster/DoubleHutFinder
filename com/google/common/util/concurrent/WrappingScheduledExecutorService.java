/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ScheduledFuture;
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
/*    */ abstract class WrappingScheduledExecutorService
/*    */   extends WrappingExecutorService
/*    */   implements ScheduledExecutorService
/*    */ {
/*    */   final ScheduledExecutorService delegate;
/*    */   
/*    */   protected WrappingScheduledExecutorService(ScheduledExecutorService delegate) {
/* 36 */     super(delegate);
/* 37 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public final ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) { return this.delegate.schedule(wrapTask(command), delay, unit); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public final <V> ScheduledFuture<V> schedule(Callable<V> task, long delay, TimeUnit unit) { return this.delegate.schedule(wrapTask(task), delay, unit); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public final ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) { return this.delegate.scheduleAtFixedRate(wrapTask(command), initialDelay, period, unit); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public final ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) { return this.delegate.scheduleWithFixedDelay(wrapTask(command), initialDelay, delay, unit); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/WrappingScheduledExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */