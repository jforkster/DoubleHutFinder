/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.Future;
/*    */ import javax.annotation.Nullable;
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
/*    */ final class AsyncSettableFuture<V>
/*    */   extends ForwardingListenableFuture<V>
/*    */ {
/* 41 */   public static <V> AsyncSettableFuture<V> create() { return new AsyncSettableFuture(); }
/*    */ 
/*    */   
/* 44 */   private final NestedFuture<V> nested = new NestedFuture(null);
/* 45 */   private final ListenableFuture<V> dereferenced = Futures.dereference(this.nested);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected ListenableFuture<V> delegate() { return this.dereferenced; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public boolean setFuture(ListenableFuture<? extends V> future) { return this.nested.setFuture((ListenableFuture)Preconditions.checkNotNull(future)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public boolean setValue(@Nullable V value) { return setFuture(Futures.immediateFuture(value)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   public boolean setException(Throwable exception) { return setFuture(Futures.immediateFailedFuture(exception)); }
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
/* 88 */   public boolean isSet() { return this.nested.isDone(); }
/*    */   
/*    */   private static final class NestedFuture<V>
/*    */     extends AbstractFuture<ListenableFuture<? extends V>> {
/*    */     boolean setFuture(ListenableFuture<? extends V> value) {
/* 93 */       boolean result = set(value);
/* 94 */       if (isCancelled()) {
/* 95 */         value.cancel(wasInterrupted());
/*    */       }
/* 97 */       return result;
/*    */     }
/*    */     
/*    */     private NestedFuture() {}
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AsyncSettableFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */