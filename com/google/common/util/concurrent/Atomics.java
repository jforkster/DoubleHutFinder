/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*    */ public final class Atomics
/*    */ {
/* 40 */   public static <V> AtomicReference<V> newReference() { return new AtomicReference(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static <V> AtomicReference<V> newReference(@Nullable V initialValue) { return new AtomicReference(initialValue); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public static <E> AtomicReferenceArray<E> newReferenceArray(int length) { return new AtomicReferenceArray(length); }
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
/* 71 */   public static <E> AtomicReferenceArray<E> newReferenceArray(E[] array) { return new AtomicReferenceArray(array); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/Atomics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */