/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class WellBehavedMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */ {
/*    */   private final Map<K, V> delegate;
/*    */   private Set<Map.Entry<K, V>> entrySet;
/*    */   
/* 42 */   private WellBehavedMap(Map<K, V> delegate) { this.delegate = delegate; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   static <K, V> WellBehavedMap<K, V> wrap(Map<K, V> delegate) { return new WellBehavedMap(delegate); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   protected Map<K, V> delegate() { return this.delegate; }
/*    */ 
/*    */   
/*    */   public Set<Map.Entry<K, V>> entrySet() {
/* 60 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 61 */     if (es != null) {
/* 62 */       return es;
/*    */     }
/* 64 */     return this.entrySet = new EntrySet(null);
/*    */   }
/*    */   
/*    */   private final class EntrySet extends Maps.EntrySet<K, V> {
/*    */     private EntrySet() {}
/*    */     
/* 70 */     Map<K, V> map() { return WellBehavedMap.this; }
/*    */ 
/*    */ 
/*    */     
/*    */     public Iterator<Map.Entry<K, V>> iterator() {
/* 75 */       return new TransformedIterator<K, Map.Entry<K, V>>(WellBehavedMap.this.keySet().iterator())
/*    */         {
/*    */           Map.Entry<K, V> transform(final K key) {
/* 78 */             return new AbstractMapEntry<K, V>()
/*    */               {
/*    */                 public K getKey() {
/* 81 */                   return (K)key;
/*    */                 }
/*    */ 
/*    */ 
/*    */                 
/* 86 */                 public V getValue() { return (V)WellBehavedMap.EntrySet.this.this$0.get(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */                 
/* 91 */                 public V setValue(V value) { return (V)WellBehavedMap.EntrySet.this.this$0.put(key, value); }
/*    */               };
/*    */           }
/*    */         };
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/WellBehavedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */