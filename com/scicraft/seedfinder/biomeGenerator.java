/*    */ package com.scicraft.seedfinder;
/*    */ 
/*    */ import minecraft.layer.GenLayer;
/*    */ import minecraft.layer.IntCache;
/*    */ 
/*    */ public class biomeGenerator {
/*    */   public GenLayer biomeIndexLayer;
/*    */   
/*    */   public biomeGenerator(long seed, int quarter) {
/* 10 */     if (quarter == 0) {
/* 11 */       this.biomeIndexLayer = GenLayer.func_180781_a(seed, "")[1];
/* 12 */     } else if (quarter == 1) {
/* 13 */       this.biomeIndexLayerquarter = GenLayer.func_180781_a(seed, "")[0];
/*    */     } else {
/*    */       
/* 16 */       this.biomeIndexLayer = GenLayer.func_180781_a(seed, null)[1];
/* 17 */       this.biomeIndexLayerquarter = GenLayer.func_180781_a(seed, "")[0];
/*    */     } 
/*    */   }
/*    */   
/*    */   public GenLayer biomeIndexLayerquarter;
/*    */   
/*    */   public int getBiomeAt(int x, int y) {
/* 24 */     IntCache.resetIntCache();
/* 25 */     return this.biomeIndexLayer.getInts(x, y, 1, 1)[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getBiomeData(int x, int y, int width, int height, boolean quarter) {
/* 30 */     IntCache.resetIntCache();
/* 31 */     if (quarter) {
/* 32 */       return this.biomeIndexLayerquarter.getInts(x, y, width, height);
/*    */     }
/* 34 */     return this.biomeIndexLayer.getInts(x, y, width, height);
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/biomeGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */