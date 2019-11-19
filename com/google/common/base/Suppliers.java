/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @GwtCompatible
/*     */ public final class Suppliers
/*     */ {
/*     */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
/*  51 */     Preconditions.checkNotNull(function);
/*  52 */     Preconditions.checkNotNull(supplier);
/*  53 */     return new SupplierComposition(function, supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierComposition<F, T> extends Object implements Supplier<T>, Serializable {
/*     */     final Function<? super F, T> function;
/*     */     final Supplier<F> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
/*  62 */       this.function = function;
/*  63 */       this.supplier = supplier;
/*     */     }
/*     */ 
/*     */     
/*  67 */     public T get() { return (T)this.function.apply(this.supplier.get()); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/*  71 */       if (obj instanceof SupplierComposition) {
/*  72 */         SupplierComposition<?, ?> that = (SupplierComposition)obj;
/*  73 */         return (this.function.equals(that.function) && this.supplier.equals(that.supplier));
/*     */       } 
/*  75 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  79 */     public int hashCode() { return Objects.hashCode(new Object[] { this.function, this.supplier }); }
/*     */ 
/*     */ 
/*     */     
/*  83 */     public String toString() { return "Suppliers.compose(" + this.function + ", " + this.supplier + ")"; }
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
/* 103 */   public static <T> Supplier<T> memoize(Supplier<T> delegate) { return (delegate instanceof MemoizingSupplier) ? delegate : new MemoizingSupplier((Supplier)Preconditions.checkNotNull(delegate)); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T>
/*     */     extends Object
/*     */     implements Supplier<T>, Serializable
/*     */   {
/*     */     final Supplier<T> delegate;
/*     */     
/*     */     T value;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 117 */     MemoizingSupplier(Supplier<T> delegate) { this.delegate = delegate; }
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 122 */       if (!this.initialized) {
/* 123 */         synchronized (this) {
/* 124 */           if (!this.initialized) {
/* 125 */             T t = (T)this.delegate.get();
/* 126 */             this.value = t;
/* 127 */             this.initialized = true;
/* 128 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 132 */       return (T)this.value;
/*     */     }
/*     */ 
/*     */     
/* 136 */     public String toString() { return "Suppliers.memoize(" + this.delegate + ")"; }
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
/* 162 */   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) { return new ExpiringMemoizingSupplier(delegate, duration, unit); }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ExpiringMemoizingSupplier<T>
/*     */     extends Object
/*     */     implements Supplier<T>, Serializable
/*     */   {
/*     */     final Supplier<T> delegate;
/*     */     final long durationNanos;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 175 */       this.delegate = (Supplier)Preconditions.checkNotNull(delegate);
/* 176 */       this.durationNanos = unit.toNanos(duration);
/* 177 */       Preconditions.checkArgument((duration > 0L));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 187 */       long nanos = this.expirationNanos;
/* 188 */       long now = Platform.systemNanoTime();
/* 189 */       if (nanos == 0L || now - nanos >= 0L) {
/* 190 */         synchronized (this) {
/* 191 */           if (nanos == this.expirationNanos) {
/* 192 */             T t = (T)this.delegate.get();
/* 193 */             this.value = t;
/* 194 */             nanos = now + this.durationNanos;
/*     */ 
/*     */             
/* 197 */             this.expirationNanos = (nanos == 0L) ? 1L : nanos;
/* 198 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 202 */       return (T)this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     public String toString() { return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)"; }
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
/* 219 */   public static <T> Supplier<T> ofInstance(@Nullable T instance) { return new SupplierOfInstance(instance); }
/*     */   
/*     */   private static class SupplierOfInstance<T>
/*     */     extends Object
/*     */     implements Supplier<T>, Serializable {
/*     */     final T instance;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 227 */     SupplierOfInstance(@Nullable T instance) { this.instance = instance; }
/*     */ 
/*     */ 
/*     */     
/* 231 */     public T get() { return (T)this.instance; }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 235 */       if (obj instanceof SupplierOfInstance) {
/* 236 */         SupplierOfInstance<?> that = (SupplierOfInstance)obj;
/* 237 */         return Objects.equal(this.instance, that.instance);
/*     */       } 
/* 239 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 243 */     public int hashCode() { return Objects.hashCode(new Object[] { this.instance }); }
/*     */ 
/*     */ 
/*     */     
/* 247 */     public String toString() { return "Suppliers.ofInstance(" + this.instance + ")"; }
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
/* 258 */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) { return new ThreadSafeSupplier((Supplier)Preconditions.checkNotNull(delegate)); }
/*     */   
/*     */   private static class ThreadSafeSupplier<T>
/*     */     extends Object
/*     */     implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 266 */     ThreadSafeSupplier(Supplier<T> delegate) { this.delegate = delegate; }
/*     */ 
/*     */     
/*     */     public T get() {
/* 270 */       synchronized (this.delegate) {
/* 271 */         return (T)this.delegate.get();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 276 */     public String toString() { return "Suppliers.synchronizedSupplier(" + this.delegate + ")"; }
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
/*     */   @Beta
/* 291 */   public static <T> Function<Supplier<T>, T> supplierFunction() { return SupplierFunctionImpl.INSTANCE; }
/*     */   
/*     */   private static interface SupplierFunction<T>
/*     */     extends Function<Supplier<T>, T> {}
/*     */   
/*     */   private enum SupplierFunctionImpl
/*     */     implements SupplierFunction<Object> {
/* 298 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/* 302 */     public Object apply(Supplier<Object> input) { return input.get(); }
/*     */ 
/*     */ 
/*     */     
/* 306 */     public String toString() { return "Suppliers.supplierFunction()"; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Suppliers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */