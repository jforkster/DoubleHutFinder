/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ObjectArrays
/*     */ {
/*  37 */   static final Object[] EMPTY_ARRAY = new Object[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Array.newInstance(Class, int)")
/*  50 */   public static <T> T[] newArray(Class<T> type, int length) { return (T[])(Object[])Array.newInstance(type, length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static <T> T[] newArray(T[] reference, int length) { return (T[])Platform.newArray(reference, length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Array.newInstance(Class, int)")
/*     */   public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
/*  73 */     T[] result = (T[])newArray(type, first.length + second.length);
/*  74 */     System.arraycopy(first, 0, result, 0, first.length);
/*  75 */     System.arraycopy(second, 0, result, first.length, second.length);
/*  76 */     return result;
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
/*     */   public static <T> T[] concat(@Nullable T element, T[] array) {
/*  89 */     T[] result = (T[])newArray(array, array.length + 1);
/*  90 */     result[0] = element;
/*  91 */     System.arraycopy(array, 0, result, 1, array.length);
/*  92 */     return result;
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
/*     */   public static <T> T[] concat(T[] array, @Nullable T element) {
/* 105 */     T[] result = (T[])arraysCopyOf(array, array.length + 1);
/* 106 */     result[array.length] = element;
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> T[] arraysCopyOf(T[] original, int newLength) {
/* 112 */     T[] copy = (T[])newArray(original, newLength);
/* 113 */     System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
/*     */     
/* 115 */     return copy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
/* 143 */     int size = c.size();
/* 144 */     if (array.length < size) {
/* 145 */       array = (T[])newArray(array, size);
/*     */     }
/* 147 */     fillArray(c, array);
/* 148 */     if (array.length > size) {
/* 149 */       array[size] = null;
/*     */     }
/* 151 */     return array;
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
/*     */   static <T> T[] toArrayImpl(Object[] src, int offset, int len, T[] dst) {
/* 166 */     Preconditions.checkPositionIndexes(offset, offset + len, src.length);
/* 167 */     if (dst.length < len) {
/* 168 */       dst = (T[])newArray(dst, len);
/* 169 */     } else if (dst.length > len) {
/* 170 */       dst[len] = null;
/*     */     } 
/* 172 */     System.arraycopy(src, offset, dst, 0, len);
/* 173 */     return dst;
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
/* 191 */   static Object[] toArrayImpl(Collection<?> c) { return fillArray(c, new Object[c.size()]); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object[] copyAsObjectArray(Object[] elements, int offset, int length) {
/* 199 */     Preconditions.checkPositionIndexes(offset, offset + length, elements.length);
/* 200 */     if (length == 0) {
/* 201 */       return EMPTY_ARRAY;
/*     */     }
/* 203 */     Object[] result = new Object[length];
/* 204 */     System.arraycopy(elements, offset, result, 0, length);
/* 205 */     return result;
/*     */   }
/*     */   
/*     */   private static Object[] fillArray(Iterable<?> elements, Object[] array) {
/* 209 */     int i = 0;
/* 210 */     for (Object element : elements) {
/* 211 */       array[i++] = element;
/*     */     }
/* 213 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void swap(Object[] array, int i, int j) {
/* 220 */     Object temp = array[i];
/* 221 */     array[i] = array[j];
/* 222 */     array[j] = temp;
/*     */   }
/*     */ 
/*     */   
/* 226 */   static Object[] checkElementsNotNull(Object... array) { return checkElementsNotNull(array, array.length); }
/*     */ 
/*     */   
/*     */   static Object[] checkElementsNotNull(Object[] array, int length) {
/* 230 */     for (int i = 0; i < length; i++) {
/* 231 */       checkElementNotNull(array[i], i);
/*     */     }
/* 233 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Object checkElementNotNull(Object element, int index) {
/* 239 */     if (element == null) {
/* 240 */       throw new NullPointerException("at index " + index);
/*     */     }
/* 242 */     return element;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ObjectArrays.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */