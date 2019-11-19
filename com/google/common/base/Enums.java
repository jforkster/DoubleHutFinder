/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ @Beta
/*     */ public final class Enums
/*     */ {
/*     */   @GwtIncompatible("reflection")
/*     */   public static Field getField(Enum<?> enumValue) {
/*  57 */     Class<?> clazz = enumValue.getDeclaringClass();
/*     */     try {
/*  59 */       return clazz.getDeclaredField(enumValue.name());
/*  60 */     } catch (NoSuchFieldException impossible) {
/*  61 */       throw new AssertionError(impossible);
/*     */     } 
/*     */   }
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
/*     */   @Deprecated
/*  80 */   public static <T extends Enum<T>> Function<String, T> valueOfFunction(Class<T> enumClass) { return new ValueOfFunction(enumClass, null); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ValueOfFunction<T extends Enum<T>>
/*     */     extends Object
/*     */     implements Function<String, T>, Serializable
/*     */   {
/*     */     private final Class<T> enumClass;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*  93 */     private ValueOfFunction(Class<T> enumClass) { this.enumClass = (Class)Preconditions.checkNotNull(enumClass); }
/*     */ 
/*     */ 
/*     */     
/*     */     public T apply(String value) {
/*     */       try {
/*  99 */         return (T)Enum.valueOf(this.enumClass, value);
/* 100 */       } catch (IllegalArgumentException e) {
/* 101 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 106 */     public boolean equals(@Nullable Object obj) { return (obj instanceof ValueOfFunction && this.enumClass.equals(((ValueOfFunction)obj).enumClass)); }
/*     */ 
/*     */ 
/*     */     
/* 110 */     public int hashCode() { return this.enumClass.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 114 */     public String toString() { return "Enums.valueOf(" + this.enumClass + ")"; }
/*     */   }
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
/*     */   public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
/* 130 */     Preconditions.checkNotNull(enumClass);
/* 131 */     Preconditions.checkNotNull(value);
/* 132 */     return Platform.getEnumIfPresent(enumClass, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/* 137 */   private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = new WeakHashMap();
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> enumClass) {
/* 143 */     Map<String, WeakReference<? extends Enum<?>>> result = new HashMap<String, WeakReference<? extends Enum<?>>>();
/*     */     
/* 145 */     for (Iterator i$ = EnumSet.allOf(enumClass).iterator(); i$.hasNext(); ) { T enumInstance = (T)(Enum)i$.next();
/* 146 */       result.put(enumInstance.name(), new WeakReference(enumInstance)); }
/*     */     
/* 148 */     enumConstantCache.put(enumClass, result);
/* 149 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> enumClass) {
/* 155 */     synchronized (enumConstantCache) {
/* 156 */       Map<String, WeakReference<? extends Enum<?>>> constants = (Map)enumConstantCache.get(enumClass);
/*     */       
/* 158 */       if (constants == null) {
/* 159 */         constants = populateCache(enumClass);
/*     */       }
/* 161 */       return constants;
/*     */     } 
/*     */   }
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
/* 174 */   public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> enumClass) { return new StringConverter(enumClass); }
/*     */   
/*     */   private static final class StringConverter<T extends Enum<T>>
/*     */     extends Converter<String, T>
/*     */     implements Serializable
/*     */   {
/*     */     private final Class<T> enumClass;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 183 */     StringConverter(Class<T> enumClass) { this.enumClass = (Class)Preconditions.checkNotNull(enumClass); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     protected T doForward(String value) { return (T)Enum.valueOf(this.enumClass, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     protected String doBackward(T enumValue) { return enumValue.name(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 198 */       if (object instanceof StringConverter) {
/* 199 */         StringConverter<?> that = (StringConverter)object;
/* 200 */         return this.enumClass.equals(that.enumClass);
/*     */       } 
/* 202 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 207 */     public int hashCode() { return this.enumClass.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     public String toString() { return "Enums.stringConverter(" + this.enumClass.getName() + ".class)"; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Enums.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */