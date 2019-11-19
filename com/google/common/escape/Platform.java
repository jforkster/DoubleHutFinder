/*    */ package com.google.common.escape;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/* 32 */   static char[] charBufferFromThreadLocal() { return (char[])DEST_TL.get(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>()
/*    */     {
/*    */       protected char[] initialValue() {
/* 43 */         return new char[1024];
/*    */       }
/*    */     };
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/escape/Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */