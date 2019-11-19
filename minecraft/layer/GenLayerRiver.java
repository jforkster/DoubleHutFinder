/*    */ package minecraft.layer;
/*    */ 
/*    */ import minecraft.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRiver
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRiver(long p_i2128_1_, GenLayer p_i2128_3_) {
/*  9 */     super(p_i2128_1_);
/* 10 */     this.parent = p_i2128_3_;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 19 */     int var5 = areaX - 1;
/* 20 */     int var6 = areaY - 1;
/* 21 */     int var7 = areaWidth + 2;
/* 22 */     int var8 = areaHeight + 2;
/* 23 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/* 24 */     int[] var10 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 26 */     for (int var11 = 0; var11 < areaHeight; var11++) {
/*    */       
/* 28 */       for (int var12 = 0; var12 < areaWidth; var12++) {
/*    */         
/* 30 */         int var13 = func_151630_c(var9[var12 + 0 + (var11 + 1) * var7]);
/* 31 */         int var14 = func_151630_c(var9[var12 + 2 + (var11 + 1) * var7]);
/* 32 */         int var15 = func_151630_c(var9[var12 + 1 + (var11 + 0) * var7]);
/* 33 */         int var16 = func_151630_c(var9[var12 + 1 + (var11 + 2) * var7]);
/* 34 */         int var17 = func_151630_c(var9[var12 + 1 + (var11 + 1) * var7]);
/*    */         
/* 36 */         if (var17 == var13 && var17 == var15 && var17 == var14 && var17 == var16) {
/*    */           
/* 38 */           var10[var12 + var11 * areaWidth] = -1;
/*    */         }
/*    */         else {
/*    */           
/* 42 */           var10[var12 + var11 * areaWidth] = BiomeGenBase.river.biomeID;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return var10;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   private int func_151630_c(int p_151630_1_) { return (p_151630_1_ >= 2) ? (2 + (p_151630_1_ & true)) : p_151630_1_; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerRiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */