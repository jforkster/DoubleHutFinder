/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class CharMatcher
/*      */   extends Object
/*      */   implements Predicate<Character>
/*      */ {
/*   67 */   public static final CharMatcher BREAKING_WHITESPACE = new CharMatcher()
/*      */     {
/*      */       public boolean matches(char c) {
/*   70 */         switch (c) {
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\013':
/*      */           case '\f':
/*      */           case '\r':
/*      */           case ' ':
/*      */           case '':
/*      */           case ' ':
/*      */           case ' ':
/*      */           case ' ':
/*      */           case ' ':
/*      */           case '　':
/*   83 */             return true;
/*      */           case ' ':
/*   85 */             return false;
/*      */         } 
/*   87 */         return (c >= ' ' && c <= ' ');
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   93 */       public String toString() { return "CharMatcher.BREAKING_WHITESPACE"; }
/*      */     };
/*      */   private static final String ZEROES = "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０";
/*      */   private static final String NINES;
/*      */   public static final CharMatcher DIGIT;
/*      */   public static final CharMatcher JAVA_DIGIT;
/*      */   public static final CharMatcher JAVA_LETTER;
/*  100 */   public static final CharMatcher ASCII = inRange(false, '', "CharMatcher.ASCII"); public static final CharMatcher JAVA_LETTER_OR_DIGIT; public static final CharMatcher JAVA_UPPER_CASE; public static final CharMatcher JAVA_LOWER_CASE; public static final CharMatcher JAVA_ISO_CONTROL; public static final CharMatcher INVISIBLE; public static final CharMatcher SINGLE_WIDTH; public static final CharMatcher ANY; public static final CharMatcher NONE; final String description; private static final int DISTINCT_CHARS = 65536;
/*      */   static final String WHITESPACE_TABLE = " 　\r   　 \013　   　 \t     \f 　 　　 \n 　";
/*      */   static final int WHITESPACE_MULTIPLIER = 1682554634;
/*      */   static final int WHITESPACE_SHIFT;
/*      */   public static final CharMatcher WHITESPACE;
/*      */   
/*      */   private static class RangesMatcher extends CharMatcher { RangesMatcher(String description, char[] rangeStarts, char[] rangeEnds) {
/*  107 */       super(description);
/*  108 */       this.rangeStarts = rangeStarts;
/*  109 */       this.rangeEnds = rangeEnds;
/*  110 */       Preconditions.checkArgument((rangeStarts.length == rangeEnds.length));
/*  111 */       for (int i = 0; i < rangeStarts.length; i++) {
/*  112 */         Preconditions.checkArgument((rangeStarts[i] <= rangeEnds[i]));
/*  113 */         if (i + 1 < rangeStarts.length)
/*  114 */           Preconditions.checkArgument((rangeEnds[i] < rangeStarts[i + 1])); 
/*      */       } 
/*      */     }
/*      */     private final char[] rangeStarts;
/*      */     private final char[] rangeEnds;
/*      */     
/*      */     public boolean matches(char c) {
/*  121 */       int index = Arrays.binarySearch(this.rangeStarts, c);
/*  122 */       if (index >= 0) {
/*  123 */         return true;
/*      */       }
/*  125 */       index = (index ^ 0xFFFFFFFF) - 1;
/*  126 */       return (index >= 0 && c <= this.rangeEnds[index]);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static  {
/*  138 */     builder = new StringBuilder("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length());
/*  139 */     for (int i = 0; i < "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".length(); i++) {
/*  140 */       builder.append((char)("0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".charAt(i) + '\t'));
/*      */     }
/*  142 */     NINES = builder.toString();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  150 */     DIGIT = new RangesMatcher("CharMatcher.DIGIT", "0٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray(), NINES.toCharArray());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  158 */     JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
/*      */         public boolean matches(char c) {
/*  160 */           return Character.isDigit(c);
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  169 */     JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
/*      */         public boolean matches(char c) {
/*  171 */           return Character.isLetter(c);
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  179 */     JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT")
/*      */       {
/*      */         public boolean matches(char c) {
/*  182 */           return Character.isLetterOrDigit(c);
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  190 */     JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE")
/*      */       {
/*      */         public boolean matches(char c) {
/*  193 */           return Character.isUpperCase(c);
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  201 */     JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE")
/*      */       {
/*      */         public boolean matches(char c) {
/*  204 */           return Character.isLowerCase(c);
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  212 */     JAVA_ISO_CONTROL = inRange(false, '\037').or(inRange('', '')).withToString("CharMatcher.JAVA_ISO_CONTROL");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  222 */     INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "\000­؀؜۝܏ ᠎   ⁦⁧⁨⁩⁪　?﻿￹￺".toCharArray(), "  ­؄؜۝܏ ᠎‏ ⁤⁦⁧⁨⁩⁯　﻿￹￻".toCharArray());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  247 */     SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "\000־א׳؀ݐ฀Ḁ℀ﭐﹰ｡".toCharArray(), "ӹ־ת״ۿݿ๿₯℺﷿﻿ￜ".toCharArray());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  252 */     ANY = new FastMatcher("CharMatcher.ANY")
/*      */       {
/*      */         public boolean matches(char c) {
/*  255 */           return true;
/*      */         }
/*      */ 
/*      */         
/*  259 */         public int indexIn(CharSequence sequence) { return (sequence.length() == 0) ? -1 : 0; }
/*      */ 
/*      */         
/*      */         public int indexIn(CharSequence sequence, int start) {
/*  263 */           int length = sequence.length();
/*  264 */           Preconditions.checkPositionIndex(start, length);
/*  265 */           return (start == length) ? -1 : start;
/*      */         }
/*      */ 
/*      */         
/*  269 */         public int lastIndexIn(CharSequence sequence) { return sequence.length() - 1; }
/*      */ 
/*      */         
/*      */         public boolean matchesAllOf(CharSequence sequence) {
/*  273 */           Preconditions.checkNotNull(sequence);
/*  274 */           return true;
/*      */         }
/*      */ 
/*      */         
/*  278 */         public boolean matchesNoneOf(CharSequence sequence) { return (sequence.length() == 0); }
/*      */ 
/*      */         
/*      */         public String removeFrom(CharSequence sequence) {
/*  282 */           Preconditions.checkNotNull(sequence);
/*  283 */           return "";
/*      */         }
/*      */         
/*      */         public String replaceFrom(CharSequence sequence, char replacement) {
/*  287 */           char[] array = new char[sequence.length()];
/*  288 */           Arrays.fill(array, replacement);
/*  289 */           return new String(array);
/*      */         }
/*      */         
/*      */         public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  293 */           StringBuilder retval = new StringBuilder(sequence.length() * replacement.length());
/*  294 */           for (int i = 0; i < sequence.length(); i++) {
/*  295 */             retval.append(replacement);
/*      */           }
/*  297 */           return retval.toString();
/*      */         }
/*      */ 
/*      */         
/*  301 */         public String collapseFrom(CharSequence sequence, char replacement) { return (sequence.length() == 0) ? "" : String.valueOf(replacement); }
/*      */ 
/*      */         
/*      */         public String trimFrom(CharSequence sequence) {
/*  305 */           Preconditions.checkNotNull(sequence);
/*  306 */           return "";
/*      */         }
/*      */ 
/*      */         
/*  310 */         public int countIn(CharSequence sequence) { return sequence.length(); }
/*      */ 
/*      */ 
/*      */         
/*  314 */         public CharMatcher and(CharMatcher other) { return (CharMatcher)Preconditions.checkNotNull(other); }
/*      */ 
/*      */         
/*      */         public CharMatcher or(CharMatcher other) {
/*  318 */           Preconditions.checkNotNull(other);
/*  319 */           return this;
/*      */         }
/*      */         
/*      */         public CharMatcher negate() {
/*  323 */           return NONE;
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*  328 */     NONE = new FastMatcher("CharMatcher.NONE")
/*      */       {
/*      */         public boolean matches(char c) {
/*  331 */           return false;
/*      */         }
/*      */         
/*      */         public int indexIn(CharSequence sequence) {
/*  335 */           Preconditions.checkNotNull(sequence);
/*  336 */           return -1;
/*      */         }
/*      */         
/*      */         public int indexIn(CharSequence sequence, int start) {
/*  340 */           int length = sequence.length();
/*  341 */           Preconditions.checkPositionIndex(start, length);
/*  342 */           return -1;
/*      */         }
/*      */         
/*      */         public int lastIndexIn(CharSequence sequence) {
/*  346 */           Preconditions.checkNotNull(sequence);
/*  347 */           return -1;
/*      */         }
/*      */ 
/*      */         
/*  351 */         public boolean matchesAllOf(CharSequence sequence) { return (sequence.length() == 0); }
/*      */ 
/*      */         
/*      */         public boolean matchesNoneOf(CharSequence sequence) {
/*  355 */           Preconditions.checkNotNull(sequence);
/*  356 */           return true;
/*      */         }
/*      */ 
/*      */         
/*  360 */         public String removeFrom(CharSequence sequence) { return sequence.toString(); }
/*      */ 
/*      */ 
/*      */         
/*  364 */         public String replaceFrom(CharSequence sequence, char replacement) { return sequence.toString(); }
/*      */ 
/*      */         
/*      */         public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*  368 */           Preconditions.checkNotNull(replacement);
/*  369 */           return sequence.toString();
/*      */         }
/*      */ 
/*      */         
/*  373 */         public String collapseFrom(CharSequence sequence, char replacement) { return sequence.toString(); }
/*      */ 
/*      */ 
/*      */         
/*  377 */         public String trimFrom(CharSequence sequence) { return sequence.toString(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  382 */         public String trimLeadingFrom(CharSequence sequence) { return sequence.toString(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  387 */         public String trimTrailingFrom(CharSequence sequence) { return sequence.toString(); }
/*      */ 
/*      */         
/*      */         public int countIn(CharSequence sequence) {
/*  391 */           Preconditions.checkNotNull(sequence);
/*  392 */           return 0;
/*      */         }
/*      */         
/*      */         public CharMatcher and(CharMatcher other) {
/*  396 */           Preconditions.checkNotNull(other);
/*  397 */           return this;
/*      */         }
/*      */ 
/*      */         
/*  401 */         public CharMatcher or(CharMatcher other) { return (CharMatcher)Preconditions.checkNotNull(other); }
/*      */ 
/*      */         
/*      */         public CharMatcher negate() {
/*  405 */           return ANY;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1358 */     WHITESPACE_SHIFT = Integer.numberOfLeadingZeros(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length() - 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1371 */     WHITESPACE = new FastMatcher("WHITESPACE")
/*      */       {
/*      */         public boolean matches(char c) {
/* 1374 */           return (" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(1682554634 * c >>> WHITESPACE_SHIFT) == c);
/*      */         }
/*      */ 
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/* 1380 */           for (int i = 0; i < " 　\r   　 \013　   　 \t     \f 　 　　 \n 　".length(); i++)
/* 1381 */             table.set(" 　\r   　 \013　   　 \t     \f 　 　　 \n 　".charAt(i)); 
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static String showCharacter(char c) {
/*      */     String hex = "0123456789ABCDEF";
/*      */     char[] tmp = { '\\', 'u', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE };
/*      */     for (int i = 0; i < 4; i++) {
/*      */       tmp[5 - i] = hex.charAt(c & 0xF);
/*      */       c = (char)(c >> '\004');
/*      */     } 
/*      */     return String.copyValueOf(tmp);
/*      */   }
/*      */   
/*      */   public static CharMatcher is(final char match) {
/*      */     String description = "CharMatcher.is('" + showCharacter(match) + "')";
/*      */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) { return (c == match); }
/*      */         
/*      */         public String replaceFrom(CharSequence sequence, char replacement) { return sequence.toString().replace(match, replacement); }
/*      */         
/*      */         public CharMatcher and(CharMatcher other) { return other.matches(match) ? this : NONE; }
/*      */         
/*      */         public CharMatcher or(CharMatcher other) { return other.matches(match) ? other : super.or(other); }
/*      */         
/*      */         public CharMatcher negate() { return CharMatcher.null.isNot(match); }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) { table.set(match); }
/*      */       };
/*      */   }
/*      */   
/*      */   public static CharMatcher isNot(final char match) {
/*      */     String description = "CharMatcher.isNot('" + showCharacter(match) + "')";
/*      */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) { return (c != match); }
/*      */         
/*      */         public CharMatcher and(CharMatcher other) { return other.matches(match) ? super.and(other) : other; }
/*      */         
/*      */         public CharMatcher or(CharMatcher other) { return other.matches(match) ? ANY : this; }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*      */           table.set(0, match);
/*      */           table.set(match + '\001', 65536);
/*      */         }
/*      */         
/*      */         public CharMatcher negate() { return CharMatcher.null.is(match); }
/*      */       };
/*      */   }
/*      */   
/*      */   public static CharMatcher anyOf(CharSequence sequence) {
/*      */     switch (sequence.length()) {
/*      */       case 0:
/*      */         return NONE;
/*      */       case 1:
/*      */         return is(sequence.charAt(0));
/*      */       case 2:
/*      */         return isEither(sequence.charAt(0), sequence.charAt(1));
/*      */     } 
/*      */     final char[] chars = sequence.toString().toCharArray();
/*      */     Arrays.sort(chars);
/*      */     StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
/*      */     for (char c : chars)
/*      */       description.append(showCharacter(c)); 
/*      */     description.append("\")");
/*      */     return new CharMatcher(description.toString()) {
/*      */         public boolean matches(char c) { return (Arrays.binarySearch(chars, c) >= 0); }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*      */           for (char c : chars)
/*      */             table.set(c); 
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static CharMatcher isEither(final char match1, final char match2) {
/*      */     String description = "CharMatcher.anyOf(\"" + showCharacter(match1) + showCharacter(match2) + "\")";
/*      */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) { return (c == match1 || c == match2); }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) {
/*      */           table.set(match1);
/*      */           table.set(match2);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public static CharMatcher noneOf(CharSequence sequence) { return anyOf(sequence).negate(); }
/*      */   
/*      */   public static CharMatcher inRange(char startInclusive, char endInclusive) {
/*      */     Preconditions.checkArgument((endInclusive >= startInclusive));
/*      */     String description = "CharMatcher.inRange('" + showCharacter(startInclusive) + "', '" + showCharacter(endInclusive) + "')";
/*      */     return inRange(startInclusive, endInclusive, description);
/*      */   }
/*      */   
/*      */   static CharMatcher inRange(final char startInclusive, final char endInclusive, String description) {
/*      */     return new FastMatcher(description) {
/*      */         public boolean matches(char c) { return (startInclusive <= c && c <= endInclusive); }
/*      */         
/*      */         @GwtIncompatible("java.util.BitSet")
/*      */         void setBits(BitSet table) { table.set(startInclusive, endInclusive + '\001'); }
/*      */       };
/*      */   }
/*      */   
/*      */   public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
/*      */     Preconditions.checkNotNull(predicate);
/*      */     if (predicate instanceof CharMatcher)
/*      */       return (CharMatcher)predicate; 
/*      */     String description = "CharMatcher.forPredicate(" + predicate + ")";
/*      */     return new CharMatcher(description) {
/*      */         public boolean matches(char c) { return predicate.apply(Character.valueOf(c)); }
/*      */         
/*      */         public boolean apply(Character character) { return predicate.apply(Preconditions.checkNotNull(character)); }
/*      */       };
/*      */   }
/*      */   
/*      */   CharMatcher(String description) { this.description = description; }
/*      */   
/*      */   protected CharMatcher() { this.description = super.toString(); }
/*      */   
/*      */   public CharMatcher negate() { return new NegatedMatcher(this); }
/*      */   
/*      */   private static class NegatedMatcher extends CharMatcher {
/*      */     final CharMatcher original;
/*      */     
/*      */     NegatedMatcher(String toString, CharMatcher original) {
/*      */       super(toString);
/*      */       this.original = original;
/*      */     }
/*      */     
/*      */     NegatedMatcher(CharMatcher original) { this(original + ".negate()", original); }
/*      */     
/*      */     public boolean matches(char c) { return !this.original.matches(c); }
/*      */     
/*      */     public boolean matchesAllOf(CharSequence sequence) { return this.original.matchesNoneOf(sequence); }
/*      */     
/*      */     public boolean matchesNoneOf(CharSequence sequence) { return this.original.matchesAllOf(sequence); }
/*      */     
/*      */     public int countIn(CharSequence sequence) { return sequence.length() - this.original.countIn(sequence); }
/*      */     
/*      */     @GwtIncompatible("java.util.BitSet")
/*      */     void setBits(BitSet table) {
/*      */       BitSet tmp = new BitSet();
/*      */       this.original.setBits(tmp);
/*      */       tmp.flip(0, 65536);
/*      */       table.or(tmp);
/*      */     }
/*      */     
/*      */     public CharMatcher negate() { return this.original; }
/*      */     
/*      */     CharMatcher withToString(String description) { return new NegatedMatcher(description, this.original); }
/*      */   }
/*      */   
/*      */   public CharMatcher and(CharMatcher other) { return new And(this, (CharMatcher)Preconditions.checkNotNull(other)); }
/*      */   
/*      */   private static class And extends CharMatcher {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     And(CharMatcher a, CharMatcher b) { this(a, b, "CharMatcher.and(" + a + ", " + b + ")"); }
/*      */     
/*      */     And(CharMatcher a, CharMatcher b, String description) {
/*      */       super(description);
/*      */       this.first = (CharMatcher)Preconditions.checkNotNull(a);
/*      */       this.second = (CharMatcher)Preconditions.checkNotNull(b);
/*      */     }
/*      */     
/*      */     public boolean matches(char c) { return (this.first.matches(c) && this.second.matches(c)); }
/*      */     
/*      */     @GwtIncompatible("java.util.BitSet")
/*      */     void setBits(BitSet table) {
/*      */       BitSet tmp1 = new BitSet();
/*      */       this.first.setBits(tmp1);
/*      */       BitSet tmp2 = new BitSet();
/*      */       this.second.setBits(tmp2);
/*      */       tmp1.and(tmp2);
/*      */       table.or(tmp1);
/*      */     }
/*      */     
/*      */     CharMatcher withToString(String description) { return new And(this.first, this.second, description); }
/*      */   }
/*      */   
/*      */   public CharMatcher or(CharMatcher other) { return new Or(this, (CharMatcher)Preconditions.checkNotNull(other)); }
/*      */   
/*      */   private static class Or extends CharMatcher {
/*      */     final CharMatcher first;
/*      */     final CharMatcher second;
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b, String description) {
/*      */       super(description);
/*      */       this.first = (CharMatcher)Preconditions.checkNotNull(a);
/*      */       this.second = (CharMatcher)Preconditions.checkNotNull(b);
/*      */     }
/*      */     
/*      */     Or(CharMatcher a, CharMatcher b) { this(a, b, "CharMatcher.or(" + a + ", " + b + ")"); }
/*      */     
/*      */     @GwtIncompatible("java.util.BitSet")
/*      */     void setBits(BitSet table) {
/*      */       this.first.setBits(table);
/*      */       this.second.setBits(table);
/*      */     }
/*      */     
/*      */     public boolean matches(char c) { return (this.first.matches(c) || this.second.matches(c)); }
/*      */     
/*      */     CharMatcher withToString(String description) { return new Or(this.first, this.second, description); }
/*      */   }
/*      */   
/*      */   public CharMatcher precomputed() { return Platform.precomputeCharMatcher(this); }
/*      */   
/*      */   CharMatcher withToString(String description) { throw new UnsupportedOperationException(); }
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   CharMatcher precomputedInternal() {
/*      */     BitSet table = new BitSet();
/*      */     setBits(table);
/*      */     int totalCharacters = table.cardinality();
/*      */     if (totalCharacters * 2 <= 65536)
/*      */       return precomputedPositive(totalCharacters, table, this.description); 
/*      */     table.flip(0, 65536);
/*      */     int negatedCharacters = 65536 - totalCharacters;
/*      */     String suffix = ".negate()";
/*      */     String negatedDescription = this.description.endsWith(suffix) ? this.description.substring(0, this.description.length() - suffix.length()) : (this.description + suffix);
/*      */     return new NegatedFastMatcher(toString(), precomputedPositive(negatedCharacters, table, negatedDescription));
/*      */   }
/*      */   
/*      */   static abstract class FastMatcher extends CharMatcher {
/*      */     FastMatcher() {}
/*      */     
/*      */     FastMatcher(String description) { super(description); }
/*      */     
/*      */     public final CharMatcher precomputed() { return this; }
/*      */     
/*      */     public CharMatcher negate() { return new CharMatcher.NegatedFastMatcher(this); }
/*      */   }
/*      */   
/*      */   static final class NegatedFastMatcher extends NegatedMatcher {
/*      */     NegatedFastMatcher(CharMatcher original) { super(original); }
/*      */     
/*      */     NegatedFastMatcher(String toString, CharMatcher original) { super(toString, original); }
/*      */     
/*      */     public final CharMatcher precomputed() { return this; }
/*      */     
/*      */     CharMatcher withToString(String description) { return new NegatedFastMatcher(description, this.original); }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   private static CharMatcher precomputedPositive(int totalCharacters, BitSet table, String description) {
/*      */     char c2;
/*      */     char c1;
/*      */     switch (totalCharacters) {
/*      */       case 0:
/*      */         return NONE;
/*      */       case 1:
/*      */         return is((char)table.nextSetBit(0));
/*      */       case 2:
/*      */         c1 = (char)table.nextSetBit(0);
/*      */         c2 = (char)table.nextSetBit(c1 + '\001');
/*      */         return isEither(c1, c2);
/*      */     } 
/*      */     return isSmall(totalCharacters, table.length()) ? SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description, null);
/*      */   }
/*      */   
/*      */   @GwtIncompatible("SmallCharMatcher")
/*      */   private static boolean isSmall(int totalCharacters, int tableLength) { return (totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16); }
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   private static class BitSetMatcher extends FastMatcher {
/*      */     private final BitSet table;
/*      */     
/*      */     private BitSetMatcher(BitSet table, String description) {
/*      */       super(description);
/*      */       if (table.length() + 64 < table.size())
/*      */         table = (BitSet)table.clone(); 
/*      */       this.table = table;
/*      */     }
/*      */     
/*      */     public boolean matches(char c) { return this.table.get(c); }
/*      */     
/*      */     void setBits(BitSet bitSet) { bitSet.or(this.table); }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("java.util.BitSet")
/*      */   void setBits(BitSet table) {
/*      */     for (int c = 65535; c >= 0; c--) {
/*      */       if (matches((char)c))
/*      */         table.set(c); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean matchesAnyOf(CharSequence sequence) { return !matchesNoneOf(sequence); }
/*      */   
/*      */   public boolean matchesAllOf(CharSequence sequence) {
/*      */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*      */       if (!matches(sequence.charAt(i)))
/*      */         return false; 
/*      */     } 
/*      */     return true;
/*      */   }
/*      */   
/*      */   public boolean matchesNoneOf(CharSequence sequence) { return (indexIn(sequence) == -1); }
/*      */   
/*      */   public int indexIn(CharSequence sequence) {
/*      */     int length = sequence.length();
/*      */     for (int i = 0; i < length; i++) {
/*      */       if (matches(sequence.charAt(i)))
/*      */         return i; 
/*      */     } 
/*      */     return -1;
/*      */   }
/*      */   
/*      */   public int indexIn(CharSequence sequence, int start) {
/*      */     int length = sequence.length();
/*      */     Preconditions.checkPositionIndex(start, length);
/*      */     for (int i = start; i < length; i++) {
/*      */       if (matches(sequence.charAt(i)))
/*      */         return i; 
/*      */     } 
/*      */     return -1;
/*      */   }
/*      */   
/*      */   public int lastIndexIn(CharSequence sequence) {
/*      */     for (int i = sequence.length() - 1; i >= 0; i--) {
/*      */       if (matches(sequence.charAt(i)))
/*      */         return i; 
/*      */     } 
/*      */     return -1;
/*      */   }
/*      */   
/*      */   public int countIn(CharSequence sequence) {
/*      */     int count = 0;
/*      */     for (int i = 0; i < sequence.length(); i++) {
/*      */       if (matches(sequence.charAt(i)))
/*      */         count++; 
/*      */     } 
/*      */     return count;
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String removeFrom(CharSequence sequence) {
/*      */     String string = sequence.toString();
/*      */     int pos = indexIn(string);
/*      */     if (pos == -1)
/*      */       return string; 
/*      */     char[] chars = string.toCharArray();
/*      */     int spread = 1;
/*      */     while (true) {
/*      */       pos++;
/*      */       while (pos != chars.length) {
/*      */         if (matches(chars[pos])) {
/*      */           spread++;
/*      */           continue;
/*      */         } 
/*      */         chars[pos - spread] = chars[pos];
/*      */         pos++;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */     return new String(chars, 0, pos - spread);
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String retainFrom(CharSequence sequence) { return negate().removeFrom(sequence); }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String replaceFrom(CharSequence sequence, char replacement) {
/*      */     String string = sequence.toString();
/*      */     int pos = indexIn(string);
/*      */     if (pos == -1)
/*      */       return string; 
/*      */     char[] chars = string.toCharArray();
/*      */     chars[pos] = replacement;
/*      */     for (int i = pos + 1; i < chars.length; i++) {
/*      */       if (matches(chars[i]))
/*      */         chars[i] = replacement; 
/*      */     } 
/*      */     return new String(chars);
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String replaceFrom(CharSequence sequence, CharSequence replacement) {
/*      */     int replacementLen = replacement.length();
/*      */     if (replacementLen == 0)
/*      */       return removeFrom(sequence); 
/*      */     if (replacementLen == 1)
/*      */       return replaceFrom(sequence, replacement.charAt(0)); 
/*      */     String string = sequence.toString();
/*      */     int pos = indexIn(string);
/*      */     if (pos == -1)
/*      */       return string; 
/*      */     int len = string.length();
/*      */     StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
/*      */     int oldpos = 0;
/*      */     do {
/*      */       buf.append(string, oldpos, pos);
/*      */       buf.append(replacement);
/*      */       oldpos = pos + 1;
/*      */       pos = indexIn(string, oldpos);
/*      */     } while (pos != -1);
/*      */     buf.append(string, oldpos, len);
/*      */     return buf.toString();
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimFrom(CharSequence sequence) {
/*      */     int len = sequence.length();
/*      */     int first;
/*      */     for (first = 0; first < len && matches(sequence.charAt(first)); first++);
/*      */     int last;
/*      */     for (last = len - 1; last > first && matches(sequence.charAt(last)); last--);
/*      */     return sequence.subSequence(first, last + 1).toString();
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimLeadingFrom(CharSequence sequence) {
/*      */     int len = sequence.length();
/*      */     for (int first = 0; first < len; first++) {
/*      */       if (!matches(sequence.charAt(first)))
/*      */         return sequence.subSequence(first, len).toString(); 
/*      */     } 
/*      */     return "";
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimTrailingFrom(CharSequence sequence) {
/*      */     int len = sequence.length();
/*      */     for (int last = len - 1; last >= 0; last--) {
/*      */       if (!matches(sequence.charAt(last)))
/*      */         return sequence.subSequence(0, last + 1).toString(); 
/*      */     } 
/*      */     return "";
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String collapseFrom(CharSequence sequence, char replacement) {
/*      */     int len = sequence.length();
/*      */     for (int i = 0; i < len; i++) {
/*      */       char c = sequence.charAt(i);
/*      */       if (matches(c))
/*      */         if (c == replacement && (i == len - 1 || !matches(sequence.charAt(i + 1)))) {
/*      */           i++;
/*      */         } else {
/*      */           StringBuilder builder = (new StringBuilder(len)).append(sequence.subSequence(0, i)).append(replacement);
/*      */           return finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
/*      */         }  
/*      */     } 
/*      */     return sequence.toString();
/*      */   }
/*      */   
/*      */   @CheckReturnValue
/*      */   public String trimAndCollapseFrom(CharSequence sequence, char replacement) {
/*      */     int len = sequence.length();
/*      */     int first;
/*      */     for (first = 0; first < len && matches(sequence.charAt(first)); first++);
/*      */     int last;
/*      */     for (last = len - 1; last > first && matches(sequence.charAt(last)); last--);
/*      */     return (first == 0 && last == len - 1) ? collapseFrom(sequence, replacement) : finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
/*      */   }
/*      */   
/*      */   private String finishCollapseFrom(CharSequence sequence, int start, int end, char replacement, StringBuilder builder, boolean inMatchingGroup) {
/*      */     for (int i = start; i < end; i++) {
/*      */       char c = sequence.charAt(i);
/*      */       if (matches(c)) {
/*      */         if (!inMatchingGroup) {
/*      */           builder.append(replacement);
/*      */           inMatchingGroup = true;
/*      */         } 
/*      */       } else {
/*      */         builder.append(c);
/*      */         inMatchingGroup = false;
/*      */       } 
/*      */     } 
/*      */     return builder.toString();
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public boolean apply(Character character) { return matches(character.charValue()); }
/*      */   
/*      */   public String toString() { return this.description; }
/*      */   
/*      */   public abstract boolean matches(char paramChar);
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/CharMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */