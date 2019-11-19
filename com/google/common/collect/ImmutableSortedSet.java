/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements NavigableSet<E>, SortedIterable<E>
/*     */ {
/*  97 */   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
/*     */ 
/*     */   
/* 100 */   private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(NATURAL_ORDER);
/*     */   final Comparator<? super E> comparator;
/*     */   @GwtIncompatible("NavigableSet")
/*     */   ImmutableSortedSet<E> descendingSet;
/*     */   
/* 105 */   private static <E> ImmutableSortedSet<E> emptySet() { return NATURAL_EMPTY_SET; }
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/* 110 */     if (NATURAL_ORDER.equals(comparator)) {
/* 111 */       return emptySet();
/*     */     }
/* 113 */     return new EmptyImmutableSortedSet(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static <E> ImmutableSortedSet<E> of() { return emptySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) { return new RegularImmutableSortedSet(ImmutableList.of(element), Ordering.natural()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) { return construct(Ordering.natural(), 2, new Comparable[] { e1, e2 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) { return construct(Ordering.natural(), 3, new Comparable[] { e1, e2, e3 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) { return construct(Ordering.natural(), 4, new Comparable[] { e1, e2, e3, e4 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) { return construct(Ordering.natural(), 5, new Comparable[] { e1, e2, e3, e4, e5 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 196 */     Comparable[] contents = new Comparable[6 + remaining.length];
/* 197 */     contents[0] = e1;
/* 198 */     contents[1] = e2;
/* 199 */     contents[2] = e3;
/* 200 */     contents[3] = e4;
/* 201 */     contents[4] = e5;
/* 202 */     contents[5] = e6;
/* 203 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/* 204 */     return construct(Ordering.natural(), contents.length, (Comparable[])contents);
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
/* 219 */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) { return construct(Ordering.natural(), elements.length, (Object[])elements.clone()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
/* 253 */     Ordering<E> naturalOrder = Ordering.natural();
/* 254 */     return copyOf(naturalOrder, elements);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
/* 291 */     Ordering<E> naturalOrder = Ordering.natural();
/* 292 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
/* 311 */     Ordering<E> naturalOrder = Ordering.natural();
/* 312 */     return copyOf(naturalOrder, elements);
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
/* 326 */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) { return (new Builder(comparator)).addAll(elements).build(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 344 */     Preconditions.checkNotNull(comparator);
/* 345 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*     */ 
/*     */     
/* 348 */     if (hasSameComparator && elements instanceof ImmutableSortedSet) {
/*     */       
/* 350 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/* 351 */       if (!original.isPartialView()) {
/* 352 */         return original;
/*     */       }
/*     */     } 
/*     */     
/* 356 */     E[] array = (E[])(Object[])Iterables.toArray(elements);
/* 357 */     return construct(comparator, array.length, array);
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
/* 380 */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) { return copyOf(comparator, elements); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
/* 401 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/* 402 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/* 403 */     if (list.isEmpty()) {
/* 404 */       return emptySet(comparator);
/*     */     }
/* 406 */     return new RegularImmutableSortedSet(list, comparator);
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
/*     */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
/* 424 */     if (n == 0) {
/* 425 */       return emptySet(comparator);
/*     */     }
/* 427 */     ObjectArrays.checkElementsNotNull(contents, n);
/* 428 */     Arrays.sort(contents, 0, n, comparator);
/* 429 */     int uniques = 1;
/* 430 */     for (int i = 1; i < n; i++) {
/* 431 */       E cur = contents[i];
/* 432 */       E prev = contents[uniques - 1];
/* 433 */       if (comparator.compare(cur, prev) != 0) {
/* 434 */         contents[uniques++] = cur;
/*     */       }
/*     */     } 
/* 437 */     Arrays.fill(contents, uniques, n, null);
/* 438 */     return new RegularImmutableSortedSet(ImmutableList.asImmutableList(contents, uniques), comparator);
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
/* 451 */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) { return new Builder(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 459 */   public static <E extends Comparable<?>> Builder<E> reverseOrder() { return new Builder(Ordering.natural().reverse()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 470 */   public static <E extends Comparable<?>> Builder<E> naturalOrder() { return new Builder(Ordering.natural()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 498 */     public Builder(Comparator<? super E> comparator) { this.comparator = (Comparator)Preconditions.checkNotNull(comparator); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E element) {
/* 512 */       super.add(element);
/* 513 */       return this;
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
/*     */     public Builder<E> add(E... elements) {
/* 525 */       super.add(elements);
/* 526 */       return this;
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 538 */       super.addAll(elements);
/* 539 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 551 */       super.addAll(elements);
/* 552 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedSet<E> build() {
/* 561 */       E[] contentsArray = (E[])(Object[])this.contents;
/* 562 */       ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, contentsArray);
/* 563 */       this.size = result.size();
/* 564 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 569 */   int unsafeCompare(Object a, Object b) { return unsafeCompare(this.comparator, a, b); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
/* 578 */     Comparator<Object> unsafeComparator = comparator;
/* 579 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 585 */   ImmutableSortedSet(Comparator<? super E> comparator) { this.comparator = comparator; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 597 */   public Comparator<? super E> comparator() { return this.comparator; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 616 */   public ImmutableSortedSet<E> headSet(E toElement) { return headSet(toElement, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 625 */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) { return headSetImpl(Preconditions.checkNotNull(toElement), inclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 643 */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement) { return subSet(fromElement, true, toElement, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 653 */     Preconditions.checkNotNull(fromElement);
/* 654 */     Preconditions.checkNotNull(toElement);
/* 655 */     Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
/* 656 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/* 672 */   public ImmutableSortedSet<E> tailSet(E fromElement) { return tailSet(fromElement, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 681 */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) { return tailSetImpl(Preconditions.checkNotNull(fromElement), inclusive); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 701 */   public E lower(E e) { return (E)Iterators.getNext(headSet(e, false).descendingIterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 710 */   public E floor(E e) { return (E)Iterators.getNext(headSet(e, true).descendingIterator(), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 719 */   public E ceiling(E e) { return (E)Iterables.getFirst(tailSet(e, true), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 728 */   public E higher(E e) { return (E)Iterables.getFirst(tailSet(e, false), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 733 */   public E first() { return (E)iterator().next(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 738 */   public E last() { return (E)descendingIterator().next(); }
/*     */ 
/*     */ 
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
/*     */   @GwtIncompatible("NavigableSet")
/* 752 */   public final E pollFirst() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
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
/*     */   @GwtIncompatible("NavigableSet")
/* 766 */   public final E pollLast() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public ImmutableSortedSet<E> descendingSet() {
/* 779 */     ImmutableSortedSet<E> result = this.descendingSet;
/* 780 */     if (result == null) {
/* 781 */       result = this.descendingSet = createDescendingSet();
/* 782 */       result.descendingSet = this;
/*     */     } 
/* 784 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 789 */   ImmutableSortedSet<E> createDescendingSet() { return new DescendingImmutableSortedSet(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm<E>
/*     */     extends Object
/*     */     implements Serializable
/*     */   {
/*     */     final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Object[] elements;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
/* 815 */       this.comparator = comparator;
/* 816 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 821 */     Object readResolve() { return (new ImmutableSortedSet.Builder(this.comparator)).add((Object[])this.elements).build(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 829 */   private void readObject(ObjectInputStream stream) throws InvalidObjectException { throw new InvalidObjectException("Use SerializedForm"); }
/*     */ 
/*     */ 
/*     */   
/* 833 */   Object writeReplace() { return new SerializedForm(this.comparator, toArray()); }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*     */   
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/*     */   public abstract UnmodifiableIterator<E> descendingIterator();
/*     */   
/*     */   abstract int indexOf(@Nullable Object paramObject);
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */