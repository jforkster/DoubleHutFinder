/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.BlockingDeque;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public abstract class ForwardingBlockingDeque<E>
/*     */   extends ForwardingDeque<E>
/*     */   implements BlockingDeque<E>
/*     */ {
/*  51 */   public int remainingCapacity() { return delegate().remainingCapacity(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public void putFirst(E e) throws InterruptedException { delegate().putFirst(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public void putLast(E e) throws InterruptedException { delegate().putLast(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException { return delegate().offerFirst(e, timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException { return delegate().offerLast(e, timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public E takeFirst() throws InterruptedException { return (E)delegate().takeFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public E takeLast() throws InterruptedException { return (E)delegate().takeLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException { return (E)delegate().pollFirst(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public E pollLast(long timeout, TimeUnit unit) throws InterruptedException { return (E)delegate().pollLast(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public void put(E e) throws InterruptedException { delegate().put(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException { return delegate().offer(e, timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public E take() throws InterruptedException { return (E)delegate().take(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public E poll(long timeout, TimeUnit unit) throws InterruptedException { return (E)delegate().poll(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public int drainTo(Collection<? super E> c) { return delegate().drainTo(c); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public int drainTo(Collection<? super E> c, int maxElements) { return delegate().drainTo(c, maxElements); }
/*     */   
/*     */   protected abstract BlockingDeque<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingBlockingDeque.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */