/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerAddIsland
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerAddIsland(long p_i2119_1_, GenLayer p_i2119_3_) {
/*  7 */     super(p_i2119_1_);
/*  8 */     this.parent = p_i2119_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 17 */     int var5 = areaX - 1;
/* 18 */     int var6 = areaY - 1;
/* 19 */     int var7 = areaWidth + 2;
/* 20 */     int var8 = areaHeight + 2;
/* 21 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/* 22 */     int[] var10 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 24 */     for (int var11 = 0; var11 < areaHeight; var11++) {
/*    */       
/* 26 */       for (int var12 = 0; var12 < areaWidth; var12++) {
/*    */         
/* 28 */         int var13 = var9[var12 + 0 + (var11 + 0) * var7];
/* 29 */         int var14 = var9[var12 + 2 + (var11 + 0) * var7];
/* 30 */         int var15 = var9[var12 + 0 + (var11 + 2) * var7];
/* 31 */         int var16 = var9[var12 + 2 + (var11 + 2) * var7];
/* 32 */         int var17 = var9[var12 + 1 + (var11 + 1) * var7];
/* 33 */         initChunkSeed((var12 + areaX), (var11 + areaY));
/*    */         
/* 35 */         if (var17 == 0 && (var13 != 0 || var14 != 0 || var15 != 0 || var16 != 0)) {
/*    */           
/* 37 */           int var18 = 1;
/* 38 */           int var19 = 1;
/*    */           
/* 40 */           if (var13 != 0 && nextInt(var18++) == 0)
/*    */           {
/* 42 */             var19 = var13;
/*    */           }
/*    */           
/* 45 */           if (var14 != 0 && nextInt(var18++) == 0)
/*    */           {
/* 47 */             var19 = var14;
/*    */           }
/*    */           
/* 50 */           if (var15 != 0 && nextInt(var18++) == 0)
/*    */           {
/* 52 */             var19 = var15;
/*    */           }
/*    */           
/* 55 */           if (var16 != 0 && nextInt(var18++) == 0)
/*    */           {
/* 57 */             var19 = var16;
/*    */           }
/*    */           
/* 60 */           if (nextInt(3) == 0)
/*    */           {
/* 62 */             var10[var12 + var11 * areaWidth] = var19;
/*    */           }
/* 64 */           else if (var19 == 4)
/*    */           {
/* 66 */             var10[var12 + var11 * areaWidth] = 4;
/*    */           }
/*    */           else
/*    */           {
/* 70 */             var10[var12 + var11 * areaWidth] = 0;
/*    */           }
/*    */         
/* 73 */         } else if (var17 > 0 && (var13 == 0 || var14 == 0 || var15 == 0 || var16 == 0)) {
/*    */           
/* 75 */           if (nextInt(5) == 0) {
/*    */             
/* 77 */             if (var17 == 4)
/*    */             {
/* 79 */               var10[var12 + var11 * areaWidth] = 4;
/*    */             }
/*    */             else
/*    */             {
/* 83 */               var10[var12 + var11 * areaWidth] = 0;
/*    */             }
/*    */           
/*    */           } else {
/*    */             
/* 88 */             var10[var12 + var11 * areaWidth] = var17;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 93 */           var10[var12 + var11 * areaWidth] = var17;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 98 */     return var10;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerAddIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */