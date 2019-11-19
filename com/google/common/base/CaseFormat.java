/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible
/*     */ public static final abstract enum CaseFormat
/*     */ {
/*     */   LOWER_HYPHEN, LOWER_UNDERSCORE, LOWER_CAMEL, UPPER_CAMEL, UPPER_UNDERSCORE;
/*     */   private final CharMatcher wordBoundary;
/*     */   private final String wordSeparator;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new com/google/common/base/CaseFormat$1
/*     */     //   3: dup
/*     */     //   4: ldc 'LOWER_HYPHEN'
/*     */     //   6: iconst_0
/*     */     //   7: bipush #45
/*     */     //   9: invokestatic is : (C)Lcom/google/common/base/CharMatcher;
/*     */     //   12: ldc '-'
/*     */     //   14: invokespecial <init> : (Ljava/lang/String;ILcom/google/common/base/CharMatcher;Ljava/lang/String;)V
/*     */     //   17: putstatic com/google/common/base/CaseFormat.LOWER_HYPHEN : Lcom/google/common/base/CaseFormat;
/*     */     //   20: new com/google/common/base/CaseFormat$2
/*     */     //   23: dup
/*     */     //   24: ldc 'LOWER_UNDERSCORE'
/*     */     //   26: iconst_1
/*     */     //   27: bipush #95
/*     */     //   29: invokestatic is : (C)Lcom/google/common/base/CharMatcher;
/*     */     //   32: ldc '_'
/*     */     //   34: invokespecial <init> : (Ljava/lang/String;ILcom/google/common/base/CharMatcher;Ljava/lang/String;)V
/*     */     //   37: putstatic com/google/common/base/CaseFormat.LOWER_UNDERSCORE : Lcom/google/common/base/CaseFormat;
/*     */     //   40: new com/google/common/base/CaseFormat$3
/*     */     //   43: dup
/*     */     //   44: ldc 'LOWER_CAMEL'
/*     */     //   46: iconst_2
/*     */     //   47: bipush #65
/*     */     //   49: bipush #90
/*     */     //   51: invokestatic inRange : (CC)Lcom/google/common/base/CharMatcher;
/*     */     //   54: ldc ''
/*     */     //   56: invokespecial <init> : (Ljava/lang/String;ILcom/google/common/base/CharMatcher;Ljava/lang/String;)V
/*     */     //   59: putstatic com/google/common/base/CaseFormat.LOWER_CAMEL : Lcom/google/common/base/CaseFormat;
/*     */     //   62: new com/google/common/base/CaseFormat$4
/*     */     //   65: dup
/*     */     //   66: ldc 'UPPER_CAMEL'
/*     */     //   68: iconst_3
/*     */     //   69: bipush #65
/*     */     //   71: bipush #90
/*     */     //   73: invokestatic inRange : (CC)Lcom/google/common/base/CharMatcher;
/*     */     //   76: ldc ''
/*     */     //   78: invokespecial <init> : (Ljava/lang/String;ILcom/google/common/base/CharMatcher;Ljava/lang/String;)V
/*     */     //   81: putstatic com/google/common/base/CaseFormat.UPPER_CAMEL : Lcom/google/common/base/CaseFormat;
/*     */     //   84: new com/google/common/base/CaseFormat$5
/*     */     //   87: dup
/*     */     //   88: ldc 'UPPER_UNDERSCORE'
/*     */     //   90: iconst_4
/*     */     //   91: bipush #95
/*     */     //   93: invokestatic is : (C)Lcom/google/common/base/CharMatcher;
/*     */     //   96: ldc '_'
/*     */     //   98: invokespecial <init> : (Ljava/lang/String;ILcom/google/common/base/CharMatcher;Ljava/lang/String;)V
/*     */     //   101: putstatic com/google/common/base/CaseFormat.UPPER_UNDERSCORE : Lcom/google/common/base/CaseFormat;
/*     */     //   104: iconst_5
/*     */     //   105: anewarray com/google/common/base/CaseFormat
/*     */     //   108: dup
/*     */     //   109: iconst_0
/*     */     //   110: getstatic com/google/common/base/CaseFormat.LOWER_HYPHEN : Lcom/google/common/base/CaseFormat;
/*     */     //   113: aastore
/*     */     //   114: dup
/*     */     //   115: iconst_1
/*     */     //   116: getstatic com/google/common/base/CaseFormat.LOWER_UNDERSCORE : Lcom/google/common/base/CaseFormat;
/*     */     //   119: aastore
/*     */     //   120: dup
/*     */     //   121: iconst_2
/*     */     //   122: getstatic com/google/common/base/CaseFormat.LOWER_CAMEL : Lcom/google/common/base/CaseFormat;
/*     */     //   125: aastore
/*     */     //   126: dup
/*     */     //   127: iconst_3
/*     */     //   128: getstatic com/google/common/base/CaseFormat.UPPER_CAMEL : Lcom/google/common/base/CaseFormat;
/*     */     //   131: aastore
/*     */     //   132: dup
/*     */     //   133: iconst_4
/*     */     //   134: getstatic com/google/common/base/CaseFormat.UPPER_UNDERSCORE : Lcom/google/common/base/CaseFormat;
/*     */     //   137: aastore
/*     */     //   138: putstatic com/google/common/base/CaseFormat.$VALUES : [Lcom/google/common/base/CaseFormat;
/*     */     //   141: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #40	-> 0
/*     */     //   #58	-> 20
/*     */     //   #76	-> 40
/*     */     //   #85	-> 62
/*     */     //   #94	-> 84
/*     */     //   #35	-> 104
/*     */   }
/*     */   
/*     */   CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
/* 113 */     this.wordBoundary = wordBoundary;
/* 114 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String to(CaseFormat format, String str) {
/* 123 */     Preconditions.checkNotNull(format);
/* 124 */     Preconditions.checkNotNull(str);
/* 125 */     return (format == this) ? str : convert(format, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String convert(CaseFormat format, String s) {
/* 133 */     StringBuilder out = null;
/* 134 */     int i = 0;
/* 135 */     int j = -1;
/* 136 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 137 */       if (i == 0) {
/*     */         
/* 139 */         out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
/* 140 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 142 */         out.append(format.normalizeWord(s.substring(i, j)));
/*     */       } 
/* 144 */       out.append(format.wordSeparator);
/* 145 */       i = j + this.wordSeparator.length();
/*     */     } 
/* 147 */     return (i == 0) ? format.normalizeFirstWord(s) : out.append(format.normalizeWord(s.substring(i))).toString();
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
/*     */   @Beta
/* 159 */   public Converter<String, String> converterTo(CaseFormat targetFormat) { return new StringConverter(this, targetFormat); }
/*     */   
/*     */   private static final class StringConverter
/*     */     extends Converter<String, String>
/*     */     implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 169 */       this.sourceFormat = (CaseFormat)Preconditions.checkNotNull(sourceFormat);
/* 170 */       this.targetFormat = (CaseFormat)Preconditions.checkNotNull(targetFormat);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 175 */     protected String doForward(String s) { return (s == null) ? null : this.sourceFormat.to(this.targetFormat, s); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     protected String doBackward(String s) { return (s == null) ? null : this.targetFormat.to(this.sourceFormat, s); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 184 */       if (object instanceof StringConverter) {
/* 185 */         StringConverter that = (StringConverter)object;
/* 186 */         return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
/*     */       } 
/*     */       
/* 189 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 193 */     public int hashCode() { return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 197 */     public String toString() { return this.sourceFormat + ".converterTo(" + this.targetFormat + ")"; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   private String normalizeFirstWord(String word) { return (this == LOWER_CAMEL) ? Ascii.toLowerCase(word) : normalizeWord(word); }
/*     */ 
/*     */ 
/*     */   
/* 210 */   private static String firstCharOnlyToUpper(String word) { return word.isEmpty() ? word : word.length().toString(); }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/CaseFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */