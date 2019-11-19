/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class BloomFilter<T>
/*     */   extends Object
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private final BloomFilterStrategies.BitArray bits;
/*     */   private final int numHashFunctions;
/*     */   private final Funnel<T> funnel;
/*     */   private final Strategy strategy;
/*     */   
/*     */   private BloomFilter(BloomFilterStrategies.BitArray bits, int numHashFunctions, Funnel<T> funnel, Strategy strategy) {
/* 105 */     Preconditions.checkArgument((numHashFunctions > 0), "numHashFunctions (%s) must be > 0", new Object[] { Integer.valueOf(numHashFunctions) });
/*     */     
/* 107 */     Preconditions.checkArgument((numHashFunctions <= 255), "numHashFunctions (%s) must be <= 255", new Object[] { Integer.valueOf(numHashFunctions) });
/*     */     
/* 109 */     this.bits = (BloomFilterStrategies.BitArray)Preconditions.checkNotNull(bits);
/* 110 */     this.numHashFunctions = numHashFunctions;
/* 111 */     this.funnel = (Funnel)Preconditions.checkNotNull(funnel);
/* 112 */     this.strategy = (Strategy)Preconditions.checkNotNull(strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public BloomFilter<T> copy() { return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public boolean mightContain(T object) { return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 140 */   public boolean apply(T input) { return mightContain(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public boolean put(T object) { return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public double expectedFpp() { return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 179 */   long bitSize() { return this.bits.bitSize(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompatible(BloomFilter<T> that) {
/* 198 */     Preconditions.checkNotNull(that);
/* 199 */     return (this != that && this.numHashFunctions == that.numHashFunctions && bitSize() == that.bitSize() && this.strategy.equals(that.strategy) && this.funnel.equals(that.funnel));
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
/*     */   public void putAll(BloomFilter<T> that) {
/* 217 */     Preconditions.checkNotNull(that);
/* 218 */     Preconditions.checkArgument((this != that), "Cannot combine a BloomFilter with itself.");
/* 219 */     Preconditions.checkArgument((this.numHashFunctions == that.numHashFunctions), "BloomFilters must have the same number of hash functions (%s != %s)", new Object[] { Integer.valueOf(this.numHashFunctions), Integer.valueOf(that.numHashFunctions) });
/*     */ 
/*     */     
/* 222 */     Preconditions.checkArgument((bitSize() == that.bitSize()), "BloomFilters must have the same size underlying bit arrays (%s != %s)", new Object[] { Long.valueOf(bitSize()), Long.valueOf(that.bitSize()) });
/*     */ 
/*     */     
/* 225 */     Preconditions.checkArgument(this.strategy.equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", new Object[] { this.strategy, that.strategy });
/*     */ 
/*     */     
/* 228 */     Preconditions.checkArgument(this.funnel.equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", new Object[] { this.funnel, that.funnel });
/*     */ 
/*     */     
/* 231 */     this.bits.putAll(that.bits);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 236 */     if (object == this) {
/* 237 */       return true;
/*     */     }
/* 239 */     if (object instanceof BloomFilter) {
/* 240 */       BloomFilter<?> that = (BloomFilter)object;
/* 241 */       return (this.numHashFunctions == that.numHashFunctions && this.funnel.equals(that.funnel) && this.bits.equals(that.bits) && this.strategy.equals(that.strategy));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 246 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 251 */   public int hashCode() { return Objects.hashCode(new Object[] { Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits }); }
/*     */ 
/*     */   
/* 254 */   private static final Strategy DEFAULT_STRATEGY = getDefaultStrategyFromSystemProperty();
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final String USE_MITZ32_PROPERTY = "com.google.common.hash.BloomFilter.useMitz32";
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 262 */   static Strategy getDefaultStrategyFromSystemProperty() { return Boolean.parseBoolean(System.getProperty("com.google.common.hash.BloomFilter.useMitz32")) ? BloomFilterStrategies.MURMUR128_MITZ_32 : BloomFilterStrategies.MURMUR128_MITZ_64; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions, double fpp) { return create(funnel, expectedInsertions, fpp, DEFAULT_STRATEGY); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions, double fpp, Strategy strategy) {
/* 296 */     Preconditions.checkNotNull(funnel);
/* 297 */     Preconditions.checkArgument((expectedInsertions >= 0), "Expected insertions (%s) must be >= 0", new Object[] { Integer.valueOf(expectedInsertions) });
/*     */     
/* 299 */     Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", new Object[] { Double.valueOf(fpp) });
/* 300 */     Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", new Object[] { Double.valueOf(fpp) });
/* 301 */     Preconditions.checkNotNull(strategy);
/*     */     
/* 303 */     if (expectedInsertions == 0) {
/* 304 */       expectedInsertions = 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     long numBits = optimalNumOfBits(expectedInsertions, fpp);
/* 313 */     int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
/*     */     try {
/* 315 */       return new BloomFilter(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, strategy);
/* 316 */     } catch (IllegalArgumentException e) {
/* 317 */       throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions) { return create(funnel, expectedInsertions, 0.03D); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 366 */   static int optimalNumOfHashFunctions(long n, long m) { return Math.max(1, (int)Math.round((m / n) * Math.log(2.0D))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static long optimalNumOfBits(long n, double p) {
/* 380 */     if (p == 0.0D) {
/* 381 */       p = Double.MIN_VALUE;
/*     */     }
/* 383 */     return (long)(-n * Math.log(p) / Math.log(2.0D) * Math.log(2.0D));
/*     */   }
/*     */ 
/*     */   
/* 387 */   private Object writeReplace() { return new SerialForm(this); }
/*     */   
/*     */   private static class SerialForm<T> extends Object implements Serializable {
/*     */     final long[] data;
/*     */     final int numHashFunctions;
/*     */     final Funnel<T> funnel;
/*     */     final BloomFilter.Strategy strategy;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SerialForm(BloomFilter<T> bf) {
/* 397 */       this.data = bf.bits.data;
/* 398 */       this.numHashFunctions = bf.numHashFunctions;
/* 399 */       this.funnel = bf.funnel;
/* 400 */       this.strategy = bf.strategy;
/*     */     }
/*     */     
/* 403 */     Object readResolve() { return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy, null); }
/*     */   }
/*     */   
/*     */   static interface Strategy extends Serializable {
/*     */     <T> boolean put(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.BitArray param1BitArray);
/*     */     
/*     */     <T> boolean mightContain(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.BitArray param1BitArray);
/*     */     
/*     */     int ordinal();
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/BloomFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */