/*     */ import com.scicraft.seedfinder.biomeGenerator;
/*     */ import com.scicraft.seedfinder.bitIterator;
/*     */ import com.scicraft.seedfinder.structureHut;
/*     */ import com.scicraft.seedfinder.xzPair;

import java.awt.event.ActionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuadHutFinder
/*     */   extends Thread implements ActionListener
/*     */ {
/*     */   public static final int MIN = 12;
/*     */   public static final int MAX = 13;
/*     */   public static final int TOPRIGHT = 0;
/*     */   public static final int BOTTOMRIGHT = 1;
/*     */   public static final int BOTTOMLEFT = 2;
/*     */   public static final int TOPLEFT = 3;
/*     */   public static final int BIOME = 6;
/*  24 */   public static Random rnd = new Random();
/*  25 */   public static int[] xpos = new int[4];
/*  26 */   public static int[] zpos = new int[4];
/*     */   public static int xmon;
/*     */   public static int zmon;
/*     */   public static structureHut hut;
/*     */   public static bitIterator bitIt;
/*     */   public static long currentSeed;
/*     */   
/*  33 */   public QuadHutFinder(Long seed) { currentSeed = seed.longValue(); }
/*     */ 
/*    */   public void actionPerformed(ActionEvent event) {
/* 43 */     if (!Main.running) {
/* 44 */       Main.stop = false;
/* 45 */       Main.running = true;
/* 46 */       Main.button.setText("Stop");
/*    */       try {
/* 48 */         Main.textArea.setText("");
/* 49 */         String str = Main.textField.getText();
/* 50 */         Long seed = Long.valueOf(Long.parseLong(str));
/* 51 */         (new QuadHutFinder(seed)).start();
/* 52 */       } catch (Exception e) {
/* 53 */         Main.button.setText("Search");
/* 54 */         Main.running = false;
/* 55 */         Main.textArea.setText("That is not a seed you mongrel.");
/*    */       } 
/*    */     } else {
/* 58 */       Main.stop = true;
/*    */     } 
/*    */   }
/*     */   
/*     */   public static boolean allSwamp(int[] x, int[] z, biomeGenerator generate) {
/*  37 */     for (int i = 0; i < 4; i++) {
/*  38 */       if (generate.getBiomeAt(x[i] * 16 + 8, z[i] * 16 + 8) != 6) {
/*  39 */         return false;
/*     */       }
/*     */     } 
/*  42 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean allSwamp2(biomeGenerator generate) {
/*  46 */     int error = 0;
/*  47 */     for (int i = 0; i < 2; i++) {
/*  48 */       if (generate.getBiomeAt(xpos[i] * 16 + 8, zpos[i] * 16 + 8) != 6) {
/*  49 */         error++;
/*     */       }
/*     */     } 
/*     */     
/*  53 */     return (error < 1);
/*     */   }
/*     */   
/*     */   public static boolean allSwamp3(biomeGenerator generate) {
/*  57 */     for (int i = 0; i < 2; i++) {
/*  58 */       if (generate.getBiomeAt(xpos[i] * 16 + 8, zpos[i] * 16 + 8) != 6) {
/*  59 */         return false;
/*     */       }
/*     */     } 
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean swamp(int x, int z, biomeGenerator generate) {
/*  66 */     if (generate.getBiomeAt(x * 16 + 8, z * 16 + 8) == 6) {
/*  67 */       return true;
/*     */     }
/*     */     
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean checkForStructureBR(int x, int z, long seed) {
/*  74 */     xzPair coords = hut.structurePosInRegion(x, z, seed);
/*  75 */     int xrand = coords.getX();
/*  76 */     int zrand = coords.getZ();
/*  77 */     if (xrand >= 13 && zrand >= 13) {
/*  78 */       xpos[3] = x * 32 + xrand;
/*  79 */       zpos[3] = z * 32 + zrand;
/*  80 */       return true;
/*     */     } 
/*  82 */     xpos[3] = Integer.MAX_VALUE;
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean checkForStructureBL(int x, int z, long seed) {
/*  87 */     xzPair coords = hut.structurePosInRegion(x, z, seed);
/*  88 */     int xrand = coords.getX();
/*  89 */     int zrand = coords.getZ();
/*  90 */     if (xrand <= 12 && zrand >= 13) {
/*  91 */       xpos[0] = x * 32 + xrand;
/*  92 */       zpos[0] = z * 32 + zrand;
/*  93 */       return true;
/*     */     } 
/*  95 */     xpos[0] = Integer.MAX_VALUE;
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean checkForStructureTR(int x, int z, long seed) {
/* 100 */     xzPair coords = hut.structurePosInRegion(x, z, seed);
/* 101 */     int xrand = coords.getX();
/* 102 */     int zrand = coords.getZ();
/* 103 */     if (xrand >= 13 && zrand <= 12) {
/* 104 */       xpos[2] = x * 32 + xrand;
/* 105 */       zpos[2] = z * 32 + zrand;
/* 106 */       return true;
/*     */     } 
/* 108 */     xpos[2] = Integer.MAX_VALUE;
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean checkForStructureTL(int x, int z, long seed) {
/* 113 */     xzPair coords = hut.structurePosInRegion(x, z, seed);
/* 114 */     int xrand = coords.getX();
/* 115 */     int zrand = coords.getZ();
/* 116 */     if (xrand <= 12 && zrand <= 12) {
/* 117 */       xpos[1] = x * 32 + xrand;
/* 118 */       zpos[1] = z * 32 + zrand;
/* 119 */       return true;
/*     */     } 
/* 121 */     xpos[1] = Integer.MAX_VALUE;
/* 122 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean checkForStructure(int x, int z, long seed, int type) {
/* 126 */     xzPair coords = hut.structurePosInRegion(x, z, seed);
/* 127 */     int xrand = coords.getX();
/* 128 */     int zrand = coords.getZ();
/* 129 */     switch (type) {
/*     */       case 0:
/* 131 */         if (xrand < 13) return false; 
/*     */         break;
/*     */       case 1:
/* 134 */         if (xrand > 12) return false; 
/*     */         break;
/*     */       case 2:
/* 137 */         if (zrand < 13) return false; 
/*     */         break;
/*     */       case 3:
/* 140 */         if (zrand > 12) return false; 
/*     */         break;
/*     */     } 
/* 143 */     xpos[1] = x * 32 + xrand;
/* 144 */     zpos[1] = z * 32 + zrand;
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean checkBits(long seed) {
/* 164 */     biomeGenerator generate = new biomeGenerator(seed, 2);
/* 165 */     int huts = 0;
/*     */     
/* 167 */     if (allSwamp2(generate))
/* 168 */       for (int i = 0; i < 4; i++) {
/* 169 */         if (xpos[i] != Integer.MAX_VALUE) {
/*     */ 
/*     */           
/* 172 */           boolean b = true;
/* 173 */           int x1 = xpos[i] * 16;
/* 174 */           int z1 = zpos[i] * 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 187 */           for (int j = 0; j < 4; j++) {
/* 188 */             if (xpos[j] != Integer.MAX_VALUE)
/*     */             {
/*     */               
/* 191 */               if (i != j) {
/*     */ 
/*     */                 
/* 194 */                 int x2 = xpos[j] * 16;
/* 195 */                 int z2 = zpos[j] * 16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 202 */                 double dist = dist((x1 - x2), (z1 - z2));
/*     */                 
/* 204 */                 if (dist > 240.0D) {
/* 205 */                   b = false;
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           } 
/* 211 */           if (b) {
/* 212 */             huts++;
/*     */           }
/*     */         } 
/*     */       }  
/* 216 */     if (huts >= 3) {
/* 217 */       if (huts == 4) {
/* 218 */         System.out.println("---- QUAD ----");
/*     */       }
/*     */ 
/*     */       
/* 222 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 233 */   public static double dist(double x, double z) { return Math.sqrt(x * x + z * z); }
/*     */ 
/*     */   
/*     */   public static boolean checkBits2(long seed, ArrayList<xzPair> list) {
/* 237 */     biomeGenerator generate = new biomeGenerator(seed, 2);
/* 238 */     int huts = 0;
/* 239 */     for (xzPair p : list) {
/* 240 */       int x = p.getX();
/* 241 */       int z = p.getZ();
/* 242 */       if (swamp(x, z, generate)) {
/* 243 */         return false;
/*     */       }
/*     */     } 
/* 246 */     for (xzPair p : list) {
/* 247 */       for (xzPair p2 : list) {
/* 248 */         if (p.equals(p2)) {
/*     */           break;
/*     */         }
/* 251 */         if (p2.dist(p) >= 240.0D) {
/* 252 */           System.out.println("fails here");
/* 253 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean checkBits3(long seed) {
/* 262 */     biomeGenerator generate = new biomeGenerator(seed, 2);
/* 263 */     if (allSwamp3(generate)) {
/* 264 */       int x1 = xpos[0] * 16;
/* 265 */       int z1 = zpos[0] * 16;
/* 266 */       int x2 = xpos[1] * 16;
/* 267 */       int z2 = zpos[1] * 16;
/*     */       
/* 269 */       double dist = dist((x1 - x2), (z1 - z2));
/*     */       
/* 271 */       if (dist < 240.0D) {
/* 272 */         return true;
/*     */       }
/*     */     } 
/* 275 */     return false;
/*     */   }
/*     */   
/*     */   private static void rotCheck(int x, int z, long currentSeed) {
/* 279 */     int i = 1;
/* 280 */     int j = 1;
/*     */     
/* 282 */     int di = -1;
/* 283 */     int dj = 0;
/*     */     
/* 285 */     int segmentPassed = 0;
/*     */     
/* 287 */     for (int dir = 0; dir < 8; dir++) {
/* 288 */       xzPair coords = hut.structurePosInRegion((x + i), (z + j), currentSeed);
/* 289 */       xpos[1] = (x + i) * 32 + coords.getX();
/* 290 */       zpos[1] = (z + j) * 32 + coords.getZ();
/*     */       
/* 292 */       if (checkBits3(currentSeed)) {
/* 293 */         double dist = dist((xpos[0] * 16), (zpos[0] * 16));
/* 294 */         String s = String.valueOf(xpos[0] * 16) + "," + (zpos[0] * 16) + " OverworldDist: " + (int)dist + " NetherDist: " + (int)(dist / 8.0D) + "\n";
/* 295 */         Main.textArea.append(s);
/*     */       } 
/*     */ 
/*     */       
/* 299 */       i += di;
/* 300 */       j += dj;
/* 301 */       segmentPassed++;
/*     */       
/* 303 */       if (segmentPassed == 2) {
/* 304 */         segmentPassed = 0;
/*     */         
/* 306 */         int buffer = di;
/* 307 */         di = -dj;
/* 308 */         dj = buffer;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void search() {
/* 316 */     radius = 50000;
/*     */     
/* 318 */     System.out.println(currentSeed);
/*     */     
/* 320 */     hut = new structureHut();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 325 */     int x = 0;
/* 326 */     int z = 0;
/* 327 */     int di = 1;
/* 328 */     int dj = 0;
/* 329 */     int segmentLength = 1;
/* 330 */     int segmentPassed = 0;
/* 331 */     while (segmentLength < radius && !Main.stop) {
/*     */       
/* 333 */       long xPart = hut.xPart(x);
/* 334 */       long zPart = hut.zPart(z);
/* 335 */       ArrayList<xzPair> list = new ArrayList<xzPair>();
/* 336 */       xzPair coords = hut.structurePosInRegionFast2(xPart, zPart, currentSeed, 12, 13);
/*     */ 
/*     */       
/* 339 */       if (coords != null) {
/* 340 */         int xr = coords.getX();
/* 341 */         int zr = coords.getZ();
/* 342 */         xpos[0] = x * 32 + xr;
/* 343 */         zpos[0] = z * 32 + zr;
/*     */         
/* 345 */         rotCheck(x, z, currentSeed);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 481 */       x += di;
/* 482 */       z += dj;
/*     */       
/* 484 */       segmentPassed++;
/*     */       
/* 486 */       if (segmentPassed == segmentLength) {
/* 487 */         segmentPassed = 0;
/*     */         
/* 489 */         int buffer = di;
/* 490 */         di = -dj;
/* 491 */         dj = buffer;
/*     */         
/* 493 */         if (dj == 0) {
/* 494 */           segmentLength++;
/*     */         }
/*     */       } 
/*     */     } 
/* 498 */     Main.running = false;
/* 499 */     Main.stop = false;
/* 500 */     Main.button.setText("Search");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 505 */   public void run() { search(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/QuadHutFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */