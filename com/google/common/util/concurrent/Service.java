/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ @Beta
/*     */ public interface Service {
/*     */   Service startAsync();
/*     */   
/*     */   boolean isRunning();
/*     */   
/*     */   State state();
/*     */   
/*     */   Service stopAsync();
/*     */   
/*     */   void awaitRunning();
/*     */   
/*     */   void awaitRunning(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException;
/*     */   
/*     */   void awaitTerminated();
/*     */   
/*     */   void awaitTerminated(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException;
/*     */   
/*     */   Throwable failureCause();
/*     */   
/*     */   void addListener(Listener paramListener, Executor paramExecutor);
/*     */   
/*     */   @Beta
/*     */   public final abstract enum State {
/*     */     NEW, STARTING, RUNNING, STOPPING, TERMINATED, FAILED;
/*     */     
/*     */     abstract boolean isTerminal();
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new com/google/common/util/concurrent/Service$State$1
/*     */       //   3: dup
/*     */       //   4: ldc 'NEW'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic com/google/common/util/concurrent/Service$State.NEW : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   13: new com/google/common/util/concurrent/Service$State$2
/*     */       //   16: dup
/*     */       //   17: ldc 'STARTING'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic com/google/common/util/concurrent/Service$State.STARTING : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   26: new com/google/common/util/concurrent/Service$State$3
/*     */       //   29: dup
/*     */       //   30: ldc 'RUNNING'
/*     */       //   32: iconst_2
/*     */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   36: putstatic com/google/common/util/concurrent/Service$State.RUNNING : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   39: new com/google/common/util/concurrent/Service$State$4
/*     */       //   42: dup
/*     */       //   43: ldc 'STOPPING'
/*     */       //   45: iconst_3
/*     */       //   46: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   49: putstatic com/google/common/util/concurrent/Service$State.STOPPING : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   52: new com/google/common/util/concurrent/Service$State$5
/*     */       //   55: dup
/*     */       //   56: ldc 'TERMINATED'
/*     */       //   58: iconst_4
/*     */       //   59: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   62: putstatic com/google/common/util/concurrent/Service$State.TERMINATED : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   65: new com/google/common/util/concurrent/Service$State$6
/*     */       //   68: dup
/*     */       //   69: ldc 'FAILED'
/*     */       //   71: iconst_5
/*     */       //   72: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   75: putstatic com/google/common/util/concurrent/Service$State.FAILED : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   78: bipush #6
/*     */       //   80: anewarray com/google/common/util/concurrent/Service$State
/*     */       //   83: dup
/*     */       //   84: iconst_0
/*     */       //   85: getstatic com/google/common/util/concurrent/Service$State.NEW : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   88: aastore
/*     */       //   89: dup
/*     */       //   90: iconst_1
/*     */       //   91: getstatic com/google/common/util/concurrent/Service$State.STARTING : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   94: aastore
/*     */       //   95: dup
/*     */       //   96: iconst_2
/*     */       //   97: getstatic com/google/common/util/concurrent/Service$State.RUNNING : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   100: aastore
/*     */       //   101: dup
/*     */       //   102: iconst_3
/*     */       //   103: getstatic com/google/common/util/concurrent/Service$State.STOPPING : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   106: aastore
/*     */       //   107: dup
/*     */       //   108: iconst_4
/*     */       //   109: getstatic com/google/common/util/concurrent/Service$State.TERMINATED : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   112: aastore
/*     */       //   113: dup
/*     */       //   114: iconst_5
/*     */       //   115: getstatic com/google/common/util/concurrent/Service$State.FAILED : Lcom/google/common/util/concurrent/Service$State;
/*     */       //   118: aastore
/*     */       //   119: putstatic com/google/common/util/concurrent/Service$State.$VALUES : [Lcom/google/common/util/concurrent/Service$State;
/*     */       //   122: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #189	-> 0
/*     */       //   #198	-> 13
/*     */       //   #207	-> 26
/*     */       //   #216	-> 39
/*     */       //   #226	-> 52
/*     */       //   #236	-> 65
/*     */       //   #183	-> 78
/*     */     }
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener {
/*     */     public void starting() {}
/*     */     
/*     */     public void running() {}
/*     */     
/*     */     public void stopping(Service.State from) {}
/*     */     
/*     */     public void terminated(Service.State from) {}
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {}
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/Service.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */