/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.Future;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ForwardingListenableFuture<V>
/*    */   extends ForwardingFuture<V>
/*    */   implements ListenableFuture<V>
/*    */ {
/* 47 */   public void addListener(Runnable listener, Executor exec) { delegate().addListener(listener, exec); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract ListenableFuture<V> delegate();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static abstract class SimpleForwardingListenableFuture<V>
/*    */     extends ForwardingListenableFuture<V>
/*    */   {
/*    */     private final ListenableFuture<V> delegate;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 66 */     protected SimpleForwardingListenableFuture(ListenableFuture<V> delegate) { this.delegate = (ListenableFuture)Preconditions.checkNotNull(delegate); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 71 */     protected final ListenableFuture<V> delegate() { return this.delegate; }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ForwardingListenableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */