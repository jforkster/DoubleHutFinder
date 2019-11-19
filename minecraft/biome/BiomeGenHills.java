/*    */ package minecraft.biome;
/*    */ 
/*    */ public class BiomeGenHills
/*    */   extends BiomeGenBase
/*    */ {
/*    */   private int field_150635_aE;
/*    */   private int field_150636_aF;
/*    */   private int field_150637_aG;
/*    */   private int field_150638_aH;
/*    */   
/*    */   protected BiomeGenHills(int p_i45373_1_, boolean p_i45373_2_) {
/* 12 */     super(p_i45373_1_);
/* 13 */     this.field_150635_aE = 0;
/* 14 */     this.field_150636_aF = 1;
/* 15 */     this.field_150637_aG = 2;
/* 16 */     this.field_150638_aH = this.field_150635_aE;
/*    */     
/* 18 */     if (p_i45373_2_)
/*    */     {
/* 20 */       this.field_150638_aH = this.field_150636_aF;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private BiomeGenHills mutateHills(BiomeGenBase p_150633_1_) {
/* 29 */     this.field_150638_aH = this.field_150637_aG;
/* 30 */     func_150557_a(p_150633_1_.color, true);
/* 31 */     setBiomeName(String.valueOf(p_150633_1_.biomeName) + " M");
/* 32 */     setHeight(new BiomeGenBase.Height(p_150633_1_.minHeight, p_150633_1_.maxHeight));
/* 33 */     setTemperatureRainfall(p_150633_1_.temperature, p_150633_1_.rainfall);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) { return (new BiomeGenHills(p_180277_1_, false)).mutateHills(this); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenHills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */