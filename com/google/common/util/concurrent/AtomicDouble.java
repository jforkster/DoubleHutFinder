/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AtomicDouble
/*     */   extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*  60 */   private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public AtomicDouble(double initialValue) { this.value = Double.doubleToRawLongBits(initialValue); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public final double get() { return Double.longBitsToDouble(this.value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(double newValue) {
/*  94 */     long next = Double.doubleToRawLongBits(newValue);
/*  95 */     this.value = next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public final void lazySet(double newValue) { set(newValue); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getAndSet(double newValue) {
/* 117 */     long next = Double.doubleToRawLongBits(newValue);
/* 118 */     return Double.longBitsToDouble(updater.getAndSet(this, next));
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
/* 132 */   public final boolean compareAndSet(double expect, double update) { return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 153 */   public final boolean weakCompareAndSet(double expect, double update) { return updater.weakCompareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getAndAdd(double delta) {
/*     */     long next;
/*     */     double currentVal;
/*     */     long current;
/*     */     do {
/* 166 */       current = this.value;
/* 167 */       currentVal = Double.longBitsToDouble(current);
/* 168 */       double nextVal = currentVal + delta;
/* 169 */       next = Double.doubleToRawLongBits(nextVal);
/* 170 */     } while (!updater.compareAndSet(this, current, next));
/* 171 */     return currentVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double addAndGet(double delta) {
/*     */     long next;
/*     */     double nextVal;
/*     */     long current;
/*     */     do {
/* 184 */       current = this.value;
/* 185 */       double currentVal = Double.longBitsToDouble(current);
/* 186 */       nextVal = currentVal + delta;
/* 187 */       next = Double.doubleToRawLongBits(nextVal);
/* 188 */     } while (!updater.compareAndSet(this, current, next));
/* 189 */     return nextVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public String toString() { return Double.toString(get()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public int intValue() { return (int)get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   public long longValue() { return (long)get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 223 */   public float floatValue() { return (float)get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 230 */   public double doubleValue() { return get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 240 */     s.defaultWriteObject();
/*     */     
/* 242 */     s.writeDouble(get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 250 */     s.defaultReadObject();
/*     */     
/* 252 */     set(s.readDouble());
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/util/concurrent/AtomicDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */