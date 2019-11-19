/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerAddSnow
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerAddSnow(long p_i2121_1_, GenLayer p_i2121_3_) {
/*  7 */     super(p_i2121_1_);
/*  8 */     this.parent = p_i2121_3_;
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
/* 28 */         int var13 = var9[var12 + 1 + (var11 + 1) * var7];
/* 29 */         initChunkSeed((var12 + areaX), (var11 + areaY));
/*    */         
/* 31 */         if (var13 == 0) {
/*    */           
/* 33 */           var10[var12 + var11 * areaWidth] = 0;
/*    */         } else {
/*    */           byte var15;
/*    */           
/* 37 */           int var14 = nextInt(6);
/*    */ 
/*    */           
/* 40 */           if (var14 == 0) {
/*    */             
/* 42 */             var15 = 4;
/*    */           }
/* 44 */           else if (var14 <= 1) {
/*    */             
/* 46 */             var15 = 3;
/*    */           }
/*    */           else {
/*    */             
/* 50 */             var15 = 1;
/*    */           } 
/*    */           
/* 53 */           var10[var12 + var11 * areaWidth] = var15;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 58 */     return var10;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerAddSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */