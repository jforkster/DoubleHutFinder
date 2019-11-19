/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ 
/*    */ @GwtCompatible
/*    */ public class ExecutionError
/*    */   extends Error
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   protected ExecutionError() {}
/*    */   
/* 46 */   protected ExecutionError(@Nullable String message) { super(message); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public ExecutionError(@Nullable String message, @Nullable Error cause) { super(message, cause); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public ExecutionError(@Nullable Error cause) { super(cause); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ExecutionError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */