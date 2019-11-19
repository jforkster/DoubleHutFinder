/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public abstract class AbstractSequentialIterator<T>
/*    */   extends UnmodifiableIterator<T>
/*    */ {
/*    */   private T nextOrNull;
/*    */   
/* 53 */   protected AbstractSequentialIterator(@Nullable T firstOrNull) { this.nextOrNull = firstOrNull; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract T computeNext(T paramT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public final boolean hasNext() { return (this.nextOrNull != null); }
/*    */ 
/*    */ 
/*    */   
/*    */   public final T next() {
/* 71 */     if (!hasNext()) {
/* 72 */       throw new NoSuchElementException();
/*    */     }
/*    */     
/* 75 */     try { object = this.nextOrNull;
/*    */       
/* 77 */       return (T)object; } finally { this.nextOrNull = computeNext(this.nextOrNull); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractSequentialIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */