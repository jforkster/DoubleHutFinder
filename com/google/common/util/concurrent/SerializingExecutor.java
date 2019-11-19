/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Executor;
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
/*     */ final class SerializingExecutor
/*     */   implements Executor
/*     */ {
/*  47 */   private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
/*     */   
/*     */   private final Executor executor;
/*     */   @GuardedBy("internalLock")
/*     */   private final Queue<Runnable> waitQueue;
/*     */   
/*     */   public SerializingExecutor(Executor executor) {
/*  54 */     this.waitQueue = new ArrayDeque();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     this.isThreadScheduled = false;
/*     */ 
/*     */ 
/*     */     
/*  69 */     this.taskRunner = new TaskRunner(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.internalLock = new Object()
/*     */       {
/*  83 */         public String toString() { return "SerializingExecutor lock: " + super.toString(); }
/*     */       };
/*     */     Preconditions.checkNotNull(executor, "'executor' must not be null.");
/*     */     this.executor = executor;
/*     */   }
/*     */   
/*     */   @GuardedBy("internalLock")
/*     */   private boolean isThreadScheduled;
/*     */   
/*     */   public void execute(Runnable r) {
/*  93 */     Preconditions.checkNotNull(r, "'r' must not be null.");
/*  94 */     boolean scheduleTaskRunner = false;
/*  95 */     synchronized (this.internalLock) {
/*  96 */       this.waitQueue.add(r);
/*     */       
/*  98 */       if (!this.isThreadScheduled) {
/*  99 */         this.isThreadScheduled = true;
/* 100 */         scheduleTaskRunner = true;
/*     */       } 
/*     */     } 
/* 103 */     if (scheduleTaskRunner) {
/* 104 */       threw = true;
/*     */       try {
/* 106 */         this.executor.execute(this.taskRunner);
/* 107 */         threw = false;
/*     */       } finally {
/* 109 */         if (threw) {
/* 110 */           synchronized (this.internalLock) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 115 */             this.isThreadScheduled = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private final TaskRunner taskRunner;
/*     */   private final Object internalLock;
/*     */   
/*     */   private class TaskRunner
/*     */     implements Runnable
/*     */   {
/*     */     private TaskRunner() {}
/*     */     
/*     */     public void run() {
/* 132 */       stillRunning = true; try {
/*     */         while (true) {
/*     */           Runnable nextToRun;
/* 135 */           Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
/*     */           
/* 137 */           synchronized (SerializingExecutor.this.internalLock) {
/* 138 */             nextToRun = (Runnable)SerializingExecutor.this.waitQueue.poll();
/* 139 */             if (nextToRun == null) {
/* 140 */               SerializingExecutor.this.isThreadScheduled = false;
/* 141 */               stillRunning = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */           
/*     */           try {
/* 148 */             nextToRun.run();
/* 149 */           } catch (RuntimeException e) {
/*     */             
/* 151 */             log.log(Level.SEVERE, "Exception while executing runnable " + nextToRun, e);
/*     */           } 
/*     */         } 
/*     */       } finally {
/*     */         
/* 156 */         if (stillRunning)
/*     */         {
/*     */ 
/*     */           
/* 160 */           synchronized (SerializingExecutor.this.internalLock) {
/* 161 */             SerializingExecutor.this.isThreadScheduled = false;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/SerializingExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */