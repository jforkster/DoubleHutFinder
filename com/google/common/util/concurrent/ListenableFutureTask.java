/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.FutureTask;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListenableFutureTask<V>
/*    */   extends FutureTask<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/* 43 */   private final ExecutionList executionList = new ExecutionList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static <V> ListenableFutureTask<V> create(Callable<V> callable) { return new ListenableFutureTask(callable); }
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
/* 70 */   public static <V> ListenableFutureTask<V> create(Runnable runnable, @Nullable V result) { return new ListenableFutureTask(runnable, result); }
/*    */ 
/*    */ 
/*    */   
/* 74 */   ListenableFutureTask(Callable<V> callable) { super(callable); }
/*    */ 
/*    */ 
/*    */   
/* 78 */   ListenableFutureTask(Runnable runnable, @Nullable V result) { super(runnable, result); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   public void addListener(Runnable listener, Executor exec) { this.executionList.add(listener, exec); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   protected void done() { this.executionList.execute(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ListenableFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */