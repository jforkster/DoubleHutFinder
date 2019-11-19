/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigInteger;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ @Beta
/*     */ public abstract class DiscreteDomain<C extends Comparable>
/*     */   extends Object
/*     */ {
/*  54 */   public static DiscreteDomain<Integer> integers() { return 
/*     */ 
/*     */       
/*  57 */       INSTANCE; }
/*     */   
/*  59 */   private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable { private static final IntegerDomain INSTANCE = new IntegerDomain(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Integer next(Integer value) {
/*  62 */       int i = value.intValue();
/*  63 */       return (i == Integer.MAX_VALUE) ? null : Integer.valueOf(i + 1);
/*     */     }
/*     */     
/*     */     public Integer previous(Integer value) {
/*  67 */       int i = value.intValue();
/*  68 */       return (i == Integer.MIN_VALUE) ? null : Integer.valueOf(i - 1);
/*     */     }
/*     */ 
/*     */     
/*  72 */     public long distance(Integer start, Integer end) { return end.intValue() - start.intValue(); }
/*     */ 
/*     */ 
/*     */     
/*  76 */     public Integer minValue() { return Integer.valueOf(-2147483648); }
/*     */ 
/*     */ 
/*     */     
/*  80 */     public Integer maxValue() { return Integer.valueOf(2147483647); }
/*     */ 
/*     */ 
/*     */     
/*  84 */     private Object readResolve() { return INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     public String toString() { return "DiscreteDomain.integers()"; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static DiscreteDomain<Long> longs() { return 
/*     */ 
/*     */       
/* 104 */       INSTANCE; }
/*     */   
/* 106 */   private static final class LongDomain extends DiscreteDomain<Long> implements Serializable { private static final LongDomain INSTANCE = new LongDomain(); private static final long serialVersionUID = 0L;
/*     */     
/*     */     public Long next(Long value) {
/* 109 */       long l = value.longValue();
/* 110 */       return (l == Float.MAX_VALUE) ? null : Long.valueOf(l + 1L);
/*     */     }
/*     */     
/*     */     public Long previous(Long value) {
/* 114 */       long l = value.longValue();
/* 115 */       return (l == Float.MIN_VALUE) ? null : Long.valueOf(l - 1L);
/*     */     }
/*     */     
/*     */     public long distance(Long start, Long end) {
/* 119 */       long result = end.longValue() - start.longValue();
/* 120 */       if (end.longValue() > start.longValue() && result < 0L) {
/* 121 */         return Float.MAX_VALUE;
/*     */       }
/* 123 */       if (end.longValue() < start.longValue() && result > 0L) {
/* 124 */         return Float.MIN_VALUE;
/*     */       }
/* 126 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 130 */     public Long minValue() { return Long.valueOf(Float.MIN_VALUE); }
/*     */ 
/*     */ 
/*     */     
/* 134 */     public Long maxValue() { return Long.valueOf(Float.MAX_VALUE); }
/*     */ 
/*     */ 
/*     */     
/* 138 */     private Object readResolve() { return INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     public String toString() { return "DiscreteDomain.longs()"; } }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract C next(C paramC);
/*     */ 
/*     */   
/*     */   public abstract C previous(C paramC);
/*     */ 
/*     */   
/*     */   public abstract long distance(C paramC1, C paramC2);
/*     */   
/* 155 */   public static DiscreteDomain<BigInteger> bigIntegers() { return 
/*     */ 
/*     */       
/* 158 */       INSTANCE; }
/*     */   
/* 160 */   private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable { private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
/*     */     
/* 162 */     private static final BigInteger MIN_LONG = BigInteger.valueOf(Float.MIN_VALUE);
/*     */     
/* 164 */     private static final BigInteger MAX_LONG = BigInteger.valueOf(Float.MAX_VALUE);
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 168 */     public BigInteger next(BigInteger value) { return value.add(BigInteger.ONE); }
/*     */ 
/*     */ 
/*     */     
/* 172 */     public BigInteger previous(BigInteger value) { return value.subtract(BigInteger.ONE); }
/*     */ 
/*     */ 
/*     */     
/* 176 */     public long distance(BigInteger start, BigInteger end) { return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue(); }
/*     */ 
/*     */ 
/*     */     
/* 180 */     private Object readResolve() { return INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     public String toString() { return "DiscreteDomain.bigIntegers()"; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   public C minValue() { throw new NoSuchElementException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 260 */   public C maxValue() { throw new NoSuchElementException(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/DiscreteDomain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */