/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
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
/*     */ final class ListenerCallQueue<L>
/*     */   extends Object
/*     */   implements Runnable
/*     */ {
/*     */   private final L listener;
/*     */   private final Executor executor;
/*     */   @GuardedBy("this")
/*     */   private final Queue<Callback<L>> waitQueue;
/*  40 */   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
/*     */   @GuardedBy("this")
/*     */   private boolean isThreadScheduled;
/*     */   
/*     */   static abstract class Callback<L>
/*     */     extends Object {
/*  46 */     Callback(String methodCall) { this.methodCall = methodCall; }
/*     */ 
/*     */     
/*     */     private final String methodCall;
/*     */ 
/*     */     
/*     */     void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
/*  53 */       for (ListenerCallQueue<L> queue : queues) {
/*  54 */         queue.add(this);
/*     */       }
/*     */     }
/*     */     
/*     */     abstract void call(L param1L);
/*     */   }
/*     */   
/*     */   ListenerCallQueue(L listener, Executor executor) {
/*  62 */     this.waitQueue = Queues.newArrayDeque();
/*     */ 
/*     */ 
/*     */     
/*  66 */     this.listener = Preconditions.checkNotNull(listener);
/*  67 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   void add(Callback<L> callback) { this.waitQueue.add(callback); }
/*     */ 
/*     */ 
/*     */   
/*     */   void execute() {
/*  77 */     boolean scheduleTaskRunner = false;
/*  78 */     synchronized (this) {
/*  79 */       if (!this.isThreadScheduled) {
/*  80 */         this.isThreadScheduled = true;
/*  81 */         scheduleTaskRunner = true;
/*     */       } 
/*     */     } 
/*  84 */     if (scheduleTaskRunner) {
/*     */       try {
/*  86 */         this.executor.execute(this);
/*  87 */       } catch (RuntimeException e) {
/*     */         
/*  89 */         synchronized (this) {
/*  90 */           this.isThreadScheduled = false;
/*     */         } 
/*     */         
/*  93 */         logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
/*     */ 
/*     */         
/*  96 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/* 102 */     stillRunning = true;
/*     */     try {
/*     */       while (true) {
/*     */         Callback<L> nextToRun;
/* 106 */         synchronized (this) {
/* 107 */           Preconditions.checkState(this.isThreadScheduled);
/* 108 */           nextToRun = (Callback)this.waitQueue.poll();
/* 109 */           if (nextToRun == null) {
/* 110 */             this.isThreadScheduled = false;
/* 111 */             stillRunning = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/*     */         try {
/* 118 */           nextToRun.call(this.listener);
/* 119 */         } catch (RuntimeException e) {
/*     */           
/* 121 */           logger.log(Level.SEVERE, "Exception while executing callback: " + this.listener + "." + nextToRun.methodCall, e);
/*     */         }
/*     */       
/*     */       } 
/*     */     } finally {
/*     */       
/* 127 */       if (stillRunning)
/*     */       {
/*     */ 
/*     */         
/* 131 */         synchronized (this) {
/* 132 */           this.isThreadScheduled = false;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ListenerCallQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */