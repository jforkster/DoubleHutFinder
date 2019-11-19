/*    */ package minecraft.layer;
/*    */ 
/*    */ 
/*    */ public class GenLayerIsland
/*    */   extends GenLayer
/*    */ {
/*  7 */   public GenLayerIsland(long p_i2124_1_) { super(p_i2124_1_); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 16 */     int[] var5 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 18 */     for (int var6 = 0; var6 < areaHeight; var6++) {
/*    */       
/* 20 */       for (int var7 = 0; var7 < areaWidth; var7++) {
/*    */         
/* 22 */         initChunkSeed((areaX + var7), (areaY + var6));
/* 23 */         var5[var7 + var6 * areaWidth] = (nextInt(10) == 0) ? 1 : 0;
/*    */       } 
/*    */     } 
/*    */     
/* 27 */     if (areaX > -areaWidth && areaX <= 0 && areaY > -areaHeight && areaY <= 0)
/*    */     {
/* 29 */       var5[-areaX + -areaY * areaWidth] = 1;
/*    */     }
/*    */     
/* 32 */     return var5;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */