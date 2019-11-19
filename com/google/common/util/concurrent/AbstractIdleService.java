/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.util.concurrent.Executor;
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
/*     */ public abstract class AbstractIdleService
/*     */   implements Service
/*     */ {
/*  41 */   private final Supplier<String> threadNameSupplier = new Supplier<String>()
/*     */     {
/*  43 */       public String get() { return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state(); }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*  48 */   private final Service delegate = new AbstractService() {
/*     */       protected final void doStart() {
/*  50 */         MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new Runnable()
/*     */             {
/*     */               public void run() {
/*     */                 try {
/*  54 */                   AbstractIdleService.null.this.this$0.startUp();
/*  55 */                   AbstractIdleService.null.this.notifyStarted();
/*  56 */                 } catch (Throwable t) {
/*  57 */                   AbstractIdleService.null.this.notifyFailed(t);
/*  58 */                   throw Throwables.propagate(t);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       protected final void doStop() {
/*  65 */         MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute(new Runnable()
/*     */             {
/*     */               public void run() {
/*     */                 try {
/*  69 */                   AbstractIdleService.null.this.this$0.shutDown();
/*  70 */                   AbstractIdleService.null.this.notifyStopped();
/*  71 */                 } catch (Throwable t) {
/*  72 */                   AbstractIdleService.null.this.notifyFailed(t);
/*  73 */                   throw Throwables.propagate(t);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */     };
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
/*     */   protected Executor executor() {
/*  98 */     return new Executor()
/*     */       {
/* 100 */         public void execute(Runnable command) { MoreExecutors.newThread((String)AbstractIdleService.this.threadNameSupplier.get(), command).start(); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public String toString() { return serviceName() + " [" + state() + "]"; }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public final boolean isRunning() { return this.delegate.isRunning(); }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public final Service.State state() { return this.delegate.state(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public final void addListener(Service.Listener listener, Executor executor) { this.delegate.addListener(listener, executor); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public final Throwable failureCause() { return this.delegate.failureCause(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service startAsync() {
/* 135 */     this.delegate.startAsync();
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service stopAsync() {
/* 143 */     this.delegate.stopAsync();
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public final void awaitRunning() { this.delegate.awaitRunning(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException { this.delegate.awaitRunning(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public final void awaitTerminated() { this.delegate.awaitTerminated(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException { this.delegate.awaitTerminated(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   protected String serviceName() { return getClass().getSimpleName(); }
/*     */   
/*     */   protected abstract void startUp();
/*     */   
/*     */   protected abstract void shutDown();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AbstractIdleService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */