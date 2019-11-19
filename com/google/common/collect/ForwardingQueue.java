/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingQueue<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*  55 */   public boolean offer(E o) { return delegate().offer(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public E poll() { return (E)delegate().poll(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public E remove() { return (E)delegate().remove(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public E peek() { return (E)delegate().peek(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public E element() { return (E)delegate().element(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardOffer(E e) {
/*     */     try {
/*  87 */       return add(e);
/*  88 */     } catch (IllegalStateException caught) {
/*  89 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPeek() {
/*     */     try {
/* 102 */       return (E)element();
/* 103 */     } catch (NoSuchElementException caught) {
/* 104 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected E standardPoll() {
/*     */     try {
/* 117 */       return (E)remove();
/* 118 */     } catch (NoSuchElementException caught) {
/* 119 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Queue<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */