/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Queue;
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
/*     */ @Beta
/*     */ @GwtIncompatible("java.util.ArrayDeque")
/*     */ public final class EvictingQueue<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final Queue<E> delegate;
/*     */   @VisibleForTesting
/*     */   final int maxSize;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private EvictingQueue(int maxSize) {
/*  54 */     Preconditions.checkArgument((maxSize >= 0), "maxSize (%s) must >= 0", new Object[] { Integer.valueOf(maxSize) });
/*  55 */     this.delegate = new ArrayDeque(maxSize);
/*  56 */     this.maxSize = maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static <E> EvictingQueue<E> create(int maxSize) { return new EvictingQueue(maxSize); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public int remainingCapacity() { return this.maxSize - size(); }
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected Queue<E> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public boolean offer(E e) { return add(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E e) {
/* 100 */     Preconditions.checkNotNull(e);
/* 101 */     if (this.maxSize == 0) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (size() == this.maxSize) {
/* 105 */       this.delegate.remove();
/*     */     }
/* 107 */     this.delegate.add(e);
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 112 */   public boolean addAll(Collection<? extends E> collection) { return standardAddAll(collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public boolean contains(Object object) { return delegate().contains(Preconditions.checkNotNull(object)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public boolean remove(Object object) { return delegate().remove(Preconditions.checkNotNull(object)); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EvictingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */