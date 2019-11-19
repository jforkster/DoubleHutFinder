/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Iterables
/*      */ {
/*      */   public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
/*   66 */     Preconditions.checkNotNull(iterable);
/*   67 */     if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection)
/*      */     {
/*   69 */       return iterable;
/*      */     }
/*   71 */     return new UnmodifiableIterable(iterable, null);
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
/*   82 */   public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) { return (Iterable)Preconditions.checkNotNull(iterable); }
/*      */   
/*      */   private static final class UnmodifiableIterable<T>
/*      */     extends FluentIterable<T>
/*      */   {
/*      */     private final Iterable<T> iterable;
/*      */     
/*   89 */     private UnmodifiableIterable(Iterable<T> iterable) { this.iterable = iterable; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   94 */     public Iterator<T> iterator() { return Iterators.unmodifiableIterator(this.iterable.iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   99 */     public String toString() { return this.iterable.toString(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   public static int size(Iterable<?> iterable) { return (iterable instanceof Collection) ? ((Collection)iterable).size() : Iterators.size(iterable.iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
/*  118 */     if (iterable instanceof Collection) {
/*  119 */       Collection<?> collection = (Collection)iterable;
/*  120 */       return Collections2.safeContains(collection, element);
/*      */     } 
/*  122 */     return Iterators.contains(iterable.iterator(), element);
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
/*  138 */   public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) { return (removeFrom instanceof Collection) ? ((Collection)removeFrom).removeAll((Collection)Preconditions.checkNotNull(elementsToRemove)) : Iterators.removeAll(removeFrom.iterator(), elementsToRemove); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  156 */   public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) { return (removeFrom instanceof Collection) ? ((Collection)removeFrom).retainAll((Collection)Preconditions.checkNotNull(elementsToRetain)) : Iterators.retainAll(removeFrom.iterator(), elementsToRetain); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  176 */     if (removeFrom instanceof java.util.RandomAccess && removeFrom instanceof List) {
/*  177 */       return removeIfFromRandomAccessList((List)removeFrom, (Predicate)Preconditions.checkNotNull(predicate));
/*      */     }
/*      */     
/*  180 */     return Iterators.removeIf(removeFrom.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> boolean removeIfFromRandomAccessList(List<T> list, Predicate<? super T> predicate) {
/*  187 */     int from = 0;
/*  188 */     int to = 0;
/*      */     
/*  190 */     for (; from < list.size(); from++) {
/*  191 */       T element = (T)list.get(from);
/*  192 */       if (!predicate.apply(element)) {
/*  193 */         if (from > to) {
/*      */           try {
/*  195 */             list.set(to, element);
/*  196 */           } catch (UnsupportedOperationException e) {
/*  197 */             slowRemoveIfForRemainingElements(list, predicate, to, from);
/*  198 */             return true;
/*      */           } 
/*      */         }
/*  201 */         to++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  206 */     list.subList(to, list.size()).clear();
/*  207 */     return (from != to);
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
/*      */   private static <T> void slowRemoveIfForRemainingElements(List<T> list, Predicate<? super T> predicate, int to, int from) {
/*  222 */     for (int n = list.size() - 1; n > from; n--) {
/*  223 */       if (predicate.apply(list.get(n))) {
/*  224 */         list.remove(n);
/*      */       }
/*      */     } 
/*      */     
/*  228 */     for (int n = from - 1; n >= to; n--) {
/*  229 */       list.remove(n);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static <T> T removeFirstMatching(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  238 */     Preconditions.checkNotNull(predicate);
/*  239 */     Iterator<T> iterator = removeFrom.iterator();
/*  240 */     while (iterator.hasNext()) {
/*  241 */       T next = (T)iterator.next();
/*  242 */       if (predicate.apply(next)) {
/*  243 */         iterator.remove();
/*  244 */         return next;
/*      */       } 
/*      */     } 
/*  247 */     return null;
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
/*      */   public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
/*  259 */     if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
/*  260 */       Collection<?> collection1 = (Collection)iterable1;
/*  261 */       Collection<?> collection2 = (Collection)iterable2;
/*  262 */       if (collection1.size() != collection2.size()) {
/*  263 */         return false;
/*      */       }
/*      */     } 
/*  266 */     return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
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
/*  278 */   public static String toString(Iterable<?> iterable) { return Iterators.toString(iterable.iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  289 */   public static <T> T getOnlyElement(Iterable<T> iterable) { return (T)Iterators.getOnlyElement(iterable.iterator()); }
/*      */ 
/*      */ 
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
/*  302 */   public static <T> T getOnlyElement(Iterable<? extends T> iterable, @Nullable T defaultValue) { return (T)Iterators.getOnlyElement(iterable.iterator(), defaultValue); }
/*      */ 
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
/*      */   public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
/*  315 */     Collection<? extends T> collection = toCollection(iterable);
/*  316 */     T[] array = (T[])ObjectArrays.newArray(type, collection.size());
/*  317 */     return (T[])collection.toArray(array);
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
/*  328 */   static Object[] toArray(Iterable<?> iterable) { return toCollection(iterable).toArray(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  337 */   private static <E> Collection<E> toCollection(Iterable<E> iterable) { return (iterable instanceof Collection) ? (Collection)iterable : Lists.newArrayList(iterable.iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
/*  350 */     if (elementsToAdd instanceof Collection) {
/*  351 */       Collection<? extends T> c = Collections2.cast(elementsToAdd);
/*  352 */       return addTo.addAll(c);
/*      */     } 
/*  354 */     return Iterators.addAll(addTo, ((Iterable)Preconditions.checkNotNull(elementsToAdd)).iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterable<?> iterable, @Nullable Object element) {
/*  365 */     if (iterable instanceof Multiset)
/*  366 */       return ((Multiset)iterable).count(element); 
/*  367 */     if (iterable instanceof Set) {
/*  368 */       return ((Set)iterable).contains(element) ? 1 : 0;
/*      */     }
/*  370 */     return Iterators.frequency(iterable.iterator(), element);
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
/*      */   public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
/*  391 */     Preconditions.checkNotNull(iterable);
/*  392 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  395 */           return Iterators.cycle(iterable);
/*      */         }
/*      */         
/*  398 */         public String toString() { return iterable.toString() + " (cycled)"; }
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
/*      */ 
/*      */ 
/*      */   
/*  422 */   public static <T> Iterable<T> cycle(T... elements) { return cycle(Lists.newArrayList(elements)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  435 */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) { return concat(ImmutableList.of(a, b)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  449 */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) { return concat(ImmutableList.of(a, b, c)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  465 */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) { return concat(ImmutableList.of(a, b, c, d)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  479 */   public static <T> Iterable<T> concat(Iterable... inputs) { return concat(ImmutableList.copyOf(inputs)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
/*  494 */     Preconditions.checkNotNull(inputs);
/*  495 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  498 */           return Iterators.concat(Iterables.iterators(inputs));
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> Iterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> iterables) {
/*  508 */     return new TransformedIterator<Iterable<? extends T>, Iterator<? extends T>>(iterables.iterator())
/*      */       {
/*      */         Iterator<? extends T> transform(Iterable<? extends T> from)
/*      */         {
/*  512 */           return from.iterator();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
/*  539 */     Preconditions.checkNotNull(iterable);
/*  540 */     Preconditions.checkArgument((size > 0));
/*  541 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  544 */           return Iterators.partition(iterable.iterator(), size);
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
/*      */   
/*      */   public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
/*  568 */     Preconditions.checkNotNull(iterable);
/*  569 */     Preconditions.checkArgument((size > 0));
/*  570 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  573 */           return Iterators.paddedPartition(iterable.iterator(), size);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> predicate) {
/*  584 */     Preconditions.checkNotNull(unfiltered);
/*  585 */     Preconditions.checkNotNull(predicate);
/*  586 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  589 */           return Iterators.filter(unfiltered.iterator(), predicate);
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
/*      */   @GwtIncompatible("Class.isInstance")
/*      */   public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> type) {
/*  608 */     Preconditions.checkNotNull(unfiltered);
/*  609 */     Preconditions.checkNotNull(type);
/*  610 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  613 */           return Iterators.filter(unfiltered.iterator(), type);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  623 */   public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) { return Iterators.any(iterable.iterator(), predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  632 */   public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) { return Iterators.all(iterable.iterator(), predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  646 */   public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) { return (T)Iterators.find(iterable.iterator(), predicate); }
/*      */ 
/*      */ 
/*      */ 
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
/*  660 */   public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T defaultValue) { return (T)Iterators.find(iterable.iterator(), predicate, defaultValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  675 */   public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) { return Iterators.tryFind(iterable.iterator(), predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  691 */   public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) { return Iterators.indexOf(iterable.iterator(), predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
/*  708 */     Preconditions.checkNotNull(fromIterable);
/*  709 */     Preconditions.checkNotNull(function);
/*  710 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  713 */           return Iterators.transform(fromIterable.iterator(), function);
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
/*      */   public static <T> T get(Iterable<T> iterable, int position) {
/*  727 */     Preconditions.checkNotNull(iterable);
/*  728 */     return (T)((iterable instanceof List) ? ((List)iterable).get(position) : Iterators.get(iterable.iterator(), position));
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
/*      */   @Nullable
/*      */   public static <T> T get(Iterable<? extends T> iterable, int position, @Nullable T defaultValue) {
/*  748 */     Preconditions.checkNotNull(iterable);
/*  749 */     Iterators.checkNonnegative(position);
/*  750 */     if (iterable instanceof List) {
/*  751 */       List<? extends T> list = Lists.cast(iterable);
/*  752 */       return (T)((position < list.size()) ? list.get(position) : defaultValue);
/*      */     } 
/*  754 */     Iterator<? extends T> iterator = iterable.iterator();
/*  755 */     Iterators.advance(iterator, position);
/*  756 */     return (T)Iterators.getNext(iterator, defaultValue);
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
/*      */   @Nullable
/*  775 */   public static <T> T getFirst(Iterable<? extends T> iterable, @Nullable T defaultValue) { return (T)Iterators.getNext(iterable.iterator(), defaultValue); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getLast(Iterable<T> iterable) {
/*  786 */     if (iterable instanceof List) {
/*  787 */       List<T> list = (List)iterable;
/*  788 */       if (list.isEmpty()) {
/*  789 */         throw new NoSuchElementException();
/*      */       }
/*  791 */       return (T)getLastInNonemptyList(list);
/*      */     } 
/*      */     
/*  794 */     return (T)Iterators.getLast(iterable.iterator());
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
/*      */   public static <T> T getLast(Iterable<? extends T> iterable, @Nullable T defaultValue) {
/*  807 */     if (iterable instanceof Collection) {
/*  808 */       Collection<? extends T> c = Collections2.cast(iterable);
/*  809 */       if (c.isEmpty())
/*  810 */         return defaultValue; 
/*  811 */       if (iterable instanceof List) {
/*  812 */         return (T)getLastInNonemptyList(Lists.cast(iterable));
/*      */       }
/*      */     } 
/*      */     
/*  816 */     return (T)Iterators.getLast(iterable.iterator(), defaultValue);
/*      */   }
/*      */ 
/*      */   
/*  820 */   private static <T> T getLastInNonemptyList(List<T> list) { return (T)list.get(list.size() - 1); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
/*  845 */     Preconditions.checkNotNull(iterable);
/*  846 */     Preconditions.checkArgument((numberToSkip >= 0), "number to skip cannot be negative");
/*      */     
/*  848 */     if (iterable instanceof List) {
/*  849 */       final List<T> list = (List)iterable;
/*  850 */       return new FluentIterable<T>()
/*      */         {
/*      */           public Iterator<T> iterator()
/*      */           {
/*  854 */             int toSkip = Math.min(list.size(), numberToSkip);
/*  855 */             return list.subList(toSkip, list.size()).iterator();
/*      */           }
/*      */         };
/*      */     } 
/*      */     
/*  860 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  863 */           final Iterator<T> iterator = iterable.iterator();
/*      */           
/*  865 */           Iterators.advance(iterator, numberToSkip);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  872 */           return new Iterator<T>()
/*      */             {
/*      */               boolean atStart = true;
/*      */ 
/*      */               
/*  877 */               public boolean hasNext() { return iterator.hasNext(); }
/*      */ 
/*      */ 
/*      */               
/*      */               public T next() {
/*  882 */                 T result = (T)iterator.next();
/*  883 */                 this.atStart = false;
/*  884 */                 return result;
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/*  889 */                 CollectPreconditions.checkRemove(!this.atStart);
/*  890 */                 iterator.remove();
/*      */               }
/*      */             };
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
/*      */   public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
/*  911 */     Preconditions.checkNotNull(iterable);
/*  912 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  913 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  916 */           return Iterators.limit(iterable.iterator(), limitSize);
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
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
/*  941 */     if (iterable instanceof Queue) {
/*  942 */       return new FluentIterable<T>()
/*      */         {
/*      */           public Iterator<T> iterator() {
/*  945 */             return new Iterables.ConsumingQueueIterator((Queue)iterable, null);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/*  950 */             return "Iterables.consumingIterable(...)";
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*  955 */     Preconditions.checkNotNull(iterable);
/*      */     
/*  957 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  960 */           return Iterators.consumingIterator(iterable.iterator());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  965 */         public String toString() { return "Iterables.consumingIterable(...)"; }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class ConsumingQueueIterator<T>
/*      */     extends AbstractIterator<T>
/*      */   {
/*      */     private final Queue<T> queue;
/*      */     
/*  974 */     private ConsumingQueueIterator(Queue<T> queue) { this.queue = queue; }
/*      */ 
/*      */     
/*      */     public T computeNext() {
/*      */       try {
/*  979 */         return (T)this.queue.remove();
/*  980 */       } catch (NoSuchElementException e) {
/*  981 */         return (T)endOfData();
/*      */       } 
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
/*      */   public static boolean isEmpty(Iterable<?> iterable) {
/*  998 */     if (iterable instanceof Collection) {
/*  999 */       return ((Collection)iterable).isEmpty();
/*      */     }
/* 1001 */     return !iterable.iterator().hasNext();
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
/*      */   @Beta
/*      */   public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
/* 1020 */     Preconditions.checkNotNull(iterables, "iterables");
/* 1021 */     Preconditions.checkNotNull(comparator, "comparator");
/* 1022 */     Iterable<T> iterable = new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/* 1025 */           return Iterators.mergeSorted(Iterables.transform(iterables, Iterables.toIterator()), comparator);
/*      */         }
/*      */       };
/*      */ 
/*      */     
/* 1030 */     return new UnmodifiableIterable(iterable, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
/* 1037 */     return new Function<Iterable<? extends T>, Iterator<? extends T>>()
/*      */       {
/*      */         public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
/* 1040 */           return iterable.iterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Iterables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */