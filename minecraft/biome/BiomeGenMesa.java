/*    */ package minecraft.biome;
/*    */ 
/*    */ public class BiomeGenMesa
/*    */   extends BiomeGenBase
/*    */ {
/*    */   private boolean field_150620_aI;
/*    */   
/*    */   public BiomeGenMesa(int p_i45380_1_, boolean p_i45380_2_, boolean p_i45380_3_) {
/*  9 */     super(p_i45380_1_);
/* 10 */     this.field_150620_aI = p_i45380_3_;
/* 11 */     setDisableRain();
/* 12 */     setTemperatureRainfall(2.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 17 */     boolean var2 = (this.biomeID == BiomeGenBase.mesa.biomeID);
/* 18 */     BiomeGenMesa var3 = new BiomeGenMesa(p_180277_1_, var2, this.field_150620_aI);
/*    */     
/* 20 */     if (!var2) {
/*    */       
/* 22 */       var3.setHeight(height_LowHills);
/* 23 */       var3.setBiomeName(String.valueOf(this.biomeName) + " M");
/*    */     }
/*    */     else {
/*    */       
/* 27 */       var3.setBiomeName(String.valueOf(this.biomeName) + " (Bryce)");
/*    */     } 
/*    */     
/* 30 */     var3.func_150557_a(this.color, true);
/* 31 */     return var3;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenMesa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */