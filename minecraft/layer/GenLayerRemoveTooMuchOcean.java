/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerRemoveTooMuchOcean
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRemoveTooMuchOcean(long p_i45480_1_, GenLayer p_i45480_3_) {
/*  7 */     super(p_i45480_1_);
/*  8 */     this.parent = p_i45480_3_;
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
/* 28 */         int var13 = var9[var12 + 1 + (var11 + 1 - 1) * (areaWidth + 2)];
/* 29 */         int var14 = var9[var12 + 1 + 1 + (var11 + 1) * (areaWidth + 2)];
/* 30 */         int var15 = var9[var12 + 1 - 1 + (var11 + 1) * (areaWidth + 2)];
/* 31 */         int var16 = var9[var12 + 1 + (var11 + 1 + 1) * (areaWidth + 2)];
/* 32 */         int var17 = var9[var12 + 1 + (var11 + 1) * var7];
/* 33 */         var10[var12 + var11 * areaWidth] = var17;
/* 34 */         initChunkSeed((var12 + areaX), (var11 + areaY));
/*    */         
/* 36 */         if (var17 == 0 && var13 == 0 && var14 == 0 && var15 == 0 && var16 == 0 && nextInt(2) == 0)
/*    */         {
/* 38 */           var10[var12 + var11 * areaWidth] = 1;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 43 */     return var10;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerRemoveTooMuchOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */