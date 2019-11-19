/*    */ package minecraft.biome;
/*    */ 
/*    */ 
/*    */ public class BiomeGenSavanna
/*    */   extends BiomeGenBase
/*    */ {
/*  7 */   protected BiomeGenSavanna(int p_i45383_1_) { super(p_i45383_1_); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 12 */     Mutated var2 = new Mutated(p_180277_1_, this);
/* 13 */     var2.temperature = (this.temperature + 1.0F) * 0.5F;
/* 14 */     var2.minHeight = this.minHeight * 0.5F + 0.3F;
/* 15 */     var2.maxHeight = this.maxHeight * 0.5F + 1.2F;
/* 16 */     return var2;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Mutated
/*    */     extends BiomeGenMutated
/*    */   {
/* 23 */     public Mutated(int p_i45382_1_, BiomeGenBase p_i45382_2_) { super(p_i45382_1_, p_i45382_2_); }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenSavanna.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */