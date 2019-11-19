/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible
/*     */ public final class Functions
/*     */ {
/*  56 */   public static Function<Object, String> toStringFunction() { return ToStringFunction.INSTANCE; }
/*     */   
/*     */   private enum ToStringFunction
/*     */     implements Function<Object, String>
/*     */   {
/*  61 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public String apply(Object o) {
/*  65 */       Preconditions.checkNotNull(o);
/*  66 */       return o.toString();
/*     */     }
/*     */ 
/*     */     
/*  70 */     public String toString() { return "toString"; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static <E> Function<E, E> identity() { return IdentityFunction.INSTANCE; }
/*     */   
/*     */   private enum IdentityFunction
/*     */     implements Function<Object, Object>
/*     */   {
/*  85 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*  90 */     public Object apply(@Nullable Object o) { return o; }
/*     */ 
/*     */ 
/*     */     
/*  94 */     public String toString() { return "identity"; }
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
/* 108 */   public static <K, V> Function<K, V> forMap(Map<K, V> map) { return new FunctionForMapNoDefault(map); }
/*     */   
/*     */   private static class FunctionForMapNoDefault<K, V>
/*     */     extends Object implements Function<K, V>, Serializable {
/*     */     final Map<K, V> map;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 115 */     FunctionForMapNoDefault(Map<K, V> map) { this.map = (Map)Preconditions.checkNotNull(map); }
/*     */ 
/*     */ 
/*     */     
/*     */     public V apply(@Nullable K key) {
/* 120 */       V result = (V)this.map.get(key);
/* 121 */       Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", new Object[] { key });
/* 122 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 126 */       if (o instanceof FunctionForMapNoDefault) {
/* 127 */         FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault)o;
/* 128 */         return this.map.equals(that.map);
/*     */       } 
/* 130 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 134 */     public int hashCode() { return this.map.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 138 */     public String toString() { return "forMap(" + this.map + ")"; }
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
/* 155 */   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V defaultValue) { return new ForMapWithDefault(map, defaultValue); }
/*     */   
/*     */   private static class ForMapWithDefault<K, V> extends Object implements Function<K, V>, Serializable {
/*     */     final Map<K, ? extends V> map;
/*     */     final V defaultValue;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ForMapWithDefault(Map<K, ? extends V> map, @Nullable V defaultValue) {
/* 163 */       this.map = (Map)Preconditions.checkNotNull(map);
/* 164 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public V apply(@Nullable K key) {
/* 169 */       V result = (V)this.map.get(key);
/* 170 */       return (result != null || this.map.containsKey(key)) ? result : this.defaultValue;
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 174 */       if (o instanceof ForMapWithDefault) {
/* 175 */         ForMapWithDefault<?, ?> that = (ForMapWithDefault)o;
/* 176 */         return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
/*     */       } 
/* 178 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 182 */     public int hashCode() { return Objects.hashCode(new Object[] { this.map, this.defaultValue }); }
/*     */ 
/*     */ 
/*     */     
/* 186 */     public String toString() { return "forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")"; }
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
/* 202 */   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) { return new FunctionComposition(g, f); }
/*     */   
/*     */   private static class FunctionComposition<A, B, C> extends Object implements Function<A, C>, Serializable {
/*     */     private final Function<B, C> g;
/*     */     private final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
/* 210 */       this.g = (Function)Preconditions.checkNotNull(g);
/* 211 */       this.f = (Function)Preconditions.checkNotNull(f);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 216 */     public C apply(@Nullable A a) { return (C)this.g.apply(this.f.apply(a)); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 220 */       if (obj instanceof FunctionComposition) {
/* 221 */         FunctionComposition<?, ?, ?> that = (FunctionComposition)obj;
/* 222 */         return (this.f.equals(that.f) && this.g.equals(that.g));
/*     */       } 
/* 224 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 228 */     public int hashCode() { return this.f.hashCode() ^ this.g.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 232 */     public String toString() { return this.g + "(" + this.f + ")"; }
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
/* 245 */   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) { return new PredicateFunction(predicate, null); }
/*     */   
/*     */   private static class PredicateFunction<T>
/*     */     extends Object
/*     */     implements Function<T, Boolean>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 253 */     private PredicateFunction(Predicate<T> predicate) { this.predicate = (Predicate)Preconditions.checkNotNull(predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     public Boolean apply(@Nullable T t) { return Boolean.valueOf(this.predicate.apply(t)); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 262 */       if (obj instanceof PredicateFunction) {
/* 263 */         PredicateFunction<?> that = (PredicateFunction)obj;
/* 264 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 270 */     public int hashCode() { return this.predicate.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 274 */     public String toString() { return "forPredicate(" + this.predicate + ")"; }
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
/* 287 */   public static <E> Function<Object, E> constant(@Nullable E value) { return new ConstantFunction(value); }
/*     */   
/*     */   private static class ConstantFunction<E>
/*     */     extends Object implements Function<Object, E>, Serializable {
/*     */     private final E value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 294 */     public ConstantFunction(@Nullable E value) { this.value = value; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     public E apply(@Nullable Object from) { return (E)this.value; }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 303 */       if (obj instanceof ConstantFunction) {
/* 304 */         ConstantFunction<?> that = (ConstantFunction)obj;
/* 305 */         return Objects.equal(this.value, that.value);
/*     */       } 
/* 307 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 311 */     public int hashCode() { return (this.value == null) ? 0 : this.value.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 315 */     public String toString() { return "constant(" + this.value + ")"; }
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
/*     */   @Beta
/* 329 */   public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) { return new SupplierFunction(supplier, null); }
/*     */   
/*     */   private static class SupplierFunction<T>
/*     */     extends Object
/*     */     implements Function<Object, T>, Serializable
/*     */   {
/*     */     private final Supplier<T> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 338 */     private SupplierFunction(Supplier<T> supplier) { this.supplier = (Supplier)Preconditions.checkNotNull(supplier); }
/*     */ 
/*     */ 
/*     */     
/* 342 */     public T apply(@Nullable Object input) { return (T)this.supplier.get(); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 346 */       if (obj instanceof SupplierFunction) {
/* 347 */         SupplierFunction<?> that = (SupplierFunction)obj;
/* 348 */         return this.supplier.equals(that.supplier);
/*     */       } 
/* 350 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 354 */     public int hashCode() { return this.supplier.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 358 */     public String toString() { return "forSupplier(" + this.supplier + ")"; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Functions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */