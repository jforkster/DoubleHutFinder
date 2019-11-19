/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class Present<T>
/*    */   extends Optional<T>
/*    */ {
/*    */   private final T reference;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 36 */   Present(T reference) { this.reference = reference; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean isPresent() { return true; }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public T get() { return (T)this.reference; }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 48 */     Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/* 49 */     return (T)this.reference;
/*    */   }
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 53 */     Preconditions.checkNotNull(secondChoice);
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 58 */     Preconditions.checkNotNull(supplier);
/* 59 */     return (T)this.reference;
/*    */   }
/*    */ 
/*    */   
/* 63 */   public T orNull() { return (T)this.reference; }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Set<T> asSet() { return Collections.singleton(this.reference); }
/*    */ 
/*    */ 
/*    */   
/* 71 */   public <V> Optional<V> transform(Function<? super T, V> function) { return new Present(Preconditions.checkNotNull(function.apply(this.reference), "the Function passed to Optional.transform() must not return null.")); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object object) {
/* 76 */     if (object instanceof Present) {
/* 77 */       Present<?> other = (Present)object;
/* 78 */       return this.reference.equals(other.reference);
/*    */     } 
/* 80 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 84 */   public int hashCode() { return 1502476572 + this.reference.hashCode(); }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public String toString() { return "Optional.of(" + this.reference + ")"; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Present.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */