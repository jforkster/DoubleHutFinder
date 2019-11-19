/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableCollection<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Serializable
/*     */ {
/*     */   private ImmutableList<E> asList;
/*     */   
/*     */   public final Object[] toArray() {
/*  60 */     int size = size();
/*  61 */     if (size == 0) {
/*  62 */       return ObjectArrays.EMPTY_ARRAY;
/*     */     }
/*  64 */     Object[] result = new Object[size()];
/*  65 */     copyIntoArray(result, 0);
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> T[] toArray(T[] other) {
/*  71 */     Preconditions.checkNotNull(other);
/*  72 */     int size = size();
/*  73 */     if (other.length < size) {
/*  74 */       other = (T[])ObjectArrays.newArray(other, size);
/*  75 */     } else if (other.length > size) {
/*  76 */       other[size] = null;
/*     */     } 
/*  78 */     copyIntoArray(other, 0);
/*  79 */     return other;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean contains(@Nullable Object object) { return (object != null && super.contains(object)); }
/*     */ 
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
/*  96 */   public final boolean add(E e) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 108 */   public final boolean remove(Object object) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 120 */   public final boolean addAll(Collection<? extends E> newElements) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 132 */   public final boolean removeAll(Collection<?> oldElements) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 144 */   public final boolean retainAll(Collection<?> elementsToKeep) { throw new UnsupportedOperationException(); }
/*     */ 
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
/* 156 */   public final void clear() { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<E> asList() {
/* 171 */     ImmutableList<E> list = this.asList;
/* 172 */     return (list == null) ? (this.asList = createAsList()) : list;
/*     */   }
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 176 */     switch (size()) {
/*     */       case 0:
/* 178 */         return ImmutableList.of();
/*     */       case 1:
/* 180 */         return ImmutableList.of(iterator().next());
/*     */     } 
/* 182 */     return new RegularImmutableAsList(this, toArray());
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
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 199 */     for (E e : this) {
/* 200 */       dst[offset++] = e;
/*     */     }
/* 202 */     return offset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 207 */   Object writeReplace() { return new ImmutableList.SerializedForm(toArray()); }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   abstract boolean isPartialView();
/*     */   
/*     */   public static abstract class Builder<E>
/*     */     extends Object
/*     */   {
/*     */     static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */     
/*     */     static int expandedCapacity(int oldCapacity, int minCapacity) {
/* 219 */       if (minCapacity < 0) {
/* 220 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 223 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 224 */       if (newCapacity < minCapacity) {
/* 225 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 227 */       if (newCapacity < 0) {
/* 228 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/*     */       
/* 231 */       return newCapacity;
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
/*     */     public abstract Builder<E> add(E param1E);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 262 */       for (E element : elements) {
/* 263 */         add(element);
/*     */       }
/* 265 */       return this;
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
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 281 */       for (E element : elements) {
/* 282 */         add(element);
/*     */       }
/* 284 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 300 */       while (elements.hasNext()) {
/* 301 */         add(elements.next());
/*     */       }
/* 303 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract ImmutableCollection<E> build();
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class ArrayBasedBuilder<E>
/*     */     extends Builder<E>
/*     */   {
/*     */     Object[] contents;
/*     */     
/*     */     int size;
/*     */ 
/*     */     
/*     */     ArrayBasedBuilder(int initialCapacity) {
/* 321 */       CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
/* 322 */       this.contents = new Object[initialCapacity];
/* 323 */       this.size = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 331 */       if (this.contents.length < minCapacity) {
/* 332 */         this.contents = ObjectArrays.arraysCopyOf(this.contents, expandedCapacity(this.contents.length, minCapacity));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ArrayBasedBuilder<E> add(E element) {
/* 339 */       Preconditions.checkNotNull(element);
/* 340 */       ensureCapacity(this.size + 1);
/* 341 */       this.contents[this.size++] = element;
/* 342 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableCollection.Builder<E> add(E... elements) {
/* 347 */       ObjectArrays.checkElementsNotNull(elements);
/* 348 */       ensureCapacity(this.size + elements.length);
/* 349 */       System.arraycopy(elements, 0, this.contents, this.size, elements.length);
/* 350 */       this.size += elements.length;
/* 351 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableCollection.Builder<E> addAll(Iterable<? extends E> elements) {
/* 356 */       if (elements instanceof Collection) {
/* 357 */         Collection<?> collection = (Collection)elements;
/* 358 */         ensureCapacity(this.size + collection.size());
/*     */       } 
/* 360 */       super.addAll(elements);
/* 361 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */