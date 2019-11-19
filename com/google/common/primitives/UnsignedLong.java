/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public final class UnsignedLong
/*     */   extends Number
/*     */   implements Comparable<UnsignedLong>, Serializable
/*     */ {
/*     */   private static final long UNSIGNED_MASK = 9223372036854775807L;
/*  47 */   public static final UnsignedLong ZERO = new UnsignedLong(0L);
/*  48 */   public static final UnsignedLong ONE = new UnsignedLong(1L);
/*  49 */   public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1L);
/*     */   
/*     */   private final long value;
/*     */ 
/*     */   
/*  54 */   private UnsignedLong(long value) { this.value = value; }
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
/*  72 */   public static UnsignedLong fromLongBits(long bits) { return new UnsignedLong(bits); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedLong valueOf(long value) {
/*  82 */     Preconditions.checkArgument((value >= 0L), "value (%s) is outside the range for an unsigned long value", new Object[] { Long.valueOf(value) });
/*     */     
/*  84 */     return fromLongBits(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedLong valueOf(BigInteger value) {
/*  94 */     Preconditions.checkNotNull(value);
/*  95 */     Preconditions.checkArgument((value.signum() >= 0 && value.bitLength() <= 64), "value (%s) is outside the range for an unsigned long value", new Object[] { value });
/*     */     
/*  97 */     return fromLongBits(value.longValue());
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
/* 108 */   public static UnsignedLong valueOf(String string) { return valueOf(string, 10); }
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
/* 120 */   public static UnsignedLong valueOf(String string, int radix) { return fromLongBits(UnsignedLongs.parseUnsignedLong(string, radix)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public UnsignedLong plus(UnsignedLong val) { return fromLongBits(this.value + ((UnsignedLong)Preconditions.checkNotNull(val)).value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public UnsignedLong minus(UnsignedLong val) { return fromLongBits(this.value - ((UnsignedLong)Preconditions.checkNotNull(val)).value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 151 */   public UnsignedLong times(UnsignedLong val) { return fromLongBits(this.value * ((UnsignedLong)Preconditions.checkNotNull(val)).value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 161 */   public UnsignedLong dividedBy(UnsignedLong val) { return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong)Preconditions.checkNotNull(val)).value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 171 */   public UnsignedLong mod(UnsignedLong val) { return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong)Preconditions.checkNotNull(val)).value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public int intValue() { return (int)this.value; }
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
/* 191 */   public long longValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 201 */     float fValue = (float)(this.value & Float.MAX_VALUE);
/* 202 */     if (this.value < 0L) {
/* 203 */       fValue += 9.223372E18F;
/*     */     }
/* 205 */     return fValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 215 */     double dValue = (this.value & Float.MAX_VALUE);
/* 216 */     if (this.value < 0L) {
/* 217 */       dValue += 9.223372036854776E18D;
/*     */     }
/* 219 */     return dValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/* 226 */     BigInteger bigInt = BigInteger.valueOf(this.value & Float.MAX_VALUE);
/* 227 */     if (this.value < 0L) {
/* 228 */       bigInt = bigInt.setBit(63);
/*     */     }
/* 230 */     return bigInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedLong o) {
/* 235 */     Preconditions.checkNotNull(o);
/* 236 */     return UnsignedLongs.compare(this.value, o.value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 241 */   public int hashCode() { return Longs.hashCode(this.value); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 246 */     if (obj instanceof UnsignedLong) {
/* 247 */       UnsignedLong other = (UnsignedLong)obj;
/* 248 */       return (this.value == other.value);
/*     */     } 
/* 250 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public String toString() { return UnsignedLongs.toString(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   public String toString(int radix) { return UnsignedLongs.toString(this.value, radix); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/primitives/UnsignedLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */