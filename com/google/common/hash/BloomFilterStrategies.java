/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
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
/*     */ static final abstract enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*     */   MURMUR128_MITZ_32, MURMUR128_MITZ_64;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new com/google/common/hash/BloomFilterStrategies$1
/*     */     //   3: dup
/*     */     //   4: ldc 'MURMUR128_MITZ_32'
/*     */     //   6: iconst_0
/*     */     //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   10: putstatic com/google/common/hash/BloomFilterStrategies.MURMUR128_MITZ_32 : Lcom/google/common/hash/BloomFilterStrategies;
/*     */     //   13: new com/google/common/hash/BloomFilterStrategies$2
/*     */     //   16: dup
/*     */     //   17: ldc 'MURMUR128_MITZ_64'
/*     */     //   19: iconst_1
/*     */     //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */     //   23: putstatic com/google/common/hash/BloomFilterStrategies.MURMUR128_MITZ_64 : Lcom/google/common/hash/BloomFilterStrategies;
/*     */     //   26: iconst_2
/*     */     //   27: anewarray com/google/common/hash/BloomFilterStrategies
/*     */     //   30: dup
/*     */     //   31: iconst_0
/*     */     //   32: getstatic com/google/common/hash/BloomFilterStrategies.MURMUR128_MITZ_32 : Lcom/google/common/hash/BloomFilterStrategies;
/*     */     //   35: aastore
/*     */     //   36: dup
/*     */     //   37: iconst_1
/*     */     //   38: getstatic com/google/common/hash/BloomFilterStrategies.MURMUR128_MITZ_64 : Lcom/google/common/hash/BloomFilterStrategies;
/*     */     //   41: aastore
/*     */     //   42: putstatic com/google/common/hash/BloomFilterStrategies.$VALUES : [Lcom/google/common/hash/BloomFilterStrategies;
/*     */     //   45: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #44	-> 0
/*     */     //   #90	-> 13
/*     */     //   #38	-> 26
/*     */   }
/*     */   
/*     */   static final class BitArray
/*     */   {
/*     */     final long[] data;
/*     */     long bitCount;
/*     */     
/* 145 */     BitArray(long bits) { this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]); }
/*     */ 
/*     */ 
/*     */     
/*     */     BitArray(long[] data) {
/* 150 */       Preconditions.checkArgument((data.length > 0), "data length is zero!");
/* 151 */       this.data = data;
/* 152 */       long bitCount = 0L;
/* 153 */       for (long value : data) {
/* 154 */         bitCount += Long.bitCount(value);
/*     */       }
/* 156 */       this.bitCount = bitCount;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean set(long index) {
/* 161 */       if (!get(index)) {
/* 162 */         this.data[(int)(index >>> 6)] = this.data[(int)(index >>> 6)] | 1L << (int)index;
/* 163 */         this.bitCount++;
/* 164 */         return true;
/*     */       } 
/* 166 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 170 */     boolean get(long index) { return ((this.data[(int)(index >>> 6)] & 1L << (int)index) != 0L); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     long bitSize() { return this.data.length * 64L; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     long bitCount() { return this.bitCount; }
/*     */ 
/*     */ 
/*     */     
/* 184 */     BitArray copy() { return new BitArray((long[])this.data.clone()); }
/*     */ 
/*     */ 
/*     */     
/*     */     void putAll(BitArray array) {
/* 189 */       Preconditions.checkArgument((this.data.length == array.data.length), "BitArrays must be of equal length (%s != %s)", new Object[] { Integer.valueOf(this.data.length), Integer.valueOf(array.data.length) });
/*     */       
/* 191 */       this.bitCount = 0L;
/* 192 */       for (int i = 0; i < this.data.length; i++) {
/* 193 */         this.data[i] = this.data[i] | array.data[i];
/* 194 */         this.bitCount += Long.bitCount(this.data[i]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 199 */       if (o instanceof BitArray) {
/* 200 */         BitArray bitArray = (BitArray)o;
/* 201 */         return Arrays.equals(this.data, bitArray.data);
/*     */       } 
/* 203 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 207 */     public int hashCode() { return Arrays.hashCode(this.data); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/BloomFilterStrategies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */