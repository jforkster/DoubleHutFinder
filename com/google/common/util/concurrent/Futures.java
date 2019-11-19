/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class Futures
/*      */ {
/*   90 */   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<Exception, X> mapper) { return new MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(future), mapper); }
/*      */   
/*      */   private static abstract class ImmediateFuture<V>
/*      */     extends Object
/*      */     implements ListenableFuture<V>
/*      */   {
/*   96 */     private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
/*      */     
/*      */     private ImmediateFuture() {}
/*      */     
/*      */     public void addListener(Runnable listener, Executor executor) {
/*  101 */       Preconditions.checkNotNull(listener, "Runnable was null.");
/*  102 */       Preconditions.checkNotNull(executor, "Executor was null.");
/*      */       try {
/*  104 */         executor.execute(listener);
/*  105 */       } catch (RuntimeException e) {
/*      */ 
/*      */         
/*  108 */         log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  115 */     public boolean cancel(boolean mayInterruptIfRunning) { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(long timeout, TimeUnit unit) {
/*  123 */       Preconditions.checkNotNull(unit);
/*  124 */       return (V)get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  129 */     public boolean isCancelled() { return false; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  134 */     public boolean isDone() { return true; }
/*      */     
/*      */     public abstract V get() throws ExecutionException;
/*      */   }
/*      */   
/*      */   private static class ImmediateSuccessfulFuture<V>
/*      */     extends ImmediateFuture<V> {
/*      */     ImmediateSuccessfulFuture(@Nullable V value) {
/*  142 */       super(null);
/*  143 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  148 */     public V get() throws ExecutionException { return (V)this.value; }
/*      */     
/*      */     @Nullable
/*      */     private final V value; }
/*      */   
/*      */   private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> { @Nullable
/*      */     private final V value;
/*      */     
/*      */     ImmediateSuccessfulCheckedFuture(@Nullable V value) {
/*  157 */       super(null);
/*  158 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  163 */     public V get() throws ExecutionException { return (V)this.value; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  168 */     public V checkedGet() throws ExecutionException { return (V)this.value; }
/*      */ 
/*      */ 
/*      */     
/*      */     public V checkedGet(long timeout, TimeUnit unit) {
/*  173 */       Preconditions.checkNotNull(unit);
/*  174 */       return (V)this.value;
/*      */     } }
/*      */ 
/*      */   
/*      */   private static class ImmediateFailedFuture<V> extends ImmediateFuture<V> {
/*      */     private final Throwable thrown;
/*      */     
/*      */     ImmediateFailedFuture(Throwable thrown) {
/*  182 */       super(null);
/*  183 */       this.thrown = thrown;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  188 */     public V get() throws ExecutionException { throw new ExecutionException(this.thrown); }
/*      */   }
/*      */   
/*      */   private static class ImmediateCancelledFuture<V>
/*      */     extends ImmediateFuture<V> {
/*      */     private final CancellationException thrown;
/*      */     
/*      */     ImmediateCancelledFuture() {
/*  196 */       super(null);
/*  197 */       this.thrown = new CancellationException("Immediate cancelled future.");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  202 */     public boolean isCancelled() { return true; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  207 */     public V get() throws ExecutionException { throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown); }
/*      */   }
/*      */   
/*      */   private static class ImmediateFailedCheckedFuture<V, X extends Exception>
/*      */     extends ImmediateFuture<V>
/*      */     implements CheckedFuture<V, X>
/*      */   {
/*      */     private final X thrown;
/*      */     
/*      */     ImmediateFailedCheckedFuture(X thrown) {
/*  217 */       super(null);
/*  218 */       this.thrown = thrown;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  223 */     public V get() throws ExecutionException { throw new ExecutionException(this.thrown); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  228 */     public V checkedGet() throws X { throw this.thrown; }
/*      */ 
/*      */ 
/*      */     
/*      */     public V checkedGet(long timeout, TimeUnit unit) throws X {
/*  233 */       Preconditions.checkNotNull(unit);
/*  234 */       throw this.thrown;
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
/*  245 */   public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) { return new ImmediateSuccessfulFuture(value); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  258 */   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) { return new ImmediateSuccessfulCheckedFuture(value); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
/*  272 */     Preconditions.checkNotNull(throwable);
/*  273 */     return new ImmediateFailedFuture(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  283 */   public static <V> ListenableFuture<V> immediateCancelledFuture() { return new ImmediateCancelledFuture(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
/*  298 */     Preconditions.checkNotNull(exception);
/*  299 */     return new ImmediateFailedCheckedFuture(exception);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  377 */   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback) { return withFallback(input, fallback, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> withFallback(ListenableFuture<? extends V> input, FutureFallback<? extends V> fallback, Executor executor) {
/*  441 */     Preconditions.checkNotNull(fallback);
/*  442 */     return new FallbackFuture(input, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class FallbackFuture<V>
/*      */     extends AbstractFuture<V>
/*      */   {
/*      */     FallbackFuture(ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, Executor executor) {
/*  456 */       this.running = input;
/*  457 */       Futures.addCallback(this.running, new FutureCallback<V>()
/*      */           {
/*      */             public void onSuccess(V value) {
/*  460 */               super.this$0.set(value);
/*      */             }
/*      */ 
/*      */             
/*      */             public void onFailure(Throwable t) {
/*  465 */               if (super.this$0.isCancelled()) {
/*      */                 return;
/*      */               }
/*      */               try {
/*  469 */                 super.this$0.running = fallback.create(t);
/*  470 */                 if (super.this$0.isCancelled()) {
/*  471 */                   super.this$0.running.cancel(super.this$0.wasInterrupted());
/*      */                   return;
/*      */                 } 
/*  474 */                 Futures.addCallback(super.this$0.running, new FutureCallback<V>()
/*      */                     {
/*      */                       public void onSuccess(V value) {
/*  477 */                         Futures.FallbackFuture.null.this.this$0.set(value);
/*      */                       }
/*      */ 
/*      */                       
/*      */                       public void onFailure(Throwable t) {
/*  482 */                         if (Futures.FallbackFuture.null.this.this$0.running.isCancelled()) {
/*  483 */                           Futures.FallbackFuture.null.this.this$0.cancel(false);
/*      */                         } else {
/*  485 */                           Futures.FallbackFuture.null.this.this$0.setException(t);
/*      */                         } 
/*      */                       }
/*      */                     },  MoreExecutors.sameThreadExecutor());
/*  489 */               } catch (Throwable e) {
/*  490 */                 super.this$0.setException(e);
/*      */               } 
/*      */             }
/*      */           }executor);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  498 */       if (super.cancel(mayInterruptIfRunning)) {
/*  499 */         this.running.cancel(mayInterruptIfRunning);
/*  500 */         return true;
/*      */       } 
/*  502 */       return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  563 */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) { return transform(input, function, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  608 */     ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>(function, input, null);
/*      */     
/*  610 */     input.addListener(output, executor);
/*  611 */     return output;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  669 */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) { return transform(input, function, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, final Function<? super I, ? extends O> function, Executor executor) {
/*  711 */     Preconditions.checkNotNull(function);
/*  712 */     AsyncFunction<I, O> wrapperFunction = new AsyncFunction<I, O>()
/*      */       {
/*      */         public ListenableFuture<O> apply(I input) {
/*  715 */           O output = (O)function.apply(input);
/*  716 */           return Futures.immediateFuture(output);
/*      */         }
/*      */       };
/*  719 */     return transform(input, wrapperFunction, executor);
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
/*      */   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
/*  747 */     Preconditions.checkNotNull(input);
/*  748 */     Preconditions.checkNotNull(function);
/*  749 */     return new Future<O>()
/*      */       {
/*      */         public boolean cancel(boolean mayInterruptIfRunning)
/*      */         {
/*  753 */           return input.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  758 */         public boolean isCancelled() { return input.isCancelled(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  763 */         public boolean isDone() { return input.isDone(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  768 */         public O get() throws InterruptedException, ExecutionException { return (O)applyTransformation(input.get()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  774 */         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException { return (O)applyTransformation(input.get(timeout, unit)); }
/*      */ 
/*      */         
/*      */         private O applyTransformation(I input) throws ExecutionException {
/*      */           try {
/*  779 */             return (O)function.apply(input);
/*  780 */           } catch (Throwable t) {
/*  781 */             throw new ExecutionException(t);
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ChainingListenableFuture<I, O>
/*      */     extends AbstractFuture<O>
/*      */     implements Runnable
/*      */   {
/*      */     private AsyncFunction<? super I, ? extends O> function;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ListenableFuture<? extends I> inputFuture;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final CountDownLatch outputCreated;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> function, ListenableFuture<? extends I> inputFuture) {
/*  813 */       this.outputCreated = new CountDownLatch(1);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  818 */       this.function = (AsyncFunction)Preconditions.checkNotNull(function);
/*  819 */       this.inputFuture = (ListenableFuture)Preconditions.checkNotNull(inputFuture);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  828 */       if (super.cancel(mayInterruptIfRunning)) {
/*      */ 
/*      */         
/*  831 */         cancel(this.inputFuture, mayInterruptIfRunning);
/*  832 */         cancel(this.outputFuture, mayInterruptIfRunning);
/*  833 */         return true;
/*      */       } 
/*  835 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     private void cancel(@Nullable Future<?> future, boolean mayInterruptIfRunning) {
/*  840 */       if (future != null) {
/*  841 */         future.cancel(mayInterruptIfRunning);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*      */       try {
/*      */         I sourceResult;
/*      */         try {
/*  850 */           sourceResult = (I)Uninterruptibles.getUninterruptibly(this.inputFuture);
/*  851 */         } catch (CancellationException e) {
/*      */ 
/*      */ 
/*      */           
/*  855 */           cancel(false);
/*      */           return;
/*  857 */         } catch (ExecutionException e) {
/*      */           
/*  859 */           setException(e.getCause());
/*      */           
/*      */           return;
/*      */         } 
/*  863 */         final ListenableFuture<? extends O> outputFuture = this.outputFuture = (ListenableFuture)Preconditions.checkNotNull(this.function.apply(sourceResult), "AsyncFunction may not return null.");
/*      */ 
/*      */         
/*  866 */         if (isCancelled()) {
/*  867 */           outputFuture.cancel(wasInterrupted());
/*  868 */           this.outputFuture = null;
/*      */           return;
/*      */         } 
/*  871 */         outputFuture.addListener(new Runnable()
/*      */             {
/*      */               public void run() {
/*      */                 try {
/*  875 */                   super.this$0.set(Uninterruptibles.getUninterruptibly(outputFuture));
/*  876 */                 } catch (CancellationException e) {
/*      */ 
/*      */ 
/*      */                   
/*  880 */                   super.this$0.cancel(false);
/*      */                   return;
/*  882 */                 } catch (ExecutionException e) {
/*      */                   
/*  884 */                   super.this$0.setException(e.getCause());
/*      */                 } finally {
/*      */                   
/*  887 */                   super.this$0.outputFuture = null;
/*      */                 } 
/*      */               }
/*      */             },  MoreExecutors.sameThreadExecutor());
/*  891 */       } catch (UndeclaredThrowableException e) {
/*      */         
/*  893 */         setException(e.getCause());
/*  894 */       } catch (Throwable t) {
/*      */ 
/*      */         
/*  897 */         setException(t);
/*      */       } finally {
/*      */         
/*  900 */         this.function = null;
/*  901 */         this.inputFuture = null;
/*      */         
/*  903 */         this.outputCreated.countDown();
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
/*      */ 
/*      */   
/*  932 */   public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) { return transform(nested, DEREFERENCER); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  938 */   private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>()
/*      */     {
/*      */       public ListenableFuture<Object> apply(ListenableFuture<Object> input) {
/*  941 */         return input;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*  964 */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture... futures) { return listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  987 */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) { return listFuture(ImmutableList.copyOf(futures), true, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1001 */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) { return new NonCancellationPropagatingFuture(future); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture<V>
/*      */   {
/*      */     NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
/* 1010 */       Preconditions.checkNotNull(delegate);
/* 1011 */       Futures.addCallback(delegate, new FutureCallback<V>()
/*      */           {
/*      */             public void onSuccess(V result) {
/* 1014 */               super.this$0.set(result);
/*      */             }
/*      */ 
/*      */             
/*      */             public void onFailure(Throwable t) {
/* 1019 */               if (delegate.isCancelled()) {
/* 1020 */                 super.this$0.cancel(false);
/*      */               } else {
/* 1022 */                 super.this$0.setException(t);
/*      */               } 
/*      */             }
/*      */           },  MoreExecutors.sameThreadExecutor());
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
/*      */   @Beta
/* 1047 */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture... futures) { return listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1069 */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) { return listFuture(ImmutableList.copyOf(futures), false, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
/* 1091 */     final ConcurrentLinkedQueue<AsyncSettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
/*      */     
/* 1093 */     ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1104 */     SerializingExecutor executor = new SerializingExecutor(MoreExecutors.sameThreadExecutor());
/* 1105 */     for (ListenableFuture<? extends T> future : futures) {
/* 1106 */       AsyncSettableFuture<T> delegate = AsyncSettableFuture.create();
/*      */       
/* 1108 */       delegates.add(delegate);
/* 1109 */       future.addListener(new Runnable() {
/*      */             public void run() {
/* 1111 */               ((AsyncSettableFuture)delegates.remove()).setFuture(future);
/*      */             }
/*      */           },  executor);
/* 1114 */       listBuilder.add(delegate);
/*      */     } 
/* 1116 */     return listBuilder.build();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1170 */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) { addCallback(future, callback, MoreExecutors.sameThreadExecutor()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
/* 1212 */     Preconditions.checkNotNull(callback);
/* 1213 */     Runnable callbackListener = new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*      */           V value;
/*      */           
/*      */           try {
/* 1220 */             value = (V)Uninterruptibles.getUninterruptibly(future);
/* 1221 */           } catch (ExecutionException e) {
/* 1222 */             callback.onFailure(e.getCause());
/*      */             return;
/* 1224 */           } catch (RuntimeException e) {
/* 1225 */             callback.onFailure(e);
/*      */             return;
/* 1227 */           } catch (Error e) {
/* 1228 */             callback.onFailure(e);
/*      */             return;
/*      */           } 
/* 1231 */           callback.onSuccess(value);
/*      */         }
/*      */       };
/* 1234 */     future.addListener(callbackListener, executor);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> V get(Future<V> future, Class<X> exceptionClass) throws X {
/* 1286 */     Preconditions.checkNotNull(future);
/* 1287 */     Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", new Object[] { exceptionClass });
/*      */ 
/*      */     
/*      */     try {
/* 1291 */       return (V)future.get();
/* 1292 */     } catch (InterruptedException e) {
/* 1293 */       Thread.currentThread().interrupt();
/* 1294 */       throw newWithCause(exceptionClass, e);
/* 1295 */     } catch (ExecutionException e) {
/* 1296 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/* 1297 */       throw new AssertionError();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V, X extends Exception> V get(Future<V> future, long timeout, TimeUnit unit, Class<X> exceptionClass) throws X {
/* 1352 */     Preconditions.checkNotNull(future);
/* 1353 */     Preconditions.checkNotNull(unit);
/* 1354 */     Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", new Object[] { exceptionClass });
/*      */ 
/*      */     
/*      */     try {
/* 1358 */       return (V)future.get(timeout, unit);
/* 1359 */     } catch (InterruptedException e) {
/* 1360 */       Thread.currentThread().interrupt();
/* 1361 */       throw newWithCause(exceptionClass, e);
/* 1362 */     } catch (TimeoutException e) {
/* 1363 */       throw newWithCause(exceptionClass, e);
/* 1364 */     } catch (ExecutionException e) {
/* 1365 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/* 1366 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
/* 1372 */     if (cause instanceof Error) {
/* 1373 */       throw new ExecutionError((Error)cause);
/*      */     }
/* 1375 */     if (cause instanceof RuntimeException) {
/* 1376 */       throw new UncheckedExecutionException(cause);
/*      */     }
/* 1378 */     throw newWithCause(exceptionClass, cause);
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
/*      */   public static <V> V getUnchecked(Future<V> future) {
/* 1419 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1421 */       return (V)Uninterruptibles.getUninterruptibly(future);
/* 1422 */     } catch (ExecutionException e) {
/* 1423 */       wrapAndThrowUnchecked(e.getCause());
/* 1424 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1429 */     if (cause instanceof Error) {
/* 1430 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1437 */     throw new UncheckedExecutionException(cause);
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
/*      */   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
/* 1461 */     List<Constructor<X>> constructors = Arrays.asList(exceptionClass.getConstructors());
/*      */     
/* 1463 */     for (Constructor<X> constructor : preferringStrings(constructors)) {
/* 1464 */       X instance = (X)(Exception)newFromConstructor(constructor, cause);
/* 1465 */       if (instance != null) {
/* 1466 */         if (instance.getCause() == null) {
/* 1467 */           instance.initCause(cause);
/*      */         }
/* 1469 */         return instance;
/*      */       } 
/*      */     } 
/* 1472 */     throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1479 */   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors) { return WITH_STRING_PARAM_FIRST.sortedCopy(constructors); }
/*      */ 
/*      */   
/* 1482 */   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new Function<Constructor<?>, Boolean>()
/*      */       {
/*      */         public Boolean apply(Constructor<?> input) {
/* 1485 */           return Boolean.valueOf(Arrays.asList(input.getParameterTypes()).contains(String.class));
/*      */         }
/*      */       }).reverse();
/*      */   
/*      */   @Nullable
/*      */   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
/* 1491 */     Class[] paramTypes = constructor.getParameterTypes();
/* 1492 */     Object[] params = new Object[paramTypes.length];
/* 1493 */     for (i = 0; i < paramTypes.length; i++) {
/* 1494 */       Class<?> paramType = paramTypes[i];
/* 1495 */       if (paramType.equals(String.class)) {
/* 1496 */         params[i] = cause.toString();
/* 1497 */       } else if (paramType.equals(Throwable.class)) {
/* 1498 */         params[i] = cause;
/*      */       } else {
/* 1500 */         return null;
/*      */       } 
/*      */     } 
/*      */     try {
/* 1504 */       return (X)constructor.newInstance(params);
/* 1505 */     } catch (IllegalArgumentException i) {
/* 1506 */       IllegalArgumentException e; return null;
/* 1507 */     } catch (InstantiationException i) {
/* 1508 */       InstantiationException e; return null;
/* 1509 */     } catch (IllegalAccessException i) {
/* 1510 */       IllegalAccessException e; return null;
/* 1511 */     } catch (InvocationTargetException i) {
/* 1512 */       InvocationTargetException e; return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CombinedFuture<V, C>
/*      */     extends AbstractFuture<C>
/*      */   {
/* 1521 */     private static final Logger logger = Logger.getLogger(CombinedFuture.class.getName());
/*      */     
/*      */     ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
/*      */     
/*      */     final boolean allMustSucceed;
/*      */     final AtomicInteger remaining;
/*      */     
/*      */     CombinedFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor, Futures.FutureCombiner<V, C> combiner) {
/* 1529 */       this.seenExceptionsLock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1536 */       this.futures = futures;
/* 1537 */       this.allMustSucceed = allMustSucceed;
/* 1538 */       this.remaining = new AtomicInteger(futures.size());
/* 1539 */       this.combiner = combiner;
/* 1540 */       this.values = Lists.newArrayListWithCapacity(futures.size());
/* 1541 */       init(listenerExecutor);
/*      */     }
/*      */     Futures.FutureCombiner<V, C> combiner;
/*      */     List<Optional<V>> values;
/*      */     final Object seenExceptionsLock;
/*      */     Set<Throwable> seenExceptions;
/*      */     
/*      */     protected void init(Executor listenerExecutor) {
/* 1549 */       addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/* 1553 */               if (super.this$0.isCancelled()) {
/* 1554 */                 for (ListenableFuture<?> future : Futures.CombinedFuture.this.futures) {
/* 1555 */                   future.cancel(super.this$0.wasInterrupted());
/*      */                 }
/*      */               }
/*      */ 
/*      */               
/* 1560 */               Futures.CombinedFuture.this.futures = null;
/*      */ 
/*      */ 
/*      */               
/* 1564 */               Futures.CombinedFuture.this.values = null;
/*      */ 
/*      */               
/* 1567 */               Futures.CombinedFuture.this.combiner = null;
/*      */             }
/*      */           },  MoreExecutors.sameThreadExecutor());
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1574 */       if (this.futures.isEmpty()) {
/* 1575 */         set(this.combiner.combine(ImmutableList.of()));
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1580 */       for (int i = 0; i < this.futures.size(); i++) {
/* 1581 */         this.values.add(null);
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
/* 1592 */       int i = 0;
/* 1593 */       for (ListenableFuture<? extends V> listenable : this.futures) {
/* 1594 */         final int index = i++;
/* 1595 */         listenable.addListener(new Runnable()
/*      */             {
/*      */               public void run() {
/* 1598 */                 super.this$0.setOneValue(index, listenable);
/*      */               }
/*      */             }listenerExecutor);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setExceptionAndMaybeLog(Throwable throwable) {
/* 1611 */       boolean visibleFromOutputFuture = false;
/* 1612 */       boolean firstTimeSeeingThisException = true;
/* 1613 */       if (this.allMustSucceed) {
/*      */ 
/*      */         
/* 1616 */         visibleFromOutputFuture = setException(throwable);
/*      */         
/* 1618 */         synchronized (this.seenExceptionsLock) {
/* 1619 */           if (this.seenExceptions == null) {
/* 1620 */             this.seenExceptions = Sets.newHashSet();
/*      */           }
/* 1622 */           firstTimeSeeingThisException = this.seenExceptions.add(throwable);
/*      */         } 
/*      */       } 
/*      */       
/* 1626 */       if (throwable instanceof Error || (this.allMustSucceed && !visibleFromOutputFuture && firstTimeSeeingThisException))
/*      */       {
/* 1628 */         logger.log(Level.SEVERE, "input future failed.", throwable);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setOneValue(int index, Future<? extends V> future) {
/* 1636 */       localValues = this.values;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1644 */       if (isDone() || localValues == null)
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1649 */         Preconditions.checkState((this.allMustSucceed || isCancelled()), "Future was done before all dependencies completed");
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 1654 */         Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
/*      */         
/* 1656 */         V returnValue = (V)Uninterruptibles.getUninterruptibly(future);
/* 1657 */         if (localValues != null) {
/* 1658 */           localValues.set(index, Optional.fromNullable(returnValue));
/*      */         }
/* 1660 */       } catch (CancellationException e) {
/* 1661 */         if (this.allMustSucceed)
/*      */         {
/*      */           
/* 1664 */           cancel(false);
/*      */         }
/* 1666 */       } catch (ExecutionException e) {
/* 1667 */         setExceptionAndMaybeLog(e.getCause());
/* 1668 */       } catch (Throwable t) {
/* 1669 */         setExceptionAndMaybeLog(t);
/*      */       } finally {
/* 1671 */         int newRemaining = this.remaining.decrementAndGet();
/* 1672 */         Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
/* 1673 */         if (newRemaining == 0) {
/* 1674 */           Futures.FutureCombiner<V, C> localCombiner = this.combiner;
/* 1675 */           if (localCombiner != null && localValues != null) {
/* 1676 */             set(localCombiner.combine(localValues));
/*      */           } else {
/* 1678 */             Preconditions.checkState(isDone());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <V> ListenableFuture<List<V>> listFuture(ImmutableList<ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor) {
/* 1690 */     return new CombinedFuture(futures, allMustSucceed, listenerExecutor, new FutureCombiner<V, List<V>>()
/*      */         {
/*      */           
/*      */           public List<V> combine(List<Optional<V>> values)
/*      */           {
/* 1695 */             List<V> result = Lists.newArrayList();
/* 1696 */             for (Optional<V> element : values) {
/* 1697 */               result.add((element != null) ? element.orNull() : null);
/*      */             }
/* 1699 */             return Collections.unmodifiableList(result);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MappingCheckedFuture<V, X extends Exception>
/*      */     extends AbstractCheckedFuture<V, X>
/*      */   {
/*      */     final Function<Exception, X> mapper;
/*      */ 
/*      */ 
/*      */     
/*      */     MappingCheckedFuture(ListenableFuture<V> delegate, Function<Exception, X> mapper) {
/* 1715 */       super(delegate);
/*      */       
/* 1717 */       this.mapper = (Function)Preconditions.checkNotNull(mapper);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1722 */     protected X mapException(Exception e) { return (X)(Exception)this.mapper.apply(e); }
/*      */   }
/*      */   
/*      */   private static interface FutureCombiner<V, C> {
/*      */     C combine(List<Optional<V>> param1List);
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/Futures.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */