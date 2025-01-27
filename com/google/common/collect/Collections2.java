/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.math.LongMath;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ public final class Collections2
/*     */ {
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/*  91 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/*  94 */       return ((FilteredCollection)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/*  97 */     return new FilteredCollection((Collection)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeContains(Collection<?> collection, @Nullable Object object) {
/* 108 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 110 */       return collection.contains(object);
/* 111 */     } catch (ClassCastException e) {
/* 112 */       return false;
/* 113 */     } catch (NullPointerException e) {
/* 114 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeRemove(Collection<?> collection, @Nullable Object object) {
/* 124 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 126 */       return collection.remove(object);
/* 127 */     } catch (ClassCastException e) {
/* 128 */       return false;
/* 129 */     } catch (NullPointerException e) {
/* 130 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E>
/*     */     extends AbstractCollection<E> {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 140 */       this.unfiltered = unfiltered;
/* 141 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/* 145 */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) { return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 152 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 153 */       return this.unfiltered.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 158 */       for (E element : collection) {
/* 159 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 161 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     public void clear() { Iterables.removeIf(this.unfiltered, this.predicate); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object element) {
/* 171 */       if (Collections2.safeContains(this.unfiltered, element)) {
/*     */         
/* 173 */         E e = (E)element;
/* 174 */         return this.predicate.apply(e);
/*     */       } 
/* 176 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 181 */     public boolean containsAll(Collection<?> collection) { return Collections2.containsAllImpl(this, collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     public boolean isEmpty() { return !Iterables.any(this.unfiltered, this.predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     public Iterator<E> iterator() { return Iterators.filter(this.unfiltered.iterator(), this.predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     public boolean remove(Object element) { return (contains(element) && this.unfiltered.remove(element)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     public boolean removeAll(Collection<?> collection) { return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.in(collection))); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     public boolean retainAll(Collection<?> collection) { return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.not(Predicates.in(collection)))); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     public int size() { return Iterators.size(iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 217 */     public Object[] toArray() { return Lists.newArrayList(iterator()).toArray(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     public <T> T[] toArray(T[] array) { return (T[])Lists.newArrayList(iterator()).toArray(array); }
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
/* 247 */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) { return new TransformedCollection(fromCollection, function); }
/*     */   
/*     */   static class TransformedCollection<F, T>
/*     */     extends AbstractCollection<T>
/*     */   {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 256 */       this.fromCollection = (Collection)Preconditions.checkNotNull(fromCollection);
/* 257 */       this.function = (Function)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/* 261 */     public void clear() { this.fromCollection.clear(); }
/*     */ 
/*     */ 
/*     */     
/* 265 */     public boolean isEmpty() { return this.fromCollection.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/* 269 */     public Iterator<T> iterator() { return Iterators.transform(this.fromCollection.iterator(), this.function); }
/*     */ 
/*     */ 
/*     */     
/* 273 */     public int size() { return this.fromCollection.size(); }
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
/* 290 */   static boolean containsAllImpl(Collection<?> self, Collection<?> c) { return Iterables.all(c, Predicates.in(self)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toStringImpl(final Collection<?> collection) {
/* 297 */     StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
/*     */     
/* 299 */     STANDARD_JOINER.appendTo(sb, Iterables.transform(collection, new Function<Object, Object>()
/*     */           {
/*     */             public Object apply(Object input) {
/* 302 */               return (input == collection) ? "(this Collection)" : input;
/*     */             }
/*     */           }));
/* 305 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static StringBuilder newStringBuilderForCollection(int size) {
/* 312 */     CollectPreconditions.checkNonnegative(size, "size");
/* 313 */     return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   static <T> Collection<T> cast(Iterable<T> iterable) { return (Collection)iterable; }
/*     */ 
/*     */   
/* 323 */   static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 354 */   public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) { return orderedPermutations(elements, Ordering.natural()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 406 */   public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) { return new OrderedPermutationCollection(elements, comparator); }
/*     */ 
/*     */   
/*     */   private static final class OrderedPermutationCollection<E>
/*     */     extends AbstractCollection<List<E>>
/*     */   {
/*     */     final ImmutableList<E> inputList;
/*     */     final Comparator<? super E> comparator;
/*     */     final int size;
/*     */     
/*     */     OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
/* 417 */       this.inputList = Ordering.from(comparator).immutableSortedCopy(input);
/* 418 */       this.comparator = comparator;
/* 419 */       this.size = calculateSize(this.inputList, comparator);
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
/*     */     private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
/* 433 */       long permutations = 1L;
/* 434 */       int n = 1;
/* 435 */       int r = 1;
/* 436 */       while (n < sortedInputList.size()) {
/* 437 */         int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
/*     */         
/* 439 */         if (comparison < 0) {
/*     */           
/* 441 */           permutations *= LongMath.binomial(n, r);
/* 442 */           r = 0;
/* 443 */           if (!Collections2.isPositiveInt(permutations)) {
/* 444 */             return Integer.MAX_VALUE;
/*     */           }
/*     */         } 
/* 447 */         n++;
/* 448 */         r++;
/*     */       } 
/* 450 */       permutations *= LongMath.binomial(n, r);
/* 451 */       if (!Collections2.isPositiveInt(permutations)) {
/* 452 */         return Integer.MAX_VALUE;
/*     */       }
/* 454 */       return (int)permutations;
/*     */     }
/*     */ 
/*     */     
/* 458 */     public int size() { return this.size; }
/*     */ 
/*     */ 
/*     */     
/* 462 */     public boolean isEmpty() { return false; }
/*     */ 
/*     */ 
/*     */     
/* 466 */     public Iterator<List<E>> iterator() { return new Collections2.OrderedPermutationIterator(this.inputList, this.comparator); }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object obj) {
/* 470 */       if (obj instanceof List) {
/* 471 */         List<?> list = (List)obj;
/* 472 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 474 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 478 */     public String toString() { return "orderedPermutationCollection(" + this.inputList + ")"; }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class OrderedPermutationIterator<E>
/*     */     extends AbstractIterator<List<E>>
/*     */   {
/*     */     List<E> nextPermutation;
/*     */     
/*     */     final Comparator<? super E> comparator;
/*     */     
/*     */     OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
/* 490 */       this.nextPermutation = Lists.newArrayList(list);
/* 491 */       this.comparator = comparator;
/*     */     }
/*     */     
/*     */     protected List<E> computeNext() {
/* 495 */       if (this.nextPermutation == null) {
/* 496 */         return (List)endOfData();
/*     */       }
/* 498 */       ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
/* 499 */       calculateNextPermutation();
/* 500 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 504 */       int j = findNextJ();
/* 505 */       if (j == -1) {
/* 506 */         this.nextPermutation = null;
/*     */         
/*     */         return;
/*     */       } 
/* 510 */       int l = findNextL(j);
/* 511 */       Collections.swap(this.nextPermutation, j, l);
/* 512 */       int n = this.nextPermutation.size();
/* 513 */       Collections.reverse(this.nextPermutation.subList(j + 1, n));
/*     */     }
/*     */     
/*     */     int findNextJ() {
/* 517 */       for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
/* 518 */         if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0)
/*     */         {
/* 520 */           return k;
/*     */         }
/*     */       } 
/* 523 */       return -1;
/*     */     }
/*     */     
/*     */     int findNextL(int j) {
/* 527 */       E ak = (E)this.nextPermutation.get(j);
/* 528 */       for (int l = this.nextPermutation.size() - 1; l > j; l--) {
/* 529 */         if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
/* 530 */           return l;
/*     */         }
/*     */       } 
/* 533 */       throw new AssertionError("this statement should be unreachable");
/*     */     }
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
/*     */   @Beta
/* 559 */   public static <E> Collection<List<E>> permutations(Collection<E> elements) { return new PermutationCollection(ImmutableList.copyOf(elements)); }
/*     */ 
/*     */   
/*     */   private static final class PermutationCollection<E>
/*     */     extends AbstractCollection<List<E>>
/*     */   {
/*     */     final ImmutableList<E> inputList;
/*     */     
/* 567 */     PermutationCollection(ImmutableList<E> input) { this.inputList = input; }
/*     */ 
/*     */ 
/*     */     
/* 571 */     public int size() { return IntMath.factorial(this.inputList.size()); }
/*     */ 
/*     */ 
/*     */     
/* 575 */     public boolean isEmpty() { return false; }
/*     */ 
/*     */ 
/*     */     
/* 579 */     public Iterator<List<E>> iterator() { return new Collections2.PermutationIterator(this.inputList); }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object obj) {
/* 583 */       if (obj instanceof List) {
/* 584 */         List<?> list = (List)obj;
/* 585 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 587 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 591 */     public String toString() { return "permutations(" + this.inputList + ")"; }
/*     */   }
/*     */   
/*     */   private static class PermutationIterator<E>
/*     */     extends AbstractIterator<List<E>>
/*     */   {
/*     */     final List<E> list;
/*     */     final int[] c;
/*     */     final int[] o;
/*     */     int j;
/*     */     
/*     */     PermutationIterator(List<E> list) {
/* 603 */       this.list = new ArrayList(list);
/* 604 */       int n = list.size();
/* 605 */       this.c = new int[n];
/* 606 */       this.o = new int[n];
/* 607 */       Arrays.fill(this.c, 0);
/* 608 */       Arrays.fill(this.o, 1);
/* 609 */       this.j = Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     protected List<E> computeNext() {
/* 613 */       if (this.j <= 0) {
/* 614 */         return (List)endOfData();
/*     */       }
/* 616 */       ImmutableList<E> next = ImmutableList.copyOf(this.list);
/* 617 */       calculateNextPermutation();
/* 618 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 622 */       this.j = this.list.size() - 1;
/* 623 */       int s = 0;
/*     */ 
/*     */ 
/*     */       
/* 627 */       if (this.j == -1) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 632 */         int q = this.c[this.j] + this.o[this.j];
/* 633 */         if (q < 0) {
/* 634 */           switchDirection();
/*     */           continue;
/*     */         } 
/* 637 */         if (q == this.j + 1) {
/* 638 */           if (this.j == 0) {
/*     */             break;
/*     */           }
/* 641 */           s++;
/* 642 */           switchDirection();
/*     */           
/*     */           continue;
/*     */         } 
/* 646 */         Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
/* 647 */         this.c[this.j] = q;
/*     */         break;
/*     */       } 
/*     */     }
/*     */     
/*     */     void switchDirection() {
/* 653 */       this.o[this.j] = -this.o[this.j];
/* 654 */       this.j--;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPermutation(List<?> first, List<?> second) {
/* 663 */     if (first.size() != second.size()) {
/* 664 */       return false;
/*     */     }
/* 666 */     Multiset<?> firstMultiset = HashMultiset.create(first);
/* 667 */     Multiset<?> secondMultiset = HashMultiset.create(second);
/* 668 */     return firstMultiset.equals(secondMultiset);
/*     */   }
/*     */ 
/*     */   
/* 672 */   private static boolean isPositiveInt(long n) { return (n >= 0L && n <= 2147483647L); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Collections2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */