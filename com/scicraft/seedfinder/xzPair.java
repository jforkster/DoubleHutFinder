/*    */ package com.scicraft.seedfinder;
/*    */ public class xzPair {
/*    */   private int x;
/*    */   
/*    */   public xzPair(int x, int z) {
/*  6 */     this.x = x;
/*  7 */     this.z = z;
/*    */   }
/*    */   private int z;
/*    */   
/* 11 */   public int getX() { return this.x; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public int getZ() { return this.z; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public double dist(xzPair p) { return Math.sqrt(((p.x - this.x) * (p.x - this.x) + (p.z - this.z) * (p.z - this.z))); }
/*    */ 
/*    */   
/*    */   public xzPair mul(int i) {
/* 23 */     this.x *= i;
/* 24 */     this.z *= i;
/*    */     
/* 26 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 30 */   public String toString() { return this.x + "," + this.z; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/xzPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */