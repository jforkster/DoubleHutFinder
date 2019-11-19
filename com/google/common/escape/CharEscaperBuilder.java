/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class CharEscaperBuilder
/*     */ {
/*     */   private static class CharArrayDecorator
/*     */     extends CharEscaper
/*     */   {
/*     */     private final char[][] replacements;
/*     */     private final int replaceLength;
/*     */     
/*     */     CharArrayDecorator(char[][] replacements) {
/*  48 */       this.replacements = replacements;
/*  49 */       this.replaceLength = replacements.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String escape(String s) {
/*  57 */       int slen = s.length();
/*  58 */       for (int index = 0; index < slen; index++) {
/*  59 */         char c = s.charAt(index);
/*  60 */         if (c < this.replacements.length && this.replacements[c] != null) {
/*  61 */           return escapeSlow(s, index);
/*     */         }
/*     */       } 
/*  64 */       return s;
/*     */     }
/*     */ 
/*     */     
/*  68 */     protected char[] escape(char c) { return (c < this.replaceLength) ? this.replacements[c] : null; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private int max = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private final Map<Character, String> map = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharEscaperBuilder addEscape(char c, String r) {
/*  89 */     this.map.put(Character.valueOf(c), Preconditions.checkNotNull(r));
/*  90 */     if (c > this.max) {
/*  91 */       this.max = c;
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharEscaperBuilder addEscapes(char[] cs, String r) {
/* 100 */     Preconditions.checkNotNull(r);
/* 101 */     for (char c : cs) {
/* 102 */       addEscape(c, r);
/*     */     }
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[][] toArray() {
/* 115 */     char[][] result = new char[this.max + 1][];
/* 116 */     for (Map.Entry<Character, String> entry : this.map.entrySet()) {
/* 117 */       result[((Character)entry.getKey()).charValue()] = ((String)entry.getValue()).toCharArray();
/*     */     }
/* 119 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public Escaper toEscaper() { return new CharArrayDecorator(toArray()); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/escape/CharEscaperBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */