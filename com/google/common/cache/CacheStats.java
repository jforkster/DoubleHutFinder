/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class CacheStats
/*     */ {
/*     */   private final long hitCount;
/*     */   private final long missCount;
/*     */   private final long loadSuccessCount;
/*     */   private final long loadExceptionCount;
/*     */   private final long totalLoadTime;
/*     */   private final long evictionCount;
/*     */   
/*     */   public CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount) {
/*  79 */     Preconditions.checkArgument((hitCount >= 0L));
/*  80 */     Preconditions.checkArgument((missCount >= 0L));
/*  81 */     Preconditions.checkArgument((loadSuccessCount >= 0L));
/*  82 */     Preconditions.checkArgument((loadExceptionCount >= 0L));
/*  83 */     Preconditions.checkArgument((totalLoadTime >= 0L));
/*  84 */     Preconditions.checkArgument((evictionCount >= 0L));
/*     */     
/*  86 */     this.hitCount = hitCount;
/*  87 */     this.missCount = missCount;
/*  88 */     this.loadSuccessCount = loadSuccessCount;
/*  89 */     this.loadExceptionCount = loadExceptionCount;
/*  90 */     this.totalLoadTime = totalLoadTime;
/*  91 */     this.evictionCount = evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public long requestCount() { return this.hitCount + this.missCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public long hitCount() { return this.hitCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hitRate() {
/* 115 */     long requestCount = requestCount();
/* 116 */     return (requestCount == 0L) ? 1.0D : (this.hitCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public long missCount() { return this.missCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double missRate() {
/* 139 */     long requestCount = requestCount();
/* 140 */     return (requestCount == 0L) ? 0.0D : (this.missCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   public long loadCount() { return this.loadSuccessCount + this.loadExceptionCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   public long loadSuccessCount() { return this.loadSuccessCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public long loadExceptionCount() { return this.loadExceptionCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double loadExceptionRate() {
/* 180 */     long totalLoadCount = this.loadSuccessCount + this.loadExceptionCount;
/* 181 */     return (totalLoadCount == 0L) ? 0.0D : (this.loadExceptionCount / totalLoadCount);
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
/* 192 */   public long totalLoadTime() { return this.totalLoadTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double averageLoadPenalty() {
/* 200 */     long totalLoadCount = this.loadSuccessCount + this.loadExceptionCount;
/* 201 */     return (totalLoadCount == 0L) ? 0.0D : (this.totalLoadTime / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public long evictionCount() { return this.evictionCount; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   public CacheStats minus(CacheStats other) { return new CacheStats(Math.max(0L, this.hitCount - other.hitCount), Math.max(0L, this.missCount - other.missCount), Math.max(0L, this.loadSuccessCount - other.loadSuccessCount), Math.max(0L, this.loadExceptionCount - other.loadExceptionCount), Math.max(0L, this.totalLoadTime - other.totalLoadTime), Math.max(0L, this.evictionCount - other.evictionCount)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   public CacheStats plus(CacheStats other) { return new CacheStats(this.hitCount + other.hitCount, this.missCount + other.missCount, this.loadSuccessCount + other.loadSuccessCount, this.loadExceptionCount + other.loadExceptionCount, this.totalLoadTime + other.totalLoadTime, this.evictionCount + other.evictionCount); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   public int hashCode() { return Objects.hashCode(new Object[] { Long.valueOf(this.hitCount), Long.valueOf(this.missCount), Long.valueOf(this.loadSuccessCount), Long.valueOf(this.loadExceptionCount), Long.valueOf(this.totalLoadTime), Long.valueOf(this.evictionCount) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 253 */     if (object instanceof CacheStats) {
/* 254 */       CacheStats other = (CacheStats)object;
/* 255 */       return (this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadExceptionCount == other.loadExceptionCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 267 */   public String toString() { return Objects.toStringHelper(this).add("hitCount", this.hitCount).add("missCount", this.missCount).add("loadSuccessCount", this.loadSuccessCount).add("loadExceptionCount", this.loadExceptionCount).add("totalLoadTime", this.totalLoadTime).add("evictionCount", this.evictionCount).toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/CacheStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */