/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.math.BigInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class UnsignedLongs
/*     */ {
/*     */   public static final long MAX_VALUE = -1L;
/*     */   
/*  63 */   private static long flip(long a) { return a ^ Float.MIN_VALUE; }
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
/*  76 */   public static int compare(long a, long b) { return Longs.compare(flip(a), flip(b)); }
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
/*     */   public static long min(long... array) {
/*  88 */     Preconditions.checkArgument((array.length > 0));
/*  89 */     long min = flip(array[0]);
/*  90 */     for (int i = 1; i < array.length; i++) {
/*  91 */       long next = flip(array[i]);
/*  92 */       if (next < min) {
/*  93 */         min = next;
/*     */       }
/*     */     } 
/*  96 */     return flip(min);
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
/*     */   public static long max(long... array) {
/* 108 */     Preconditions.checkArgument((array.length > 0));
/* 109 */     long max = flip(array[0]);
/* 110 */     for (int i = 1; i < array.length; i++) {
/* 111 */       long next = flip(array[i]);
/* 112 */       if (next > max) {
/* 113 */         max = next;
/*     */       }
/*     */     } 
/* 116 */     return flip(max);
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
/*     */   public static String join(String separator, long... array) {
/* 128 */     Preconditions.checkNotNull(separator);
/* 129 */     if (array.length == 0) {
/* 130 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 134 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 135 */     builder.append(toString(array[0]));
/* 136 */     for (int i = 1; i < array.length; i++) {
/* 137 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 139 */     return builder.toString();
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
/*     */   
/* 156 */   public static Comparator<long[]> lexicographicalComparator() { return LexicographicalComparator.INSTANCE; }
/*     */   
/*     */   enum LexicographicalComparator
/*     */     implements Comparator<long[]> {
/* 160 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 164 */       int minLength = Math.min(left.length, right.length);
/* 165 */       for (int i = 0; i < minLength; i++) {
/* 166 */         if (left[i] != right[i]) {
/* 167 */           return UnsignedLongs.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 170 */       return left.length - right.length;
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
/*     */   public static long divide(long dividend, long divisor) {
/* 183 */     if (divisor < 0L) {
/* 184 */       if (compare(dividend, divisor) < 0) {
/* 185 */         return 0L;
/*     */       }
/* 187 */       return 1L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 192 */     if (dividend >= 0L) {
/* 193 */       return dividend / divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     long quotient = (dividend >>> true) / divisor << true;
/* 203 */     long rem = dividend - quotient * divisor;
/* 204 */     return quotient + ((compare(rem, divisor) >= 0) ? true : false);
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
/*     */   public static long remainder(long dividend, long divisor) {
/* 217 */     if (divisor < 0L) {
/* 218 */       if (compare(dividend, divisor) < 0) {
/* 219 */         return dividend;
/*     */       }
/* 221 */       return dividend - divisor;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 226 */     if (dividend >= 0L) {
/* 227 */       return dividend % divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     long quotient = (dividend >>> true) / divisor << true;
/* 237 */     long rem = dividend - quotient * divisor;
/* 238 */     return rem - ((compare(rem, divisor) >= 0) ? divisor : 0L);
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
/* 250 */   public static long parseUnsignedLong(String s) { return parseUnsignedLong(s, 10); }
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
/*     */   public static long decode(String stringValue) {
/* 270 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 273 */       return parseUnsignedLong(request.rawValue, request.radix);
/* 274 */     } catch (NumberFormatException e) {
/* 275 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 277 */       decodeException.initCause(e);
/* 278 */       throw decodeException;
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
/*     */ 
/*     */   
/*     */   public static long parseUnsignedLong(String s, int radix) {
/* 294 */     Preconditions.checkNotNull(s);
/* 295 */     if (s.length() == 0) {
/* 296 */       throw new NumberFormatException("empty string");
/*     */     }
/* 298 */     if (radix < 2 || radix > 36) {
/* 299 */       throw new NumberFormatException("illegal radix: " + radix);
/*     */     }
/*     */     
/* 302 */     int max_safe_pos = maxSafeDigits[radix] - 1;
/* 303 */     long value = 0L;
/* 304 */     for (int pos = 0; pos < s.length(); pos++) {
/* 305 */       int digit = Character.digit(s.charAt(pos), radix);
/* 306 */       if (digit == -1) {
/* 307 */         throw new NumberFormatException(s);
/*     */       }
/* 309 */       if (pos > max_safe_pos && overflowInParse(value, digit, radix)) {
/* 310 */         throw new NumberFormatException("Too large for unsigned long: " + s);
/*     */       }
/* 312 */       value = value * radix + digit;
/*     */     } 
/*     */     
/* 315 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean overflowInParse(long current, int digit, int radix) {
/* 325 */     if (current >= 0L) {
/* 326 */       if (current < maxValueDivs[radix]) {
/* 327 */         return false;
/*     */       }
/* 329 */       if (current > maxValueDivs[radix]) {
/* 330 */         return true;
/*     */       }
/*     */       
/* 333 */       return (digit > maxValueMods[radix]);
/*     */     } 
/*     */ 
/*     */     
/* 337 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 344 */   public static String toString(long x) { return toString(x, 10); }
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
/*     */   public static String toString(long x, int radix) {
/* 357 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", new Object[] { Integer.valueOf(radix) });
/*     */     
/* 359 */     if (x == 0L)
/*     */     {
/* 361 */       return "0";
/*     */     }
/* 363 */     char[] buf = new char[64];
/* 364 */     int i = buf.length;
/* 365 */     if (x < 0L) {
/*     */ 
/*     */       
/* 368 */       long quotient = divide(x, radix);
/* 369 */       long rem = x - quotient * radix;
/* 370 */       buf[--i] = Character.forDigit((int)rem, radix);
/* 371 */       x = quotient;
/*     */     } 
/*     */     
/* 374 */     while (x > 0L) {
/* 375 */       buf[--i] = Character.forDigit((int)(x % radix), radix);
/* 376 */       x /= radix;
/*     */     } 
/*     */     
/* 379 */     return new String(buf, i, buf.length - i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 384 */   private static final long[] maxValueDivs = new long[37];
/* 385 */   private static final int[] maxValueMods = new int[37];
/* 386 */   private static final int[] maxSafeDigits = new int[37];
/*     */   static  {
/* 388 */     overflow = new BigInteger("10000000000000000", 16);
/* 389 */     for (int i = 2; i <= 36; i++) {
/* 390 */       maxValueDivs[i] = divide(-1L, i);
/* 391 */       maxValueMods[i] = (int)remainder(-1L, i);
/* 392 */       maxSafeDigits[i] = overflow.toString(i).length() - 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/primitives/UnsignedLongs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */