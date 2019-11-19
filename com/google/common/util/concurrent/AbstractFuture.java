/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/*     */ import javax.annotation.Nullable;
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
/*     */ public abstract class AbstractFuture<V>
/*     */   extends Object
/*     */   implements ListenableFuture<V>
/*     */ {
/*  68 */   private final Sync<V> sync = new Sync();
/*     */ 
/*     */   
/*  71 */   private final ExecutionList executionList = new ExecutionList();
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
/*  96 */   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException { return (V)this.sync.get(unit.toNanos(timeout)); }
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
/* 116 */   public V get() throws InterruptedException, ExecutionException { return (V)this.sync.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public boolean isDone() { return this.sync.isDone(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public boolean isCancelled() { return this.sync.isCancelled(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 131 */     if (!this.sync.cancel(mayInterruptIfRunning)) {
/* 132 */       return false;
/*     */     }
/* 134 */     this.executionList.execute();
/* 135 */     if (mayInterruptIfRunning) {
/* 136 */       interruptTask();
/*     */     }
/* 138 */     return true;
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
/*     */   protected void interruptTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   protected final boolean wasInterrupted() { return this.sync.wasInterrupted(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public void addListener(Runnable listener, Executor exec) { this.executionList.add(listener, exec); }
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
/*     */   protected boolean set(@Nullable V value) {
/* 183 */     boolean result = this.sync.set(value);
/* 184 */     if (result) {
/* 185 */       this.executionList.execute();
/*     */     }
/* 187 */     return result;
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
/*     */   protected boolean setException(Throwable throwable) {
/* 200 */     boolean result = this.sync.setException((Throwable)Preconditions.checkNotNull(throwable));
/* 201 */     if (result) {
/* 202 */       this.executionList.execute();
/*     */     }
/* 204 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Sync<V>
/*     */     extends AbstractQueuedSynchronizer
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int RUNNING = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int COMPLETING = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int COMPLETED = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     static final int CANCELLED = 4;
/*     */ 
/*     */     
/*     */     static final int INTERRUPTED = 8;
/*     */ 
/*     */     
/*     */     private V value;
/*     */ 
/*     */     
/*     */     private Throwable exception;
/*     */ 
/*     */ 
/*     */     
/*     */     protected int tryAcquireShared(int ignored) {
/* 243 */       if (isDone()) {
/* 244 */         return 1;
/*     */       }
/* 246 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean tryReleaseShared(int finalState) {
/* 255 */       setState(finalState);
/* 256 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     V get(long nanos) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
/* 268 */       if (!tryAcquireSharedNanos(-1, nanos)) {
/* 269 */         throw new TimeoutException("Timeout waiting for task.");
/*     */       }
/*     */       
/* 272 */       return (V)getValue();
/*     */     }
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
/*     */     V get() throws InterruptedException, ExecutionException {
/* 285 */       acquireSharedInterruptibly(-1);
/* 286 */       return (V)getValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private V getValue() throws InterruptedException, ExecutionException {
/* 295 */       int state = getState();
/* 296 */       switch (state) {
/*     */         case 2:
/* 298 */           if (this.exception != null) {
/* 299 */             throw new ExecutionException(this.exception);
/*     */           }
/* 301 */           return (V)this.value;
/*     */ 
/*     */         
/*     */         case 4:
/*     */         case 8:
/* 306 */           throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.exception);
/*     */       } 
/*     */ 
/*     */       
/* 310 */       throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 320 */     boolean isDone() { return ((getState() & 0xE) != 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     boolean isCancelled() { return ((getState() & 0xC) != 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     boolean wasInterrupted() { return (getState() == 8); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     boolean set(@Nullable V v) { return complete(v, null, 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 348 */     boolean setException(Throwable t) { return complete(null, t, 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 355 */     boolean cancel(boolean interrupt) { return complete(null, null, interrupt ? 8 : 4); }
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
/*     */     private boolean complete(@Nullable V v, @Nullable Throwable t, int finalState) {
/* 372 */       boolean doCompletion = compareAndSetState(0, 1);
/* 373 */       if (doCompletion) {
/*     */ 
/*     */         
/* 376 */         this.value = v;
/*     */         
/* 378 */         this.exception = ((finalState & 0xC) != 0) ? new CancellationException("Future.cancel() was called.") : t;
/*     */         
/* 380 */         releaseShared(finalState);
/* 381 */       } else if (getState() == 1) {
/*     */ 
/*     */         
/* 384 */         acquireShared(-1);
/*     */       } 
/* 386 */       return doCompletion;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause) {
/* 392 */     CancellationException exception = new CancellationException(message);
/* 393 */     exception.initCause(cause);
/* 394 */     return exception;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AbstractFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */