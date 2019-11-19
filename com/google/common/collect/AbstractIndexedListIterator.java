/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.NoSuchElementException;
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
/*     */ abstract class AbstractIndexedListIterator<E>
/*     */   extends UnmodifiableListIterator<E>
/*     */ {
/*     */   private final int size;
/*     */   private int position;
/*     */   
/*     */   protected abstract E get(int paramInt);
/*     */   
/*  54 */   protected AbstractIndexedListIterator(int size) { this(size, 0); }
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
/*     */   protected AbstractIndexedListIterator(int size, int position) {
/*  69 */     Preconditions.checkPositionIndex(position, size);
/*  70 */     this.size = size;
/*  71 */     this.position = position;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public final boolean hasNext() { return (this.position < this.size); }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E next() {
/*  81 */     if (!hasNext()) {
/*  82 */       throw new NoSuchElementException();
/*     */     }
/*  84 */     return (E)get(this.position++);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public final int nextIndex() { return this.position; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public final boolean hasPrevious() { return (this.position > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public final E previous() {
/*  99 */     if (!hasPrevious()) {
/* 100 */       throw new NoSuchElementException();
/*     */     }
/* 102 */     return (E)get(--this.position);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public final int previousIndex() { return this.position - 1; }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractIndexedListIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */