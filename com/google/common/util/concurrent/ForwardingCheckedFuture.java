/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ @Beta
/*    */ public abstract class ForwardingCheckedFuture<V, X extends Exception>
/*    */   extends ForwardingListenableFuture<V>
/*    */   implements CheckedFuture<V, X>
/*    */ {
/* 46 */   public V checkedGet() throws X { return (V)delegate().checkedGet(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, X { return (V)delegate().checkedGet(timeout, unit); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract CheckedFuture<V, X> delegate();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Beta
/*    */   public static abstract class SimpleForwardingCheckedFuture<V, X extends Exception>
/*    */     extends ForwardingCheckedFuture<V, X>
/*    */   {
/*    */     private final CheckedFuture<V, X> delegate;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 70 */     protected SimpleForwardingCheckedFuture(CheckedFuture<V, X> delegate) { this.delegate = (CheckedFuture)Preconditions.checkNotNull(delegate); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 75 */     protected final CheckedFuture<V, X> delegate() { return this.delegate; }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ForwardingCheckedFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */