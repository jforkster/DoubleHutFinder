/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.InvalidObjectException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ abstract class ImmutableAsList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/*    */   abstract ImmutableCollection<E> delegateCollection();
/*    */   
/* 41 */   public boolean contains(Object target) { return delegateCollection().contains(target); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public int size() { return delegateCollection().size(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public boolean isEmpty() { return delegateCollection().isEmpty(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   boolean isPartialView() { return delegateCollection().isPartialView(); }
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/*    */   static class SerializedForm
/*    */     implements Serializable
/*    */   {
/*    */     final ImmutableCollection<?> collection;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/* 66 */     SerializedForm(ImmutableCollection<?> collection) { this.collection = collection; }
/*    */ 
/*    */     
/* 69 */     Object readResolve() { return this.collection.asList(); }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/* 77 */   private void readObject(ObjectInputStream stream) throws InvalidObjectException { throw new InvalidObjectException("Use SerializedForm"); }
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible("serialization")
/* 82 */   Object writeReplace() { return new SerializedForm(delegateCollection()); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */