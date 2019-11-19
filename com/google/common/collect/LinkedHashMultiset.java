/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class LinkedHashMultiset<E>
/*     */   extends AbstractMapBasedMultiset<E>
/*     */ {
/*     */   @GwtIncompatible("not needed in emulated source")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  52 */   public static <E> LinkedHashMultiset<E> create() { return new LinkedHashMultiset(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static <E> LinkedHashMultiset<E> create(int distinctElements) { return new LinkedHashMultiset(distinctElements); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> elements) {
/*  76 */     LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/*     */     
/*  78 */     Iterables.addAll(multiset, elements);
/*  79 */     return multiset;
/*     */   }
/*     */ 
/*     */   
/*  83 */   private LinkedHashMultiset() { super(new LinkedHashMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private LinkedHashMultiset(int distinctElements) { super(new LinkedHashMap(Maps.capacity(distinctElements))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/*  97 */     stream.defaultWriteObject();
/*  98 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 104 */     stream.defaultReadObject();
/* 105 */     int distinctElements = Serialization.readCount(stream);
/* 106 */     setBackingMap(new LinkedHashMap(Maps.capacity(distinctElements)));
/*     */     
/* 108 */     Serialization.populateMultiset(this, stream, distinctElements);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/LinkedHashMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */