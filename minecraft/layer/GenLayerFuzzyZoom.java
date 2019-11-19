/*    */ package minecraft.layer;
/*    */ 
/*    */ 
/*    */ public class GenLayerFuzzyZoom
/*    */   extends GenLayerZoom
/*    */ {
/*  7 */   public GenLayerFuzzyZoom(long p_i2123_1_, GenLayer p_i2123_3_) { super(p_i2123_1_, p_i2123_3_); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_) { return selectRandom(new int[] { p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_ }); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerFuzzyZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */