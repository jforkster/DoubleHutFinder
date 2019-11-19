/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ class Element
/*     */   extends AccessibleObject
/*     */   implements Member
/*     */ {
/*     */   private final AccessibleObject accessibleObject;
/*     */   private final Member member;
/*     */   
/*     */   <M extends AccessibleObject & Member> Element(M member) {
/*  43 */     Preconditions.checkNotNull(member);
/*  44 */     this.accessibleObject = member;
/*  45 */     this.member = (Member)member;
/*     */   }
/*     */ 
/*     */   
/*  49 */   public TypeToken<?> getOwnerType() { return TypeToken.of(getDeclaringClass()); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) { return this.accessibleObject.isAnnotationPresent(annotationClass); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) { return (A)this.accessibleObject.getAnnotation(annotationClass); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public final Annotation[] getAnnotations() { return this.accessibleObject.getAnnotations(); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public final Annotation[] getDeclaredAnnotations() { return this.accessibleObject.getDeclaredAnnotations(); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public final void setAccessible(boolean flag) throws SecurityException { this.accessibleObject.setAccessible(flag); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public final boolean isAccessible() { return this.accessibleObject.isAccessible(); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public Class<?> getDeclaringClass() { return this.member.getDeclaringClass(); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public final String getName() { return this.member.getName(); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public final int getModifiers() { return this.member.getModifiers(); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public final boolean isSynthetic() { return this.member.isSynthetic(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public final boolean isPublic() { return Modifier.isPublic(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public final boolean isProtected() { return Modifier.isProtected(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public final boolean isPackagePrivate() { return (!isPrivate() && !isPublic() && !isProtected()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public final boolean isPrivate() { return Modifier.isPrivate(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public final boolean isStatic() { return Modifier.isStatic(getModifiers()); }
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
/* 125 */   public final boolean isFinal() { return Modifier.isFinal(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   public final boolean isAbstract() { return Modifier.isAbstract(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public final boolean isNative() { return Modifier.isNative(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public final boolean isSynchronized() { return Modifier.isSynchronized(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   final boolean isVolatile() { return Modifier.isVolatile(getModifiers()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 150 */   final boolean isTransient() { return Modifier.isTransient(getModifiers()); }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 154 */     if (obj instanceof Element) {
/* 155 */       Element that = (Element)obj;
/* 156 */       return (getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member));
/*     */     } 
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 162 */   public int hashCode() { return this.member.hashCode(); }
/*     */ 
/*     */ 
/*     */   
/* 166 */   public String toString() { return this.member.toString(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/Element.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */