/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class Escapers
/*     */ {
/*  46 */   public static Escaper nullEscaper() { return NULL_ESCAPER; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static final Escaper NULL_ESCAPER = new CharEscaper()
/*     */     {
/*  53 */       public String escape(String string) { return (String)Preconditions.checkNotNull(string); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  58 */       protected char[] escape(char c) { return null; }
/*     */     };
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
/*  78 */   public static Builder builder() { return new Builder(null); }
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
/*     */   @Beta
/*     */   public static final class Builder
/*     */   {
/*  95 */     private final Map<Character, String> replacementMap = new HashMap();
/*     */     
/*  97 */     private char safeMin = Character.MIN_VALUE;
/*  98 */     private char safeMax = Character.MAX_VALUE;
/*  99 */     private String unsafeReplacement = null;
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
/*     */     public Builder setSafeRange(char safeMin, char safeMax) {
/* 115 */       this.safeMin = safeMin;
/* 116 */       this.safeMax = safeMax;
/* 117 */       return this;
/*     */     }
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
/*     */     public Builder setUnsafeReplacement(@Nullable String unsafeReplacement) {
/* 130 */       this.unsafeReplacement = unsafeReplacement;
/* 131 */       return this;
/*     */     }
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
/*     */     public Builder addEscape(char c, String replacement) {
/* 146 */       Preconditions.checkNotNull(replacement);
/*     */       
/* 148 */       this.replacementMap.put(Character.valueOf(c), replacement);
/* 149 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Escaper build() {
/* 156 */       return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax)
/*     */         {
/*     */           private final char[] replacementChars;
/*     */           
/* 160 */           protected char[] escapeUnsafe(char c) { return this.replacementChars; }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static UnicodeEscaper asUnicodeEscaper(Escaper escaper) {
/* 183 */     Preconditions.checkNotNull(escaper);
/* 184 */     if (escaper instanceof UnicodeEscaper)
/* 185 */       return (UnicodeEscaper)escaper; 
/* 186 */     if (escaper instanceof CharEscaper) {
/* 187 */       return wrap((CharEscaper)escaper);
/*     */     }
/*     */ 
/*     */     
/* 191 */     throw new IllegalArgumentException("Cannot create a UnicodeEscaper from: " + escaper.getClass().getName());
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
/* 206 */   public static String computeReplacement(CharEscaper escaper, char c) { return stringOrNull(escaper.escape(c)); }
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
/* 220 */   public static String computeReplacement(UnicodeEscaper escaper, int cp) { return stringOrNull(escaper.escape(cp)); }
/*     */ 
/*     */ 
/*     */   
/* 224 */   private static String stringOrNull(char[] in) { return (in == null) ? null : new String(in); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static UnicodeEscaper wrap(final CharEscaper escaper) {
/* 229 */     return new UnicodeEscaper()
/*     */       {
/*     */         protected char[] escape(int cp) {
/* 232 */           if (cp < 65536) {
/* 233 */             return escaper.escape((char)cp);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 239 */           char[] surrogateChars = new char[2];
/* 240 */           Character.toChars(cp, surrogateChars, 0);
/* 241 */           char[] hiChars = escaper.escape(surrogateChars[0]);
/* 242 */           char[] loChars = escaper.escape(surrogateChars[1]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 248 */           if (hiChars == null && loChars == null)
/*     */           {
/* 250 */             return null;
/*     */           }
/*     */           
/* 253 */           int hiCount = (hiChars != null) ? hiChars.length : 1;
/* 254 */           int loCount = (loChars != null) ? loChars.length : 1;
/* 255 */           char[] output = new char[hiCount + loCount];
/* 256 */           if (hiChars != null) {
/*     */             
/* 258 */             for (int n = 0; n < hiChars.length; n++) {
/* 259 */               output[n] = hiChars[n];
/*     */             }
/*     */           } else {
/* 262 */             output[0] = surrogateChars[0];
/*     */           } 
/* 264 */           if (loChars != null) {
/* 265 */             for (int n = 0; n < loChars.length; n++) {
/* 266 */               output[hiCount + n] = loChars[n];
/*     */             }
/*     */           } else {
/* 269 */             output[hiCount] = surrogateChars[1];
/*     */           } 
/* 271 */           return output;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/escape/Escapers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */