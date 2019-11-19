/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class UnsignedInteger
/*     */   extends Number
/*     */   implements Comparable<UnsignedInteger>
/*     */ {
/*  46 */   public static final UnsignedInteger ZERO = fromIntBits(0);
/*  47 */   public static final UnsignedInteger ONE = fromIntBits(1);
/*  48 */   public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
/*     */ 
/*     */   
/*     */   private final int value;
/*     */ 
/*     */   
/*  54 */   private UnsignedInteger(int value) { this.value = value & 0xFFFFFFFF; }
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
/*  70 */   public static UnsignedInteger fromIntBits(int bits) { return new UnsignedInteger(bits); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(long value) {
/*  78 */     Preconditions.checkArgument(((value & 0xFFFFFFFFL) == value), "value (%s) is outside the range for an unsigned integer value", new Object[] { Long.valueOf(value) });
/*     */     
/*  80 */     return fromIntBits((int)value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static UnsignedInteger valueOf(BigInteger value) {
/*  90 */     Preconditions.checkNotNull(value);
/*  91 */     Preconditions.checkArgument((value.signum() >= 0 && value.bitLength() <= 32), "value (%s) is outside the range for an unsigned integer value", new Object[] { value });
/*     */     
/*  93 */     return fromIntBits(value.intValue());
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
/* 104 */   public static UnsignedInteger valueOf(String string) { return valueOf(string, 10); }
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
/* 115 */   public static UnsignedInteger valueOf(String string, int radix) { return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix)); }
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
/* 126 */   public UnsignedInteger plus(UnsignedInteger val) { return fromIntBits(this.value + ((UnsignedInteger)Preconditions.checkNotNull(val)).value); }
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
/* 137 */   public UnsignedInteger minus(UnsignedInteger val) { return fromIntBits(this.value - ((UnsignedInteger)Preconditions.checkNotNull(val)).value); }
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
/*     */   @CheckReturnValue
/*     */   @GwtIncompatible("Does not truncate correctly")
/* 150 */   public UnsignedInteger times(UnsignedInteger val) { return fromIntBits(this.value * ((UnsignedInteger)Preconditions.checkNotNull(val)).value); }
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
/* 161 */   public UnsignedInteger dividedBy(UnsignedInteger val) { return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value)); }
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
/* 172 */   public UnsignedInteger mod(UnsignedInteger val) { return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger)Preconditions.checkNotNull(val)).value)); }
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
/* 184 */   public int intValue() { return this.value; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public long longValue() { return UnsignedInts.toLong(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public float floatValue() { return (float)longValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public double doubleValue() { return longValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public BigInteger bigIntegerValue() { return BigInteger.valueOf(longValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(UnsignedInteger other) {
/* 227 */     Preconditions.checkNotNull(other);
/* 228 */     return UnsignedInts.compare(this.value, other.value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 233 */   public int hashCode() { return this.value; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 238 */     if (obj instanceof UnsignedInteger) {
/* 239 */       UnsignedInteger other = (UnsignedInteger)obj;
/* 240 */       return (this.value == other.value);
/*     */     } 
/* 242 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public String toString() { return toString(10); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 259 */   public String toString(int radix) { return UnsignedInts.toString(this.value, radix); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/primitives/UnsignedInteger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */