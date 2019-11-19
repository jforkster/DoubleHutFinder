/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ListIterator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableList<E>
/*     */   extends ImmutableList<E>
/*     */ {
/*     */   private final int offset;
/*     */   private final int size;
/*     */   private final Object[] array;
/*     */   
/*     */   RegularImmutableList(Object[] array, int offset, int size) {
/*  37 */     this.offset = offset;
/*  38 */     this.size = size;
/*  39 */     this.array = array;
/*     */   }
/*     */ 
/*     */   
/*  43 */   RegularImmutableList(Object[] array) { this(array, 0, array.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public int size() { return this.size; }
/*     */ 
/*     */ 
/*     */   
/*  52 */   boolean isPartialView() { return (this.size != this.array.length); }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int dstOff) {
/*  57 */     System.arraycopy(this.array, this.offset, dst, dstOff, this.size);
/*  58 */     return dstOff + this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/*  65 */     Preconditions.checkElementIndex(index, this.size);
/*  66 */     return (E)this.array[index + this.offset];
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@Nullable Object object) {
/*  71 */     if (object == null) {
/*  72 */       return -1;
/*     */     }
/*  74 */     for (int i = 0; i < this.size; i++) {
/*  75 */       if (this.array[this.offset + i].equals(object)) {
/*  76 */         return i;
/*     */       }
/*     */     } 
/*  79 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@Nullable Object object) {
/*  84 */     if (object == null) {
/*  85 */       return -1;
/*     */     }
/*  87 */     for (int i = this.size - 1; i >= 0; i--) {
/*  88 */       if (this.array[this.offset + i].equals(object)) {
/*  89 */         return i;
/*     */       }
/*     */     } 
/*  92 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  97 */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) { return new RegularImmutableList(this.array, this.offset + fromIndex, toIndex - fromIndex); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public UnmodifiableListIterator<E> listIterator(int index) { return Iterators.forArray(this.array, this.offset, this.size, index); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */