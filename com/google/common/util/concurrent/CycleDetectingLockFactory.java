/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.MapMaker;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.annotation.concurrent.ThreadSafe;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @ThreadSafe
/*      */ public class CycleDetectingLockFactory
/*      */ {
/*      */   @Beta
/*      */   public final abstract enum Policies
/*      */     implements Policy
/*      */   {
/*      */     THROW, WARN, DISABLED;
/*      */     
/*      */     static  {
/*      */       // Byte code:
/*      */       //   0: new com/google/common/util/concurrent/CycleDetectingLockFactory$Policies$1
/*      */       //   3: dup
/*      */       //   4: ldc 'THROW'
/*      */       //   6: iconst_0
/*      */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   10: putstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.THROW : Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   13: new com/google/common/util/concurrent/CycleDetectingLockFactory$Policies$2
/*      */       //   16: dup
/*      */       //   17: ldc 'WARN'
/*      */       //   19: iconst_1
/*      */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   23: putstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.WARN : Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   26: new com/google/common/util/concurrent/CycleDetectingLockFactory$Policies$3
/*      */       //   29: dup
/*      */       //   30: ldc 'DISABLED'
/*      */       //   32: iconst_2
/*      */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   36: putstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.DISABLED : Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   39: iconst_3
/*      */       //   40: anewarray com/google/common/util/concurrent/CycleDetectingLockFactory$Policies
/*      */       //   43: dup
/*      */       //   44: iconst_0
/*      */       //   45: getstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.THROW : Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   48: aastore
/*      */       //   49: dup
/*      */       //   50: iconst_1
/*      */       //   51: getstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.WARN : Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   54: aastore
/*      */       //   55: dup
/*      */       //   56: iconst_2
/*      */       //   57: getstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.DISABLED : Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   60: aastore
/*      */       //   61: putstatic com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.$VALUES : [Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
/*      */       //   64: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #205	-> 0
/*      */       //   #218	-> 13
/*      */       //   #235	-> 26
/*      */       //   #197	-> 39
/*      */     }
/*      */   }
/*      */   
/*  246 */   public static CycleDetectingLockFactory newInstance(Policy policy) { return new CycleDetectingLockFactory(policy); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  253 */   public ReentrantLock newReentrantLock(String lockName) { return newReentrantLock(lockName, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  262 */   public ReentrantLock newReentrantLock(String lockName, boolean fair) { return (this.policy == Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair, null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  271 */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) { return newReentrantReadWriteLock(lockName, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  281 */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) { return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair, null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  288 */   private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = (new MapMaker()).weakKeys().makeMap();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy) {
/*  298 */     Preconditions.checkNotNull(enumClass);
/*  299 */     Preconditions.checkNotNull(policy);
/*      */     
/*  301 */     Map<E, LockGraphNode> lockGraphNodes = getOrCreateNodes(enumClass);
/*      */     
/*  303 */     return new WithExplicitOrdering(policy, lockGraphNodes);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
/*  308 */     Map<? extends Enum, LockGraphNode> existing = (Map)lockGraphNodesPerType.get(clazz);
/*      */     
/*  310 */     if (existing != null) {
/*  311 */       return existing;
/*      */     }
/*  313 */     Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
/*  314 */     existing = (Map)lockGraphNodesPerType.putIfAbsent(clazz, created);
/*  315 */     return (Map)Objects.firstNonNull(existing, created);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz) {
/*  326 */     EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
/*  327 */     E[] keys = (E[])(Enum[])clazz.getEnumConstants();
/*  328 */     int numKeys = keys.length;
/*  329 */     ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
/*      */ 
/*      */     
/*  332 */     for (E key : keys) {
/*  333 */       LockGraphNode node = new LockGraphNode(getLockName(key));
/*  334 */       nodes.add(node);
/*  335 */       map.put(key, node);
/*      */     } 
/*      */     
/*  338 */     for (int i = 1; i < numKeys; i++) {
/*  339 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
/*      */     }
/*      */     
/*  342 */     for (int i = 0; i < numKeys - 1; i++) {
/*  343 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
/*      */     }
/*      */     
/*  346 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  355 */   private static String getLockName(Enum<?> rank) { return rank.getDeclaringClass().getSimpleName() + "." + rank.name(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static final class WithExplicitOrdering<E extends Enum<E>>
/*      */     extends CycleDetectingLockFactory
/*      */   {
/*      */     private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes) {
/*  428 */       super(policy, null);
/*  429 */       this.lockGraphNodes = lockGraphNodes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  436 */     public ReentrantLock newReentrantLock(E rank) { return newReentrantLock(rank, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  449 */     public ReentrantLock newReentrantLock(E rank, boolean fair) { return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this, (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank), fair, null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  457 */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) { return newReentrantReadWriteLock(rank, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  471 */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) { return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank), fair, null); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  479 */   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
/*      */ 
/*      */   
/*      */   final Policy policy;
/*      */ 
/*      */   
/*  485 */   private CycleDetectingLockFactory(Policy policy) { this.policy = (Policy)Preconditions.checkNotNull(policy); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  496 */   private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>()
/*      */     {
/*      */       protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue() {
/*  499 */         return Lists.newArrayListWithCapacity(3);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ExampleStackTrace
/*      */     extends IllegalStateException
/*      */   {
/*  519 */     static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*      */ 
/*      */     
/*  522 */     static Set<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), CycleDetectingLockFactory.LockGraphNode.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
/*  528 */       super(node1.getLockName() + " -> " + node2.getLockName());
/*  529 */       StackTraceElement[] origStackTrace = getStackTrace();
/*  530 */       for (int i = 0, n = origStackTrace.length; i < n; i++) {
/*  531 */         if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
/*      */ 
/*      */           
/*  534 */           setStackTrace(EMPTY_STACK_TRACE);
/*      */           break;
/*      */         } 
/*  537 */         if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
/*  538 */           setStackTrace((StackTraceElement[])Arrays.copyOfRange(origStackTrace, i, n));
/*      */           break;
/*      */         } 
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
/*      */   
/*      */   @Beta
/*      */   public static final class PotentialDeadlockException
/*      */     extends ExampleStackTrace
/*      */   {
/*      */     private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace) {
/*  577 */       super(node1, node2);
/*  578 */       this.conflictingStackTrace = conflictingStackTrace;
/*  579 */       initCause(conflictingStackTrace);
/*      */     }
/*      */ 
/*      */     
/*  583 */     public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() { return this.conflictingStackTrace; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMessage() {
/*  592 */       StringBuilder message = new StringBuilder(super.getMessage());
/*  593 */       for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
/*  594 */         message.append(", ").append(t.getMessage());
/*      */       }
/*  596 */       return message.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class LockGraphNode
/*      */   {
/*      */     final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String lockName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LockGraphNode(String lockName) {
/*  625 */       this.allowedPriorLocks = (new MapMaker()).weakKeys().makeMap();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  632 */       this.disallowedPriorLocks = (new MapMaker()).weakKeys().makeMap();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  638 */       this.lockName = (String)Preconditions.checkNotNull(lockName);
/*      */     }
/*      */ 
/*      */     
/*  642 */     String getLockName() { return this.lockName; }
/*      */ 
/*      */ 
/*      */     
/*      */     void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
/*  647 */       for (int i = 0, size = acquiredLocks.size(); i < size; i++) {
/*  648 */         checkAcquiredLock(policy, (LockGraphNode)acquiredLocks.get(i));
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
/*      */ 
/*      */ 
/*      */     
/*      */     void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock) {
/*  668 */       Preconditions.checkState((this != acquiredLock), "Attempted to acquire multiple locks with the same rank " + acquiredLock.getLockName());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  673 */       if (this.allowedPriorLocks.containsKey(acquiredLock)) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  679 */       CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = (CycleDetectingLockFactory.PotentialDeadlockException)this.disallowedPriorLocks.get(acquiredLock);
/*      */       
/*  681 */       if (previousDeadlockException != null) {
/*      */ 
/*      */ 
/*      */         
/*  685 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace(), null);
/*      */ 
/*      */         
/*  688 */         policy.handlePotentialDeadlock(exception);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  693 */       Set<LockGraphNode> seen = Sets.newIdentityHashSet();
/*  694 */       CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
/*      */       
/*  696 */       if (path == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  705 */         this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  710 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path, null);
/*      */         
/*  712 */         this.disallowedPriorLocks.put(acquiredLock, exception);
/*  713 */         policy.handlePotentialDeadlock(exception);
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
/*      */     @Nullable
/*      */     private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen) {
/*  729 */       if (!seen.add(this)) {
/*  730 */         return null;
/*      */       }
/*  732 */       CycleDetectingLockFactory.ExampleStackTrace found = (CycleDetectingLockFactory.ExampleStackTrace)this.allowedPriorLocks.get(node);
/*  733 */       if (found != null) {
/*  734 */         return found;
/*      */       }
/*      */ 
/*      */       
/*  738 */       for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
/*  739 */         LockGraphNode preAcquiredLock = (LockGraphNode)entry.getKey();
/*  740 */         found = preAcquiredLock.findPathTo(node, seen);
/*  741 */         if (found != null) {
/*      */ 
/*      */ 
/*      */           
/*  745 */           CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
/*      */           
/*  747 */           path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
/*  748 */           path.initCause(found);
/*  749 */           return path;
/*      */         } 
/*      */       } 
/*  752 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void aboutToAcquire(CycleDetectingLock lock) {
/*  761 */     if (!lock.isAcquiredByCurrentThread()) {
/*  762 */       ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
/*  763 */       LockGraphNode node = lock.getLockGraphNode();
/*  764 */       node.checkAcquiredLocks(this.policy, acquiredLockList);
/*  765 */       acquiredLockList.add(node);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void lockStateChanged(CycleDetectingLock lock) {
/*  776 */     if (!lock.isAcquiredByCurrentThread()) {
/*  777 */       ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
/*  778 */       LockGraphNode node = lock.getLockGraphNode();
/*      */ 
/*      */       
/*  781 */       for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
/*  782 */         if (acquiredLockList.get(i) == node) {
/*  783 */           acquiredLockList.remove(i);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   final class CycleDetectingReentrantLock
/*      */     extends ReentrantLock
/*      */     implements CycleDetectingLock
/*      */   {
/*      */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*      */     
/*      */     private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/*  797 */       super(fair);
/*  798 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  805 */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() { return this.lockGraphNode; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  810 */     public boolean isAcquiredByCurrentThread() { return isHeldByCurrentThread(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void lock() {
/*  817 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*      */       try {
/*  819 */         super.lock();
/*      */       } finally {
/*  821 */         CycleDetectingLockFactory.this.lockStateChanged(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void lockInterruptibly() {
/*  827 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*      */       try {
/*  829 */         super.lockInterruptibly();
/*      */       } finally {
/*  831 */         CycleDetectingLockFactory.this.lockStateChanged(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean tryLock() {
/*  837 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*      */       try {
/*  839 */         return super.tryLock();
/*      */       } finally {
/*  841 */         CycleDetectingLockFactory.this.lockStateChanged(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/*  848 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*      */       try {
/*  850 */         return super.tryLock(timeout, unit);
/*      */       } finally {
/*  852 */         CycleDetectingLockFactory.this.lockStateChanged(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void unlock() {
/*      */       try {
/*  859 */         super.unlock();
/*      */       } finally {
/*  861 */         CycleDetectingLockFactory.this.lockStateChanged(this);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class CycleDetectingReentrantReadWriteLock
/*      */     extends ReentrantReadWriteLock
/*      */     implements CycleDetectingLock
/*      */   {
/*      */     private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
/*      */     
/*      */     private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
/*      */     
/*      */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*      */ 
/*      */     
/*      */     private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/*  880 */       super(fair);
/*  881 */       this.readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(this$0, this);
/*  882 */       this.writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(this$0, this);
/*  883 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  890 */     public ReentrantReadWriteLock.ReadLock readLock() { return this.readLock; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  895 */     public ReentrantReadWriteLock.WriteLock writeLock() { return this.writeLock; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  902 */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() { return this.lockGraphNode; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  907 */     public boolean isAcquiredByCurrentThread() { return (isWriteLockedByCurrentThread() || getReadHoldCount() > 0); }
/*      */   }
/*      */ 
/*      */   
/*      */   private class CycleDetectingReentrantReadLock
/*      */     extends ReentrantReadWriteLock.ReadLock
/*      */   {
/*      */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*      */ 
/*      */     
/*      */     CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/*  918 */       super(readWriteLock);
/*  919 */       this.readWriteLock = readWriteLock;
/*      */     }
/*      */ 
/*      */     
/*      */     public void lock() {
/*  924 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/*  926 */         super.lock();
/*      */       } finally {
/*  928 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void lockInterruptibly() {
/*  934 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/*  936 */         super.lockInterruptibly();
/*      */       } finally {
/*  938 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean tryLock() {
/*  944 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/*  946 */         return super.tryLock();
/*      */       } finally {
/*  948 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/*  955 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/*  957 */         return super.tryLock(timeout, unit);
/*      */       } finally {
/*  959 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void unlock() {
/*      */       try {
/*  966 */         super.unlock();
/*      */       } finally {
/*  968 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class CycleDetectingReentrantWriteLock
/*      */     extends ReentrantReadWriteLock.WriteLock
/*      */   {
/*      */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*      */     
/*      */     CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/*  980 */       super(readWriteLock);
/*  981 */       this.readWriteLock = readWriteLock;
/*      */     }
/*      */ 
/*      */     
/*      */     public void lock() {
/*  986 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/*  988 */         super.lock();
/*      */       } finally {
/*  990 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void lockInterruptibly() {
/*  996 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/*  998 */         super.lockInterruptibly();
/*      */       } finally {
/* 1000 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean tryLock() {
/* 1006 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/* 1008 */         return super.tryLock();
/*      */       } finally {
/* 1010 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 1017 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*      */       try {
/* 1019 */         return super.tryLock(timeout, unit);
/*      */       } finally {
/* 1021 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void unlock() {
/*      */       try {
/* 1028 */         super.unlock();
/*      */       } finally {
/* 1030 */         CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static interface CycleDetectingLock {
/*      */     CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
/*      */     
/*      */     boolean isAcquiredByCurrentThread();
/*      */   }
/*      */   
/*      */   @Beta
/*      */   @ThreadSafe
/*      */   public static interface Policy {
/*      */     void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException param1PotentialDeadlockException);
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/CycleDetectingLockFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */