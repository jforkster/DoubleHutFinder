/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ServiceManager
/*     */ {
/* 126 */   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
/* 127 */   private static final ListenerCallQueue.Callback<Listener> HEALTHY_CALLBACK = new ListenerCallQueue.Callback<Listener>("healthy()")
/*     */     {
/* 129 */       void call(ServiceManager.Listener listener) { listener.healthy(); }
/*     */     };
/*     */   
/* 132 */   private static final ListenerCallQueue.Callback<Listener> STOPPED_CALLBACK = new ListenerCallQueue.Callback<Listener>("stopped()")
/*     */     {
/* 134 */       void call(ServiceManager.Listener listener) { listener.stopped(); }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ServiceManagerState state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<Service> services;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stopped() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void failure(Service service) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager(Iterable<? extends Service> services) {
/* 192 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 193 */     if (copy.isEmpty()) {
/*     */ 
/*     */       
/* 196 */       logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning(null));
/*     */ 
/*     */       
/* 199 */       copy = ImmutableList.of(new NoOpService(null));
/*     */     } 
/* 201 */     this.state = new ServiceManagerState(copy);
/* 202 */     this.services = copy;
/* 203 */     WeakReference<ServiceManagerState> stateReference = new WeakReference<ServiceManagerState>(this.state);
/*     */     
/* 205 */     Executor sameThreadExecutor = MoreExecutors.sameThreadExecutor();
/* 206 */     for (Service service : copy) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 215 */       service.addListener(new ServiceListener(service, stateReference), sameThreadExecutor);
/*     */ 
/*     */       
/* 218 */       Preconditions.checkArgument((service.state() == Service.State.NEW), "Can only manage NEW services, %s", new Object[] { service });
/*     */     } 
/*     */ 
/*     */     
/* 222 */     this.state.markReady();
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
/* 249 */   public void addListener(Listener listener, Executor executor) { this.state.addListener(listener, executor); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   public void addListener(Listener listener) { this.state.addListener(listener, MoreExecutors.sameThreadExecutor()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager startAsync() {
/* 281 */     for (Service service : this.services) {
/* 282 */       Service.State state = service.state();
/* 283 */       Preconditions.checkState((state == Service.State.NEW), "Service %s is %s, cannot start it.", new Object[] { service, state });
/*     */     } 
/* 285 */     for (Service service : this.services) {
/*     */       try {
/* 287 */         service.startAsync();
/* 288 */       } catch (IllegalStateException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 293 */         logger.log(Level.WARNING, "Unable to start Service " + service, e);
/*     */       } 
/*     */     } 
/* 296 */     return this;
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
/* 308 */   public void awaitHealthy() { this.state.awaitHealthy(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 323 */   public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException { this.state.awaitHealthy(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager stopAsync() {
/* 333 */     for (Service service : this.services) {
/* 334 */       service.stopAsync();
/*     */     }
/* 336 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   public void awaitStopped() { this.state.awaitStopped(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 358 */   public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException { this.state.awaitStopped(timeout, unit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHealthy() {
/* 368 */     for (Service service : this.services) {
/* 369 */       if (!service.isRunning()) {
/* 370 */         return false;
/*     */       }
/*     */     } 
/* 373 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 383 */   public ImmutableMultimap<Service.State, Service> servicesByState() { return this.state.servicesByState(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 394 */   public ImmutableMap<Service, Long> startupTimes() { return this.state.startupTimes(); }
/*     */ 
/*     */ 
/*     */   
/* 398 */   public String toString() { return Objects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString(); }
/*     */ 
/*     */   
/*     */   private static final class ServiceManagerState
/*     */   {
/*     */     final Monitor monitor;
/*     */     @GuardedBy("monitor")
/*     */     final SetMultimap<Service.State, Service> servicesByState;
/*     */     
/*     */     ServiceManagerState(ImmutableCollection<Service> services) {
/* 408 */       this.monitor = new Monitor();
/*     */       
/* 410 */       this.servicesByState = Multimaps.newSetMultimap(new EnumMap(Service.State.class), new Supplier<Set<Service>>()
/*     */           {
/*     */             
/*     */             public Set<Service> get()
/*     */             {
/* 415 */               return Sets.newLinkedHashSet();
/*     */             }
/*     */           });
/*     */       
/* 419 */       this.states = this.servicesByState.keys();
/*     */ 
/*     */       
/* 422 */       this.startupTimers = Maps.newIdentityHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 448 */       this.awaitHealthGuard = new Monitor.Guard(this.monitor)
/*     */         {
/*     */           public boolean isSatisfied() {
/* 451 */             return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices || ServiceManager.ServiceManagerState.this.states.contains(Service.State.STOPPING) || ServiceManager.ServiceManagerState.this.states.contains(Service.State.TERMINATED) || ServiceManager.ServiceManagerState.this.states.contains(Service.State.FAILED));
/*     */           }
/*     */         };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 461 */       this.stoppedGuard = new Monitor.Guard(this.monitor) {
/*     */           public boolean isSatisfied() {
/* 463 */             return (ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices);
/*     */           }
/*     */         };
/*     */ 
/*     */       
/* 468 */       this.listeners = Collections.synchronizedList(new ArrayList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 479 */       this.numberOfServices = services.size();
/* 480 */       this.servicesByState.putAll(Service.State.NEW, services);
/* 481 */       for (Service service : services)
/* 482 */         this.startupTimers.put(service, Stopwatch.createUnstarted()); 
/*     */     }
/*     */     @GuardedBy("monitor")
/*     */     final Multiset<Service.State> states; @GuardedBy("monitor")
/*     */     final Map<Service, Stopwatch> startupTimers; @GuardedBy("monitor")
/*     */     boolean ready; @GuardedBy("monitor")
/*     */     boolean transitioned; final int numberOfServices; final Monitor.Guard awaitHealthGuard; final Monitor.Guard stoppedGuard; @GuardedBy("monitor")
/*     */     final List<ListenerCallQueue<ServiceManager.Listener>> listeners;
/*     */     void markReady() {
/* 491 */       this.monitor.enter();
/*     */       try {
/* 493 */         if (!this.transitioned) {
/*     */           
/* 495 */           this.ready = true;
/*     */         } else {
/*     */           
/* 498 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 499 */           for (Service service : servicesByState().values()) {
/* 500 */             if (service.state() != Service.State.NEW) {
/* 501 */               servicesInBadStates.add(service);
/*     */             }
/*     */           } 
/* 504 */           throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
/*     */         } 
/*     */       } finally {
/*     */         
/* 508 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 513 */       Preconditions.checkNotNull(listener, "listener");
/* 514 */       Preconditions.checkNotNull(executor, "executor");
/* 515 */       this.monitor.enter();
/*     */       
/*     */       try {
/* 518 */         if (!this.stoppedGuard.isSatisfied()) {
/* 519 */           this.listeners.add(new ListenerCallQueue(listener, executor));
/*     */         }
/*     */       } finally {
/* 522 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy() {
/* 527 */       this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
/*     */       try {
/* 529 */         checkHealthy();
/*     */       } finally {
/* 531 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 536 */       this.monitor.enter();
/*     */       try {
/* 538 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
/* 539 */           throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
/*     */         }
/*     */ 
/*     */         
/* 543 */         checkHealthy();
/*     */       } finally {
/* 545 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 550 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 551 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 555 */       this.monitor.enter();
/*     */       try {
/* 557 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
/* 558 */           throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(ImmutableSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
/*     */         
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 564 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     ImmutableMultimap<Service.State, Service> servicesByState() {
/* 569 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 570 */       this.monitor.enter();
/*     */       try {
/* 572 */         for (Map.Entry<Service.State, Service> entry : this.servicesByState.entries()) {
/* 573 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 574 */             builder.put(entry.getKey(), entry.getValue());
/*     */           }
/*     */         } 
/*     */       } finally {
/* 578 */         this.monitor.leave();
/*     */       } 
/* 580 */       return builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes() {
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 585 */       this.monitor.enter();
/*     */       try {
/* 587 */         loadTimes = Lists.newArrayListWithCapacity(this.states.size() - this.states.count(Service.State.NEW) + this.states.count(Service.State.STARTING));
/*     */         
/* 589 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 590 */           Service service = (Service)entry.getKey();
/* 591 */           Stopwatch stopWatch = (Stopwatch)entry.getValue();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 596 */           if (!stopWatch.isRunning() && !this.servicesByState.containsEntry(Service.State.NEW, service) && !(service instanceof ServiceManager.NoOpService))
/*     */           {
/* 598 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */           }
/*     */         } 
/*     */       } finally {
/* 602 */         this.monitor.leave();
/*     */       } 
/* 604 */       Collections.sort(loadTimes, Ordering.natural().onResultOf(new Function<Map.Entry<Service, Long>, Long>()
/*     */             {
/*     */               public Long apply(Map.Entry<Service, Long> input) {
/* 607 */                 return (Long)input.getValue();
/*     */               }
/*     */             }));
/* 610 */       ImmutableMap.Builder<Service, Long> builder = ImmutableMap.builder();
/* 611 */       for (Map.Entry<Service, Long> entry : loadTimes) {
/* 612 */         builder.put(entry);
/*     */       }
/* 614 */       return builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void transitionService(Service service, Service.State from, Service.State to) {
/* 629 */       Preconditions.checkNotNull(service);
/* 630 */       Preconditions.checkArgument((from != to));
/* 631 */       this.monitor.enter();
/*     */       try {
/* 633 */         this.transitioned = true;
/* 634 */         if (!this.ready) {
/*     */           return;
/*     */         }
/*     */         
/* 638 */         Preconditions.checkState(this.servicesByState.remove(from, service), "Service %s not at the expected location in the state map %s", new Object[] { service, from });
/*     */         
/* 640 */         Preconditions.checkState(this.servicesByState.put(to, service), "Service %s in the state map unexpectedly at %s", new Object[] { service, to });
/*     */ 
/*     */         
/* 643 */         Stopwatch stopwatch = (Stopwatch)this.startupTimers.get(service);
/* 644 */         if (from == Service.State.NEW) {
/* 645 */           stopwatch.start();
/*     */         }
/* 647 */         if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
/*     */           
/* 649 */           stopwatch.stop();
/* 650 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 651 */             logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 657 */         if (to == Service.State.FAILED) {
/* 658 */           fireFailedListeners(service);
/*     */         }
/*     */         
/* 661 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
/*     */ 
/*     */           
/* 664 */           fireHealthyListeners();
/* 665 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 666 */           fireStoppedListeners();
/*     */         } 
/*     */       } finally {
/* 669 */         this.monitor.leave();
/*     */         
/* 671 */         executeListeners();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 677 */     void fireStoppedListeners() { STOPPED_CALLBACK.enqueueOn(this.listeners); }
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 682 */     void fireHealthyListeners() { HEALTHY_CALLBACK.enqueueOn(this.listeners); }
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 687 */     void fireFailedListeners(final Service service) { (new ListenerCallQueue.Callback<ServiceManager.Listener>("failed({service=" + service + "})")
/*     */         {
/* 689 */           void call(ServiceManager.Listener listener) { listener.failure(service); }
/*     */         }).enqueueOn(this.listeners); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void executeListeners() {
/* 696 */       Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */ 
/*     */       
/* 699 */       for (int i = 0; i < this.listeners.size(); i++) {
/* 700 */         ((ListenerCallQueue)this.listeners.get(i)).execute();
/*     */       }
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 706 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
/* 707 */         throw new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */ 
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */ 
/*     */ 
/*     */     
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state) {
/* 726 */       this.service = service;
/* 727 */       this.state = state;
/*     */     }
/*     */     
/*     */     public void starting() {
/* 731 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 732 */       if (state != null) {
/* 733 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 734 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 735 */           logger.log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void running() {
/* 741 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 742 */       if (state != null) {
/* 743 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */     
/*     */     public void stopping(Service.State from) {
/* 748 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 749 */       if (state != null) {
/* 750 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */     
/*     */     public void terminated(Service.State from) {
/* 755 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 756 */       if (state != null) {
/* 757 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 758 */           logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */         
/* 761 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {
/* 766 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 767 */       if (state != null) {
/*     */ 
/*     */         
/* 770 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 771 */           logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
/*     */         }
/*     */         
/* 774 */         state.transitionService(this.service, from, Service.State.FAILED);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     private NoOpService() {}
/*     */ 
/*     */ 
/*     */     
/* 788 */     protected void doStart() { notifyStarted(); }
/* 789 */     protected void doStop() { notifyStopped(); }
/*     */   }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning extends Throwable {
/*     */     private EmptyServiceManagerWarning() {}
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/ServiceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */