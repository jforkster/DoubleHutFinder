/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class ImmutableEnumSet<E extends Enum<E>>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   private final EnumSet<E> delegate;
/*     */   private int hashCode;
/*     */   
/*     */   static <E extends Enum<E>> ImmutableSet<E> asImmutable(EnumSet<E> set) {
/*  35 */     switch (set.size()) {
/*     */       case 0:
/*  37 */         return ImmutableSet.of();
/*     */       case 1:
/*  39 */         return ImmutableSet.of(Iterables.getOnlyElement(set));
/*     */     } 
/*  41 */     return new ImmutableEnumSet(set);
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
/*  56 */   private ImmutableEnumSet(EnumSet<E> delegate) { this.delegate = delegate; }
/*     */ 
/*     */ 
/*     */   
/*  60 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public UnmodifiableIterator<E> iterator() { return Iterators.unmodifiableIterator(this.delegate.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public int size() { return this.delegate.size(); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public boolean contains(Object object) { return this.delegate.contains(object); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public boolean containsAll(Collection<?> collection) { return this.delegate.containsAll(collection); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public boolean isEmpty() { return this.delegate.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public boolean equals(Object object) { return (object == this || this.delegate.equals(object)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     int result = this.hashCode;
/*  92 */     return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
/*     */   }
/*     */ 
/*     */   
/*  96 */   public String toString() { return this.delegate.toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   Object writeReplace() { return new EnumSerializedForm(this.delegate); }
/*     */ 
/*     */   
/*     */   private static class EnumSerializedForm<E extends Enum<E>>
/*     */     extends Object
/*     */     implements Serializable
/*     */   {
/*     */     final EnumSet<E> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 111 */     EnumSerializedForm(EnumSet<E> delegate) { this.delegate = delegate; }
/*     */ 
/*     */ 
/*     */     
/* 115 */     Object readResolve() { return new ImmutableEnumSet(this.delegate.clone(), null); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableEnumSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */