/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class WrappingExecutorService
/*     */   implements ExecutorService
/*     */ {
/*     */   private final ExecutorService delegate;
/*     */   
/*  50 */   protected WrappingExecutorService(ExecutorService delegate) { this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract <T> Callable<T> wrapTask(Callable<T> paramCallable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Runnable wrapTask(Runnable command) {
/*  65 */     final Callable<Object> wrapped = wrapTask(Executors.callable(command, null));
/*     */     
/*  67 */     return new Runnable() {
/*     */         public void run() {
/*     */           try {
/*  70 */             wrapped.call();
/*  71 */           } catch (Exception e) {
/*  72 */             Throwables.propagate(e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final <T> ImmutableList<Callable<T>> wrapTasks(Collection<? extends Callable<T>> tasks) {
/*  85 */     ImmutableList.Builder<Callable<T>> builder = ImmutableList.builder();
/*  86 */     for (Callable<T> task : tasks) {
/*  87 */       builder.add(wrapTask(task));
/*     */     }
/*  89 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public final void execute(Runnable command) { this.delegate.execute(wrapTask(command)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public final <T> Future<T> submit(Callable<T> task) { return this.delegate.submit(wrapTask((Callable)Preconditions.checkNotNull(task))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public final Future<?> submit(Runnable task) { return this.delegate.submit(wrapTask(task)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public final <T> Future<T> submit(Runnable task, T result) { return this.delegate.submit(wrapTask(task), result); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException { return this.delegate.invokeAll(wrapTasks(tasks)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException { return this.delegate.invokeAll(wrapTasks(tasks), timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException { return (T)this.delegate.invokeAny(wrapTasks(tasks)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return (T)this.delegate.invokeAny(wrapTasks(tasks), timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public final void shutdown() { this.delegate.shutdown(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public final List<Runnable> shutdownNow() { return this.delegate.shutdownNow(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public final boolean isShutdown() { return this.delegate.isShutdown(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public final boolean isTerminated() { return this.delegate.isTerminated(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException { return this.delegate.awaitTermination(timeout, unit); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/WrappingExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */