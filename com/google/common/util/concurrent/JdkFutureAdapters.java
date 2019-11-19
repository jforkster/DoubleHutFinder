/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ @Beta
/*     */ public final class JdkFutureAdapters
/*     */ {
/*     */   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
/*  60 */     if (future instanceof ListenableFuture) {
/*  61 */       return (ListenableFuture)future;
/*     */     }
/*  63 */     return new ListenableFutureAdapter(future);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
/*  92 */     Preconditions.checkNotNull(executor);
/*  93 */     if (future instanceof ListenableFuture) {
/*  94 */       return (ListenableFuture)future;
/*     */     }
/*  96 */     return new ListenableFutureAdapter(future, executor);
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
/*     */   private static class ListenableFutureAdapter<V>
/*     */     extends ForwardingFuture<V>
/*     */     implements ListenableFuture<V>
/*     */   {
/* 112 */     private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     private static final Executor defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
/*     */ 
/*     */     
/*     */     private final Executor adapterExecutor;
/*     */ 
/*     */     
/*     */     private final ExecutionList executionList;
/*     */ 
/*     */     
/*     */     private final AtomicBoolean hasListeners;
/*     */ 
/*     */     
/*     */     private final Future<V> delegate;
/*     */ 
/*     */ 
/*     */     
/* 133 */     ListenableFutureAdapter(Future<V> delegate) { this(delegate, defaultAdapterExecutor); }
/*     */     ListenableFutureAdapter(Future<V> delegate, Executor adapterExecutor) {
/*     */       this.executionList = new ExecutionList();
/*     */       this.hasListeners = new AtomicBoolean(false);
/* 137 */       this.delegate = (Future)Preconditions.checkNotNull(delegate);
/* 138 */       this.adapterExecutor = (Executor)Preconditions.checkNotNull(adapterExecutor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 143 */     protected Future<V> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void addListener(Runnable listener, Executor exec) {
/* 148 */       this.executionList.add(listener, exec);
/*     */ 
/*     */ 
/*     */       
/* 152 */       if (this.hasListeners.compareAndSet(false, true)) {
/* 153 */         if (this.delegate.isDone()) {
/*     */ 
/*     */           
/* 156 */           this.executionList.execute();
/*     */           
/*     */           return;
/*     */         } 
/* 160 */         this.adapterExecutor.execute(new Runnable()
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void run()
/*     */               {
/*     */                 try {
/* 170 */                   Uninterruptibles.getUninterruptibly(super.this$0.delegate);
/* 171 */                 } catch (Error e) {
/* 172 */                   throw e;
/* 173 */                 } catch (Throwable e) {}
/*     */ 
/*     */ 
/*     */                 
/* 177 */                 super.this$0.executionList.execute();
/*     */               }
/*     */             });
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/JdkFutureAdapters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */