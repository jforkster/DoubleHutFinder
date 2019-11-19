/*    */ package minecraft.biome;
/*    */ 
/*    */ public class BiomeGenMutated
/*    */   extends BiomeGenBase
/*    */ {
/*    */   protected BiomeGenBase baseBiome;
/*    */   
/*    */   public BiomeGenMutated(int p_i45381_1_, BiomeGenBase p_i45381_2_) {
/*  9 */     super(p_i45381_1_);
/* 10 */     this.baseBiome = p_i45381_2_;
/* 11 */     func_150557_a(p_i45381_2_.color, true);
/* 12 */     this.biomeName = String.valueOf(p_i45381_2_.biomeName) + " M";
/* 13 */     this.fillerBlockMetadata = p_i45381_2_.fillerBlockMetadata;
/* 14 */     this.minHeight = p_i45381_2_.minHeight;
/* 15 */     this.maxHeight = p_i45381_2_.maxHeight;
/* 16 */     this.temperature = p_i45381_2_.temperature;
/* 17 */     this.rainfall = p_i45381_2_.rainfall;
/* 18 */     this.waterColorMultiplier = p_i45381_2_.waterColorMultiplier;
/* 19 */     this.enableSnow = p_i45381_2_.enableSnow;
/* 20 */     this.enableRain = p_i45381_2_.enableRain;
/* 21 */     this.temperature = p_i45381_2_.temperature;
/* 22 */     this.rainfall = p_i45381_2_.rainfall;
/* 23 */     this.minHeight = p_i45381_2_.minHeight + 0.1F;
/* 24 */     this.maxHeight = p_i45381_2_.maxHeight + 0.2F;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public float getSpawningChance() { return this.baseBiome.getSpawningChance(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public boolean isEqualTo(BiomeGenBase p_150569_1_) { return this.baseBiome.isEqualTo(p_150569_1_); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public BiomeGenBase.TempCategory getTempCategory() { return this.baseBiome.getTempCategory(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenMutated.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */