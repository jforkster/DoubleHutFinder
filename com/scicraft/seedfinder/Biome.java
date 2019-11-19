/*     */ package com.scicraft.seedfinder;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ public class Biome
/*     */ {
/*   8 */   public static final HashMap<String, Biome> biomeMap = new HashMap();
/*   9 */   public static final BiomeType typeA = new BiomeType(0.1F, 0.2F);
/*  10 */   public static final BiomeType typeB = new BiomeType(-0.5F, 0.0F);
/*  11 */   public static final BiomeType typeC = new BiomeType(-1.0F, 0.1F);
/*  12 */   public static final BiomeType typeD = new BiomeType(-1.8F, 0.1F);
/*  13 */   public static final BiomeType typeE = new BiomeType(0.125F, 0.05F);
/*  14 */   public static final BiomeType typeF = new BiomeType(0.2F, 0.2F);
/*  15 */   public static final BiomeType typeG = new BiomeType(0.45F, 0.3F);
/*  16 */   public static final BiomeType typeH = new BiomeType(1.5F, 0.025F);
/*  17 */   public static final BiomeType typeI = new BiomeType(1.0F, 0.5F);
/*  18 */   public static final BiomeType typeJ = new BiomeType(0.0F, 0.025F);
/*  19 */   public static final BiomeType typeK = new BiomeType(0.1F, 0.8F);
/*  20 */   public static final BiomeType typeL = new BiomeType(0.2F, 0.3F);
/*  21 */   public static final BiomeType typeM = new BiomeType(-0.2F, 0.1F);
/*     */   
/*  23 */   public static final Biome[] biomes = new Biome[256];
/*  24 */   public static final Biome ocean = new Biome("Ocean", 0, typeC);
/*  25 */   public static final Biome plains = new Biome("Plains", 1, typeA);
/*  26 */   public static final Biome desert = new Biome("Desert", 2, typeE);
/*  27 */   public static final Biome extremeHills = new Biome("Extreme Hills", 3, typeI);
/*  28 */   public static final Biome forest = new Biome("Forest", 4, typeA);
/*  29 */   public static final Biome taiga = new Biome("Taiga", 5, typeF);
/*  30 */   public static final Biome swampland = new Biome("Swampland", 6, typeM);
/*  31 */   public static final Biome river = new Biome("River", 7, typeB);
/*  32 */   public static final Biome hell = new Biome("Hell", 8, typeA);
/*  33 */   public static final Biome sky = new Biome("Sky", 9, typeA);
/*  34 */   public static final Biome frozenOcean = new Biome("Frozen Ocean", 10, typeC);
/*  35 */   public static final Biome frozenRiver = new Biome("Frozen River", 11, typeB);
/*  36 */   public static final Biome icePlains = new Biome("Ice Plains", 12, typeE);
/*  37 */   public static final Biome iceMountains = new Biome("Ice Mountains", 13, typeG);
/*  38 */   public static final Biome mushroomIsland = new Biome("Mushroom Island", 14, typeL);
/*  39 */   public static final Biome mushroomIslandShore = new Biome("Mushroom Island Shore", 15, typeJ);
/*  40 */   public static final Biome beach = new Biome("Beach", 16, typeJ);
/*  41 */   public static final Biome desertHills = new Biome("Desert Hills", 17, typeG);
/*  42 */   public static final Biome forestHills = new Biome("Forest Hills", 18, typeG);
/*  43 */   public static final Biome taigaHills = new Biome("Taiga Hills", 19, typeG);
/*  44 */   public static final Biome extremeHillsEdge = new Biome("Extreme Hills Edge", 20, typeI.getExtreme());
/*  45 */   public static final Biome jungle = new Biome("Jungle", 21, typeA);
/*  46 */   public static final Biome jungleHills = new Biome("Jungle Hills", 22, typeG);
/*  47 */   public static final Biome jungleEdge = new Biome("Jungle Edge", 23, typeA);
/*  48 */   public static final Biome deepOcean = new Biome("Deep Ocean", 24, typeD);
/*  49 */   public static final Biome stoneBeach = new Biome("Stone Beach", 25, typeK);
/*  50 */   public static final Biome coldBeach = new Biome("Cold Beach", 26, typeJ);
/*  51 */   public static final Biome birchForest = new Biome("Birch Forest", 27, typeA);
/*  52 */   public static final Biome birchForestHills = new Biome("Birch Forest Hills", 28, typeG);
/*  53 */   public static final Biome roofedForest = new Biome("Roofed Forest", 29, typeA);
/*  54 */   public static final Biome coldTaiga = new Biome("Cold Taiga", 30, typeF);
/*  55 */   public static final Biome coldTaigaHills = new Biome("Cold Taiga Hills", 31, typeG);
/*  56 */   public static final Biome megaTaiga = new Biome("Mega Taiga", 32, typeF);
/*  57 */   public static final Biome megaTaigaHills = new Biome("Mega Taiga Hills", 33, typeG);
/*  58 */   public static final Biome extremeHillsPlus = new Biome("Extreme Hills+", 34, typeI);
/*  59 */   public static final Biome savanna = new Biome("Savanna", 35, typeE);
/*  60 */   public static final Biome savannaPlateau = new Biome("Savanna Plateau", 36, typeH);
/*  61 */   public static final Biome mesa = new Biome("Mesa", 37, typeA);
/*  62 */   public static final Biome mesaPlateauF = new Biome("Mesa Plateau F", 38, typeH);
/*  63 */   public static final Biome mesaPlateau = new Biome("Mesa Plateau", 39, typeH);
/*     */ 
/*     */   
/*  66 */   public static final Biome oceanM = new Biome("Ocean M", 128);
/*  67 */   public static final Biome sunflowerPlains = new Biome("Sunflower Plains", 129);
/*  68 */   public static final Biome desertM = new Biome("Desert M", 130);
/*  69 */   public static final Biome extremeHillsM = new Biome("Extreme Hills M", 131);
/*  70 */   public static final Biome flowerForest = new Biome("Flower Forest", 132);
/*  71 */   public static final Biome taigaM = new Biome("Taiga M", 133);
/*  72 */   public static final Biome swamplandM = new Biome("Swampland M", 134);
/*  73 */   public static final Biome riverM = new Biome("River M", 135);
/*  74 */   public static final Biome hellM = new Biome("Hell M", 136);
/*  75 */   public static final Biome skyM = new Biome("Sky M", 137);
/*  76 */   public static final Biome frozenOceanM = new Biome("Frozen Ocean M", 138);
/*  77 */   public static final Biome frozenRiverM = new Biome("Frozen River M", 139);
/*  78 */   public static final Biome icePlainsSpikes = new Biome("Ice Plains Spikes", 140);
/*  79 */   public static final Biome iceMountainsM = new Biome("Ice Mountains M", 141);
/*  80 */   public static final Biome mushroomIslandM = new Biome("Mushroom Island M", 142);
/*  81 */   public static final Biome mushroomIslandShoreM = new Biome("Mushroom Island Shore M", 143);
/*  82 */   public static final Biome beachM = new Biome("Beach M", 144);
/*  83 */   public static final Biome desertHillsM = new Biome("Desert Hills M", 145);
/*  84 */   public static final Biome forestHillsM = new Biome("Forest Hills M", 146);
/*  85 */   public static final Biome taigaHillsM = new Biome("Taiga Hills M", 147);
/*  86 */   public static final Biome extremeHillsEdgeM = new Biome("Extreme Hills Edge M", 148);
/*  87 */   public static final Biome jungleM = new Biome("Jungle M", 149);
/*  88 */   public static final Biome jungleHillsM = new Biome("Jungle Hills M", 150);
/*  89 */   public static final Biome jungleEdgeM = new Biome("Jungle Edge M", 151);
/*  90 */   public static final Biome deepOceanM = new Biome("Deep Ocean M", 152);
/*  91 */   public static final Biome stoneBeachM = new Biome("Stone Beach M", 153);
/*  92 */   public static final Biome coldBeachM = new Biome("Cold Beach M", 154);
/*  93 */   public static final Biome birchForestM = new Biome("Birch Forest M", 155);
/*  94 */   public static final Biome birchForestHillsM = new Biome("Birch Forest Hills M", 156);
/*  95 */   public static final Biome roofedForestM = new Biome("Roofed Forest M", 157);
/*  96 */   public static final Biome coldTaigaM = new Biome("Cold Taiga M", 158);
/*  97 */   public static final Biome coldTaigaHillsM = new Biome("Cold Taiga Hills M", 159);
/*  98 */   public static final Biome megaSpruceTaiga = new Biome("Mega Spruce Taiga", 160);
/*  99 */   public static final Biome megaSpurceTaigaHills = new Biome("Mega Spruce Taiga (Hills)", 161);
/* 100 */   public static final Biome extremeHillsPlusM = new Biome("Extreme Hills+ M", 162);
/* 101 */   public static final Biome savannaM = new Biome("Savanna M", 163);
/* 102 */   public static final Biome savannaPlateauM = new Biome("Savanna Plateau M", 164);
/* 103 */   public static final Biome mesaBryce = new Biome("Mesa (Bryce)", 165);
/* 104 */   public static final Biome mesaPlateauFM = new Biome("Mesa Plateau F M", 166);
/* 105 */   public static final Biome mesaPlateauM = new Biome("Mesa Plateau M", 167);
/*     */   
/*     */   public String name;
/*     */   
/*     */   public int index;
/*     */   
/*     */   public int color;
/*     */   public BiomeType type;
/*     */   
/* 114 */   public Biome(String name, int index) { this(name, index, (biomes[index - 128]).type.getRare()); }
/*     */ 
/*     */   
/*     */   public Biome(String name, int index, BiomeType type) {
/* 118 */     biomes[index] = this;
/* 119 */     this.name = name;
/* 120 */     this.index = index;
/* 121 */     this.type = type;
/* 122 */     biomeMap.put(name, this);
/*     */   }
/*     */ 
/*     */   
/* 126 */   public String toString() { return "Biome." + this.name; }
/*     */ 
/*     */   
/*     */   public static int indexFromName(String name) {
/* 130 */     Biome biome = (Biome)biomeMap.get(name);
/* 131 */     if (biome != null)
/* 132 */       return biome.index; 
/* 133 */     return -1;
/*     */   }
/*     */   
/*     */   public static final class BiomeType {
/*     */     public float value1;
/*     */     
/*     */     public BiomeType(float value1, float value2) {
/* 140 */       this.value1 = value1;
/* 141 */       this.value2 = value2;
/*     */     }
/*     */     public float value2;
/*     */     
/* 145 */     public BiomeType getExtreme() { return new BiomeType(this.value1 * 0.8F, this.value2 * 0.6F); }
/*     */ 
/*     */     
/* 148 */     public BiomeType getRare() { return new BiomeType(this.value1 + 0.1F, this.value2 + 0.2F); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/scicraft/seedfinder/Biome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */