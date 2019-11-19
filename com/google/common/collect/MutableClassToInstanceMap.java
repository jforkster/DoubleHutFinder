/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.primitives.Primitives;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MutableClassToInstanceMap<B>
/*    */   extends MapConstraints.ConstrainedMap<Class<? extends B>, B>
/*    */   implements ClassToInstanceMap<B>
/*    */ {
/* 45 */   public static <B> MutableClassToInstanceMap<B> create() { return new MutableClassToInstanceMap(new HashMap()); }
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
/* 56 */   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) { return new MutableClassToInstanceMap(backingMap); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) { super(delegate, VALUE_CAN_BE_CAST_TO_KEY); }
/*    */ 
/*    */   
/* 63 */   private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>()
/*    */     {
/*    */       public void checkKeyValue(Class<?> key, Object value)
/*    */       {
/* 67 */         MutableClassToInstanceMap.cast(key, value);
/*    */       }
/*    */     };
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 73 */   public <T extends B> T putInstance(Class<T> type, T value) { return (T)cast(type, put(type, value)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   public <T extends B> T getInstance(Class<T> type) { return (T)cast(type, get(type)); }
/*    */ 
/*    */ 
/*    */   
/* 82 */   private static <B, T extends B> T cast(Class<T> type, B value) { return (T)Primitives.wrap(type).cast(value); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/MutableClassToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */