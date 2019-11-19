/*    */ package com.google.common.escape;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class ArrayBasedEscaperMap
/*    */ {
/*    */   private final char[][] replacementArray;
/*    */   
/* 56 */   public static ArrayBasedEscaperMap create(Map<Character, String> replacements) { return new ArrayBasedEscaperMap(createReplacementArray(replacements)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   private ArrayBasedEscaperMap(char[][] replacementArray) { this.replacementArray = replacementArray; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   char[][] getReplacementArray() { return this.replacementArray; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/*    */   static char[][] createReplacementArray(Map<Character, String> map) {
/* 77 */     Preconditions.checkNotNull(map);
/* 78 */     if (map.isEmpty()) {
/* 79 */       return EMPTY_REPLACEMENT_ARRAY;
/*    */     }
/* 81 */     char max = ((Character)Collections.max(map.keySet())).charValue();
/* 82 */     char[][] replacements = new char[max + '\001'][];
/* 83 */     for (Iterator i$ = map.keySet().iterator(); i$.hasNext(); ) { char c = ((Character)i$.next()).charValue();
/* 84 */       replacements[c] = ((String)map.get(Character.valueOf(c))).toCharArray(); }
/*    */     
/* 86 */     return replacements;
/*    */   }
/*    */ 
/*    */   
/* 90 */   private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/escape/ArrayBasedEscaperMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */