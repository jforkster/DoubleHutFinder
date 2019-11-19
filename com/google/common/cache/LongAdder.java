/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class LongAdder
/*     */   extends Striped64
/*     */   implements Serializable, LongAddable
/*     */ {
/*     */   private static final long serialVersionUID = 7249069246863182397L;
/*     */   
/*  56 */   final long fn(long v, long x) { return v + x; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(long x) {
/*     */     Striped64.Cell[] arrayOfCell;
/*     */     long b;
/*  71 */     if ((arrayOfCell = this.cells) != null || !casBase(b = this.base, b + x)) {
/*  72 */       boolean uncontended = true; Striped64.HashCode hc;
/*  73 */       int h = (hc = (Striped64.HashCode)threadHashCode.get()).code; long v; Striped64.Cell a; int n;
/*  74 */       if (arrayOfCell == null || (n = arrayOfCell.length) < 1 || (a = arrayOfCell[n - true & h]) == null || !(uncontended = a.cas(v = a.value, v + x)))
/*     */       {
/*     */         
/*  77 */         retryUpdate(x, hc, uncontended);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void increment() { add(1L); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public void decrement() { add(-1L); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sum() {
/* 105 */     long sum = this.base;
/* 106 */     Striped64.Cell[] arrayOfCell = this.cells;
/* 107 */     if (arrayOfCell != null) {
/* 108 */       int n = arrayOfCell.length;
/* 109 */       for (int i = 0; i < n; i++) {
/* 110 */         Striped64.Cell a = arrayOfCell[i];
/* 111 */         if (a != null)
/* 112 */           sum += a.value; 
/*     */       } 
/*     */     } 
/* 115 */     return sum;
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
/* 126 */   public void reset() { internalReset(0L); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sumThenReset() {
/* 140 */     long sum = this.base;
/* 141 */     Striped64.Cell[] arrayOfCell = this.cells;
/* 142 */     this.base = 0L;
/* 143 */     if (arrayOfCell != null) {
/* 144 */       int n = arrayOfCell.length;
/* 145 */       for (int i = 0; i < n; i++) {
/* 146 */         Striped64.Cell a = arrayOfCell[i];
/* 147 */         if (a != null) {
/* 148 */           sum += a.value;
/* 149 */           a.value = 0L;
/*     */         } 
/*     */       } 
/*     */     } 
/* 153 */     return sum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public String toString() { return Long.toString(sum()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   public long longValue() { return sum(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public int intValue() { return (int)sum(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public float floatValue() { return (float)sum(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   public double doubleValue() { return sum(); }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 199 */     s.defaultWriteObject();
/* 200 */     s.writeLong(sum());
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 205 */     s.defaultReadObject();
/* 206 */     this.busy = 0;
/* 207 */     this.cells = null;
/* 208 */     this.base = s.readLong();
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/LongAdder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */