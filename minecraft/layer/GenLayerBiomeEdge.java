/*     */ package minecraft.layer;
/*     */ 
/*     */ import minecraft.biome.BiomeGenBase;
/*     */ 
/*     */ public class GenLayerBiomeEdge
/*     */   extends GenLayer
/*     */ {
/*     */   public GenLayerBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_) {
/*   9 */     super(p_i45475_1_);
/*  10 */     this.parent = p_i45475_3_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  19 */     int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  20 */     int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  22 */     for (int var7 = 0; var7 < areaHeight; var7++) {
/*     */       
/*  24 */       for (int var8 = 0; var8 < areaWidth; var8++) {
/*     */         
/*  26 */         initChunkSeed((var8 + areaX), (var7 + areaY));
/*  27 */         int var9 = var5[var8 + 1 + (var7 + 1) * (areaWidth + 2)];
/*     */         
/*  29 */         if (!replaceBiomeEdgeIfNecessary(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) && !replaceBiomeEdge(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) && !replaceBiomeEdge(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) && !replaceBiomeEdge(var5, var6, var8, var7, areaWidth, var9, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  36 */           if (var9 == BiomeGenBase.desert.biomeID) {
/*     */             
/*  38 */             int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
/*  39 */             int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
/*  40 */             int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
/*  41 */             int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
/*     */             
/*  43 */             if (var10 != BiomeGenBase.icePlains.biomeID && var11 != BiomeGenBase.icePlains.biomeID && var12 != BiomeGenBase.icePlains.biomeID && var13 != BiomeGenBase.icePlains.biomeID)
/*     */             {
/*  45 */               var6[var8 + var7 * areaWidth] = var9;
/*     */             }
/*     */             else
/*     */             {
/*  49 */               var6[var8 + var7 * areaWidth] = BiomeGenBase.extremeHillsPlus.biomeID;
/*     */             }
/*     */           
/*  52 */           } else if (var9 == BiomeGenBase.swampland.biomeID) {
/*     */             
/*  54 */             int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (areaWidth + 2)];
/*  55 */             int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (areaWidth + 2)];
/*  56 */             int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (areaWidth + 2)];
/*  57 */             int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (areaWidth + 2)];
/*     */             
/*  59 */             if (var10 != BiomeGenBase.desert.biomeID && var11 != BiomeGenBase.desert.biomeID && var12 != BiomeGenBase.desert.biomeID && var13 != BiomeGenBase.desert.biomeID && var10 != BiomeGenBase.coldTaiga.biomeID && var11 != BiomeGenBase.coldTaiga.biomeID && var12 != BiomeGenBase.coldTaiga.biomeID && var13 != BiomeGenBase.coldTaiga.biomeID && var10 != BiomeGenBase.icePlains.biomeID && var11 != BiomeGenBase.icePlains.biomeID && var12 != BiomeGenBase.icePlains.biomeID && var13 != BiomeGenBase.icePlains.biomeID) {
/*     */               
/*  61 */               if (var10 != BiomeGenBase.jungle.biomeID && var13 != BiomeGenBase.jungle.biomeID && var11 != BiomeGenBase.jungle.biomeID && var12 != BiomeGenBase.jungle.biomeID)
/*     */               {
/*  63 */                 var6[var8 + var7 * areaWidth] = var9;
/*     */               }
/*     */               else
/*     */               {
/*  67 */                 var6[var8 + var7 * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/*  72 */               var6[var8 + var7 * areaWidth] = BiomeGenBase.plains.biomeID;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  77 */             var6[var8 + var7 * areaWidth] = var9;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     return var6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean replaceBiomeEdgeIfNecessary(int[] p_151636_1_, int[] p_151636_2_, int p_151636_3_, int p_151636_4_, int p_151636_5_, int p_151636_6_, int p_151636_7_, int p_151636_8_) {
/*  91 */     if (!biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_))
/*     */     {
/*  93 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  97 */     int var9 = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
/*  98 */     int var10 = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
/*  99 */     int var11 = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
/* 100 */     int var12 = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];
/*     */     
/* 102 */     if (canBiomesBeNeighbors(var9, p_151636_7_) && canBiomesBeNeighbors(var10, p_151636_7_) && canBiomesBeNeighbors(var11, p_151636_7_) && canBiomesBeNeighbors(var12, p_151636_7_)) {
/*     */       
/* 104 */       p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_6_;
/*     */     }
/*     */     else {
/*     */       
/* 108 */       p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_8_;
/*     */     } 
/*     */     
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_) {
/* 120 */     if (p_151635_6_ != p_151635_7_)
/*     */     {
/* 122 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 126 */     int var9 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
/* 127 */     int var10 = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
/* 128 */     int var11 = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
/* 129 */     int var12 = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
/*     */     
/* 131 */     if (biomesEqualOrMesaPlateau(var9, p_151635_7_) && biomesEqualOrMesaPlateau(var10, p_151635_7_) && biomesEqualOrMesaPlateau(var11, p_151635_7_) && biomesEqualOrMesaPlateau(var12, p_151635_7_)) {
/*     */       
/* 133 */       p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
/*     */     }
/*     */     else {
/*     */       
/* 137 */       p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
/*     */     } 
/*     */     
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_) {
/* 150 */     if (biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_))
/*     */     {
/* 152 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 156 */     BiomeGenBase var3 = BiomeGenBase.getBiome(p_151634_1_);
/* 157 */     BiomeGenBase var4 = BiomeGenBase.getBiome(p_151634_2_);
/*     */     
/* 159 */     if (var3 != null && var4 != null) {
/*     */       
/* 161 */       BiomeGenBase.TempCategory var5 = var3.getTempCategory();
/* 162 */       BiomeGenBase.TempCategory var6 = var4.getTempCategory();
/* 163 */       return !(var5 != var6 && var5 != BiomeGenBase.TempCategory.MEDIUM && var6 != BiomeGenBase.TempCategory.MEDIUM);
/*     */     } 
/*     */ 
/*     */     
/* 167 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerBiomeEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */