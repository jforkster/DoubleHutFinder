/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Beta
/*     */ final class SortedLists
/*     */ {
/*     */   public final abstract enum KeyPresentBehavior
/*     */   {
/*     */     ANY_PRESENT, LAST_PRESENT, FIRST_PRESENT, FIRST_AFTER, LAST_BEFORE;
/*     */     
/*     */     abstract <E> int resultIndex(Comparator<? super E> param1Comparator, E param1E, List<? extends E> param1List, int param1Int);
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new com/google/common/collect/SortedLists$KeyPresentBehavior$1
/*     */       //   3: dup
/*     */       //   4: ldc 'ANY_PRESENT'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic com/google/common/collect/SortedLists$KeyPresentBehavior.ANY_PRESENT : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   13: new com/google/common/collect/SortedLists$KeyPresentBehavior$2
/*     */       //   16: dup
/*     */       //   17: ldc 'LAST_PRESENT'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic com/google/common/collect/SortedLists$KeyPresentBehavior.LAST_PRESENT : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   26: new com/google/common/collect/SortedLists$KeyPresentBehavior$3
/*     */       //   29: dup
/*     */       //   30: ldc 'FIRST_PRESENT'
/*     */       //   32: iconst_2
/*     */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   36: putstatic com/google/common/collect/SortedLists$KeyPresentBehavior.FIRST_PRESENT : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   39: new com/google/common/collect/SortedLists$KeyPresentBehavior$4
/*     */       //   42: dup
/*     */       //   43: ldc 'FIRST_AFTER'
/*     */       //   45: iconst_3
/*     */       //   46: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   49: putstatic com/google/common/collect/SortedLists$KeyPresentBehavior.FIRST_AFTER : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   52: new com/google/common/collect/SortedLists$KeyPresentBehavior$5
/*     */       //   55: dup
/*     */       //   56: ldc 'LAST_BEFORE'
/*     */       //   58: iconst_4
/*     */       //   59: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   62: putstatic com/google/common/collect/SortedLists$KeyPresentBehavior.LAST_BEFORE : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   65: iconst_5
/*     */       //   66: anewarray com/google/common/collect/SortedLists$KeyPresentBehavior
/*     */       //   69: dup
/*     */       //   70: iconst_0
/*     */       //   71: getstatic com/google/common/collect/SortedLists$KeyPresentBehavior.ANY_PRESENT : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   74: aastore
/*     */       //   75: dup
/*     */       //   76: iconst_1
/*     */       //   77: getstatic com/google/common/collect/SortedLists$KeyPresentBehavior.LAST_PRESENT : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   80: aastore
/*     */       //   81: dup
/*     */       //   82: iconst_2
/*     */       //   83: getstatic com/google/common/collect/SortedLists$KeyPresentBehavior.FIRST_PRESENT : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   86: aastore
/*     */       //   87: dup
/*     */       //   88: iconst_3
/*     */       //   89: getstatic com/google/common/collect/SortedLists$KeyPresentBehavior.FIRST_AFTER : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   92: aastore
/*     */       //   93: dup
/*     */       //   94: iconst_4
/*     */       //   95: getstatic com/google/common/collect/SortedLists$KeyPresentBehavior.LAST_BEFORE : Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   98: aastore
/*     */       //   99: putstatic com/google/common/collect/SortedLists$KeyPresentBehavior.$VALUES : [Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
/*     */       //   102: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       //   #63	-> 13
/*     */       //   #87	-> 26
/*     */       //   #113	-> 39
/*     */       //   #124	-> 52
/*     */       //   #48	-> 65
/*     */     }
/*     */   }
/*     */   
/*     */   public final abstract enum KeyAbsentBehavior
/*     */   {
/*     */     NEXT_LOWER, NEXT_HIGHER, INVERTED_INSERTION_INDEX;
/*     */     
/*     */     abstract int resultIndex(int param1Int);
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new com/google/common/collect/SortedLists$KeyAbsentBehavior$1
/*     */       //   3: dup
/*     */       //   4: ldc 'NEXT_LOWER'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.NEXT_LOWER : Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   13: new com/google/common/collect/SortedLists$KeyAbsentBehavior$2
/*     */       //   16: dup
/*     */       //   17: ldc 'NEXT_HIGHER'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.NEXT_HIGHER : Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   26: new com/google/common/collect/SortedLists$KeyAbsentBehavior$3
/*     */       //   29: dup
/*     */       //   30: ldc 'INVERTED_INSERTION_INDEX'
/*     */       //   32: iconst_2
/*     */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   36: putstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.INVERTED_INSERTION_INDEX : Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   39: iconst_3
/*     */       //   40: anewarray com/google/common/collect/SortedLists$KeyAbsentBehavior
/*     */       //   43: dup
/*     */       //   44: iconst_0
/*     */       //   45: getstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.NEXT_LOWER : Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   48: aastore
/*     */       //   49: dup
/*     */       //   50: iconst_1
/*     */       //   51: getstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.NEXT_HIGHER : Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   54: aastore
/*     */       //   55: dup
/*     */       //   56: iconst_2
/*     */       //   57: getstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.INVERTED_INSERTION_INDEX : Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   60: aastore
/*     */       //   61: putstatic com/google/common/collect/SortedLists$KeyAbsentBehavior.$VALUES : [Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
/*     */       //   64: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #144	-> 0
/*     */       //   #154	-> 13
/*     */       //   #172	-> 26
/*     */       //   #139	-> 39
/*     */     }
/*     */   }
/*     */   
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 191 */     Preconditions.checkNotNull(e);
/* 192 */     return binarySearch(list, Preconditions.checkNotNull(e), Ordering.natural(), presentBehavior, absentBehavior);
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
/* 205 */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) { return binarySearch(list, keyFunction, key, Ordering.natural(), presentBehavior, absentBehavior); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @Nullable K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) { return binarySearch(Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> int binarySearch(List<? extends E> list, @Nullable E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 258 */     Preconditions.checkNotNull(comparator);
/* 259 */     Preconditions.checkNotNull(list);
/* 260 */     Preconditions.checkNotNull(presentBehavior);
/* 261 */     Preconditions.checkNotNull(absentBehavior);
/* 262 */     if (!(list instanceof java.util.RandomAccess)) {
/* 263 */       list = Lists.newArrayList(list);
/*     */     }
/*     */ 
/*     */     
/* 267 */     int lower = 0;
/* 268 */     int upper = list.size() - 1;
/*     */     
/* 270 */     while (lower <= upper) {
/* 271 */       int middle = lower + upper >>> 1;
/* 272 */       int c = comparator.compare(key, list.get(middle));
/* 273 */       if (c < 0) {
/* 274 */         upper = middle - 1; continue;
/* 275 */       }  if (c > 0) {
/* 276 */         lower = middle + 1; continue;
/*     */       } 
/* 278 */       return lower + presentBehavior.resultIndex(comparator, key, list.subList(lower, upper + 1), middle - lower);
/*     */     } 
/*     */ 
/*     */     
/* 282 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/SortedLists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */