/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class Callables
/*     */ {
/*     */   public static <T> Callable<T> returning(@Nullable final T value) {
/*  41 */     return new Callable<T>()
/*     */       {
/*  43 */         public T call() throws Exception { return (T)value; }
/*     */       };
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
/*     */   static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
/*  59 */     Preconditions.checkNotNull(nameSupplier);
/*  60 */     Preconditions.checkNotNull(callable);
/*  61 */     return new Callable<T>() {
/*     */         public T call() throws Exception {
/*  63 */           currentThread = Thread.currentThread();
/*  64 */           oldName = currentThread.getName();
/*  65 */           restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
/*     */           
/*  67 */           try { object = callable.call();
/*     */ 
/*     */             
/*  70 */             return (T)object; } finally { if (restoreName) Callables.trySetName(oldName, currentThread);
/*     */              }
/*     */         
/*     */         }
/*     */       };
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
/*     */   static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
/*  87 */     Preconditions.checkNotNull(nameSupplier);
/*  88 */     Preconditions.checkNotNull(task);
/*  89 */     return new Runnable() {
/*     */         public void run() {
/*  91 */           currentThread = Thread.currentThread();
/*  92 */           oldName = currentThread.getName();
/*  93 */           restoreName = Callables.trySetName((String)nameSupplier.get(), currentThread);
/*     */           try {
/*  95 */             task.run();
/*     */           } finally {
/*  97 */             if (restoreName) {
/*  98 */               Callables.trySetName(oldName, currentThread);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean trySetName(String threadName, Thread currentThread) {
/*     */     try {
/* 111 */       currentThread.setName(threadName);
/* 112 */       return true;
/* 113 */     } catch (SecurityException e) {
/* 114 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/Callables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */