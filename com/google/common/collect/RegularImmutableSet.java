/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Iterator;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class RegularImmutableSet<E>
/*    */   extends ImmutableSet<E>
/*    */ {
/*    */   private final Object[] elements;
/*    */   @VisibleForTesting
/*    */   final Object[] table;
/*    */   private final int mask;
/*    */   private final int hashCode;
/*    */   
/*    */   RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
/* 39 */     this.elements = elements;
/* 40 */     this.table = table;
/* 41 */     this.mask = mask;
/* 42 */     this.hashCode = hashCode;
/*    */   }
/*    */   
/*    */   public boolean contains(Object target) {
/* 46 */     if (target == null) {
/* 47 */       return false;
/*    */     }
/* 49 */     for (int i = Hashing.smear(target.hashCode());; i++) {
/* 50 */       Object candidate = this.table[i & this.mask];
/* 51 */       if (candidate == null) {
/* 52 */         return false;
/*    */       }
/* 54 */       if (candidate.equals(target)) {
/* 55 */         return true;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public int size() { return this.elements.length; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 68 */   public UnmodifiableIterator<E> iterator() { return Iterators.forArray(this.elements); }
/*    */ 
/*    */ 
/*    */   
/*    */   int copyIntoArray(Object[] dst, int offset) {
/* 73 */     System.arraycopy(this.elements, 0, dst, offset, this.elements.length);
/* 74 */     return offset + this.elements.length;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 79 */   ImmutableList<E> createAsList() { return new RegularImmutableAsList(this, this.elements); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   boolean isPartialView() { return false; }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public int hashCode() { return this.hashCode; }
/*    */ 
/*    */ 
/*    */   
/* 92 */   boolean isHashCodeFast() { return true; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/RegularImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */