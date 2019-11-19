/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.primitives.Booleans;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible
/*     */ public abstract class ComparisonChain
/*     */ {
/*     */   private ComparisonChain() {}
/*     */   
/*  69 */   public static ComparisonChain start() { return ACTIVE; }
/*     */ 
/*     */   
/*  72 */   private static final ComparisonChain ACTIVE = new ComparisonChain()
/*     */     {
/*     */       public ComparisonChain compare(Comparable left, Comparable right)
/*     */       {
/*  76 */         return classify(left.compareTo(right));
/*     */       }
/*     */ 
/*     */       
/*  80 */       public <T> ComparisonChain compare(@Nullable T left, @Nullable T right, Comparator<T> comparator) { return classify(comparator.compare(left, right)); }
/*     */ 
/*     */       
/*  83 */       public ComparisonChain compare(int left, int right) { return classify(Ints.compare(left, right)); }
/*     */ 
/*     */       
/*  86 */       public ComparisonChain compare(long left, long right) { return classify(Longs.compare(left, right)); }
/*     */ 
/*     */       
/*  89 */       public ComparisonChain compare(float left, float right) { return classify(Float.compare(left, right)); }
/*     */ 
/*     */       
/*  92 */       public ComparisonChain compare(double left, double right) { return classify(Double.compare(left, right)); }
/*     */ 
/*     */       
/*  95 */       public ComparisonChain compareTrueFirst(boolean left, boolean right) { return classify(Booleans.compare(right, left)); }
/*     */ 
/*     */       
/*  98 */       public ComparisonChain compareFalseFirst(boolean left, boolean right) { return classify(Booleans.compare(left, right)); }
/*     */ 
/*     */       
/* 101 */       ComparisonChain classify(int result) { return (result < 0) ? LESS : ((result > 0) ? GREATER : ACTIVE); }
/*     */ 
/*     */       
/* 104 */       public int result() { return 0; }
/*     */     };
/*     */ 
/*     */   
/* 108 */   private static final ComparisonChain LESS = new InactiveComparisonChain(-1); public abstract ComparisonChain compare(Comparable<?> paramComparable1, Comparable<?> paramComparable2); public abstract <T> ComparisonChain compare(@Nullable T paramT1, @Nullable T paramT2, Comparator<T> paramComparator); public abstract ComparisonChain compare(int paramInt1, int paramInt2); public abstract ComparisonChain compare(long paramLong1, long paramLong2);
/*     */   public abstract ComparisonChain compare(float paramFloat1, float paramFloat2);
/* 110 */   private static final ComparisonChain GREATER = new InactiveComparisonChain(1); public abstract ComparisonChain compare(double paramDouble1, double paramDouble2);
/*     */   public abstract ComparisonChain compareTrueFirst(boolean paramBoolean1, boolean paramBoolean2);
/*     */   public abstract ComparisonChain compareFalseFirst(boolean paramBoolean1, boolean paramBoolean2);
/*     */   public abstract int result();
/*     */   private static final class InactiveComparisonChain extends ComparisonChain { InactiveComparisonChain(int result) {
/* 115 */       super(null);
/* 116 */       this.result = result;
/*     */     }
/*     */     final int result;
/*     */     
/* 120 */     public ComparisonChain compare(@Nullable Comparable left, @Nullable Comparable right) { return this; }
/*     */ 
/*     */ 
/*     */     
/* 124 */     public <T> ComparisonChain compare(@Nullable T left, @Nullable T right, @Nullable Comparator<T> comparator) { return this; }
/*     */ 
/*     */     
/* 127 */     public ComparisonChain compare(int left, int right) { return this; }
/*     */ 
/*     */     
/* 130 */     public ComparisonChain compare(long left, long right) { return this; }
/*     */ 
/*     */     
/* 133 */     public ComparisonChain compare(float left, float right) { return this; }
/*     */ 
/*     */     
/* 136 */     public ComparisonChain compare(double left, double right) { return this; }
/*     */ 
/*     */     
/* 139 */     public ComparisonChain compareTrueFirst(boolean left, boolean right) { return this; }
/*     */ 
/*     */     
/* 142 */     public ComparisonChain compareFalseFirst(boolean left, boolean right) { return this; }
/*     */ 
/*     */     
/* 145 */     public int result() { return this.result; } }
/*     */ 
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ComparisonChain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */