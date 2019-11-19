/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Iterators
/*      */ {
/*   72 */   static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>()
/*      */     {
/*      */       public boolean hasNext()
/*      */       {
/*   76 */         return false;
/*      */       }
/*      */ 
/*      */       
/*   80 */       public Object next() { throw new NoSuchElementException(); }
/*      */ 
/*      */ 
/*      */       
/*   84 */       public boolean hasPrevious() { return false; }
/*      */ 
/*      */ 
/*      */       
/*   88 */       public Object previous() { throw new NoSuchElementException(); }
/*      */ 
/*      */ 
/*      */       
/*   92 */       public int nextIndex() { return 0; }
/*      */ 
/*      */ 
/*      */       
/*   96 */       public int previousIndex() { return -1; }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  107 */   public static <T> UnmodifiableIterator<T> emptyIterator() { return emptyListIterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  119 */   static <T> UnmodifiableListIterator<T> emptyListIterator() { return EMPTY_LIST_ITERATOR; }
/*      */ 
/*      */   
/*  122 */   private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator<Object>()
/*      */     {
/*      */       public boolean hasNext() {
/*  125 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  129 */       public Object next() { throw new NoSuchElementException(); }
/*      */ 
/*      */ 
/*      */       
/*  133 */       public void remove() { CollectPreconditions.checkRemove(false); }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  146 */   static <T> Iterator<T> emptyModifiableIterator() { return EMPTY_MODIFIABLE_ITERATOR; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> iterator) {
/*  152 */     Preconditions.checkNotNull(iterator);
/*  153 */     if (iterator instanceof UnmodifiableIterator) {
/*  154 */       return (UnmodifiableIterator)iterator;
/*      */     }
/*  156 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  159 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*  163 */         public T next() { return (T)iterator.next(); }
/*      */       };
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
/*      */   @Deprecated
/*  176 */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) { return (UnmodifiableIterator)Preconditions.checkNotNull(iterator); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterator<?> iterator) {
/*  185 */     int count = 0;
/*  186 */     while (iterator.hasNext()) {
/*  187 */       iterator.next();
/*  188 */       count++;
/*      */     } 
/*  190 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  197 */   public static boolean contains(Iterator<?> iterator, @Nullable Object element) { return any(iterator, Predicates.equalTo(element)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  211 */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) { return removeIf(removeFrom, Predicates.in(elementsToRemove)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
/*  227 */     Preconditions.checkNotNull(predicate);
/*  228 */     boolean modified = false;
/*  229 */     while (removeFrom.hasNext()) {
/*  230 */       if (predicate.apply(removeFrom.next())) {
/*  231 */         removeFrom.remove();
/*  232 */         modified = true;
/*      */       } 
/*      */     } 
/*  235 */     return modified;
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
/*  249 */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) { return removeIf(removeFrom, Predicates.not(Predicates.in(elementsToRetain))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
/*  264 */     while (iterator1.hasNext()) {
/*  265 */       if (!iterator2.hasNext()) {
/*  266 */         return false;
/*      */       }
/*  268 */       Object o1 = iterator1.next();
/*  269 */       Object o2 = iterator2.next();
/*  270 */       if (!Objects.equal(o1, o2)) {
/*  271 */         return false;
/*      */       }
/*      */     } 
/*  274 */     return !iterator2.hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  283 */   public static String toString(Iterator<?> iterator) { return Collections2.STANDARD_JOINER.appendTo((new StringBuilder()).append('['), iterator).append(']').toString(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator) {
/*  297 */     T first = (T)iterator.next();
/*  298 */     if (!iterator.hasNext()) {
/*  299 */       return first;
/*      */     }
/*      */     
/*  302 */     StringBuilder sb = new StringBuilder();
/*  303 */     sb.append("expected one element but was: <" + first);
/*  304 */     for (int i = 0; i < 4 && iterator.hasNext(); i++) {
/*  305 */       sb.append(", " + iterator.next());
/*      */     }
/*  307 */     if (iterator.hasNext()) {
/*  308 */       sb.append(", ...");
/*      */     }
/*  310 */     sb.append('>');
/*      */     
/*  312 */     throw new IllegalArgumentException(sb.toString());
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
/*      */   @Nullable
/*  324 */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue) { return (T)(iterator.hasNext() ? getOnlyElement(iterator) : defaultValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("Array.newInstance(Class, int)")
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
/*  339 */     List<T> list = Lists.newArrayList(iterator);
/*  340 */     return (T[])Iterables.toArray(list, type);
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
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
/*  353 */     Preconditions.checkNotNull(addTo);
/*  354 */     Preconditions.checkNotNull(iterator);
/*  355 */     boolean wasModified = false;
/*  356 */     while (iterator.hasNext()) {
/*  357 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  359 */     return wasModified;
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
/*  370 */   public static int frequency(Iterator<?> iterator, @Nullable Object element) { return size(filter(iterator, Predicates.equalTo(element))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
/*  388 */     Preconditions.checkNotNull(iterable);
/*  389 */     return new Iterator<T>() {
/*  390 */         Iterator<T> iterator = Iterators.emptyIterator();
/*      */         
/*      */         Iterator<T> removeFrom;
/*      */         
/*      */         public boolean hasNext() {
/*  395 */           if (!this.iterator.hasNext()) {
/*  396 */             this.iterator = iterable.iterator();
/*      */           }
/*  398 */           return this.iterator.hasNext();
/*      */         }
/*      */         
/*      */         public T next() {
/*  402 */           if (!hasNext()) {
/*  403 */             throw new NoSuchElementException();
/*      */           }
/*  405 */           this.removeFrom = this.iterator;
/*  406 */           return (T)this.iterator.next();
/*      */         }
/*      */         
/*      */         public void remove() {
/*  410 */           CollectPreconditions.checkRemove((this.removeFrom != null));
/*  411 */           this.removeFrom.remove();
/*  412 */           this.removeFrom = null;
/*      */         }
/*      */       };
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
/*  431 */   public static <T> Iterator<T> cycle(T... elements) { return cycle(Lists.newArrayList(elements)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  449 */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) { return concat(ImmutableList.of(a, b).iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  468 */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) { return concat(ImmutableList.of(a, b, c).iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  488 */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) { return concat(ImmutableList.of(a, b, c, d).iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  507 */   public static <T> Iterator<T> concat(Iterator... inputs) { return concat(ImmutableList.copyOf(inputs).iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
/*  526 */     Preconditions.checkNotNull(inputs);
/*  527 */     return new Iterator<T>() {
/*  528 */         Iterator<? extends T> current = Iterators.emptyIterator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<? extends T> removeFrom;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*      */           boolean currentHasNext;
/*  542 */           while (!(currentHasNext = ((Iterator)Preconditions.checkNotNull(this.current)).hasNext()) && inputs.hasNext()) {
/*  543 */             this.current = (Iterator)inputs.next();
/*      */           }
/*  545 */           return currentHasNext;
/*      */         }
/*      */         
/*      */         public T next() {
/*  549 */           if (!hasNext()) {
/*  550 */             throw new NoSuchElementException();
/*      */           }
/*  552 */           this.removeFrom = this.current;
/*  553 */           return (T)this.current.next();
/*      */         }
/*      */         
/*      */         public void remove() {
/*  557 */           CollectPreconditions.checkRemove((this.removeFrom != null));
/*  558 */           this.removeFrom.remove();
/*  559 */           this.removeFrom = null;
/*      */         }
/*      */       };
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
/*  581 */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) { return partitionImpl(iterator, size, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  602 */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) { return partitionImpl(iterator, size, true); }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
/*  607 */     Preconditions.checkNotNull(iterator);
/*  608 */     Preconditions.checkArgument((size > 0));
/*  609 */     return new UnmodifiableIterator<List<T>>()
/*      */       {
/*      */         public boolean hasNext() {
/*  612 */           return iterator.hasNext();
/*      */         }
/*      */         
/*      */         public List<T> next() {
/*  616 */           if (!hasNext()) {
/*  617 */             throw new NoSuchElementException();
/*      */           }
/*  619 */           Object[] array = new Object[size];
/*  620 */           int count = 0;
/*  621 */           for (; count < size && iterator.hasNext(); count++) {
/*  622 */             array[count] = iterator.next();
/*      */           }
/*  624 */           for (int i = count; i < size; i++) {
/*  625 */             array[i] = null;
/*      */           }
/*      */ 
/*      */           
/*  629 */           List<T> list = Collections.unmodifiableList(Arrays.asList(array));
/*      */           
/*  631 */           return (pad || count == size) ? list : list.subList(0, count);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> predicate) {
/*  641 */     Preconditions.checkNotNull(unfiltered);
/*  642 */     Preconditions.checkNotNull(predicate);
/*  643 */     return new AbstractIterator<T>() {
/*      */         protected T computeNext() {
/*  645 */           while (unfiltered.hasNext()) {
/*  646 */             T element = (T)unfiltered.next();
/*  647 */             if (predicate.apply(element)) {
/*  648 */               return element;
/*      */             }
/*      */           } 
/*  651 */           return (T)endOfData();
/*      */         }
/*      */       };
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
/*      */   @GwtIncompatible("Class.isInstance")
/*  670 */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> type) { return filter(unfiltered, Predicates.instanceOf(type)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  679 */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) { return (indexOf(iterator, predicate) != -1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  689 */     Preconditions.checkNotNull(predicate);
/*  690 */     while (iterator.hasNext()) {
/*  691 */       T element = (T)iterator.next();
/*  692 */       if (!predicate.apply(element)) {
/*  693 */         return false;
/*      */       }
/*      */     } 
/*  696 */     return true;
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
/*  712 */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) { return (T)filter(iterator, predicate).next(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*  728 */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue) { return (T)getNext(filter(iterator, predicate), defaultValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  746 */     UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
/*  747 */     return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
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
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  770 */     Preconditions.checkNotNull(predicate, "predicate");
/*  771 */     for (int i = 0; iterator.hasNext(); i++) {
/*  772 */       T current = (T)iterator.next();
/*  773 */       if (predicate.apply(current)) {
/*  774 */         return i;
/*      */       }
/*      */     } 
/*  777 */     return -1;
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
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
/*  790 */     Preconditions.checkNotNull(function);
/*  791 */     return new TransformedIterator<F, T>(fromIterator)
/*      */       {
/*      */         T transform(F from) {
/*  794 */           return (T)function.apply(from);
/*      */         }
/*      */       };
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
/*      */   public static <T> T get(Iterator<T> iterator, int position) {
/*  810 */     checkNonnegative(position);
/*  811 */     int skipped = advance(iterator, position);
/*  812 */     if (!iterator.hasNext()) {
/*  813 */       throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
/*      */     }
/*      */ 
/*      */     
/*  817 */     return (T)iterator.next();
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  821 */     if (position < 0) {
/*  822 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
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
/*      */   @Nullable
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue) {
/*  844 */     checkNonnegative(position);
/*  845 */     advance(iterator, position);
/*  846 */     return (T)getNext(iterator, defaultValue);
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
/*      */   @Nullable
/*  860 */   public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue) { return (T)(iterator.hasNext() ? iterator.next() : defaultValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getLast(Iterator<T> iterator) {
/*      */     T current;
/*      */     do {
/*  871 */       current = (T)iterator.next();
/*  872 */     } while (iterator.hasNext());
/*  873 */     return current;
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
/*      */   @Nullable
/*  888 */   public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue) { return (T)(iterator.hasNext() ? getLast(iterator) : defaultValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance) {
/*  899 */     Preconditions.checkNotNull(iterator);
/*  900 */     Preconditions.checkArgument((numberToAdvance >= 0), "numberToAdvance must be nonnegative");
/*      */     
/*      */     int i;
/*  903 */     for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
/*  904 */       iterator.next();
/*      */     }
/*  906 */     return i;
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
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
/*  923 */     Preconditions.checkNotNull(iterator);
/*  924 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  925 */     return new Iterator<T>()
/*      */       {
/*      */         private int count;
/*      */ 
/*      */         
/*  930 */         public boolean hasNext() { return (this.count < limitSize && iterator.hasNext()); }
/*      */ 
/*      */ 
/*      */         
/*      */         public T next() {
/*  935 */           if (!hasNext()) {
/*  936 */             throw new NoSuchElementException();
/*      */           }
/*  938 */           this.count++;
/*  939 */           return (T)iterator.next();
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  944 */         public void remove() { iterator.remove(); }
/*      */       };
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
/*      */   public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
/*  963 */     Preconditions.checkNotNull(iterator);
/*  964 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  967 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  972 */           T next = (T)iterator.next();
/*  973 */           iterator.remove();
/*  974 */           return next;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  979 */         public String toString() { return "Iterators.consumingIterator(...)"; }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static <T> T pollNext(Iterator<T> iterator) {
/*  990 */     if (iterator.hasNext()) {
/*  991 */       T result = (T)iterator.next();
/*  992 */       iterator.remove();
/*  993 */       return result;
/*      */     } 
/*  995 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void clear(Iterator<?> iterator) {
/* 1005 */     Preconditions.checkNotNull(iterator);
/* 1006 */     while (iterator.hasNext()) {
/* 1007 */       iterator.next();
/* 1008 */       iterator.remove();
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
/* 1026 */   public static <T> UnmodifiableIterator<T> forArray(T... array) { return forArray(array, 0, array.length, 0); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, int length, int index) {
/* 1038 */     Preconditions.checkArgument((length >= 0));
/* 1039 */     int end = offset + length;
/*      */ 
/*      */     
/* 1042 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/* 1043 */     Preconditions.checkPositionIndex(index, length);
/* 1044 */     if (length == 0) {
/* 1045 */       return emptyListIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1053 */     return new AbstractIndexedListIterator<T>(length, index)
/*      */       {
/* 1055 */         protected T get(int index) { return (T)array[offset + index]; }
/*      */       };
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
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
/* 1068 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         boolean done;
/*      */         
/* 1072 */         public boolean hasNext() { return !this.done; }
/*      */ 
/*      */         
/*      */         public T next() {
/* 1076 */           if (this.done) {
/* 1077 */             throw new NoSuchElementException();
/*      */           }
/* 1079 */           this.done = true;
/* 1080 */           return (T)value;
/*      */         }
/*      */       };
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
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
/* 1095 */     Preconditions.checkNotNull(enumeration);
/* 1096 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1099 */           return enumeration.hasMoreElements();
/*      */         }
/*      */ 
/*      */         
/* 1103 */         public T next() { return (T)enumeration.nextElement(); }
/*      */       };
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
/*      */   public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
/* 1116 */     Preconditions.checkNotNull(iterator);
/* 1117 */     return new Enumeration<T>()
/*      */       {
/*      */         public boolean hasMoreElements() {
/* 1120 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/* 1124 */         public T nextElement() { return (T)iterator.next(); }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static class PeekingImpl<E>
/*      */     extends Object
/*      */     implements PeekingIterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> iterator;
/*      */     
/*      */     private boolean hasPeeked;
/*      */     
/*      */     private E peekedElement;
/*      */     
/* 1139 */     public PeekingImpl(Iterator<? extends E> iterator) { this.iterator = (Iterator)Preconditions.checkNotNull(iterator); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1144 */     public boolean hasNext() { return (this.hasPeeked || this.iterator.hasNext()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public E next() {
/* 1149 */       if (!this.hasPeeked) {
/* 1150 */         return (E)this.iterator.next();
/*      */       }
/* 1152 */       E result = (E)this.peekedElement;
/* 1153 */       this.hasPeeked = false;
/* 1154 */       this.peekedElement = null;
/* 1155 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1160 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1161 */       this.iterator.remove();
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1166 */       if (!this.hasPeeked) {
/* 1167 */         this.peekedElement = this.iterator.next();
/* 1168 */         this.hasPeeked = true;
/*      */       } 
/* 1170 */       return (E)this.peekedElement;
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
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
/* 1214 */     if (iterator instanceof PeekingImpl)
/*      */     {
/*      */ 
/*      */       
/* 1218 */       return (PeekingImpl)iterator;
/*      */     }
/*      */     
/* 1221 */     return new PeekingImpl(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1232 */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) { return (PeekingIterator)Preconditions.checkNotNull(iterator); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
/* 1252 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1253 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1255 */     return new MergingIterator(iterators, comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
/* 1274 */       Comparator<PeekingIterator<T>> heapComparator = new Comparator<PeekingIterator<T>>()
/*      */         {
/*      */           public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
/*      */           {
/* 1278 */             return itemComparator.compare(o1.peek(), o2.peek());
/*      */           }
/*      */         };
/*      */       
/* 1282 */       this.queue = new PriorityQueue(2, heapComparator);
/*      */       
/* 1284 */       for (Iterator<? extends T> iterator : iterators) {
/* 1285 */         if (iterator.hasNext()) {
/* 1286 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1293 */     public boolean hasNext() { return !this.queue.isEmpty(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public T next() {
/* 1298 */       PeekingIterator<T> nextIter = (PeekingIterator)this.queue.remove();
/* 1299 */       T next = (T)nextIter.next();
/* 1300 */       if (nextIter.hasNext()) {
/* 1301 */         this.queue.add(nextIter);
/*      */       }
/* 1303 */       return next;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1311 */   static <T> ListIterator<T> cast(Iterator<T> iterator) { return (ListIterator)iterator; }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Iterators.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */