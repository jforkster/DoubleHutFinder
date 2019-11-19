/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @GwtCompatible
/*     */ public abstract class Ordering<T>
/*     */   extends Object
/*     */   implements Comparator<T>
/*     */ {
/*     */   static final int LEFT_IS_GREATER = 1;
/*     */   static final int RIGHT_IS_GREATER = -1;
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 106 */   public static <C extends Comparable> Ordering<C> natural() { return NaturalOrdering.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 124 */   public static <T> Ordering<T> from(Comparator<T> comparator) { return (comparator instanceof Ordering) ? (Ordering)comparator : new ComparatorOrdering(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @GwtCompatible(serializable = true)
/* 136 */   public static <T> Ordering<T> from(Ordering<T> ordering) { return (Ordering)Preconditions.checkNotNull(ordering); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 162 */   public static <T> Ordering<T> explicit(List<T> valuesInOrder) { return new ExplicitOrdering(valuesInOrder); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 191 */   public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) { return explicit(Lists.asList(leastValue, remainingValuesInOrder)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 223 */   public static Ordering<Object> allEqual() { return AllEqualOrdering.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 235 */   public static Ordering<Object> usingToString() { return UsingToStringOrdering.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   public static Ordering<Object> arbitrary() { return ArbitraryOrderingHolder.ARBITRARY_ORDERING; }
/*     */   
/*     */   private static class ArbitraryOrderingHolder
/*     */   {
/* 259 */     static final Ordering<Object> ARBITRARY_ORDERING = new Ordering.ArbitraryOrdering(); }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ArbitraryOrdering extends Ordering<Object> {
/* 263 */     private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new Function<Object, Integer>()
/*     */         {
/*     */ 
/*     */           
/* 267 */           final AtomicInteger counter = new AtomicInteger(0);
/*     */ 
/*     */           
/* 270 */           public Integer apply(Object from) { return Integer.valueOf(this.counter.getAndIncrement()); }
/*     */         });
/*     */ 
/*     */     
/*     */     public int compare(Object left, Object right) {
/* 275 */       if (left == right)
/* 276 */         return 0; 
/* 277 */       if (left == null)
/* 278 */         return -1; 
/* 279 */       if (right == null) {
/* 280 */         return 1;
/*     */       }
/* 282 */       int leftCode = identityHashCode(left);
/* 283 */       int rightCode = identityHashCode(right);
/* 284 */       if (leftCode != rightCode) {
/* 285 */         return (leftCode < rightCode) ? -1 : 1;
/*     */       }
/*     */ 
/*     */       
/* 289 */       int result = ((Integer)this.uids.get(left)).compareTo((Integer)this.uids.get(right));
/* 290 */       if (result == 0) {
/* 291 */         throw new AssertionError();
/*     */       }
/* 293 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 297 */     public String toString() { return "Ordering.arbitrary()"; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     int identityHashCode(Object object) { return System.identityHashCode(object); }
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
/*     */   @GwtCompatible(serializable = true)
/* 331 */   public <S extends T> Ordering<S> reverse() { return new ReverseOrdering(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 342 */   public <S extends T> Ordering<S> nullsFirst() { return new NullsFirstOrdering(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 353 */   public <S extends T> Ordering<S> nullsLast() { return new NullsLastOrdering(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 367 */   public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) { return new ByFunctionOrdering(function, this); }
/*     */ 
/*     */ 
/*     */   
/* 371 */   <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() { return onResultOf(Maps.keyFunction()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 388 */   public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) { return new CompoundOrdering(this, (Comparator)Preconditions.checkNotNull(secondaryComparator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 409 */   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) { return new CompoundOrdering(comparators); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/* 438 */   public <S extends T> Ordering<Iterable<S>> lexicographical() { return new LexicographicalOrdering(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int compare(@Nullable T paramT1, @Nullable T paramT2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(Iterator<E> iterator) {
/* 461 */     E minSoFar = (E)iterator.next();
/*     */     
/* 463 */     while (iterator.hasNext()) {
/* 464 */       minSoFar = (E)min(minSoFar, iterator.next());
/*     */     }
/*     */     
/* 467 */     return minSoFar;
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
/* 480 */   public <E extends T> E min(Iterable<E> iterable) { return (E)min(iterable.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 497 */   public <E extends T> E min(@Nullable E a, @Nullable E b) { return (compare(a, b) <= 0) ? a : b; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E min(@Nullable E a, @Nullable E b, @Nullable E c, E... rest) {
/* 513 */     E minSoFar = (E)min(min(a, b), c);
/*     */     
/* 515 */     for (E r : rest) {
/* 516 */       minSoFar = (E)min(minSoFar, r);
/*     */     }
/*     */     
/* 519 */     return minSoFar;
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
/*     */   public <E extends T> E max(Iterator<E> iterator) {
/* 537 */     E maxSoFar = (E)iterator.next();
/*     */     
/* 539 */     while (iterator.hasNext()) {
/* 540 */       maxSoFar = (E)max(maxSoFar, iterator.next());
/*     */     }
/*     */     
/* 543 */     return maxSoFar;
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
/* 556 */   public <E extends T> E max(Iterable<E> iterable) { return (E)max(iterable.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 573 */   public <E extends T> E max(@Nullable E a, @Nullable E b) { return (compare(a, b) >= 0) ? a : b; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> E max(@Nullable E a, @Nullable E b, @Nullable E c, E... rest) {
/* 589 */     E maxSoFar = (E)max(max(a, b), c);
/*     */     
/* 591 */     for (E r : rest) {
/* 592 */       maxSoFar = (E)max(maxSoFar, r);
/*     */     }
/*     */     
/* 595 */     return maxSoFar;
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
/*     */   public <E extends T> List<E> leastOf(Iterable<E> iterable, int k) {
/* 613 */     if (iterable instanceof Collection) {
/* 614 */       Collection<E> collection = (Collection)iterable;
/* 615 */       if (collection.size() <= 2L * k) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 621 */         E[] array = (E[])(Object[])collection.toArray();
/* 622 */         Arrays.sort(array, this);
/* 623 */         if (array.length > k) {
/* 624 */           array = (E[])ObjectArrays.arraysCopyOf(array, k);
/*     */         }
/* 626 */         return Collections.unmodifiableList(Arrays.asList(array));
/*     */       } 
/*     */     } 
/* 629 */     return leastOf(iterable.iterator(), k);
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
/*     */   public <E extends T> List<E> leastOf(Iterator<E> elements, int k) {
/* 647 */     Preconditions.checkNotNull(elements);
/* 648 */     CollectPreconditions.checkNonnegative(k, "k");
/*     */     
/* 650 */     if (k == 0 || !elements.hasNext())
/* 651 */       return ImmutableList.of(); 
/* 652 */     if (k >= 1073741823) {
/*     */       
/* 654 */       ArrayList<E> list = Lists.newArrayList(elements);
/* 655 */       Collections.sort(list, this);
/* 656 */       if (list.size() > k) {
/* 657 */         list.subList(k, list.size()).clear();
/*     */       }
/* 659 */       list.trimToSize();
/* 660 */       return Collections.unmodifiableList(list);
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
/*     */ 
/*     */ 
/*     */     
/* 677 */     int bufferCap = k * 2;
/*     */     
/* 679 */     E[] buffer = (E[])(Object[])new Object[bufferCap];
/* 680 */     E threshold = (E)elements.next();
/* 681 */     buffer[0] = threshold;
/* 682 */     int bufferSize = 1;
/*     */ 
/*     */ 
/*     */     
/* 686 */     while (bufferSize < k && elements.hasNext()) {
/* 687 */       E e = (E)elements.next();
/* 688 */       buffer[bufferSize++] = e;
/* 689 */       threshold = (E)max(threshold, e);
/*     */     } 
/*     */     
/* 692 */     while (elements.hasNext()) {
/* 693 */       E e = (E)elements.next();
/* 694 */       if (compare(e, threshold) >= 0) {
/*     */         continue;
/*     */       }
/*     */       
/* 698 */       buffer[bufferSize++] = e;
/* 699 */       if (bufferSize == bufferCap) {
/*     */ 
/*     */         
/* 702 */         int left = 0;
/* 703 */         int right = bufferCap - 1;
/*     */         
/* 705 */         int minThresholdPosition = 0;
/*     */ 
/*     */ 
/*     */         
/* 709 */         while (left < right) {
/* 710 */           int pivotIndex = left + right + 1 >>> 1;
/* 711 */           int pivotNewIndex = partition(buffer, left, right, pivotIndex);
/* 712 */           if (pivotNewIndex > k) {
/* 713 */             right = pivotNewIndex - 1; continue;
/* 714 */           }  if (pivotNewIndex < k) {
/* 715 */             left = Math.max(pivotNewIndex, left + 1);
/* 716 */             minThresholdPosition = pivotNewIndex;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 721 */         bufferSize = k;
/*     */         
/* 723 */         threshold = buffer[minThresholdPosition];
/* 724 */         for (int i = minThresholdPosition + 1; i < bufferSize; i++) {
/* 725 */           threshold = (E)max(threshold, buffer[i]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 730 */     Arrays.sort(buffer, 0, bufferSize, this);
/*     */     
/* 732 */     bufferSize = Math.min(bufferSize, k);
/* 733 */     return Collections.unmodifiableList(Arrays.asList(ObjectArrays.arraysCopyOf(buffer, bufferSize)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <E extends T> int partition(E[] values, int left, int right, int pivotIndex) {
/* 740 */     E pivotValue = values[pivotIndex];
/*     */     
/* 742 */     values[pivotIndex] = values[right];
/* 743 */     values[right] = pivotValue;
/*     */     
/* 745 */     int storeIndex = left;
/* 746 */     for (int i = left; i < right; i++) {
/* 747 */       if (compare(values[i], pivotValue) < 0) {
/* 748 */         ObjectArrays.swap(values, storeIndex, i);
/* 749 */         storeIndex++;
/*     */       } 
/*     */     } 
/* 752 */     ObjectArrays.swap(values, right, storeIndex);
/* 753 */     return storeIndex;
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
/* 773 */   public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k) { return reverse().leastOf(iterable, k); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 791 */   public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k) { return reverse().leastOf(iterator, k); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
/* 814 */     E[] array = (E[])(Object[])Iterables.toArray(elements);
/* 815 */     Arrays.sort(array, this);
/* 816 */     return Lists.newArrayList(Arrays.asList(array));
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
/*     */   public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
/* 840 */     E[] array = (E[])(Object[])Iterables.toArray(elements);
/* 841 */     for (E e : array) {
/* 842 */       Preconditions.checkNotNull(e);
/*     */     }
/* 844 */     Arrays.sort(array, this);
/* 845 */     return ImmutableList.asImmutableList(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOrdered(Iterable<? extends T> iterable) {
/* 855 */     Iterator<? extends T> it = iterable.iterator();
/* 856 */     if (it.hasNext()) {
/* 857 */       T prev = (T)it.next();
/* 858 */       while (it.hasNext()) {
/* 859 */         T next = (T)it.next();
/* 860 */         if (compare(prev, next) > 0) {
/* 861 */           return false;
/*     */         }
/* 863 */         prev = next;
/*     */       } 
/*     */     } 
/* 866 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
/* 876 */     Iterator<? extends T> it = iterable.iterator();
/* 877 */     if (it.hasNext()) {
/* 878 */       T prev = (T)it.next();
/* 879 */       while (it.hasNext()) {
/* 880 */         T next = (T)it.next();
/* 881 */         if (compare(prev, next) >= 0) {
/* 882 */           return false;
/*     */         }
/* 884 */         prev = next;
/*     */       } 
/*     */     } 
/* 887 */     return true;
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
/* 899 */   public int binarySearch(List<? extends T> sortedList, @Nullable T key) { return Collections.binarySearch(sortedList, key, this); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class IncomparableValueException
/*     */     extends ClassCastException
/*     */   {
/*     */     final Object value;
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     IncomparableValueException(Object value) {
/* 914 */       super("Cannot compare value: " + value);
/* 915 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Ordering.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */