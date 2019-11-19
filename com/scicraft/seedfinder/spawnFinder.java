/*    */ package com.scicraft.seedfinder;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class spawnFinder
/*    */ {
/* 10 */   public static final ArrayList<Biome> validBiomes = new ArrayList(Arrays.asList(new Biome[] {
/* 11 */           Biome.forest, 
/* 12 */           Biome.plains, 
/* 13 */           Biome.taiga, 
/* 14 */           Biome.taigaHills, 
/* 15 */           Biome.forestHills, 
/* 16 */           Biome.jungle, 
/* 17 */           Biome.jungleHills
/*    */         }));
/*    */ 
/*    */   
/*    */   public static xzPair findValidLocation(int searchX, int searchY, int size, List<Biome> paramList, Random random, biomeGenerator generator) {
/* 22 */     int x1 = searchX - size >> 2;
/* 23 */     int y1 = searchY - size >> 2;
/* 24 */     int x2 = searchX + size >> 2;
/* 25 */     int y2 = searchY + size >> 2;
/*    */     
/* 27 */     int width = x2 - x1 + 1;
/* 28 */     int height = y2 - y1 + 1;
/* 29 */     int[] arrayOfInt = generator.getBiomeData(x1, y1, width, height, true);
/* 30 */     xzPair location = null;
/* 31 */     int numberOfValidFound = 0;
/* 32 */     for (int i = 0; i < width * height; i++) {
/* 33 */       int x = x1 + i % width << 2;
/* 34 */       int y = y1 + i / width << 2;
/* 35 */       if (arrayOfInt[i] > Biome.biomes.length)
/* 36 */         return null; 
/* 37 */       Biome localBiome = Biome.biomes[arrayOfInt[i]];
/* 38 */       if (paramList.contains(localBiome) && (location == null || random.nextInt(numberOfValidFound + 1) == 0)) {
/*    */         
/* 40 */         location = new xzPair(x, y);
/* 41 */         numberOfValidFound++;
/*    */       } 
/*    */     } 
/* 44 */     return location;
/*    */   }
/*    */   
/*    */   public xzPair getSpawnPosition(long seed, biomeGenerator generator) {
/* 48 */     Random random = new Random(seed);
/* 49 */     xzPair location = findValidLocation(0, 0, 256, validBiomes, random, generator);
/* 50 */     int x = 0;
/* 51 */     int z = 0;
/* 52 */     if (location != null) {
/* 53 */       x = location.getX();
/* 54 */       z = location.getZ();
/*    */     } else {
/* 56 */       return null;
/*    */     } 
/*    */     
/* 59 */     return new xzPair(x, z);
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/spawnFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */