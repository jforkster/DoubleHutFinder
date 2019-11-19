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
/*    */ class EmptyImmutableListMultimap
/*    */   extends ImmutableListMultimap<Object, Object>
/*    */ {
/* 28 */   static final EmptyImmutableListMultimap INSTANCE = new EmptyImmutableListMultimap();
/*    */   
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/* 32 */   private EmptyImmutableListMultimap() { super(ImmutableMap.of(), 0); }
/*    */ 
/*    */ 
/*    */   
/* 36 */   private Object readResolve() { return INSTANCE; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/EmptyImmutableListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */