/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Future;
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
/*    */ public abstract class ForwardingListeningExecutorService
/*    */   extends ForwardingExecutorService
/*    */   implements ListeningExecutorService
/*    */ {
/* 40 */   public <T> ListenableFuture<T> submit(Callable<T> task) { return delegate().submit(task); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public ListenableFuture<?> submit(Runnable task) { return delegate().submit(task); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public <T> ListenableFuture<T> submit(Runnable task, T result) { return delegate().submit(task, result); }
/*    */   
/*    */   protected abstract ListeningExecutorService delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ForwardingListeningExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */