/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.locks.Condition;
/*      */ import java.util.concurrent.locks.ReentrantLock;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class Monitor
/*      */ {
/*      */   private final boolean fair;
/*      */   private final ReentrantLock lock;
/*      */   @GuardedBy("lock")
/*      */   private Guard activeGuards;
/*      */   
/*      */   @Beta
/*      */   public static abstract class Guard
/*      */   {
/*      */     final Monitor monitor;
/*      */     final Condition condition;
/*      */     @GuardedBy("monitor.lock")
/*      */     int waiterCount;
/*      */     @GuardedBy("monitor.lock")
/*      */     Guard next;
/*      */     
/*      */     protected Guard(Monitor monitor) {
/*  296 */       this.waiterCount = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  304 */       this.monitor = (Monitor)Preconditions.checkNotNull(monitor, "monitor");
/*  305 */       this.condition = monitor.lock.newCondition();
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
/*      */     public abstract boolean isSatisfied();
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
/*      */   
/*  339 */   public Monitor() { this(false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Monitor(boolean fair) {
/*      */     this.activeGuards = null;
/*  349 */     this.fair = fair;
/*  350 */     this.lock = new ReentrantLock(fair);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  357 */   public void enter() { this.lock.lock(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  364 */   public void enterInterruptibly() { this.lock.lockInterruptibly(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enter(long time, TimeUnit unit) {
/*  373 */     timeoutNanos = unit.toNanos(time);
/*  374 */     ReentrantLock lock = this.lock;
/*  375 */     if (!this.fair && lock.tryLock()) {
/*  376 */       return true;
/*      */     }
/*  378 */     deadline = System.nanoTime() + timeoutNanos;
/*  379 */     interrupted = Thread.interrupted();
/*      */     
/*      */     while (true) {
/*      */       try {
/*  383 */         return lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
/*  384 */       } catch (InterruptedException interrupt) {
/*  385 */         interrupted = true;
/*      */       
/*      */       }
/*      */       finally {
/*      */         
/*  390 */         if (interrupted) {
/*  391 */           Thread.currentThread().interrupt();
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
/*  402 */   public boolean enterInterruptibly(long time, TimeUnit unit) { return this.lock.tryLock(time, unit); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  413 */   public boolean tryEnter() { return this.lock.tryLock(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhen(Guard guard) throws InterruptedException {
/*  420 */     if (guard.monitor != this) {
/*  421 */       throw new IllegalMonitorStateException();
/*      */     }
/*  423 */     ReentrantLock lock = this.lock;
/*  424 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  425 */     lock.lockInterruptibly();
/*      */     
/*  427 */     satisfied = false;
/*      */     try {
/*  429 */       if (!guard.isSatisfied()) {
/*  430 */         await(guard, signalBeforeWaiting);
/*      */       }
/*  432 */       satisfied = true;
/*      */     } finally {
/*  434 */       if (!satisfied) {
/*  435 */         leave();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterWhenUninterruptibly(Guard guard) throws InterruptedException {
/*  444 */     if (guard.monitor != this) {
/*  445 */       throw new IllegalMonitorStateException();
/*      */     }
/*  447 */     ReentrantLock lock = this.lock;
/*  448 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  449 */     lock.lock();
/*      */     
/*  451 */     satisfied = false;
/*      */     try {
/*  453 */       if (!guard.isSatisfied()) {
/*  454 */         awaitUninterruptibly(guard, signalBeforeWaiting);
/*      */       }
/*  456 */       satisfied = true;
/*      */     } finally {
/*  458 */       if (!satisfied) {
/*  459 */         leave();
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
/*      */   public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  472 */     long timeoutNanos = unit.toNanos(time);
/*  473 */     if (guard.monitor != this) {
/*  474 */       throw new IllegalMonitorStateException();
/*      */     }
/*  476 */     lock = this.lock;
/*  477 */     reentrant = lock.isHeldByCurrentThread();
/*  478 */     if (this.fair || !lock.tryLock()) {
/*  479 */       long deadline = System.nanoTime() + timeoutNanos;
/*  480 */       if (!lock.tryLock(time, unit)) {
/*  481 */         return false;
/*      */       }
/*  483 */       timeoutNanos = deadline - System.nanoTime();
/*      */     } 
/*      */     
/*  486 */     satisfied = false;
/*  487 */     threw = true;
/*      */     try {
/*  489 */       satisfied = (guard.isSatisfied() || awaitNanos(guard, timeoutNanos, reentrant));
/*  490 */       threw = false;
/*  491 */       return satisfied;
/*      */     } finally {
/*  493 */       if (!satisfied) {
/*      */         
/*      */         try {
/*  496 */           if (threw && !reentrant) {
/*  497 */             signalNextWaiter();
/*      */           }
/*      */         } finally {
/*  500 */           lock.unlock();
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
/*      */   public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  513 */     timeoutNanos = unit.toNanos(time);
/*  514 */     if (guard.monitor != this) {
/*  515 */       throw new IllegalMonitorStateException();
/*      */     }
/*  517 */     lock = this.lock;
/*  518 */     deadline = System.nanoTime() + timeoutNanos;
/*  519 */     boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
/*  520 */     interrupted = Thread.interrupted();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean enterIf(Guard guard) {
/*  568 */     if (guard.monitor != this) {
/*  569 */       throw new IllegalMonitorStateException();
/*      */     }
/*  571 */     lock = this.lock;
/*  572 */     lock.lock();
/*      */     
/*  574 */     satisfied = false;
/*      */     try {
/*  576 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  578 */       if (!satisfied) {
/*  579 */         lock.unlock();
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
/*      */   public boolean enterIfInterruptibly(Guard guard) {
/*  591 */     if (guard.monitor != this) {
/*  592 */       throw new IllegalMonitorStateException();
/*      */     }
/*  594 */     lock = this.lock;
/*  595 */     lock.lockInterruptibly();
/*      */     
/*  597 */     satisfied = false;
/*      */     try {
/*  599 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  601 */       if (!satisfied) {
/*  602 */         lock.unlock();
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
/*      */   public boolean enterIf(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  614 */     if (guard.monitor != this) {
/*  615 */       throw new IllegalMonitorStateException();
/*      */     }
/*  617 */     if (!enter(time, unit)) {
/*  618 */       return false;
/*      */     }
/*      */     
/*  621 */     satisfied = false;
/*      */     try {
/*  623 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  625 */       if (!satisfied) {
/*  626 */         this.lock.unlock();
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
/*      */   public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  639 */     if (guard.monitor != this) {
/*  640 */       throw new IllegalMonitorStateException();
/*      */     }
/*  642 */     lock = this.lock;
/*  643 */     if (!lock.tryLock(time, unit)) {
/*  644 */       return false;
/*      */     }
/*      */     
/*  647 */     satisfied = false;
/*      */     try {
/*  649 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  651 */       if (!satisfied) {
/*  652 */         lock.unlock();
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
/*      */   public boolean tryEnterIf(Guard guard) {
/*  666 */     if (guard.monitor != this) {
/*  667 */       throw new IllegalMonitorStateException();
/*      */     }
/*  669 */     lock = this.lock;
/*  670 */     if (!lock.tryLock()) {
/*  671 */       return false;
/*      */     }
/*      */     
/*  674 */     satisfied = false;
/*      */     try {
/*  676 */       return satisfied = guard.isSatisfied();
/*      */     } finally {
/*  678 */       if (!satisfied) {
/*  679 */         lock.unlock();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitFor(Guard guard) throws InterruptedException {
/*  689 */     if (!(((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread())) {
/*  690 */       throw new IllegalMonitorStateException();
/*      */     }
/*  692 */     if (!guard.isSatisfied()) {
/*  693 */       await(guard, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void waitForUninterruptibly(Guard guard) throws InterruptedException {
/*  702 */     if (!(((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread())) {
/*  703 */       throw new IllegalMonitorStateException();
/*      */     }
/*  705 */     if (!guard.isSatisfied()) {
/*  706 */       awaitUninterruptibly(guard, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  717 */     long timeoutNanos = unit.toNanos(time);
/*  718 */     if (!(((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread())) {
/*  719 */       throw new IllegalMonitorStateException();
/*      */     }
/*  721 */     return (guard.isSatisfied() || awaitNanos(guard, timeoutNanos, true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
/*  731 */     timeoutNanos = unit.toNanos(time);
/*  732 */     if (!(((guard.monitor == this) ? 1 : 0) & this.lock.isHeldByCurrentThread())) {
/*  733 */       throw new IllegalMonitorStateException();
/*      */     }
/*  735 */     if (guard.isSatisfied()) {
/*  736 */       return true;
/*      */     }
/*  738 */     boolean signalBeforeWaiting = true;
/*  739 */     deadline = System.nanoTime() + timeoutNanos;
/*  740 */     interrupted = Thread.interrupted();
/*      */     
/*      */     while (true) {
/*      */       try {
/*  744 */         return awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
/*  745 */       } catch (InterruptedException interrupt) {
/*  746 */         interrupted = true;
/*  747 */         if (guard.isSatisfied()) {
/*  748 */           return true;
/*      */         }
/*  750 */         signalBeforeWaiting = false;
/*      */       
/*      */       }
/*      */       finally {
/*      */         
/*  755 */         if (interrupted) {
/*  756 */           Thread.currentThread().interrupt();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void leave() {
/*  765 */     lock = this.lock;
/*      */     
/*      */     try {
/*  768 */       if (lock.getHoldCount() == 1) {
/*  769 */         signalNextWaiter();
/*      */       }
/*      */     } finally {
/*  772 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  780 */   public boolean isFair() { return this.fair; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  788 */   public boolean isOccupied() { return this.lock.isLocked(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  796 */   public boolean isOccupiedByCurrentThread() { return this.lock.isHeldByCurrentThread(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  804 */   public int getOccupiedDepth() { return this.lock.getHoldCount(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  814 */   public int getQueueLength() { return this.lock.getQueueLength(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  824 */   public boolean hasQueuedThreads() { return this.lock.hasQueuedThreads(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  834 */   public boolean hasQueuedThread(Thread thread) { return this.lock.hasQueuedThread(thread); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  844 */   public boolean hasWaiters(Guard guard) { return (getWaitQueueLength(guard) > 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWaitQueueLength(Guard guard) {
/*  854 */     if (guard.monitor != this) {
/*  855 */       throw new IllegalMonitorStateException();
/*      */     }
/*  857 */     this.lock.lock();
/*      */     try {
/*  859 */       return guard.waiterCount;
/*      */     } finally {
/*  861 */       this.lock.unlock();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalNextWaiter() {
/*  891 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/*  892 */       if (isSatisfied(guard)) {
/*  893 */         guard.condition.signal();
/*      */         break;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean isSatisfied(Guard guard) {
/*      */     try {
/*  924 */       return guard.isSatisfied();
/*  925 */     } catch (Throwable throwable) {
/*  926 */       signalAllWaiters();
/*  927 */       throw Throwables.propagate(throwable);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void signalAllWaiters() {
/*  936 */     for (Guard guard = this.activeGuards; guard != null; guard = guard.next) {
/*  937 */       guard.condition.signalAll();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void beginWaitingFor(Guard guard) throws InterruptedException {
/*  946 */     int waiters = guard.waiterCount++;
/*  947 */     if (waiters == 0) {
/*      */       
/*  949 */       guard.next = this.activeGuards;
/*  950 */       this.activeGuards = guard;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void endWaitingFor(Guard guard) throws InterruptedException {
/*  959 */     int waiters = --guard.waiterCount;
/*  960 */     if (waiters == 0)
/*      */     {
/*  962 */       for (Guard p = this.activeGuards, pred = null;; pred = p, p = p.next) {
/*  963 */         if (p == guard) {
/*  964 */           if (pred == null) {
/*  965 */             this.activeGuards = p.next;
/*      */           } else {
/*  967 */             pred.next = p.next;
/*      */           } 
/*  969 */           p.next = null;
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
/*      */   @GuardedBy("lock")
/*      */   private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
/*  985 */     if (signalBeforeWaiting) {
/*  986 */       signalNextWaiter();
/*      */     }
/*  988 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/*  991 */         guard.condition.await();
/*  992 */       } while (!guard.isSatisfied());
/*      */     } finally {
/*  994 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
/* 1000 */     if (signalBeforeWaiting) {
/* 1001 */       signalNextWaiter();
/*      */     }
/* 1003 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1006 */         guard.condition.awaitUninterruptibly();
/* 1007 */       } while (!guard.isSatisfied());
/*      */     } finally {
/* 1009 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @GuardedBy("lock")
/*      */   private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
/* 1016 */     if (signalBeforeWaiting) {
/* 1017 */       signalNextWaiter();
/*      */     }
/* 1019 */     beginWaitingFor(guard);
/*      */     try {
/*      */       do {
/* 1022 */         if (nanos < 0L) {
/* 1023 */           return false;
/*      */         }
/* 1025 */         nanos = guard.condition.awaitNanos(nanos);
/* 1026 */       } while (!guard.isSatisfied());
/* 1027 */       return true;
/*      */     } finally {
/* 1029 */       endWaitingFor(guard);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/Monitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */