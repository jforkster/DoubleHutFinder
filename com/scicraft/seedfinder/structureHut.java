/*    */ package com.scicraft.seedfinder;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class structureHut extends structure {
/*  6 */   private Random rnd = new Random();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public xzPair structurePosInRegion(long x, long z, long seed) {
/* 12 */     this.rnd.setSeed(x * 341873128712L + z * 132897987541L + seed + 14357620L);
/* 13 */     return new xzPair(this.rnd.nextInt(24), this.rnd.nextInt(24));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public xzPair structurePosInRegionFast(long xPart, long zPart, long seed, int lowerThen, int higherThen) {
/* 20 */     this.rnd.setSeed(xPart + zPart + seed + 14357620L);
/* 21 */     int xRand = this.rnd.nextInt(24);
/* 22 */     if (xRand <= lowerThen || xRand >= higherThen) {
/* 23 */       return new xzPair(xRand, this.rnd.nextInt(24));
/*    */     }
/* 25 */     return null;
/*    */   }
/*    */   
/*    */   public xzPair structurePosInRegionFast2(long xPart, long zPart, long seed, int lowerThen, int higherThen) {
/* 29 */     this.rnd.setSeed(xPart + zPart + seed + 14357620L);
/* 30 */     int xRand = this.rnd.nextInt(24);
/* 31 */     if (xRand <= lowerThen || xRand >= higherThen) {
/* 32 */       int zRand = this.rnd.nextInt(24);
/*    */       
/* 34 */       if (zRand <= lowerThen || zRand >= higherThen) {
/* 35 */         return new xzPair(xRand, zRand);
/*    */       }
/* 37 */       return null;
/*    */     } 
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean structureWillSpawn(int xRegion, int zRegion, int xRandom, int zRandom, biomeGenerator generator) {
/* 50 */     if (generator.getBiomeAt(xRegion * 512 + xRandom * 16 + 8, zRegion * 512 + zRandom * 16 + 8) == 6)
/* 51 */       return true; 
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/structureHut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */