/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ class EmptyImmutableSetMultimap
/*    */   extends ImmutableSetMultimap<Object, Object>
/*    */ {
/* 28 */   static final EmptyImmutableSetMultimap INSTANCE = new EmptyImmutableSetMultimap();
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 32 */   private EmptyImmutableSetMultimap() { super(ImmutableMap.of(), 0, null); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   private Object readResolve() { return INSTANCE; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableSetMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */