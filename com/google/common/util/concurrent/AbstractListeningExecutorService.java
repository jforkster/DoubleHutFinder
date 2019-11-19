/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.util.concurrent.AbstractExecutorService;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.RunnableFuture;
/*    */ import javax.annotation.Nullable;
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
/*    */ public abstract class AbstractListeningExecutorService
/*    */   extends AbstractExecutorService
/*    */   implements ListeningExecutorService
/*    */ {
/* 42 */   protected final <T> ListenableFutureTask<T> newTaskFor(Runnable runnable, T value) { return ListenableFutureTask.create(runnable, value); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   protected final <T> ListenableFutureTask<T> newTaskFor(Callable<T> callable) { return ListenableFutureTask.create(callable); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public ListenableFuture<?> submit(Runnable task) { return (ListenableFuture)super.submit(task); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public <T> ListenableFuture<T> submit(Runnable task, @Nullable T result) { return (ListenableFuture)super.submit(task, result); }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public <T> ListenableFuture<T> submit(Callable<T> task) { return (ListenableFuture)super.submit(task); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AbstractListeningExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */