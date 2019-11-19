/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMultimap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Multimap<K, V>
/*     */ {
/*  48 */   public Map<K, Collection<V>> asMap() { return delegate().asMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public void clear() { delegate().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) { return delegate().containsEntry(key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public boolean containsKey(@Nullable Object key) { return delegate().containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public boolean containsValue(@Nullable Object value) { return delegate().containsValue(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public Collection<Map.Entry<K, V>> entries() { return delegate().entries(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public Collection<V> get(@Nullable K key) { return delegate().get(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   public boolean isEmpty() { return delegate().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public Multiset<K> keys() { return delegate().keys(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public Set<K> keySet() { return delegate().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public boolean put(K key, V value) { return delegate().put(key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean putAll(K key, Iterable<? extends V> values) { return delegate().putAll(key, values); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) { return delegate().putAll(multimap); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public boolean remove(@Nullable Object key, @Nullable Object value) { return delegate().remove(key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public Collection<V> removeAll(@Nullable Object key) { return delegate().removeAll(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public Collection<V> replaceValues(K key, Iterable<? extends V> values) { return delegate().replaceValues(key, values); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public int size() { return delegate().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public Collection<V> values() { return delegate().values(); }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public boolean equals(@Nullable Object object) { return (object == this || delegate().equals(object)); }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public int hashCode() { return delegate().hashCode(); }
/*     */   
/*     */   protected abstract Multimap<K, V> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */