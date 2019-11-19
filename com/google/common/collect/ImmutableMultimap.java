/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   final ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final int size;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  70 */   public static <K, V> ImmutableMultimap<K, V> of() { return ImmutableListMultimap.of(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) { return ImmutableListMultimap.of(k1, v1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) { return ImmutableListMultimap.of(k1, v1, k2, v2); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) { return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) { return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) { return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static <K, V> Builder<K, V> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class BuilderMultimap<K, V>
/*     */     extends AbstractMapBasedMultimap<K, V>
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/* 128 */     BuilderMultimap() { super(new LinkedHashMap()); }
/*     */ 
/*     */     
/* 131 */     Collection<V> createCollection() { return Lists.newArrayList(); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<K, V>
/*     */     extends Object
/*     */   {
/* 155 */     Multimap<K, V> builderMultimap = new ImmutableMultimap.BuilderMultimap();
/*     */ 
/*     */ 
/*     */     
/*     */     Comparator<? super K> keyComparator;
/*     */ 
/*     */ 
/*     */     
/*     */     Comparator<? super V> valueComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> put(K key, V value) {
/* 169 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 170 */       this.builderMultimap.put(key, value);
/* 171 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) { return put(entry.getKey(), entry.getValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 191 */       if (key == null) {
/* 192 */         throw new NullPointerException("null key in entry: null=" + Iterables.toString(values));
/*     */       }
/*     */       
/* 195 */       Collection<V> valueList = this.builderMultimap.get(key);
/* 196 */       for (V value : values) {
/* 197 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 198 */         valueList.add(value);
/*     */       } 
/* 200 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     public Builder<K, V> putAll(K key, V... values) { return putAll(key, Arrays.asList(values)); }
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
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 224 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : multimap.asMap().entrySet()) {
/* 225 */         putAll(entry.getKey(), (Iterable)entry.getValue());
/*     */       }
/* 227 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 236 */       this.keyComparator = (Comparator)Preconditions.checkNotNull(keyComparator);
/* 237 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 246 */       this.valueComparator = (Comparator)Preconditions.checkNotNull(valueComparator);
/* 247 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 254 */       if (this.valueComparator != null) {
/* 255 */         for (Collection<V> values : this.builderMultimap.asMap().values()) {
/* 256 */           List<V> list = (List)values;
/* 257 */           Collections.sort(list, this.valueComparator);
/*     */         } 
/*     */       }
/* 260 */       if (this.keyComparator != null) {
/* 261 */         Multimap<K, V> sortedCopy = new ImmutableMultimap.BuilderMultimap<K, V>();
/* 262 */         List<Map.Entry<K, Collection<V>>> entries = Lists.newArrayList(this.builderMultimap.asMap().entrySet());
/*     */         
/* 264 */         Collections.sort(entries, Ordering.from(this.keyComparator).onKeys());
/*     */ 
/*     */         
/* 267 */         for (Map.Entry<K, Collection<V>> entry : entries) {
/* 268 */           sortedCopy.putAll(entry.getKey(), (Iterable)entry.getValue());
/*     */         }
/* 270 */         this.builderMultimap = sortedCopy;
/*     */       } 
/* 272 */       return ImmutableMultimap.copyOf(this.builderMultimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 290 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 292 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/*     */       
/* 294 */       if (!kvMultimap.isPartialView()) {
/* 295 */         return kvMultimap;
/*     */       }
/*     */     } 
/* 298 */     return ImmutableListMultimap.copyOf(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java serialization is not supported")
/*     */   static class FieldSettersHolder
/*     */   {
/* 310 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */ 
/*     */     
/* 313 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */ 
/*     */     
/* 316 */     static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 322 */     this.map = map;
/* 323 */     this.size = size;
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
/*     */   @Deprecated
/* 337 */   public ImmutableCollection<V> removeAll(Object key) { throw new UnsupportedOperationException(); }
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
/*     */   @Deprecated
/* 350 */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
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
/* 362 */   public void clear() { throw new UnsupportedOperationException(); }
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
/*     */   @Deprecated
/* 392 */   public boolean put(K key, V value) { throw new UnsupportedOperationException(); }
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
/* 404 */   public boolean putAll(K key, Iterable<? extends V> values) { throw new UnsupportedOperationException(); }
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
/* 416 */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) { throw new UnsupportedOperationException(); }
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
/* 428 */   public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 438 */   boolean isPartialView() { return this.map.isPartialView(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 445 */   public boolean containsKey(@Nullable Object key) { return this.map.containsKey(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 450 */   public boolean containsValue(@Nullable Object value) { return (value != null && super.containsValue(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 455 */   public int size() { return this.size; }
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
/* 467 */   public ImmutableSet<K> keySet() { return this.map.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 477 */   public ImmutableMap<K, Collection<V>> asMap() { return this.map; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 482 */   Map<K, Collection<V>> createAsMap() { throw new AssertionError("should never be called"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 492 */   public ImmutableCollection<Map.Entry<K, V>> entries() { return (ImmutableCollection)super.entries(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 497 */   ImmutableCollection<Map.Entry<K, V>> createEntries() { return new EntryCollection(this); }
/*     */   
/*     */   private static class EntryCollection<K, V>
/*     */     extends ImmutableCollection<Map.Entry<K, V>>
/*     */   {
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 505 */     EntryCollection(ImmutableMultimap<K, V> multimap) { this.multimap = multimap; }
/*     */ 
/*     */ 
/*     */     
/* 509 */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() { return this.multimap.entryIterator(); }
/*     */ 
/*     */ 
/*     */     
/* 513 */     boolean isPartialView() { return this.multimap.isPartialView(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 518 */     public int size() { return this.multimap.size(); }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 522 */       if (object instanceof Map.Entry) {
/* 523 */         Map.Entry<?, ?> entry = (Map.Entry)object;
/* 524 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 526 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class Itr<T>
/*     */     extends UnmodifiableIterator<T>
/*     */   {
/* 533 */     final Iterator<Map.Entry<K, Collection<V>>> mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
/* 534 */     K key = null;
/* 535 */     Iterator<V> valueIterator = Iterators.emptyIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     public boolean hasNext() { return (this.mapIterator.hasNext() || this.valueIterator.hasNext()); }
/*     */     
/*     */     private Itr() {}
/*     */     
/*     */     public T next() {
/* 546 */       if (!this.valueIterator.hasNext()) {
/* 547 */         Map.Entry<K, Collection<V>> mapEntry = (Map.Entry)this.mapIterator.next();
/* 548 */         this.key = mapEntry.getKey();
/* 549 */         this.valueIterator = ((Collection)mapEntry.getValue()).iterator();
/*     */       } 
/* 551 */       return (T)output(this.key, this.valueIterator.next());
/*     */     }
/*     */     
/*     */     abstract T output(K param1K, V param1V); }
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 557 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(K key, V value) {
/* 560 */           return Maps.immutableEntry(key, value);
/*     */         }
/*     */       };
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
/* 573 */   public ImmutableMultiset<K> keys() { return (ImmutableMultiset)super.keys(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 578 */   ImmutableMultiset<K> createKeys() { return new Keys(); }
/*     */ 
/*     */ 
/*     */   
/*     */   class Keys
/*     */     extends ImmutableMultiset<K>
/*     */   {
/* 585 */     public boolean contains(@Nullable Object object) { return ImmutableMultimap.this.containsKey(object); }
/*     */ 
/*     */ 
/*     */     
/*     */     public int count(@Nullable Object element) {
/* 590 */       Collection<V> values = (Collection)ImmutableMultimap.this.map.get(element);
/* 591 */       return (values == null) ? 0 : values.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 596 */     public Set<K> elementSet() { return ImmutableMultimap.this.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 601 */     public int size() { return ImmutableMultimap.this.size(); }
/*     */ 
/*     */ 
/*     */     
/*     */     Multiset.Entry<K> getEntry(int index) {
/* 606 */       Map.Entry<K, ? extends Collection<V>> entry = (Map.Entry)ImmutableMultimap.this.map.entrySet().asList().get(index);
/* 607 */       return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 612 */     boolean isPartialView() { return true; }
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
/* 623 */   public ImmutableCollection<V> values() { return (ImmutableCollection)super.values(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 628 */   ImmutableCollection<V> createValues() { return new Values(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<V> valueIterator() {
/* 633 */     return new Itr<V>()
/*     */       {
/*     */         V output(K key, V value) {
/* 636 */           return value; }
/*     */       };
/*     */   }
/*     */   
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   
/*     */   public abstract ImmutableMultimap<V, K> inverse();
/*     */   
/*     */   private static final class Values<K, V> extends ImmutableCollection<V> {
/* 645 */     Values(ImmutableMultimap<K, V> multimap) { this.multimap = multimap; }
/*     */     
/*     */     private final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 650 */     public boolean contains(@Nullable Object object) { return this.multimap.containsValue(object); }
/*     */ 
/*     */ 
/*     */     
/* 654 */     public UnmodifiableIterator<V> iterator() { return this.multimap.valueIterator(); }
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("not present in emulated superclass")
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 660 */       for (ImmutableCollection<V> valueCollection : this.multimap.map.values()) {
/* 661 */         offset = valueCollection.copyIntoArray(dst, offset);
/*     */       }
/* 663 */       return offset;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 668 */     public int size() { return this.multimap.size(); }
/*     */ 
/*     */ 
/*     */     
/* 672 */     boolean isPartialView() { return true; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */