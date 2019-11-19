/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*  50 */   public static <K, V> ImmutableBiMap<K, V> of() { return EmptyImmutableBiMap.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) { return new SingletonImmutableBiMap(k1, v1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) { return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) { return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) { return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) { return new RegularImmutableBiMap(new ImmutableMapEntry.TerminalEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static <K, V> Builder<K, V> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder<K, V> put(K key, V value) {
/* 144 */       super.put(key, value);
/* 145 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 156 */       super.putAll(map);
/* 157 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> build() {
/* 166 */       switch (this.size) {
/*     */         case 0:
/* 168 */           return ImmutableBiMap.of();
/*     */         case 1:
/* 170 */           return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 172 */       return new RegularImmutableBiMap(this.size, this.entries);
/*     */     }
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/*     */     Map.Entry<K, V> entry;
/* 192 */     if (map instanceof ImmutableBiMap) {
/*     */       
/* 194 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/*     */ 
/*     */       
/* 197 */       if (!bimap.isPartialView()) {
/* 198 */         return bimap;
/*     */       }
/*     */     } 
/* 201 */     Entry[] entries = (Entry[])map.entrySet().toArray(EMPTY_ENTRY_ARRAY);
/* 202 */     switch (entries.length) {
/*     */       case 0:
/* 204 */         return of();
/*     */       
/*     */       case 1:
/* 207 */         entry = entries[0];
/* 208 */         return of(entry.getKey(), entry.getValue());
/*     */     } 
/* 210 */     return new RegularImmutableBiMap(entries);
/*     */   }
/*     */ 
/*     */   
/* 214 */   private static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Map.Entry[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 232 */   public ImmutableSet<V> values() { return inverse().keySet(); }
/*     */ 
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
/* 244 */   public V forcePut(K key, V value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     SerializedForm(ImmutableBiMap<?, ?> bimap) { super(bimap); }
/*     */     
/*     */     Object readResolve() {
/* 261 */       ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<Object, Object>();
/* 262 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 268 */   Object writeReplace() { return new SerializedForm(this); }
/*     */   
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */