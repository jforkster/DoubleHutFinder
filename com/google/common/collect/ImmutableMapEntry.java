/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtIncompatible("unnecessary")
/*    */ abstract class ImmutableMapEntry<K, V>
/*    */   extends ImmutableEntry<K, V>
/*    */ {
/*    */   ImmutableMapEntry(K key, V value) {
/* 36 */     super(key, value);
/* 37 */     CollectPreconditions.checkEntryNotNull(key, value);
/*    */   }
/*    */ 
/*    */   
/* 41 */   ImmutableMapEntry(ImmutableMapEntry<K, V> contents) { super(contents.getKey(), contents.getValue()); }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   abstract ImmutableMapEntry<K, V> getNextInKeyBucket();
/*    */   
/*    */   @Nullable
/*    */   abstract ImmutableMapEntry<K, V> getNextInValueBucket();
/*    */   
/*    */   static final class TerminalEntry<K, V>
/*    */     extends ImmutableMapEntry<K, V>
/*    */   {
/* 53 */     TerminalEntry(ImmutableMapEntry<K, V> contents) { super(contents); }
/*    */ 
/*    */ 
/*    */     
/* 57 */     TerminalEntry(K key, V value) { super(key, value); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     @Nullable
/* 63 */     ImmutableMapEntry<K, V> getNextInKeyBucket() { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     @Nullable
/* 69 */     ImmutableMapEntry<K, V> getNextInValueBucket() { return null; }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMapEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */