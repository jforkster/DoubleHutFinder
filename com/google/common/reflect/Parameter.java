/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
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
/*     */ @Beta
/*     */ public final class Parameter
/*     */   implements AnnotatedElement
/*     */ {
/*     */   private final Invokable<?, ?> declaration;
/*     */   private final int position;
/*     */   private final TypeToken<?> type;
/*     */   private final ImmutableList<Annotation> annotations;
/*     */   
/*     */   Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations) {
/*  48 */     this.declaration = declaration;
/*  49 */     this.position = position;
/*  50 */     this.type = type;
/*  51 */     this.annotations = ImmutableList.copyOf(annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public TypeToken<?> getType() { return this.type; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public Invokable<?, ?> getDeclaringInvokable() { return this.declaration; }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) { return (getAnnotation(annotationType) != null); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*  71 */     Preconditions.checkNotNull(annotationType);
/*  72 */     for (Annotation annotation : this.annotations) {
/*  73 */       if (annotationType.isInstance(annotation)) {
/*  74 */         return (A)(Annotation)annotationType.cast(annotation);
/*     */       }
/*     */     } 
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   
/*  81 */   public Annotation[] getAnnotations() { return getDeclaredAnnotations(); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public Annotation[] getDeclaredAnnotations() { return (Annotation[])this.annotations.toArray(new Annotation[this.annotations.size()]); }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/*  89 */     if (obj instanceof Parameter) {
/*  90 */       Parameter that = (Parameter)obj;
/*  91 */       return (this.position == that.position && this.declaration.equals(that.declaration));
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*  97 */   public int hashCode() { return this.position; }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public String toString() { return this.type + " arg" + this.position; }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/Parameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */