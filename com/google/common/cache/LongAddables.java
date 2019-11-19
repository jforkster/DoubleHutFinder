/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Supplier;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ final class LongAddables
/*    */ {
/*    */   private static final Supplier<LongAddable> SUPPLIER;
/*    */   
/*    */   static  {
/*    */     try {
/* 37 */       new LongAdder();
/* 38 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get() {
/* 41 */             return new LongAdder();
/*    */           }
/*    */         };
/* 44 */     } catch (Throwable t) {
/* 45 */       supplier = new Supplier<LongAddable>()
/*    */         {
/*    */           public LongAddable get() {
/* 48 */             return new LongAddables.PureJavaLongAddable(null);
/*    */           }
/*    */         };
/*    */     } 
/* 52 */     SUPPLIER = supplier;
/*    */   }
/*    */ 
/*    */   
/* 56 */   public static LongAddable create() { return (LongAddable)SUPPLIER.get(); }
/*    */   
/*    */   private static final class PureJavaLongAddable
/*    */     extends AtomicLong implements LongAddable {
/*    */     private PureJavaLongAddable() {}
/*    */     
/* 62 */     public void increment() { getAndIncrement(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     public void add(long x) { getAndAdd(x); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 72 */     public long sum() { return get(); }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/LongAddables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */