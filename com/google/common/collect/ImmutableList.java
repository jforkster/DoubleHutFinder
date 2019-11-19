/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*  66 */   private static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static <E> ImmutableList<E> of() { return EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static <E> ImmutableList<E> of(E element) { return new SingletonImmutableList(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public static <E> ImmutableList<E> of(E e1, E e2) { return construct(new Object[] { e1, e2 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) { return construct(new Object[] { e1, e2, e3 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) { return construct(new Object[] { e1, e2, e3, e4 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) { return construct(new Object[] { e1, e2, e3, e4, e5 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) { return construct(new Object[] { e1, e2, e3, e4, e5, e6 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) { return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) { return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) { return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) { return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 184 */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) { return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
/* 199 */     Object[] array = new Object[12 + others.length];
/* 200 */     array[0] = e1;
/* 201 */     array[1] = e2;
/* 202 */     array[2] = e3;
/* 203 */     array[3] = e4;
/* 204 */     array[4] = e5;
/* 205 */     array[5] = e6;
/* 206 */     array[6] = e7;
/* 207 */     array[7] = e8;
/* 208 */     array[8] = e9;
/* 209 */     array[9] = e10;
/* 210 */     array[10] = e11;
/* 211 */     array[11] = e12;
/* 212 */     System.arraycopy(others, 0, array, 12, others.length);
/* 213 */     return construct(array);
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
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 225 */     Preconditions.checkNotNull(elements);
/* 226 */     return (elements instanceof Collection) ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
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
/*     */   public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
/* 251 */     if (elements instanceof ImmutableCollection) {
/*     */       
/* 253 */       ImmutableList<E> list = ((ImmutableCollection)elements).asList();
/* 254 */       return list.isPartialView() ? asImmutableList(list.toArray()) : list;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return construct(elements.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 268 */     if (!elements.hasNext()) {
/* 269 */       return of();
/*     */     }
/* 271 */     E first = (E)elements.next();
/* 272 */     if (!elements.hasNext()) {
/* 273 */       return of(first);
/*     */     }
/* 275 */     return (new Builder()).add(first).addAll(elements).build();
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
/*     */   public static <E> ImmutableList<E> copyOf(E[] elements) {
/* 289 */     switch (elements.length) {
/*     */       case 0:
/* 291 */         return of();
/*     */       case 1:
/* 293 */         return new SingletonImmutableList(elements[0]);
/*     */     } 
/* 295 */     return new RegularImmutableList(ObjectArrays.checkElementsNotNull((Object[])elements.clone()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 303 */   private static <E> ImmutableList<E> construct(Object... elements) { return asImmutableList(ObjectArrays.checkElementsNotNull(elements)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 312 */   static <E> ImmutableList<E> asImmutableList(Object[] elements) { return asImmutableList(elements, elements.length); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
/* 320 */     switch (length) {
/*     */       case 0:
/* 322 */         return of();
/*     */       
/*     */       case 1:
/* 325 */         return new SingletonImmutableList<E>(elements[0]);
/*     */     } 
/*     */     
/* 328 */     if (length < elements.length) {
/* 329 */       elements = ObjectArrays.arraysCopyOf(elements, length);
/*     */     }
/* 331 */     return new RegularImmutableList(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 340 */   public UnmodifiableIterator<E> iterator() { return listIterator(); }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public UnmodifiableListIterator<E> listIterator() { return listIterator(0); }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 348 */     return new AbstractIndexedListIterator<E>(size(), index)
/*     */       {
/*     */         protected E get(int index) {
/* 351 */           return (E)super.this$0.get(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 358 */   public int indexOf(@Nullable Object object) { return (object == null) ? -1 : Lists.indexOfImpl(this, object); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 363 */   public int lastIndexOf(@Nullable Object object) { return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 368 */   public boolean contains(@Nullable Object object) { return (indexOf(object) >= 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 381 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 382 */     int length = toIndex - fromIndex;
/* 383 */     switch (length) {
/*     */       case 0:
/* 385 */         return of();
/*     */       case 1:
/* 387 */         return of(get(fromIndex));
/*     */     } 
/* 389 */     return subListUnchecked(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 399 */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) { return new SubList(fromIndex, toIndex - fromIndex); }
/*     */   
/*     */   class SubList
/*     */     extends ImmutableList<E> {
/*     */     final int offset;
/*     */     final int length;
/*     */     
/*     */     SubList(int offset, int length) {
/* 407 */       this.offset = offset;
/* 408 */       this.length = length;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 413 */     public int size() { return this.length; }
/*     */ 
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 418 */       Preconditions.checkElementIndex(index, this.length);
/* 419 */       return (E)ImmutableList.this.get(index + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 424 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
/* 425 */       return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 430 */     boolean isPartialView() { return true; }
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
/* 443 */   public final boolean addAll(int index, Collection<? extends E> newElements) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 455 */   public final E set(int index, E element) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 467 */   public final void add(int index, E element) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 479 */   public final E remove(int index) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 488 */   public final ImmutableList<E> asList() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 494 */     int size = size();
/* 495 */     for (int i = 0; i < size; i++) {
/* 496 */       dst[offset + i] = get(i);
/*     */     }
/* 498 */     return offset + size;
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
/* 510 */   public ImmutableList<E> reverse() { return new ReverseImmutableList(this); }
/*     */   
/*     */   private static class ReverseImmutableList<E>
/*     */     extends ImmutableList<E>
/*     */   {
/*     */     private final ImmutableList<E> forwardList;
/*     */     
/* 517 */     ReverseImmutableList(ImmutableList<E> backingList) { this.forwardList = backingList; }
/*     */ 
/*     */ 
/*     */     
/* 521 */     private int reverseIndex(int index) { return size() - 1 - index; }
/*     */ 
/*     */ 
/*     */     
/* 525 */     private int reversePosition(int index) { return size() - index; }
/*     */ 
/*     */ 
/*     */     
/* 529 */     public ImmutableList<E> reverse() { return this.forwardList; }
/*     */ 
/*     */ 
/*     */     
/* 533 */     public boolean contains(@Nullable Object object) { return this.forwardList.contains(object); }
/*     */ 
/*     */     
/*     */     public int indexOf(@Nullable Object object) {
/* 537 */       int index = this.forwardList.lastIndexOf(object);
/* 538 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */     
/*     */     public int lastIndexOf(@Nullable Object object) {
/* 542 */       int index = this.forwardList.indexOf(object);
/* 543 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 547 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 548 */       return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 553 */       Preconditions.checkElementIndex(index, size());
/* 554 */       return (E)this.forwardList.get(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/* 558 */     public int size() { return this.forwardList.size(); }
/*     */ 
/*     */ 
/*     */     
/* 562 */     boolean isPartialView() { return this.forwardList.isPartialView(); }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 567 */   public boolean equals(@Nullable Object obj) { return Lists.equalsImpl(this, obj); }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 571 */     int hashCode = 1;
/* 572 */     int n = size();
/* 573 */     for (int i = 0; i < n; i++) {
/* 574 */       hashCode = 31 * hashCode + get(i).hashCode();
/*     */       
/* 576 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 579 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 589 */     SerializedForm(Object[] elements) { this.elements = elements; }
/*     */ 
/*     */     
/* 592 */     Object readResolve() { return ImmutableList.copyOf(this.elements); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 599 */   private void readObject(ObjectInputStream stream) throws InvalidObjectException { throw new InvalidObjectException("Use SerializedForm"); }
/*     */ 
/*     */ 
/*     */   
/* 603 */   Object writeReplace() { return new SerializedForm(toArray()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 611 */   public static <E> Builder<E> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */     extends ImmutableCollection.ArrayBasedBuilder<E>
/*     */   {
/* 636 */     public Builder() { this(4); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 641 */     Builder(int capacity) { super(capacity); }
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
/* 652 */       super.add(element);
/* 653 */       return this;
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
/* 665 */       super.addAll(elements);
/* 666 */       return this;
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
/* 678 */       super.add(elements);
/* 679 */       return this;
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
/* 691 */       super.addAll(elements);
/* 692 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 700 */     public ImmutableList<E> build() { return ImmutableList.asImmutableList(this.contents, this.size); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */