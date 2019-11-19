/*    */ package com.google.thirdparty.publicsuffix;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ static enum PublicSuffixType
/*    */ {
/* 28 */   PRIVATE(':', ','),
/*    */   
/* 30 */   ICANN('!', '?');
/*    */ 
/*    */   
/*    */   private final char innerNodeCode;
/*    */   
/*    */   private final char leafNodeCode;
/*    */ 
/*    */   
/*    */   PublicSuffixType(char innerNodeCode, char leafNodeCode) {
/* 39 */     this.innerNodeCode = innerNodeCode;
/* 40 */     this.leafNodeCode = leafNodeCode;
/*    */   }
/*    */ 
/*    */   
/* 44 */   char getLeafNodeCode() { return this.leafNodeCode; }
/*    */ 
/*    */ 
/*    */   
/* 48 */   char getInnerNodeCode() { return this.innerNodeCode; }
/*    */ 
/*    */ 
/*    */   
/*    */   static PublicSuffixType fromCode(char code) {
/* 53 */     for (PublicSuffixType value : values()) {
/* 54 */       if (value.getInnerNodeCode() == code || value.getLeafNodeCode() == code) {
/* 55 */         return value;
/*    */       }
/*    */     } 
/* 58 */     throw new IllegalArgumentException("No enum corresponding to given code: " + code);
/*    */   }
/*    */ 
/*    */   
/* 62 */   static PublicSuffixType fromIsPrivate(boolean isPrivate) { return isPrivate ? PRIVATE : ICANN; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/thirdparty/publicsuffix/PublicSuffixType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */