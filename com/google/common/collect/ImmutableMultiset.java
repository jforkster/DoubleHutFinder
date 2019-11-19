/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMultiset<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*  55 */   private static final ImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableMap.of(), 0);
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableSet<Multiset.Entry<E>> entrySet;
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static <E> ImmutableMultiset<E> of() { return EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public static <E> ImmutableMultiset<E> of(E element) { return copyOfInternal(new Object[] { element }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static <E> ImmutableMultiset<E> of(E e1, E e2) { return copyOfInternal(new Object[] { e1, e2 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) { return copyOfInternal(new Object[] { e1, e2, e3 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) { return copyOfInternal(new Object[] { e1, e2, e3, e4 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) { return copyOfInternal(new Object[] { e1, e2, e3, e4, e5 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) { return (new Builder()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static <E> ImmutableMultiset<E> copyOf(E[] elements) { return copyOf(Arrays.asList(elements)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 174 */     if (elements instanceof ImmutableMultiset) {
/*     */       
/* 176 */       ImmutableMultiset<E> result = (ImmutableMultiset)elements;
/* 177 */       if (!result.isPartialView()) {
/* 178 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 182 */     Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.cast(elements) : LinkedHashMultiset.create(elements);
/*     */ 
/*     */ 
/*     */     
/* 186 */     return copyOfInternal(multiset);
/*     */   }
/*     */ 
/*     */   
/* 190 */   private static <E> ImmutableMultiset<E> copyOfInternal(E... elements) { return copyOf(Arrays.asList(elements)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) { return copyFromEntries(multiset.entrySet()); }
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
/* 200 */     long size = 0L;
/* 201 */     ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();
/* 202 */     for (Multiset.Entry<? extends E> entry : entries) {
/* 203 */       int count = entry.getCount();
/* 204 */       if (count > 0) {
/*     */ 
/*     */         
/* 207 */         builder.put(entry.getElement(), Integer.valueOf(count));
/* 208 */         size += count;
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     if (size == 0L) {
/* 213 */       return of();
/*     */     }
/* 215 */     return new RegularImmutableMultiset(builder.build(), Ints.saturatedCast(size));
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
/*     */   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 231 */     Multiset<E> multiset = LinkedHashMultiset.create();
/* 232 */     Iterators.addAll(multiset, elements);
/* 233 */     return copyOfInternal(multiset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 239 */     final Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 240 */     return new UnmodifiableIterator<E>()
/*     */       {
/*     */         int remaining;
/*     */         
/*     */         E element;
/*     */         
/* 246 */         public boolean hasNext() { return (super.remaining > 0 || entryIterator.hasNext()); }
/*     */ 
/*     */ 
/*     */         
/*     */         public E next() {
/* 251 */           if (super.remaining <= 0) {
/* 252 */             Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
/* 253 */             super.element = entry.getElement();
/* 254 */             super.remaining = entry.getCount();
/*     */           } 
/* 256 */           super.remaining--;
/* 257 */           return (E)super.element;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 264 */   public boolean contains(@Nullable Object object) { return (count(object) > 0); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 269 */   public boolean containsAll(Collection<?> targets) { return elementSet().containsAll(targets); }
/*     */ 
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
/* 281 */   public final int add(E element, int occurrences) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 293 */   public final int remove(Object element, int occurrences) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 305 */   public final int setCount(E element, int count) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 317 */   public final boolean setCount(E element, int oldCount, int newCount) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("not present in emulated superclass")
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 323 */     for (Multiset.Entry<E> entry : entrySet()) {
/* 324 */       Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
/* 325 */       offset += entry.getCount();
/*     */     } 
/* 327 */     return offset;
/*     */   }
/*     */ 
/*     */   
/* 331 */   public boolean equals(@Nullable Object object) { return Multisets.equalsImpl(this, object); }
/*     */ 
/*     */ 
/*     */   
/* 335 */   public int hashCode() { return Sets.hashCodeImpl(entrySet()); }
/*     */ 
/*     */ 
/*     */   
/* 339 */   public String toString() { return entrySet().toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Multiset.Entry<E>> entrySet() {
/* 346 */     ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
/* 347 */     return (es == null) ? (this.entrySet = createEntrySet()) : es;
/*     */   }
/*     */ 
/*     */   
/* 351 */   private final ImmutableSet<Multiset.Entry<E>> createEntrySet() { return isEmpty() ? ImmutableSet.of() : new EntrySet(null); }
/*     */   
/*     */   private final class EntrySet
/*     */     extends ImmutableSet<Multiset.Entry<E>> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private EntrySet() {}
/*     */     
/* 359 */     boolean isPartialView() { return ImmutableMultiset.this.isPartialView(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 364 */     public UnmodifiableIterator<Multiset.Entry<E>> iterator() { return asList().iterator(); }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableList<Multiset.Entry<E>> createAsList() {
/* 369 */       return new ImmutableAsList<Multiset.Entry<E>>()
/*     */         {
/*     */           public Multiset.Entry<E> get(int index) {
/* 372 */             return ImmutableMultiset.EntrySet.this.this$0.getEntry(index);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 377 */           ImmutableCollection<Multiset.Entry<E>> delegateCollection() { return super.this$1; }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 384 */     public int size() { return ImmutableMultiset.this.elementSet().size(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 389 */       if (o instanceof Multiset.Entry) {
/* 390 */         Multiset.Entry<?> entry = (Multiset.Entry)o;
/* 391 */         if (entry.getCount() <= 0) {
/* 392 */           return false;
/*     */         }
/* 394 */         int count = ImmutableMultiset.this.count(entry.getElement());
/* 395 */         return (count == entry.getCount());
/*     */       } 
/* 397 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 402 */     public int hashCode() { return ImmutableMultiset.this.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 409 */     Object writeReplace() { return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this); }
/*     */   }
/*     */ 
/*     */   
/*     */   static class EntrySetSerializedForm<E>
/*     */     extends Object
/*     */     implements Serializable
/*     */   {
/*     */     final ImmutableMultiset<E> multiset;
/*     */     
/* 419 */     EntrySetSerializedForm(ImmutableMultiset<E> multiset) { this.multiset = multiset; }
/*     */ 
/*     */ 
/*     */     
/* 423 */     Object readResolve() { return this.multiset.entrySet(); }
/*     */   }
/*     */   
/*     */   private static class SerializedForm implements Serializable {
/*     */     final Object[] elements;
/*     */     final int[] counts;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Multiset<?> multiset) {
/* 432 */       int distinct = multiset.entrySet().size();
/* 433 */       this.elements = new Object[distinct];
/* 434 */       this.counts = new int[distinct];
/* 435 */       int i = 0;
/* 436 */       for (Multiset.Entry<?> entry : multiset.entrySet()) {
/* 437 */         this.elements[i] = entry.getElement();
/* 438 */         this.counts[i] = entry.getCount();
/* 439 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 444 */       LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);
/*     */       
/* 446 */       for (int i = 0; i < this.elements.length; i++) {
/* 447 */         multiset.add(this.elements[i], this.counts[i]);
/*     */       }
/* 449 */       return ImmutableMultiset.copyOf(multiset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 458 */   Object writeReplace() { return new SerializedForm(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 466 */   public static <E> Builder<E> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset.Entry<E> getEntry(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.Builder<E>
/*     */   {
/*     */     final Multiset<E> contents;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 495 */     public Builder() { this(LinkedHashMultiset.create()); }
/*     */ 
/*     */ 
/*     */     
/* 499 */     Builder(Multiset<E> contents) { this.contents = contents; }
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
/* 510 */       this.contents.add(Preconditions.checkNotNull(element));
/* 511 */       return this;
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
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 528 */       this.contents.add(Preconditions.checkNotNull(element), occurrences);
/* 529 */       return this;
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
/*     */     public Builder<E> setCount(E element, int count) {
/* 543 */       this.contents.setCount(Preconditions.checkNotNull(element), count);
/* 544 */       return this;
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
/* 556 */       super.add(elements);
/* 557 */       return this;
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 570 */       if (elements instanceof Multiset) {
/* 571 */         Multiset<? extends E> multiset = Multisets.cast(elements);
/* 572 */         for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
/* 573 */           addCopies(entry.getElement(), entry.getCount());
/*     */         }
/*     */       } else {
/* 576 */         super.addAll(elements);
/*     */       } 
/* 578 */       return this;
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
/* 590 */       super.addAll(elements);
/* 591 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 599 */     public ImmutableMultiset<E> build() { return ImmutableMultiset.copyOf(this.contents); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */