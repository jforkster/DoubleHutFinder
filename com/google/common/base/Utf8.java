/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Utf8
/*     */ {
/*     */   public static int encodedLength(CharSequence sequence) {
/*  50 */     int utf16Length = sequence.length();
/*  51 */     int utf8Length = utf16Length;
/*  52 */     int i = 0;
/*     */ 
/*     */     
/*  55 */     while (i < utf16Length && sequence.charAt(i) < '') {
/*  56 */       i++;
/*     */     }
/*     */ 
/*     */     
/*  60 */     for (; i < utf16Length; i++) {
/*  61 */       char c = sequence.charAt(i);
/*  62 */       if (c < 'ࠀ') {
/*  63 */         utf8Length += ('' - c >>> '\037');
/*     */       } else {
/*  65 */         utf8Length += encodedLengthGeneral(sequence, i);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  70 */     if (utf8Length < utf16Length)
/*     */     {
/*  72 */       throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (utf8Length + 4294967296L));
/*     */     }
/*     */     
/*  75 */     return utf8Length;
/*     */   }
/*     */   
/*     */   private static int encodedLengthGeneral(CharSequence sequence, int start) {
/*  79 */     int utf16Length = sequence.length();
/*  80 */     int utf8Length = 0;
/*  81 */     for (int i = start; i < utf16Length; i++) {
/*  82 */       char c = sequence.charAt(i);
/*  83 */       if (c < 'ࠀ') {
/*  84 */         utf8Length += ('' - c >>> '\037');
/*     */       } else {
/*  86 */         utf8Length += 2;
/*     */         
/*  88 */         if ('?' <= c && c <= '?') {
/*     */           
/*  90 */           int cp = Character.codePointAt(sequence, i);
/*  91 */           if (cp < 65536) {
/*  92 */             throw new IllegalArgumentException("Unpaired surrogate at index " + i);
/*     */           }
/*  94 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     return utf8Length;
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
/* 112 */   public static boolean isWellFormed(byte[] bytes) { return isWellFormed(bytes, 0, bytes.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWellFormed(byte[] bytes, int off, int len) {
/* 125 */     int end = off + len;
/* 126 */     Preconditions.checkPositionIndexes(off, end, bytes.length);
/*     */     
/* 128 */     for (int i = off; i < end; i++) {
/* 129 */       if (bytes[i] < 0) {
/* 130 */         return isWellFormedSlowPath(bytes, i, end);
/*     */       }
/*     */     } 
/* 133 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isWellFormedSlowPath(byte[] bytes, int off, int end) {
/* 137 */     int index = off;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 143 */       if (index >= end)
/* 144 */         return true; 
/*     */       int byte1;
/* 146 */       if ((byte1 = bytes[index++]) < 0) {
/*     */         
/* 148 */         if (byte1 < -32) {
/*     */           
/* 150 */           if (index == end) {
/* 151 */             return false;
/*     */           }
/*     */ 
/*     */           
/* 155 */           if (byte1 < -62 || bytes[index++] > -65)
/* 156 */             return false;  continue;
/*     */         } 
/* 158 */         if (byte1 < -16) {
/*     */           
/* 160 */           if (index + 1 >= end) {
/* 161 */             return false;
/*     */           }
/* 163 */           int byte2 = bytes[index++];
/* 164 */           if (byte2 > -65 || (byte1 == -32 && byte2 < -96) || (byte1 == -19 && -96 <= byte2) || bytes[index++] > -65)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 171 */             return false;
/*     */           }
/*     */           continue;
/*     */         } 
/* 175 */         if (index + 2 >= end) {
/* 176 */           return false;
/*     */         }
/* 178 */         int byte2 = bytes[index++];
/* 179 */         if (byte2 > -65 || (byte1 << 28) + byte2 - -112 >> 30 != 0 || bytes[index++] > -65 || bytes[index++] > -65) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Utf8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */