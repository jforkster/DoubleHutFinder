/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class UnsignedInts
/*     */ {
/*     */   static final long INT_MASK = 4294967295L;
/*     */   
/*  55 */   static int flip(int value) { return value ^ 0x80000000; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static int compare(int a, int b) { return Ints.compare(flip(a), flip(b)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static long toLong(int value) { return value & 0xFFFFFFFFL; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int min(int... array) {
/*  87 */     Preconditions.checkArgument((array.length > 0));
/*  88 */     int min = flip(array[0]);
/*  89 */     for (int i = 1; i < array.length; i++) {
/*  90 */       int next = flip(array[i]);
/*  91 */       if (next < min) {
/*  92 */         min = next;
/*     */       }
/*     */     } 
/*  95 */     return flip(min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int max(int... array) {
/* 107 */     Preconditions.checkArgument((array.length > 0));
/* 108 */     int max = flip(array[0]);
/* 109 */     for (int i = 1; i < array.length; i++) {
/* 110 */       int next = flip(array[i]);
/* 111 */       if (next > max) {
/* 112 */         max = next;
/*     */       }
/*     */     } 
/* 115 */     return flip(max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String join(String separator, int... array) {
/* 127 */     Preconditions.checkNotNull(separator);
/* 128 */     if (array.length == 0) {
/* 129 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 133 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 134 */     builder.append(toString(array[0]));
/* 135 */     for (int i = 1; i < array.length; i++) {
/* 136 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 138 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static Comparator<int[]> lexicographicalComparator() { return LexicographicalComparator.INSTANCE; }
/*     */   
/*     */   enum LexicographicalComparator
/*     */     implements Comparator<int[]> {
/* 158 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 162 */       int minLength = Math.min(left.length, right.length);
/* 163 */       for (int i = 0; i < minLength; i++) {
/* 164 */         if (left[i] != right[i]) {
/* 165 */           return UnsignedInts.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 168 */       return left.length - right.length;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 181 */   public static int divide(int dividend, int divisor) { return (int)(toLong(dividend) / toLong(divisor)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public static int remainder(int dividend, int divisor) { return (int)(toLong(dividend) % toLong(divisor)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int decode(String stringValue) {
/* 212 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 215 */       return parseUnsignedInt(request.rawValue, request.radix);
/* 216 */     } catch (NumberFormatException e) {
/* 217 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 219 */       decodeException.initCause(e);
/* 220 */       throw decodeException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   public static int parseUnsignedInt(String s) { return parseUnsignedInt(s, 10); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int parseUnsignedInt(String string, int radix) {
/* 247 */     Preconditions.checkNotNull(string);
/* 248 */     long result = Long.parseLong(string, radix);
/* 249 */     if ((result & 0xFFFFFFFFL) != result) {
/* 250 */       throw new NumberFormatException("Input " + string + " in base " + radix + " is not in the range of an unsigned integer");
/*     */     }
/*     */     
/* 253 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 260 */   public static String toString(int x) { return toString(x, 10); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(int x, int radix) {
/* 273 */     long asLong = x & 0xFFFFFFFFL;
/* 274 */     return Long.toString(asLong, radix);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/primitives/UnsignedInts.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */