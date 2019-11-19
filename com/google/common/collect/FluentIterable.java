/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import javax.annotation.CheckReturnValue;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class FluentIterable<E>
/*     */   extends Object
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Iterable<E> iterable;
/*     */   
/*  78 */   protected FluentIterable() { this.iterable = this; }
/*     */ 
/*     */ 
/*     */   
/*  82 */   FluentIterable(Iterable<E> iterable) { this.iterable = (Iterable)Preconditions.checkNotNull(iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
/*  90 */     return (iterable instanceof FluentIterable) ? (FluentIterable)iterable : new FluentIterable<E>(iterable)
/*     */       {
/*     */         public Iterator<E> iterator()
/*     */         {
/*  94 */           return iterable.iterator();
/*     */         }
/*     */       };
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
/*     */   @Deprecated
/* 109 */   public static <E> FluentIterable<E> from(FluentIterable<E> iterable) { return (FluentIterable)Preconditions.checkNotNull(iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public String toString() { return Iterables.toString(this.iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public final int size() { return Iterables.size(this.iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public final boolean contains(@Nullable Object element) { return Iterables.contains(this.iterable, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 151 */   public final FluentIterable<E> cycle() { return from(Iterables.cycle(this.iterable)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 160 */   public final FluentIterable<E> filter(Predicate<? super E> predicate) { return from(Iterables.filter(this.iterable, predicate)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @GwtIncompatible("Class.isInstance")
/* 171 */   public final <T> FluentIterable<T> filter(Class<T> type) { return from(Iterables.filter(this.iterable, type)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public final boolean anyMatch(Predicate<? super E> predicate) { return Iterables.any(this.iterable, predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public final boolean allMatch(Predicate<? super E> predicate) { return Iterables.all(this.iterable, predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public final Optional<E> firstMatch(Predicate<? super E> predicate) { return Iterables.tryFind(this.iterable, predicate); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   public final <T> FluentIterable<T> transform(Function<? super E, T> function) { return from(Iterables.transform(this.iterable, function)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) { return from(Iterables.concat(transform(function))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Optional<E> first() {
/* 236 */     Iterator<E> iterator = this.iterable.iterator();
/* 237 */     return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
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
/*     */   public final Optional<E> last() {
/*     */     E current;
/* 253 */     if (this.iterable instanceof List) {
/* 254 */       List<E> list = (List)this.iterable;
/* 255 */       if (list.isEmpty()) {
/* 256 */         return Optional.absent();
/*     */       }
/* 258 */       return Optional.of(list.get(list.size() - 1));
/*     */     } 
/* 260 */     Iterator<E> iterator = this.iterable.iterator();
/* 261 */     if (!iterator.hasNext()) {
/* 262 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     if (this.iterable instanceof SortedSet) {
/* 271 */       SortedSet<E> sortedSet = (SortedSet)this.iterable;
/* 272 */       return Optional.of(sortedSet.last());
/*     */     } 
/*     */     
/*     */     do {
/* 276 */       current = (E)iterator.next();
/* 277 */     } while (iterator.hasNext());
/* 278 */     return Optional.of(current);
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
/*     */   @CheckReturnValue
/* 302 */   public final FluentIterable<E> skip(int numberToSkip) { return from(Iterables.skip(this.iterable, numberToSkip)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 317 */   public final FluentIterable<E> limit(int size) { return from(Iterables.limit(this.iterable, size)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 324 */   public final boolean isEmpty() { return !this.iterable.iterator().hasNext(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 334 */   public final ImmutableList<E> toList() { return ImmutableList.copyOf(this.iterable); }
/*     */ 
/*     */ 
/*     */ 
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
/* 348 */   public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) { return Ordering.from(comparator).immutableSortedCopy(this.iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 358 */   public final ImmutableSet<E> toSet() { return ImmutableSet.copyOf(this.iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 372 */   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) { return ImmutableSortedSet.copyOf(comparator, this.iterable); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 386 */   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction) { return Maps.toMap(this.iterable, valueFunction); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 408 */   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction) { return Multimaps.index(this.iterable, keyFunction); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 424 */   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction) { return Maps.uniqueIndex(this.iterable, keyFunction); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Array.newArray(Class, int)")
/* 436 */   public final E[] toArray(Class<E> type) { return (E[])Iterables.toArray(this.iterable, type); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <C extends java.util.Collection<? super E>> C copyInto(C collection) {
/* 448 */     Preconditions.checkNotNull(collection);
/* 449 */     if (this.iterable instanceof java.util.Collection) {
/* 450 */       collection.addAll(Collections2.cast(this.iterable));
/*     */     } else {
/* 452 */       for (E item : this.iterable) {
/* 453 */         collection.add(item);
/*     */       }
/*     */     } 
/* 456 */     return collection;
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
/* 468 */   public final E get(int position) { return (E)Iterables.get(this.iterable, position); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FromIterableFunction<E>
/*     */     extends Object
/*     */     implements Function<Iterable<E>, FluentIterable<E>>
/*     */   {
/* 478 */     public FluentIterable<E> apply(Iterable<E> fromObject) { return FluentIterable.from(fromObject); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/FluentIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */