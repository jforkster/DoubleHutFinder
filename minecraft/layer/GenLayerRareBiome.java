/*    */ package minecraft.layer;
/*    */ 
/*    */ import minecraft.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRareBiome
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRareBiome(long p_i45478_1_, GenLayer p_i45478_3_) {
/*  9 */     super(p_i45478_1_);
/* 10 */     this.parent = p_i45478_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 19 */     int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/* 20 */     int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 22 */     for (int var7 = 0; var7 < areaHeight; var7++) {
/*    */       
/* 24 */       for (int var8 = 0; var8 < areaWidth; var8++) {
/*    */         
/* 26 */         initChunkSeed((var8 + areaX), (var7 + areaY));
/* 27 */         int var9 = var5[var8 + 1 + (var7 + 1) * (areaWidth + 2)];
/*    */         
/* 29 */         if (nextInt(57) == 0) {
/*    */           
/* 31 */           if (var9 == BiomeGenBase.plains.biomeID)
/*    */           {
/* 33 */             var6[var8 + var7 * areaWidth] = BiomeGenBase.plains.biomeID + 128;
/*    */           }
/*    */           else
/*    */           {
/* 37 */             var6[var8 + var7 * areaWidth] = var9;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 42 */           var6[var8 + var7 * areaWidth] = var9;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return var6;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerRareBiome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */