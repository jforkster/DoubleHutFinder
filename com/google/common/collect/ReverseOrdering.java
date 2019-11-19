/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ReverseOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Ordering<? super T> forwardOrder;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 34 */   ReverseOrdering(Ordering<? super T> forwardOrder) { this.forwardOrder = (Ordering)Preconditions.checkNotNull(forwardOrder); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public int compare(T a, T b) { return this.forwardOrder.compare(b, a); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public <S extends T> Ordering<S> reverse() { return this.forwardOrder; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public <E extends T> E min(E a, E b) { return (E)this.forwardOrder.max(a, b); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public <E extends T> E min(E a, E b, E c, E... rest) { return (E)this.forwardOrder.max(a, b, c, rest); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public <E extends T> E min(Iterator<E> iterator) { return (E)this.forwardOrder.max(iterator); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public <E extends T> E min(Iterable<E> iterable) { return (E)this.forwardOrder.max(iterable); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public <E extends T> E max(E a, E b) { return (E)this.forwardOrder.min(a, b); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public <E extends T> E max(E a, E b, E c, E... rest) { return (E)this.forwardOrder.min(a, b, c, rest); }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public <E extends T> E max(Iterator<E> iterator) { return (E)this.forwardOrder.min(iterator); }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public <E extends T> E max(Iterable<E> iterable) { return (E)this.forwardOrder.min(iterable); }
/*    */ 
/*    */ 
/*    */   
/* 81 */   public int hashCode() { return -this.forwardOrder.hashCode(); }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 85 */     if (object == this) {
/* 86 */       return true;
/*    */     }
/* 88 */     if (object instanceof ReverseOrdering) {
/* 89 */       ReverseOrdering<?> that = (ReverseOrdering)object;
/* 90 */       return this.forwardOrder.equals(that.forwardOrder);
/*    */     } 
/* 92 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 96 */   public String toString() { return this.forwardOrder + ".reverse()"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ReverseOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */