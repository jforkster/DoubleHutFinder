/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ public final class HashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   @GwtIncompatible("Not needed in emulated source.")
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 42 */   public static <E> HashMultiset<E> create() { return new HashMultiset(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public static <E> HashMultiset<E> create(int distinctElements) { return new HashMultiset(distinctElements); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E> HashMultiset<E> create(Iterable<? extends E> elements) {
/* 65 */     HashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/*    */     
/* 67 */     Iterables.addAll(multiset, elements);
/* 68 */     return multiset;
/*    */   }
/*    */ 
/*    */   
/* 72 */   private HashMultiset() { super(new HashMap()); }
/*    */ 
/*    */ 
/*    */   
/* 76 */   private HashMultiset(int distinctElements) { super(Maps.newHashMapWithExpectedSize(distinctElements)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("java.io.ObjectOutputStream")
/*    */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 85 */     stream.defaultWriteObject();
/* 86 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible("java.io.ObjectInputStream")
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 92 */     stream.defaultReadObject();
/* 93 */     int distinctElements = Serialization.readCount(stream);
/* 94 */     setBackingMap(Maps.newHashMapWithExpectedSize(distinctElements));
/*    */     
/* 96 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/HashMultiset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */