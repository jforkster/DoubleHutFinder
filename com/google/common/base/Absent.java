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
/*    */ final class Absent<T>
/*    */   extends Optional<T>
/*    */ {
/* 33 */   static final Absent<Object> INSTANCE = new Absent();
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 37 */   static <T> Optional<T> withType() { return INSTANCE; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   public boolean isPresent() { return false; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public T get() { throw new IllegalStateException("Optional.get() cannot be called on an absent value"); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public T or(T defaultValue) { return (T)Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Optional<T> or(Optional<? extends T> secondChoice) { return (Optional)Preconditions.checkNotNull(secondChoice); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public T or(Supplier<? extends T> supplier) { return (T)Preconditions.checkNotNull(supplier.get(), "use Optional.orNull() instead of a Supplier that returns null"); }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/* 65 */   public T orNull() { return null; }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public Set<T> asSet() { return Collections.emptySet(); }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 73 */     Preconditions.checkNotNull(function);
/* 74 */     return Optional.absent();
/*    */   }
/*    */ 
/*    */   
/* 78 */   public boolean equals(@Nullable Object object) { return (object == this); }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public int hashCode() { return 1502476572; }
/*    */ 
/*    */ 
/*    */   
/* 86 */   public String toString() { return "Optional.absent()"; }
/*    */ 
/*    */ 
/*    */   
/* 90 */   private Object readResolve() { return INSTANCE; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Absent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */