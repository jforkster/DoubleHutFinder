/*    */ package com.scicraft.seedfinder;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class strongholdFinder
/*    */ {
/*    */   public static xzPair findValidLocation(int searchX, int searchY, int size, List<Biome> paramList, Random random, biomeGenerator generator) {
/* 12 */     int x1 = searchX - size >> 2;
/* 13 */     int y1 = searchY - size >> 2;
/* 14 */     int x2 = searchX + size >> 2;
/* 15 */     int y2 = searchY + size >> 2;
/*    */     
/* 17 */     int width = x2 - x1 + 1;
/* 18 */     int height = y2 - y1 + 1;
/* 19 */     int[] arrayOfInt = generator.getBiomeData(x1, y1, width, height, true);
/* 20 */     xzPair location = null;
/* 21 */     int numberOfValidFound = 0;
/* 22 */     for (int i = 0; i < width * height; i++) {
/* 23 */       int x = x1 + i % width << 2;
/* 24 */       int y = y1 + i / width << 2;
/* 25 */       if (arrayOfInt[i] > Biome.biomes.length)
/* 26 */         return null; 
/* 27 */       Biome localBiome = Biome.biomes[arrayOfInt[i]];
/* 28 */       if (paramList.contains(localBiome) && (location == null || random.nextInt(numberOfValidFound + 1) == 0)) {
/*    */         
/* 30 */         location = new xzPair(x, y);
/* 31 */         numberOfValidFound++;
/*    */       } 
/*    */     } 
/* 34 */     return location;
/*    */   }
/*    */   
/*    */   public xzPair[] findStrongholds(long seed, biomeGenerator generator) {
/* 38 */     xzPair[] strongholds = new xzPair[3];
/* 39 */     Random random = new Random();
/* 40 */     random.setSeed(seed);
/*    */     
/* 42 */     List<Biome> biomeArrayList = new ArrayList<Biome>();
/*    */ 
/*    */     
/* 45 */     for (int i = 0; i < Biome.biomes.length; i++) {
/* 46 */       if (Biome.biomes[i] != null && (Biome.biomes[i]).type.value1 > 0.0F) {
/* 47 */         biomeArrayList.add(Biome.biomes[i]);
/*    */       }
/*    */     } 
/*    */     
/* 51 */     double angle = random.nextDouble() * Math.PI * 2.0D;
/* 52 */     for (int i = 0; i < 3; i++) {
/* 53 */       double distance = (1.25D + random.nextDouble()) * 32.0D;
/* 54 */       int x = (int)Math.round(Math.cos(angle) * distance);
/* 55 */       int z = (int)Math.round(Math.sin(angle) * distance);
/*    */ 
/*    */       
/* 58 */       xzPair location = findValidLocation((x << 4) + 8, (z << 4) + 8, 112, biomeArrayList, random, generator);
/* 59 */       if (location != null) {
/* 60 */         x = location.getX() >> 4;
/* 61 */         z = location.getZ() >> 4;
/*    */       } 
/*    */ 
/*    */       
/* 65 */       strongholds[i] = new xzPair(x << 4, z << 4);
/* 66 */       angle += 2.0943951023931953D;
/*    */     } 
/* 68 */     return strongholds;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/strongholdFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */