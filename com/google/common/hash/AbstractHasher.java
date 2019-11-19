/*    */ package com.google.common.hash;
/*    */ 
/*    */ import java.nio.charset.Charset;
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
/*    */ abstract class AbstractHasher
/*    */   implements Hasher
/*    */ {
/* 28 */   public final Hasher putBoolean(boolean b) { return putByte(b ? 1 : 0); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public final Hasher putDouble(double d) { return putLong(Double.doubleToRawLongBits(d)); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public final Hasher putFloat(float f) { return putInt(Float.floatToRawIntBits(f)); }
/*    */ 
/*    */   
/*    */   public Hasher putUnencodedChars(CharSequence charSequence) {
/* 40 */     for (int i = 0, len = charSequence.length(); i < len; i++) {
/* 41 */       putChar(charSequence.charAt(i));
/*    */     }
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 47 */   public Hasher putString(CharSequence charSequence, Charset charset) { return putBytes(charSequence.toString().getBytes(charset)); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/AbstractHasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */