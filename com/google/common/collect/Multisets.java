/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Multisets
/*      */ {
/*      */   public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
/*   74 */     if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset)
/*      */     {
/*      */ 
/*      */       
/*   78 */       return multiset;
/*      */     }
/*      */     
/*   81 */     return new UnmodifiableMultiset((Multiset)Preconditions.checkNotNull(multiset));
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
/*   92 */   public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> multiset) { return (Multiset)Preconditions.checkNotNull(multiset); }
/*      */   
/*      */   static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
/*      */     final Multiset<? extends E> delegate;
/*      */     Set<E> elementSet;
/*      */     Set<Multiset.Entry<E>> entrySet;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*  100 */     UnmodifiableMultiset(Multiset<? extends E> delegate) { this.delegate = delegate; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  106 */     protected Multiset<E> delegate() { return this.delegate; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  112 */     Set<E> createElementSet() { return Collections.unmodifiableSet(this.delegate.elementSet()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<E> elementSet() {
/*  117 */       Set<E> es = this.elementSet;
/*  118 */       return (es == null) ? (this.elementSet = createElementSet()) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Multiset.Entry<E>> entrySet() {
/*  125 */       Set<Multiset.Entry<E>> es = this.entrySet;
/*  126 */       return (es == null) ? (this.entrySet = Collections.unmodifiableSet(this.delegate.entrySet())) : es;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  136 */     public Iterator<E> iterator() { return Iterators.unmodifiableIterator(this.delegate.iterator()); }
/*      */ 
/*      */ 
/*      */     
/*  140 */     public boolean add(E element) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  144 */     public int add(E element, int occurences) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  148 */     public boolean addAll(Collection<? extends E> elementsToAdd) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  152 */     public boolean remove(Object element) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  156 */     public int remove(Object element, int occurrences) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  160 */     public boolean removeAll(Collection<?> elementsToRemove) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  164 */     public boolean retainAll(Collection<?> elementsToRetain) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  168 */     public void clear() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  172 */     public int setCount(E element, int count) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*  176 */     public boolean setCount(E element, int oldCount, int newCount) { throw new UnsupportedOperationException(); }
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
/*      */   @Beta
/*  200 */   public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) { return new UnmodifiableSortedMultiset((SortedMultiset)Preconditions.checkNotNull(sortedMultiset)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  212 */   public static <E> Multiset.Entry<E> immutableEntry(@Nullable E e, int n) { return new ImmutableEntry(e, n); }
/*      */   
/*      */   static final class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
/*      */     @Nullable
/*      */     final E element;
/*      */     final int count;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     ImmutableEntry(@Nullable E element, int count) {
/*  221 */       this.element = element;
/*  222 */       this.count = count;
/*  223 */       CollectPreconditions.checkNonnegative(count, "count");
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*  228 */     public E getElement() { return (E)this.element; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  233 */     public int getCount() { return this.count; }
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> filter(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  267 */     if (unfiltered instanceof FilteredMultiset) {
/*      */ 
/*      */       
/*  270 */       FilteredMultiset<E> filtered = (FilteredMultiset)unfiltered;
/*  271 */       Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
/*      */       
/*  273 */       return new FilteredMultiset(filtered.unfiltered, combinedPredicate);
/*      */     } 
/*  275 */     return new FilteredMultiset(unfiltered, predicate);
/*      */   }
/*      */   
/*      */   private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
/*      */     final Multiset<E> unfiltered;
/*      */     final Predicate<? super E> predicate;
/*      */     
/*      */     FilteredMultiset(Multiset<E> unfiltered, Predicate<? super E> predicate) {
/*  283 */       this.unfiltered = (Multiset)Preconditions.checkNotNull(unfiltered);
/*  284 */       this.predicate = (Predicate)Preconditions.checkNotNull(predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  289 */     public UnmodifiableIterator<E> iterator() { return Iterators.filter(this.unfiltered.iterator(), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  294 */     Set<E> createElementSet() { return Sets.filter(this.unfiltered.elementSet(), this.predicate); }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<Multiset.Entry<E>> createEntrySet() {
/*  299 */       return Sets.filter(this.unfiltered.entrySet(), new Predicate<Multiset.Entry<E>>()
/*      */           {
/*      */             public boolean apply(Multiset.Entry<E> entry) {
/*  302 */               return Multisets.FilteredMultiset.this.predicate.apply(entry.getElement());
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  309 */     Iterator<Multiset.Entry<E>> entryIterator() { throw new AssertionError("should never be called"); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  314 */     int distinctElements() { return elementSet().size(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public int count(@Nullable Object element) {
/*  319 */       int count = this.unfiltered.count(element);
/*  320 */       if (count > 0) {
/*      */         
/*  322 */         E e = (E)element;
/*  323 */         return this.predicate.apply(e) ? count : 0;
/*      */       } 
/*  325 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int add(@Nullable E element, int occurrences) {
/*  330 */       Preconditions.checkArgument(this.predicate.apply(element), "Element %s does not match predicate %s", new Object[] { element, this.predicate });
/*      */       
/*  332 */       return this.unfiltered.add(element, occurrences);
/*      */     }
/*      */ 
/*      */     
/*      */     public int remove(@Nullable Object element, int occurrences) {
/*  337 */       CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  338 */       if (occurrences == 0) {
/*  339 */         return count(element);
/*      */       }
/*  341 */       return contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  347 */     public void clear() { elementSet().clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int inferDistinctElements(Iterable<?> elements) {
/*  358 */     if (elements instanceof Multiset) {
/*  359 */       return ((Multiset)elements).elementSet().size();
/*      */     }
/*  361 */     return 11;
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  382 */     Preconditions.checkNotNull(multiset1);
/*  383 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  385 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public boolean contains(@Nullable Object element) {
/*  388 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  393 */         public boolean isEmpty() { return (multiset1.isEmpty() && multiset2.isEmpty()); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  398 */         public int count(Object element) { return Math.max(multiset1.count(element), multiset2.count(element)); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  403 */         Set<E> createElementSet() { return Sets.union(multiset1.elementSet(), multiset2.elementSet()); }
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  408 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  410 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */ 
/*      */           
/*  413 */           return new AbstractIterator<Multiset.Entry<E>>()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  416 */                 if (iterator1.hasNext()) {
/*  417 */                   Multiset.Entry<? extends E> entry1 = (Multiset.Entry)iterator1.next();
/*  418 */                   E element = (E)entry1.getElement();
/*  419 */                   int count = Math.max(entry1.getCount(), multiset2.count(element));
/*  420 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  422 */                 while (iterator2.hasNext()) {
/*  423 */                   Multiset.Entry<? extends E> entry2 = (Multiset.Entry)iterator2.next();
/*  424 */                   E element = (E)entry2.getElement();
/*  425 */                   if (!multiset1.contains(element)) {
/*  426 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  429 */                 return (Multiset.Entry)endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  436 */         int distinctElements() { return elementSet().size(); }
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
/*      */   public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  457 */     Preconditions.checkNotNull(multiset1);
/*  458 */     Preconditions.checkNotNull(multiset2);
/*      */     
/*  460 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public int count(Object element) {
/*  463 */           int count1 = multiset1.count(element);
/*  464 */           return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  469 */         Set<E> createElementSet() { return Sets.intersection(multiset1.elementSet(), multiset2.elementSet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  475 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  477 */           return new AbstractIterator<Multiset.Entry<E>>()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  480 */                 while (iterator1.hasNext()) {
/*  481 */                   Multiset.Entry<E> entry1 = (Multiset.Entry)iterator1.next();
/*  482 */                   E element = (E)entry1.getElement();
/*  483 */                   int count = Math.min(entry1.getCount(), multiset2.count(element));
/*  484 */                   if (count > 0) {
/*  485 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  488 */                 return (Multiset.Entry)endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  495 */         int distinctElements() { return elementSet().size(); }
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
/*  518 */     Preconditions.checkNotNull(multiset1);
/*  519 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  522 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public boolean contains(@Nullable Object element) {
/*  525 */           return (multiset1.contains(element) || multiset2.contains(element));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  530 */         public boolean isEmpty() { return (multiset1.isEmpty() && multiset2.isEmpty()); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  535 */         public int size() { return multiset1.size() + multiset2.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  540 */         public int count(Object element) { return multiset1.count(element) + multiset2.count(element); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  545 */         Set<E> createElementSet() { return Sets.union(multiset1.elementSet(), multiset2.elementSet()); }
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  550 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
/*      */           
/*  552 */           final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
/*      */           
/*  554 */           return new AbstractIterator<Multiset.Entry<E>>()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  557 */                 if (iterator1.hasNext()) {
/*  558 */                   Multiset.Entry<? extends E> entry1 = (Multiset.Entry)iterator1.next();
/*  559 */                   E element = (E)entry1.getElement();
/*  560 */                   int count = entry1.getCount() + multiset2.count(element);
/*  561 */                   return Multisets.immutableEntry(element, count);
/*      */                 } 
/*  563 */                 while (iterator2.hasNext()) {
/*  564 */                   Multiset.Entry<? extends E> entry2 = (Multiset.Entry)iterator2.next();
/*  565 */                   E element = (E)entry2.getElement();
/*  566 */                   if (!multiset1.contains(element)) {
/*  567 */                     return Multisets.immutableEntry(element, entry2.getCount());
/*      */                   }
/*      */                 } 
/*  570 */                 return (Multiset.Entry)endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  577 */         int distinctElements() { return elementSet().size(); }
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
/*      */   @Beta
/*      */   public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
/*  600 */     Preconditions.checkNotNull(multiset1);
/*  601 */     Preconditions.checkNotNull(multiset2);
/*      */ 
/*      */     
/*  604 */     return new AbstractMultiset<E>()
/*      */       {
/*      */         public int count(@Nullable Object element) {
/*  607 */           int count1 = multiset1.count(element);
/*  608 */           return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         Iterator<Multiset.Entry<E>> entryIterator() {
/*  614 */           final Iterator<Multiset.Entry<E>> iterator1 = multiset1.entrySet().iterator();
/*  615 */           return new AbstractIterator<Multiset.Entry<E>>()
/*      */             {
/*      */               protected Multiset.Entry<E> computeNext() {
/*  618 */                 while (iterator1.hasNext()) {
/*  619 */                   Multiset.Entry<E> entry1 = (Multiset.Entry)iterator1.next();
/*  620 */                   E element = (E)entry1.getElement();
/*  621 */                   int count = entry1.getCount() - multiset2.count(element);
/*  622 */                   if (count > 0) {
/*  623 */                     return Multisets.immutableEntry(element, count);
/*      */                   }
/*      */                 } 
/*  626 */                 return (Multiset.Entry)endOfData();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  633 */         int distinctElements() { return Iterators.size(entryIterator()); }
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
/*      */   public static boolean containsOccurrences(Multiset<?> superMultiset, Multiset<?> subMultiset) {
/*  646 */     Preconditions.checkNotNull(superMultiset);
/*  647 */     Preconditions.checkNotNull(subMultiset);
/*  648 */     for (Multiset.Entry<?> entry : subMultiset.entrySet()) {
/*  649 */       int superCount = superMultiset.count(entry.getElement());
/*  650 */       if (superCount < entry.getCount()) {
/*  651 */         return false;
/*      */       }
/*      */     } 
/*  654 */     return true;
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
/*  678 */   public static boolean retainOccurrences(Multiset<?> multisetToModify, Multiset<?> multisetToRetain) { return retainOccurrencesImpl(multisetToModify, multisetToRetain); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean retainOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRetain) {
/*  686 */     Preconditions.checkNotNull(multisetToModify);
/*  687 */     Preconditions.checkNotNull(occurrencesToRetain);
/*      */     
/*  689 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  690 */     boolean changed = false;
/*  691 */     while (entryIterator.hasNext()) {
/*  692 */       Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/*  693 */       int retainCount = occurrencesToRetain.count(entry.getElement());
/*  694 */       if (retainCount == 0) {
/*  695 */         entryIterator.remove();
/*  696 */         changed = true; continue;
/*  697 */       }  if (retainCount < entry.getCount()) {
/*  698 */         multisetToModify.setCount(entry.getElement(), retainCount);
/*  699 */         changed = true;
/*      */       } 
/*      */     } 
/*  702 */     return changed;
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
/*  730 */   public static boolean removeOccurrences(Multiset<?> multisetToModify, Multiset<?> occurrencesToRemove) { return removeOccurrencesImpl(multisetToModify, occurrencesToRemove); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> boolean removeOccurrencesImpl(Multiset<E> multisetToModify, Multiset<?> occurrencesToRemove) {
/*  739 */     Preconditions.checkNotNull(multisetToModify);
/*  740 */     Preconditions.checkNotNull(occurrencesToRemove);
/*      */     
/*  742 */     boolean changed = false;
/*  743 */     Iterator<Multiset.Entry<E>> entryIterator = multisetToModify.entrySet().iterator();
/*  744 */     while (entryIterator.hasNext()) {
/*  745 */       Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/*  746 */       int removeCount = occurrencesToRemove.count(entry.getElement());
/*  747 */       if (removeCount >= entry.getCount()) {
/*  748 */         entryIterator.remove();
/*  749 */         changed = true; continue;
/*  750 */       }  if (removeCount > 0) {
/*  751 */         multisetToModify.remove(entry.getElement(), removeCount);
/*  752 */         changed = true;
/*      */       } 
/*      */     } 
/*  755 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractEntry<E>
/*      */     extends Object
/*      */     implements Multiset.Entry<E>
/*      */   {
/*      */     public boolean equals(@Nullable Object object) {
/*  768 */       if (object instanceof Multiset.Entry) {
/*  769 */         Multiset.Entry<?> that = (Multiset.Entry)object;
/*  770 */         return (getCount() == that.getCount() && Objects.equal(getElement(), that.getElement()));
/*      */       } 
/*      */       
/*  773 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  781 */       E e = (E)getElement();
/*  782 */       return ((e == null) ? 0 : e.hashCode()) ^ getCount();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  793 */       String text = String.valueOf(getElement());
/*  794 */       int n = getCount();
/*  795 */       return (n == 1) ? text : (text + " x " + n);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Multiset<?> multiset, @Nullable Object object) {
/*  803 */     if (object == multiset) {
/*  804 */       return true;
/*      */     }
/*  806 */     if (object instanceof Multiset) {
/*  807 */       Multiset<?> that = (Multiset)object;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  814 */       if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size())
/*      */       {
/*  816 */         return false;
/*      */       }
/*  818 */       for (Multiset.Entry<?> entry : that.entrySet()) {
/*  819 */         if (multiset.count(entry.getElement()) != entry.getCount()) {
/*  820 */           return false;
/*      */         }
/*      */       } 
/*  823 */       return true;
/*      */     } 
/*  825 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(Multiset<E> self, Collection<? extends E> elements) {
/*  833 */     if (elements.isEmpty()) {
/*  834 */       return false;
/*      */     }
/*  836 */     if (elements instanceof Multiset) {
/*  837 */       Multiset<? extends E> that = cast(elements);
/*  838 */       for (Multiset.Entry<? extends E> entry : that.entrySet()) {
/*  839 */         self.add(entry.getElement(), entry.getCount());
/*      */       }
/*      */     } else {
/*  842 */       Iterators.addAll(self, elements.iterator());
/*      */     } 
/*  844 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean removeAllImpl(Multiset<?> self, Collection<?> elementsToRemove) {
/*  852 */     Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
/*      */ 
/*      */     
/*  855 */     return self.elementSet().removeAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean retainAllImpl(Multiset<?> self, Collection<?> elementsToRetain) {
/*  863 */     Preconditions.checkNotNull(elementsToRetain);
/*  864 */     Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
/*      */ 
/*      */     
/*  867 */     return self.elementSet().retainAll(collection);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> int setCountImpl(Multiset<E> self, E element, int count) {
/*  874 */     CollectPreconditions.checkNonnegative(count, "count");
/*      */     
/*  876 */     int oldCount = self.count(element);
/*      */     
/*  878 */     int delta = count - oldCount;
/*  879 */     if (delta > 0) {
/*  880 */       self.add(element, delta);
/*  881 */     } else if (delta < 0) {
/*  882 */       self.remove(element, -delta);
/*      */     } 
/*      */     
/*  885 */     return oldCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean setCountImpl(Multiset<E> self, E element, int oldCount, int newCount) {
/*  893 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  894 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*      */     
/*  896 */     if (self.count(element) == oldCount) {
/*  897 */       self.setCount(element, newCount);
/*  898 */       return true;
/*      */     } 
/*  900 */     return false;
/*      */   }
/*      */   
/*      */   static abstract class ElementSet<E>
/*      */     extends Sets.ImprovedAbstractSet<E>
/*      */   {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*  908 */     public void clear() { multiset().clear(); }
/*      */ 
/*      */ 
/*      */     
/*  912 */     public boolean contains(Object o) { return multiset().contains(o); }
/*      */ 
/*      */ 
/*      */     
/*  916 */     public boolean containsAll(Collection<?> c) { return multiset().containsAll(c); }
/*      */ 
/*      */ 
/*      */     
/*  920 */     public boolean isEmpty() { return multiset().isEmpty(); }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/*  924 */       return new TransformedIterator<Multiset.Entry<E>, E>(multiset().entrySet().iterator())
/*      */         {
/*      */           E transform(Multiset.Entry<E> entry) {
/*  927 */             return (E)entry.getElement();
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*  934 */       int count = multiset().count(o);
/*  935 */       if (count > 0) {
/*  936 */         multiset().remove(o, count);
/*  937 */         return true;
/*      */       } 
/*  939 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  943 */     public int size() { return multiset().entrySet().size(); }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<E>
/*      */     extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
/*      */     abstract Multiset<E> multiset();
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/*  951 */       if (o instanceof Multiset.Entry) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  956 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/*  957 */         if (entry.getCount() <= 0) {
/*  958 */           return false;
/*      */         }
/*  960 */         int count = multiset().count(entry.getElement());
/*  961 */         return (count == entry.getCount());
/*      */       } 
/*      */       
/*  964 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object object) {
/*  970 */       if (object instanceof Multiset.Entry) {
/*  971 */         Multiset.Entry<?> entry = (Multiset.Entry)object;
/*  972 */         Object element = entry.getElement();
/*  973 */         int entryCount = entry.getCount();
/*  974 */         if (entryCount != 0) {
/*      */ 
/*      */           
/*  977 */           Multiset<Object> multiset = multiset();
/*  978 */           return multiset.setCount(element, entryCount, 0);
/*      */         } 
/*      */       } 
/*  981 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  985 */     public void clear() { multiset().clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  993 */   static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) { return new MultisetIteratorImpl(multiset, multiset.entrySet().iterator()); }
/*      */ 
/*      */   
/*      */   static final class MultisetIteratorImpl<E>
/*      */     extends Object
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Multiset<E> multiset;
/*      */     
/*      */     private final Iterator<Multiset.Entry<E>> entryIterator;
/*      */     private Multiset.Entry<E> currentEntry;
/*      */     private int laterCount;
/*      */     private int totalCount;
/*      */     private boolean canRemove;
/*      */     
/*      */     MultisetIteratorImpl(Multiset<E> multiset, Iterator<Multiset.Entry<E>> entryIterator) {
/* 1009 */       this.multiset = multiset;
/* 1010 */       this.entryIterator = entryIterator;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1015 */     public boolean hasNext() { return (this.laterCount > 0 || this.entryIterator.hasNext()); }
/*      */ 
/*      */ 
/*      */     
/*      */     public E next() {
/* 1020 */       if (!hasNext()) {
/* 1021 */         throw new NoSuchElementException();
/*      */       }
/* 1023 */       if (this.laterCount == 0) {
/* 1024 */         this.currentEntry = (Multiset.Entry)this.entryIterator.next();
/* 1025 */         this.totalCount = this.laterCount = this.currentEntry.getCount();
/*      */       } 
/* 1027 */       this.laterCount--;
/* 1028 */       this.canRemove = true;
/* 1029 */       return (E)this.currentEntry.getElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1034 */       CollectPreconditions.checkRemove(this.canRemove);
/* 1035 */       if (this.totalCount == 1) {
/* 1036 */         this.entryIterator.remove();
/*      */       } else {
/* 1038 */         this.multiset.remove(this.currentEntry.getElement());
/*      */       } 
/* 1040 */       this.totalCount--;
/* 1041 */       this.canRemove = false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int sizeImpl(Multiset<?> multiset) {
/* 1049 */     long size = 0L;
/* 1050 */     for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 1051 */       size += entry.getCount();
/*      */     }
/* 1053 */     return Ints.saturatedCast(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1060 */   static <T> Multiset<T> cast(Iterable<T> iterable) { return (Multiset)iterable; }
/*      */ 
/*      */   
/* 1063 */   private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING = new Ordering<Multiset.Entry<?>>()
/*      */     {
/*      */       public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
/* 1066 */         return Ints.compare(entry2.getCount(), entry1.getCount());
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
/* 1078 */     List<Multiset.Entry<E>> sortedEntries = DECREASING_COUNT_ORDERING.immutableSortedCopy(multiset.entrySet());
/*      */     
/* 1080 */     return ImmutableMultiset.copyFromEntries(sortedEntries);
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Multisets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */