/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Interners
/*     */ {
/*     */   public static <E> Interner<E> newStrongInterner() {
/*  45 */     map = (new MapMaker()).makeMap();
/*  46 */     return new Interner<E>() {
/*     */         public E intern(E sample) {
/*  48 */           E canonical = (E)map.putIfAbsent(Preconditions.checkNotNull(sample), sample);
/*  49 */           return (canonical == null) ? sample : canonical;
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*  63 */   public static <E> Interner<E> newWeakInterner() { return new WeakInterner(null); }
/*     */   
/*     */   private static class WeakInterner<E>
/*     */     extends Object
/*     */     implements Interner<E> {
/*  68 */     private final MapMakerInternalMap<E, Dummy> map = (new MapMaker()).weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E intern(E sample) {
/*     */       Dummy sneaky;
/*     */       do {
/*  76 */         MapMakerInternalMap.ReferenceEntry<E, Dummy> entry = this.map.getEntry(sample);
/*  77 */         if (entry != null) {
/*  78 */           E canonical = (E)entry.getKey();
/*  79 */           if (canonical != null) {
/*  80 */             return canonical;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  85 */         sneaky = (Dummy)this.map.putIfAbsent(sample, Dummy.VALUE);
/*  86 */       } while (sneaky != null);
/*  87 */       return sample;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private WeakInterner() {}
/*     */ 
/*     */ 
/*     */     
/*     */     private enum Dummy
/*     */     {
/*  99 */       VALUE;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static <E> Function<E, E> asFunction(Interner<E> interner) { return new InternerFunction((Interner)Preconditions.checkNotNull(interner)); }
/*     */   
/*     */   private static class InternerFunction<E>
/*     */     extends Object
/*     */     implements Function<E, E>
/*     */   {
/*     */     private final Interner<E> interner;
/*     */     
/* 116 */     public InternerFunction(Interner<E> interner) { this.interner = interner; }
/*     */ 
/*     */ 
/*     */     
/* 120 */     public E apply(E input) { return (E)this.interner.intern(input); }
/*     */ 
/*     */ 
/*     */     
/* 124 */     public int hashCode() { return this.interner.hashCode(); }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 128 */       if (other instanceof InternerFunction) {
/* 129 */         InternerFunction<?> that = (InternerFunction)other;
/* 130 */         return this.interner.equals(that.interner);
/*     */       } 
/*     */       
/* 133 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Interners.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */