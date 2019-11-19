/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.annotation.concurrent.GuardedBy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class MapMakerInternalMap<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*      */   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
/*  135 */   private static final Logger logger = Logger.getLogger(MapMakerInternalMap.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentMask;
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentShift;
/*      */ 
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   final int concurrencyLevel;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> keyEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> valueEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength keyStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength valueStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final int maximumSize;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterAccessNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterWriteNanos;
/*      */ 
/*      */   
/*      */   final Queue<MapMaker.RemovalNotification<K, V>> removalNotificationQueue;
/*      */ 
/*      */   
/*      */   final MapMaker.RemovalListener<K, V> removalListener;
/*      */ 
/*      */   
/*      */   final EntryFactory entryFactory;
/*      */ 
/*      */   
/*      */   final Ticker ticker;
/*      */ 
/*      */ 
/*      */   
/*      */   MapMakerInternalMap(MapMaker builder) {
/*  196 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  198 */     this.keyStrength = builder.getKeyStrength();
/*  199 */     this.valueStrength = builder.getValueStrength();
/*      */     
/*  201 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  202 */     this.valueEquivalence = this.valueStrength.defaultEquivalence();
/*      */     
/*  204 */     this.maximumSize = builder.maximumSize;
/*  205 */     this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
/*  206 */     this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
/*      */     
/*  208 */     this.entryFactory = EntryFactory.getFactory(this.keyStrength, expires(), evictsBySize());
/*  209 */     this.ticker = builder.getTicker();
/*      */     
/*  211 */     this.removalListener = builder.getRemovalListener();
/*  212 */     this.removalNotificationQueue = (this.removalListener == GenericMapMaker.NullListener.INSTANCE) ? discardingQueue() : new ConcurrentLinkedQueue();
/*      */ 
/*      */ 
/*      */     
/*  216 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*  217 */     if (evictsBySize()) {
/*  218 */       initialCapacity = Math.min(initialCapacity, this.maximumSize);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  224 */     int segmentShift = 0;
/*  225 */     int segmentCount = 1;
/*      */     
/*  227 */     while (segmentCount < this.concurrencyLevel && (!evictsBySize() || segmentCount * 2 <= this.maximumSize)) {
/*  228 */       segmentShift++;
/*  229 */       segmentCount <<= 1;
/*      */     } 
/*  231 */     this.segmentShift = 32 - segmentShift;
/*  232 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  234 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  236 */     int segmentCapacity = initialCapacity / segmentCount;
/*  237 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  238 */       segmentCapacity++;
/*      */     }
/*      */     
/*  241 */     int segmentSize = 1;
/*  242 */     while (segmentSize < segmentCapacity) {
/*  243 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  246 */     if (evictsBySize()) {
/*      */       
/*  248 */       int maximumSegmentSize = this.maximumSize / segmentCount + 1;
/*  249 */       int remainder = this.maximumSize % segmentCount;
/*  250 */       for (int i = 0; i < this.segments.length; i++) {
/*  251 */         if (i == remainder) {
/*  252 */           maximumSegmentSize--;
/*      */         }
/*  254 */         this.segments[i] = createSegment(segmentSize, maximumSegmentSize);
/*      */       } 
/*      */     } else {
/*      */       
/*  258 */       for (int i = 0; i < this.segments.length; i++) {
/*  259 */         this.segments[i] = createSegment(segmentSize, -1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  266 */   boolean evictsBySize() { return (this.maximumSize != -1); }
/*      */ 
/*      */ 
/*      */   
/*  270 */   boolean expires() { return (expiresAfterWrite() || expiresAfterAccess()); }
/*      */ 
/*      */ 
/*      */   
/*  274 */   boolean expiresAfterWrite() { return (this.expireAfterWriteNanos > 0L); }
/*      */ 
/*      */ 
/*      */   
/*  278 */   boolean expiresAfterAccess() { return (this.expireAfterAccessNanos > 0L); }
/*      */ 
/*      */ 
/*      */   
/*  282 */   boolean usesKeyReferences() { return (this.keyStrength != Strength.STRONG); }
/*      */ 
/*      */ 
/*      */   
/*  286 */   boolean usesValueReferences() { return (this.valueStrength != Strength.STRONG); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final abstract enum Strength
/*      */   {
/*  720 */     STRONG, SOFT, WEAK; abstract <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> param1Segment, MapMakerInternalMap.ReferenceEntry<K, V> param1ReferenceEntry, V param1V); abstract Equivalence<Object> defaultEquivalence(); static  { // Byte code:
/*      */       //   0: new com/google/common/collect/MapMakerInternalMap$Strength$1
/*      */       //   3: dup
/*      */       //   4: ldc 'STRONG'
/*      */       //   6: iconst_0
/*      */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   10: putstatic com/google/common/collect/MapMakerInternalMap$Strength.STRONG : Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   13: new com/google/common/collect/MapMakerInternalMap$Strength$2
/*      */       //   16: dup
/*      */       //   17: ldc 'SOFT'
/*      */       //   19: iconst_1
/*      */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   23: putstatic com/google/common/collect/MapMakerInternalMap$Strength.SOFT : Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   26: new com/google/common/collect/MapMakerInternalMap$Strength$3
/*      */       //   29: dup
/*      */       //   30: ldc 'WEAK'
/*      */       //   32: iconst_2
/*      */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   36: putstatic com/google/common/collect/MapMakerInternalMap$Strength.WEAK : Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   39: iconst_3
/*      */       //   40: anewarray com/google/common/collect/MapMakerInternalMap$Strength
/*      */       //   43: dup
/*      */       //   44: iconst_0
/*      */       //   45: getstatic com/google/common/collect/MapMakerInternalMap$Strength.STRONG : Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   48: aastore
/*      */       //   49: dup
/*      */       //   50: iconst_1
/*      */       //   51: getstatic com/google/common/collect/MapMakerInternalMap$Strength.SOFT : Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   54: aastore
/*      */       //   55: dup
/*      */       //   56: iconst_2
/*      */       //   57: getstatic com/google/common/collect/MapMakerInternalMap$Strength.WEAK : Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   60: aastore
/*      */       //   61: putstatic com/google/common/collect/MapMakerInternalMap$Strength.$VALUES : [Lcom/google/common/collect/MapMakerInternalMap$Strength;
/*      */       //   64: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #295	-> 0
/*      */       //   #308	-> 13
/*      */       //   #321	-> 26
/*      */       //   #289	-> 39 } } final abstract enum EntryFactory { static final int EXPIRABLE_MASK = 1; static final int EVICTABLE_MASK = 2; static final EntryFactory[][] factories; static  { // Byte code:
/*      */       //   0: new com/google/common/collect/MapMakerInternalMap$EntryFactory$1
/*      */       //   3: dup
/*      */       //   4: ldc 'STRONG'
/*      */       //   6: iconst_0
/*      */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   10: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   13: new com/google/common/collect/MapMakerInternalMap$EntryFactory$2
/*      */       //   16: dup
/*      */       //   17: ldc 'STRONG_EXPIRABLE'
/*      */       //   19: iconst_1
/*      */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   23: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EXPIRABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   26: new com/google/common/collect/MapMakerInternalMap$EntryFactory$3
/*      */       //   29: dup
/*      */       //   30: ldc 'STRONG_EVICTABLE'
/*      */       //   32: iconst_2
/*      */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   36: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   39: new com/google/common/collect/MapMakerInternalMap$EntryFactory$4
/*      */       //   42: dup
/*      */       //   43: ldc 'STRONG_EXPIRABLE_EVICTABLE'
/*      */       //   45: iconst_3
/*      */       //   46: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   49: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EXPIRABLE_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   52: new com/google/common/collect/MapMakerInternalMap$EntryFactory$5
/*      */       //   55: dup
/*      */       //   56: ldc 'WEAK'
/*      */       //   58: iconst_4
/*      */       //   59: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   62: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   65: new com/google/common/collect/MapMakerInternalMap$EntryFactory$6
/*      */       //   68: dup
/*      */       //   69: ldc 'WEAK_EXPIRABLE'
/*      */       //   71: iconst_5
/*      */       //   72: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   75: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EXPIRABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   78: new com/google/common/collect/MapMakerInternalMap$EntryFactory$7
/*      */       //   81: dup
/*      */       //   82: ldc 'WEAK_EVICTABLE'
/*      */       //   84: bipush #6
/*      */       //   86: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   89: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   92: new com/google/common/collect/MapMakerInternalMap$EntryFactory$8
/*      */       //   95: dup
/*      */       //   96: ldc 'WEAK_EXPIRABLE_EVICTABLE'
/*      */       //   98: bipush #7
/*      */       //   100: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   103: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EXPIRABLE_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   106: bipush #8
/*      */       //   108: anewarray com/google/common/collect/MapMakerInternalMap$EntryFactory
/*      */       //   111: dup
/*      */       //   112: iconst_0
/*      */       //   113: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   116: aastore
/*      */       //   117: dup
/*      */       //   118: iconst_1
/*      */       //   119: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EXPIRABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   122: aastore
/*      */       //   123: dup
/*      */       //   124: iconst_2
/*      */       //   125: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   128: aastore
/*      */       //   129: dup
/*      */       //   130: iconst_3
/*      */       //   131: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EXPIRABLE_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   134: aastore
/*      */       //   135: dup
/*      */       //   136: iconst_4
/*      */       //   137: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   140: aastore
/*      */       //   141: dup
/*      */       //   142: iconst_5
/*      */       //   143: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EXPIRABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   146: aastore
/*      */       //   147: dup
/*      */       //   148: bipush #6
/*      */       //   150: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   153: aastore
/*      */       //   154: dup
/*      */       //   155: bipush #7
/*      */       //   157: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EXPIRABLE_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   160: aastore
/*      */       //   161: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.$VALUES : [Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   164: iconst_3
/*      */       //   165: anewarray [Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   168: dup
/*      */       //   169: iconst_0
/*      */       //   170: iconst_4
/*      */       //   171: anewarray com/google/common/collect/MapMakerInternalMap$EntryFactory
/*      */       //   174: dup
/*      */       //   175: iconst_0
/*      */       //   176: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   179: aastore
/*      */       //   180: dup
/*      */       //   181: iconst_1
/*      */       //   182: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EXPIRABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   185: aastore
/*      */       //   186: dup
/*      */       //   187: iconst_2
/*      */       //   188: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   191: aastore
/*      */       //   192: dup
/*      */       //   193: iconst_3
/*      */       //   194: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.STRONG_EXPIRABLE_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   197: aastore
/*      */       //   198: aastore
/*      */       //   199: dup
/*      */       //   200: iconst_1
/*      */       //   201: iconst_0
/*      */       //   202: anewarray com/google/common/collect/MapMakerInternalMap$EntryFactory
/*      */       //   205: aastore
/*      */       //   206: dup
/*      */       //   207: iconst_2
/*      */       //   208: iconst_4
/*      */       //   209: anewarray com/google/common/collect/MapMakerInternalMap$EntryFactory
/*      */       //   212: dup
/*      */       //   213: iconst_0
/*      */       //   214: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   217: aastore
/*      */       //   218: dup
/*      */       //   219: iconst_1
/*      */       //   220: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EXPIRABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   223: aastore
/*      */       //   224: dup
/*      */       //   225: iconst_2
/*      */       //   226: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   229: aastore
/*      */       //   230: dup
/*      */       //   231: iconst_3
/*      */       //   232: getstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.WEAK_EXPIRABLE_EVICTABLE : Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   235: aastore
/*      */       //   236: aastore
/*      */       //   237: putstatic com/google/common/collect/MapMakerInternalMap$EntryFactory.factories : [[Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
/*      */       //   240: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #352	-> 0
/*      */       //   #359	-> 13
/*      */       //   #374	-> 26
/*      */       //   #389	-> 39
/*      */       //   #406	-> 52
/*      */       //   #413	-> 65
/*      */       //   #428	-> 78
/*      */       //   #443	-> 92
/*      */       //   #351	-> 106
/*  720 */       //   #470	-> 164 } static EntryFactory getFactory(MapMakerInternalMap.Strength keyStrength, boolean expireAfterWrite, boolean evictsBySize) { int flags = (expireAfterWrite ? 1 : 0) | (evictsBySize ? 2 : 0); return factories[keyStrength.ordinal()][flags]; } @GuardedBy("Segment.this") <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> segment, MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) { return newEntry(segment, original.getKey(), original.getHash(), newNext); } @GuardedBy("Segment.this") <K, V> void copyExpirableEntry(MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newEntry) { newEntry.setExpirationTime(original.getExpirationTime()); MapMakerInternalMap.connectExpirables(original.getPreviousExpirable(), newEntry); MapMakerInternalMap.connectExpirables(newEntry, original.getNextExpirable()); MapMakerInternalMap.nullifyExpirable(original); } STRONG, STRONG_EXPIRABLE, STRONG_EVICTABLE, STRONG_EXPIRABLE_EVICTABLE, WEAK, WEAK_EXPIRABLE, WEAK_EVICTABLE, WEAK_EXPIRABLE_EVICTABLE; @GuardedBy("Segment.this") <K, V> void copyEvictableEntry(MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newEntry) { MapMakerInternalMap.connectEvictables(original.getPreviousEvictable(), newEntry); MapMakerInternalMap.connectEvictables(newEntry, original.getNextEvictable()); MapMakerInternalMap.nullifyEvictable(original); } abstract <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> param1Segment, K param1K, int param1Int, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> param1ReferenceEntry); } static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>() { public Object get() { return null; } public MapMakerInternalMap.ReferenceEntry<Object, Object> getEntry() { return null; } public ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @Nullable Object value, MapMakerInternalMap.ReferenceEntry<Object, Object> entry) { return this; } public boolean isComputingReference() { return false; } public Object waitForValue() { return null; } public void clear(ValueReference<Object, Object> newValue) {} }; static <K, V> ValueReference<K, V> unset() { return UNSET; } private enum NullEntry implements ReferenceEntry<Object, Object> { INSTANCE;
/*      */ 
/*      */ 
/*      */     
/*  724 */     public MapMakerInternalMap.ValueReference<Object, Object> getValueReference() { return null; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(MapMakerInternalMap.ValueReference<Object, Object> valueReference) {}
/*      */ 
/*      */ 
/*      */     
/*  732 */     public MapMakerInternalMap.ReferenceEntry<Object, Object> getNext() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  737 */     public int getHash() { return 0; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  742 */     public Object getKey() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  747 */     public long getExpirationTime() { return 0L; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setExpirationTime(long time) {}
/*      */ 
/*      */ 
/*      */     
/*  755 */     public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextExpirable() { return this; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */ 
/*      */     
/*  763 */     public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousExpirable() { return this; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> previous) {}
/*      */ 
/*      */ 
/*      */     
/*  771 */     public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextEvictable() { return this; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */ 
/*      */     
/*  779 */     public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousEvictable() { return this; }
/*      */ 
/*      */     
/*      */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> previous) {} }
/*      */ 
/*      */   
/*      */   static abstract class AbstractReferenceEntry<K, V>
/*      */     extends Object
/*      */     implements ReferenceEntry<K, V>
/*      */   {
/*  789 */     public MapMakerInternalMap.ValueReference<K, V> getValueReference() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  794 */     public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  799 */     public MapMakerInternalMap.ReferenceEntry<K, V> getNext() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  804 */     public int getHash() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  809 */     public K getKey() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  814 */     public long getExpirationTime() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  819 */     public void setExpirationTime(long time) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  824 */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  829 */     public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  834 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  839 */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  844 */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  849 */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  854 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  859 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  865 */   static <K, V> ReferenceEntry<K, V> nullEntry() { return NullEntry.INSTANCE; }
/*      */ 
/*      */   
/*  868 */   static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue<Object>()
/*      */     {
/*      */       public boolean offer(Object o) {
/*  871 */         return true;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  876 */       public Object peek() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  881 */       public Object poll() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  886 */       public int size() { return 0; }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  891 */       public Iterator<Object> iterator() { return Iterators.emptyIterator(); }
/*      */     };
/*      */ 
/*      */   
/*      */   Set<K> keySet;
/*      */   Collection<V> values;
/*      */   Set<Map.Entry<K, V>> entrySet;
/*      */   private static final long serialVersionUID = 5L;
/*      */   
/*  900 */   static <E> Queue<E> discardingQueue() { return DISCARDING_QUEUE; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class StrongEntry<K, V>
/*      */     extends Object
/*      */     implements ReferenceEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.ReferenceEntry<K, V> next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/*  986 */       this.valueReference = MapMakerInternalMap.unset();
/*      */       this.key = key;
/*      */       this.hash = hash;
/*      */       this.next = next;
/*  990 */     } public K getKey() { return (K)this.key; } public long getExpirationTime() { throw new UnsupportedOperationException(); } public void setExpirationTime(long time) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { throw new UnsupportedOperationException(); } public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { throw new UnsupportedOperationException(); } public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { throw new UnsupportedOperationException(); } public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { throw new UnsupportedOperationException(); } public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
/*  995 */       MapMakerInternalMap.ValueReference<K, V> previous = this.valueReference;
/*  996 */       this.valueReference = valueReference;
/*  997 */       previous.clear(valueReference);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1002 */     public int getHash() { return this.hash; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1007 */     public MapMakerInternalMap.ReferenceEntry<K, V> getNext() { return this.next; } }
/*      */   static final class StrongExpirableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
/*      */     
/*      */     StrongExpirableEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1014 */       super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1019 */       this.time = Float.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1031 */       this.nextExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1044 */       this.previousExpirable = MapMakerInternalMap.nullEntry();
/*      */     } public long getExpirationTime() { return this.time; }
/*      */     public void setExpirationTime(long time) { this.time = time; }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return this.nextExpirable; }
/*      */     public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextExpirable = next; }
/* 1049 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return this.previousExpirable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1054 */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousExpirable = previous; } }
/*      */   static final class StrongEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */     
/*      */     StrongEvictableEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1061 */       super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1066 */       this.nextEvictable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1079 */       this.previousEvictable = MapMakerInternalMap.nullEntry();
/*      */     }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return this.nextEvictable; }
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextEvictable = next; }
/*      */     
/* 1084 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return this.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1089 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousEvictable = previous; } }
/*      */   static final class StrongExpirableEvictableEntry<K, V> extends StrongEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */     StrongExpirableEvictableEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1096 */       super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1101 */       this.time = Float.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1113 */       this.nextExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1126 */       this.previousExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1141 */       this.nextEvictable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1154 */       this.previousEvictable = MapMakerInternalMap.nullEntry();
/*      */     } public long getExpirationTime() { return this.time; } public void setExpirationTime(long time) { this.time = time; } public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return this.nextExpirable; } public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextExpirable = next; } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return this.previousExpirable; }
/*      */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousExpirable = previous; }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return this.nextEvictable; }
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextEvictable = next; }
/* 1159 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return this.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1164 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousEvictable = previous; } }
/*      */ 
/*      */   
/*      */   static class SoftEntry<K, V>
/*      */     extends SoftReference<K> implements ReferenceEntry<K, V> {
/*      */     final int hash;
/*      */     final MapMakerInternalMap.ReferenceEntry<K, V> next;
/*      */     
/*      */     SoftEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1173 */       super(key, queue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1240 */       this.valueReference = MapMakerInternalMap.unset();
/*      */       this.hash = hash;
/*      */       this.next = next;
/*      */     } public K getKey() { return (K)get(); } public long getExpirationTime() { throw new UnsupportedOperationException(); } public void setExpirationTime(long time) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { throw new UnsupportedOperationException(); } public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { throw new UnsupportedOperationException(); } public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { throw new UnsupportedOperationException(); } public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { throw new UnsupportedOperationException(); } public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/* 1244 */     public MapMakerInternalMap.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 1249 */       MapMakerInternalMap.ValueReference<K, V> previous = this.valueReference;
/* 1250 */       this.valueReference = valueReference;
/* 1251 */       previous.clear(valueReference);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1256 */     public int getHash() { return this.hash; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1261 */     public MapMakerInternalMap.ReferenceEntry<K, V> getNext() { return this.next; } }
/*      */   
/*      */   static final class SoftExpirableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
/*      */     
/*      */     SoftExpirableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1269 */       super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1274 */       this.time = Float.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1286 */       this.nextExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1299 */       this.previousExpirable = MapMakerInternalMap.nullEntry();
/*      */     } public long getExpirationTime() { return this.time; }
/*      */     public void setExpirationTime(long time) { this.time = time; }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return this.nextExpirable; }
/*      */     public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextExpirable = next; }
/* 1304 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return this.previousExpirable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1309 */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousExpirable = previous; } }
/*      */   
/*      */   static final class SoftEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */     
/*      */     SoftEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1317 */       super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1322 */       this.nextEvictable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1335 */       this.previousEvictable = MapMakerInternalMap.nullEntry();
/*      */     }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return this.nextEvictable; }
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextEvictable = next; }
/*      */     
/* 1340 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return this.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1345 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousEvictable = previous; } }
/*      */   static final class SoftExpirableEvictableEntry<K, V> extends SoftEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */     
/*      */     SoftExpirableEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1353 */       super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1358 */       this.time = Float.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1370 */       this.nextExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1383 */       this.previousExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1398 */       this.nextEvictable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1411 */       this.previousEvictable = MapMakerInternalMap.nullEntry();
/*      */     } public long getExpirationTime() { return this.time; } public void setExpirationTime(long time) { this.time = time; } public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return this.nextExpirable; } public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextExpirable = next; } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return this.previousExpirable; }
/*      */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousExpirable = previous; }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return this.nextEvictable; }
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextEvictable = next; }
/* 1416 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return this.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1421 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousEvictable = previous; } }
/*      */ 
/*      */   
/*      */   static class WeakEntry<K, V>
/*      */     extends WeakReference<K> implements ReferenceEntry<K, V> {
/*      */     final int hash;
/*      */     final MapMakerInternalMap.ReferenceEntry<K, V> next;
/*      */     
/*      */     WeakEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1430 */       super(key, queue);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1498 */       this.valueReference = MapMakerInternalMap.unset();
/*      */       this.hash = hash;
/*      */       this.next = next;
/*      */     } public K getKey() { return (K)get(); } public long getExpirationTime() { throw new UnsupportedOperationException(); } public void setExpirationTime(long time) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { throw new UnsupportedOperationException(); } public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { throw new UnsupportedOperationException(); } public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { throw new UnsupportedOperationException(); } public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { throw new UnsupportedOperationException(); } public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/* 1502 */     public MapMakerInternalMap.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 1507 */       MapMakerInternalMap.ValueReference<K, V> previous = this.valueReference;
/* 1508 */       this.valueReference = valueReference;
/* 1509 */       previous.clear(valueReference);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1514 */     public int getHash() { return this.hash; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1519 */     public MapMakerInternalMap.ReferenceEntry<K, V> getNext() { return this.next; } }
/*      */   
/*      */   static final class WeakExpirableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
/*      */     
/*      */     WeakExpirableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1527 */       super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1532 */       this.time = Float.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1544 */       this.nextExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1557 */       this.previousExpirable = MapMakerInternalMap.nullEntry();
/*      */     } public long getExpirationTime() { return this.time; }
/*      */     public void setExpirationTime(long time) { this.time = time; }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return this.nextExpirable; }
/*      */     public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextExpirable = next; }
/* 1562 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return this.previousExpirable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1567 */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousExpirable = previous; } }
/*      */   
/*      */   static final class WeakEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */     
/*      */     WeakEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1575 */       super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1580 */       this.nextEvictable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1593 */       this.previousEvictable = MapMakerInternalMap.nullEntry();
/*      */     }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return this.nextEvictable; }
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextEvictable = next; }
/*      */     
/* 1598 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return this.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1603 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousEvictable = previous; } }
/*      */   static final class WeakExpirableEvictableEntry<K, V> extends WeakEntry<K, V> implements ReferenceEntry<K, V> { @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable; @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */     
/*      */     WeakExpirableEvictableEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) {
/* 1611 */       super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1616 */       this.time = Float.MAX_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1628 */       this.nextExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1641 */       this.previousExpirable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1656 */       this.nextEvictable = MapMakerInternalMap.nullEntry();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1669 */       this.previousEvictable = MapMakerInternalMap.nullEntry();
/*      */     } public long getExpirationTime() { return this.time; } public void setExpirationTime(long time) { this.time = time; } public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return this.nextExpirable; } public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextExpirable = next; } public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return this.previousExpirable; }
/*      */     public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousExpirable = previous; }
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return this.nextEvictable; }
/*      */     public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { this.nextEvictable = next; }
/* 1674 */     public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return this.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1679 */     public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { this.previousEvictable = previous; } }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class WeakValueReference<K, V>
/*      */     extends WeakReference<V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final MapMakerInternalMap.ReferenceEntry<K, V> entry;
/*      */ 
/*      */     
/*      */     WeakValueReference(ReferenceQueue<V> queue, V referent, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 1691 */       super(referent, queue);
/* 1692 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1697 */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() { return this.entry; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1702 */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) { clear(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1708 */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) { return new WeakValueReference(queue, value, entry); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1713 */     public boolean isComputingReference() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1718 */     public V waitForValue() { return (V)get(); }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class SoftValueReference<K, V>
/*      */     extends SoftReference<V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final MapMakerInternalMap.ReferenceEntry<K, V> entry;
/*      */ 
/*      */     
/*      */     SoftValueReference(ReferenceQueue<V> queue, V referent, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 1730 */       super(referent, queue);
/* 1731 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1736 */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() { return this.entry; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1741 */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) { clear(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1747 */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) { return new SoftValueReference(queue, value, entry); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1752 */     public boolean isComputingReference() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1757 */     public V waitForValue() { return (V)get(); }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class StrongValueReference<K, V>
/*      */     extends Object
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final V referent;
/*      */ 
/*      */     
/* 1768 */     StrongValueReference(V referent) { this.referent = referent; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1773 */     public V get() { return (V)this.referent; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1778 */     public MapMakerInternalMap.ReferenceEntry<K, V> getEntry() { return null; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1784 */     public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, MapMakerInternalMap.ReferenceEntry<K, V> entry) { return this; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1789 */     public boolean isComputingReference() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1794 */     public V waitForValue() { return (V)get(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear(MapMakerInternalMap.ValueReference<K, V> newValue) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int rehash(int h) {
/* 1813 */     h += (h << 15 ^ 0xFFFFCD7D);
/* 1814 */     h ^= h >>> 10;
/* 1815 */     h += (h << 3);
/* 1816 */     h ^= h >>> 6;
/* 1817 */     h += (h << 2) + (h << 14);
/* 1818 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   @VisibleForTesting
/* 1827 */   ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) { return segmentFor(hash).newEntry(key, hash, next); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/* 1836 */     int hash = original.getHash();
/* 1837 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   @VisibleForTesting
/*      */   ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value) {
/* 1846 */     int hash = entry.getHash();
/* 1847 */     return this.valueStrength.referenceValue(segmentFor(hash), entry, value);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/* 1851 */     int h = this.keyEquivalence.hash(key);
/* 1852 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(ValueReference<K, V> valueReference) {
/* 1856 */     ReferenceEntry<K, V> entry = valueReference.getEntry();
/* 1857 */     int hash = entry.getHash();
/* 1858 */     segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(ReferenceEntry<K, V> entry) {
/* 1862 */     int hash = entry.getHash();
/* 1863 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/* 1872 */   boolean isLive(ReferenceEntry<K, V> entry) { return (segmentFor(entry.getHash()).getLiveValue(entry) != null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1883 */   Segment<K, V> segmentFor(int hash) { return this.segments[hash >>> this.segmentShift & this.segmentMask]; }
/*      */ 
/*      */ 
/*      */   
/* 1887 */   Segment<K, V> createSegment(int initialCapacity, int maxSegmentSize) { return new Segment(this, initialCapacity, maxSegmentSize); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   V getLiveValue(ReferenceEntry<K, V> entry) {
/* 1896 */     if (entry.getKey() == null) {
/* 1897 */       return null;
/*      */     }
/* 1899 */     V value = (V)entry.getValueReference().get();
/* 1900 */     if (value == null) {
/* 1901 */       return null;
/*      */     }
/*      */     
/* 1904 */     if (expires() && isExpired(entry)) {
/* 1905 */       return null;
/*      */     }
/* 1907 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1916 */   boolean isExpired(ReferenceEntry<K, V> entry) { return isExpired(entry, this.ticker.read()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1924 */   boolean isExpired(ReferenceEntry<K, V> entry, long now) { return (now - entry.getExpirationTime() > 0L); }
/*      */ 
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   static <K, V> void connectExpirables(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1929 */     previous.setNextExpirable(next);
/* 1930 */     next.setPreviousExpirable(previous);
/*      */   }
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   static <K, V> void nullifyExpirable(ReferenceEntry<K, V> nulled) {
/* 1935 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1936 */     nulled.setNextExpirable(nullEntry);
/* 1937 */     nulled.setPreviousExpirable(nullEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void processPendingNotifications() {
/*      */     MapMaker.RemovalNotification<K, V> notification;
/* 1949 */     while ((notification = (MapMaker.RemovalNotification)this.removalNotificationQueue.poll()) != null) {
/*      */       try {
/* 1951 */         this.removalListener.onRemoval(notification);
/* 1952 */       } catch (Exception e) {
/* 1953 */         logger.log(Level.WARNING, "Exception thrown by removal listener", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   static <K, V> void connectEvictables(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1961 */     previous.setNextEvictable(next);
/* 1962 */     next.setPreviousEvictable(previous);
/*      */   }
/*      */   
/*      */   @GuardedBy("Segment.this")
/*      */   static <K, V> void nullifyEvictable(ReferenceEntry<K, V> nulled) {
/* 1967 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1968 */     nulled.setNextEvictable(nullEntry);
/* 1969 */     nulled.setPreviousEvictable(nullEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1974 */   final Segment<K, V>[] newSegmentArray(int ssize) { return new Segment[ssize]; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class Segment<K, V>
/*      */     extends ReentrantLock
/*      */   {
/*      */     final MapMakerInternalMap<K, V> map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int maxSegmentSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceQueue<K> keyReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceQueue<V> valueReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> recencyQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final AtomicInteger readCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> evictionQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> expirationQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Segment(MapMakerInternalMap<K, V> map, int initialCapacity, int maxSegmentSize) {
/* 2074 */       this.readCount = new AtomicInteger();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2091 */       this.map = map;
/* 2092 */       this.maxSegmentSize = maxSegmentSize;
/* 2093 */       initTable(newEntryArray(initialCapacity));
/*      */       
/* 2095 */       this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue() : null;
/*      */ 
/*      */       
/* 2098 */       this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue() : null;
/*      */ 
/*      */       
/* 2101 */       this.recencyQueue = (map.evictsBySize() || map.expiresAfterAccess()) ? new ConcurrentLinkedQueue() : MapMakerInternalMap.discardingQueue();
/*      */ 
/*      */ 
/*      */       
/* 2105 */       this.evictionQueue = map.evictsBySize() ? new MapMakerInternalMap.EvictionQueue() : MapMakerInternalMap.discardingQueue();
/*      */ 
/*      */ 
/*      */       
/* 2109 */       this.expirationQueue = map.expires() ? new MapMakerInternalMap.ExpirationQueue() : MapMakerInternalMap.discardingQueue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2115 */     AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newEntryArray(int size) { return new AtomicReferenceArray(size); }
/*      */ 
/*      */     
/*      */     void initTable(AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newTable) {
/* 2119 */       this.threshold = newTable.length() * 3 / 4;
/* 2120 */       if (this.threshold == this.maxSegmentSize)
/*      */       {
/* 2122 */         this.threshold++;
/*      */       }
/* 2124 */       this.table = newTable;
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/* 2129 */     MapMakerInternalMap.ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable MapMakerInternalMap.ReferenceEntry<K, V> next) { return this.map.entryFactory.newEntry(this, key, hash, next); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.ReferenceEntry<K, V> original, MapMakerInternalMap.ReferenceEntry<K, V> newNext) {
/* 2138 */       if (original.getKey() == null)
/*      */       {
/* 2140 */         return null;
/*      */       }
/*      */       
/* 2143 */       MapMakerInternalMap.ValueReference<K, V> valueReference = original.getValueReference();
/* 2144 */       V value = (V)valueReference.get();
/* 2145 */       if (value == null && !valueReference.isComputingReference())
/*      */       {
/* 2147 */         return null;
/*      */       }
/*      */       
/* 2150 */       MapMakerInternalMap.ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
/* 2151 */       newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
/* 2152 */       return newEntry;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void setValue(MapMakerInternalMap.ReferenceEntry<K, V> entry, V value) {
/* 2160 */       MapMakerInternalMap.ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value);
/* 2161 */       entry.setValueReference(valueReference);
/* 2162 */       recordWrite(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 2171 */       if (tryLock()) {
/*      */         try {
/* 2173 */           drainReferenceQueues();
/*      */         } finally {
/* 2175 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void drainReferenceQueues() {
/* 2186 */       if (this.map.usesKeyReferences()) {
/* 2187 */         drainKeyReferenceQueue();
/*      */       }
/* 2189 */       if (this.map.usesValueReferences()) {
/* 2190 */         drainValueReferenceQueue();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void drainKeyReferenceQueue() {
/* 2197 */       int i = 0; Reference<? extends K> ref;
/* 2198 */       while ((ref = this.keyReferenceQueue.poll()) != null) {
/*      */         
/* 2200 */         MapMakerInternalMap.ReferenceEntry<K, V> entry = (MapMakerInternalMap.ReferenceEntry)ref;
/* 2201 */         this.map.reclaimKey(entry);
/* 2202 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void drainValueReferenceQueue() {
/* 2211 */       int i = 0; Reference<? extends V> ref;
/* 2212 */       while ((ref = this.valueReferenceQueue.poll()) != null) {
/*      */         
/* 2214 */         MapMakerInternalMap.ValueReference<K, V> valueReference = (MapMakerInternalMap.ValueReference)ref;
/* 2215 */         this.map.reclaimValue(valueReference);
/* 2216 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void clearReferenceQueues() {
/* 2226 */       if (this.map.usesKeyReferences()) {
/* 2227 */         clearKeyReferenceQueue();
/*      */       }
/* 2229 */       if (this.map.usesValueReferences()) {
/* 2230 */         clearValueReferenceQueue();
/*      */       }
/*      */     }
/*      */     
/*      */     void clearKeyReferenceQueue() {
/* 2235 */       while (this.keyReferenceQueue.poll() != null);
/*      */     }
/*      */     
/*      */     void clearValueReferenceQueue() {
/* 2239 */       while (this.valueReferenceQueue.poll() != null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void recordRead(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 2252 */       if (this.map.expiresAfterAccess()) {
/* 2253 */         recordExpirationTime(entry, this.map.expireAfterAccessNanos);
/*      */       }
/* 2255 */       this.recencyQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void recordLockedRead(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 2267 */       this.evictionQueue.add(entry);
/* 2268 */       if (this.map.expiresAfterAccess()) {
/* 2269 */         recordExpirationTime(entry, this.map.expireAfterAccessNanos);
/* 2270 */         this.expirationQueue.add(entry);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void recordWrite(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 2281 */       drainRecencyQueue();
/* 2282 */       this.evictionQueue.add(entry);
/* 2283 */       if (this.map.expires()) {
/*      */ 
/*      */         
/* 2286 */         long expiration = this.map.expiresAfterAccess() ? this.map.expireAfterAccessNanos : this.map.expireAfterWriteNanos;
/*      */ 
/*      */         
/* 2289 */         recordExpirationTime(entry, expiration);
/* 2290 */         this.expirationQueue.add(entry);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void drainRecencyQueue() {
/*      */       MapMakerInternalMap.ReferenceEntry<K, V> e;
/* 2303 */       while ((e = (MapMakerInternalMap.ReferenceEntry)this.recencyQueue.poll()) != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2308 */         if (this.evictionQueue.contains(e)) {
/* 2309 */           this.evictionQueue.add(e);
/*      */         }
/* 2311 */         if (this.map.expiresAfterAccess() && this.expirationQueue.contains(e)) {
/* 2312 */           this.expirationQueue.add(e);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2321 */     void recordExpirationTime(MapMakerInternalMap.ReferenceEntry<K, V> entry, long expirationNanos) { entry.setExpirationTime(this.map.ticker.read() + expirationNanos); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryExpireEntries() {
/* 2328 */       if (tryLock()) {
/*      */         try {
/* 2330 */           expireEntries();
/*      */         } finally {
/* 2332 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void expireEntries() {
/* 2340 */       drainRecencyQueue();
/*      */       
/* 2342 */       if (this.expirationQueue.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 2347 */       long now = this.map.ticker.read();
/*      */       MapMakerInternalMap.ReferenceEntry<K, V> e;
/* 2349 */       while ((e = (MapMakerInternalMap.ReferenceEntry)this.expirationQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2350 */         if (!removeEntry(e, e.getHash(), MapMaker.RemovalCause.EXPIRED)) {
/* 2351 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2359 */     void enqueueNotification(MapMakerInternalMap.ReferenceEntry<K, V> entry, MapMaker.RemovalCause cause) { enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), cause); }
/*      */ 
/*      */     
/*      */     void enqueueNotification(@Nullable K key, int hash, @Nullable V value, MapMaker.RemovalCause cause) {
/* 2363 */       if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
/* 2364 */         MapMaker.RemovalNotification<K, V> notification = new MapMaker.RemovalNotification<K, V>(key, value, cause);
/* 2365 */         this.map.removalNotificationQueue.offer(notification);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     boolean evictEntries() {
/* 2377 */       if (this.map.evictsBySize() && this.count >= this.maxSegmentSize) {
/* 2378 */         drainRecencyQueue();
/*      */         
/* 2380 */         MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)this.evictionQueue.remove();
/* 2381 */         if (!removeEntry(e, e.getHash(), MapMaker.RemovalCause.SIZE)) {
/* 2382 */           throw new AssertionError();
/*      */         }
/* 2384 */         return true;
/*      */       } 
/* 2386 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> getFirst(int hash) {
/* 2394 */       AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2395 */       return (MapMakerInternalMap.ReferenceEntry)table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> getEntry(Object key, int hash) {
/* 2401 */       if (this.count != 0) {
/* 2402 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
/* 2403 */           if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */             
/* 2407 */             K entryKey = (K)e.getKey();
/* 2408 */             if (entryKey == null) {
/* 2409 */               tryDrainReferenceQueues();
/*      */ 
/*      */             
/*      */             }
/* 2413 */             else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 2414 */               return e;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 2419 */       return null;
/*      */     }
/*      */     
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> getLiveEntry(Object key, int hash) {
/* 2423 */       MapMakerInternalMap.ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2424 */       if (e == null)
/* 2425 */         return null; 
/* 2426 */       if (this.map.expires() && this.map.isExpired(e)) {
/* 2427 */         tryExpireEntries();
/* 2428 */         return null;
/*      */       } 
/* 2430 */       return e;
/*      */     }
/*      */     
/*      */     V get(Object key, int hash) {
/*      */       
/* 2435 */       try { MapMakerInternalMap.ReferenceEntry<K, V> e = getLiveEntry(key, hash);
/* 2436 */         if (e == null)
/* 2437 */         { object = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2448 */           return (V)object; }  V value = (V)e.getValueReference().get(); if (value != null) { recordRead(e); } else { tryDrainReferenceQueues(); }  return value; } finally { postReadCleanup(); }
/*      */     
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 2454 */         if (this.count != 0) {
/* 2455 */           MapMakerInternalMap.ReferenceEntry<K, V> e = getLiveEntry(key, hash);
/* 2456 */           if (e == null) {
/* 2457 */             return false;
/*      */           }
/* 2459 */           return (e.getValueReference().get() != null);
/*      */         } 
/*      */         
/* 2462 */         return false;
/*      */       } finally {
/* 2464 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value) {
/*      */       try {
/* 2475 */         if (this.count != 0) {
/* 2476 */           AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2477 */           int length = table.length();
/* 2478 */           for (int i = 0; i < length; i++) {
/* 2479 */             for (MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)table.get(i); e != null; e = e.getNext()) {
/* 2480 */               V entryValue = (V)getLiveValue(e);
/* 2481 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 2484 */                 if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 2485 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 2491 */         return false;
/*      */       } finally {
/* 2493 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 2498 */       lock();
/*      */       
/* 2500 */       try { preWriteCleanup();
/*      */         
/* 2502 */         int newCount = this.count + 1;
/* 2503 */         if (newCount > this.threshold) {
/* 2504 */           expand();
/* 2505 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 2508 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2509 */         int index = hash & table.length() - 1;
/* 2510 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */ 
/*      */         
/* 2513 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext())
/* 2514 */         { K entryKey = (K)e.getKey();
/* 2515 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey))
/*      */           
/*      */           { 
/*      */             
/* 2519 */             MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 2520 */             V entryValue = (V)valueReference.get();
/*      */             
/* 2522 */             if (entryValue == null)
/* 2523 */             { this.modCount++;
/* 2524 */               setValue(e, value);
/* 2525 */               if (!valueReference.isComputingReference()) {
/* 2526 */                 enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
/* 2527 */                 newCount = this.count;
/* 2528 */               } else if (evictEntries()) {
/* 2529 */                 newCount = this.count + 1;
/*      */               } 
/* 2531 */               this.count = newCount;
/* 2532 */               object1 = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2561 */               return (V)object1; }  if (onlyIfAbsent) { recordLockedRead(e); return entryValue; }  this.modCount++; enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED); setValue(e, value); return entryValue; }  }  this.modCount++; MapMakerInternalMap.ReferenceEntry<K, V> newEntry = newEntry(key, hash, first); setValue(newEntry, value); table.set(index, newEntry); if (evictEntries()) newCount = this.count + 1;  this.count = newCount; object = null; return (V)object; } finally { unlock(); postWriteCleanup(); }
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     void expand() {
/* 2570 */       AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> oldTable = this.table;
/* 2571 */       int oldCapacity = oldTable.length();
/* 2572 */       if (oldCapacity >= 1073741824) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2586 */       int newCount = this.count;
/* 2587 */       AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
/* 2588 */       this.threshold = newTable.length() * 3 / 4;
/* 2589 */       int newMask = newTable.length() - 1;
/* 2590 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 2593 */         MapMakerInternalMap.ReferenceEntry<K, V> head = (MapMakerInternalMap.ReferenceEntry)oldTable.get(oldIndex);
/*      */         
/* 2595 */         if (head != null) {
/* 2596 */           MapMakerInternalMap.ReferenceEntry<K, V> next = head.getNext();
/* 2597 */           int headIndex = head.getHash() & newMask;
/*      */ 
/*      */           
/* 2600 */           if (next == null) {
/* 2601 */             newTable.set(headIndex, head);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 2606 */             MapMakerInternalMap.ReferenceEntry<K, V> tail = head;
/* 2607 */             int tailIndex = headIndex;
/* 2608 */             for (MapMakerInternalMap.ReferenceEntry<K, V> e = next; e != null; e = e.getNext()) {
/* 2609 */               int newIndex = e.getHash() & newMask;
/* 2610 */               if (newIndex != tailIndex) {
/*      */                 
/* 2612 */                 tailIndex = newIndex;
/* 2613 */                 tail = e;
/*      */               } 
/*      */             } 
/* 2616 */             newTable.set(tailIndex, tail);
/*      */ 
/*      */             
/* 2619 */             for (MapMakerInternalMap.ReferenceEntry<K, V> e = head; e != tail; e = e.getNext()) {
/* 2620 */               int newIndex = e.getHash() & newMask;
/* 2621 */               MapMakerInternalMap.ReferenceEntry<K, V> newNext = (MapMakerInternalMap.ReferenceEntry)newTable.get(newIndex);
/* 2622 */               MapMakerInternalMap.ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
/* 2623 */               if (newFirst != null) {
/* 2624 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 2626 */                 removeCollectedEntry(e);
/* 2627 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 2633 */       this.table = newTable;
/* 2634 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 2638 */       lock();
/*      */       try {
/* 2640 */         preWriteCleanup();
/*      */         
/* 2642 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2643 */         int index = hash & table.length() - 1;
/* 2644 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2646 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2647 */           K entryKey = (K)e.getKey();
/* 2648 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
/*      */ 
/*      */ 
/*      */             
/* 2652 */             MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 2653 */             V entryValue = (V)valueReference.get();
/* 2654 */             if (entryValue == null) {
/* 2655 */               if (isCollected(valueReference)) {
/* 2656 */                 int newCount = this.count - 1;
/* 2657 */                 this.modCount++;
/* 2658 */                 enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
/* 2659 */                 MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 2660 */                 newCount = this.count - 1;
/* 2661 */                 table.set(index, newFirst);
/* 2662 */                 this.count = newCount;
/*      */               } 
/* 2664 */               return false;
/*      */             } 
/*      */             
/* 2667 */             if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
/* 2668 */               this.modCount++;
/* 2669 */               enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED);
/* 2670 */               setValue(e, newValue);
/* 2671 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 2675 */             recordLockedRead(e);
/* 2676 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2681 */         return false;
/*      */       } finally {
/* 2683 */         unlock();
/* 2684 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/* 2689 */       lock();
/*      */       
/* 2691 */       try { preWriteCleanup();
/*      */         
/* 2693 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2694 */         int index = hash & table.length() - 1;
/* 2695 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2697 */         for (e = first; e != null; e = e.getNext())
/* 2698 */         { K entryKey = (K)e.getKey();
/* 2699 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey))
/*      */           
/*      */           { 
/*      */             
/* 2703 */             MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 2704 */             V entryValue = (V)valueReference.get();
/* 2705 */             if (entryValue == null)
/* 2706 */             { if (isCollected(valueReference)) {
/* 2707 */                 int newCount = this.count - 1;
/* 2708 */                 this.modCount++;
/* 2709 */                 enqueueNotification(entryKey, hash, entryValue, MapMaker.RemovalCause.COLLECTED);
/* 2710 */                 MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 2711 */                 newCount = this.count - 1;
/* 2712 */                 table.set(index, newFirst);
/* 2713 */                 this.count = newCount;
/*      */               } 
/* 2715 */               object = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2728 */               return (V)object; }  this.modCount++; enqueueNotification(key, hash, entryValue, MapMaker.RemovalCause.REPLACED); setValue(e, newValue); return entryValue; }  }  e = null; return (V)e; } finally { unlock(); postWriteCleanup(); }
/*      */     
/*      */     }
/*      */     
/*      */     V remove(Object key, int hash) {
/* 2733 */       lock();
/*      */       
/* 2735 */       try { preWriteCleanup();
/*      */         
/* 2737 */         int newCount = this.count - 1;
/* 2738 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2739 */         int index = hash & table.length() - 1;
/* 2740 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2742 */         for (e = first; e != null; e = e.getNext())
/* 2743 */         { K entryKey = (K)e.getKey();
/* 2744 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey))
/*      */           { MapMaker.RemovalCause cause;
/* 2746 */             MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 2747 */             V entryValue = (V)valueReference.get();
/*      */ 
/*      */             
/* 2750 */             if (entryValue != null)
/* 2751 */             { cause = MapMaker.RemovalCause.EXPLICIT; }
/* 2752 */             else if (isCollected(valueReference))
/* 2753 */             { cause = MapMaker.RemovalCause.COLLECTED; }
/*      */             else
/* 2755 */             { object = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2771 */               return (V)object; }  this.modCount++; enqueueNotification(entryKey, hash, entryValue, cause); MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e); newCount = this.count - 1; table.set(index, newFirst); this.count = newCount; return entryValue; }  }  e = null; return (V)e; } finally { unlock(); postWriteCleanup(); }
/*      */     
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 2776 */       lock();
/*      */       try {
/* 2778 */         preWriteCleanup();
/*      */         
/* 2780 */         int newCount = this.count - 1;
/* 2781 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2782 */         int index = hash & table.length() - 1;
/* 2783 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2785 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2786 */           K entryKey = (K)e.getKey();
/* 2787 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
/*      */             MapMaker.RemovalCause cause;
/* 2789 */             MapMakerInternalMap.ValueReference<K, V> valueReference = e.getValueReference();
/* 2790 */             V entryValue = (V)valueReference.get();
/*      */ 
/*      */             
/* 2793 */             if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 2794 */               cause = MapMaker.RemovalCause.EXPLICIT;
/* 2795 */             } else if (isCollected(valueReference)) {
/* 2796 */               cause = MapMaker.RemovalCause.COLLECTED;
/*      */             } else {
/* 2798 */               return false;
/*      */             } 
/*      */             
/* 2801 */             this.modCount++;
/* 2802 */             enqueueNotification(entryKey, hash, entryValue, cause);
/* 2803 */             MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 2804 */             newCount = this.count - 1;
/* 2805 */             table.set(index, newFirst);
/* 2806 */             this.count = newCount;
/* 2807 */             return (cause == MapMaker.RemovalCause.EXPLICIT);
/*      */           } 
/*      */         } 
/*      */         
/* 2811 */         return false;
/*      */       } finally {
/* 2813 */         unlock();
/* 2814 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 2819 */       if (this.count != 0) {
/* 2820 */         lock();
/*      */         try {
/* 2822 */           AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2823 */           if (this.map.removalNotificationQueue != MapMakerInternalMap.DISCARDING_QUEUE) {
/* 2824 */             for (int i = 0; i < table.length(); i++) {
/* 2825 */               for (MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)table.get(i); e != null; e = e.getNext()) {
/*      */                 
/* 2827 */                 if (!e.getValueReference().isComputingReference()) {
/* 2828 */                   enqueueNotification(e, MapMaker.RemovalCause.EXPLICIT);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/* 2833 */           for (int i = 0; i < table.length(); i++) {
/* 2834 */             table.set(i, null);
/*      */           }
/* 2836 */           clearReferenceQueues();
/* 2837 */           this.evictionQueue.clear();
/* 2838 */           this.expirationQueue.clear();
/* 2839 */           this.readCount.set(0);
/*      */           
/* 2841 */           this.modCount++;
/* 2842 */           this.count = 0;
/*      */         } finally {
/* 2844 */           unlock();
/* 2845 */           postWriteCleanup();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> removeFromChain(MapMakerInternalMap.ReferenceEntry<K, V> first, MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 2864 */       this.evictionQueue.remove(entry);
/* 2865 */       this.expirationQueue.remove(entry);
/*      */       
/* 2867 */       int newCount = this.count;
/* 2868 */       MapMakerInternalMap.ReferenceEntry<K, V> newFirst = entry.getNext();
/* 2869 */       for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
/* 2870 */         MapMakerInternalMap.ReferenceEntry<K, V> next = copyEntry(e, newFirst);
/* 2871 */         if (next != null) {
/* 2872 */           newFirst = next;
/*      */         } else {
/* 2874 */           removeCollectedEntry(e);
/* 2875 */           newCount--;
/*      */         } 
/*      */       } 
/* 2878 */       this.count = newCount;
/* 2879 */       return newFirst;
/*      */     }
/*      */     
/*      */     void removeCollectedEntry(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 2883 */       enqueueNotification(entry, MapMaker.RemovalCause.COLLECTED);
/* 2884 */       this.evictionQueue.remove(entry);
/* 2885 */       this.expirationQueue.remove(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean reclaimKey(MapMakerInternalMap.ReferenceEntry<K, V> entry, int hash) {
/* 2892 */       lock();
/*      */       try {
/* 2894 */         int newCount = this.count - 1;
/* 2895 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2896 */         int index = hash & table.length() - 1;
/* 2897 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2899 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2900 */           if (e == entry) {
/* 2901 */             this.modCount++;
/* 2902 */             enqueueNotification(e.getKey(), hash, e.getValueReference().get(), MapMaker.RemovalCause.COLLECTED);
/*      */             
/* 2904 */             MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 2905 */             newCount = this.count - 1;
/* 2906 */             table.set(index, newFirst);
/* 2907 */             this.count = newCount;
/* 2908 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 2912 */         return false;
/*      */       } finally {
/* 2914 */         unlock();
/* 2915 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean reclaimValue(K key, int hash, MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 2923 */       lock();
/*      */       try {
/* 2925 */         int newCount = this.count - 1;
/* 2926 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2927 */         int index = hash & table.length() - 1;
/* 2928 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2930 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2931 */           K entryKey = (K)e.getKey();
/* 2932 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
/*      */             
/* 2934 */             MapMakerInternalMap.ValueReference<K, V> v = e.getValueReference();
/* 2935 */             if (v == valueReference) {
/* 2936 */               this.modCount++;
/* 2937 */               enqueueNotification(key, hash, valueReference.get(), MapMaker.RemovalCause.COLLECTED);
/* 2938 */               MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 2939 */               newCount = this.count - 1;
/* 2940 */               table.set(index, newFirst);
/* 2941 */               this.count = newCount;
/* 2942 */               return true;
/*      */             } 
/* 2944 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 2948 */         return false;
/*      */       } finally {
/* 2950 */         unlock();
/* 2951 */         if (!isHeldByCurrentThread()) {
/* 2952 */           postWriteCleanup();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean clearValue(K key, int hash, MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 2961 */       lock();
/*      */       try {
/* 2963 */         AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2964 */         int index = hash & table.length() - 1;
/* 2965 */         MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */         
/* 2967 */         for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2968 */           K entryKey = (K)e.getKey();
/* 2969 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
/*      */             
/* 2971 */             MapMakerInternalMap.ValueReference<K, V> v = e.getValueReference();
/* 2972 */             if (v == valueReference) {
/* 2973 */               MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 2974 */               table.set(index, newFirst);
/* 2975 */               return true;
/*      */             } 
/* 2977 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 2981 */         return false;
/*      */       } finally {
/* 2983 */         unlock();
/* 2984 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @GuardedBy("Segment.this")
/*      */     boolean removeEntry(MapMakerInternalMap.ReferenceEntry<K, V> entry, int hash, MapMaker.RemovalCause cause) {
/* 2990 */       int newCount = this.count - 1;
/* 2991 */       AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table = this.table;
/* 2992 */       int index = hash & table.length() - 1;
/* 2993 */       MapMakerInternalMap.ReferenceEntry<K, V> first = (MapMakerInternalMap.ReferenceEntry)table.get(index);
/*      */       
/* 2995 */       for (MapMakerInternalMap.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2996 */         if (e == entry) {
/* 2997 */           this.modCount++;
/* 2998 */           enqueueNotification(e.getKey(), hash, e.getValueReference().get(), cause);
/* 2999 */           MapMakerInternalMap.ReferenceEntry<K, V> newFirst = removeFromChain(first, e);
/* 3000 */           newCount = this.count - 1;
/* 3001 */           table.set(index, newFirst);
/* 3002 */           this.count = newCount;
/* 3003 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 3007 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isCollected(MapMakerInternalMap.ValueReference<K, V> valueReference) {
/* 3015 */       if (valueReference.isComputingReference()) {
/* 3016 */         return false;
/*      */       }
/* 3018 */       return (valueReference.get() == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValue(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 3026 */       if (entry.getKey() == null) {
/* 3027 */         tryDrainReferenceQueues();
/* 3028 */         return null;
/*      */       } 
/* 3030 */       V value = (V)entry.getValueReference().get();
/* 3031 */       if (value == null) {
/* 3032 */         tryDrainReferenceQueues();
/* 3033 */         return null;
/*      */       } 
/*      */       
/* 3036 */       if (this.map.expires() && this.map.isExpired(entry)) {
/* 3037 */         tryExpireEntries();
/* 3038 */         return null;
/*      */       } 
/* 3040 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 3049 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 3050 */         runCleanup();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("Segment.this")
/* 3062 */     void preWriteCleanup() { runLockedCleanup(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3069 */     void postWriteCleanup() { runUnlockedCleanup(); }
/*      */ 
/*      */     
/*      */     void runCleanup() {
/* 3073 */       runLockedCleanup();
/* 3074 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup() {
/* 3078 */       if (tryLock()) {
/*      */         try {
/* 3080 */           drainReferenceQueues();
/* 3081 */           expireEntries();
/* 3082 */           this.readCount.set(0);
/*      */         } finally {
/* 3084 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     void runUnlockedCleanup() {
/* 3091 */       if (!isHeldByCurrentThread()) {
/* 3092 */         this.map.processPendingNotifications();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class EvictionQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3112 */     final MapMakerInternalMap.ReferenceEntry<K, V> head = new MapMakerInternalMap.AbstractReferenceEntry<K, V>()
/*      */       {
/*      */         MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
/*      */         
/*      */         MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
/*      */         
/* 3118 */         public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable() { return super.nextEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3123 */         public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> next) { super.nextEvictable = next; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3130 */         public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable() { return super.previousEvictable; }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3135 */         public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { super.previousEvictable = previous; }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 3144 */       MapMakerInternalMap.connectEvictables(entry.getPreviousEvictable(), entry.getNextEvictable());
/*      */ 
/*      */       
/* 3147 */       MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), entry);
/* 3148 */       MapMakerInternalMap.connectEvictables(entry, this.head);
/*      */       
/* 3150 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> peek() {
/* 3155 */       MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextEvictable();
/* 3156 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> poll() {
/* 3161 */       MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextEvictable();
/* 3162 */       if (next == this.head) {
/* 3163 */         return null;
/*      */       }
/*      */       
/* 3166 */       remove(next);
/* 3167 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3173 */       MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)o;
/* 3174 */       MapMakerInternalMap.ReferenceEntry<K, V> previous = e.getPreviousEvictable();
/* 3175 */       MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextEvictable();
/* 3176 */       MapMakerInternalMap.connectEvictables(previous, next);
/* 3177 */       MapMakerInternalMap.nullifyEvictable(e);
/*      */       
/* 3179 */       return (next != MapMakerInternalMap.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3185 */       MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)o;
/* 3186 */       return (e.getNextEvictable() != MapMakerInternalMap.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3191 */     public boolean isEmpty() { return (this.head.getNextEvictable() == this.head); }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/* 3196 */       int size = 0;
/* 3197 */       for (MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextEvictable(); e != this.head; e = e.getNextEvictable()) {
/* 3198 */         size++;
/*      */       }
/* 3200 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3205 */       MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextEvictable();
/* 3206 */       while (e != this.head) {
/* 3207 */         MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextEvictable();
/* 3208 */         MapMakerInternalMap.nullifyEvictable(e);
/* 3209 */         e = next;
/*      */       } 
/*      */       
/* 3212 */       this.head.setNextEvictable(this.head);
/* 3213 */       this.head.setPreviousEvictable(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator() {
/* 3218 */       return new AbstractSequentialIterator<MapMakerInternalMap.ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
/* 3221 */             MapMakerInternalMap.ReferenceEntry<K, V> next = previous.getNextEvictable();
/* 3222 */             return (next == MapMakerInternalMap.EvictionQueue.this.head) ? null : next;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class ExpirationQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3240 */     final MapMakerInternalMap.ReferenceEntry<K, V> head = new MapMakerInternalMap.AbstractReferenceEntry<K, V>() {
/*      */         MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
/*      */         MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
/*      */         
/* 3244 */         public long getExpirationTime() { return Float.MAX_VALUE; }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public void setExpirationTime(long time) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3254 */         public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable() { return super.nextExpirable; }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3259 */         public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> next) { super.nextExpirable = next; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3266 */         public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable() { return super.previousExpirable; }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3271 */         public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> previous) { super.previousExpirable = previous; }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/* 3280 */       MapMakerInternalMap.connectExpirables(entry.getPreviousExpirable(), entry.getNextExpirable());
/*      */ 
/*      */       
/* 3283 */       MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), entry);
/* 3284 */       MapMakerInternalMap.connectExpirables(entry, this.head);
/*      */       
/* 3286 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> peek() {
/* 3291 */       MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextExpirable();
/* 3292 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.ReferenceEntry<K, V> poll() {
/* 3297 */       MapMakerInternalMap.ReferenceEntry<K, V> next = this.head.getNextExpirable();
/* 3298 */       if (next == this.head) {
/* 3299 */         return null;
/*      */       }
/*      */       
/* 3302 */       remove(next);
/* 3303 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3309 */       MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)o;
/* 3310 */       MapMakerInternalMap.ReferenceEntry<K, V> previous = e.getPreviousExpirable();
/* 3311 */       MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextExpirable();
/* 3312 */       MapMakerInternalMap.connectExpirables(previous, next);
/* 3313 */       MapMakerInternalMap.nullifyExpirable(e);
/*      */       
/* 3315 */       return (next != MapMakerInternalMap.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3321 */       MapMakerInternalMap.ReferenceEntry<K, V> e = (MapMakerInternalMap.ReferenceEntry)o;
/* 3322 */       return (e.getNextExpirable() != MapMakerInternalMap.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3327 */     public boolean isEmpty() { return (this.head.getNextExpirable() == this.head); }
/*      */ 
/*      */ 
/*      */     
/*      */     public int size() {
/* 3332 */       int size = 0;
/* 3333 */       for (MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextExpirable(); e != this.head; e = e.getNextExpirable()) {
/* 3334 */         size++;
/*      */       }
/* 3336 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3341 */       MapMakerInternalMap.ReferenceEntry<K, V> e = this.head.getNextExpirable();
/* 3342 */       while (e != this.head) {
/* 3343 */         MapMakerInternalMap.ReferenceEntry<K, V> next = e.getNextExpirable();
/* 3344 */         MapMakerInternalMap.nullifyExpirable(e);
/* 3345 */         e = next;
/*      */       } 
/*      */       
/* 3348 */       this.head.setNextExpirable(this.head);
/* 3349 */       this.head.setPreviousExpirable(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator() {
/* 3354 */       return new AbstractSequentialIterator<MapMakerInternalMap.ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> previous) {
/* 3357 */             MapMakerInternalMap.ReferenceEntry<K, V> next = previous.getNextExpirable();
/* 3358 */             return (next == MapMakerInternalMap.ExpirationQueue.this.head) ? null : next;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   static final class CleanupMapTask
/*      */     implements Runnable {
/*      */     final WeakReference<MapMakerInternalMap<?, ?>> mapReference;
/*      */     
/* 3368 */     public CleanupMapTask(MapMakerInternalMap<?, ?> map) { this.mapReference = new WeakReference(map); }
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/* 3373 */       MapMakerInternalMap<?, ?> map = (MapMakerInternalMap)this.mapReference.get();
/* 3374 */       if (map == null) {
/* 3375 */         throw new CancellationException();
/*      */       }
/*      */       
/* 3378 */       for (MapMakerInternalMap.Segment<?, ?> segment : map.segments) {
/* 3379 */         segment.runCleanup();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 3395 */     long sum = 0L;
/* 3396 */     Segment[] arrayOfSegment = this.segments;
/* 3397 */     for (int i = 0; i < arrayOfSegment.length; i++) {
/* 3398 */       if ((arrayOfSegment[i]).count != 0) {
/* 3399 */         return false;
/*      */       }
/* 3401 */       sum += (arrayOfSegment[i]).modCount;
/*      */     } 
/*      */     
/* 3404 */     if (sum != 0L) {
/* 3405 */       for (int i = 0; i < arrayOfSegment.length; i++) {
/* 3406 */         if ((arrayOfSegment[i]).count != 0) {
/* 3407 */           return false;
/*      */         }
/* 3409 */         sum -= (arrayOfSegment[i]).modCount;
/*      */       } 
/* 3411 */       if (sum != 0L) {
/* 3412 */         return false;
/*      */       }
/*      */     } 
/* 3415 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 3420 */     Segment[] arrayOfSegment = this.segments;
/* 3421 */     long sum = 0L;
/* 3422 */     for (int i = 0; i < arrayOfSegment.length; i++) {
/* 3423 */       sum += (arrayOfSegment[i]).count;
/*      */     }
/* 3425 */     return Ints.saturatedCast(sum);
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(@Nullable Object key) {
/* 3430 */     if (key == null) {
/* 3431 */       return null;
/*      */     }
/* 3433 */     int hash = hash(key);
/* 3434 */     return (V)segmentFor(hash).get(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReferenceEntry<K, V> getEntry(@Nullable Object key) {
/* 3442 */     if (key == null) {
/* 3443 */       return null;
/*      */     }
/* 3445 */     int hash = hash(key);
/* 3446 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/* 3451 */     if (key == null) {
/* 3452 */       return false;
/*      */     }
/* 3454 */     int hash = hash(key);
/* 3455 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(@Nullable Object value) {
/* 3460 */     if (value == null) {
/* 3461 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3469 */     Segment[] arrayOfSegment = this.segments;
/* 3470 */     long last = -1L;
/* 3471 */     for (int i = 0; i < 3; i++) {
/* 3472 */       long sum = 0L;
/* 3473 */       for (Segment<K, V> segment : arrayOfSegment) {
/*      */ 
/*      */         
/* 3476 */         int c = segment.count;
/*      */         
/* 3478 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
/* 3479 */         for (int j = 0; j < table.length(); j++) {
/* 3480 */           for (ReferenceEntry<K, V> e = (ReferenceEntry)table.get(j); e != null; e = e.getNext()) {
/* 3481 */             V v = (V)segment.getLiveValue(e);
/* 3482 */             if (v != null && this.valueEquivalence.equivalent(value, v)) {
/* 3483 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 3487 */         sum += segment.modCount;
/*      */       } 
/* 3489 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 3492 */       last = sum;
/*      */     } 
/* 3494 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K key, V value) {
/* 3499 */     Preconditions.checkNotNull(key);
/* 3500 */     Preconditions.checkNotNull(value);
/* 3501 */     int hash = hash(key);
/* 3502 */     return (V)segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public V putIfAbsent(K key, V value) {
/* 3507 */     Preconditions.checkNotNull(key);
/* 3508 */     Preconditions.checkNotNull(value);
/* 3509 */     int hash = hash(key);
/* 3510 */     return (V)segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 3515 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 3516 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(@Nullable Object key) {
/* 3522 */     if (key == null) {
/* 3523 */       return null;
/*      */     }
/* 3525 */     int hash = hash(key);
/* 3526 */     return (V)segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/* 3531 */     if (key == null || value == null) {
/* 3532 */       return false;
/*      */     }
/* 3534 */     int hash = hash(key);
/* 3535 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K key, @Nullable V oldValue, V newValue) {
/* 3540 */     Preconditions.checkNotNull(key);
/* 3541 */     Preconditions.checkNotNull(newValue);
/* 3542 */     if (oldValue == null) {
/* 3543 */       return false;
/*      */     }
/* 3545 */     int hash = hash(key);
/* 3546 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public V replace(K key, V value) {
/* 3551 */     Preconditions.checkNotNull(key);
/* 3552 */     Preconditions.checkNotNull(value);
/* 3553 */     int hash = hash(key);
/* 3554 */     return (V)segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 3559 */     for (Segment<K, V> segment : this.segments) {
/* 3560 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 3568 */     Set<K> ks = this.keySet;
/* 3569 */     return (ks != null) ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 3576 */     Collection<V> vs = this.values;
/* 3577 */     return (vs != null) ? vs : (this.values = new Values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 3584 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 3585 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */   
/*      */   abstract class HashIterator<E>
/*      */     extends Object
/*      */     implements Iterator<E>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     MapMakerInternalMap.Segment<K, V> currentSegment;
/*      */     AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> currentTable;
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> nextEntry;
/*      */     MapMakerInternalMap<K, V>.WriteThroughEntry nextExternal;
/*      */     MapMakerInternalMap<K, V>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 3601 */       this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
/* 3602 */       this.nextTableIndex = -1;
/* 3603 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract E next();
/*      */     
/*      */     final void advance() {
/* 3610 */       this.nextExternal = null;
/*      */       
/* 3612 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 3616 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 3620 */       while (this.nextSegmentIndex >= 0) {
/* 3621 */         this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
/* 3622 */         if (this.currentSegment.count != 0) {
/* 3623 */           this.currentTable = this.currentSegment.table;
/* 3624 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 3625 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 3636 */       if (this.nextEntry != null) {
/* 3637 */         for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
/* 3638 */           if (advanceTo(this.nextEntry)) {
/* 3639 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 3643 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 3650 */       while (this.nextTableIndex >= 0) {
/* 3651 */         if ((this.nextEntry = (MapMakerInternalMap.ReferenceEntry)this.currentTable.get(this.nextTableIndex--)) != null && (
/* 3652 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 3653 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 3657 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(MapMakerInternalMap.ReferenceEntry<K, V> entry) {
/*      */       try {
/* 3666 */         K key = (K)entry.getKey();
/* 3667 */         V value = (V)MapMakerInternalMap.this.getLiveValue(entry);
/* 3668 */         if (value != null) {
/* 3669 */           this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(MapMakerInternalMap.this, key, value);
/* 3670 */           return true;
/*      */         } 
/*      */         
/* 3673 */         return false;
/*      */       } finally {
/*      */         
/* 3676 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3682 */     public boolean hasNext() { return (this.nextExternal != null); }
/*      */ 
/*      */     
/*      */     MapMakerInternalMap<K, V>.WriteThroughEntry nextEntry() {
/* 3686 */       if (this.nextExternal == null) {
/* 3687 */         throw new NoSuchElementException();
/*      */       }
/* 3689 */       this.lastReturned = this.nextExternal;
/* 3690 */       advance();
/* 3691 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 3696 */       CollectPreconditions.checkRemove((this.lastReturned != null));
/* 3697 */       MapMakerInternalMap.this.remove(this.lastReturned.getKey());
/* 3698 */       this.lastReturned = null;
/*      */     } }
/*      */   
/*      */   final class KeyIterator extends HashIterator<K> { KeyIterator() {
/* 3702 */       super(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/* 3706 */     public K next() { return (K)nextEntry().getKey(); } }
/*      */   
/*      */   final class ValueIterator extends HashIterator<V> {
/*      */     ValueIterator() {
/* 3710 */       super(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/* 3714 */     public V next() { return (V)nextEntry().getValue(); }
/*      */   }
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */ 
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 3727 */       this.key = key;
/* 3728 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3733 */     public K getKey() { return (K)this.key; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3738 */     public V getValue() { return (V)this.value; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 3744 */       if (object instanceof Map.Entry) {
/* 3745 */         Map.Entry<?, ?> that = (Map.Entry)object;
/* 3746 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 3748 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3754 */     public int hashCode() { return this.key.hashCode() ^ this.value.hashCode(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 3759 */       V oldValue = (V)MapMakerInternalMap.this.put(this.key, newValue);
/* 3760 */       this.value = newValue;
/* 3761 */       return oldValue;
/*      */     } }
/*      */   
/*      */   final class EntryIterator extends HashIterator<Map.Entry<K, V>> { EntryIterator() {
/* 3765 */       super(MapMakerInternalMap.this);
/*      */     }
/*      */ 
/*      */     
/* 3769 */     public Map.Entry<K, V> next() { return nextEntry(); } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final class KeySet
/*      */     extends AbstractSet<K>
/*      */   {
/* 3777 */     public Iterator<K> iterator() { return new MapMakerInternalMap.KeyIterator(MapMakerInternalMap.this); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3782 */     public int size() { return MapMakerInternalMap.this.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3787 */     public boolean isEmpty() { return MapMakerInternalMap.this.isEmpty(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3792 */     public boolean contains(Object o) { return MapMakerInternalMap.this.containsKey(o); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3797 */     public boolean remove(Object o) { return (MapMakerInternalMap.this.remove(o) != null); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3802 */     public void clear() { MapMakerInternalMap.this.clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V>
/*      */   {
/* 3810 */     public Iterator<V> iterator() { return new MapMakerInternalMap.ValueIterator(MapMakerInternalMap.this); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3815 */     public int size() { return MapMakerInternalMap.this.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3820 */     public boolean isEmpty() { return MapMakerInternalMap.this.isEmpty(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3825 */     public boolean contains(Object o) { return MapMakerInternalMap.this.containsValue(o); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3830 */     public void clear() { MapMakerInternalMap.this.clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class EntrySet
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/* 3838 */     public Iterator<Map.Entry<K, V>> iterator() { return new MapMakerInternalMap.EntryIterator(MapMakerInternalMap.this); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3843 */       if (!(o instanceof Map.Entry)) {
/* 3844 */         return false;
/*      */       }
/* 3846 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 3847 */       Object key = e.getKey();
/* 3848 */       if (key == null) {
/* 3849 */         return false;
/*      */       }
/* 3851 */       V v = (V)MapMakerInternalMap.this.get(key);
/*      */       
/* 3853 */       return (v != null && MapMakerInternalMap.this.valueEquivalence.equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3858 */       if (!(o instanceof Map.Entry)) {
/* 3859 */         return false;
/*      */       }
/* 3861 */       Map.Entry<?, ?> e = (Map.Entry)o;
/* 3862 */       Object key = e.getKey();
/* 3863 */       return (key != null && MapMakerInternalMap.this.remove(key, e.getValue()));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3868 */     public int size() { return MapMakerInternalMap.this.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3873 */     public boolean isEmpty() { return MapMakerInternalMap.this.isEmpty(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3878 */     public void clear() { MapMakerInternalMap.this.clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3887 */   Object writeReplace() { return new SerializationProxy(this.keyStrength, this.valueStrength, this.keyEquivalence, this.valueEquivalence, this.expireAfterWriteNanos, this.expireAfterAccessNanos, this.maximumSize, this.concurrencyLevel, this.removalListener, this); }
/*      */ 
/*      */   
/*      */   static abstract class AbstractSerializationProxy<K, V>
/*      */     extends ForwardingConcurrentMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */     
/*      */     final MapMakerInternalMap.Strength keyStrength;
/*      */     
/*      */     final MapMakerInternalMap.Strength valueStrength;
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final long expireAfterWriteNanos;
/*      */     
/*      */     final long expireAfterAccessNanos;
/*      */     
/*      */     final int maximumSize;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     
/*      */     final MapMaker.RemovalListener<? super K, ? super V> removalListener;
/*      */     
/*      */     ConcurrentMap<K, V> delegate;
/*      */     
/*      */     AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate) {
/* 3917 */       this.keyStrength = keyStrength;
/* 3918 */       this.valueStrength = valueStrength;
/* 3919 */       this.keyEquivalence = keyEquivalence;
/* 3920 */       this.valueEquivalence = valueEquivalence;
/* 3921 */       this.expireAfterWriteNanos = expireAfterWriteNanos;
/* 3922 */       this.expireAfterAccessNanos = expireAfterAccessNanos;
/* 3923 */       this.maximumSize = maximumSize;
/* 3924 */       this.concurrencyLevel = concurrencyLevel;
/* 3925 */       this.removalListener = removalListener;
/* 3926 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3931 */     protected ConcurrentMap<K, V> delegate() { return this.delegate; }
/*      */ 
/*      */     
/*      */     void writeMapTo(ObjectOutputStream out) throws IOException {
/* 3935 */       out.writeInt(this.delegate.size());
/* 3936 */       for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
/* 3937 */         out.writeObject(entry.getKey());
/* 3938 */         out.writeObject(entry.getValue());
/*      */       } 
/* 3940 */       out.writeObject(null);
/*      */     }
/*      */ 
/*      */     
/*      */     MapMaker readMapMaker(ObjectInputStream in) throws IOException {
/* 3945 */       int size = in.readInt();
/* 3946 */       MapMaker mapMaker = (new MapMaker()).initialCapacity(size).setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).concurrencyLevel(this.concurrencyLevel);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3952 */       mapMaker.removalListener(this.removalListener);
/* 3953 */       if (this.expireAfterWriteNanos > 0L) {
/* 3954 */         mapMaker.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 3956 */       if (this.expireAfterAccessNanos > 0L) {
/* 3957 */         mapMaker.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 3959 */       if (this.maximumSize != -1) {
/* 3960 */         mapMaker.maximumSize(this.maximumSize);
/*      */       }
/* 3962 */       return mapMaker;
/*      */     }
/*      */ 
/*      */     
/*      */     void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */       while (true) {
/* 3968 */         K key = (K)in.readObject();
/* 3969 */         if (key == null) {
/*      */           break;
/*      */         }
/* 3972 */         V value = (V)in.readObject();
/* 3973 */         this.delegate.put(key, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SerializationProxy<K, V>
/*      */     extends AbstractSerializationProxy<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3990 */     SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, int maximumSize, int concurrencyLevel, MapMaker.RemovalListener<? super K, ? super V> removalListener, ConcurrentMap<K, V> delegate) { super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, expireAfterWriteNanos, expireAfterAccessNanos, maximumSize, concurrencyLevel, removalListener, delegate); }
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 3995 */       out.defaultWriteObject();
/* 3996 */       writeMapTo(out);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 4000 */       in.defaultReadObject();
/* 4001 */       MapMaker mapMaker = readMapMaker(in);
/* 4002 */       this.delegate = mapMaker.makeMap();
/* 4003 */       readEntries(in);
/*      */     }
/*      */ 
/*      */     
/* 4007 */     private Object readResolve() { return this.delegate; }
/*      */   }
/*      */   
/*      */   static interface ReferenceEntry<K, V> {
/*      */     MapMakerInternalMap.ValueReference<K, V> getValueReference();
/*      */     
/*      */     void setValueReference(MapMakerInternalMap.ValueReference<K, V> param1ValueReference);
/*      */     
/*      */     ReferenceEntry<K, V> getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     K getKey();
/*      */     
/*      */     long getExpirationTime();
/*      */     
/*      */     void setExpirationTime(long param1Long);
/*      */     
/*      */     ReferenceEntry<K, V> getNextExpirable();
/*      */     
/*      */     void setNextExpirable(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     ReferenceEntry<K, V> getPreviousExpirable();
/*      */     
/*      */     void setPreviousExpirable(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     ReferenceEntry<K, V> getNextEvictable();
/*      */     
/*      */     void setNextEvictable(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     ReferenceEntry<K, V> getPreviousEvictable();
/*      */     
/*      */     void setPreviousEvictable(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */   }
/*      */   
/*      */   static interface ValueReference<K, V> {
/*      */     V get();
/*      */     
/*      */     V waitForValue();
/*      */     
/*      */     MapMakerInternalMap.ReferenceEntry<K, V> getEntry();
/*      */     
/*      */     ValueReference<K, V> copyFor(ReferenceQueue<V> param1ReferenceQueue, @Nullable V param1V, MapMakerInternalMap.ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     void clear(@Nullable ValueReference<K, V> param1ValueReference);
/*      */     
/*      */     boolean isComputingReference();
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/MapMakerInternalMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */