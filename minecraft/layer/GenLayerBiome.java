/*    */ package minecraft.layer;
/*    */ 
/*    */ import minecraft.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerBiome
/*    */   extends GenLayer
/*    */ {
/*    */   private BiomeGenBase[] field_151623_c;
/*    */   private BiomeGenBase[] field_151621_d;
/*    */   private BiomeGenBase[] field_151622_e;
/*    */   private BiomeGenBase[] field_151620_f;
/*    */   
/*    */   public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, String p_i45560_5_) {
/* 14 */     super(p_i45560_1_);
/* 15 */     this.field_151623_c = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna, BiomeGenBase.savanna, BiomeGenBase.plains };
/* 16 */     this.field_151621_d = new BiomeGenBase[] { BiomeGenBase.forest, BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.plains, BiomeGenBase.birchForest, BiomeGenBase.swampland };
/* 17 */     this.field_151622_e = new BiomeGenBase[] { BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.taiga, BiomeGenBase.plains };
/* 18 */     this.field_151620_f = new BiomeGenBase[] { BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.coldTaiga };
/* 19 */     this.parent = p_i45560_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 28 */     int[] var5 = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
/* 29 */     int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 31 */     for (int var7 = 0; var7 < areaHeight; var7++) {
/*    */       
/* 33 */       for (int var8 = 0; var8 < areaWidth; var8++) {
/*    */         
/* 35 */         initChunkSeed((var8 + areaX), (var7 + areaY));
/* 36 */         int var9 = var5[var8 + var7 * areaWidth];
/* 37 */         int var10 = (var9 & 0xF00) >> 8;
/* 38 */         var9 &= 0xFFFFF0FF;
/*    */         
/* 40 */         if (isBiomeOceanic(var9)) {
/*    */           
/* 42 */           var6[var8 + var7 * areaWidth] = var9;
/*    */         }
/* 44 */         else if (var9 == BiomeGenBase.mushroomIsland.biomeID) {
/*    */           
/* 46 */           var6[var8 + var7 * areaWidth] = var9;
/*    */         }
/* 48 */         else if (var9 == 1) {
/*    */           
/* 50 */           if (var10 > 0) {
/*    */             
/* 52 */             if (nextInt(3) == 0)
/*    */             {
/* 54 */               var6[var8 + var7 * areaWidth] = BiomeGenBase.mesaPlateau.biomeID;
/*    */             }
/*    */             else
/*    */             {
/* 58 */               var6[var8 + var7 * areaWidth] = BiomeGenBase.mesaPlateau_F.biomeID;
/*    */             }
/*    */           
/*    */           } else {
/*    */             
/* 63 */             var6[var8 + var7 * areaWidth] = (this.field_151623_c[nextInt(this.field_151623_c.length)]).biomeID;
/*    */           }
/*    */         
/* 66 */         } else if (var9 == 2) {
/*    */           
/* 68 */           if (var10 > 0)
/*    */           {
/* 70 */             var6[var8 + var7 * areaWidth] = BiomeGenBase.jungle.biomeID;
/*    */           }
/*    */           else
/*    */           {
/* 74 */             var6[var8 + var7 * areaWidth] = (this.field_151621_d[nextInt(this.field_151621_d.length)]).biomeID;
/*    */           }
/*    */         
/* 77 */         } else if (var9 == 3) {
/*    */           
/* 79 */           if (var10 > 0)
/*    */           {
/* 81 */             var6[var8 + var7 * areaWidth] = BiomeGenBase.megaTaiga.biomeID;
/*    */           }
/*    */           else
/*    */           {
/* 85 */             var6[var8 + var7 * areaWidth] = (this.field_151622_e[nextInt(this.field_151622_e.length)]).biomeID;
/*    */           }
/*    */         
/* 88 */         } else if (var9 == 4) {
/*    */           
/* 90 */           var6[var8 + var7 * areaWidth] = (this.field_151620_f[nextInt(this.field_151620_f.length)]).biomeID;
/*    */         }
/*    */         else {
/*    */           
/* 94 */           var6[var8 + var7 * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 99 */     return var6;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerBiome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */