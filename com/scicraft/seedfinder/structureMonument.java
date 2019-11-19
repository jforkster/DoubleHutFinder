/*    */ package com.scicraft.seedfinder;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class structureMonument extends structure {
/*  8 */   private Random rnd = new Random();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public xzPair structurePosInRegion(long x, long z, long seed) {
/* 14 */     this.rnd.setSeed(x * 341873128712L + z * 132897987541L + seed + 10387313L);
/* 15 */     return new xzPair((this.rnd.nextInt(27) + this.rnd.nextInt(27)) / 2, (this.rnd.nextInt(27) + this.rnd.nextInt(27)) / 2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public xzPair structurePosInRegionFast(long xPart, long zPart, long seed, int lowerThen, int higherThen) {
/* 22 */     this.rnd.setSeed(xPart + zPart + seed + 10387313L);
/* 23 */     int xRand = (this.rnd.nextInt(27) + this.rnd.nextInt(27)) / 2;
/* 24 */     if (xRand <= lowerThen || xRand >= higherThen) {
/* 25 */       return new xzPair(xRand, (this.rnd.nextInt(27) + this.rnd.nextInt(27)) / 2);
/*    */     }
/* 27 */     return null;
/*    */   }
/*    */   
/* 30 */   public static List<Biome> validSurroundingBiomes = Arrays.asList(
/* 31 */       new Biome[] {
/* 32 */         Biome.ocean, 
/* 33 */         Biome.deepOcean, 
/* 34 */         Biome.frozenOcean, 
/* 35 */         Biome.river, 
/* 36 */         Biome.frozenRiver, 
/*    */         
/* 38 */         Biome.oceanM, 
/* 39 */         Biome.deepOceanM, 
/* 40 */         Biome.frozenOceanM, 
/* 41 */         Biome.riverM, 
/* 42 */         Biome.frozenRiverM
/*    */       });
/*    */ 
/*    */   
/*    */   public static boolean isValidBiome(int x, int y, int size, List<Biome> validBiomes, biomeGenerator generator) {
/* 47 */     int x1 = x - size >> 2;
/* 48 */     int y1 = y - size >> 2;
/* 49 */     int x2 = x + size >> 2;
/* 50 */     int y2 = y + size >> 2;
/*    */     
/* 52 */     int width = x2 - x1 + 1;
/* 53 */     int height = y2 - y1 + 1;
/*    */     
/* 55 */     int[] arrayOfInt = generator.getBiomeData(x1, y1, width, height, true);
/* 56 */     for (int i = 0; i < width * height; i++) {
/* 57 */       Biome localBiome = Biome.biomes[arrayOfInt[i]];
/* 58 */       if (!validBiomes.contains(localBiome)) return false; 
/*    */     } 
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean structureWillSpawn(int xRegion, int zRegion, int xRandom, int zRandom, biomeGenerator generator) {
/* 69 */     if (generator.getBiomeAt(xRegion * 512 + xRandom * 16 + 8, zRegion * 512 + zRandom * 16 + 8) == 24 && 
/* 70 */       isValidBiome(xRegion * 512 + xRandom * 16 + 8, zRegion * 512 + zRandom * 16 + 8, 29, validSurroundingBiomes, generator))
/* 71 */       return true; 
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/structureMonument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */