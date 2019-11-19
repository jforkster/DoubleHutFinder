/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerVoronoiZoom
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerVoronoiZoom(long p_i2133_1_, GenLayer p_i2133_3_) {
/*  7 */     super(p_i2133_1_);
/*  8 */     this.parent = p_i2133_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 17 */     areaX -= 2;
/* 18 */     areaY -= 2;
/* 19 */     int var5 = areaX >> 2;
/* 20 */     int var6 = areaY >> 2;
/* 21 */     int var7 = (areaWidth >> 2) + 2;
/* 22 */     int var8 = (areaHeight >> 2) + 2;
/* 23 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/* 24 */     int var10 = var7 - 1 << 2;
/* 25 */     int var11 = var8 - 1 << 2;
/* 26 */     int[] var12 = IntCache.getIntCache(var10 * var11);
/*    */ 
/*    */     
/* 29 */     for (int var13 = 0; var13 < var8 - 1; var13++) {
/*    */       
/* 31 */       int var14 = 0;
/* 32 */       int var15 = var9[var14 + 0 + (var13 + 0) * var7];
/*    */       
/* 34 */       for (int var16 = var9[var14 + 0 + (var13 + 1) * var7]; var14 < var7 - 1; var14++) {
/*    */         
/* 36 */         double var17 = 3.6D;
/* 37 */         initChunkSeed((var14 + var5 << 2), (var13 + var6 << 2));
/* 38 */         double var19 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
/* 39 */         double var21 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
/* 40 */         initChunkSeed((var14 + var5 + 1 << 2), (var13 + var6 << 2));
/* 41 */         double var23 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
/* 42 */         double var25 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
/* 43 */         initChunkSeed((var14 + var5 << 2), (var13 + var6 + 1 << 2));
/* 44 */         double var27 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D;
/* 45 */         double var29 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
/* 46 */         initChunkSeed((var14 + var5 + 1 << 2), (var13 + var6 + 1 << 2));
/* 47 */         double var31 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
/* 48 */         double var33 = (nextInt(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
/* 49 */         int var35 = var9[var14 + 1 + (var13 + 0) * var7] & 0xFF;
/* 50 */         int var36 = var9[var14 + 1 + (var13 + 1) * var7] & 0xFF;
/*    */         
/* 52 */         for (int var37 = 0; var37 < 4; var37++) {
/*    */           
/* 54 */           int var38 = ((var13 << 2) + var37) * var10 + (var14 << 2);
/*    */           
/* 56 */           for (int var39 = 0; var39 < 4; var39++) {
/*    */             
/* 58 */             double var40 = (var37 - var21) * (var37 - var21) + (var39 - var19) * (var39 - var19);
/* 59 */             double var42 = (var37 - var25) * (var37 - var25) + (var39 - var23) * (var39 - var23);
/* 60 */             double var44 = (var37 - var29) * (var37 - var29) + (var39 - var27) * (var39 - var27);
/* 61 */             double var46 = (var37 - var33) * (var37 - var33) + (var39 - var31) * (var39 - var31);
/*    */             
/* 63 */             if (var40 < var42 && var40 < var44 && var40 < var46) {
/*    */               
/* 65 */               var12[var38++] = var15;
/*    */             }
/* 67 */             else if (var42 < var40 && var42 < var44 && var42 < var46) {
/*    */               
/* 69 */               var12[var38++] = var35;
/*    */             }
/* 71 */             else if (var44 < var40 && var44 < var42 && var44 < var46) {
/*    */               
/* 73 */               var12[var38++] = var16;
/*    */             }
/*    */             else {
/*    */               
/* 77 */               var12[var38++] = var36;
/*    */             } 
/*    */           } 
/*    */         } 
/*    */         
/* 82 */         var15 = var35;
/* 83 */         var16 = var36;
/*    */       } 
/*    */     } 
/*    */     
/* 87 */     int[] var48 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 89 */     for (int var14 = 0; var14 < areaHeight; var14++)
/*    */     {
/* 91 */       System.arraycopy(var12, (var14 + (areaY & 0x3)) * var10 + (areaX & 0x3), var48, var14 * areaWidth, areaWidth);
/*    */     }
/*    */     
/* 94 */     return var48;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerVoronoiZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */