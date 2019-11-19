/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class AllEqualOrdering
/*    */   extends Ordering<Object>
/*    */   implements Serializable
/*    */ {
/* 33 */   static final AllEqualOrdering INSTANCE = new AllEqualOrdering();
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 37 */   public int compare(@Nullable Object left, @Nullable Object right) { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public <E> List<E> sortedCopy(Iterable<E> iterable) { return Lists.newArrayList(iterable); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public <E> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) { return ImmutableList.copyOf(iterable); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public <S> Ordering<S> reverse() { return this; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   private Object readResolve() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public String toString() { return "Ordering.allEqual()"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AllEqualOrdering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */