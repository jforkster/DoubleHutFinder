/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class Constraints
/*     */ {
/*  54 */   public static <E> Collection<E> constrainedCollection(Collection<E> collection, Constraint<? super E> constraint) { return new ConstrainedCollection(collection, constraint); }
/*     */ 
/*     */   
/*     */   static class ConstrainedCollection<E>
/*     */     extends ForwardingCollection<E>
/*     */   {
/*     */     private final Collection<E> delegate;
/*     */     private final Constraint<? super E> constraint;
/*     */     
/*     */     public ConstrainedCollection(Collection<E> delegate, Constraint<? super E> constraint) {
/*  64 */       this.delegate = (Collection)Preconditions.checkNotNull(delegate);
/*  65 */       this.constraint = (Constraint)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/*  68 */     protected Collection<E> delegate() { return this.delegate; }
/*     */     
/*     */     public boolean add(E element) {
/*  71 */       this.constraint.checkElement(element);
/*  72 */       return this.delegate.add(element);
/*     */     }
/*     */     
/*  75 */     public boolean addAll(Collection<? extends E> elements) { return this.delegate.addAll(Constraints.checkElements(elements, this.constraint)); }
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
/*  93 */   public static <E> Set<E> constrainedSet(Set<E> set, Constraint<? super E> constraint) { return new ConstrainedSet(set, constraint); }
/*     */   
/*     */   static class ConstrainedSet<E>
/*     */     extends ForwardingSet<E>
/*     */   {
/*     */     private final Set<E> delegate;
/*     */     private final Constraint<? super E> constraint;
/*     */     
/*     */     public ConstrainedSet(Set<E> delegate, Constraint<? super E> constraint) {
/* 102 */       this.delegate = (Set)Preconditions.checkNotNull(delegate);
/* 103 */       this.constraint = (Constraint)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/* 106 */     protected Set<E> delegate() { return this.delegate; }
/*     */     
/*     */     public boolean add(E element) {
/* 109 */       this.constraint.checkElement(element);
/* 110 */       return this.delegate.add(element);
/*     */     }
/*     */     
/* 113 */     public boolean addAll(Collection<? extends E> elements) { return this.delegate.addAll(Constraints.checkElements(elements, this.constraint)); }
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
/* 131 */   public static <E> SortedSet<E> constrainedSortedSet(SortedSet<E> sortedSet, Constraint<? super E> constraint) { return new ConstrainedSortedSet(sortedSet, constraint); }
/*     */ 
/*     */   
/*     */   private static class ConstrainedSortedSet<E>
/*     */     extends ForwardingSortedSet<E>
/*     */   {
/*     */     final SortedSet<E> delegate;
/*     */     final Constraint<? super E> constraint;
/*     */     
/*     */     ConstrainedSortedSet(SortedSet<E> delegate, Constraint<? super E> constraint) {
/* 141 */       this.delegate = (SortedSet)Preconditions.checkNotNull(delegate);
/* 142 */       this.constraint = (Constraint)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/* 145 */     protected SortedSet<E> delegate() { return this.delegate; }
/*     */ 
/*     */     
/* 148 */     public SortedSet<E> headSet(E toElement) { return Constraints.constrainedSortedSet(this.delegate.headSet(toElement), this.constraint); }
/*     */ 
/*     */     
/* 151 */     public SortedSet<E> subSet(E fromElement, E toElement) { return Constraints.constrainedSortedSet(this.delegate.subSet(fromElement, toElement), this.constraint); }
/*     */ 
/*     */ 
/*     */     
/* 155 */     public SortedSet<E> tailSet(E fromElement) { return Constraints.constrainedSortedSet(this.delegate.tailSet(fromElement), this.constraint); }
/*     */     
/*     */     public boolean add(E element) {
/* 158 */       this.constraint.checkElement(element);
/* 159 */       return this.delegate.add(element);
/*     */     }
/*     */     
/* 162 */     public boolean addAll(Collection<? extends E> elements) { return this.delegate.addAll(Constraints.checkElements(elements, this.constraint)); }
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
/* 181 */   public static <E> List<E> constrainedList(List<E> list, Constraint<? super E> constraint) { return (list instanceof RandomAccess) ? new ConstrainedRandomAccessList(list, constraint) : new ConstrainedList(list, constraint); }
/*     */ 
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ConstrainedList<E>
/*     */     extends ForwardingList<E>
/*     */   {
/*     */     final List<E> delegate;
/*     */     
/*     */     final Constraint<? super E> constraint;
/*     */     
/*     */     ConstrainedList(List<E> delegate, Constraint<? super E> constraint) {
/* 193 */       this.delegate = (List)Preconditions.checkNotNull(delegate);
/* 194 */       this.constraint = (Constraint)Preconditions.checkNotNull(constraint);
/*     */     }
/*     */     
/* 197 */     protected List<E> delegate() { return this.delegate; }
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 201 */       this.constraint.checkElement(element);
/* 202 */       return this.delegate.add(element);
/*     */     }
/*     */     public void add(int index, E element) {
/* 205 */       this.constraint.checkElement(element);
/* 206 */       this.delegate.add(index, element);
/*     */     }
/*     */     
/* 209 */     public boolean addAll(Collection<? extends E> elements) { return this.delegate.addAll(Constraints.checkElements(elements, this.constraint)); }
/*     */ 
/*     */ 
/*     */     
/* 213 */     public boolean addAll(int index, Collection<? extends E> elements) { return this.delegate.addAll(index, Constraints.checkElements(elements, this.constraint)); }
/*     */ 
/*     */     
/* 216 */     public ListIterator<E> listIterator() { return Constraints.constrainedListIterator(this.delegate.listIterator(), this.constraint); }
/*     */ 
/*     */     
/* 219 */     public ListIterator<E> listIterator(int index) { return Constraints.constrainedListIterator(this.delegate.listIterator(index), this.constraint); }
/*     */     
/*     */     public E set(int index, E element) {
/* 222 */       this.constraint.checkElement(element);
/* 223 */       return (E)this.delegate.set(index, element);
/*     */     }
/*     */     
/* 226 */     public List<E> subList(int fromIndex, int toIndex) { return Constraints.constrainedList(this.delegate.subList(fromIndex, toIndex), this.constraint); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConstrainedRandomAccessList<E>
/*     */     extends ConstrainedList<E>
/*     */     implements RandomAccess
/*     */   {
/* 236 */     ConstrainedRandomAccessList(List<E> delegate, Constraint<? super E> constraint) { super(delegate, constraint); }
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
/* 251 */   private static <E> ListIterator<E> constrainedListIterator(ListIterator<E> listIterator, Constraint<? super E> constraint) { return new ConstrainedListIterator(listIterator, constraint); }
/*     */ 
/*     */   
/*     */   static class ConstrainedListIterator<E>
/*     */     extends ForwardingListIterator<E>
/*     */   {
/*     */     private final ListIterator<E> delegate;
/*     */     private final Constraint<? super E> constraint;
/*     */     
/*     */     public ConstrainedListIterator(ListIterator<E> delegate, Constraint<? super E> constraint) {
/* 261 */       this.delegate = delegate;
/* 262 */       this.constraint = constraint;
/*     */     }
/*     */     
/* 265 */     protected ListIterator<E> delegate() { return this.delegate; }
/*     */ 
/*     */     
/*     */     public void add(E element) {
/* 269 */       this.constraint.checkElement(element);
/* 270 */       this.delegate.add(element);
/*     */     }
/*     */     public void set(E element) {
/* 273 */       this.constraint.checkElement(element);
/* 274 */       this.delegate.set(element);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Collection<E> constrainedTypePreservingCollection(Collection<E> collection, Constraint<E> constraint) {
/* 280 */     if (collection instanceof SortedSet)
/* 281 */       return constrainedSortedSet((SortedSet)collection, constraint); 
/* 282 */     if (collection instanceof Set)
/* 283 */       return constrainedSet((Set)collection, constraint); 
/* 284 */     if (collection instanceof List) {
/* 285 */       return constrainedList((List)collection, constraint);
/*     */     }
/* 287 */     return constrainedCollection(collection, constraint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> Collection<E> checkElements(Collection<E> elements, Constraint<? super E> constraint) {
/* 298 */     Collection<E> copy = Lists.newArrayList(elements);
/* 299 */     for (E element : copy) {
/* 300 */       constraint.checkElement(element);
/*     */     }
/* 302 */     return copy;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Constraints.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */