/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.base.Ticker;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class CacheBuilder<K, V>
/*     */   extends Object
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*     */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*     */   
/* 159 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*     */       {
/*     */         public void recordHits(int count) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void recordMisses(int count) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void recordLoadSuccess(long loadTime) {}
/*     */ 
/*     */         
/*     */         public void recordLoadException(long loadTime) {}
/*     */ 
/*     */         
/*     */         public void recordEviction() {}
/*     */ 
/*     */         
/* 178 */         public CacheStats snapshot() { return CacheBuilder.EMPTY_STATS; }
/*     */       });
/*     */   
/* 181 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*     */   
/* 183 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>()
/*     */     {
/*     */       public AbstractCache.StatsCounter get()
/*     */       {
/* 187 */         return new AbstractCache.SimpleStatsCounter();
/*     */       }
/*     */     };
/*     */   
/*     */   enum NullListener implements RemovalListener<Object, Object> {
/* 192 */     INSTANCE;
/*     */     
/*     */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*     */   }
/*     */   
/*     */   enum OneWeigher
/*     */     implements Weigher<Object, Object> {
/* 199 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/* 203 */     public int weigh(Object key, Object value) { return 1; }
/*     */   }
/*     */ 
/*     */   
/* 207 */   static final Ticker NULL_TICKER = new Ticker()
/*     */     {
/*     */       public long read() {
/* 210 */         return 0L;
/*     */       }
/*     */     };
/*     */   
/* 214 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*     */   
/*     */   static final int UNSET_INT = -1;
/*     */   
/*     */   boolean strictParsing = true;
/*     */   
/* 220 */   int initialCapacity = -1;
/* 221 */   int concurrencyLevel = -1;
/* 222 */   long maximumSize = -1L;
/* 223 */   long maximumWeight = -1L;
/*     */   
/*     */   Weigher<? super K, ? super V> weigher;
/*     */   
/*     */   LocalCache.Strength keyStrength;
/*     */   LocalCache.Strength valueStrength;
/* 229 */   long expireAfterWriteNanos = -1L;
/* 230 */   long expireAfterAccessNanos = -1L;
/* 231 */   long refreshNanos = -1L;
/*     */   
/*     */   Equivalence<Object> keyEquivalence;
/*     */   
/*     */   Equivalence<Object> valueEquivalence;
/*     */   
/*     */   RemovalListener<? super K, ? super V> removalListener;
/*     */   Ticker ticker;
/* 239 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   public static CacheBuilder<Object, Object> newBuilder() { return new CacheBuilder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible("To be supported")
/* 260 */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) { return spec.toCacheBuilder().lenientParsing(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible("To be supported")
/* 274 */   public static CacheBuilder<Object, Object> from(String spec) { return from(CacheBuilderSpec.parse(spec)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   CacheBuilder<K, V> lenientParsing() {
/* 282 */     this.strictParsing = false;
/* 283 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
/* 294 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", new Object[] { this.keyEquivalence });
/* 295 */     this.keyEquivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 300 */   Equivalence<Object> getKeyEquivalence() { return (Equivalence)Objects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
/* 312 */     Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", new Object[] { this.valueEquivalence });
/*     */     
/* 314 */     this.valueEquivalence = (Equivalence)Preconditions.checkNotNull(equivalence);
/* 315 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 319 */   Equivalence<Object> getValueEquivalence() { return (Equivalence)Objects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
/* 333 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", new Object[] { Integer.valueOf(this.initialCapacity) });
/*     */     
/* 335 */     Preconditions.checkArgument((initialCapacity >= 0));
/* 336 */     this.initialCapacity = initialCapacity;
/* 337 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 341 */   int getInitialCapacity() { return (this.initialCapacity == -1) ? 16 : this.initialCapacity; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
/* 375 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", new Object[] { Integer.valueOf(this.concurrencyLevel) });
/*     */     
/* 377 */     Preconditions.checkArgument((concurrencyLevel > 0));
/* 378 */     this.concurrencyLevel = concurrencyLevel;
/* 379 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 383 */   int getConcurrencyLevel() { return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> maximumSize(long size) {
/* 402 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", new Object[] { Long.valueOf(this.maximumSize) });
/*     */     
/* 404 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", new Object[] { Long.valueOf(this.maximumWeight) });
/*     */     
/* 406 */     Preconditions.checkState((this.weigher == null), "maximum size can not be combined with weigher");
/* 407 */     Preconditions.checkArgument((size >= 0L), "maximum size must not be negative");
/* 408 */     this.maximumSize = size;
/* 409 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   public CacheBuilder<K, V> maximumWeight(long weight) {
/* 438 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", new Object[] { Long.valueOf(this.maximumWeight) });
/*     */     
/* 440 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", new Object[] { Long.valueOf(this.maximumSize) });
/*     */     
/* 442 */     this.maximumWeight = weight;
/* 443 */     Preconditions.checkArgument((weight >= 0L), "maximum weight must not be negative");
/* 444 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("To be supported")
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
/* 478 */     Preconditions.checkState((this.weigher == null));
/* 479 */     if (this.strictParsing) {
/* 480 */       Preconditions.checkState((this.maximumSize == -1L), "weigher can not be combined with maximum size", new Object[] { Long.valueOf(this.maximumSize) });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 486 */     CacheBuilder<K1, V1> me = this;
/* 487 */     me.weigher = (Weigher)Preconditions.checkNotNull(weigher);
/* 488 */     return me;
/*     */   }
/*     */   
/*     */   long getMaximumWeight() {
/* 492 */     if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
/* 493 */       return 0L;
/*     */     }
/* 495 */     return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 501 */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() { return (Weigher)Objects.firstNonNull(this.weigher, OneWeigher.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/* 519 */   public CacheBuilder<K, V> weakKeys() { return setKeyStrength(LocalCache.Strength.WEAK); }
/*     */ 
/*     */   
/*     */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/* 523 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", new Object[] { this.keyStrength });
/* 524 */     this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/* 525 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 529 */   LocalCache.Strength getKeyStrength() { return (LocalCache.Strength)Objects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/* 550 */   public CacheBuilder<K, V> weakValues() { return setValueStrength(LocalCache.Strength.WEAK); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.SoftReference")
/* 574 */   public CacheBuilder<K, V> softValues() { return setValueStrength(LocalCache.Strength.SOFT); }
/*     */ 
/*     */   
/*     */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/* 578 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", new Object[] { this.valueStrength });
/* 579 */     this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/* 580 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 584 */   LocalCache.Strength getValueStrength() { return (LocalCache.Strength)Objects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
/* 607 */     Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", new Object[] { Long.valueOf(this.expireAfterWriteNanos) });
/*     */     
/* 609 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", new Object[] { Long.valueOf(duration), unit });
/* 610 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/* 611 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 615 */   long getExpireAfterWriteNanos() { return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
/* 641 */     Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", new Object[] { Long.valueOf(this.expireAfterAccessNanos) });
/*     */     
/* 643 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", new Object[] { Long.valueOf(duration), unit });
/* 644 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/* 645 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 649 */   long getExpireAfterAccessNanos() { return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @GwtIncompatible("To be supported (synchronously).")
/*     */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
/* 681 */     Preconditions.checkNotNull(unit);
/* 682 */     Preconditions.checkState((this.refreshNanos == -1L), "refresh was already set to %s ns", new Object[] { Long.valueOf(this.refreshNanos) });
/* 683 */     Preconditions.checkArgument((duration > 0L), "duration must be positive: %s %s", new Object[] { Long.valueOf(duration), unit });
/* 684 */     this.refreshNanos = unit.toNanos(duration);
/* 685 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 689 */   long getRefreshNanos() { return (this.refreshNanos == -1L) ? 0L : this.refreshNanos; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> ticker(Ticker ticker) {
/* 702 */     Preconditions.checkState((this.ticker == null));
/* 703 */     this.ticker = (Ticker)Preconditions.checkNotNull(ticker);
/* 704 */     return this;
/*     */   }
/*     */   
/*     */   Ticker getTicker(boolean recordsTime) {
/* 708 */     if (this.ticker != null) {
/* 709 */       return this.ticker;
/*     */     }
/* 711 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
/* 738 */     Preconditions.checkState((this.removalListener == null));
/*     */ 
/*     */ 
/*     */     
/* 742 */     CacheBuilder<K1, V1> me = this;
/* 743 */     me.removalListener = (RemovalListener)Preconditions.checkNotNull(listener);
/* 744 */     return me;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 750 */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() { return (RemovalListener)Objects.firstNonNull(this.removalListener, NullListener.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheBuilder<K, V> recordStats() {
/* 762 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/* 763 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 767 */   boolean isRecordingStats() { return (this.statsCounterSupplier == CACHE_STATS_COUNTER); }
/*     */ 
/*     */ 
/*     */   
/* 771 */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() { return this.statsCounterSupplier; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
/* 788 */     checkWeightWithWeigher();
/* 789 */     return new LocalCache.LocalLoadingCache(this, loader);
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
/*     */   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
/* 805 */     checkWeightWithWeigher();
/* 806 */     checkNonLoadingCache();
/* 807 */     return new LocalCache.LocalManualCache(this);
/*     */   }
/*     */ 
/*     */   
/* 811 */   private void checkNonLoadingCache() { Preconditions.checkState((this.refreshNanos == -1L), "refreshAfterWrite requires a LoadingCache"); }
/*     */ 
/*     */   
/*     */   private void checkWeightWithWeigher() {
/* 815 */     if (this.weigher == null) {
/* 816 */       Preconditions.checkState((this.maximumWeight == -1L), "maximumWeight requires weigher");
/*     */     }
/* 818 */     else if (this.strictParsing) {
/* 819 */       Preconditions.checkState((this.maximumWeight != -1L), "weigher requires maximumWeight");
/*     */     }
/* 821 */     else if (this.maximumWeight == -1L) {
/* 822 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
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
/*     */   public String toString() {
/* 834 */     Objects.ToStringHelper s = Objects.toStringHelper(this);
/* 835 */     if (this.initialCapacity != -1) {
/* 836 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 838 */     if (this.concurrencyLevel != -1) {
/* 839 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 841 */     if (this.maximumSize != -1L) {
/* 842 */       s.add("maximumSize", this.maximumSize);
/*     */     }
/* 844 */     if (this.maximumWeight != -1L) {
/* 845 */       s.add("maximumWeight", this.maximumWeight);
/*     */     }
/* 847 */     if (this.expireAfterWriteNanos != -1L) {
/* 848 */       s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
/*     */     }
/* 850 */     if (this.expireAfterAccessNanos != -1L) {
/* 851 */       s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
/*     */     }
/* 853 */     if (this.keyStrength != null) {
/* 854 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 856 */     if (this.valueStrength != null) {
/* 857 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 859 */     if (this.keyEquivalence != null) {
/* 860 */       s.addValue("keyEquivalence");
/*     */     }
/* 862 */     if (this.valueEquivalence != null) {
/* 863 */       s.addValue("valueEquivalence");
/*     */     }
/* 865 */     if (this.removalListener != null) {
/* 866 */       s.addValue("removalListener");
/*     */     }
/* 868 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/CacheBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */