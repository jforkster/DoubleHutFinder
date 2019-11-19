/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private Map<K, V> delegate;
/*     */   AbstractBiMap<V, K> inverse;
/*     */   private Set<K> keySet;
/*     */   private Set<V> valueSet;
/*     */   private Set<Map.Entry<K, V>> entrySet;
/*     */   @GwtIncompatible("Not needed in emulated source.")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  57 */   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) { setDelegates(forward, backward); }
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/*  62 */     this.delegate = backward;
/*  63 */     this.inverse = forward;
/*     */   }
/*     */ 
/*     */   
/*  67 */   protected Map<K, V> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   K checkKey(@Nullable K key) { return key; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   V checkValue(@Nullable V value) { return value; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/*  89 */     Preconditions.checkState((this.delegate == null));
/*  90 */     Preconditions.checkState((this.inverse == null));
/*  91 */     Preconditions.checkArgument(forward.isEmpty());
/*  92 */     Preconditions.checkArgument(backward.isEmpty());
/*  93 */     Preconditions.checkArgument((forward != backward));
/*  94 */     this.delegate = forward;
/*  95 */     this.inverse = new Inverse(backward, this, null);
/*     */   }
/*     */ 
/*     */   
/*  99 */   void setInverse(AbstractBiMap<V, K> inverse) { this.inverse = inverse; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public boolean containsValue(@Nullable Object value) { return this.inverse.containsKey(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public V put(@Nullable K key, @Nullable V value) { return (V)putInBothMaps(key, value, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public V forcePut(@Nullable K key, @Nullable V value) { return (V)putInBothMaps(key, value, true); }
/*     */ 
/*     */   
/*     */   private V putInBothMaps(@Nullable K key, @Nullable V value, boolean force) {
/* 120 */     checkKey(key);
/* 121 */     checkValue(value);
/* 122 */     boolean containedKey = containsKey(key);
/* 123 */     if (containedKey && Objects.equal(value, get(key))) {
/* 124 */       return value;
/*     */     }
/* 126 */     if (force) {
/* 127 */       inverse().remove(value);
/*     */     } else {
/* 129 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", new Object[] { value });
/*     */     } 
/* 131 */     V oldValue = (V)this.delegate.put(key, value);
/* 132 */     updateInverseMap(key, containedKey, oldValue, value);
/* 133 */     return oldValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
/* 138 */     if (containedKey) {
/* 139 */       removeFromInverseMap(oldValue);
/*     */     }
/* 141 */     this.inverse.delegate.put(newValue, key);
/*     */   }
/*     */ 
/*     */   
/* 145 */   public V remove(@Nullable Object key) { return (V)(containsKey(key) ? removeFromBothMaps(key) : null); }
/*     */ 
/*     */   
/*     */   private V removeFromBothMaps(Object key) {
/* 149 */     V oldValue = (V)this.delegate.remove(key);
/* 150 */     removeFromInverseMap(oldValue);
/* 151 */     return oldValue;
/*     */   }
/*     */ 
/*     */   
/* 155 */   private void removeFromInverseMap(V oldValue) { this.inverse.delegate.remove(oldValue); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 161 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 162 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 167 */     this.delegate.clear();
/* 168 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public BiMap<V, K> inverse() { return this.inverse; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 181 */     Set<K> result = this.keySet;
/* 182 */     return (result == null) ? (this.keySet = new KeySet(null)) : result;
/*     */   }
/*     */   
/*     */   private class KeySet
/*     */     extends ForwardingSet<K> {
/* 187 */     protected Set<K> delegate() { return AbstractBiMap.this.delegate.keySet(); }
/*     */     
/*     */     private KeySet() {}
/*     */     
/* 191 */     public void clear() { AbstractBiMap.this.clear(); }
/*     */ 
/*     */     
/*     */     public boolean remove(Object key) {
/* 195 */       if (!contains(key)) {
/* 196 */         return false;
/*     */       }
/* 198 */       AbstractBiMap.this.removeFromBothMaps(key);
/* 199 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 203 */     public boolean removeAll(Collection<?> keysToRemove) { return standardRemoveAll(keysToRemove); }
/*     */ 
/*     */ 
/*     */     
/* 207 */     public boolean retainAll(Collection<?> keysToRetain) { return standardRetainAll(keysToRetain); }
/*     */ 
/*     */ 
/*     */     
/* 211 */     public Iterator<K> iterator() { return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator()); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 222 */     Set<V> result = this.valueSet;
/* 223 */     return (result == null) ? (this.valueSet = new ValueSet(null)) : result;
/*     */   }
/*     */   
/*     */   private class ValueSet extends ForwardingSet<V> {
/* 227 */     final Set<V> valuesDelegate = AbstractBiMap.this.inverse.keySet();
/*     */ 
/*     */     
/* 230 */     protected Set<V> delegate() { return this.valuesDelegate; }
/*     */ 
/*     */ 
/*     */     
/* 234 */     public Iterator<V> iterator() { return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator()); }
/*     */ 
/*     */ 
/*     */     
/* 238 */     public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */ 
/*     */     
/* 242 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */ 
/*     */ 
/*     */     
/* 246 */     public String toString() { return standardToString(); }
/*     */ 
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 253 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 254 */     return (result == null) ? (this.entrySet = new EntrySet(null)) : result;
/*     */   }
/*     */   
/*     */   private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/* 258 */     final Set<Map.Entry<K, V>> esDelegate = AbstractBiMap.this.delegate.entrySet();
/*     */ 
/*     */     
/* 261 */     protected Set<Map.Entry<K, V>> delegate() { return this.esDelegate; }
/*     */ 
/*     */ 
/*     */     
/* 265 */     public void clear() { AbstractBiMap.this.clear(); }
/*     */ 
/*     */     
/*     */     public boolean remove(Object object) {
/* 269 */       if (!this.esDelegate.contains(object)) {
/* 270 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 274 */       Map.Entry<?, ?> entry = (Map.Entry)object;
/* 275 */       AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 281 */       this.esDelegate.remove(entry);
/* 282 */       return true;
/*     */     }
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 286 */       final Iterator<Map.Entry<K, V>> iterator = this.esDelegate.iterator();
/* 287 */       return new Iterator<Map.Entry<K, V>>()
/*     */         {
/*     */           Map.Entry<K, V> entry;
/*     */           
/* 291 */           public boolean hasNext() { return iterator.hasNext(); }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, V> next() {
/* 295 */             super.entry = (Map.Entry)iterator.next();
/* 296 */             final Map.Entry<K, V> finalEntry = super.entry;
/*     */             
/* 298 */             return new ForwardingMapEntry<K, V>()
/*     */               {
/* 300 */                 protected Map.Entry<K, V> delegate() { return finalEntry; }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public V setValue(V value) {
/* 305 */                   Preconditions.checkState(AbstractBiMap.EntrySet.null.this.this$1.contains(this), "entry no longer in map");
/*     */                   
/* 307 */                   if (Objects.equal(value, getValue())) {
/* 308 */                     return value;
/*     */                   }
/* 310 */                   Preconditions.checkArgument(!AbstractBiMap.EntrySet.this.this$0.containsValue(value), "value already present: %s", new Object[] { value });
/*     */                   
/* 312 */                   V oldValue = (V)finalEntry.setValue(value);
/* 313 */                   Preconditions.checkState(Objects.equal(value, AbstractBiMap.EntrySet.this.this$0.get(getKey())), "entry no longer in map");
/*     */                   
/* 315 */                   AbstractBiMap.EntrySet.this.this$0.updateInverseMap(getKey(), true, oldValue, value);
/* 316 */                   return oldValue;
/*     */                 }
/*     */               };
/*     */           }
/*     */           
/*     */           public void remove() {
/* 322 */             CollectPreconditions.checkRemove((super.entry != null));
/* 323 */             V value = (V)super.entry.getValue();
/* 324 */             iterator.remove();
/* 325 */             AbstractBiMap.EntrySet.this.this$0.removeFromInverseMap(value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 333 */     public Object[] toArray() { return standardToArray(); }
/*     */ 
/*     */     
/* 336 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*     */ 
/*     */     
/* 339 */     public boolean contains(Object o) { return Maps.containsEntryImpl(delegate(), o); }
/*     */ 
/*     */     
/* 342 */     public boolean containsAll(Collection<?> c) { return standardContainsAll(c); }
/*     */ 
/*     */     
/* 345 */     public boolean removeAll(Collection<?> c) { return standardRemoveAll(c); }
/*     */ 
/*     */     
/* 348 */     public boolean retainAll(Collection<?> c) { return standardRetainAll(c); }
/*     */     
/*     */     private EntrySet() {}
/*     */   }
/*     */   
/*     */   private static class Inverse<K, V>
/*     */     extends AbstractBiMap<K, V> {
/* 355 */     private Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) { super(backward, forward, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("Not needed in emulated source.")
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     K checkKey(K key) { return (K)this.inverse.checkValue(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     V checkValue(V value) { return (V)this.inverse.checkKey(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("java.io.ObjectOuputStream")
/*     */     private void writeObject(ObjectOutputStream stream) throws IOException {
/* 382 */       stream.defaultWriteObject();
/* 383 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("java.io.ObjectInputStream")
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 390 */       stream.defaultReadObject();
/* 391 */       setInverse((AbstractBiMap)stream.readObject());
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("Not needed in the emulated source.")
/* 396 */     Object readResolve() { return inverse().inverse(); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/AbstractBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */