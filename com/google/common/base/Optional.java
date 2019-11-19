/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ public abstract class Optional<T>
/*     */   extends Object
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  78 */   public static <T> Optional<T> absent() { return Absent.withType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static <T> Optional<T> of(T reference) { return new Present(Preconditions.checkNotNull(reference)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static <T> Optional<T> fromNullable(@Nullable T nullableReference) { return (nullableReference == null) ? absent() : new Present(nullableReference); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isPresent();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract T get();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract T or(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Optional<T> or(Optional<? extends T> paramOptional);
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
/*     */   public abstract T or(Supplier<? extends T> paramSupplier);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public abstract T orNull();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Set<T> asSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract <V> Optional<V> transform(Function<? super T, V> paramFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean equals(@Nullable Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int hashCode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String toString();
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
/*     */   public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
/* 218 */     Preconditions.checkNotNull(optionals);
/* 219 */     return new Iterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/* 222 */           return new AbstractIterator<T>() {
/* 223 */               private final Iterator<? extends Optional<? extends T>> iterator = (Iterator)Preconditions.checkNotNull(Optional.null.this.val$optionals.iterator());
/*     */ 
/*     */ 
/*     */               
/*     */               protected T computeNext() {
/* 228 */                 while (this.iterator.hasNext()) {
/* 229 */                   Optional<? extends T> optional = (Optional)this.iterator.next();
/* 230 */                   if (optional.isPresent()) {
/* 231 */                     return (T)optional.get();
/*     */                   }
/*     */                 } 
/* 234 */                 return (T)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Optional.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */