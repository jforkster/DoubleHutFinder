/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class TreeMultimap<K, V>
/*     */   extends AbstractSortedKeySortedSetMultimap<K, V>
/*     */ {
/*     */   private Comparator<? super K> keyComparator;
/*     */   private Comparator<? super V> valueComparator;
/*     */   @GwtIncompatible("not needed in emulated source")
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*  89 */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() { return new TreeMultimap(Ordering.natural(), Ordering.natural()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) { return new TreeMultimap((Comparator)Preconditions.checkNotNull(keyComparator), (Comparator)Preconditions.checkNotNull(valueComparator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) { return new TreeMultimap(Ordering.natural(), Ordering.natural(), multimap); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/* 121 */     super(new TreeMap(keyComparator));
/* 122 */     this.keyComparator = keyComparator;
/* 123 */     this.valueComparator = valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
/* 129 */     this(keyComparator, valueComparator);
/* 130 */     putAll(multimap);
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
/* 142 */   SortedSet<V> createCollection() { return new TreeSet(this.valueComparator); }
/*     */ 
/*     */ 
/*     */   
/*     */   Collection<V> createCollection(@Nullable K key) {
/* 147 */     if (key == null) {
/* 148 */       keyComparator().compare(key, key);
/*     */     }
/* 150 */     return super.createCollection(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public Comparator<? super K> keyComparator() { return this.keyComparator; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public Comparator<? super V> valueComparator() { return this.valueComparator; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableMap")
/* 174 */   NavigableMap<K, Collection<V>> backingMap() { return (NavigableMap)super.backingMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 183 */   public NavigableSet<V> get(@Nullable K key) { return (NavigableSet)super.get(key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 189 */   Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) { return Sets.unmodifiableNavigableSet((NavigableSet)collection); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 195 */   Collection<V> wrapCollection(K key, Collection<V> collection) { return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet)collection, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 210 */   public NavigableSet<K> keySet() { return (NavigableSet)super.keySet(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableSet")
/* 216 */   NavigableSet<K> createKeySet() { return new AbstractMapBasedMultimap.NavigableKeySet(this, backingMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableMap")
/* 231 */   public NavigableMap<K, Collection<V>> asMap() { return (NavigableMap)super.asMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("NavigableMap")
/* 237 */   NavigableMap<K, Collection<V>> createAsMap() { return new AbstractMapBasedMultimap.NavigableAsMap(this, backingMap()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectOutputStream")
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 247 */     stream.defaultWriteObject();
/* 248 */     stream.writeObject(keyComparator());
/* 249 */     stream.writeObject(valueComparator());
/* 250 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.io.ObjectInputStream")
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 257 */     stream.defaultReadObject();
/* 258 */     this.keyComparator = (Comparator)Preconditions.checkNotNull((Comparator)stream.readObject());
/* 259 */     this.valueComparator = (Comparator)Preconditions.checkNotNull((Comparator)stream.readObject());
/* 260 */     setMap(new TreeMap(this.keyComparator));
/* 261 */     Serialization.populateMultimap(this, stream);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/TreeMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */