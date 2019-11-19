/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible
/*    */ final class Count
/*    */   implements Serializable
/*    */ {
/*    */   private int value;
/*    */   
/* 33 */   Count(int value) { this.value = value; }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public int get() { return this.value; }
/*    */ 
/*    */   
/*    */   public int getAndAdd(int delta) {
/* 41 */     int result = this.value;
/* 42 */     this.value = result + delta;
/* 43 */     return result;
/*    */   }
/*    */ 
/*    */   
/* 47 */   public int addAndGet(int delta) { return this.value += delta; }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void set(int newValue) { this.value = newValue; }
/*    */ 
/*    */   
/*    */   public int getAndSet(int newValue) {
/* 55 */     int result = this.value;
/* 56 */     this.value = newValue;
/* 57 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public int hashCode() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public boolean equals(@Nullable Object obj) { return (obj instanceof Count && ((Count)obj).value == this.value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public String toString() { return Integer.toString(this.value); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Count.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */