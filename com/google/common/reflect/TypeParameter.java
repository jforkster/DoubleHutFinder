/*    */ package com.google.common.reflect;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
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
/*    */ @Beta
/*    */ public abstract class TypeParameter<T>
/*    */   extends TypeCapture<T>
/*    */ {
/*    */   final TypeVariable<?> typeVariable;
/*    */   
/*    */   protected TypeParameter() {
/* 47 */     Type type = capture();
/* 48 */     Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", new Object[] { type });
/* 49 */     this.typeVariable = (TypeVariable)type;
/*    */   }
/*    */ 
/*    */   
/* 53 */   public final int hashCode() { return this.typeVariable.hashCode(); }
/*    */ 
/*    */   
/*    */   public final boolean equals(@Nullable Object o) {
/* 57 */     if (o instanceof TypeParameter) {
/* 58 */       TypeParameter<?> that = (TypeParameter)o;
/* 59 */       return this.typeVariable.equals(that.typeVariable);
/*    */     } 
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 65 */   public String toString() { return this.typeVariable.toString(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/TypeParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */