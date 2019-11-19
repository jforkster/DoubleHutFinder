/*    */ package com.scicraft.seedfinder;public class bitIterator {
/*    */   private long baseSeed;
/*    */   
/*    */   public bitIterator(long baseSeed) {
/*  5 */     this.baseEnd = 65536L;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 13 */     this.baseSeed = baseSeed & 0xFFFFFFFFFFFFL;
/* 14 */     this.current = 0L;
/*    */   }
/*    */   private long current; private long baseEnd;
/*    */   
/* 18 */   public boolean hasNext() { return (this.current < this.baseEnd); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long next() {
/* 26 */     if (hasNext()) {
/* 27 */       this.current++;
/* 28 */       return this.baseSeed ^ this.current << 48;
/*    */     } 
/* 30 */     return null.longValue();
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/bitIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */