/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
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
/*     */ @Beta
/*     */ public abstract class Invokable<T, R>
/*     */   extends Element
/*     */   implements GenericDeclaration
/*     */ {
/*  63 */   <M extends java.lang.reflect.AccessibleObject & java.lang.reflect.Member> Invokable(M member) { super(member); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static Invokable<?, Object> from(Method method) { return new MethodInvokable(method); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static <T> Invokable<T, T> from(Constructor<T> constructor) { return new ConstructorInvokable(constructor); }
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
/* 102 */   public final R invoke(@Nullable T receiver, Object... args) throws InvocationTargetException, IllegalAccessException { return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public final TypeToken<? extends R> getReturnType() { return TypeToken.of(getGenericReturnType()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<Parameter> getParameters() {
/* 118 */     Type[] parameterTypes = getGenericParameterTypes();
/* 119 */     Annotation[][] annotations = getParameterAnnotations();
/* 120 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 121 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 122 */       builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i]));
/*     */     }
/*     */     
/* 125 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
/* 130 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 131 */     for (Type type : getGenericExceptionTypes()) {
/*     */ 
/*     */       
/* 134 */       TypeToken<? extends Throwable> exceptionType = TypeToken.of(type);
/*     */       
/* 136 */       builder.add(exceptionType);
/*     */     } 
/* 138 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) { return returning(TypeToken.of(returnType)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
/* 153 */     if (!returnType.isAssignableFrom(getReturnType())) {
/* 154 */       throw new IllegalArgumentException("Invokable is known to return " + getReturnType() + ", not " + returnType);
/*     */     }
/*     */ 
/*     */     
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public final Class<? super T> getDeclaringClass() { return super.getDeclaringClass(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public TypeToken<T> getOwnerType() { return TypeToken.of(getDeclaringClass()); }
/*     */   
/*     */   public abstract boolean isOverridable();
/*     */   
/*     */   public abstract boolean isVarArgs();
/*     */   
/*     */   abstract Object invokeInternal(@Nullable Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   static class MethodInvokable<T> extends Invokable<T, Object> {
/*     */     final Method method;
/*     */     
/*     */     MethodInvokable(Method method) {
/* 191 */       super(method);
/* 192 */       this.method = method;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 197 */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException { return this.method.invoke(receiver, args); }
/*     */ 
/*     */ 
/*     */     
/* 201 */     Type getGenericReturnType() { return this.method.getGenericReturnType(); }
/*     */ 
/*     */ 
/*     */     
/* 205 */     Type[] getGenericParameterTypes() { return this.method.getGenericParameterTypes(); }
/*     */ 
/*     */ 
/*     */     
/* 209 */     Type[] getGenericExceptionTypes() { return this.method.getGenericExceptionTypes(); }
/*     */ 
/*     */ 
/*     */     
/* 213 */     final Annotation[][] getParameterAnnotations() { return this.method.getParameterAnnotations(); }
/*     */ 
/*     */ 
/*     */     
/* 217 */     public final TypeVariable<?>[] getTypeParameters() { return this.method.getTypeParameters(); }
/*     */ 
/*     */ 
/*     */     
/* 221 */     public final boolean isOverridable() { return (!isFinal() && !isPrivate() && !isStatic() && !Modifier.isFinal(getDeclaringClass().getModifiers())); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 226 */     public final boolean isVarArgs() { return this.method.isVarArgs(); }
/*     */   }
/*     */   
/*     */   static class ConstructorInvokable<T>
/*     */     extends Invokable<T, T>
/*     */   {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 235 */       super(constructor);
/* 236 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/*     */       try {
/* 242 */         return this.constructor.newInstance(args);
/* 243 */       } catch (InstantiationException e) {
/* 244 */         throw new RuntimeException(this.constructor + " failed.", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 250 */       Class<?> declaringClass = getDeclaringClass();
/* 251 */       TypeVariable[] typeParams = declaringClass.getTypeParameters();
/* 252 */       if (typeParams.length > 0) {
/* 253 */         return Types.newParameterizedType(declaringClass, typeParams);
/*     */       }
/* 255 */       return declaringClass;
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 260 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 261 */       if (types.length > 0 && mayNeedHiddenThis()) {
/* 262 */         Class[] rawParamTypes = this.constructor.getParameterTypes();
/* 263 */         if (types.length == rawParamTypes.length && rawParamTypes[false] == getDeclaringClass().getEnclosingClass())
/*     */         {
/*     */           
/* 266 */           return (Type[])Arrays.copyOfRange(types, 1, types.length);
/*     */         }
/*     */       } 
/* 269 */       return types;
/*     */     }
/*     */ 
/*     */     
/* 273 */     Type[] getGenericExceptionTypes() { return this.constructor.getGenericExceptionTypes(); }
/*     */ 
/*     */ 
/*     */     
/* 277 */     final Annotation[][] getParameterAnnotations() { return this.constructor.getParameterAnnotations(); }
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
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 290 */       TypeVariable[] declaredByClass = getDeclaringClass().getTypeParameters();
/* 291 */       TypeVariable[] declaredByConstructor = this.constructor.getTypeParameters();
/* 292 */       TypeVariable[] result = new TypeVariable[declaredByClass.length + declaredByConstructor.length];
/*     */       
/* 294 */       System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
/* 295 */       System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
/*     */ 
/*     */ 
/*     */       
/* 299 */       return result;
/*     */     }
/*     */ 
/*     */     
/* 303 */     public final boolean isOverridable() { return false; }
/*     */ 
/*     */ 
/*     */     
/* 307 */     public final boolean isVarArgs() { return this.constructor.isVarArgs(); }
/*     */ 
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 311 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 312 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 314 */         return true;
/*     */       }
/* 316 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 317 */       if (enclosingMethod != null)
/*     */       {
/* 319 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 327 */       return (declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/Invokable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */