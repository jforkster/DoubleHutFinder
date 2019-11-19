/*    */ package minecraft.biome;
/*    */ 
/*    */ public class BiomeGenForest
/*    */   extends BiomeGenBase
/*    */ {
/*    */   private int field_150632_aF;
/*    */   
/*    */   public BiomeGenForest(int p_i45377_1_, int p_i45377_2_) {
/*  9 */     super(p_i45377_1_);
/* 10 */     this.field_150632_aF = p_i45377_2_;
/*    */     
/* 12 */     setFillerBlockMetadata(5159473);
/* 13 */     setTemperatureRainfall(0.7F, 0.8F);
/*    */     
/* 15 */     if (this.field_150632_aF == 2) {
/*    */       
/* 17 */       this.field_150609_ah = 353825;
/* 18 */       this.color = 3175492;
/* 19 */       setTemperatureRainfall(0.6F, 0.6F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
/* 25 */     if (this.field_150632_aF == 2) {
/*    */       
/* 27 */       this.field_150609_ah = 353825;
/* 28 */       this.color = p_150557_1_;
/*    */       
/* 30 */       if (p_150557_2_)
/*    */       {
/* 32 */         this.field_150609_ah = (this.field_150609_ah & 0xFEFEFE) >> 1;
/*    */       }
/*    */       
/* 35 */       return this;
/*    */     } 
/*    */ 
/*    */     
/* 39 */     return super.func_150557_a(p_150557_1_, p_150557_2_);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 45 */     if (this.biomeID == BiomeGenBase.forest.biomeID) {
/*    */       
/* 47 */       BiomeGenForest var2 = new BiomeGenForest(p_180277_1_, 1);
/* 48 */       var2.setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight + 0.2F));
/* 49 */       var2.setBiomeName("Flower Forest");
/* 50 */       var2.func_150557_a(6976549, true);
/* 51 */       var2.setFillerBlockMetadata(8233509);
/* 52 */       return var2;
/*    */     } 
/*    */ 
/*    */     
/* 56 */     return new BiomeGenMutated(p_180277_1_, this);
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenForest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */