/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class ImmutableEnumMap<K extends Enum<K>, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   private final EnumMap<K, V> delegate;
/*     */   
/*     */   static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
/*     */     Map.Entry<K, V> entry;
/*  38 */     switch (map.size()) {
/*     */       case 0:
/*  40 */         return ImmutableMap.of();
/*     */       case 1:
/*  42 */         entry = (Map.Entry)Iterables.getOnlyElement(map.entrySet());
/*  43 */         return ImmutableMap.of(entry.getKey(), entry.getValue());
/*     */     } 
/*     */     
/*  46 */     return new ImmutableEnumMap(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableEnumMap(EnumMap<K, V> delegate) {
/*  53 */     this.delegate = delegate;
/*  54 */     Preconditions.checkArgument(!delegate.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/*  59 */     return new ImmutableSet<K>()
/*     */       {
/*     */         public boolean contains(Object object)
/*     */         {
/*  63 */           return super.this$0.delegate.containsKey(object);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  68 */         public int size() { return super.this$0.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  73 */         public UnmodifiableIterator<K> iterator() { return Iterators.unmodifiableIterator(super.this$0.delegate.keySet().iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  78 */         boolean isPartialView() { return true; }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public int size() { return this.delegate.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public boolean containsKey(@Nullable Object key) { return this.delegate.containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public V get(Object key) { return (V)this.delegate.get(key); }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 100 */     return new ImmutableMapEntrySet<K, V>()
/*     */       {
/*     */         ImmutableMap<K, V> map()
/*     */         {
/* 104 */           return super.this$0;
/*     */         }
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 109 */           return new UnmodifiableIterator<Map.Entry<K, V>>()
/*     */             {
/*     */               private final Iterator<Map.Entry<K, V>> backingIterator;
/*     */ 
/*     */               
/* 114 */               public boolean hasNext() { return super.backingIterator.hasNext(); }
/*     */ 
/*     */ 
/*     */               
/*     */               public Map.Entry<K, V> next() {
/* 119 */                 Map.Entry<K, V> entry = (Map.Entry)super.backingIterator.next();
/* 120 */                 return Maps.immutableEntry(entry.getKey(), entry.getValue());
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 129 */   boolean isPartialView() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   Object writeReplace() { return new EnumSerializedForm(this.delegate); }
/*     */ 
/*     */   
/*     */   private static class EnumSerializedForm<K extends Enum<K>, V>
/*     */     extends Object
/*     */     implements Serializable
/*     */   {
/*     */     final EnumMap<K, V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 144 */     EnumSerializedForm(EnumMap<K, V> delegate) { this.delegate = delegate; }
/*     */ 
/*     */     
/* 147 */     Object readResolve() { return new ImmutableEnumMap(this.delegate, null); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableEnumMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */