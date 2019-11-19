/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.lang.ref.WeakReference;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class Platform
/*    */ {
/* 34 */   static long systemNanoTime() { return System.nanoTime(); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   static CharMatcher precomputeCharMatcher(CharMatcher matcher) { return matcher.precomputedInternal(); }
/*    */ 
/*    */   
/*    */   static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
/* 42 */     WeakReference<? extends Enum<?>> ref = (WeakReference)Enums.getEnumConstants(enumClass).get(value);
/* 43 */     return (ref == null) ? Optional.absent() : Optional.of(enumClass.cast(ref.get()));
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */