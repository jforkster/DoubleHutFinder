/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ForwardingExecutorService
/*     */   extends ForwardingObject
/*     */   implements ExecutorService
/*     */ {
/*  50 */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException { return delegate().awaitTermination(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException { return delegate().invokeAll(tasks); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException { return delegate().invokeAll(tasks, timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException { return (T)delegate().invokeAny(tasks); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return (T)delegate().invokeAny(tasks, timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean isShutdown() { return delegate().isShutdown(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public boolean isTerminated() { return delegate().isTerminated(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void shutdown() { delegate().shutdown(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public List<Runnable> shutdownNow() { return delegate().shutdownNow(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public void execute(Runnable command) { delegate().execute(command); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public <T> Future<T> submit(Callable<T> task) { return delegate().submit(task); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public Future<?> submit(Runnable task) { return delegate().submit(task); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public <T> Future<T> submit(Runnable task, T result) { return delegate().submit(task, result); }
/*     */   
/*     */   protected abstract ExecutorService delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ForwardingExecutorService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */