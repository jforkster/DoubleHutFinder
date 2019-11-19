/*    */ package minecraft.layer;
/*    */ 
/*    */ import minecraft.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerAddMushroomIsland
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerAddMushroomIsland(long p_i2120_1_, GenLayer p_i2120_3_) {
/*  9 */     super(p_i2120_1_);
/* 10 */     this.parent = p_i2120_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 19 */     int var5 = areaX - 1;
/* 20 */     int var6 = areaY - 1;
/* 21 */     int var7 = areaWidth + 2;
/* 22 */     int var8 = areaHeight + 2;
/* 23 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/* 24 */     int[] var10 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 26 */     for (int var11 = 0; var11 < areaHeight; var11++) {
/*    */       
/* 28 */       for (int var12 = 0; var12 < areaWidth; var12++) {
/*    */         
/* 30 */         int var13 = var9[var12 + 0 + (var11 + 0) * var7];
/* 31 */         int var14 = var9[var12 + 2 + (var11 + 0) * var7];
/* 32 */         int var15 = var9[var12 + 0 + (var11 + 2) * var7];
/* 33 */         int var16 = var9[var12 + 2 + (var11 + 2) * var7];
/* 34 */         int var17 = var9[var12 + 1 + (var11 + 1) * var7];
/* 35 */         initChunkSeed((var12 + areaX), (var11 + areaY));
/*    */         
/* 37 */         if (var17 == 0 && var13 == 0 && var14 == 0 && var15 == 0 && var16 == 0 && nextInt(100) == 0) {
/*    */           
/* 39 */           var10[var12 + var11 * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
/*    */         }
/*    */         else {
/*    */           
/* 43 */           var10[var12 + var11 * areaWidth] = var17;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     return var10;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerAddMushroomIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */