/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nullable;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class EmptyImmutableBiMap
/*    */   extends ImmutableBiMap<Object, Object>
/*    */ {
/* 31 */   static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public ImmutableBiMap<Object, Object> inverse() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public int size() { return 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean isEmpty() { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Object get(@Nullable Object key) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public ImmutableSet<Map.Entry<Object, Object>> entrySet() { return ImmutableSet.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   ImmutableSet<Map.Entry<Object, Object>> createEntrySet() { throw new AssertionError("should never be called"); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public ImmutableSetMultimap<Object, Object> asMultimap() { return ImmutableSetMultimap.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   public ImmutableSet<Object> keySet() { return ImmutableSet.of(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 76 */   boolean isPartialView() { return false; }
/*    */ 
/*    */ 
/*    */   
/* 80 */   Object readResolve() { return INSTANCE; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */