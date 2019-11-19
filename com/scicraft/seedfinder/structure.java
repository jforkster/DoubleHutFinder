/*    */ package com.scicraft.seedfinder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class structure
/*    */ {
/*  8 */   public long xPart(int x) { return x * 341873128712L; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   public long zPart(int z) { return z * 132897987541L; }
/*    */   
/*    */   public abstract xzPair structurePosInRegion(long paramLong1, long paramLong2, long paramLong3);
/*    */   
/*    */   public abstract xzPair structurePosInRegionFast(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract boolean structureWillSpawn(int paramInt1, int paramInt2, int paramInt3, int paramInt4, biomeGenerator parambiomeGenerator);
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/structure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */