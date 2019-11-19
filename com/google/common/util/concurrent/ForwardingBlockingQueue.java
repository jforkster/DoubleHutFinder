/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.collect.ForwardingQueue;
/*    */ import java.util.Collection;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public abstract class ForwardingBlockingQueue<E>
/*    */   extends ForwardingQueue<E>
/*    */   implements BlockingQueue<E>
/*    */ {
/* 46 */   public int drainTo(Collection<? super E> c, int maxElements) { return delegate().drainTo(c, maxElements); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public int drainTo(Collection<? super E> c) { return delegate().drainTo(c); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException { return delegate().offer(e, timeout, unit); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public E poll(long timeout, TimeUnit unit) throws InterruptedException { return (E)delegate().poll(timeout, unit); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public void put(E e) throws InterruptedException { delegate().put(e); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public int remainingCapacity() { return delegate().remainingCapacity(); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public E take() throws InterruptedException { return (E)delegate().take(); }
/*    */   
/*    */   protected abstract BlockingQueue<E> delegate();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ForwardingBlockingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */