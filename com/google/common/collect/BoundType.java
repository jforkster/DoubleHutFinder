/*    */ package com.google.common.collect;
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
/*    */ public static final abstract enum BoundType
/*    */ {
/*    */   OPEN, CLOSED;
/*    */   
/*    */   static  {
/*    */     // Byte code:
/*    */     //   0: new com/google/common/collect/BoundType$1
/*    */     //   3: dup
/*    */     //   4: ldc 'OPEN'
/*    */     //   6: iconst_0
/*    */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   10: putstatic com/google/common/collect/BoundType.OPEN : Lcom/google/common/collect/BoundType;
/*    */     //   13: new com/google/common/collect/BoundType$2
/*    */     //   16: dup
/*    */     //   17: ldc 'CLOSED'
/*    */     //   19: iconst_1
/*    */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   23: putstatic com/google/common/collect/BoundType.CLOSED : Lcom/google/common/collect/BoundType;
/*    */     //   26: iconst_2
/*    */     //   27: anewarray com/google/common/collect/BoundType
/*    */     //   30: dup
/*    */     //   31: iconst_0
/*    */     //   32: getstatic com/google/common/collect/BoundType.OPEN : Lcom/google/common/collect/BoundType;
/*    */     //   35: aastore
/*    */     //   36: dup
/*    */     //   37: iconst_1
/*    */     //   38: getstatic com/google/common/collect/BoundType.CLOSED : Lcom/google/common/collect/BoundType;
/*    */     //   41: aastore
/*    */     //   42: putstatic com/google/common/collect/BoundType.$VALUES : [Lcom/google/common/collect/BoundType;
/*    */     //   45: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     //   #40	-> 13
/*    */     //   #26	-> 26
/*    */   }
/*    */   
/* 51 */   static BoundType forBoolean(boolean inclusive) { return inclusive ? CLOSED : OPEN; }
/*    */   
/*    */   abstract BoundType flip();
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/BoundType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */