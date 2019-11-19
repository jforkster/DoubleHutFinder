/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class SingletonImmutableList<E>
/*     */   extends ImmutableList<E>
/*     */ {
/*     */   final E element;
/*     */   
/*  40 */   SingletonImmutableList(E element) { this.element = Preconditions.checkNotNull(element); }
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  45 */     Preconditions.checkElementIndex(index, 1);
/*  46 */     return (E)this.element;
/*     */   }
/*     */ 
/*     */   
/*  50 */   public int indexOf(@Nullable Object object) { return this.element.equals(object) ? 0 : -1; }
/*     */ 
/*     */ 
/*     */   
/*  54 */   public UnmodifiableIterator<E> iterator() { return Iterators.singletonIterator(this.element); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public int lastIndexOf(@Nullable Object object) { return indexOf(object); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public int size() { return 1; }
/*     */ 
/*     */   
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/*  67 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
/*  68 */     return (fromIndex == toIndex) ? ImmutableList.of() : this;
/*     */   }
/*     */ 
/*     */   
/*  72 */   public ImmutableList<E> reverse() { return this; }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public boolean contains(@Nullable Object object) { return this.element.equals(object); }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  80 */     if (object == this) {
/*  81 */       return true;
/*     */     }
/*  83 */     if (object instanceof List) {
/*  84 */       List<?> that = (List)object;
/*  85 */       return (that.size() == 1 && this.element.equals(that.get(0)));
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public int hashCode() { return 31 + this.element.hashCode(); }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  97 */     String elementToString = this.element.toString();
/*  98 */     return (elementToString.length() + 2).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public boolean isEmpty() { return false; }
/*     */ 
/*     */ 
/*     */   
/* 110 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 115 */     dst[offset] = this.element;
/* 116 */     return offset + 1;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SingletonImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */