/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingMap;
/*     */ import com.google.common.collect.ForwardingMapEntry;
/*     */ import com.google.common.collect.ForwardingSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ @Beta
/*     */ public final class MutableTypeToInstanceMap<B>
/*     */   extends ForwardingMap<TypeToken<? extends B>, B>
/*     */   implements TypeToInstanceMap<B>
/*     */ {
/*  46 */   private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  51 */   public <T extends B> T getInstance(Class<T> type) { return (T)trustedGet(TypeToken.of(type)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  57 */   public <T extends B> T putInstance(Class<T> type, @Nullable T value) { return (T)trustedPut(TypeToken.of(type), value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  63 */   public <T extends B> T getInstance(TypeToken<T> type) { return (T)trustedGet(type.rejectTypeVariables()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  69 */   public <T extends B> T putInstance(TypeToken<T> type, @Nullable T value) { return (T)trustedPut(type.rejectTypeVariables(), value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public B put(TypeToken<? extends B> key, B value) { throw new UnsupportedOperationException("Please use putInstance() instead."); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) { throw new UnsupportedOperationException("Please use putInstance() instead."); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet() { return UnmodifiableEntry.transformEntries(super.entrySet()); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected Map<TypeToken<? extends B>, B> delegate() { return this.backingMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  93 */   private <T extends B> T trustedPut(TypeToken<T> type, @Nullable T value) { return (T)this.backingMap.put(type, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*  99 */   private <T extends B> T trustedGet(TypeToken<T> type) { return (T)this.backingMap.get(type); }
/*     */   
/*     */   private static final class UnmodifiableEntry<K, V>
/*     */     extends ForwardingMapEntry<K, V>
/*     */   {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     static <K, V> Set<Map.Entry<K, V>> transformEntries(final Set<Map.Entry<K, V>> entries) {
/* 107 */       return new ForwardingSet<Map.Entry<K, V>>()
/*     */         {
/* 109 */           protected Set<Map.Entry<K, V>> delegate() { return entries; }
/*     */ 
/*     */           
/* 112 */           public Iterator<Map.Entry<K, V>> iterator() { return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.iterator()); }
/*     */ 
/*     */           
/* 115 */           public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */           
/* 118 */           public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     private static <K, V> Iterator<Map.Entry<K, V>> transformEntries(Iterator<Map.Entry<K, V>> entries) {
/* 124 */       return Iterators.transform(entries, new Function<Map.Entry<K, V>, Map.Entry<K, V>>()
/*     */           {
/* 126 */             public Map.Entry<K, V> apply(Map.Entry<K, V> entry) { return new MutableTypeToInstanceMap.UnmodifiableEntry(entry, null); }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     private UnmodifiableEntry(Map.Entry<K, V> delegate) { this.delegate = (Map.Entry)Preconditions.checkNotNull(delegate); }
/*     */ 
/*     */ 
/*     */     
/* 136 */     protected Map.Entry<K, V> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */     
/* 140 */     public V setValue(V value) { throw new UnsupportedOperationException(); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/MutableTypeToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */