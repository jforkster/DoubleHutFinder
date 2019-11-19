/*     */ package com.google.common.cache;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Random;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Striped64
/*     */   extends Number
/*     */ {
/*     */   static final class Cell
/*     */   {
/*     */     private static final Unsafe UNSAFE;
/*     */     private static final long valueOffset;
/*     */     
/*  97 */     Cell(long x) { this.value = x; }
/*     */ 
/*     */     
/* 100 */     final boolean cas(long cmp, long val) { return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       try {
/* 108 */         UNSAFE = Striped64.getUnsafe();
/* 109 */         ak = Cell.class;
/* 110 */         valueOffset = UNSAFE.objectFieldOffset(ak.getDeclaredField("value"));
/*     */       }
/* 112 */       catch (Exception e) {
/* 113 */         throw new Error(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class HashCode
/*     */   {
/* 124 */     static final Random rng = new Random(); int code;
/*     */     
/*     */     HashCode() {
/* 127 */       int h = rng.nextInt();
/* 128 */       this.code = (h == 0) ? 1 : h;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ThreadHashCode
/*     */     extends ThreadLocal<HashCode>
/*     */   {
/* 136 */     public Striped64.HashCode initialValue() { return new Striped64.HashCode(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   static final ThreadHashCode threadHashCode = new ThreadHashCode();
/*     */ 
/*     */   
/* 148 */   static final int NCPU = Runtime.getRuntime().availableProcessors();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Unsafe UNSAFE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long baseOffset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long busyOffset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   final boolean casBase(long cmp, long val) { return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   final boolean casBusy() { return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void retryUpdate(long x, HashCode hc, boolean wasUncontended) {
/* 209 */     int h = hc.code;
/* 210 */     boolean collide = false; while (true) {
/*     */       Cell[] arrayOfCell;
/*     */       int n;
/* 213 */       if ((arrayOfCell = this.cells) != null && (n = arrayOfCell.length) > 0) {
/* 214 */         Cell a; if ((a = arrayOfCell[n - true & h]) == null)
/* 215 */         { if (this.busy == 0) {
/* 216 */             Cell r = new Cell(x);
/* 217 */             if (this.busy == 0 && casBusy()) {
/* 218 */               boolean created = false; try {
/*     */                 Cell[] arrayOfCell1; int m;
/*     */                 int j;
/* 221 */                 if ((arrayOfCell1 = this.cells) != null && (m = arrayOfCell1.length) > 0 && arrayOfCell1[j = m - true & h] == null) {
/*     */ 
/*     */                   
/* 224 */                   arrayOfCell1[j] = r;
/* 225 */                   created = true;
/*     */                 } 
/*     */               } finally {
/* 228 */                 this.busy = 0;
/*     */               } 
/* 230 */               if (created)
/*     */                 break; 
/*     */               continue;
/*     */             } 
/*     */           } 
/* 235 */           collide = false; }
/*     */         
/* 237 */         else if (!wasUncontended)
/* 238 */         { wasUncontended = true; }
/* 239 */         else { long v; if (a.cas(v = a.value, fn(v, x)))
/*     */             break; 
/* 241 */           if (n >= NCPU || this.cells != arrayOfCell) {
/* 242 */             collide = false;
/* 243 */           } else if (!collide) {
/* 244 */             collide = true;
/* 245 */           } else if (this.busy == 0 && casBusy()) {
/*     */             try {
/* 247 */               if (this.cells == arrayOfCell) {
/* 248 */                 Cell[] arrayOfCell1 = new Cell[n << 1];
/* 249 */                 for (int i = 0; i < n; i++)
/* 250 */                   arrayOfCell1[i] = arrayOfCell[i]; 
/* 251 */                 this.cells = arrayOfCell1;
/*     */               } 
/*     */             } finally {
/* 254 */               this.busy = 0;
/*     */             } 
/* 256 */             collide = false; continue;
/*     */           }  }
/*     */         
/* 259 */         h ^= h << 13;
/* 260 */         h ^= h >>> 17;
/* 261 */         h ^= h << 5; continue;
/*     */       } 
/* 263 */       if (this.busy == 0 && this.cells == arrayOfCell && casBusy()) {
/* 264 */         boolean init = false;
/*     */         try {
/* 266 */           if (this.cells == arrayOfCell) {
/* 267 */             Cell[] arrayOfCell1 = new Cell[2];
/* 268 */             arrayOfCell1[h & true] = new Cell(x);
/* 269 */             this.cells = arrayOfCell1;
/* 270 */             init = true;
/*     */           } 
/*     */         } finally {
/* 273 */           this.busy = 0;
/*     */         } 
/* 275 */         if (init)
/*     */           break;  continue;
/*     */       }  long v;
/* 278 */       if (casBase(v = this.base, fn(v, x)))
/*     */         break; 
/*     */     } 
/* 281 */     hc.code = h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void internalReset(long initialValue) {
/* 288 */     Cell[] arrayOfCell = this.cells;
/* 289 */     this.base = initialValue;
/* 290 */     if (arrayOfCell != null) {
/* 291 */       int n = arrayOfCell.length;
/* 292 */       for (int i = 0; i < n; i++) {
/* 293 */         Cell a = arrayOfCell[i];
/* 294 */         if (a != null) {
/* 295 */           a.value = initialValue;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static  {
/*     */     try {
/* 306 */       UNSAFE = getUnsafe();
/* 307 */       sk = Striped64.class;
/* 308 */       baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
/*     */       
/* 310 */       busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
/*     */     }
/* 312 */     catch (Exception e) {
/* 313 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Unsafe getUnsafe() {
/*     */     try {
/* 326 */       return Unsafe.getUnsafe();
/* 327 */     } catch (SecurityException e) {
/*     */       try {
/* 329 */         return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */             {
/*     */               public Unsafe run() {
/* 332 */                 Class<Unsafe> k = Unsafe.class;
/* 333 */                 for (Field f : k.getDeclaredFields()) {
/* 334 */                   f.setAccessible(true);
/* 335 */                   Object x = f.get(null);
/* 336 */                   if (k.isInstance(x))
/* 337 */                     return (Unsafe)k.cast(x); 
/*     */                 } 
/* 339 */                 throw new NoSuchFieldError("the Unsafe"); }
/*     */             });
/* 341 */       } catch (PrivilegedActionException tryReflectionInstead) {
/* 342 */         throw new RuntimeException("Could not initialize intrinsics", tryReflectionInstead.getCause());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract long fn(long paramLong1, long paramLong2);
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/Striped64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */