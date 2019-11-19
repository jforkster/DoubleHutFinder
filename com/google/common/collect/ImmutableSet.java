/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ImmutableSet<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements Set<E>
/*     */ {
/*     */   static final int MAX_TABLE_SIZE = 1073741824;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.7D;
/*     */   private static final int CUTOFF = 751619276;
/*     */   
/*  84 */   public static <E> ImmutableSet<E> of() { return EmptyImmutableSet.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static <E> ImmutableSet<E> of(E element) { return new SingletonImmutableSet(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static <E> ImmutableSet<E> of(E e1, E e2) { return construct(2, new Object[] { e1, e2 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) { return construct(3, new Object[] { e1, e2, e3 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) { return construct(4, new Object[] { e1, e2, e3, e4 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) { return construct(5, new Object[] { e1, e2, e3, e4, e5 }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
/* 151 */     int paramCount = 6;
/* 152 */     Object[] elements = new Object[6 + others.length];
/* 153 */     elements[0] = e1;
/* 154 */     elements[1] = e2;
/* 155 */     elements[2] = e3;
/* 156 */     elements[3] = e4;
/* 157 */     elements[4] = e5;
/* 158 */     elements[5] = e6;
/* 159 */     System.arraycopy(others, 0, elements, 6, others.length);
/* 160 */     return construct(elements.length, elements);
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
/*     */   private static <E> ImmutableSet<E> construct(int n, Object... elements) {
/*     */     E elem;
/* 179 */     switch (n) {
/*     */       case 0:
/* 181 */         return of();
/*     */       
/*     */       case 1:
/* 184 */         elem = (E)elements[0];
/* 185 */         return of(elem);
/*     */     } 
/*     */ 
/*     */     
/* 189 */     int tableSize = chooseTableSize(n);
/* 190 */     Object[] table = new Object[tableSize];
/* 191 */     int mask = tableSize - 1;
/* 192 */     int hashCode = 0;
/* 193 */     int uniques = 0;
/* 194 */     for (int i = 0; i < n; i++) {
/* 195 */       Object element = ObjectArrays.checkElementNotNull(elements[i], i);
/* 196 */       int hash = element.hashCode();
/* 197 */       for (int j = Hashing.smear(hash);; j++) {
/* 198 */         int index = j & mask;
/* 199 */         Object value = table[index];
/* 200 */         if (value == null) {
/*     */           
/* 202 */           elements[uniques++] = element;
/* 203 */           table[index] = element;
/* 204 */           hashCode += hash; break;
/*     */         } 
/* 206 */         if (value.equals(element)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 211 */     Arrays.fill(elements, uniques, n, null);
/* 212 */     if (uniques == 1) {
/*     */ 
/*     */       
/* 215 */       E element = (E)elements[0];
/* 216 */       return new SingletonImmutableSet(element, hashCode);
/* 217 */     }  if (tableSize != chooseTableSize(uniques))
/*     */     {
/*     */       
/* 220 */       return construct(uniques, elements);
/*     */     }
/* 222 */     Object[] uniqueElements = (uniques < elements.length) ? ObjectArrays.arraysCopyOf(elements, uniques) : elements;
/*     */ 
/*     */     
/* 225 */     return new RegularImmutableSet(uniqueElements, hashCode, table, mask);
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
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/* 249 */     if (setSize < 751619276) {
/*     */       
/* 251 */       int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/* 252 */       while (tableSize * 0.7D < setSize) {
/* 253 */         tableSize <<= 1;
/*     */       }
/* 255 */       return tableSize;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     Preconditions.checkArgument((setSize < 1073741824), "collection too large");
/* 260 */     return 1073741824;
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
/*     */   public static <E> ImmutableSet<E> copyOf(E[] elements) {
/* 272 */     switch (elements.length) {
/*     */       case 0:
/* 274 */         return of();
/*     */       case 1:
/* 276 */         return of(elements[0]);
/*     */     } 
/* 278 */     return construct(elements.length, (Object[])elements.clone());
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
/* 300 */   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) { return (elements instanceof Collection) ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
/* 314 */     if (!elements.hasNext()) {
/* 315 */       return of();
/*     */     }
/* 317 */     E first = (E)elements.next();
/* 318 */     if (!elements.hasNext()) {
/* 319 */       return of(first);
/*     */     }
/* 321 */     return (new Builder()).add(first).addAll(elements).build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
/* 364 */     if (elements instanceof ImmutableSet && !(elements instanceof ImmutableSortedSet)) {
/*     */ 
/*     */       
/* 367 */       ImmutableSet<E> set = (ImmutableSet)elements;
/* 368 */       if (!set.isPartialView()) {
/* 369 */         return set;
/*     */       }
/* 371 */     } else if (elements instanceof EnumSet) {
/* 372 */       return copyOfEnumSet((EnumSet)elements);
/*     */     } 
/* 374 */     Object[] array = elements.toArray();
/* 375 */     return construct(array.length, array);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 380 */   private static <E extends Enum<E>> ImmutableSet<E> copyOfEnumSet(EnumSet<E> enumSet) { return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 387 */   boolean isHashCodeFast() { return false; }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 391 */     if (object == this)
/* 392 */       return true; 
/* 393 */     if (object instanceof ImmutableSet && isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && hashCode() != object.hashCode())
/*     */     {
/*     */ 
/*     */       
/* 397 */       return false;
/*     */     }
/* 399 */     return Sets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/* 403 */   public int hashCode() { return Sets.hashCodeImpl(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 420 */     SerializedForm(Object[] elements) { this.elements = elements; }
/*     */ 
/*     */     
/* 423 */     Object readResolve() { return ImmutableSet.copyOf(this.elements); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 429 */   Object writeReplace() { return new SerializedForm(toArray()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 437 */   public static <E> Builder<E> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableCollection.ArrayBasedBuilder<E>
/*     */   {
/* 463 */     public Builder() { this(4); }
/*     */ 
/*     */ 
/*     */     
/* 467 */     Builder(int capacity) { super(capacity); }
/*     */ 
/*     */ 
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
/* 480 */       super.add(element);
/* 481 */       return this;
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
/*     */     public Builder<E> add(E... elements) {
/* 494 */       super.add(elements);
/* 495 */       return this;
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
/* 508 */       super.addAll(elements);
/* 509 */       return this;
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
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 522 */       super.addAll(elements);
/* 523 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSet<E> build() {
/* 531 */       ImmutableSet<E> result = ImmutableSet.construct(this.size, this.contents);
/*     */ 
/*     */       
/* 534 */       this.size = result.size();
/* 535 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */