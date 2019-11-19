/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @Beta
/*     */ public abstract class AbstractCheckedFuture<V, X extends Exception>
/*     */   extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */   implements CheckedFuture<V, X>
/*     */ {
/*  41 */   protected AbstractCheckedFuture(ListenableFuture<V> delegate) { super(delegate); }
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
/*     */   protected abstract X mapException(Exception paramException);
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
/*     */   public V checkedGet() throws X {
/*     */     try {
/*  78 */       return (V)get();
/*  79 */     } catch (InterruptedException e) {
/*  80 */       Thread.currentThread().interrupt();
/*  81 */       throw mapException(e);
/*  82 */     } catch (CancellationException e) {
/*  83 */       throw mapException(e);
/*  84 */     } catch (ExecutionException e) {
/*  85 */       throw mapException(e);
/*     */     } 
/*     */   }
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
/*     */   public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, X {
/*     */     try {
/* 107 */       return (V)get(timeout, unit);
/* 108 */     } catch (InterruptedException e) {
/* 109 */       Thread.currentThread().interrupt();
/* 110 */       throw mapException(e);
/* 111 */     } catch (CancellationException e) {
/* 112 */       throw mapException(e);
/* 113 */     } catch (ExecutionException e) {
/* 114 */       throw mapException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AbstractCheckedFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */