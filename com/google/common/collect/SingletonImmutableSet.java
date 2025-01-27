/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class SingletonImmutableSet<E>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   final E element;
/*     */   private int cachedHashCode;
/*     */   
/*  47 */   SingletonImmutableSet(E element) { this.element = Preconditions.checkNotNull(element); }
/*     */ 
/*     */ 
/*     */   
/*     */   SingletonImmutableSet(E element, int hashCode) {
/*  52 */     this.element = element;
/*  53 */     this.cachedHashCode = hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public int size() { return 1; }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public boolean isEmpty() { return false; }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public boolean contains(Object target) { return this.element.equals(target); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public UnmodifiableIterator<E> iterator() { return Iterators.singletonIterator(this.element); }
/*     */ 
/*     */ 
/*     */   
/*  74 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/*  79 */     dst[offset] = this.element;
/*  80 */     return offset + 1;
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  84 */     if (object == this) {
/*  85 */       return true;
/*     */     }
/*  87 */     if (object instanceof Set) {
/*  88 */       Set<?> that = (Set)object;
/*  89 */       return (that.size() == 1 && this.element.equals(that.iterator().next()));
/*     */     } 
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  96 */     int code = this.cachedHashCode;
/*  97 */     if (code == 0) {
/*  98 */       this.cachedHashCode = code = this.element.hashCode();
/*     */     }
/* 100 */     return code;
/*     */   }
/*     */ 
/*     */   
/* 104 */   boolean isHashCodeFast() { return (this.cachedHashCode != 0); }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     String elementToString = this.element.toString();
/* 109 */     return (elementToString.length() + 2).toString();
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SingletonImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */