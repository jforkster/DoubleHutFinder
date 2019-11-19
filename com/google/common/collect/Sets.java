/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Sets
/*      */ {
/*      */   static abstract class ImprovedAbstractSet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*   74 */     public boolean removeAll(Collection<?> c) { return Sets.removeAllImpl(this, c); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   79 */     public boolean retainAll(Collection<?> c) { return super.retainAll((Collection)Preconditions.checkNotNull(c)); }
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
/*      */   @GwtCompatible(serializable = true)
/*   98 */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) { return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
/*  116 */     if (elements instanceof ImmutableEnumSet)
/*  117 */       return (ImmutableEnumSet)elements; 
/*  118 */     if (elements instanceof Collection) {
/*  119 */       Collection<E> collection = (Collection)elements;
/*  120 */       if (collection.isEmpty()) {
/*  121 */         return ImmutableSet.of();
/*      */       }
/*  123 */       return ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
/*      */     } 
/*      */     
/*  126 */     Iterator<E> itr = elements.iterator();
/*  127 */     if (itr.hasNext()) {
/*  128 */       EnumSet<E> enumSet = EnumSet.of((Enum)itr.next());
/*  129 */       Iterators.addAll(enumSet, itr);
/*  130 */       return ImmutableEnumSet.asImmutable(enumSet);
/*      */     } 
/*  132 */     return ImmutableSet.of();
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
/*      */   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
/*  145 */     EnumSet<E> set = EnumSet.noneOf(elementType);
/*  146 */     Iterables.addAll(set, iterable);
/*  147 */     return set;
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
/*  164 */   public static <E> HashSet<E> newHashSet() { return new HashSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> HashSet<E> newHashSet(E... elements) {
/*  182 */     HashSet<E> set = newHashSetWithExpectedSize(elements.length);
/*  183 */     Collections.addAll(set, elements);
/*  184 */     return set;
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
/*  201 */   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) { return new HashSet(Maps.capacity(expectedSize)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  218 */   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) { return (elements instanceof Collection) ? new HashSet(Collections2.cast(elements)) : newHashSet(elements.iterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
/*  237 */     HashSet<E> set = newHashSet();
/*  238 */     Iterators.addAll(set, elements);
/*  239 */     return set;
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
/*  254 */   public static <E> Set<E> newConcurrentHashSet() { return newSetFromMap(new ConcurrentHashMap()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
/*  273 */     Set<E> set = newConcurrentHashSet();
/*  274 */     Iterables.addAll(set, elements);
/*  275 */     return set;
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
/*  289 */   public static <E> LinkedHashSet<E> newLinkedHashSet() { return new LinkedHashSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  308 */   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) { return new LinkedHashSet(Maps.capacity(expectedSize)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
/*  324 */     if (elements instanceof Collection) {
/*  325 */       return new LinkedHashSet(Collections2.cast(elements));
/*      */     }
/*  327 */     LinkedHashSet<E> set = newLinkedHashSet();
/*  328 */     Iterables.addAll(set, elements);
/*  329 */     return set;
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
/*  344 */   public static <E extends Comparable> TreeSet<E> newTreeSet() { return new TreeSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
/*  364 */     TreeSet<E> set = newTreeSet();
/*  365 */     Iterables.addAll(set, elements);
/*  366 */     return set;
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
/*  381 */   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) { return new TreeSet((Comparator)Preconditions.checkNotNull(comparator)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  395 */   public static <E> Set<E> newIdentityHashSet() { return newSetFromMap(Maps.newIdentityHashMap()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("CopyOnWriteArraySet")
/*  409 */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() { return new CopyOnWriteArraySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("CopyOnWriteArraySet")
/*      */   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
/*  424 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.cast(elements) : Lists.newArrayList(elements);
/*      */ 
/*      */     
/*  427 */     return new CopyOnWriteArraySet(elementsCollection);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
/*  447 */     if (collection instanceof EnumSet) {
/*  448 */       return EnumSet.complementOf((EnumSet)collection);
/*      */     }
/*  450 */     Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
/*      */     
/*  452 */     Class<E> type = ((Enum)collection.iterator().next()).getDeclaringClass();
/*  453 */     return makeComplementByHand(collection, type);
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
/*      */   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
/*  470 */     Preconditions.checkNotNull(collection);
/*  471 */     return (collection instanceof EnumSet) ? EnumSet.complementOf((EnumSet)collection) : makeComplementByHand(collection, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
/*  478 */     EnumSet<E> result = EnumSet.allOf(type);
/*  479 */     result.removeAll(collection);
/*  480 */     return result;
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
/*  515 */   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) { return Platform.newSetFromMap(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static abstract class SetView<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SetView() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  541 */     public ImmutableSet<E> immutableCopy() { return ImmutableSet.copyOf(this); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <S extends Set<E>> S copyInto(S set) {
/*  554 */       set.addAll(this);
/*  555 */       return set;
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
/*      */   public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
/*  581 */     Preconditions.checkNotNull(set1, "set1");
/*  582 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  584 */     final Set<? extends E> set2minus1 = difference(set2, set1);
/*      */     
/*  586 */     return new SetView<E>()
/*      */       {
/*  588 */         public int size() { return set1.size() + set2minus1.size(); }
/*      */ 
/*      */         
/*  591 */         public boolean isEmpty() { return (set1.isEmpty() && set2.isEmpty()); }
/*      */ 
/*      */         
/*  594 */         public Iterator<E> iterator() { return Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator())); }
/*      */ 
/*      */ 
/*      */         
/*  598 */         public boolean contains(Object object) { return (set1.contains(object) || set2.contains(object)); }
/*      */         
/*      */         public <S extends Set<E>> S copyInto(S set) {
/*  601 */           set.addAll(set1);
/*  602 */           set.addAll(set2);
/*  603 */           return set;
/*      */         }
/*      */         
/*  606 */         public ImmutableSet<E> immutableCopy() { return (new ImmutableSet.Builder()).addAll(set1).addAll(set2).build(); }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
/*  640 */     Preconditions.checkNotNull(set1, "set1");
/*  641 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  643 */     final Predicate<Object> inSet2 = Predicates.in(set2);
/*  644 */     return new SetView<E>()
/*      */       {
/*  646 */         public Iterator<E> iterator() { return Iterators.filter(set1.iterator(), inSet2); }
/*      */ 
/*      */         
/*  649 */         public int size() { return Iterators.size(iterator()); }
/*      */ 
/*      */         
/*  652 */         public boolean isEmpty() { return !iterator().hasNext(); }
/*      */ 
/*      */         
/*  655 */         public boolean contains(Object object) { return (set1.contains(object) && set2.contains(object)); }
/*      */ 
/*      */         
/*  658 */         public boolean containsAll(Collection<?> collection) { return (set1.containsAll(collection) && set2.containsAll(collection)); }
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
/*      */   public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
/*  677 */     Preconditions.checkNotNull(set1, "set1");
/*  678 */     Preconditions.checkNotNull(set2, "set2");
/*      */     
/*  680 */     final Predicate<Object> notInSet2 = Predicates.not(Predicates.in(set2));
/*  681 */     return new SetView<E>()
/*      */       {
/*  683 */         public Iterator<E> iterator() { return Iterators.filter(set1.iterator(), notInSet2); }
/*      */ 
/*      */         
/*  686 */         public int size() { return Iterators.size(iterator()); }
/*      */ 
/*      */         
/*  689 */         public boolean isEmpty() { return set2.containsAll(set1); }
/*      */ 
/*      */         
/*  692 */         public boolean contains(Object element) { return (set1.contains(element) && !set2.contains(element)); }
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
/*      */   public static <E> SetView<E> symmetricDifference(Set<? extends E> set1, Set<? extends E> set2) {
/*  711 */     Preconditions.checkNotNull(set1, "set1");
/*  712 */     Preconditions.checkNotNull(set2, "set2");
/*      */ 
/*      */     
/*  715 */     return difference(union(set1, set2), intersection(set1, set2));
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
/*      */   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
/*  747 */     if (unfiltered instanceof SortedSet) {
/*  748 */       return filter((SortedSet)unfiltered, predicate);
/*      */     }
/*  750 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/*  753 */       FilteredSet<E> filtered = (FilteredSet)unfiltered;
/*  754 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  756 */       return new FilteredSet((Set)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */ 
/*      */     
/*  760 */     return new FilteredSet((Set)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   private static class FilteredSet<E>
/*      */     extends Collections2.FilteredCollection<E>
/*      */     implements Set<E>
/*      */   {
/*  767 */     FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) { super(unfiltered, predicate); }
/*      */ 
/*      */ 
/*      */     
/*  771 */     public boolean equals(@Nullable Object object) { return Sets.equalsImpl(this, object); }
/*      */ 
/*      */ 
/*      */     
/*  775 */     public int hashCode() { return Sets.hashCodeImpl(this); }
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
/*  810 */   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) { return Platform.setsFilterSortedSet(unfiltered, predicate); }
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> SortedSet<E> filterSortedIgnoreNavigable(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
/*  815 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/*  818 */       FilteredSet<E> filtered = (FilteredSet)unfiltered;
/*  819 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  821 */       return new FilteredSortedSet((SortedSet)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */ 
/*      */     
/*  825 */     return new FilteredSortedSet((SortedSet)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredSortedSet<E>
/*      */     extends FilteredSet<E>
/*      */     implements SortedSet<E>
/*      */   {
/*  833 */     FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) { super(unfiltered, predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  838 */     public Comparator<? super E> comparator() { return ((SortedSet)this.unfiltered).comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  843 */     public SortedSet<E> subSet(E fromElement, E toElement) { return new FilteredSortedSet(((SortedSet)this.unfiltered).subSet(fromElement, toElement), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  849 */     public SortedSet<E> headSet(E toElement) { return new FilteredSortedSet(((SortedSet)this.unfiltered).headSet(toElement), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  854 */     public SortedSet<E> tailSet(E fromElement) { return new FilteredSortedSet(((SortedSet)this.unfiltered).tailSet(fromElement), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  859 */     public E first() { return (E)iterator().next(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public E last() {
/*  864 */       SortedSet<E> sortedUnfiltered = (SortedSet)this.unfiltered;
/*      */       while (true) {
/*  866 */         E element = (E)sortedUnfiltered.last();
/*  867 */         if (this.predicate.apply(element)) {
/*  868 */           return element;
/*      */         }
/*  870 */         sortedUnfiltered = sortedUnfiltered.headSet(element);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
/*  908 */     if (unfiltered instanceof FilteredSet) {
/*      */ 
/*      */       
/*  911 */       FilteredSet<E> filtered = (FilteredSet)unfiltered;
/*  912 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  914 */       return new FilteredNavigableSet((NavigableSet)filtered.unfiltered, combinedPredicate);
/*      */     } 
/*      */ 
/*      */     
/*  918 */     return new FilteredNavigableSet((NavigableSet)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   private static class FilteredNavigableSet<E>
/*      */     extends FilteredSortedSet<E>
/*      */     implements NavigableSet<E>
/*      */   {
/*  926 */     FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) { super(unfiltered, predicate); }
/*      */ 
/*      */ 
/*      */     
/*  930 */     NavigableSet<E> unfiltered() { return (NavigableSet)this.unfiltered; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*  936 */     public E lower(E e) { return (E)Iterators.getNext(headSet(e, false).descendingIterator(), null); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*  942 */     public E floor(E e) { return (E)Iterators.getNext(headSet(e, true).descendingIterator(), null); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  947 */     public E ceiling(E e) { return (E)Iterables.getFirst(tailSet(e, true), null); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     public E higher(E e) { return (E)Iterables.getFirst(tailSet(e, false), null); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  957 */     public E pollFirst() { return (E)Iterables.removeFirstMatching(unfiltered(), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  962 */     public E pollLast() { return (E)Iterables.removeFirstMatching(unfiltered().descendingSet(), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  967 */     public NavigableSet<E> descendingSet() { return Sets.filter(unfiltered().descendingSet(), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  972 */     public Iterator<E> descendingIterator() { return Iterators.filter(unfiltered().descendingIterator(), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  977 */     public E last() { return (E)descendingIterator().next(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  983 */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return Sets.filter(unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  989 */     public NavigableSet<E> headSet(E toElement, boolean inclusive) { return Sets.filter(unfiltered().headSet(toElement, inclusive), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  994 */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { return Sets.filter(unfiltered().tailSet(fromElement, inclusive), this.predicate); }
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
/* 1055 */   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) { return CartesianSet.create(sets); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1115 */   public static <B> Set<List<B>> cartesianProduct(Set... sets) { return cartesianProduct(Arrays.asList(sets)); }
/*      */   
/*      */   private static final class CartesianSet<E>
/*      */     extends ForwardingCollection<List<E>>
/*      */     implements Set<List<E>> {
/*      */     private final ImmutableList<ImmutableSet<E>> axes;
/*      */     private final CartesianList<E> delegate;
/*      */     
/*      */     static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
/* 1124 */       ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<ImmutableSet<E>>(sets.size());
/*      */       
/* 1126 */       for (Set<? extends E> set : sets) {
/* 1127 */         ImmutableSet<E> copy = ImmutableSet.copyOf(set);
/* 1128 */         if (copy.isEmpty()) {
/* 1129 */           return ImmutableSet.of();
/*      */         }
/* 1131 */         axesBuilder.add(copy);
/*      */       } 
/* 1133 */       final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
/* 1134 */       ImmutableList<List<E>> listAxes = new ImmutableList<List<E>>()
/*      */         {
/*      */           public int size()
/*      */           {
/* 1138 */             return axes.size();
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1143 */           public List<E> get(int index) { return ((ImmutableSet)axes.get(index)).asList(); }
/*      */ 
/*      */ 
/*      */           
/*      */           boolean isPartialView() {
/* 1148 */             return true;
/*      */           }
/*      */         };
/* 1151 */       return new CartesianSet(axes, new CartesianList(listAxes));
/*      */     }
/*      */ 
/*      */     
/*      */     private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
/* 1156 */       this.axes = axes;
/* 1157 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1162 */     protected Collection<List<E>> delegate() { return this.delegate; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1168 */       if (object instanceof CartesianSet) {
/* 1169 */         CartesianSet<?> that = (CartesianSet)object;
/* 1170 */         return this.axes.equals(that.axes);
/*      */       } 
/* 1172 */       return super.equals(object);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1181 */       int adjust = size() - 1;
/* 1182 */       for (int i = 0; i < this.axes.size(); i++) {
/* 1183 */         adjust *= 31;
/* 1184 */         adjust = adjust ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/*      */       
/* 1187 */       int hash = 1;
/* 1188 */       for (Set<E> axis : this.axes) {
/* 1189 */         hash = 31 * hash + size() / axis.size() * axis.hashCode();
/*      */         
/* 1191 */         hash = hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */       } 
/* 1193 */       hash += adjust;
/* 1194 */       return hash ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
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
/*      */   @GwtCompatible(serializable = false)
/* 1229 */   public static <E> Set<Set<E>> powerSet(Set<E> set) { return new PowerSet(set); }
/*      */   
/*      */   private static final class SubSet<E>
/*      */     extends AbstractSet<E> {
/*      */     private final ImmutableMap<E, Integer> inputSet;
/*      */     private final int mask;
/*      */     
/*      */     SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
/* 1237 */       this.inputSet = inputSet;
/* 1238 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 1243 */       return new UnmodifiableIterator<E>()
/*      */         {
/*      */           final ImmutableList<E> elements;
/*      */           
/*      */           int remainingSetBits;
/*      */           
/* 1249 */           public boolean hasNext() { return (super.remainingSetBits != 0); }
/*      */ 
/*      */ 
/*      */           
/*      */           public E next() {
/* 1254 */             int index = Integer.numberOfTrailingZeros(super.remainingSetBits);
/* 1255 */             if (index == 32) {
/* 1256 */               throw new NoSuchElementException();
/*      */             }
/* 1258 */             super.remainingSetBits &= (1 << index ^ 0xFFFFFFFF);
/* 1259 */             return (E)super.elements.get(index);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1266 */     public int size() { return Integer.bitCount(this.mask); }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/* 1271 */       Integer index = (Integer)this.inputSet.get(o);
/* 1272 */       return (index != null && (this.mask & 1 << index.intValue()) != 0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
/*      */     final ImmutableMap<E, Integer> inputSet;
/*      */     
/*      */     PowerSet(Set<E> input) {
/* 1280 */       ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
/* 1281 */       int i = 0;
/* 1282 */       for (E e : (Set)Preconditions.checkNotNull(input)) {
/* 1283 */         builder.put(e, Integer.valueOf(i++));
/*      */       }
/* 1285 */       this.inputSet = builder.build();
/* 1286 */       Preconditions.checkArgument((this.inputSet.size() <= 30), "Too many elements to create power set: %s > 30", new Object[] { Integer.valueOf(this.inputSet.size()) });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1291 */     public int size() { return 1 << this.inputSet.size(); }
/*      */ 
/*      */ 
/*      */     
/* 1295 */     public boolean isEmpty() { return false; }
/*      */ 
/*      */     
/*      */     public Iterator<Set<E>> iterator() {
/* 1299 */       return new AbstractIndexedListIterator<Set<E>>(size())
/*      */         {
/* 1301 */           protected Set<E> get(int setBits) { return new Sets.SubSet(Sets.PowerSet.this.inputSet, setBits); }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object obj) {
/* 1307 */       if (obj instanceof Set) {
/* 1308 */         Set<?> set = (Set)obj;
/* 1309 */         return this.inputSet.keySet().containsAll(set);
/*      */       } 
/* 1311 */       return false;
/*      */     }
/*      */     
/*      */     public boolean equals(@Nullable Object obj) {
/* 1315 */       if (obj instanceof PowerSet) {
/* 1316 */         PowerSet<?> that = (PowerSet)obj;
/* 1317 */         return this.inputSet.equals(that.inputSet);
/*      */       } 
/* 1319 */       return super.equals(obj);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1328 */     public int hashCode() { return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1; }
/*      */ 
/*      */ 
/*      */     
/* 1332 */     public String toString() { return "powerSet(" + this.inputSet + ")"; }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(Set<?> s) {
/* 1340 */     int hashCode = 0;
/* 1341 */     for (Object o : s) {
/* 1342 */       hashCode += ((o != null) ? o.hashCode() : 0);
/*      */       
/* 1344 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1347 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Set<?> s, @Nullable Object object) {
/* 1354 */     if (s == object) {
/* 1355 */       return true;
/*      */     }
/* 1357 */     if (object instanceof Set) {
/* 1358 */       Set<?> o = (Set)object;
/*      */       
/*      */       try {
/* 1361 */         return (s.size() == o.size() && s.containsAll(o));
/* 1362 */       } catch (NullPointerException ignored) {
/* 1363 */         return false;
/* 1364 */       } catch (ClassCastException ignored) {
/* 1365 */         return false;
/*      */       } 
/*      */     } 
/* 1368 */     return false;
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
/*      */   @GwtIncompatible("NavigableSet")
/*      */   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
/* 1390 */     if (set instanceof ImmutableSortedSet || set instanceof UnmodifiableNavigableSet)
/*      */     {
/* 1392 */       return set;
/*      */     }
/* 1394 */     return new UnmodifiableNavigableSet(set);
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
/*      */     private final NavigableSet<E> delegate;
/*      */     private UnmodifiableNavigableSet<E> descendingSet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1403 */     UnmodifiableNavigableSet(NavigableSet<E> delegate) { this.delegate = (NavigableSet)Preconditions.checkNotNull(delegate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1408 */     protected SortedSet<E> delegate() { return Collections.unmodifiableSortedSet(this.delegate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1413 */     public E lower(E e) { return (E)this.delegate.lower(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1418 */     public E floor(E e) { return (E)this.delegate.floor(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1423 */     public E ceiling(E e) { return (E)this.delegate.ceiling(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1428 */     public E higher(E e) { return (E)this.delegate.higher(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1433 */     public E pollFirst() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1438 */     public E pollLast() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<E> descendingSet() {
/* 1445 */       UnmodifiableNavigableSet<E> result = this.descendingSet;
/* 1446 */       if (result == null) {
/* 1447 */         result = this.descendingSet = new UnmodifiableNavigableSet(this.delegate.descendingSet());
/*      */         
/* 1449 */         result.descendingSet = this;
/*      */       } 
/* 1451 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1456 */     public Iterator<E> descendingIterator() { return Iterators.unmodifiableIterator(this.delegate.descendingIterator()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1465 */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1474 */     public NavigableSet<E> headSet(E toElement, boolean inclusive) { return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1479 */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive)); }
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
/*      */   @GwtIncompatible("NavigableSet")
/* 1532 */   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) { return Synchronized.navigableSet(navigableSet); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
/* 1539 */     boolean changed = false;
/* 1540 */     while (iterator.hasNext()) {
/* 1541 */       changed |= set.remove(iterator.next());
/*      */     }
/* 1543 */     return changed;
/*      */   }
/*      */   
/*      */   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
/* 1547 */     Preconditions.checkNotNull(collection);
/* 1548 */     if (collection instanceof Multiset) {
/* 1549 */       collection = ((Multiset)collection).elementSet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1558 */     if (collection instanceof Set && collection.size() > set.size()) {
/* 1559 */       return Iterators.removeAll(set.iterator(), collection);
/*      */     }
/* 1561 */     return removeAllImpl(set, collection.iterator());
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   static class DescendingSet<E>
/*      */     extends ForwardingNavigableSet<E>
/*      */   {
/*      */     private final NavigableSet<E> forward;
/*      */     
/* 1570 */     DescendingSet(NavigableSet<E> forward) { this.forward = forward; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1575 */     protected NavigableSet<E> delegate() { return this.forward; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1580 */     public E lower(E e) { return (E)this.forward.higher(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1585 */     public E floor(E e) { return (E)this.forward.ceiling(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1590 */     public E ceiling(E e) { return (E)this.forward.floor(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1595 */     public E higher(E e) { return (E)this.forward.lower(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1600 */     public E pollFirst() { return (E)this.forward.pollLast(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1605 */     public E pollLast() { return (E)this.forward.pollFirst(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1610 */     public NavigableSet<E> descendingSet() { return this.forward; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1615 */     public Iterator<E> descendingIterator() { return this.forward.iterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1624 */     public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1629 */     public NavigableSet<E> headSet(E toElement, boolean inclusive) { return this.forward.tailSet(toElement, inclusive).descendingSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1634 */     public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { return this.forward.headSet(fromElement, inclusive).descendingSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super E> comparator() {
/* 1640 */       Comparator<? super E> forwardComparator = this.forward.comparator();
/* 1641 */       if (forwardComparator == null) {
/* 1642 */         return Ordering.natural().reverse();
/*      */       }
/* 1644 */       return reverse(forwardComparator);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1650 */     private static <T> Ordering<T> reverse(Comparator<T> forward) { return Ordering.from(forward).reverse(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1655 */     public E first() { return (E)this.forward.last(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1660 */     public SortedSet<E> headSet(E toElement) { return standardHeadSet(toElement); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1665 */     public E last() { return (E)this.forward.first(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1670 */     public SortedSet<E> subSet(E fromElement, E toElement) { return standardSubSet(fromElement, toElement); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1675 */     public SortedSet<E> tailSet(E fromElement) { return standardTailSet(fromElement); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1680 */     public Iterator<E> iterator() { return this.forward.descendingIterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1685 */     public Object[] toArray() { return standardToArray(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1690 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1695 */     public String toString() { return standardToString(); }
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Sets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */