/*    */ package minecraft.biome;
/*    */ 
/*    */ 
/*    */ public class BiomeGenSnow
/*    */   extends BiomeGenBase
/*    */ {
/*  7 */   public BiomeGenSnow(int p_i45378_1_, boolean p_i45378_2_) { super(p_i45378_1_); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 12 */     BiomeGenBase var2 = (new BiomeGenSnow(p_180277_1_, true)).func_150557_a(13828095, true).setBiomeName(String.valueOf(this.biomeName) + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(new BiomeGenBase.Height(this.minHeight + 0.1F, this.maxHeight + 0.1F));
/* 13 */     var2.minHeight = this.minHeight + 0.3F;
/* 14 */     var2.maxHeight = this.maxHeight + 0.4F;
/* 15 */     return var2;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */