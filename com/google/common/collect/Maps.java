/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Converter;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Maps
/*      */ {
/*      */   private final abstract enum EntryFunction
/*      */     implements Function<Map.Entry<?, ?>, Object>
/*      */   {
/*      */     KEY, VALUE;
/*      */     
/*      */     static  {
/*      */       // Byte code:
/*      */       //   0: new com/google/common/collect/Maps$EntryFunction$1
/*      */       //   3: dup
/*      */       //   4: ldc 'KEY'
/*      */       //   6: iconst_0
/*      */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   10: putstatic com/google/common/collect/Maps$EntryFunction.KEY : Lcom/google/common/collect/Maps$EntryFunction;
/*      */       //   13: new com/google/common/collect/Maps$EntryFunction$2
/*      */       //   16: dup
/*      */       //   17: ldc 'VALUE'
/*      */       //   19: iconst_1
/*      */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   23: putstatic com/google/common/collect/Maps$EntryFunction.VALUE : Lcom/google/common/collect/Maps$EntryFunction;
/*      */       //   26: iconst_2
/*      */       //   27: anewarray com/google/common/collect/Maps$EntryFunction
/*      */       //   30: dup
/*      */       //   31: iconst_0
/*      */       //   32: getstatic com/google/common/collect/Maps$EntryFunction.KEY : Lcom/google/common/collect/Maps$EntryFunction;
/*      */       //   35: aastore
/*      */       //   36: dup
/*      */       //   37: iconst_1
/*      */       //   38: getstatic com/google/common/collect/Maps$EntryFunction.VALUE : Lcom/google/common/collect/Maps$EntryFunction;
/*      */       //   41: aastore
/*      */       //   42: putstatic com/google/common/collect/Maps$EntryFunction.$VALUES : [Lcom/google/common/collect/Maps$EntryFunction;
/*      */       //   45: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #86	-> 0
/*      */       //   #93	-> 13
/*      */       //   #85	-> 26
/*      */     }
/*      */   }
/*      */   
/*  104 */   static <K> Function<Map.Entry<K, ?>, K> keyFunction() { return EntryFunction.KEY; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  109 */   static <V> Function<Map.Entry<?, V>, V> valueFunction() { return EntryFunction.VALUE; }
/*      */ 
/*      */ 
/*      */   
/*  113 */   static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) { return Iterators.transform(entryIterator, keyFunction()); }
/*      */ 
/*      */ 
/*      */   
/*  117 */   static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) { return Iterators.transform(entryIterator, valueFunction()); }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Map.Entry<K, V>> entryIterator) {
/*  122 */     return new UnmodifiableIterator<V>()
/*      */       {
/*      */         public boolean hasNext() {
/*  125 */           return entryIterator.hasNext();
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  130 */         public V next() { return (V)((Map.Entry)entryIterator.next()).getValue(); }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   @Beta
/*      */   public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
/*  150 */     if (map instanceof ImmutableEnumMap)
/*      */     {
/*  152 */       return (ImmutableEnumMap)map;
/*      */     }
/*  154 */     if (map.isEmpty()) {
/*  155 */       return ImmutableMap.of();
/*      */     }
/*  157 */     for (Map.Entry<K, ? extends V> entry : map.entrySet()) {
/*  158 */       Preconditions.checkNotNull(entry.getKey());
/*  159 */       Preconditions.checkNotNull(entry.getValue());
/*      */     } 
/*  161 */     return ImmutableEnumMap.asImmutable(new EnumMap(map));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  177 */   public static <K, V> HashMap<K, V> newHashMap() { return new HashMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  195 */   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) { return new HashMap(capacity(expectedSize)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int capacity(int expectedSize) {
/*  204 */     if (expectedSize < 3) {
/*  205 */       CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  206 */       return expectedSize + 1;
/*      */     } 
/*  208 */     if (expectedSize < 1073741824) {
/*  209 */       return expectedSize + expectedSize / 3;
/*      */     }
/*  211 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  230 */   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) { return new HashMap(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  243 */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() { return new LinkedHashMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  259 */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) { return new LinkedHashMap(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  278 */   public static <K, V> ConcurrentMap<K, V> newConcurrentMap() { return (new MapMaker()).makeMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  291 */   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() { return new TreeMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  307 */   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) { return new TreeMap(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  327 */   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) { return new TreeMap(comparator); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  337 */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) { return new EnumMap((Class)Preconditions.checkNotNull(type)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  351 */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) { return new EnumMap(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  360 */   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() { return new IdentityHashMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  382 */     if (left instanceof SortedMap) {
/*  383 */       SortedMap<K, ? extends V> sortedLeft = (SortedMap)left;
/*  384 */       return difference(sortedLeft, right);
/*      */     } 
/*      */     
/*  387 */     return difference(left, right, Equivalence.equals());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
/*  413 */     Preconditions.checkNotNull(valueEquivalence);
/*      */     
/*  415 */     Map<K, V> onlyOnLeft = newHashMap();
/*  416 */     Map<K, V> onlyOnRight = new HashMap<K, V>(right);
/*  417 */     Map<K, V> onBoth = newHashMap();
/*  418 */     Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
/*  419 */     doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
/*  420 */     return new MapDifferenceImpl(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  428 */     for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
/*  429 */       K leftKey = (K)entry.getKey();
/*  430 */       V leftValue = (V)entry.getValue();
/*  431 */       if (right.containsKey(leftKey)) {
/*  432 */         V rightValue = (V)onlyOnRight.remove(leftKey);
/*  433 */         if (valueEquivalence.equivalent(leftValue, rightValue)) {
/*  434 */           onBoth.put(leftKey, leftValue); continue;
/*      */         } 
/*  436 */         differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
/*      */         
/*      */         continue;
/*      */       } 
/*  440 */       onlyOnLeft.put(leftKey, leftValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
/*  446 */     if (map instanceof SortedMap) {
/*  447 */       return Collections.unmodifiableSortedMap((SortedMap)map);
/*      */     }
/*  449 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */   
/*      */   static class MapDifferenceImpl<K, V>
/*      */     extends Object
/*      */     implements MapDifference<K, V>
/*      */   {
/*      */     final Map<K, V> onlyOnLeft;
/*      */     final Map<K, V> onlyOnRight;
/*      */     final Map<K, V> onBoth;
/*      */     final Map<K, MapDifference.ValueDifference<V>> differences;
/*      */     
/*      */     MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  462 */       this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
/*  463 */       this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
/*  464 */       this.onBoth = Maps.unmodifiableMap(onBoth);
/*  465 */       this.differences = Maps.unmodifiableMap(differences);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  470 */     public boolean areEqual() { return (this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  475 */     public Map<K, V> entriesOnlyOnLeft() { return this.onlyOnLeft; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  480 */     public Map<K, V> entriesOnlyOnRight() { return this.onlyOnRight; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  485 */     public Map<K, V> entriesInCommon() { return this.onBoth; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  490 */     public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() { return this.differences; }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  494 */       if (object == this) {
/*  495 */         return true;
/*      */       }
/*  497 */       if (object instanceof MapDifference) {
/*  498 */         MapDifference<?, ?> other = (MapDifference)object;
/*  499 */         return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && entriesInCommon().equals(other.entriesInCommon()) && entriesDiffering().equals(other.entriesDiffering()));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  504 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  508 */     public int hashCode() { return Objects.hashCode(new Object[] { entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering() }); }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  513 */       if (areEqual()) {
/*  514 */         return "equal";
/*      */       }
/*      */       
/*  517 */       StringBuilder result = new StringBuilder("not equal");
/*  518 */       if (!this.onlyOnLeft.isEmpty()) {
/*  519 */         result.append(": only on left=").append(this.onlyOnLeft);
/*      */       }
/*  521 */       if (!this.onlyOnRight.isEmpty()) {
/*  522 */         result.append(": only on right=").append(this.onlyOnRight);
/*      */       }
/*  524 */       if (!this.differences.isEmpty()) {
/*  525 */         result.append(": value differences=").append(this.differences);
/*      */       }
/*  527 */       return result.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   static class ValueDifferenceImpl<V>
/*      */     extends Object
/*      */     implements MapDifference.ValueDifference<V> {
/*      */     private final V left;
/*      */     private final V right;
/*      */     
/*  537 */     static <V> MapDifference.ValueDifference<V> create(@Nullable V left, @Nullable V right) { return new ValueDifferenceImpl(left, right); }
/*      */ 
/*      */     
/*      */     private ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
/*  541 */       this.left = left;
/*  542 */       this.right = right;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  547 */     public V leftValue() { return (V)this.left; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  552 */     public V rightValue() { return (V)this.right; }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  556 */       if (object instanceof MapDifference.ValueDifference) {
/*  557 */         MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
/*      */         
/*  559 */         return (Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue()));
/*      */       } 
/*      */       
/*  562 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  566 */     public int hashCode() { return Objects.hashCode(new Object[] { this.left, this.right }); }
/*      */ 
/*      */ 
/*      */     
/*  570 */     public String toString() { return "(" + this.left + ", " + this.right + ")"; }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  595 */     Preconditions.checkNotNull(left);
/*  596 */     Preconditions.checkNotNull(right);
/*  597 */     Comparator<? super K> comparator = orNaturalOrder(left.comparator());
/*  598 */     SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
/*  599 */     SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
/*  600 */     onlyOnRight.putAll(right);
/*  601 */     SortedMap<K, V> onBoth = newTreeMap(comparator);
/*  602 */     SortedMap<K, MapDifference.ValueDifference<V>> differences = newTreeMap(comparator);
/*      */     
/*  604 */     doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
/*  605 */     return new SortedMapDifferenceImpl(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */   
/*      */   static class SortedMapDifferenceImpl<K, V>
/*      */     extends MapDifferenceImpl<K, V>
/*      */     implements SortedMapDifference<K, V>
/*      */   {
/*  613 */     SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) { super(onlyOnLeft, onlyOnRight, onBoth, differences); }
/*      */ 
/*      */ 
/*      */     
/*  617 */     public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() { return (SortedMap)super.entriesDiffering(); }
/*      */ 
/*      */ 
/*      */     
/*  621 */     public SortedMap<K, V> entriesInCommon() { return (SortedMap)super.entriesInCommon(); }
/*      */ 
/*      */ 
/*      */     
/*  625 */     public SortedMap<K, V> entriesOnlyOnLeft() { return (SortedMap)super.entriesOnlyOnLeft(); }
/*      */ 
/*      */ 
/*      */     
/*  629 */     public SortedMap<K, V> entriesOnlyOnRight() { return (SortedMap)super.entriesOnlyOnRight(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
/*  641 */     if (comparator != null) {
/*  642 */       return comparator;
/*      */     }
/*  644 */     return Ordering.natural();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
/*  677 */     if (set instanceof SortedSet) {
/*  678 */       return asMap((SortedSet)set, function);
/*      */     }
/*  680 */     return new AsMapView(set, function);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*  713 */   public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) { return Platform.mapsAsMapSortedSet(set, function); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  718 */   static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(SortedSet<K> set, Function<? super K, V> function) { return new SortedAsMapView(set, function); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible("NavigableMap")
/*  751 */   public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) { return new NavigableAsMapView(set, function); }
/*      */ 
/*      */   
/*      */   private static class AsMapView<K, V>
/*      */     extends ImprovedAbstractMap<K, V>
/*      */   {
/*      */     private final Set<K> set;
/*      */     final Function<? super K, V> function;
/*      */     
/*  760 */     Set<K> backingSet() { return this.set; }
/*      */ 
/*      */     
/*      */     AsMapView(Set<K> set, Function<? super K, V> function) {
/*  764 */       this.set = (Set)Preconditions.checkNotNull(set);
/*  765 */       this.function = (Function)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  770 */     public Set<K> createKeySet() { return Maps.removeOnlySet(backingSet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  775 */     Collection<V> createValues() { return Collections2.transform(this.set, this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  780 */     public int size() { return backingSet().size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  785 */     public boolean containsKey(@Nullable Object key) { return backingSet().contains(key); }
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(@Nullable Object key) {
/*  790 */       if (Collections2.safeContains(backingSet(), key)) {
/*      */         
/*  792 */         K k = (K)key;
/*  793 */         return (V)this.function.apply(k);
/*      */       } 
/*  795 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V remove(@Nullable Object key) {
/*  801 */       if (backingSet().remove(key)) {
/*      */         
/*  803 */         K k = (K)key;
/*  804 */         return (V)this.function.apply(k);
/*      */       } 
/*  806 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  812 */     public void clear() { backingSet().clear(); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/*  817 */       return new Maps.EntrySet<K, V>()
/*      */         {
/*      */           Map<K, V> map() {
/*  820 */             return super.this$0;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  825 */           public Iterator<Map.Entry<K, V>> iterator() { return Maps.asMapEntryIterator(super.this$0.backingSet(), Maps.AsMapView.this.function); }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
/*  833 */     return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator())
/*      */       {
/*      */         Map.Entry<K, V> transform(K key) {
/*  836 */           return Maps.immutableEntry(key, function.apply(key));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class SortedAsMapView<K, V>
/*      */     extends AsMapView<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*  845 */     SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) { super(set, function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  850 */     SortedSet<K> backingSet() { return (SortedSet)super.backingSet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  855 */     public Comparator<? super K> comparator() { return backingSet().comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  860 */     public Set<K> keySet() { return Maps.removeOnlySortedSet(backingSet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  865 */     public SortedMap<K, V> subMap(K fromKey, K toKey) { return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  870 */     public SortedMap<K, V> headMap(K toKey) { return Maps.asMap(backingSet().headSet(toKey), this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  875 */     public SortedMap<K, V> tailMap(K fromKey) { return Maps.asMap(backingSet().tailSet(fromKey), this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  880 */     public K firstKey() { return (K)backingSet().first(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  885 */     public K lastKey() { return (K)backingSet().last(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   private static final class NavigableAsMapView<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableSet<K> set;
/*      */ 
/*      */     
/*      */     private final Function<? super K, V> function;
/*      */ 
/*      */     
/*      */     NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
/*  901 */       this.set = (NavigableSet)Preconditions.checkNotNull(ks);
/*  902 */       this.function = (Function)Preconditions.checkNotNull(vFunction);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  908 */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  913 */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) { return Maps.asMap(this.set.headSet(toKey, inclusive), this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  918 */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) { return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  923 */     public Comparator<? super K> comparator() { return this.set.comparator(); }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V get(@Nullable Object key) {
/*  929 */       if (Collections2.safeContains(this.set, key)) {
/*      */         
/*  931 */         K k = (K)key;
/*  932 */         return (V)this.function.apply(k);
/*      */       } 
/*  934 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  940 */     public void clear() { this.set.clear(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  945 */     Iterator<Map.Entry<K, V>> entryIterator() { return Maps.asMapEntryIterator(this.set, this.function); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  950 */     Iterator<Map.Entry<K, V>> descendingEntryIterator() { return descendingMap().entrySet().iterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  955 */     public NavigableSet<K> navigableKeySet() { return Maps.removeOnlyNavigableSet(this.set); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  960 */     public int size() { return this.set.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  965 */     public NavigableMap<K, V> descendingMap() { return Maps.asMap(this.set.descendingSet(), this.function); }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> Set<E> removeOnlySet(final Set<E> set) {
/*  970 */     return new ForwardingSet<E>()
/*      */       {
/*      */         protected Set<E> delegate() {
/*  973 */           return set;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  978 */         public boolean add(E element) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  983 */         public boolean addAll(Collection<? extends E> es) { throw new UnsupportedOperationException(); }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
/*  989 */     return new ForwardingSortedSet<E>()
/*      */       {
/*      */         protected SortedSet<E> delegate() {
/*  992 */           return set;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  997 */         public boolean add(E element) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1002 */         public boolean addAll(Collection<? extends E> es) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1007 */         public SortedSet<E> headSet(E toElement) { return Maps.removeOnlySortedSet(super.headSet(toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1012 */         public SortedSet<E> subSet(E fromElement, E toElement) { return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1017 */         public SortedSet<E> tailSet(E fromElement) { return Maps.removeOnlySortedSet(super.tailSet(fromElement)); }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableSet")
/*      */   private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
/* 1024 */     return new ForwardingNavigableSet<E>()
/*      */       {
/*      */         protected NavigableSet<E> delegate() {
/* 1027 */           return set;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1032 */         public boolean add(E element) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1037 */         public boolean addAll(Collection<? extends E> es) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1042 */         public SortedSet<E> headSet(E toElement) { return Maps.removeOnlySortedSet(super.headSet(toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1047 */         public SortedSet<E> subSet(E fromElement, E toElement) { return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1053 */         public SortedSet<E> tailSet(E fromElement) { return Maps.removeOnlySortedSet(super.tailSet(fromElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1058 */         public NavigableSet<E> headSet(E toElement, boolean inclusive) { return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1063 */         public NavigableSet<E> tailSet(E fromElement, boolean inclusive) { return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1069 */         public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) { return Maps.removeOnlyNavigableSet(super.subSet(fromElement, fromInclusive, toElement, toInclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1075 */         public NavigableSet<E> descendingSet() { return Maps.removeOnlyNavigableSet(super.descendingSet()); }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/* 1097 */   public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) { return toMap(keys.iterator(), valueFunction); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
/* 1114 */     Preconditions.checkNotNull(valueFunction);
/*      */     
/* 1116 */     Map<K, V> builder = newLinkedHashMap();
/* 1117 */     while (keys.hasNext()) {
/* 1118 */       K key = (K)keys.next();
/* 1119 */       builder.put(key, valueFunction.apply(key));
/*      */     } 
/* 1121 */     return ImmutableMap.copyOf(builder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1140 */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) { return uniqueIndex(values.iterator(), keyFunction); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1160 */     Preconditions.checkNotNull(keyFunction);
/* 1161 */     ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/* 1162 */     while (values.hasNext()) {
/* 1163 */       V value = (V)values.next();
/* 1164 */       builder.put(keyFunction.apply(value), value);
/*      */     } 
/* 1166 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("java.util.Properties")
/*      */   public static ImmutableMap<String, String> fromProperties(Properties properties) {
/* 1185 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*      */     
/* 1187 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/* 1188 */       String key = (String)e.nextElement();
/* 1189 */       builder.put(key, properties.getProperty(key));
/*      */     } 
/*      */     
/* 1192 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/* 1207 */   public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) { return new ImmutableEntry(key, value); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1220 */   static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) { return new UnmodifiableEntrySet(Collections.unmodifiableSet(entrySet)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
/* 1234 */     Preconditions.checkNotNull(entry);
/* 1235 */     return new AbstractMapEntry<K, V>()
/*      */       {
/* 1237 */         public K getKey() { return (K)entry.getKey(); }
/*      */ 
/*      */ 
/*      */         
/* 1241 */         public V getValue() { return (V)entry.getValue(); }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static class UnmodifiableEntries<K, V>
/*      */     extends ForwardingCollection<Map.Entry<K, V>>
/*      */   {
/*      */     private final Collection<Map.Entry<K, V>> entries;
/*      */ 
/*      */     
/* 1252 */     UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) { this.entries = entries; }
/*      */ 
/*      */ 
/*      */     
/* 1256 */     protected Collection<Map.Entry<K, V>> delegate() { return this.entries; }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1260 */       final Iterator<Map.Entry<K, V>> delegate = super.iterator();
/* 1261 */       return new UnmodifiableIterator<Map.Entry<K, V>>()
/*      */         {
/*      */           public boolean hasNext() {
/* 1264 */             return delegate.hasNext();
/*      */           }
/*      */ 
/*      */           
/* 1268 */           public Map.Entry<K, V> next() { return Maps.unmodifiableEntry((Map.Entry)delegate.next()); }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1276 */     public Object[] toArray() { return standardToArray(); }
/*      */ 
/*      */ 
/*      */     
/* 1280 */     public <T> T[] toArray(T[] array) { return (T[])standardToArray(array); }
/*      */   }
/*      */ 
/*      */   
/*      */   static class UnmodifiableEntrySet<K, V>
/*      */     extends UnmodifiableEntries<K, V>
/*      */     implements Set<Map.Entry<K, V>>
/*      */   {
/* 1288 */     UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) { super(entries); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1294 */     public boolean equals(@Nullable Object object) { return Sets.equalsImpl(this, object); }
/*      */ 
/*      */ 
/*      */     
/* 1298 */     public int hashCode() { return Sets.hashCodeImpl(this); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/* 1315 */   public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) { return new BiMapConverter(bimap); }
/*      */   
/*      */   private static final class BiMapConverter<A, B>
/*      */     extends Converter<A, B> implements Serializable {
/*      */     private final BiMap<A, B> bimap;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/* 1322 */     BiMapConverter(BiMap<A, B> bimap) { this.bimap = (BiMap)Preconditions.checkNotNull(bimap); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1327 */     protected B doForward(A a) { return (B)convert(this.bimap, a); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1332 */     protected A doBackward(B b) { return (A)convert(this.bimap.inverse(), b); }
/*      */ 
/*      */     
/*      */     private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
/* 1336 */       Y output = (Y)bimap.get(input);
/* 1337 */       Preconditions.checkArgument((output != null), "No non-null mapping present for input: %s", new Object[] { input });
/* 1338 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1343 */       if (object instanceof BiMapConverter) {
/* 1344 */         BiMapConverter<?, ?> that = (BiMapConverter)object;
/* 1345 */         return this.bimap.equals(that.bimap);
/*      */       } 
/* 1347 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1352 */     public int hashCode() { return this.bimap.hashCode(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1358 */     public String toString() { return "Maps.asConverter(" + this.bimap + ")"; }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1393 */   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) { return Synchronized.biMap(bimap, null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1411 */   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) { return new UnmodifiableBiMap(bimap, null); }
/*      */   
/*      */   private static class UnmodifiableBiMap<K, V>
/*      */     extends ForwardingMap<K, V>
/*      */     implements BiMap<K, V>, Serializable
/*      */   {
/*      */     final Map<K, V> unmodifiableMap;
/*      */     final BiMap<? extends K, ? extends V> delegate;
/*      */     BiMap<V, K> inverse;
/*      */     Set<V> values;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
/* 1424 */       this.unmodifiableMap = Collections.unmodifiableMap(delegate);
/* 1425 */       this.delegate = delegate;
/* 1426 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/* 1430 */     protected Map<K, V> delegate() { return this.unmodifiableMap; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1435 */     public V forcePut(K key, V value) { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1440 */       BiMap<V, K> result = this.inverse;
/* 1441 */       return (result == null) ? (this.inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1447 */       Set<V> result = this.values;
/* 1448 */       return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1494 */   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) { return transformEntries(fromMap, asEntryTransformer(function)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1538 */   public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) { return transformEntries(fromMap, asEntryTransformer(function)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 1585 */   public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) { return transformEntries(fromMap, asEntryTransformer(function)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1642 */     if (fromMap instanceof SortedMap) {
/* 1643 */       return transformEntries((SortedMap)fromMap, transformer);
/*      */     }
/* 1645 */     return new TransformedEntriesMap(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1703 */   public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) { return Platform.mapsTransformEntriesSortedMap(fromMap, transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 1763 */   public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) { return new TransformedEntriesNavigableMap(fromMap, transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1769 */   static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) { return new TransformedEntriesSortedMap(fromMap, transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
/* 1808 */     Preconditions.checkNotNull(function);
/* 1809 */     return new EntryTransformer<K, V1, V2>()
/*      */       {
/*      */         public V2 transformEntry(K key, V1 value) {
/* 1812 */           return (V2)function.apply(value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
/* 1819 */     Preconditions.checkNotNull(transformer);
/* 1820 */     return new Function<V1, V2>()
/*      */       {
/*      */         public V2 apply(@Nullable V1 v1) {
/* 1823 */           return (V2)transformer.transformEntry(key, v1);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1833 */     Preconditions.checkNotNull(transformer);
/* 1834 */     return new Function<Map.Entry<K, V1>, V2>()
/*      */       {
/*      */         public V2 apply(Map.Entry<K, V1> entry) {
/* 1837 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
/* 1847 */     Preconditions.checkNotNull(transformer);
/* 1848 */     Preconditions.checkNotNull(entry);
/* 1849 */     return new AbstractMapEntry<K, V2>()
/*      */       {
/*      */         public K getKey() {
/* 1852 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1857 */         public V2 getValue() { return (V2)transformer.transformEntry(entry.getKey(), entry.getValue()); }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1867 */     Preconditions.checkNotNull(transformer);
/* 1868 */     return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>()
/*      */       {
/*      */         public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
/* 1871 */           return Maps.transformEntry(transformer, entry);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static class TransformedEntriesMap<K, V1, V2>
/*      */     extends ImprovedAbstractMap<K, V2>
/*      */   {
/*      */     final Map<K, V1> fromMap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1884 */       this.fromMap = (Map)Preconditions.checkNotNull(fromMap);
/* 1885 */       this.transformer = (Maps.EntryTransformer)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */ 
/*      */     
/* 1889 */     public int size() { return this.fromMap.size(); }
/*      */ 
/*      */ 
/*      */     
/* 1893 */     public boolean containsKey(Object key) { return this.fromMap.containsKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V2 get(Object key) {
/* 1899 */       V1 value = (V1)this.fromMap.get(key);
/* 1900 */       return (V2)((value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry(key, value) : null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1908 */     public V2 remove(Object key) { return (V2)(this.fromMap.containsKey(key) ? this.transformer.transformEntry(key, this.fromMap.remove(key)) : null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1914 */     public void clear() { this.fromMap.clear(); }
/*      */ 
/*      */ 
/*      */     
/* 1918 */     public Set<K> keySet() { return this.fromMap.keySet(); }
/*      */ 
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V2>> createEntrySet() {
/* 1923 */       return new Maps.EntrySet<K, V2>()
/*      */         {
/* 1925 */           Map<K, V2> map() { return super.this$0; }
/*      */ 
/*      */ 
/*      */           
/* 1929 */           public Iterator<Map.Entry<K, V2>> iterator() { return Iterators.transform(Maps.TransformedEntriesMap.this.fromMap.entrySet().iterator(), Maps.asEntryToEntryFunction(Maps.TransformedEntriesMap.this.transformer)); }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class TransformedEntriesSortedMap<K, V1, V2>
/*      */     extends TransformedEntriesMap<K, V1, V2>
/*      */     implements SortedMap<K, V2>
/*      */   {
/* 1940 */     protected SortedMap<K, V1> fromMap() { return (SortedMap)this.fromMap; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1945 */     TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) { super(fromMap, transformer); }
/*      */ 
/*      */ 
/*      */     
/* 1949 */     public Comparator<? super K> comparator() { return fromMap().comparator(); }
/*      */ 
/*      */ 
/*      */     
/* 1953 */     public K firstKey() { return (K)fromMap().firstKey(); }
/*      */ 
/*      */ 
/*      */     
/* 1957 */     public SortedMap<K, V2> headMap(K toKey) { return Maps.transformEntries(fromMap().headMap(toKey), this.transformer); }
/*      */ 
/*      */ 
/*      */     
/* 1961 */     public K lastKey() { return (K)fromMap().lastKey(); }
/*      */ 
/*      */ 
/*      */     
/* 1965 */     public SortedMap<K, V2> subMap(K fromKey, K toKey) { return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1970 */     public SortedMap<K, V2> tailMap(K fromKey) { return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   private static class TransformedEntriesNavigableMap<K, V1, V2>
/*      */     extends TransformedEntriesSortedMap<K, V1, V2>
/*      */     implements NavigableMap<K, V2>
/*      */   {
/* 1981 */     TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) { super(fromMap, transformer); }
/*      */ 
/*      */ 
/*      */     
/* 1985 */     public Map.Entry<K, V2> ceilingEntry(K key) { return transformEntry(fromMap().ceilingEntry(key)); }
/*      */ 
/*      */ 
/*      */     
/* 1989 */     public K ceilingKey(K key) { return (K)fromMap().ceilingKey(key); }
/*      */ 
/*      */ 
/*      */     
/* 1993 */     public NavigableSet<K> descendingKeySet() { return fromMap().descendingKeySet(); }
/*      */ 
/*      */ 
/*      */     
/* 1997 */     public NavigableMap<K, V2> descendingMap() { return Maps.transformEntries(fromMap().descendingMap(), this.transformer); }
/*      */ 
/*      */ 
/*      */     
/* 2001 */     public Map.Entry<K, V2> firstEntry() { return transformEntry(fromMap().firstEntry()); }
/*      */ 
/*      */     
/* 2004 */     public Map.Entry<K, V2> floorEntry(K key) { return transformEntry(fromMap().floorEntry(key)); }
/*      */ 
/*      */ 
/*      */     
/* 2008 */     public K floorKey(K key) { return (K)fromMap().floorKey(key); }
/*      */ 
/*      */ 
/*      */     
/* 2012 */     public NavigableMap<K, V2> headMap(K toKey) { return headMap(toKey, false); }
/*      */ 
/*      */ 
/*      */     
/* 2016 */     public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) { return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2021 */     public Map.Entry<K, V2> higherEntry(K key) { return transformEntry(fromMap().higherEntry(key)); }
/*      */ 
/*      */ 
/*      */     
/* 2025 */     public K higherKey(K key) { return (K)fromMap().higherKey(key); }
/*      */ 
/*      */ 
/*      */     
/* 2029 */     public Map.Entry<K, V2> lastEntry() { return transformEntry(fromMap().lastEntry()); }
/*      */ 
/*      */ 
/*      */     
/* 2033 */     public Map.Entry<K, V2> lowerEntry(K key) { return transformEntry(fromMap().lowerEntry(key)); }
/*      */ 
/*      */ 
/*      */     
/* 2037 */     public K lowerKey(K key) { return (K)fromMap().lowerKey(key); }
/*      */ 
/*      */ 
/*      */     
/* 2041 */     public NavigableSet<K> navigableKeySet() { return fromMap().navigableKeySet(); }
/*      */ 
/*      */ 
/*      */     
/* 2045 */     public Map.Entry<K, V2> pollFirstEntry() { return transformEntry(fromMap().pollFirstEntry()); }
/*      */ 
/*      */ 
/*      */     
/* 2049 */     public Map.Entry<K, V2> pollLastEntry() { return transformEntry(fromMap().pollLastEntry()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2054 */     public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return Maps.transformEntries(fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2060 */     public NavigableMap<K, V2> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*      */ 
/*      */ 
/*      */     
/* 2064 */     public NavigableMap<K, V2> tailMap(K fromKey) { return tailMap(fromKey, true); }
/*      */ 
/*      */ 
/*      */     
/* 2068 */     public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) { return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/* 2074 */     private Map.Entry<K, V2> transformEntry(@Nullable Map.Entry<K, V1> entry) { return (entry == null) ? null : Maps.transformEntry(this.transformer, entry); }
/*      */ 
/*      */ 
/*      */     
/* 2078 */     protected NavigableMap<K, V1> fromMap() { return (NavigableMap)super.fromMap(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 2083 */   static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) { return Predicates.compose(keyPredicate, keyFunction()); }
/*      */ 
/*      */ 
/*      */   
/* 2087 */   static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) { return Predicates.compose(valuePredicate, valueFunction()); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2120 */     if (unfiltered instanceof SortedMap)
/* 2121 */       return filterKeys((SortedMap)unfiltered, keyPredicate); 
/* 2122 */     if (unfiltered instanceof BiMap) {
/* 2123 */       return filterKeys((BiMap)unfiltered, keyPredicate);
/*      */     }
/* 2125 */     Preconditions.checkNotNull(keyPredicate);
/* 2126 */     Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
/* 2127 */     return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredKeyMap((Map)Preconditions.checkNotNull(unfiltered), keyPredicate, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2167 */   public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) { return filterEntries(unfiltered, keyPredicateOnEntries(keyPredicate)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 2205 */   public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) { return filterEntries(unfiltered, keyPredicateOnEntries(keyPredicate)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2235 */     Preconditions.checkNotNull(keyPredicate);
/* 2236 */     return filterEntries(unfiltered, keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2270 */     if (unfiltered instanceof SortedMap)
/* 2271 */       return filterValues((SortedMap)unfiltered, valuePredicate); 
/* 2272 */     if (unfiltered instanceof BiMap) {
/* 2273 */       return filterValues((BiMap)unfiltered, valuePredicate);
/*      */     }
/* 2275 */     return filterEntries(unfiltered, valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2311 */   public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) { return filterEntries(unfiltered, valuePredicateOnEntries(valuePredicate)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 2348 */   public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) { return filterEntries(unfiltered, valuePredicateOnEntries(valuePredicate)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2381 */   public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) { return filterEntries(unfiltered, valuePredicateOnEntries(valuePredicate)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2415 */     if (unfiltered instanceof SortedMap)
/* 2416 */       return filterEntries((SortedMap)unfiltered, entryPredicate); 
/* 2417 */     if (unfiltered instanceof BiMap) {
/* 2418 */       return filterEntries((BiMap)unfiltered, entryPredicate);
/*      */     }
/* 2420 */     Preconditions.checkNotNull(entryPredicate);
/* 2421 */     return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap)unfiltered, entryPredicate) : new FilteredEntryMap((Map)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2460 */   public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) { return Platform.mapsFilterSortedMap(unfiltered, entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2466 */     Preconditions.checkNotNull(entryPredicate);
/* 2467 */     return (unfiltered instanceof FilteredEntrySortedMap) ? filterFiltered((FilteredEntrySortedMap)unfiltered, entryPredicate) : new FilteredEntrySortedMap((SortedMap)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2507 */     Preconditions.checkNotNull(entryPredicate);
/* 2508 */     return (unfiltered instanceof FilteredEntryNavigableMap) ? filterFiltered((FilteredEntryNavigableMap)unfiltered, entryPredicate) : new FilteredEntryNavigableMap((NavigableMap)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2542 */     Preconditions.checkNotNull(unfiltered);
/* 2543 */     Preconditions.checkNotNull(entryPredicate);
/* 2544 */     return (unfiltered instanceof FilteredEntryBiMap) ? filterFiltered((FilteredEntryBiMap)unfiltered, entryPredicate) : new FilteredEntryBiMap(unfiltered, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2555 */   private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) { return new FilteredEntryMap(map.unfiltered, Predicates.and(map.predicate, entryPredicate)); }
/*      */ 
/*      */   
/*      */   private static abstract class AbstractFilteredMap<K, V>
/*      */     extends ImprovedAbstractMap<K, V>
/*      */   {
/*      */     final Map<K, V> unfiltered;
/*      */     
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2566 */       this.unfiltered = unfiltered;
/* 2567 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean apply(@Nullable Object key, @Nullable V value) {
/* 2574 */       K k = (K)key;
/* 2575 */       return this.predicate.apply(Maps.immutableEntry(k, value));
/*      */     }
/*      */     
/*      */     public V put(K key, V value) {
/* 2579 */       Preconditions.checkArgument(apply(key, value));
/* 2580 */       return (V)this.unfiltered.put(key, value);
/*      */     }
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 2584 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 2585 */         Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
/*      */       }
/* 2587 */       this.unfiltered.putAll(map);
/*      */     }
/*      */ 
/*      */     
/* 2591 */     public boolean containsKey(Object key) { return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key))); }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 2595 */       V value = (V)this.unfiltered.get(key);
/* 2596 */       return (value != null && apply(key, value)) ? value : null;
/*      */     }
/*      */ 
/*      */     
/* 2600 */     public boolean isEmpty() { return entrySet().isEmpty(); }
/*      */ 
/*      */ 
/*      */     
/* 2604 */     public V remove(Object key) { return (V)(containsKey(key) ? this.unfiltered.remove(key) : null); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2609 */     Collection<V> createValues() { return new Maps.FilteredMapValues(this, this.unfiltered, this.predicate); }
/*      */   }
/*      */   
/*      */   private static final class FilteredMapValues<K, V>
/*      */     extends Values<K, V>
/*      */   {
/*      */     Map<K, V> unfiltered;
/*      */     Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2619 */       super(filteredMap);
/* 2620 */       this.unfiltered = unfiltered;
/* 2621 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */     
/* 2625 */     public boolean remove(Object o) { return (Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(o)))) != null); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2631 */     private boolean removeIf(Predicate<? super V> valuePredicate) { return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(valuePredicate))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2636 */     public boolean removeAll(Collection<?> collection) { return removeIf(Predicates.in(collection)); }
/*      */ 
/*      */ 
/*      */     
/* 2640 */     public boolean retainAll(Collection<?> collection) { return removeIf(Predicates.not(Predicates.in(collection))); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2645 */     public Object[] toArray() { return Lists.newArrayList(iterator()).toArray(); }
/*      */ 
/*      */ 
/*      */     
/* 2649 */     public <T> T[] toArray(T[] array) { return (T[])Lists.newArrayList(iterator()).toArray(array); }
/*      */   }
/*      */   
/*      */   private static class FilteredKeyMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     Predicate<? super K> keyPredicate;
/*      */     
/*      */     FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2658 */       super(unfiltered, entryPredicate);
/* 2659 */       this.keyPredicate = keyPredicate;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2664 */     protected Set<Map.Entry<K, V>> createEntrySet() { return Sets.filter(this.unfiltered.entrySet(), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2669 */     Set<K> createKeySet() { return Sets.filter(this.unfiltered.keySet(), this.keyPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2677 */     public boolean containsKey(Object key) { return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key)); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class FilteredEntryMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Set<Map.Entry<K, V>> filteredEntrySet;
/*      */ 
/*      */ 
/*      */     
/*      */     FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2690 */       super(unfiltered, entryPredicate);
/* 2691 */       this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2696 */     protected Set<Map.Entry<K, V>> createEntrySet() { return new EntrySet(null); }
/*      */     
/*      */     private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/*      */       private EntrySet() {}
/*      */       
/* 2701 */       protected Set<Map.Entry<K, V>> delegate() { return Maps.FilteredEntryMap.this.filteredEntrySet; }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 2705 */         return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
/*      */           {
/*      */             Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
/* 2708 */               return new ForwardingMapEntry<K, V>()
/*      */                 {
/*      */                   protected Map.Entry<K, V> delegate() {
/* 2711 */                     return entry;
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public V setValue(V newValue) {
/* 2716 */                     Preconditions.checkArgument(Maps.FilteredEntryMap.EntrySet.this.this$0.apply(getKey(), newValue));
/* 2717 */                     return (V)super.setValue(newValue);
/*      */                   }
/*      */                 };
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2727 */     Set<K> createKeySet() { return new KeySet(); }
/*      */     
/*      */     class KeySet
/*      */       extends Maps.KeySet<K, V> {
/*      */       KeySet() {
/* 2732 */         super(Maps.FilteredEntryMap.this);
/*      */       }
/*      */       
/*      */       public boolean remove(Object o) {
/* 2736 */         if (Maps.FilteredEntryMap.this.containsKey(o)) {
/* 2737 */           Maps.FilteredEntryMap.this.unfiltered.remove(o);
/* 2738 */           return true;
/*      */         } 
/* 2740 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 2744 */       private boolean removeIf(Predicate<? super K> keyPredicate) { return Iterables.removeIf(Maps.FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(Maps.FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(keyPredicate))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2750 */       public boolean removeAll(Collection<?> c) { return removeIf(Predicates.in(c)); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2755 */       public boolean retainAll(Collection<?> c) { return removeIf(Predicates.not(Predicates.in(c))); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2760 */       public Object[] toArray() { return Lists.newArrayList(iterator()).toArray(); }
/*      */ 
/*      */ 
/*      */       
/* 2764 */       public <T> T[] toArray(T[] array) { return (T[])Lists.newArrayList(iterator()).toArray(array); }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2776 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/*      */     
/* 2778 */     return new FilteredEntrySortedMap(map.sortedMap(), predicate);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredEntrySortedMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/* 2786 */     FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) { super(unfiltered, entryPredicate); }
/*      */ 
/*      */ 
/*      */     
/* 2790 */     SortedMap<K, V> sortedMap() { return (SortedMap)this.unfiltered; }
/*      */ 
/*      */ 
/*      */     
/* 2794 */     public SortedSet<K> keySet() { return (SortedSet)super.keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2799 */     SortedSet<K> createKeySet() { return new SortedKeySet(); }
/*      */     
/*      */     class SortedKeySet extends Maps.FilteredEntryMap<K, V>.KeySet implements SortedSet<K> { SortedKeySet() {
/* 2802 */         super(Maps.FilteredEntrySortedMap.this);
/*      */       }
/*      */       
/* 2805 */       public Comparator<? super K> comparator() { return Maps.FilteredEntrySortedMap.this.sortedMap().comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2810 */       public SortedSet<K> subSet(K fromElement, K toElement) { return (SortedSet)Maps.FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2815 */       public SortedSet<K> headSet(K toElement) { return (SortedSet)Maps.FilteredEntrySortedMap.this.headMap(toElement).keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2820 */       public SortedSet<K> tailSet(K fromElement) { return (SortedSet)Maps.FilteredEntrySortedMap.this.tailMap(fromElement).keySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2825 */       public K first() { return (K)Maps.FilteredEntrySortedMap.this.firstKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2830 */       public K last() { return (K)Maps.FilteredEntrySortedMap.this.lastKey(); } }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2835 */     public Comparator<? super K> comparator() { return sortedMap().comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2840 */     public K firstKey() { return (K)keySet().iterator().next(); }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 2844 */       SortedMap<K, V> headMap = sortedMap();
/*      */       
/*      */       while (true) {
/* 2847 */         K key = (K)headMap.lastKey();
/* 2848 */         if (apply(key, this.unfiltered.get(key))) {
/* 2849 */           return key;
/*      */         }
/* 2851 */         headMap = sortedMap().headMap(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2856 */     public SortedMap<K, V> headMap(K toKey) { return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate); }
/*      */ 
/*      */ 
/*      */     
/* 2860 */     public SortedMap<K, V> subMap(K fromKey, K toKey) { return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2865 */     public SortedMap<K, V> tailMap(K fromKey) { return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2878 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.entryPredicate, entryPredicate);
/*      */     
/* 2880 */     return new FilteredEntryNavigableMap(map.unfiltered, predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   private static class FilteredEntryNavigableMap<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableMap<K, V> unfiltered;
/*      */     
/*      */     private final Predicate<? super Map.Entry<K, V>> entryPredicate;
/*      */     
/*      */     private final Map<K, V> filteredDelegate;
/*      */ 
/*      */     
/*      */     FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2897 */       this.unfiltered = (NavigableMap)Preconditions.checkNotNull(unfiltered);
/* 2898 */       this.entryPredicate = entryPredicate;
/* 2899 */       this.filteredDelegate = new Maps.FilteredEntryMap(unfiltered, entryPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2904 */     public Comparator<? super K> comparator() { return this.unfiltered.comparator(); }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 2909 */       return new Maps.NavigableKeySet<K, V>(this)
/*      */         {
/*      */           public boolean removeAll(Collection<?> c) {
/* 2912 */             return Iterators.removeIf(super.this$0.unfiltered.entrySet().iterator(), Predicates.and(super.this$0.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(c))));
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2918 */           public boolean retainAll(Collection<?> c) { return Iterators.removeIf(super.this$0.unfiltered.entrySet().iterator(), Predicates.and(super.this$0.entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))))); }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2926 */     public Collection<V> values() { return new Maps.FilteredMapValues(this, this.unfiltered, this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2931 */     Iterator<Map.Entry<K, V>> entryIterator() { return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2936 */     Iterator<Map.Entry<K, V>> descendingEntryIterator() { return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2941 */     public int size() { return this.filteredDelegate.size(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/* 2947 */     public V get(@Nullable Object key) { return (V)this.filteredDelegate.get(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2952 */     public boolean containsKey(@Nullable Object key) { return this.filteredDelegate.containsKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2957 */     public V put(K key, V value) { return (V)this.filteredDelegate.put(key, value); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2962 */     public V remove(@Nullable Object key) { return (V)this.filteredDelegate.remove(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2967 */     public void putAll(Map<? extends K, ? extends V> m) { this.filteredDelegate.putAll(m); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2972 */     public void clear() { this.filteredDelegate.clear(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2977 */     public Set<Map.Entry<K, V>> entrySet() { return this.filteredDelegate.entrySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2982 */     public Map.Entry<K, V> pollFirstEntry() { return (Map.Entry)Iterables.removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2987 */     public Map.Entry<K, V> pollLastEntry() { return (Map.Entry)Iterables.removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2992 */     public NavigableMap<K, V> descendingMap() { return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2998 */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return Maps.filterEntries(this.unfiltered.subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3004 */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) { return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3009 */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) { return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3019 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 3020 */     return new FilteredEntryBiMap(map.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static final class FilteredEntryBiMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements BiMap<K, V> {
/*      */     private final BiMap<V, K> inverse;
/*      */     
/*      */     private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
/* 3029 */       return new Predicate<Map.Entry<V, K>>()
/*      */         {
/*      */           public boolean apply(Map.Entry<V, K> input) {
/* 3032 */             return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
/* 3040 */       super(delegate, predicate);
/* 3041 */       this.inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
/* 3048 */       super(delegate, predicate);
/* 3049 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/* 3053 */     BiMap<K, V> unfiltered() { return (BiMap)this.unfiltered; }
/*      */ 
/*      */ 
/*      */     
/*      */     public V forcePut(@Nullable K key, @Nullable V value) {
/* 3058 */       Preconditions.checkArgument(apply(key, value));
/* 3059 */       return (V)unfiltered().forcePut(key, value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3064 */     public BiMap<V, K> inverse() { return this.inverse; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3069 */     public Set<V> values() { return this.inverse.keySet(); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, V> map) {
/* 3087 */     Preconditions.checkNotNull(map);
/* 3088 */     if (map instanceof UnmodifiableNavigableMap) {
/* 3089 */       return map;
/*      */     }
/* 3091 */     return new UnmodifiableNavigableMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/* 3096 */   private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@Nullable Map.Entry<K, V> entry) { return (entry == null) ? null : unmodifiableEntry(entry); }
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   static class UnmodifiableNavigableMap<K, V>
/*      */     extends ForwardingSortedMap<K, V>
/*      */     implements NavigableMap<K, V>, Serializable {
/*      */     private final NavigableMap<K, V> delegate;
/*      */     private UnmodifiableNavigableMap<K, V> descendingMap;
/*      */     
/* 3105 */     UnmodifiableNavigableMap(NavigableMap<K, V> delegate) { this.delegate = delegate; }
/*      */ 
/*      */ 
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
/* 3110 */       this.delegate = delegate;
/* 3111 */       this.descendingMap = descendingMap;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3116 */     protected SortedMap<K, V> delegate() { return Collections.unmodifiableSortedMap(this.delegate); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3121 */     public Map.Entry<K, V> lowerEntry(K key) { return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3126 */     public K lowerKey(K key) { return (K)this.delegate.lowerKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3131 */     public Map.Entry<K, V> floorEntry(K key) { return Maps.unmodifiableOrNull(this.delegate.floorEntry(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3136 */     public K floorKey(K key) { return (K)this.delegate.floorKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3141 */     public Map.Entry<K, V> ceilingEntry(K key) { return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3146 */     public K ceilingKey(K key) { return (K)this.delegate.ceilingKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3151 */     public Map.Entry<K, V> higherEntry(K key) { return Maps.unmodifiableOrNull(this.delegate.higherEntry(key)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3156 */     public K higherKey(K key) { return (K)this.delegate.higherKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3161 */     public Map.Entry<K, V> firstEntry() { return Maps.unmodifiableOrNull(this.delegate.firstEntry()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3166 */     public Map.Entry<K, V> lastEntry() { return Maps.unmodifiableOrNull(this.delegate.lastEntry()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3171 */     public final Map.Entry<K, V> pollFirstEntry() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3176 */     public final Map.Entry<K, V> pollLastEntry() { throw new UnsupportedOperationException(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3183 */       UnmodifiableNavigableMap<K, V> result = this.descendingMap;
/* 3184 */       return (result == null) ? (this.descendingMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3191 */     public Set<K> keySet() { return navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3196 */     public NavigableSet<K> navigableKeySet() { return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3201 */     public NavigableSet<K> descendingKeySet() { return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3206 */     public SortedMap<K, V> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3211 */     public SortedMap<K, V> headMap(K toKey) { return headMap(toKey, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3216 */     public SortedMap<K, V> tailMap(K fromKey) { return tailMap(fromKey, true); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3223 */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return Maps.unmodifiableNavigableMap(this.delegate.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3232 */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) { return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3237 */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) { return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive)); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/* 3292 */   public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) { return Synchronized.navigableMap(navigableMap); }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class ImprovedAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     private Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */     
/*      */     private Set<K> keySet;
/*      */ 
/*      */     
/*      */     private Collection<V> values;
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3314 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 3315 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3321 */       Set<K> result = this.keySet;
/* 3322 */       return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */     }
/*      */ 
/*      */     
/* 3326 */     Set<K> createKeySet() { return new Maps.KeySet(this); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3332 */       Collection<V> result = this.values;
/* 3333 */       return (result == null) ? (this.values = createValues()) : result;
/*      */     }
/*      */ 
/*      */     
/* 3337 */     Collection<V> createValues() { return new Maps.Values(this); }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeGet(Map<?, V> map, @Nullable Object key) {
/* 3346 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3348 */       return (V)map.get(key);
/* 3349 */     } catch (ClassCastException e) {
/* 3350 */       return null;
/* 3351 */     } catch (NullPointerException e) {
/* 3352 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean safeContainsKey(Map<?, ?> map, Object key) {
/* 3361 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3363 */       return map.containsKey(key);
/* 3364 */     } catch (ClassCastException e) {
/* 3365 */       return false;
/* 3366 */     } catch (NullPointerException e) {
/* 3367 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeRemove(Map<?, V> map, Object key) {
/* 3376 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3378 */       return (V)map.remove(key);
/* 3379 */     } catch (ClassCastException e) {
/* 3380 */       return null;
/* 3381 */     } catch (NullPointerException e) {
/* 3382 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3390 */   static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object key) { return Iterators.contains(keyIterator(map.entrySet().iterator()), key); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3397 */   static boolean containsValueImpl(Map<?, ?> map, @Nullable Object value) { return Iterators.contains(valueIterator(map.entrySet().iterator()), value); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3414 */     if (!(o instanceof Map.Entry)) {
/* 3415 */       return false;
/*      */     }
/* 3417 */     return c.contains(unmodifiableEntry((Map.Entry)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3434 */     if (!(o instanceof Map.Entry)) {
/* 3435 */       return false;
/*      */     }
/* 3437 */     return c.remove(unmodifiableEntry((Map.Entry)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Map<?, ?> map, Object object) {
/* 3444 */     if (map == object)
/* 3445 */       return true; 
/* 3446 */     if (object instanceof Map) {
/* 3447 */       Map<?, ?> o = (Map)object;
/* 3448 */       return map.entrySet().equals(o.entrySet());
/*      */     } 
/* 3450 */     return false;
/*      */   }
/*      */   
/* 3453 */   static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String toStringImpl(Map<?, ?> map) {
/* 3460 */     StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
/*      */     
/* 3462 */     STANDARD_JOINER.appendTo(sb, map);
/* 3463 */     return sb.append('}').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
/* 3471 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
/* 3472 */       self.put(entry.getKey(), entry.getValue()); 
/*      */   }
/*      */   
/*      */   static class KeySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<K>
/*      */   {
/*      */     final Map<K, V> map;
/*      */     
/* 3480 */     KeySet(Map<K, V> map) { this.map = (Map)Preconditions.checkNotNull(map); }
/*      */ 
/*      */ 
/*      */     
/* 3484 */     Map<K, V> map() { return this.map; }
/*      */ 
/*      */ 
/*      */     
/* 3488 */     public Iterator<K> iterator() { return Maps.keyIterator(map().entrySet().iterator()); }
/*      */ 
/*      */ 
/*      */     
/* 3492 */     public int size() { return map().size(); }
/*      */ 
/*      */ 
/*      */     
/* 3496 */     public boolean isEmpty() { return map().isEmpty(); }
/*      */ 
/*      */ 
/*      */     
/* 3500 */     public boolean contains(Object o) { return map().containsKey(o); }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3504 */       if (contains(o)) {
/* 3505 */         map().remove(o);
/* 3506 */         return true;
/*      */       } 
/* 3508 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 3512 */     public void clear() { map().clear(); }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/* 3518 */   static <K> K keyOrNull(@Nullable Map.Entry<K, ?> entry) { return (K)((entry == null) ? null : entry.getKey()); }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/* 3523 */   static <V> V valueOrNull(@Nullable Map.Entry<?, V> entry) { return (V)((entry == null) ? null : entry.getValue()); }
/*      */   
/*      */   static class SortedKeySet<K, V>
/*      */     extends KeySet<K, V>
/*      */     implements SortedSet<K> {
/* 3528 */     SortedKeySet(SortedMap<K, V> map) { super(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3533 */     SortedMap<K, V> map() { return (SortedMap)super.map(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3538 */     public Comparator<? super K> comparator() { return map().comparator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3543 */     public SortedSet<K> subSet(K fromElement, K toElement) { return new SortedKeySet(map().subMap(fromElement, toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3548 */     public SortedSet<K> headSet(K toElement) { return new SortedKeySet(map().headMap(toElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3553 */     public SortedSet<K> tailSet(K fromElement) { return new SortedKeySet(map().tailMap(fromElement)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3558 */     public K first() { return (K)map().firstKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3563 */     public K last() { return (K)map().lastKey(); }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   static class NavigableKeySet<K, V>
/*      */     extends SortedKeySet<K, V>
/*      */     implements NavigableSet<K> {
/* 3570 */     NavigableKeySet(NavigableMap<K, V> map) { super(map); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3575 */     NavigableMap<K, V> map() { return (NavigableMap)this.map; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3580 */     public K lower(K e) { return (K)map().lowerKey(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3585 */     public K floor(K e) { return (K)map().floorKey(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3590 */     public K ceiling(K e) { return (K)map().ceilingKey(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3595 */     public K higher(K e) { return (K)map().higherKey(e); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3600 */     public K pollFirst() { return (K)Maps.keyOrNull(map().pollFirstEntry()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3605 */     public K pollLast() { return (K)Maps.keyOrNull(map().pollLastEntry()); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3610 */     public NavigableSet<K> descendingSet() { return map().descendingKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3615 */     public Iterator<K> descendingIterator() { return descendingSet().iterator(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3624 */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) { return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3629 */     public NavigableSet<K> headSet(K toElement, boolean inclusive) { return map().headMap(toElement, inclusive).navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3634 */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) { return map().tailMap(fromElement, inclusive).navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3639 */     public SortedSet<K> subSet(K fromElement, K toElement) { return subSet(fromElement, true, toElement, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3644 */     public SortedSet<K> headSet(K toElement) { return headSet(toElement, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3649 */     public SortedSet<K> tailSet(K fromElement) { return tailSet(fromElement, true); }
/*      */   }
/*      */   
/*      */   static class Values<K, V>
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     final Map<K, V> map;
/*      */     
/* 3657 */     Values(Map<K, V> map) { this.map = (Map)Preconditions.checkNotNull(map); }
/*      */ 
/*      */ 
/*      */     
/* 3661 */     final Map<K, V> map() { return this.map; }
/*      */ 
/*      */ 
/*      */     
/* 3665 */     public Iterator<V> iterator() { return Maps.valueIterator(map().entrySet().iterator()); }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*      */       try {
/* 3670 */         return super.remove(o);
/* 3671 */       } catch (UnsupportedOperationException e) {
/* 3672 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3673 */           if (Objects.equal(o, entry.getValue())) {
/* 3674 */             map().remove(entry.getKey());
/* 3675 */             return true;
/*      */           } 
/*      */         } 
/* 3678 */         return false;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 3684 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 3685 */       } catch (UnsupportedOperationException e) {
/* 3686 */         Set<K> toRemove = Sets.newHashSet();
/* 3687 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3688 */           if (c.contains(entry.getValue())) {
/* 3689 */             toRemove.add(entry.getKey());
/*      */           }
/*      */         } 
/* 3692 */         return map().keySet().removeAll(toRemove);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 3698 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 3699 */       } catch (UnsupportedOperationException e) {
/* 3700 */         Set<K> toRetain = Sets.newHashSet();
/* 3701 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3702 */           if (c.contains(entry.getValue())) {
/* 3703 */             toRetain.add(entry.getKey());
/*      */           }
/*      */         } 
/* 3706 */         return map().keySet().retainAll(toRetain);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 3711 */     public int size() { return map().size(); }
/*      */ 
/*      */ 
/*      */     
/* 3715 */     public boolean isEmpty() { return map().isEmpty(); }
/*      */ 
/*      */ 
/*      */     
/* 3719 */     public boolean contains(@Nullable Object o) { return map().containsValue(o); }
/*      */ 
/*      */ 
/*      */     
/* 3723 */     public void clear() { map().clear(); }
/*      */   }
/*      */ 
/*      */   
/*      */   static abstract class EntrySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     abstract Map<K, V> map();
/*      */     
/* 3732 */     public int size() { return map().size(); }
/*      */ 
/*      */ 
/*      */     
/* 3736 */     public void clear() { map().clear(); }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3740 */       if (o instanceof Map.Entry) {
/* 3741 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 3742 */         Object key = entry.getKey();
/* 3743 */         V value = (V)Maps.safeGet(map(), key);
/* 3744 */         return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
/*      */       } 
/*      */       
/* 3747 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 3751 */     public boolean isEmpty() { return map().isEmpty(); }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3755 */       if (contains(o)) {
/* 3756 */         Map.Entry<?, ?> entry = (Map.Entry)o;
/* 3757 */         return map().keySet().remove(entry.getKey());
/*      */       } 
/* 3759 */       return false;
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 3764 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 3765 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 3767 */         return Sets.removeAllImpl(this, c.iterator());
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 3773 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 3774 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 3776 */         Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
/* 3777 */         for (Object o : c) {
/* 3778 */           if (contains(o)) {
/* 3779 */             Map.Entry<?, ?> entry = (Map.Entry)o;
/* 3780 */             keys.add(entry.getKey());
/*      */           } 
/*      */         } 
/* 3783 */         return map().keySet().retainAll(keys);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible("NavigableMap")
/*      */   static abstract class DescendingMap<K, V>
/*      */     extends ForwardingMap<K, V>
/*      */     implements NavigableMap<K, V> {
/*      */     private Comparator<? super K> comparator;
/*      */     private Set<Map.Entry<K, V>> entrySet;
/*      */     private NavigableSet<K> navigableKeySet;
/*      */     
/* 3796 */     protected final Map<K, V> delegate() { return forward(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3804 */       Comparator<? super K> result = this.comparator;
/* 3805 */       if (result == null) {
/* 3806 */         Comparator<? super K> forwardCmp = forward().comparator();
/* 3807 */         if (forwardCmp == null) {
/* 3808 */           forwardCmp = Ordering.natural();
/*      */         }
/* 3810 */         result = this.comparator = reverse(forwardCmp);
/*      */       } 
/* 3812 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3817 */     private static <T> Ordering<T> reverse(Comparator<T> forward) { return Ordering.from(forward).reverse(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3822 */     public K firstKey() { return (K)forward().lastKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3827 */     public K lastKey() { return (K)forward().firstKey(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3832 */     public Map.Entry<K, V> lowerEntry(K key) { return forward().higherEntry(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3837 */     public K lowerKey(K key) { return (K)forward().higherKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3842 */     public Map.Entry<K, V> floorEntry(K key) { return forward().ceilingEntry(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3847 */     public K floorKey(K key) { return (K)forward().ceilingKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3852 */     public Map.Entry<K, V> ceilingEntry(K key) { return forward().floorEntry(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3857 */     public K ceilingKey(K key) { return (K)forward().floorKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3862 */     public Map.Entry<K, V> higherEntry(K key) { return forward().lowerEntry(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3867 */     public K higherKey(K key) { return (K)forward().lowerKey(key); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3872 */     public Map.Entry<K, V> firstEntry() { return forward().lastEntry(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3877 */     public Map.Entry<K, V> lastEntry() { return forward().firstEntry(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3882 */     public Map.Entry<K, V> pollFirstEntry() { return forward().pollLastEntry(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3887 */     public Map.Entry<K, V> pollLastEntry() { return forward().pollFirstEntry(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3892 */     public NavigableMap<K, V> descendingMap() { return forward(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3899 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 3900 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<Map.Entry<K, V>> createEntrySet() {
/* 3906 */       return new Maps.EntrySet<K, V>()
/*      */         {
/*      */           Map<K, V> map() {
/* 3909 */             return super.this$0;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 3914 */           public Iterator<Map.Entry<K, V>> iterator() { return super.this$0.entryIterator(); }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3921 */     public Set<K> keySet() { return navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3928 */       NavigableSet<K> result = this.navigableKeySet;
/* 3929 */       return (result == null) ? (this.navigableKeySet = new Maps.NavigableKeySet(this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3934 */     public NavigableSet<K> descendingKeySet() { return forward().navigableKeySet(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3941 */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) { return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3946 */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) { return forward().tailMap(toKey, inclusive).descendingMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3951 */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) { return forward().headMap(fromKey, inclusive).descendingMap(); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3956 */     public SortedMap<K, V> subMap(K fromKey, K toKey) { return subMap(fromKey, true, toKey, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3961 */     public SortedMap<K, V> headMap(K toKey) { return headMap(toKey, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3966 */     public SortedMap<K, V> tailMap(K fromKey) { return tailMap(fromKey, true); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3971 */     public Collection<V> values() { return new Maps.Values(this); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3976 */     public String toString() { return standardToString(); }
/*      */     
/*      */     abstract NavigableMap<K, V> forward();
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator();
/*      */   }
/*      */   
/*      */   public static interface EntryTransformer<K, V1, V2> {
/*      */     V2 transformEntry(@Nullable K param1K, @Nullable V1 param1V1);
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/Maps.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */