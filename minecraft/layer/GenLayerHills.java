/*     */ package minecraft.layer;
/*     */ 
/*     */ import minecraft.biome.BiomeGenBase;
/*     */ 
/*     */ public class GenLayerHills
/*     */   extends GenLayer
/*     */ {
/*     */   private GenLayer field_151628_d;
/*     */   
/*     */   public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_) {
/*  11 */     super(p_i45479_1_);
/*  12 */     this.parent = p_i45479_3_;
/*  13 */     this.field_151628_d = p_i45479_4_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  22 */     int[] var5 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  23 */     int[] var6 = this.field_151628_d.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  24 */     int[] var7 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  26 */     for (int var8 = 0; var8 < areaHeight; var8++) {
/*     */       
/*  28 */       for (int var9 = 0; var9 < areaWidth; var9++) {
/*     */         
/*  30 */         initChunkSeed((var9 + areaX), (var8 + areaY));
/*  31 */         int var10 = var5[var9 + 1 + (var8 + 1) * (areaWidth + 2)];
/*  32 */         int var11 = var6[var9 + 1 + (var8 + 1) * (areaWidth + 2)];
/*  33 */         boolean var12 = ((var11 - 2) % 29 == 0);
/*     */         
/*  35 */         if (var10 > 255)
/*     */         {
/*  37 */           System.err.println("Error: old! " + var10);
/*     */         }
/*     */         
/*  40 */         if (var10 != 0 && var11 >= 2 && (var11 - 2) % 29 == 1 && var10 < 128) {
/*     */           
/*  42 */           if (BiomeGenBase.getBiome(var10 + '') != null)
/*     */           {
/*  44 */             var7[var9 + var8 * areaWidth] = var10 + 128;
/*     */           }
/*     */           else
/*     */           {
/*  48 */             var7[var9 + var8 * areaWidth] = var10;
/*     */           }
/*     */         
/*  51 */         } else if (nextInt(3) != 0 && !var12) {
/*     */           
/*  53 */           var7[var9 + var8 * areaWidth] = var10;
/*     */         }
/*     */         else {
/*     */           
/*  57 */           int var13 = var10;
/*     */ 
/*     */           
/*  60 */           if (var10 == BiomeGenBase.desert.biomeID) {
/*     */             
/*  62 */             var13 = BiomeGenBase.desertHills.biomeID;
/*     */           }
/*  64 */           else if (var10 == BiomeGenBase.forest.biomeID) {
/*     */             
/*  66 */             var13 = BiomeGenBase.forestHills.biomeID;
/*     */           }
/*  68 */           else if (var10 == BiomeGenBase.birchForest.biomeID) {
/*     */             
/*  70 */             var13 = BiomeGenBase.birchForestHills.biomeID;
/*     */           }
/*  72 */           else if (var10 == BiomeGenBase.roofedForest.biomeID) {
/*     */             
/*  74 */             var13 = BiomeGenBase.plains.biomeID;
/*     */           }
/*  76 */           else if (var10 == BiomeGenBase.taiga.biomeID) {
/*     */             
/*  78 */             var13 = BiomeGenBase.taigaHills.biomeID;
/*     */           }
/*  80 */           else if (var10 == BiomeGenBase.megaTaiga.biomeID) {
/*     */             
/*  82 */             var13 = BiomeGenBase.megaTaigaHills.biomeID;
/*     */           }
/*  84 */           else if (var10 == BiomeGenBase.coldTaiga.biomeID) {
/*     */             
/*  86 */             var13 = BiomeGenBase.coldTaigaHills.biomeID;
/*     */           }
/*  88 */           else if (var10 == BiomeGenBase.plains.biomeID) {
/*     */             
/*  90 */             if (nextInt(3) == 0)
/*     */             {
/*  92 */               var13 = BiomeGenBase.forestHills.biomeID;
/*     */             }
/*     */             else
/*     */             {
/*  96 */               var13 = BiomeGenBase.forest.biomeID;
/*     */             }
/*     */           
/*  99 */           } else if (var10 == BiomeGenBase.icePlains.biomeID) {
/*     */             
/* 101 */             var13 = BiomeGenBase.iceMountains.biomeID;
/*     */           }
/* 103 */           else if (var10 == BiomeGenBase.jungle.biomeID) {
/*     */             
/* 105 */             var13 = BiomeGenBase.jungleHills.biomeID;
/*     */           }
/* 107 */           else if (var10 == BiomeGenBase.ocean.biomeID) {
/*     */             
/* 109 */             var13 = BiomeGenBase.deepOcean.biomeID;
/*     */           }
/* 111 */           else if (var10 == BiomeGenBase.extremeHills.biomeID) {
/*     */             
/* 113 */             var13 = BiomeGenBase.extremeHillsPlus.biomeID;
/*     */           }
/* 115 */           else if (var10 == BiomeGenBase.savanna.biomeID) {
/*     */             
/* 117 */             var13 = BiomeGenBase.savannaPlateau.biomeID;
/*     */           }
/* 119 */           else if (biomesEqualOrMesaPlateau(var10, BiomeGenBase.mesaPlateau_F.biomeID)) {
/*     */             
/* 121 */             var13 = BiomeGenBase.mesa.biomeID;
/*     */           }
/* 123 */           else if (var10 == BiomeGenBase.deepOcean.biomeID && nextInt(3) == 0) {
/*     */             
/* 125 */             int var14 = nextInt(2);
/*     */             
/* 127 */             if (var14 == 0) {
/*     */               
/* 129 */               var13 = BiomeGenBase.plains.biomeID;
/*     */             }
/*     */             else {
/*     */               
/* 133 */               var13 = BiomeGenBase.forest.biomeID;
/*     */             } 
/*     */           } 
/*     */           
/* 137 */           if (var12 && var13 != var10)
/*     */           {
/* 139 */             if (BiomeGenBase.getBiome(var13 + '') != null) {
/*     */               
/* 141 */               var13 += 128;
/*     */             }
/*     */             else {
/*     */               
/* 145 */               var13 = var10;
/*     */             } 
/*     */           }
/*     */           
/* 149 */           if (var13 == var10) {
/*     */             
/* 151 */             var7[var9 + var8 * areaWidth] = var10;
/*     */           }
/*     */           else {
/*     */             
/* 155 */             int var14 = var5[var9 + 1 + (var8 + 1 - 1) * (areaWidth + 2)];
/* 156 */             int var15 = var5[var9 + 1 + 1 + (var8 + 1) * (areaWidth + 2)];
/* 157 */             int var16 = var5[var9 + 1 - 1 + (var8 + 1) * (areaWidth + 2)];
/* 158 */             int var17 = var5[var9 + 1 + (var8 + 1 + 1) * (areaWidth + 2)];
/* 159 */             int var18 = 0;
/*     */             
/* 161 */             if (biomesEqualOrMesaPlateau(var14, var10))
/*     */             {
/* 163 */               var18++;
/*     */             }
/*     */             
/* 166 */             if (biomesEqualOrMesaPlateau(var15, var10))
/*     */             {
/* 168 */               var18++;
/*     */             }
/*     */             
/* 171 */             if (biomesEqualOrMesaPlateau(var16, var10))
/*     */             {
/* 173 */               var18++;
/*     */             }
/*     */             
/* 176 */             if (biomesEqualOrMesaPlateau(var17, var10))
/*     */             {
/* 178 */               var18++;
/*     */             }
/*     */             
/* 181 */             if (var18 >= 3) {
/*     */               
/* 183 */               var7[var9 + var8 * areaWidth] = var13;
/*     */             }
/*     */             else {
/*     */               
/* 187 */               var7[var9 + var8 * areaWidth] = var10;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 194 */     return var7;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerHills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */