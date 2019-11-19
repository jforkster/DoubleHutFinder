/*    */ package minecraft.layer;
/*    */ 
/*    */ public class GenLayerSmooth
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerSmooth(long p_i2131_1_, GenLayer p_i2131_3_) {
/*  7 */     super(p_i2131_1_);
/*  8 */     this.parent = p_i2131_3_;
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
/* 28 */         int var13 = var9[var12 + 0 + (var11 + 1) * var7];
/* 29 */         int var14 = var9[var12 + 2 + (var11 + 1) * var7];
/* 30 */         int var15 = var9[var12 + 1 + (var11 + 0) * var7];
/* 31 */         int var16 = var9[var12 + 1 + (var11 + 2) * var7];
/* 32 */         int var17 = var9[var12 + 1 + (var11 + 1) * var7];
/*    */         
/* 34 */         if (var13 == var14 && var15 == var16) {
/*    */           
/* 36 */           initChunkSeed((var12 + areaX), (var11 + areaY));
/*    */           
/* 38 */           if (nextInt(2) == 0)
/*    */           {
/* 40 */             var17 = var13;
/*    */           }
/*    */           else
/*    */           {
/* 44 */             var17 = var15;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 49 */           if (var13 == var14)
/*    */           {
/* 51 */             var17 = var13;
/*    */           }
/*    */           
/* 54 */           if (var15 == var16)
/*    */           {
/* 56 */             var17 = var15;
/*    */           }
/*    */         } 
/*    */         
/* 60 */         var10[var12 + var11 * areaWidth] = var17;
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return var10;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerSmooth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */