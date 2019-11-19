/*     */ package minecraft.layer;
/*     */ 
/*     */ import minecraft.biome.BiomeGenBase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenLayer
/*     */ {
/*     */   private long worldGenSeed;
/*     */   protected GenLayer parent;
/*     */   private long chunkSeed;
/*     */   protected long baseSeed;
/*     */   
/*     */   public static GenLayer[] func_180781_a(long p_180781_0_, String p_180781_3_) {
/*  24 */     GenLayerIsland var4 = new GenLayerIsland(1L);
/*  25 */     GenLayerFuzzyZoom var13 = new GenLayerFuzzyZoom(2000L, var4);
/*  26 */     GenLayerAddIsland var14 = new GenLayerAddIsland(1L, var13);
/*  27 */     GenLayerZoom var15 = new GenLayerZoom(2001L, var14);
/*  28 */     var14 = new GenLayerAddIsland(2L, var15);
/*  29 */     var14 = new GenLayerAddIsland(50L, var14);
/*  30 */     var14 = new GenLayerAddIsland(70L, var14);
/*  31 */     GenLayerRemoveTooMuchOcean var16 = new GenLayerRemoveTooMuchOcean(2L, var14);
/*  32 */     GenLayerAddSnow var17 = new GenLayerAddSnow(2L, var16);
/*  33 */     var14 = new GenLayerAddIsland(3L, var17);
/*  34 */     GenLayerEdge var18 = new GenLayerEdge(2L, var14, GenLayerEdge.Mode.COOL_WARM);
/*  35 */     var18 = new GenLayerEdge(2L, var18, GenLayerEdge.Mode.HEAT_ICE);
/*  36 */     var18 = new GenLayerEdge(3L, var18, GenLayerEdge.Mode.SPECIAL);
/*  37 */     var15 = new GenLayerZoom(2002L, var18);
/*  38 */     var15 = new GenLayerZoom(2003L, var15);
/*  39 */     var14 = new GenLayerAddIsland(4L, var15);
/*  40 */     GenLayerAddMushroomIsland var19 = new GenLayerAddMushroomIsland(5L, var14);
/*  41 */     GenLayerDeepOcean var20 = new GenLayerDeepOcean(4L, var19);
/*  42 */     GenLayer var21 = GenLayerZoom.magnify(1000L, var20, 0);
/*  43 */     int var6 = 4;
/*  44 */     int var7 = var6;
/*     */     
/*  46 */     GenLayer var8 = GenLayerZoom.magnify(1000L, var21, 0);
/*  47 */     GenLayerRiverInit var22 = new GenLayerRiverInit(100L, var8);
/*  48 */     GenLayerBiome var9 = new GenLayerBiome(200L, var21, p_180781_3_);
/*  49 */     GenLayer var25 = GenLayerZoom.magnify(1000L, var9, 2);
/*  50 */     GenLayerBiomeEdge var26 = new GenLayerBiomeEdge(1000L, var25);
/*  51 */     GenLayer var10 = GenLayerZoom.magnify(1000L, var22, 2);
/*  52 */     GenLayerHills var27 = new GenLayerHills(1000L, var26, var10);
/*  53 */     var8 = GenLayerZoom.magnify(1000L, var22, 2);
/*  54 */     var8 = GenLayerZoom.magnify(1000L, var8, var7);
/*  55 */     GenLayerRiver var23 = new GenLayerRiver(1L, var8);
/*  56 */     GenLayerSmooth var24 = new GenLayerSmooth(1000L, var23);
/*  57 */     Object var28 = new GenLayerRareBiome(1001L, var27);
/*     */     
/*  59 */     for (int var11 = 0; var11 < var6; var11++) {
/*     */       
/*  61 */       var28 = new GenLayerZoom((1000 + var11), (GenLayer)var28);
/*     */       
/*  63 */       if (var11 == 0)
/*     */       {
/*  65 */         var28 = new GenLayerAddIsland(3L, (GenLayer)var28);
/*     */       }
/*     */       
/*  68 */       if (var11 == 1 || var6 == 1)
/*     */       {
/*  70 */         var28 = new GenLayerShore(1000L, (GenLayer)var28);
/*     */       }
/*     */     } 
/*     */     
/*  74 */     GenLayerSmooth var29 = new GenLayerSmooth(1000L, (GenLayer)var28);
/*  75 */     GenLayerRiverMix var30 = new GenLayerRiverMix(100L, var29, var24);
/*  76 */     GenLayerVoronoiZoom var12 = new GenLayerVoronoiZoom(10L, var30);
/*  77 */     var30.initWorldGenSeed(p_180781_0_);
/*  78 */     var12.initWorldGenSeed(p_180781_0_);
/*  79 */     return new GenLayer[] { var30, var12, var30 };
/*     */   }
/*     */ 
/*     */   
/*     */   public GenLayer(long p_i2125_1_) {
/*  84 */     this.baseSeed = p_i2125_1_;
/*  85 */     this.baseSeed *= (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
/*  86 */     this.baseSeed += p_i2125_1_;
/*  87 */     this.baseSeed *= (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
/*  88 */     this.baseSeed += p_i2125_1_;
/*  89 */     this.baseSeed *= (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
/*  90 */     this.baseSeed += p_i2125_1_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initWorldGenSeed(long p_75905_1_) {
/*  99 */     this.worldGenSeed = p_75905_1_;
/*     */     
/* 101 */     if (this.parent != null)
/*     */     {
/* 103 */       this.parent.initWorldGenSeed(p_75905_1_);
/*     */     }
/*     */     
/* 106 */     this.worldGenSeed *= (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
/* 107 */     this.worldGenSeed += this.baseSeed;
/* 108 */     this.worldGenSeed *= (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
/* 109 */     this.worldGenSeed += this.baseSeed;
/* 110 */     this.worldGenSeed *= (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
/* 111 */     this.worldGenSeed += this.baseSeed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initChunkSeed(long p_75903_1_, long p_75903_3_) {
/* 119 */     this.chunkSeed = this.worldGenSeed;
/* 120 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 121 */     this.chunkSeed += p_75903_1_;
/* 122 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 123 */     this.chunkSeed += p_75903_3_;
/* 124 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 125 */     this.chunkSeed += p_75903_1_;
/* 126 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 127 */     this.chunkSeed += p_75903_3_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextInt(int p_75902_1_) {
/* 135 */     int var2 = (int)((this.chunkSeed >> 24) % p_75902_1_);
/*     */     
/* 137 */     if (var2 < 0)
/*     */     {
/* 139 */       var2 += p_75902_1_;
/*     */     }
/*     */     
/* 142 */     this.chunkSeed *= (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
/* 143 */     this.chunkSeed += this.worldGenSeed;
/* 144 */     return var2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int[] getInts(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB) {
/* 155 */     if (biomeIDA == biomeIDB)
/*     */     {
/* 157 */       return true;
/*     */     }
/* 159 */     if (biomeIDA != BiomeGenBase.mesaPlateau_F.biomeID && biomeIDA != BiomeGenBase.mesaPlateau.biomeID) {
/*     */       
/* 161 */       BiomeGenBase var2 = BiomeGenBase.getBiome(biomeIDA);
/* 162 */       BiomeGenBase var3 = BiomeGenBase.getBiome(biomeIDB);
/*     */ 
/*     */       
/*     */       try {
/* 166 */         return (var2 != null && var3 != null) ? var2.isEqualTo(var3) : 0;
/*     */       }
/* 168 */       catch (Throwable var7) {
/*     */         
/* 170 */         System.err.println("Error in biomesEqualOrMesaPlateau()");
/* 171 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 176 */     return !(biomeIDB != BiomeGenBase.mesaPlateau_F.biomeID && biomeIDB != BiomeGenBase.mesaPlateau.biomeID);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   protected static boolean isBiomeOceanic(int p_151618_0_) { return !(p_151618_0_ != BiomeGenBase.ocean.biomeID && p_151618_0_ != BiomeGenBase.deepOcean.biomeID && p_151618_0_ != BiomeGenBase.frozenOcean.biomeID); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   protected int selectRandom(int... p_151619_1_) { return p_151619_1_[nextInt(p_151619_1_.length)]; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_) { return (p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_) ? p_151617_2_ : ((p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_) ? p_151617_1_ : ((p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_) ? p_151617_1_ : ((p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_) ? p_151617_2_ : ((p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_) ? p_151617_2_ : ((p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_) ? p_151617_3_ : selectRandom(new int[] { p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_ })))))))))); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */