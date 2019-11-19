/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public abstract class AbstractExecutionThreadService
/*     */   implements Service
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  44 */   private final Service delegate = new AbstractService() {
/*     */       protected final void doStart() {
/*  46 */         Executor executor = MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this.executor(), new Supplier<String>() {
/*     */               public String get() {
/*  48 */                 return AbstractExecutionThreadService.null.this.this$0.serviceName();
/*     */               }
/*     */             });
/*  51 */         executor.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/*     */                 try {
/*  55 */                   AbstractExecutionThreadService.null.this.this$0.startUp();
/*  56 */                   AbstractExecutionThreadService.null.this.notifyStarted();
/*     */                   
/*  58 */                   if (AbstractExecutionThreadService.null.this.isRunning()) {
/*     */                     try {
/*  60 */                       AbstractExecutionThreadService.null.this.this$0.run();
/*  61 */                     } catch (Throwable t) {
/*     */                       try {
/*  63 */                         AbstractExecutionThreadService.null.this.this$0.shutDown();
/*  64 */                       } catch (Exception ignored) {
/*  65 */                         logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
/*     */                       } 
/*     */ 
/*     */                       
/*  69 */                       throw t;
/*     */                     } 
/*     */                   }
/*     */                   
/*  73 */                   AbstractExecutionThreadService.null.this.this$0.shutDown();
/*  74 */                   AbstractExecutionThreadService.null.this.notifyStopped();
/*  75 */                 } catch (Throwable t) {
/*  76 */                   AbstractExecutionThreadService.null.this.notifyFailed(t);
/*  77 */                   throw Throwables.propagate(t);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*  84 */       protected void doStop() { AbstractExecutionThreadService.this.triggerShutdown(); }
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
/*     */   protected void startUp() {}
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
/*     */   protected void shutDown() {}
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
/*     */   protected void triggerShutdown() {}
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
/* 143 */     return new Executor()
/*     */       {
/*     */         public void execute(Runnable command) {
/* 146 */           MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), command).start();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 152 */   public String toString() { return serviceName() + " [" + state() + "]"; }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public final boolean isRunning() { return this.delegate.isRunning(); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public final Service.State state() { return this.delegate.state(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public final void addListener(Service.Listener listener, Executor executor) { this.delegate.addListener(listener, executor); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public final Throwable failureCause() { return this.delegate.failureCause(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service startAsync() {
/* 181 */     this.delegate.startAsync();
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service stopAsync() {
/* 189 */     this.delegate.stopAsync();
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public final void awaitRunning() { this.delegate.awaitRunning(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException { this.delegate.awaitRunning(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public final void awaitTerminated() { this.delegate.awaitTerminated(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException { this.delegate.awaitTerminated(timeout, unit); }
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
/* 230 */   protected String serviceName() { return getClass().getSimpleName(); }
/*     */   
/*     */   protected abstract void run();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AbstractExecutionThreadService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */