/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ReverseNaturalOrdering
/*    */   extends Ordering<Comparable>
/*    */   implements Serializable
/*    */ {
/* 31 */   static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
/*    */   
/*    */   public int compare(Comparable left, Comparable right) {
/* 34 */     Preconditions.checkNotNull(left);
/* 35 */     if (left == right) {
/* 36 */       return 0;
/*    */     }
/*    */     
/* 39 */     return right.compareTo(left);
/*    */   }
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 43 */   public <S extends Comparable> Ordering<S> reverse() { return Ordering.natural(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public <E extends Comparable> E min(E a, E b) { return (E)(Comparable)NaturalOrdering.INSTANCE.max(a, b); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public <E extends Comparable> E min(E a, E b, E c, E... rest) { return (E)(Comparable)NaturalOrdering.INSTANCE.max(a, b, c, rest); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public <E extends Comparable> E min(Iterator<E> iterator) { return (E)(Comparable)NaturalOrdering.INSTANCE.max(iterator); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public <E extends Comparable> E min(Iterable<E> iterable) { return (E)(Comparable)NaturalOrdering.INSTANCE.max(iterable); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public <E extends Comparable> E max(E a, E b) { return (E)(Comparable)NaturalOrdering.INSTANCE.min(a, b); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public <E extends Comparable> E max(E a, E b, E c, E... rest) { return (E)(Comparable)NaturalOrdering.INSTANCE.min(a, b, c, rest); }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public <E extends Comparable> E max(Iterator<E> iterator) { return (E)(Comparable)NaturalOrdering.INSTANCE.min(iterator); }
/*    */ 
/*    */ 
/*    */   
/* 77 */   public <E extends Comparable> E max(Iterable<E> iterable) { return (E)(Comparable)NaturalOrdering.INSTANCE.min(iterable); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   private Object readResolve() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public String toString() { return "Ordering.natural().reverse()"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ReverseNaturalOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */