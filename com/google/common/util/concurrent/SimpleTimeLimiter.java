/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class SimpleTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   
/*  67 */   public SimpleTimeLimiter(ExecutorService executor) { this.executor = (ExecutorService)Preconditions.checkNotNull(executor); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public SimpleTimeLimiter() { this(Executors.newCachedThreadPool()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
/*  86 */     Preconditions.checkNotNull(target);
/*  87 */     Preconditions.checkNotNull(interfaceType);
/*  88 */     Preconditions.checkNotNull(timeoutUnit);
/*  89 */     Preconditions.checkArgument((timeoutDuration > 0L), "bad timeout: %s", new Object[] { Long.valueOf(timeoutDuration) });
/*  90 */     Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
/*     */ 
/*     */     
/*  93 */     final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
/*     */ 
/*     */     
/*  96 */     InvocationHandler handler = new InvocationHandler()
/*     */       {
/*     */         public Object invoke(Object obj, final Method method, final Object[] args) throws Throwable
/*     */         {
/* 100 */           Callable<Object> callable = new Callable<Object>()
/*     */             {
/*     */               public Object call() throws Exception {
/*     */                 try {
/* 104 */                   return method.invoke(target, args);
/* 105 */                 } catch (InvocationTargetException e) {
/* 106 */                   SimpleTimeLimiter.throwCause(e, false);
/* 107 */                   throw new AssertionError("can't get here");
/*     */                 } 
/*     */               }
/*     */             };
/* 111 */           return SimpleTimeLimiter.this.callWithTimeout(callable, timeoutDuration, timeoutUnit, interruptibleMethods.contains(method));
/*     */         }
/*     */       };
/*     */     
/* 115 */     return (T)newProxy(interfaceType, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
/* 122 */     Preconditions.checkNotNull(callable);
/* 123 */     Preconditions.checkNotNull(timeoutUnit);
/* 124 */     Preconditions.checkArgument((timeoutDuration > 0L), "timeout must be positive: %s", new Object[] { Long.valueOf(timeoutDuration) });
/*     */     
/* 126 */     Future<T> future = this.executor.submit(callable);
/*     */     try {
/* 128 */       if (amInterruptible) {
/*     */         try {
/* 130 */           return (T)future.get(timeoutDuration, timeoutUnit);
/* 131 */         } catch (InterruptedException e) {
/* 132 */           future.cancel(true);
/* 133 */           throw e;
/*     */         } 
/*     */       }
/* 136 */       return (T)Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     
/*     */     }
/* 139 */     catch (ExecutionException e) {
/* 140 */       throw throwCause(e, true);
/* 141 */     } catch (TimeoutException e) {
/* 142 */       future.cancel(true);
/* 143 */       throw new UncheckedTimeoutException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
/* 149 */     Throwable cause = e.getCause();
/* 150 */     if (cause == null) {
/* 151 */       throw e;
/*     */     }
/* 153 */     if (combineStackTraces) {
/* 154 */       StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat(cause.getStackTrace(), e.getStackTrace(), StackTraceElement.class);
/*     */       
/* 156 */       cause.setStackTrace(combined);
/*     */     } 
/* 158 */     if (cause instanceof Exception) {
/* 159 */       throw (Exception)cause;
/*     */     }
/* 161 */     if (cause instanceof Error) {
/* 162 */       throw (Error)cause;
/*     */     }
/*     */     
/* 165 */     throw e;
/*     */   }
/*     */   
/*     */   private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
/* 169 */     Set<Method> set = Sets.newHashSet();
/* 170 */     for (Method m : interfaceType.getMethods()) {
/* 171 */       if (declaresInterruptedEx(m)) {
/* 172 */         set.add(m);
/*     */       }
/*     */     } 
/* 175 */     return set;
/*     */   }
/*     */   
/*     */   private static boolean declaresInterruptedEx(Method method) {
/* 179 */     for (Class<?> exType : method.getExceptionTypes()) {
/*     */       
/* 181 */       if (exType == InterruptedException.class) {
/* 182 */         return true;
/*     */       }
/*     */     } 
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 191 */     Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
/*     */     
/* 193 */     return (T)interfaceType.cast(object);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/SimpleTimeLimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */