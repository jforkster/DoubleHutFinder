/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ 
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public static final abstract enum RemovalCause {
/*    */   EXPLICIT, REPLACED, COLLECTED, EXPIRED, SIZE;
/*    */   
/*    */   abstract boolean wasEvicted();
/*    */   
/*    */   static  {
/*    */     // Byte code:
/*    */     //   0: new com/google/common/cache/RemovalCause$1
/*    */     //   3: dup
/*    */     //   4: ldc 'EXPLICIT'
/*    */     //   6: iconst_0
/*    */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   10: putstatic com/google/common/cache/RemovalCause.EXPLICIT : Lcom/google/common/cache/RemovalCause;
/*    */     //   13: new com/google/common/cache/RemovalCause$2
/*    */     //   16: dup
/*    */     //   17: ldc 'REPLACED'
/*    */     //   19: iconst_1
/*    */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   23: putstatic com/google/common/cache/RemovalCause.REPLACED : Lcom/google/common/cache/RemovalCause;
/*    */     //   26: new com/google/common/cache/RemovalCause$3
/*    */     //   29: dup
/*    */     //   30: ldc 'COLLECTED'
/*    */     //   32: iconst_2
/*    */     //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   36: putstatic com/google/common/cache/RemovalCause.COLLECTED : Lcom/google/common/cache/RemovalCause;
/*    */     //   39: new com/google/common/cache/RemovalCause$4
/*    */     //   42: dup
/*    */     //   43: ldc 'EXPIRED'
/*    */     //   45: iconst_3
/*    */     //   46: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   49: putstatic com/google/common/cache/RemovalCause.EXPIRED : Lcom/google/common/cache/RemovalCause;
/*    */     //   52: new com/google/common/cache/RemovalCause$5
/*    */     //   55: dup
/*    */     //   56: ldc 'SIZE'
/*    */     //   58: iconst_4
/*    */     //   59: invokespecial <init> : (Ljava/lang/String;I)V
/*    */     //   62: putstatic com/google/common/cache/RemovalCause.SIZE : Lcom/google/common/cache/RemovalCause;
/*    */     //   65: iconst_5
/*    */     //   66: anewarray com/google/common/cache/RemovalCause
/*    */     //   69: dup
/*    */     //   70: iconst_0
/*    */     //   71: getstatic com/google/common/cache/RemovalCause.EXPLICIT : Lcom/google/common/cache/RemovalCause;
/*    */     //   74: aastore
/*    */     //   75: dup
/*    */     //   76: iconst_1
/*    */     //   77: getstatic com/google/common/cache/RemovalCause.REPLACED : Lcom/google/common/cache/RemovalCause;
/*    */     //   80: aastore
/*    */     //   81: dup
/*    */     //   82: iconst_2
/*    */     //   83: getstatic com/google/common/cache/RemovalCause.COLLECTED : Lcom/google/common/cache/RemovalCause;
/*    */     //   86: aastore
/*    */     //   87: dup
/*    */     //   88: iconst_3
/*    */     //   89: getstatic com/google/common/cache/RemovalCause.EXPIRED : Lcom/google/common/cache/RemovalCause;
/*    */     //   92: aastore
/*    */     //   93: dup
/*    */     //   94: iconst_4
/*    */     //   95: getstatic com/google/common/cache/RemovalCause.SIZE : Lcom/google/common/cache/RemovalCause;
/*    */     //   98: aastore
/*    */     //   99: putstatic com/google/common/cache/RemovalCause.$VALUES : [Lcom/google/common/cache/RemovalCause;
/*    */     //   102: return
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #40	-> 0
/*    */     //   #53	-> 13
/*    */     //   #65	-> 26
/*    */     //   #76	-> 39
/*    */     //   #87	-> 52
/*    */     //   #32	-> 65
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/RemovalCause.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */