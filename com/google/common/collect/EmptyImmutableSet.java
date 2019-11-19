/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class EmptyImmutableSet
/*    */   extends ImmutableSet<Object>
/*    */ {
/* 33 */   static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
/*    */ 
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */ 
/*    */   
/* 39 */   public int size() { return 0; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public boolean isEmpty() { return true; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public boolean contains(@Nullable Object target) { return false; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean containsAll(Collection<?> targets) { return targets.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public UnmodifiableIterator<Object> iterator() { return Iterators.emptyIterator(); }
/*    */ 
/*    */ 
/*    */   
/* 59 */   boolean isPartialView() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   int copyIntoArray(Object[] dst, int offset) { return offset; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public ImmutableList<Object> asList() { return ImmutableList.of(); }
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 73 */     if (object instanceof Set) {
/* 74 */       Set<?> that = (Set)object;
/* 75 */       return that.isEmpty();
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 81 */   public final int hashCode() { return 0; }
/*    */ 
/*    */ 
/*    */   
/* 85 */   boolean isHashCodeFast() { return true; }
/*    */ 
/*    */ 
/*    */   
/* 89 */   public String toString() { return "[]"; }
/*    */ 
/*    */ 
/*    */   
/* 93 */   Object readResolve() { return INSTANCE; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */