/*     */ package com.google.common.collect;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class ForwardingDeque<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Deque<E>
/*     */ {
/*  47 */   public void addFirst(E e) { delegate().addFirst(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   public void addLast(E e) { delegate().addLast(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public Iterator<E> descendingIterator() { return delegate().descendingIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public E getFirst() { return (E)delegate().getFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public E getLast() { return (E)delegate().getLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public boolean offerFirst(E e) { return delegate().offerFirst(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean offerLast(E e) { return delegate().offerLast(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public E peekFirst() { return (E)delegate().peekFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public E peekLast() { return (E)delegate().peekLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public E pollFirst() { return (E)delegate().pollFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public E pollLast() { return (E)delegate().pollLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public E pop() { return (E)delegate().pop(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public void push(E e) { delegate().push(e); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public E removeFirst() { return (E)delegate().removeFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public E removeLast() { return (E)delegate().removeLast(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public boolean removeFirstOccurrence(Object o) { return delegate().removeFirstOccurrence(o); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public boolean removeLastOccurrence(Object o) { return delegate().removeLastOccurrence(o); }
/*     */   
/*     */   protected abstract Deque<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingDeque.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */