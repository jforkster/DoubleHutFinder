/*     */ package minecraft.layer;
/*     */ 
/*     */ import minecraft.biome.BiomeGenBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenLayerShore
/*     */   extends GenLayer
/*     */ {
/*     */   public GenLayerShore(long p_i2130_1_, GenLayer p_i2130_3_) {
/*  11 */     super(p_i2130_1_);
/*  12 */     this.parent = p_i2130_3_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  21 */     int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  22 */     int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  24 */     for (int var7 = 0; var7 < areaHeight; var7++) {
/*     */       
/*  26 */       for (int var8 = 0; var8 < areaWidth; var8++) {
/*     */         
/*  28 */         initChunkSeed((var8 + areaX), (var7 + areaY));
/*  29 */         int var9 = var5[var8 + 1 + (var7 + 1) * (areaWidth + 2)];
/*  30 */         BiomeGenBase var10 = BiomeGenBase.getBiome(var9);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  36 */         if (var9 == BiomeGenBase.mushroomIsland.biomeID) {
/*     */           
/*  38 */           int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
/*  39 */           int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
/*  40 */           int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
/*  41 */           int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
/*     */           
/*  43 */           if (var11 != BiomeGenBase.ocean.biomeID && var12 != BiomeGenBase.ocean.biomeID && var13 != BiomeGenBase.ocean.biomeID && var14 != BiomeGenBase.ocean.biomeID)
/*     */           {
/*  45 */             var6[var8 + var7 * areaWidth] = var9;
/*     */           }
/*     */           else
/*     */           {
/*  49 */             var6[var8 + var7 * areaWidth] = BiomeGenBase.mushroomIslandShore.biomeID;
/*     */           }
/*     */         
/*  52 */         } else if (var10 != null && var10.getBiomeClass() == minecraft.biome.BiomeGenJungle.class) {
/*     */           
/*  54 */           int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
/*  55 */           int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
/*  56 */           int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
/*  57 */           int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
/*     */           
/*  59 */           if (func_151631_c(var11) && func_151631_c(var12) && func_151631_c(var13) && func_151631_c(var14)) {
/*     */             
/*  61 */             if (!isBiomeOceanic(var11) && !isBiomeOceanic(var12) && !isBiomeOceanic(var13) && !isBiomeOceanic(var14))
/*     */             {
/*  63 */               var6[var8 + var7 * areaWidth] = var9;
/*     */             }
/*     */             else
/*     */             {
/*  67 */               var6[var8 + var7 * areaWidth] = BiomeGenBase.beach.biomeID;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  72 */             var6[var8 + var7 * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
/*     */           }
/*     */         
/*  75 */         } else if (var9 != BiomeGenBase.extremeHills.biomeID && var9 != BiomeGenBase.extremeHillsPlus.biomeID && var9 != BiomeGenBase.extremeHillsEdge.biomeID) {
/*     */           
/*  77 */           if (var10 != null && var10.isSnowyBiome()) {
/*     */             
/*  79 */             func_151632_a(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.coldBeach.biomeID);
/*     */           }
/*  81 */           else if (var9 != BiomeGenBase.mesa.biomeID && var9 != BiomeGenBase.mesaPlateau_F.biomeID) {
/*     */             
/*  83 */             if (var9 != BiomeGenBase.ocean.biomeID && var9 != BiomeGenBase.deepOcean.biomeID && var9 != BiomeGenBase.river.biomeID && var9 != BiomeGenBase.swampland.biomeID) {
/*     */               
/*  85 */               int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
/*  86 */               int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
/*  87 */               int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
/*  88 */               int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
/*     */               
/*  90 */               if (!isBiomeOceanic(var11) && !isBiomeOceanic(var12) && !isBiomeOceanic(var13) && !isBiomeOceanic(var14))
/*     */               {
/*  92 */                 var6[var8 + var7 * areaWidth] = var9;
/*     */               }
/*     */               else
/*     */               {
/*  96 */                 var6[var8 + var7 * areaWidth] = BiomeGenBase.beach.biomeID;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 101 */               var6[var8 + var7 * areaWidth] = var9;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 106 */             int var11 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
/* 107 */             int var12 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
/* 108 */             int var13 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
/* 109 */             int var14 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
/*     */             
/* 111 */             if (!isBiomeOceanic(var11) && !isBiomeOceanic(var12) && !isBiomeOceanic(var13) && !isBiomeOceanic(var14)) {
/*     */               
/* 113 */               if (func_151633_d(var11) && func_151633_d(var12) && func_151633_d(var13) && func_151633_d(var14))
/*     */               {
/* 115 */                 var6[var8 + var7 * areaWidth] = var9;
/*     */               }
/*     */               else
/*     */               {
/* 119 */                 var6[var8 + var7 * areaWidth] = BiomeGenBase.desert.biomeID;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 124 */               var6[var8 + var7 * areaWidth] = var9;
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/* 130 */           func_151632_a(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.stoneBeach.biomeID);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return var6;
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_) {
/* 140 */     if (isBiomeOceanic(p_151632_6_)) {
/*     */       
/* 142 */       p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
/*     */     }
/*     */     else {
/*     */       
/* 146 */       int var8 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
/* 147 */       int var9 = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
/* 148 */       int var10 = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
/* 149 */       int var11 = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];
/*     */       
/* 151 */       if (!isBiomeOceanic(var8) && !isBiomeOceanic(var9) && !isBiomeOceanic(var10) && !isBiomeOceanic(var11)) {
/*     */         
/* 153 */         p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
/*     */       }
/*     */       else {
/*     */         
/* 157 */         p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 164 */   private boolean func_151631_c(int p_151631_1_) { return (BiomeGenBase.getBiome(p_151631_1_) != null && BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == minecraft.biome.BiomeGenJungle.class) ? true : (!(p_151631_1_ != BiomeGenBase.jungleEdge.biomeID && p_151631_1_ != BiomeGenBase.jungle.biomeID && p_151631_1_ != BiomeGenBase.jungleHills.biomeID && p_151631_1_ != BiomeGenBase.forest.biomeID && p_151631_1_ != BiomeGenBase.taiga.biomeID && !isBiomeOceanic(p_151631_1_))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   private boolean func_151633_d(int p_151633_1_) { return BiomeGenBase.getBiome(p_151633_1_) instanceof minecraft.biome.BiomeGenMesa; }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerShore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */