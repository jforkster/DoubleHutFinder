/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.ListenableFutureTask;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class CacheLoader<K, V>
/*     */   extends Object
/*     */ {
/*     */   public abstract V load(K paramK) throws Exception;
/*     */   
/*     */   @GwtIncompatible("Futures")
/*     */   public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
/*  95 */     Preconditions.checkNotNull(key);
/*  96 */     Preconditions.checkNotNull(oldValue);
/*  97 */     return Futures.immediateFuture(load(key));
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
/* 125 */   public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception { throw new UnsupportedLoadingOperationException(); }
/*     */ 
/*     */ 
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
/* 138 */   public static <K, V> CacheLoader<K, V> from(Function<K, V> function) { return new FunctionToCacheLoader(function); }
/*     */   
/*     */   private static final class FunctionToCacheLoader<K, V>
/*     */     extends CacheLoader<K, V>
/*     */     implements Serializable {
/*     */     private final Function<K, V> computingFunction;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 146 */     public FunctionToCacheLoader(Function<K, V> computingFunction) { this.computingFunction = (Function)Preconditions.checkNotNull(computingFunction); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     public V load(K key) throws Exception { return (V)this.computingFunction.apply(Preconditions.checkNotNull(key)); }
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
/*     */   @Beta
/* 168 */   public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) { return new SupplierToCacheLoader(supplier); }
/*     */ 
/*     */ 
/*     */ 
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
/*     */   @GwtIncompatible("Executor + Futures")
/*     */   public static <K, V> CacheLoader<K, V> asyncReloading(final CacheLoader<K, V> loader, final Executor executor) {
/* 184 */     Preconditions.checkNotNull(loader);
/* 185 */     Preconditions.checkNotNull(executor);
/* 186 */     return new CacheLoader<K, V>()
/*     */       {
/*     */         public V load(K key) throws Exception {
/* 189 */           return (V)loader.load(key);
/*     */         }
/*     */ 
/*     */         
/*     */         public ListenableFuture<V> reload(final K key, final V oldValue) throws Exception {
/* 194 */           ListenableFutureTask<V> task = ListenableFutureTask.create(new Callable<V>()
/*     */               {
/*     */                 public V call() throws Exception {
/* 197 */                   return (V)loader.reload(key, oldValue).get();
/*     */                 }
/*     */               });
/* 200 */           executor.execute(task);
/* 201 */           return task;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 206 */         public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception { return loader.loadAll(keys); }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class SupplierToCacheLoader<V>
/*     */     extends CacheLoader<Object, V>
/*     */     implements Serializable {
/*     */     private final Supplier<V> computingSupplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 216 */     public SupplierToCacheLoader(Supplier<V> computingSupplier) { this.computingSupplier = (Supplier)Preconditions.checkNotNull(computingSupplier); }
/*     */ 
/*     */ 
/*     */     
/*     */     public V load(Object key) {
/* 221 */       Preconditions.checkNotNull(key);
/* 222 */       return (V)this.computingSupplier.get();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class UnsupportedLoadingOperationException
/*     */     extends UnsupportedOperationException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class InvalidCacheLoadException
/*     */     extends RuntimeException
/*     */   {
/* 237 */     public InvalidCacheLoadException(String message) { super(message); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/CacheLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */