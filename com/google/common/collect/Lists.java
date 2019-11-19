/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.AbstractList;
/*      */ import java.util.AbstractSequentialList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Lists
/*      */ {
/*      */   @GwtCompatible(serializable = true)
/*   84 */   public static <E> ArrayList<E> newArrayList() { return new ArrayList(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  100 */     Preconditions.checkNotNull(elements);
/*      */     
/*  102 */     int capacity = computeArrayListCapacity(elements.length);
/*  103 */     ArrayList<E> list = new ArrayList<E>(capacity);
/*  104 */     Collections.addAll(list, elements);
/*  105 */     return list;
/*      */   }
/*      */   @VisibleForTesting
/*      */   static int computeArrayListCapacity(int arraySize) {
/*  109 */     CollectPreconditions.checkNonnegative(arraySize, "arraySize");
/*      */ 
/*      */     
/*  112 */     return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/*  127 */     Preconditions.checkNotNull(elements);
/*      */     
/*  129 */     return (elements instanceof Collection) ? new ArrayList(Collections2.cast(elements)) : newArrayList(elements.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/*  146 */     ArrayList<E> list = newArrayList();
/*  147 */     Iterators.addAll(list, elements);
/*  148 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/*  174 */     CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
/*  175 */     return new ArrayList(initialArraySize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*  196 */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) { return new ArrayList(computeArrayListCapacity(estimatedSize)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*  211 */   public static <E> LinkedList<E> newLinkedList() { return new LinkedList(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/*  223 */     LinkedList<E> list = newLinkedList();
/*  224 */     Iterables.addAll(list, elements);
/*  225 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("CopyOnWriteArrayList")
/*  239 */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() { return new CopyOnWriteArrayList(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("CopyOnWriteArrayList")
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
/*  254 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.cast(elements) : newArrayList(elements);
/*      */ 
/*      */     
/*  257 */     return new CopyOnWriteArrayList(elementsCollection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  277 */   public static <E> List<E> asList(@Nullable E first, E[] rest) { return new OnePlusArrayList(first, rest); }
/*      */   
/*      */   private static class OnePlusArrayList<E>
/*      */     extends AbstractList<E>
/*      */     implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     OnePlusArrayList(@Nullable E first, E[] rest) {
/*  287 */       this.first = first;
/*  288 */       this.rest = (Object[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */     
/*  291 */     public int size() { return this.rest.length + 1; }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  295 */       Preconditions.checkElementIndex(index, size());
/*  296 */       return (E)((index == 0) ? this.first : this.rest[index - 1]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  320 */   public static <E> List<E> asList(@Nullable E first, @Nullable E second, E[] rest) { return new TwoPlusArrayList(first, second, rest); }
/*      */   
/*      */   private static class TwoPlusArrayList<E>
/*      */     extends AbstractList<E>
/*      */     implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E second;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TwoPlusArrayList(@Nullable E first, @Nullable E second, E[] rest) {
/*  331 */       this.first = first;
/*  332 */       this.second = second;
/*  333 */       this.rest = (Object[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */     
/*  336 */     public int size() { return this.rest.length + 2; }
/*      */     
/*      */     public E get(int index) {
/*  339 */       switch (index) {
/*      */         case 0:
/*  341 */           return (E)this.first;
/*      */         case 1:
/*  343 */           return (E)this.second;
/*      */       } 
/*      */       
/*  346 */       Preconditions.checkElementIndex(index, size());
/*  347 */       return (E)this.rest[index - 2];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  410 */   static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) { return CartesianList.create(lists); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  470 */   static <B> List<List<B>> cartesianProduct(List... lists) { return cartesianProduct(Arrays.asList(lists)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  508 */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) { return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList(fromList, function) : new TransformingSequentialList(fromList, function); }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingSequentialList<F, T>
/*      */     extends AbstractSequentialList<T>
/*      */     implements Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  525 */       this.fromList = (List)Preconditions.checkNotNull(fromList);
/*  526 */       this.function = (Function)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  534 */     public void clear() { this.fromList.clear(); }
/*      */ 
/*      */     
/*  537 */     public int size() { return this.fromList.size(); }
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  540 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  543 */             return (T)Lists.TransformingSequentialList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingRandomAccessList<F, T>
/*      */     extends AbstractList<T>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  566 */       this.fromList = (List)Preconditions.checkNotNull(fromList);
/*  567 */       this.function = (Function)Preconditions.checkNotNull(function);
/*      */     }
/*      */     
/*  570 */     public void clear() { this.fromList.clear(); }
/*      */ 
/*      */     
/*  573 */     public T get(int index) { return (T)this.function.apply(this.fromList.get(index)); }
/*      */ 
/*      */     
/*  576 */     public Iterator<T> iterator() { return listIterator(); }
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  579 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  582 */             return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*  587 */     public boolean isEmpty() { return this.fromList.isEmpty(); }
/*      */ 
/*      */     
/*  590 */     public T remove(int index) { return (T)this.function.apply(this.fromList.remove(index)); }
/*      */ 
/*      */     
/*  593 */     public int size() { return this.fromList.size(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<List<T>> partition(List<T> list, int size) {
/*  617 */     Preconditions.checkNotNull(list);
/*  618 */     Preconditions.checkArgument((size > 0));
/*  619 */     return (list instanceof RandomAccess) ? new RandomAccessPartition(list, size) : new Partition(list, size);
/*      */   }
/*      */   
/*      */   private static class Partition<T>
/*      */     extends AbstractList<List<T>>
/*      */   {
/*      */     final List<T> list;
/*      */     final int size;
/*      */     
/*      */     Partition(List<T> list, int size) {
/*  629 */       this.list = list;
/*  630 */       this.size = size;
/*      */     }
/*      */     
/*      */     public List<T> get(int index) {
/*  634 */       Preconditions.checkElementIndex(index, size());
/*  635 */       int start = index * this.size;
/*  636 */       int end = Math.min(start + this.size, this.list.size());
/*  637 */       return this.list.subList(start, end);
/*      */     }
/*      */ 
/*      */     
/*  641 */     public int size() { return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING); }
/*      */ 
/*      */ 
/*      */     
/*  645 */     public boolean isEmpty() { return this.list.isEmpty(); }
/*      */   }
/*      */   
/*      */   private static class RandomAccessPartition<T>
/*      */     extends Partition<T>
/*      */     implements RandomAccess
/*      */   {
/*  652 */     RandomAccessPartition(List<T> list, int size) { super(list, size); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*  663 */   public static ImmutableList<Character> charactersOf(String string) { return new StringAsImmutableList((String)Preconditions.checkNotNull(string)); }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class StringAsImmutableList
/*      */     extends ImmutableList<Character>
/*      */   {
/*      */     private final String string;
/*      */ 
/*      */     
/*  673 */     StringAsImmutableList(String string) { this.string = string; }
/*      */ 
/*      */ 
/*      */     
/*  677 */     public int indexOf(@Nullable Object object) { return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  682 */     public int lastIndexOf(@Nullable Object object) { return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ImmutableList<Character> subList(int fromIndex, int toIndex) {
/*  688 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  689 */       return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
/*      */     }
/*      */ 
/*      */     
/*  693 */     boolean isPartialView() { return false; }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  697 */       Preconditions.checkElementIndex(index, size());
/*  698 */       return Character.valueOf(this.string.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*  702 */     public int size() { return this.string.length(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*  718 */   public static List<Character> charactersOf(CharSequence sequence) { return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence)); }
/*      */ 
/*      */   
/*      */   private static final class CharSequenceAsList
/*      */     extends AbstractList<Character>
/*      */   {
/*      */     private final CharSequence sequence;
/*      */     
/*  726 */     CharSequenceAsList(CharSequence sequence) { this.sequence = sequence; }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  730 */       Preconditions.checkElementIndex(index, size());
/*  731 */       return Character.valueOf(this.sequence.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*  735 */     public int size() { return this.sequence.length(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> List<T> reverse(List<T> list) {
/*  752 */     if (list instanceof ImmutableList)
/*  753 */       return ((ImmutableList)list).reverse(); 
/*  754 */     if (list instanceof ReverseList)
/*  755 */       return ((ReverseList)list).getForwardList(); 
/*  756 */     if (list instanceof RandomAccess) {
/*  757 */       return new RandomAccessReverseList(list);
/*      */     }
/*  759 */     return new ReverseList(list);
/*      */   }
/*      */   
/*      */   private static class ReverseList<T>
/*      */     extends AbstractList<T>
/*      */   {
/*      */     private final List<T> forwardList;
/*      */     
/*  767 */     ReverseList(List<T> forwardList) { this.forwardList = (List)Preconditions.checkNotNull(forwardList); }
/*      */ 
/*      */ 
/*      */     
/*  771 */     List<T> getForwardList() { return this.forwardList; }
/*      */ 
/*      */     
/*      */     private int reverseIndex(int index) {
/*  775 */       int size = size();
/*  776 */       Preconditions.checkElementIndex(index, size);
/*  777 */       return size - 1 - index;
/*      */     }
/*      */     
/*      */     private int reversePosition(int index) {
/*  781 */       int size = size();
/*  782 */       Preconditions.checkPositionIndex(index, size);
/*  783 */       return size - index;
/*      */     }
/*      */ 
/*      */     
/*  787 */     public void add(int index, @Nullable T element) { this.forwardList.add(reversePosition(index), element); }
/*      */ 
/*      */ 
/*      */     
/*  791 */     public void clear() { this.forwardList.clear(); }
/*      */ 
/*      */ 
/*      */     
/*  795 */     public T remove(int index) { return (T)this.forwardList.remove(reverseIndex(index)); }
/*      */ 
/*      */ 
/*      */     
/*  799 */     protected void removeRange(int fromIndex, int toIndex) { subList(fromIndex, toIndex).clear(); }
/*      */ 
/*      */ 
/*      */     
/*  803 */     public T set(int index, @Nullable T element) { return (T)this.forwardList.set(reverseIndex(index), element); }
/*      */ 
/*      */ 
/*      */     
/*  807 */     public T get(int index) { return (T)this.forwardList.get(reverseIndex(index)); }
/*      */ 
/*      */ 
/*      */     
/*  811 */     public int size() { return this.forwardList.size(); }
/*      */ 
/*      */     
/*      */     public List<T> subList(int fromIndex, int toIndex) {
/*  815 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  816 */       return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  821 */     public Iterator<T> iterator() { return listIterator(); }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  825 */       int start = reversePosition(index);
/*  826 */       final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
/*  827 */       return new ListIterator<T>()
/*      */         {
/*      */           boolean canRemoveOrSet;
/*      */           
/*      */           public void add(T e) {
/*  832 */             forwardIterator.add(e);
/*  833 */             forwardIterator.previous();
/*  834 */             super.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*  838 */           public boolean hasNext() { return forwardIterator.hasPrevious(); }
/*      */ 
/*      */ 
/*      */           
/*  842 */           public boolean hasPrevious() { return forwardIterator.hasNext(); }
/*      */ 
/*      */           
/*      */           public T next() {
/*  846 */             if (!super.hasNext()) {
/*  847 */               throw new NoSuchElementException();
/*      */             }
/*  849 */             super.canRemoveOrSet = true;
/*  850 */             return (T)forwardIterator.previous();
/*      */           }
/*      */ 
/*      */           
/*  854 */           public int nextIndex() { return super.this$0.reversePosition(forwardIterator.nextIndex()); }
/*      */ 
/*      */           
/*      */           public T previous() {
/*  858 */             if (!super.hasPrevious()) {
/*  859 */               throw new NoSuchElementException();
/*      */             }
/*  861 */             super.canRemoveOrSet = true;
/*  862 */             return (T)forwardIterator.next();
/*      */           }
/*      */ 
/*      */           
/*  866 */           public int previousIndex() { return super.nextIndex() - 1; }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  870 */             CollectPreconditions.checkRemove(super.canRemoveOrSet);
/*  871 */             forwardIterator.remove();
/*  872 */             super.canRemoveOrSet = false;
/*      */           }
/*      */           
/*      */           public void set(T e) {
/*  876 */             Preconditions.checkState(super.canRemoveOrSet);
/*  877 */             forwardIterator.set(e);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessReverseList<T>
/*      */     extends ReverseList<T>
/*      */     implements RandomAccess {
/*  886 */     RandomAccessReverseList(List<T> forwardList) { super(forwardList); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(List<?> list) {
/*  895 */     int hashCode = 1;
/*  896 */     for (Object o : list) {
/*  897 */       hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
/*      */       
/*  899 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/*  902 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(List<?> list, @Nullable Object object) {
/*  909 */     if (object == Preconditions.checkNotNull(list)) {
/*  910 */       return true;
/*      */     }
/*  912 */     if (!(object instanceof List)) {
/*  913 */       return false;
/*      */     }
/*      */     
/*  916 */     List<?> o = (List)object;
/*      */     
/*  918 */     return (list.size() == o.size() && Iterators.elementsEqual(list.iterator(), o.iterator()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
/*  927 */     boolean changed = false;
/*  928 */     ListIterator<E> listIterator = list.listIterator(index);
/*  929 */     for (E e : elements) {
/*  930 */       listIterator.add(e);
/*  931 */       changed = true;
/*      */     } 
/*  933 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int indexOfImpl(List<?> list, @Nullable Object element) {
/*  940 */     ListIterator<?> listIterator = list.listIterator();
/*  941 */     while (listIterator.hasNext()) {
/*  942 */       if (Objects.equal(element, listIterator.next())) {
/*  943 */         return listIterator.previousIndex();
/*      */       }
/*      */     } 
/*  946 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int lastIndexOfImpl(List<?> list, @Nullable Object element) {
/*  953 */     ListIterator<?> listIterator = list.listIterator(list.size());
/*  954 */     while (listIterator.hasPrevious()) {
/*  955 */       if (Objects.equal(element, listIterator.previous())) {
/*  956 */         return listIterator.nextIndex();
/*      */       }
/*      */     } 
/*  959 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  966 */   static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) { return (new AbstractListWrapper(list)).listIterator(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
/*      */     List<E> wrapper;
/*  975 */     if (list instanceof RandomAccess) {
/*  976 */       wrapper = new RandomAccessListWrapper<E>(list) { private static final long serialVersionUID = 0L;
/*      */           
/*  978 */           public ListIterator<E> listIterator(int index) { return this.backingList.listIterator(index); }
/*      */            }
/*      */         ;
/*      */     }
/*      */     else {
/*      */       
/*  984 */       wrapper = new AbstractListWrapper<E>(list) { private static final long serialVersionUID = 0L;
/*      */           
/*  986 */           public ListIterator<E> listIterator(int index) { return this.backingList.listIterator(index); }
/*      */            }
/*      */         ;
/*      */     } 
/*      */ 
/*      */     
/*  992 */     return wrapper.subList(fromIndex, toIndex);
/*      */   }
/*      */   
/*      */   private static class AbstractListWrapper<E>
/*      */     extends AbstractList<E> {
/*      */     final List<E> backingList;
/*      */     
/*  999 */     AbstractListWrapper(List<E> backingList) { this.backingList = (List)Preconditions.checkNotNull(backingList); }
/*      */ 
/*      */ 
/*      */     
/* 1003 */     public void add(int index, E element) { this.backingList.add(index, element); }
/*      */ 
/*      */ 
/*      */     
/* 1007 */     public boolean addAll(int index, Collection<? extends E> c) { return this.backingList.addAll(index, c); }
/*      */ 
/*      */ 
/*      */     
/* 1011 */     public E get(int index) { return (E)this.backingList.get(index); }
/*      */ 
/*      */ 
/*      */     
/* 1015 */     public E remove(int index) { return (E)this.backingList.remove(index); }
/*      */ 
/*      */ 
/*      */     
/* 1019 */     public E set(int index, E element) { return (E)this.backingList.set(index, element); }
/*      */ 
/*      */ 
/*      */     
/* 1023 */     public boolean contains(Object o) { return this.backingList.contains(o); }
/*      */ 
/*      */ 
/*      */     
/* 1027 */     public int size() { return this.backingList.size(); }
/*      */   }
/*      */   
/*      */   private static class RandomAccessListWrapper<E>
/*      */     extends AbstractListWrapper<E>
/*      */     implements RandomAccess
/*      */   {
/* 1034 */     RandomAccessListWrapper(List<E> backingList) { super(backingList); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1042 */   static <T> List<T> cast(Iterable<T> iterable) { return (List)iterable; }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Lists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */