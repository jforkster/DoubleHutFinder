/*    */ package minecraft.biome;
/*    */ 
/*    */ public class BiomeGenPlains
/*    */   extends BiomeGenBase
/*    */ {
/*    */   protected boolean field_150628_aC;
/*    */   
/*    */   protected BiomeGenPlains(int p_i1986_1_) {
/*  9 */     super(p_i1986_1_);
/* 10 */     setTemperatureRainfall(0.8F, 0.4F);
/* 11 */     setHeight(height_LowPlains);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 16 */     BiomeGenPlains var2 = new BiomeGenPlains(p_180277_1_);
/* 17 */     var2.setBiomeName("Sunflower Plains");
/* 18 */     var2.field_150628_aC = true;
/* 19 */     var2.setColor(9286496);
/* 20 */     var2.field_150609_ah = 14273354;
/* 21 */     return var2;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenPlains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */