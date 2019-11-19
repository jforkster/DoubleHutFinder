/*    */ package minecraft.layer;
/*    */ 
/*    */ import minecraft.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerDeepOcean
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerDeepOcean(long p_i45472_1_, GenLayer p_i45472_3_) {
/*  9 */     super(p_i45472_1_);
/* 10 */     this.parent = p_i45472_3_;
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
/* 30 */         int var13 = var9[var12 + 1 + (var11 + 1 - 1) * (areaWidth + 2)];
/* 31 */         int var14 = var9[var12 + 1 + 1 + (var11 + 1) * (areaWidth + 2)];
/* 32 */         int var15 = var9[var12 + 1 - 1 + (var11 + 1) * (areaWidth + 2)];
/* 33 */         int var16 = var9[var12 + 1 + (var11 + 1 + 1) * (areaWidth + 2)];
/* 34 */         int var17 = var9[var12 + 1 + (var11 + 1) * var7];
/* 35 */         int var18 = 0;
/*    */         
/* 37 */         if (var13 == 0)
/*    */         {
/* 39 */           var18++;
/*    */         }
/*    */         
/* 42 */         if (var14 == 0)
/*    */         {
/* 44 */           var18++;
/*    */         }
/*    */         
/* 47 */         if (var15 == 0)
/*    */         {
/* 49 */           var18++;
/*    */         }
/*    */         
/* 52 */         if (var16 == 0)
/*    */         {
/* 54 */           var18++;
/*    */         }
/*    */         
/* 57 */         if (var17 == 0 && var18 > 3) {
/*    */           
/* 59 */           var10[var12 + var11 * areaWidth] = BiomeGenBase.deepOcean.biomeID;
/*    */         }
/*    */         else {
/*    */           
/* 63 */           var10[var12 + var11 * areaWidth] = var17;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 68 */     return var10;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerDeepOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */