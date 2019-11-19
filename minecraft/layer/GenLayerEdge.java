/*     */ package minecraft.layer;
/*     */ 
/*     */ public class GenLayerEdge
/*     */   extends GenLayer
/*     */ {
/*     */   private final Mode field_151627_c;
/*     */   
/*     */   public GenLayerEdge(long p_i45474_1_, GenLayer p_i45474_3_, Mode p_i45474_4_) {
/*   9 */     super(p_i45474_1_);
/*  10 */     this.parent = p_i45474_3_;
/*  11 */     this.field_151627_c = p_i45474_4_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  20 */     switch (SwitchMode.field_151642_a[this.field_151627_c.ordinal()]) {
/*     */ 
/*     */       
/*     */       default:
/*  24 */         return getIntsCoolWarm(areaX, areaY, areaWidth, areaHeight);
/*     */       
/*     */       case 2:
/*  27 */         return getIntsHeatIce(areaX, areaY, areaWidth, areaHeight);
/*     */       case 3:
/*     */         break;
/*  30 */     }  return getIntsSpecial(areaX, areaY, areaWidth, areaHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] getIntsCoolWarm(int p_151626_1_, int p_151626_2_, int p_151626_3_, int p_151626_4_) {
/*  36 */     int var5 = p_151626_1_ - 1;
/*  37 */     int var6 = p_151626_2_ - 1;
/*  38 */     int var7 = 1 + p_151626_3_ + 1;
/*  39 */     int var8 = 1 + p_151626_4_ + 1;
/*  40 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/*  41 */     int[] var10 = IntCache.getIntCache(p_151626_3_ * p_151626_4_);
/*     */     
/*  43 */     for (int var11 = 0; var11 < p_151626_4_; var11++) {
/*     */       
/*  45 */       for (int var12 = 0; var12 < p_151626_3_; var12++) {
/*     */         
/*  47 */         initChunkSeed((var12 + p_151626_1_), (var11 + p_151626_2_));
/*  48 */         int var13 = var9[var12 + 1 + (var11 + 1) * var7];
/*     */         
/*  50 */         if (var13 == 1) {
/*     */           
/*  52 */           int var14 = var9[var12 + 1 + (var11 + 1 - 1) * var7];
/*  53 */           int var15 = var9[var12 + 1 + 1 + (var11 + 1) * var7];
/*  54 */           int var16 = var9[var12 + 1 - 1 + (var11 + 1) * var7];
/*  55 */           int var17 = var9[var12 + 1 + (var11 + 1 + 1) * var7];
/*  56 */           boolean var18 = !(var14 != 3 && var15 != 3 && var16 != 3 && var17 != 3);
/*  57 */           boolean var19 = !(var14 != 4 && var15 != 4 && var16 != 4 && var17 != 4);
/*     */           
/*  59 */           if (var18 || var19)
/*     */           {
/*  61 */             var13 = 2;
/*     */           }
/*     */         } 
/*     */         
/*  65 */         var10[var12 + var11 * p_151626_3_] = var13;
/*     */       } 
/*     */     } 
/*     */     
/*  69 */     return var10;
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] getIntsHeatIce(int p_151624_1_, int p_151624_2_, int p_151624_3_, int p_151624_4_) {
/*  74 */     int var5 = p_151624_1_ - 1;
/*  75 */     int var6 = p_151624_2_ - 1;
/*  76 */     int var7 = 1 + p_151624_3_ + 1;
/*  77 */     int var8 = 1 + p_151624_4_ + 1;
/*  78 */     int[] var9 = this.parent.getInts(var5, var6, var7, var8);
/*  79 */     int[] var10 = IntCache.getIntCache(p_151624_3_ * p_151624_4_);
/*     */     
/*  81 */     for (int var11 = 0; var11 < p_151624_4_; var11++) {
/*     */       
/*  83 */       for (int var12 = 0; var12 < p_151624_3_; var12++) {
/*     */         
/*  85 */         int var13 = var9[var12 + 1 + (var11 + 1) * var7];
/*     */         
/*  87 */         if (var13 == 4) {
/*     */           
/*  89 */           int var14 = var9[var12 + 1 + (var11 + 1 - 1) * var7];
/*  90 */           int var15 = var9[var12 + 1 + 1 + (var11 + 1) * var7];
/*  91 */           int var16 = var9[var12 + 1 - 1 + (var11 + 1) * var7];
/*  92 */           int var17 = var9[var12 + 1 + (var11 + 1 + 1) * var7];
/*  93 */           boolean var18 = !(var14 != 2 && var15 != 2 && var16 != 2 && var17 != 2);
/*  94 */           boolean var19 = !(var14 != 1 && var15 != 1 && var16 != 1 && var17 != 1);
/*     */           
/*  96 */           if (var19 || var18)
/*     */           {
/*  98 */             var13 = 3;
/*     */           }
/*     */         } 
/*     */         
/* 102 */         var10[var12 + var11 * p_151624_3_] = var13;
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     return var10;
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] getIntsSpecial(int p_151625_1_, int p_151625_2_, int p_151625_3_, int p_151625_4_) {
/* 111 */     int[] var5 = this.parent.getInts(p_151625_1_, p_151625_2_, p_151625_3_, p_151625_4_);
/* 112 */     int[] var6 = IntCache.getIntCache(p_151625_3_ * p_151625_4_);
/*     */     
/* 114 */     for (int var7 = 0; var7 < p_151625_4_; var7++) {
/*     */       
/* 116 */       for (int var8 = 0; var8 < p_151625_3_; var8++) {
/*     */         
/* 118 */         initChunkSeed((var8 + p_151625_1_), (var7 + p_151625_2_));
/* 119 */         int var9 = var5[var8 + var7 * p_151625_3_];
/*     */         
/* 121 */         if (var9 != 0 && nextInt(13) == 0)
/*     */         {
/* 123 */           var9 |= 1 + nextInt(15) << 8 & 0xF00;
/*     */         }
/*     */         
/* 126 */         var6[var8 + var7 * p_151625_3_] = var9;
/*     */       } 
/*     */     } 
/*     */     
/* 130 */     return var6;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 135 */     COOL_WARM("COOL_WARM", 0),
/* 136 */     HEAT_ICE("HEAT_ICE", 1),
/* 137 */     SPECIAL("SPECIAL", 2);
/*     */     static  {
/* 139 */       $VALUES = new Mode[] { COOL_WARM, HEAT_ICE, SPECIAL };
/*     */     }
/*     */     
/*     */     private static final Mode[] $VALUES;
/*     */   }
/*     */   
/*     */   static final class SwitchMode {
/* 146 */     static final int[] field_151642_a = new int[GenLayerEdge.Mode.values().length];
/*     */ 
/*     */     
/*     */     static  {
/*     */       try {
/* 151 */         field_151642_a[GenLayerEdge.Mode.COOL_WARM.ordinal()] = 1;
/*     */       }
/* 153 */       catch (NoSuchFieldError noSuchFieldError) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 160 */         field_151642_a[GenLayerEdge.Mode.HEAT_ICE.ordinal()] = 2;
/*     */       }
/* 162 */       catch (NoSuchFieldError noSuchFieldError) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 169 */         field_151642_a[GenLayerEdge.Mode.SPECIAL.ordinal()] = 3;
/*     */       }
/* 171 */       catch (NoSuchFieldError noSuchFieldError) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/GenLayerEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */