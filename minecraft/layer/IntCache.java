/*     */ package minecraft.layer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ public class IntCache
/*     */ {
/*   8 */   private static int intCacheSize = 256;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  13 */   private static List<int[]> freeSmallArrays = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  19 */   private static List<int[]> inUseSmallArrays = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   private static List<int[]> freeLargeArrays = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   private static List<int[]> inUseLargeArrays = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getIntCache(int p_76445_0_) {
/*  36 */     if (p_76445_0_ <= 256) {
/*     */       
/*  38 */       if (freeSmallArrays.isEmpty()) {
/*     */         
/*  40 */         int[] var1 = new int[256];
/*  41 */         inUseSmallArrays.add(var1);
/*  42 */         return var1;
/*     */       } 
/*     */ 
/*     */       
/*  46 */       int[] var1 = (int[])freeSmallArrays.remove(freeSmallArrays.size() - 1);
/*  47 */       inUseSmallArrays.add(var1);
/*  48 */       return var1;
/*     */     } 
/*     */     
/*  51 */     if (p_76445_0_ > intCacheSize) {
/*     */       
/*  53 */       intCacheSize = p_76445_0_;
/*  54 */       freeLargeArrays.clear();
/*  55 */       inUseLargeArrays.clear();
/*  56 */       int[] var1 = new int[intCacheSize];
/*  57 */       inUseLargeArrays.add(var1);
/*  58 */       return var1;
/*     */     } 
/*  60 */     if (freeLargeArrays.isEmpty()) {
/*     */       
/*  62 */       int[] var1 = new int[intCacheSize];
/*  63 */       inUseLargeArrays.add(var1);
/*  64 */       return var1;
/*     */     } 
/*     */ 
/*     */     
/*  68 */     int[] var1 = (int[])freeLargeArrays.remove(freeLargeArrays.size() - 1);
/*  69 */     inUseLargeArrays.add(var1);
/*  70 */     return var1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resetIntCache() {
/*  79 */     if (!freeLargeArrays.isEmpty())
/*     */     {
/*  81 */       freeLargeArrays.remove(freeLargeArrays.size() - 1);
/*     */     }
/*     */     
/*  84 */     if (!freeSmallArrays.isEmpty())
/*     */     {
/*  86 */       freeSmallArrays.remove(freeSmallArrays.size() - 1);
/*     */     }
/*     */     
/*  89 */     freeLargeArrays.addAll(inUseLargeArrays);
/*  90 */     freeSmallArrays.addAll(inUseSmallArrays);
/*  91 */     inUseLargeArrays.clear();
/*  92 */     inUseSmallArrays.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static String getCacheSizes() { return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/layer/IntCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */