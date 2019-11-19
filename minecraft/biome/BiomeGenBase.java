/*     */ package minecraft.biome;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class BiomeGenBase
/*     */ {
/*  10 */   protected static final Height height_Default = new Height(0.1F, 0.2F);
/*  11 */   protected static final Height height_ShallowWaters = new Height(-0.5F, 0.0F);
/*  12 */   protected static final Height height_Oceans = new Height(-1.0F, 0.1F);
/*  13 */   protected static final Height height_DeepOceans = new Height(-1.8F, 0.1F);
/*  14 */   protected static final Height height_LowPlains = new Height(0.125F, 0.05F);
/*  15 */   protected static final Height height_MidPlains = new Height(0.2F, 0.2F);
/*  16 */   protected static final Height height_LowHills = new Height(0.45F, 0.3F);
/*  17 */   protected static final Height height_HighPlateaus = new Height(1.5F, 0.025F);
/*  18 */   protected static final Height height_MidHills = new Height(1.0F, 0.5F);
/*  19 */   protected static final Height height_Shores = new Height(0.0F, 0.025F);
/*  20 */   protected static final Height height_RockyWaters = new Height(0.1F, 0.8F);
/*  21 */   protected static final Height height_LowIslands = new Height(0.2F, 0.3F);
/*  22 */   protected static final Height height_PartiallySubmerged = new Height(-0.2F, 0.1F);
/*     */ 
/*     */   
/*  25 */   private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
/*  26 */   public static final Set<BiomeGenBase> explorationBiomesList = Sets.newHashSet();
/*  27 */   public static final Map<String, BiomeGenBase> field_180278_o = Maps.newHashMap();
/*  28 */   public static final BiomeGenBase ocean = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
/*  29 */   public static final BiomeGenBase plains = (new BiomeGenPlains(1)).setColor(9286496).setBiomeName("Plains");
/*  30 */   public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowPlains);
/*  31 */   public static final BiomeGenBase extremeHills = (new BiomeGenHills(3, false)).setColor(6316128).setBiomeName("Extreme Hills").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
/*  32 */   public static final BiomeGenBase forest = (new BiomeGenForest(4, 0)).setColor(353825).setBiomeName("Forest");
/*  33 */   public static final BiomeGenBase taiga = (new BiomeGenTaiga(5, 0)).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_MidPlains);
/*  34 */   public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8F, 0.9F);
/*  35 */   public static final BiomeGenBase river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
/*  36 */   public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
/*     */ 
/*     */   
/*  39 */   public static final BiomeGenBase sky = (new BiomeGenEnd(9)).setColor(8421631).setBiomeName("The End").setDisableRain();
/*  40 */   public static final BiomeGenBase frozenOcean = (new BiomeGenOcean(10)).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans).setTemperatureRainfall(0.0F, 0.5F);
/*  41 */   public static final BiomeGenBase frozenRiver = (new BiomeGenRiver(11)).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters).setTemperatureRainfall(0.0F, 0.5F);
/*  42 */   public static final BiomeGenBase icePlains = (new BiomeGenSnow(12, false)).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(height_LowPlains);
/*  43 */   public static final BiomeGenBase iceMountains = (new BiomeGenSnow(13, false)).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills).setTemperatureRainfall(0.0F, 0.5F);
/*  44 */   public static final BiomeGenBase mushroomIsland = (new BiomeGenMushroomIsland(14)).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_LowIslands);
/*  45 */   public static final BiomeGenBase mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_Shores);
/*     */ 
/*     */   
/*  48 */   public static final BiomeGenBase beach = (new BiomeGenBeach(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setHeight(height_Shores);
/*     */ 
/*     */   
/*  51 */   public static final BiomeGenBase desertHills = (new BiomeGenDesert(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowHills);
/*     */ 
/*     */   
/*  54 */   public static final BiomeGenBase forestHills = (new BiomeGenForest(18, 0)).setColor(2250012).setBiomeName("ForestHills").setHeight(height_LowHills);
/*     */ 
/*     */   
/*  57 */   public static final BiomeGenBase taigaHills = (new BiomeGenTaiga(19, 0)).setColor(1456435).setBiomeName("TaigaHills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_LowHills);
/*     */ 
/*     */   
/*  60 */   public static final BiomeGenBase extremeHillsEdge = (new BiomeGenHills(20, true)).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2F, 0.3F);
/*     */ 
/*     */   
/*  63 */   public static final BiomeGenBase jungle = (new BiomeGenJungle(21, false)).setColor(5470985).setBiomeName("Jungle").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F);
/*  64 */   public static final BiomeGenBase jungleHills = (new BiomeGenJungle(22, false)).setColor(2900485).setBiomeName("JungleHills").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F).setHeight(height_LowHills);
/*  65 */   public static final BiomeGenBase jungleEdge = (new BiomeGenJungle(23, true)).setColor(6458135).setBiomeName("JungleEdge").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.8F);
/*  66 */   public static final BiomeGenBase deepOcean = (new BiomeGenOcean(24)).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
/*  67 */   public static final BiomeGenBase stoneBeach = (new BiomeGenStoneBeach(25)).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2F, 0.3F).setHeight(height_RockyWaters);
/*  68 */   public static final BiomeGenBase coldBeach = (new BiomeGenBeach(26)).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05F, 0.3F).setHeight(height_Shores).setEnableSnow();
/*  69 */   public static final BiomeGenBase birchForest = (new BiomeGenForest(27, 2)).setBiomeName("Birch Forest").setColor(3175492);
/*  70 */   public static final BiomeGenBase birchForestHills = (new BiomeGenForest(28, 2)).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills);
/*  71 */   public static final BiomeGenBase roofedForest = (new BiomeGenForest(29, 3)).setColor(4215066).setBiomeName("Roofed Forest");
/*  72 */   public static final BiomeGenBase coldTaiga = (new BiomeGenTaiga(30, 0)).setColor(3233098).setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_MidPlains).func_150563_c(16777215);
/*  73 */   public static final BiomeGenBase coldTaigaHills = (new BiomeGenTaiga(31, 0)).setColor(2375478).setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_LowHills).func_150563_c(16777215);
/*  74 */   public static final BiomeGenBase megaTaiga = (new BiomeGenTaiga(32, 1)).setColor(5858897).setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_MidPlains);
/*  75 */   public static final BiomeGenBase megaTaigaHills = (new BiomeGenTaiga(33, 1)).setColor(4542270).setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_LowHills);
/*  76 */   public static final BiomeGenBase extremeHillsPlus = (new BiomeGenHills(34, true)).setColor(5271632).setBiomeName("Extreme Hills+").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
/*  77 */   public static final BiomeGenBase savanna = (new BiomeGenSavanna(35)).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2F, 0.0F).setDisableRain().setHeight(height_LowPlains);
/*  78 */   public static final BiomeGenBase savannaPlateau = (new BiomeGenSavanna(36)).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0F, 0.0F).setDisableRain().setHeight(height_HighPlateaus);
/*  79 */   public static final BiomeGenBase mesa = (new BiomeGenMesa(37, false, false)).setColor(14238997).setBiomeName("Mesa");
/*  80 */   public static final BiomeGenBase mesaPlateau_F = (new BiomeGenMesa(38, false, true)).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus);
/*  81 */   public static final BiomeGenBase mesaPlateau = (new BiomeGenMesa(39, false, false)).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus);
/*  82 */   public static final BiomeGenBase field_180279_ad = ocean;
/*     */ 
/*     */   
/*     */   public String biomeName;
/*     */ 
/*     */   
/*     */   public int color;
/*     */ 
/*     */   
/*     */   public int field_150609_ah;
/*     */ 
/*     */   
/*     */   public int fillerBlockMetadata;
/*     */ 
/*     */   
/*     */   public float minHeight;
/*     */ 
/*     */   
/*     */   public float maxHeight;
/*     */ 
/*     */   
/*     */   public float temperature;
/*     */   
/*     */   public float rainfall;
/*     */   
/*     */   public int waterColorMultiplier;
/*     */   
/*     */   protected boolean enableSnow;
/*     */   
/*     */   protected boolean enableRain;
/*     */   
/*     */   public final int biomeID;
/*     */ 
/*     */   
/*     */   protected BiomeGenBase(int p_i1971_1_) {
/* 117 */     this.fillerBlockMetadata = 5169201;
/* 118 */     this.minHeight = height_Default.rootHeight;
/* 119 */     this.maxHeight = height_Default.variation;
/* 120 */     this.temperature = 0.5F;
/* 121 */     this.rainfall = 0.5F;
/* 122 */     this.waterColorMultiplier = 16777215;
/* 123 */     this.enableRain = true;
/*     */     
/* 125 */     this.biomeID = p_i1971_1_;
/* 126 */     biomeList[p_i1971_1_] = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setTemperatureRainfall(float p_76732_1_, float p_76732_2_) {
/* 135 */     if (p_76732_1_ > 0.1F && p_76732_1_ < 0.2F)
/*     */     {
/* 137 */       throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
/*     */     }
/*     */ 
/*     */     
/* 141 */     this.temperature = p_76732_1_;
/* 142 */     this.rainfall = p_76732_2_;
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BiomeGenBase setHeight(Height p_150570_1_) {
/* 149 */     this.minHeight = p_150570_1_.rootHeight;
/* 150 */     this.maxHeight = p_150570_1_.variation;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setDisableRain() {
/* 159 */     this.enableRain = false;
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setEnableSnow() {
/* 168 */     this.enableSnow = true;
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setBiomeName(String p_76735_1_) {
/* 174 */     this.biomeName = p_76735_1_;
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setFillerBlockMetadata(int p_76733_1_) {
/* 180 */     this.fillerBlockMetadata = p_76733_1_;
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setColor(int p_76739_1_) {
/* 186 */     func_150557_a(p_76739_1_, false);
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase func_150563_c(int p_150563_1_) {
/* 192 */     this.field_150609_ah = p_150563_1_;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_) {
/* 198 */     this.color = p_150557_1_;
/*     */     
/* 200 */     if (p_150557_2_) {
/*     */       
/* 202 */       this.field_150609_ah = (p_150557_1_ & 0xFEFEFE) >> 1;
/*     */     }
/*     */     else {
/*     */       
/* 206 */       this.field_150609_ah = p_150557_1_;
/*     */     } 
/*     */     
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 218 */   public boolean getEnableSnow() { return isSnowyBiome(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public boolean canSpawnLightningBolt() { return isSnowyBiome() ? false : this.enableRain; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   public boolean isHighHumidity() { return (this.rainfall > 0.85F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   public float getSpawningChance() { return 0.1F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 250 */   public final int getIntRainfall() { return (int)(this.rainfall * 65536.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public final float getFloatRainfall() { return this.rainfall; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   public boolean isSnowyBiome() { return this.enableSnow; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   protected BiomeGenBase createMutation() { return createMutatedBiome(this.biomeID + 128); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 277 */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) { return new BiomeGenMutated(p_180277_1_, this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 282 */   public Class<? extends BiomeGenBase> getBiomeClass() { return getClass(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 290 */   public boolean isEqualTo(BiomeGenBase p_150569_1_) { return (p_150569_1_ == this) ? true : ((p_150569_1_ == null) ? false : ((getBiomeClass() == p_150569_1_.getBiomeClass()))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 295 */   public TempCategory getTempCategory() { return (this.temperature < 0.2D) ? TempCategory.COLD : ((this.temperature < 1.0D) ? TempCategory.MEDIUM : TempCategory.WARM); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   public static BiomeGenBase[] getBiomeGenArray() { return biomeList; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public static BiomeGenBase getBiome(int p_150568_0_) { return getBiomeFromBiomeList(p_150568_0_, null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BiomeGenBase getBiomeFromBiomeList(int p_180276_0_, BiomeGenBase p_180276_1_) {
/* 313 */     if (p_180276_0_ >= 0 && p_180276_0_ <= biomeList.length) {
/*     */       
/* 315 */       BiomeGenBase var2 = biomeList[p_180276_0_];
/* 316 */       return (var2 == null) ? p_180276_1_ : var2;
/*     */     } 
/*     */ 
/*     */     
/* 320 */     System.err.println("Biome ID is out of bounds: " + p_180276_0_ + ", defaulting to 0 (Ocean)");
/* 321 */     return ocean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static  {
/* 327 */     plains.createMutation();
/* 328 */     desert.createMutation();
/* 329 */     forest.createMutation();
/* 330 */     taiga.createMutation();
/* 331 */     swampland.createMutation();
/* 332 */     icePlains.createMutation();
/* 333 */     jungle.createMutation();
/* 334 */     jungleEdge.createMutation();
/* 335 */     coldTaiga.createMutation();
/* 336 */     savanna.createMutation();
/* 337 */     savannaPlateau.createMutation();
/* 338 */     mesa.createMutation();
/* 339 */     mesaPlateau_F.createMutation();
/* 340 */     mesaPlateau.createMutation();
/* 341 */     birchForest.createMutation();
/* 342 */     birchForestHills.createMutation();
/* 343 */     roofedForest.createMutation();
/* 344 */     megaTaiga.createMutation();
/* 345 */     extremeHills.createMutation();
/* 346 */     extremeHillsPlus.createMutation();
/* 347 */     megaTaiga.createMutatedBiome(megaTaigaHills.biomeID + 128).setBiomeName("Redwood Taiga Hills M");
/* 348 */     var0 = biomeList;
/* 349 */     int var1 = var0.length;
/*     */     
/* 351 */     for (int var2 = 0; var2 < var1; var2++) {
/*     */       
/* 353 */       BiomeGenBase var3 = var0[var2];
/*     */       
/* 355 */       if (var3 != null) {
/*     */         
/* 357 */         if (field_180278_o.containsKey(var3.biomeName))
/*     */         {
/* 359 */           throw new Error("Biome \"" + var3.biomeName + "\" is defined as both ID " + ((BiomeGenBase)field_180278_o.get(var3.biomeName)).biomeID + " and " + var3.biomeID);
/*     */         }
/*     */         
/* 362 */         field_180278_o.put(var3.biomeName, var3);
/*     */         
/* 364 */         if (var3.biomeID < 128)
/*     */         {
/* 366 */           explorationBiomesList.add(var3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 371 */     explorationBiomesList.remove(hell);
/* 372 */     explorationBiomesList.remove(sky);
/* 373 */     explorationBiomesList.remove(frozenOcean);
/* 374 */     explorationBiomesList.remove(extremeHillsEdge);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Height
/*     */   {
/*     */     public float rootHeight;
/*     */     public float variation;
/*     */     
/*     */     public Height(float p_i45371_1_, float p_i45371_2_) {
/* 384 */       this.rootHeight = p_i45371_1_;
/* 385 */       this.variation = p_i45371_2_;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 390 */     public Height attenuate() { return new Height(this.rootHeight * 0.8F, this.variation * 0.6F); }
/*     */   }
/*     */ 
/*     */   
/*     */   public enum TempCategory
/*     */   {
/* 396 */     OCEAN("OCEAN", 0),
/* 397 */     COLD("COLD", 1),
/* 398 */     MEDIUM("MEDIUM", 2),
/* 399 */     WARM("WARM", 3);
/*     */     static  {
/* 401 */       $VALUES = new TempCategory[] { OCEAN, COLD, MEDIUM, WARM };
/*     */     }
/*     */     
/*     */     private static final TempCategory[] $VALUES;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/minecraft/biome/BiomeGenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */