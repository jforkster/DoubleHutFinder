/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractScheduledService
/*     */   implements Service
/*     */ {
/*  95 */   private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Scheduler
/*     */   {
/*     */     public static Scheduler newFixedDelaySchedule(final long initialDelay, final long delay, final TimeUnit unit) {
/* 121 */       return new Scheduler()
/*     */         {
/*     */           public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
/*     */           {
/* 125 */             return executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
/*     */           }
/*     */         };
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
/*     */     public static Scheduler newFixedRateSchedule(final long initialDelay, final long period, final TimeUnit unit) {
/* 140 */       return new Scheduler()
/*     */         {
/*     */           public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
/*     */           {
/* 144 */             return executor.scheduleAtFixedRate(task, initialDelay, period, unit);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Scheduler() {}
/*     */ 
/*     */     
/*     */     abstract Future<?> schedule(AbstractService param1AbstractService, ScheduledExecutorService param1ScheduledExecutorService, Runnable param1Runnable);
/*     */   }
/*     */   
/* 157 */   private final AbstractService delegate = new AbstractService()
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 166 */       private final ReentrantLock lock = new ReentrantLock();
/*     */       
/* 168 */       private final Runnable task = new Runnable() {
/*     */           public void run() {
/* 170 */             AbstractScheduledService.null.this.lock.lock();
/*     */             try {
/* 172 */               AbstractScheduledService.null.this.this$0.runOneIteration();
/* 173 */             } catch (Throwable t) {
/*     */               try {
/* 175 */                 AbstractScheduledService.null.this.this$0.shutDown();
/* 176 */               } catch (Exception ignored) {
/* 177 */                 logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
/*     */               } 
/*     */               
/* 180 */               AbstractScheduledService.null.this.notifyFailed(t);
/* 181 */               throw Throwables.propagate(t);
/*     */             } finally {
/* 183 */               AbstractScheduledService.null.this.lock.unlock();
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/*     */       protected final void doStart() {
/* 189 */         this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier<String>() {
/*     */               public String get() {
/* 191 */                 return AbstractScheduledService.null.this.this$0.serviceName() + " " + AbstractScheduledService.null.this.state();
/*     */               }
/*     */             });
/* 194 */         this.executorService.execute(new Runnable() {
/*     */               public void run() {
/* 196 */                 AbstractScheduledService.null.this.lock.lock();
/*     */                 try {
/* 198 */                   AbstractScheduledService.null.this.this$0.startUp();
/* 199 */                   AbstractScheduledService.null.this.runningTask = AbstractScheduledService.null.this.this$0.scheduler().schedule(AbstractScheduledService.null.this.this$0.delegate, AbstractScheduledService.null.this.executorService, AbstractScheduledService.null.this.task);
/* 200 */                   AbstractScheduledService.null.this.notifyStarted();
/* 201 */                 } catch (Throwable t) {
/* 202 */                   AbstractScheduledService.null.this.notifyFailed(t);
/* 203 */                   throw Throwables.propagate(t);
/*     */                 } finally {
/* 205 */                   AbstractScheduledService.null.this.lock.unlock();
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       protected final void doStop() {
/* 212 */         this.runningTask.cancel(false);
/* 213 */         this.executorService.execute(new Runnable() {
/*     */               public void run() {
/*     */                 try {
/* 216 */                   AbstractScheduledService.null.this.lock.lock();
/*     */                   try {
/* 218 */                     if (AbstractScheduledService.null.this.state() != Service.State.STOPPING) {
/*     */                       return;
/*     */                     }
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 225 */                     AbstractScheduledService.null.this.this$0.shutDown();
/*     */                   } finally {
/* 227 */                     AbstractScheduledService.null.this.lock.unlock();
/*     */                   } 
/* 229 */                   AbstractScheduledService.null.this.notifyStopped();
/* 230 */                 } catch (Throwable t) {
/* 231 */                   AbstractScheduledService.null.this.notifyFailed(t);
/* 232 */                   throw Throwables.propagate(t);
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
/*     */   
/*     */   protected ScheduledExecutorService executor() {
/* 285 */     final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
/*     */         {
/*     */           public Thread newThread(Runnable runnable) {
/* 288 */             return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), runnable);
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 296 */     addListener(new Service.Listener()
/*     */         {
/* 298 */           public void terminated(Service.State from) { executor.shutdown(); }
/*     */ 
/*     */           
/* 301 */           public void failed(Service.State from, Throwable failure) { executor.shutdown(); }
/*     */         },  MoreExecutors.sameThreadExecutor());
/* 303 */     return executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   protected String serviceName() { return getClass().getSimpleName(); }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public String toString() { return serviceName() + " [" + state() + "]"; }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public final boolean isRunning() { return this.delegate.isRunning(); }
/*     */ 
/*     */ 
/*     */   
/* 325 */   public final Service.State state() { return this.delegate.state(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 332 */   public final void addListener(Service.Listener listener, Executor executor) { this.delegate.addListener(listener, executor); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 339 */   public final Throwable failureCause() { return this.delegate.failureCause(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service startAsync() {
/* 346 */     this.delegate.startAsync();
/* 347 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Service stopAsync() {
/* 354 */     this.delegate.stopAsync();
/* 355 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 362 */   public final void awaitRunning() { this.delegate.awaitRunning(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 369 */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException { this.delegate.awaitRunning(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   public final void awaitTerminated() { this.delegate.awaitTerminated(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 383 */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException { this.delegate.awaitTerminated(timeout, unit); }
/*     */ 
/*     */   
/*     */   protected abstract void runOneIteration();
/*     */ 
/*     */   
/*     */   protected abstract Scheduler scheduler();
/*     */   
/*     */   @Beta
/*     */   public static abstract class CustomScheduler
/*     */     extends Scheduler
/*     */   {
/* 395 */     public CustomScheduler() { super(null); }
/*     */ 
/*     */ 
/*     */     
/*     */     private class ReschedulableCallable
/*     */       extends ForwardingFuture<Void>
/*     */       implements Callable<Void>
/*     */     {
/*     */       private final Runnable wrappedRunnable;
/*     */ 
/*     */       
/*     */       private final ScheduledExecutorService executor;
/*     */ 
/*     */       
/*     */       private final AbstractService service;
/*     */ 
/*     */       
/*     */       private final ReentrantLock lock;
/*     */       
/*     */       @GuardedBy("lock")
/*     */       private Future<Void> currentFuture;
/*     */ 
/*     */       
/*     */       ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
/* 419 */         this.lock = new ReentrantLock();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 427 */         this.wrappedRunnable = runnable;
/* 428 */         this.executor = executor;
/* 429 */         this.service = service;
/*     */       }
/*     */ 
/*     */       
/*     */       public Void call() throws Exception {
/* 434 */         this.wrappedRunnable.run();
/* 435 */         reschedule();
/* 436 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void reschedule() {
/* 447 */         this.lock.lock();
/*     */         try {
/* 449 */           if (this.currentFuture == null || !this.currentFuture.isCancelled()) {
/* 450 */             AbstractScheduledService.CustomScheduler.Schedule schedule = AbstractScheduledService.CustomScheduler.this.getNextSchedule();
/* 451 */             this.currentFuture = this.executor.schedule(this, schedule.delay, schedule.unit);
/*     */           } 
/* 453 */         } catch (Throwable e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 459 */           this.service.notifyFailed(e);
/*     */         } finally {
/* 461 */           this.lock.unlock();
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean cancel(boolean mayInterruptIfRunning) {
/* 470 */         this.lock.lock();
/*     */         try {
/* 472 */           return this.currentFuture.cancel(mayInterruptIfRunning);
/*     */         } finally {
/* 474 */           this.lock.unlock();
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 480 */       protected Future<Void> delegate() { throw new UnsupportedOperationException("Only cancel is supported by this future"); }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable) {
/* 487 */       ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
/* 488 */       task.reschedule();
/* 489 */       return task;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract Schedule getNextSchedule() throws Exception;
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     protected static final class Schedule
/*     */     {
/*     */       private final long delay;
/*     */ 
/*     */       
/*     */       private final TimeUnit unit;
/*     */ 
/*     */ 
/*     */       
/*     */       public Schedule(long delay, TimeUnit unit) throws TimeoutException {
/* 509 */         this.delay = delay;
/* 510 */         this.unit = (TimeUnit)Preconditions.checkNotNull(unit);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AbstractScheduledService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */