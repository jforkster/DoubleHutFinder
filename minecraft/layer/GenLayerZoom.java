/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerZoom
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerZoom(long p_i2134_1_, GenLayer p_i2134_3_) {
/*  7 */     super(p_i2134_1_);
/*  8 */     this.parent = p_i2134_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 17 */     int var5 = areaX >> 1;
/* 18 */     int var6 = areaY >> 1;
/* 19 */     int var7 = (areaWidth >> 1) + 2;
/* 20 */     int var8 = (areaHeight >> 1) + 2;
/* 21 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/* 22 */     int var10 = var7 - 1 << 1;
/* 23 */     int var11 = var8 - 1 << 1;
/* 24 */     int[] var12 = IntCache.getIntCache(var10 * var11);
/*    */ 
/*    */     
/* 27 */     for (int var13 = 0; var13 < var8 - 1; var13++) {
/*    */       
/* 29 */       int var14 = (var13 << 1) * var10;
/* 30 */       int var15 = 0;
/* 31 */       int var16 = var9[var15 + 0 + (var13 + 0) * var7];
/*    */       
/* 33 */       for (int var17 = var9[var15 + 0 + (var13 + 1) * var7]; var15 < var7 - 1; var15++) {
/*    */         
/* 35 */         initChunkSeed((var15 + var5 << 1), (var13 + var6 << 1));
/* 36 */         int var18 = var9[var15 + 1 + (var13 + 0) * var7];
/* 37 */         int var19 = var9[var15 + 1 + (var13 + 1) * var7];
/* 38 */         var12[var14] = var16;
/* 39 */         var12[var14++ + var10] = selectRandom(new int[] { var16, var17 });
/* 40 */         var12[var14] = selectRandom(new int[] { var16, var18 });
/* 41 */         var12[var14++ + var10] = selectModeOrRandom(var16, var18, var17, var19);
/* 42 */         var16 = var18;
/* 43 */         var17 = var19;
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     int[] var20 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 49 */     for (int var14 = 0; var14 < areaHeight; var14++)
/*    */     {
/* 51 */       System.arraycopy(var12, (var14 + (areaY & true)) * var10 + (areaX & true), var20, var14 * areaWidth, areaWidth);
/*    */     }
/*    */     
/* 54 */     return var20;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static GenLayer magnify(long p_75915_0_, GenLayer p_75915_2_, int p_75915_3_) {
/* 62 */     Object var4 = p_75915_2_;
/*    */     
/* 64 */     for (int var5 = 0; var5 < p_75915_3_; var5++)
/*    */     {
/* 66 */       var4 = new GenLayerZoom(p_75915_0_ + var5, (GenLayer)var4);
/*    */     }
/*    */     
/* 69 */     return (GenLayer)var4;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */