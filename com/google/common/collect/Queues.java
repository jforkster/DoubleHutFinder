/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
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
/*     */ public final class Queues
/*     */ {
/*  51 */   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) { return new ArrayBlockingQueue(capacity); }
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
/*  62 */   public static <E> ArrayDeque<E> newArrayDeque() { return new ArrayDeque(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
/*  72 */     if (elements instanceof Collection) {
/*  73 */       return new ArrayDeque(Collections2.cast(elements));
/*     */     }
/*  75 */     ArrayDeque<E> deque = new ArrayDeque<E>();
/*  76 */     Iterables.addAll(deque, elements);
/*  77 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() { return new ConcurrentLinkedQueue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
/*  95 */     if (elements instanceof Collection) {
/*  96 */       return new ConcurrentLinkedQueue(Collections2.cast(elements));
/*     */     }
/*  98 */     ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();
/*  99 */     Iterables.addAll(queue, elements);
/* 100 */     return queue;
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
/* 111 */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() { return new LinkedBlockingDeque(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) { return new LinkedBlockingDeque(capacity); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
/* 132 */     if (elements instanceof Collection) {
/* 133 */       return new LinkedBlockingDeque(Collections2.cast(elements));
/*     */     }
/* 135 */     LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<E>();
/* 136 */     Iterables.addAll(deque, elements);
/* 137 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() { return new LinkedBlockingQueue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) { return new LinkedBlockingQueue(capacity); }
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
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
/* 167 */     if (elements instanceof Collection) {
/* 168 */       return new LinkedBlockingQueue(Collections2.cast(elements));
/*     */     }
/* 170 */     LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<E>();
/* 171 */     Iterables.addAll(queue, elements);
/* 172 */     return queue;
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
/* 186 */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() { return new PriorityBlockingQueue(); }
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
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
/* 199 */     if (elements instanceof Collection) {
/* 200 */       return new PriorityBlockingQueue(Collections2.cast(elements));
/*     */     }
/* 202 */     PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<E>();
/* 203 */     Iterables.addAll(queue, elements);
/* 204 */     return queue;
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
/* 216 */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() { return new PriorityQueue(); }
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
/* 229 */     if (elements instanceof Collection) {
/* 230 */       return new PriorityQueue(Collections2.cast(elements));
/*     */     }
/* 232 */     PriorityQueue<E> queue = new PriorityQueue<E>();
/* 233 */     Iterables.addAll(queue, elements);
/* 234 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   public static <E> SynchronousQueue<E> newSynchronousQueue() { return new SynchronousQueue(); }
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
/*     */   @Beta
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 262 */     Preconditions.checkNotNull(buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 269 */     int added = 0;
/* 270 */     while (added < numElements) {
/*     */ 
/*     */       
/* 273 */       added += q.drainTo(buffer, numElements - added);
/* 274 */       if (added < numElements) {
/* 275 */         E e = (E)q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/* 276 */         if (e == null) {
/*     */           break;
/*     */         }
/* 279 */         buffer.add(e);
/* 280 */         added++;
/*     */       } 
/*     */     } 
/* 283 */     return added;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 302 */     Preconditions.checkNotNull(buffer);
/* 303 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 304 */     int added = 0;
/* 305 */     interrupted = false;
/*     */     try {
/* 307 */       while (added < numElements) {
/*     */ 
/*     */         
/* 310 */         added += q.drainTo(buffer, numElements - added);
/* 311 */         if (added < numElements) {
/*     */           E e;
/*     */           while (true) {
/*     */             try {
/* 315 */               e = (E)q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */               break;
/* 317 */             } catch (InterruptedException ex) {
/* 318 */               interrupted = true;
/*     */             } 
/*     */           } 
/* 321 */           if (e == null) {
/*     */             break;
/*     */           }
/* 324 */           buffer.add(e);
/* 325 */           added++;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 329 */       if (interrupted) {
/* 330 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 333 */     return added;
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
/*     */   @Beta
/* 365 */   public static <E> Queue<E> synchronizedQueue(Queue<E> queue) { return Synchronized.queue(queue, null); }
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
/*     */   @Beta
/* 397 */   public static <E> Deque<E> synchronizedDeque(Deque<E> deque) { return Synchronized.deque(deque, null); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Queues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */