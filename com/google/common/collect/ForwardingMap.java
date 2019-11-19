/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingMap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Map<K, V>
/*     */ {
/*  70 */   public int size() { return delegate().size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public boolean isEmpty() { return delegate().isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public V remove(Object object) { return (V)delegate().remove(object); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void clear() { delegate().clear(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public boolean containsKey(@Nullable Object key) { return delegate().containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public boolean containsValue(@Nullable Object value) { return delegate().containsValue(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public V get(@Nullable Object key) { return (V)delegate().get(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public V put(K key, V value) { return (V)delegate().put(key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public void putAll(Map<? extends K, ? extends V> map) { delegate().putAll(map); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public Set<K> keySet() { return delegate().keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public Collection<V> values() { return delegate().values(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   public Set<Map.Entry<K, V>> entrySet() { return delegate().entrySet(); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public boolean equals(@Nullable Object object) { return (object == this || delegate().equals(object)); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public int hashCode() { return delegate().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   protected void standardPutAll(Map<? extends K, ? extends V> map) { Maps.putAllImpl(this, map); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected V standardRemove(@Nullable Object key) {
/* 161 */     Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 162 */     while (entryIterator.hasNext()) {
/* 163 */       Map.Entry<K, V> entry = (Map.Entry)entryIterator.next();
/* 164 */       if (Objects.equal(entry.getKey(), key)) {
/* 165 */         V value = (V)entry.getValue();
/* 166 */         entryIterator.remove();
/* 167 */         return value;
/*     */       } 
/*     */     } 
/* 170 */     return null;
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
/* 181 */   protected void standardClear() { Iterators.clear(entrySet().iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.KeySet<K, V>
/*     */   {
/*     */     public StandardKeySet() {
/* 199 */       super(ForwardingMap.this);
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
/*     */   @Beta
/* 212 */   protected boolean standardContainsKey(@Nullable Object key) { return Maps.containsKeyImpl(this, key); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected class StandardValues
/*     */     extends Maps.Values<K, V>
/*     */   {
/*     */     public StandardValues() {
/* 229 */       super(ForwardingMap.this);
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
/* 242 */   protected boolean standardContainsValue(@Nullable Object value) { return Maps.containsValueImpl(this, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected abstract class StandardEntrySet
/*     */     extends Maps.EntrySet<K, V>
/*     */   {
/* 262 */     Map<K, V> map() { return ForwardingMap.this; }
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
/* 274 */   protected boolean standardIsEmpty() { return !entrySet().iterator().hasNext(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 285 */   protected boolean standardEquals(@Nullable Object object) { return Maps.equalsImpl(this, object); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 296 */   protected int standardHashCode() { return Sets.hashCodeImpl(entrySet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 307 */   protected String standardToString() { return Maps.toStringImpl(this); }
/*     */   
/*     */   protected abstract Map<K, V> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */