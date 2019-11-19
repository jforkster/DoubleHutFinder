/*    */ package minecraft.layer;
/*    */ 
/*    */ import minecraft.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRiverMix
/*    */   extends GenLayer
/*    */ {
/*    */   private GenLayer biomePatternGeneratorChain;
/*    */   private GenLayer riverPatternGeneratorChain;
/*    */   
/*    */   public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_) {
/* 12 */     super(p_i2129_1_);
/* 13 */     this.biomePatternGeneratorChain = p_i2129_3_;
/* 14 */     this.riverPatternGeneratorChain = p_i2129_4_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initWorldGenSeed(long p_75905_1_) {
/* 23 */     this.biomePatternGeneratorChain.initWorldGenSeed(p_75905_1_);
/* 24 */     this.riverPatternGeneratorChain.initWorldGenSeed(p_75905_1_);
/* 25 */     super.initWorldGenSeed(p_75905_1_);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 34 */     int[] var5 = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
/* 35 */     int[] var6 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
/* 36 */     int[] var7 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 38 */     for (int var8 = 0; var8 < areaWidth * areaHeight; var8++) {
/*    */       
/* 40 */       if (var5[var8] != BiomeGenBase.ocean.biomeID && var5[var8] != BiomeGenBase.deepOcean.biomeID) {
/*    */         
/* 42 */         if (var6[var8] == BiomeGenBase.river.biomeID) {
/*    */           
/* 44 */           if (var5[var8] == BiomeGenBase.icePlains.biomeID)
/*    */           {
/* 46 */             var7[var8] = BiomeGenBase.frozenRiver.biomeID;
/*    */           }
/* 48 */           else if (var5[var8] != BiomeGenBase.mushroomIsland.biomeID && var5[var8] != BiomeGenBase.mushroomIslandShore.biomeID)
/*    */           {
/* 50 */             var7[var8] = var6[var8] & 0xFF;
/*    */           }
/*    */           else
/*    */           {
/* 54 */             var7[var8] = BiomeGenBase.mushroomIslandShore.biomeID;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 59 */           var7[var8] = var5[var8];
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 64 */         var7[var8] = var5[var8];
/*    */       } 
/*    */     } 
/*    */     
/* 68 */     return var7;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerRiverMix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */