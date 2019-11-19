/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerRiverInit
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
/*  7 */     super(p_i2127_1_);
/*  8 */     this.parent = p_i2127_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 17 */     int[] var5 = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
/* 18 */     int[] var6 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 20 */     for (int var7 = 0; var7 < areaHeight; var7++) {
/*    */       
/* 22 */       for (int var8 = 0; var8 < areaWidth; var8++) {
/*    */         
/* 24 */         initChunkSeed((var8 + areaX), (var7 + areaY));
/* 25 */         var6[var8 + var7 * areaWidth] = (var5[var8 + var7 * areaWidth] > 0) ? (nextInt(299999) + 2) : 0;
/*    */       } 
/*    */     } 
/*    */     
/* 29 */     return var6;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerRiverInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */