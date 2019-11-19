/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ForwardingObject;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ForwardingFuture<V>
/*    */   extends ForwardingObject
/*    */   implements Future<V>
/*    */ {
/* 48 */   public boolean cancel(boolean mayInterruptIfRunning) { return delegate().cancel(mayInterruptIfRunning); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean isCancelled() { return delegate().isCancelled(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public boolean isDone() { return delegate().isDone(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public V get() throws InterruptedException, ExecutionException { return (V)delegate().get(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return (V)delegate().get(timeout, unit); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Future<V> delegate();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static abstract class SimpleForwardingFuture<V>
/*    */     extends ForwardingFuture<V>
/*    */   {
/*    */     private final Future<V> delegate;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 87 */     protected SimpleForwardingFuture(Future<V> delegate) { this.delegate = (Future)Preconditions.checkNotNull(delegate); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 92 */     protected final Future<V> delegate() { return this.delegate; }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ForwardingFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */