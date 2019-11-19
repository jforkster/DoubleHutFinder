/*    */ package com.google.common.util.concurrent;
/*    */ 
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
/*    */ public class UncheckedTimeoutException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public UncheckedTimeoutException() {}
/*    */   
/* 31 */   public UncheckedTimeoutException(@Nullable String message) { super(message); }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public UncheckedTimeoutException(@Nullable Throwable cause) { super(cause); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public UncheckedTimeoutException(@Nullable String message, @Nullable Throwable cause) { super(message, cause); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/UncheckedTimeoutException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */