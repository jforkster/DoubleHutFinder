/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Arrays;
/*     */ import javax.annotation.CheckReturnValue;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Objects
/*     */ {
/*     */   @CheckReturnValue
/*  57 */   public static boolean equal(@Nullable Object a, @Nullable Object b) { return (a == b || (a != null && a.equals(b))); }
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
/*  78 */   public static int hashCode(@Nullable Object... objects) { return Arrays.hashCode(objects); }
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
/* 121 */   public static ToStringHelper toStringHelper(Object self) { return new ToStringHelper(simpleName(self.getClass()), null); }
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
/* 135 */   public static ToStringHelper toStringHelper(Class<?> clazz) { return new ToStringHelper(simpleName(clazz), null); }
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
/* 147 */   public static ToStringHelper toStringHelper(String className) { return new ToStringHelper(className, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String simpleName(Class<?> clazz) {
/* 155 */     String name = clazz.getName();
/*     */ 
/*     */ 
/*     */     
/* 159 */     name = name.replaceAll("\\$[0-9]+", "\\$");
/*     */ 
/*     */     
/* 162 */     int start = name.lastIndexOf('$');
/*     */ 
/*     */ 
/*     */     
/* 166 */     if (start == -1) {
/* 167 */       start = name.lastIndexOf('.');
/*     */     }
/* 169 */     return name.substring(start + 1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public static <T> T firstNonNull(@Nullable T first, @Nullable T second) { return (first != null) ? first : Preconditions.checkNotNull(second); }
/*     */ 
/*     */   
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */     private ValueHolder holderHead;
/*     */     private ValueHolder holderTail;
/*     */     private boolean omitNullValues;
/*     */     
/*     */     private ToStringHelper(String className) {
/* 201 */       this.holderHead = new ValueHolder(null);
/* 202 */       this.holderTail = this.holderHead;
/* 203 */       this.omitNullValues = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 209 */       this.className = (String)Preconditions.checkNotNull(className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ToStringHelper omitNullValues() {
/* 220 */       this.omitNullValues = true;
/* 221 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     public ToStringHelper add(String name, @Nullable Object value) { return addHolder(name, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     public ToStringHelper add(String name, boolean value) { return addHolder(name, String.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     public ToStringHelper add(String name, char value) { return addHolder(name, String.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     public ToStringHelper add(String name, double value) { return addHolder(name, String.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     public ToStringHelper add(String name, float value) { return addHolder(name, String.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     public ToStringHelper add(String name, int value) { return addHolder(name, String.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     public ToStringHelper add(String name, long value) { return addHolder(name, String.valueOf(value)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     public ToStringHelper addValue(@Nullable Object value) { return addHolder(value); }
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
/* 313 */     public ToStringHelper addValue(boolean value) { return addHolder(String.valueOf(value)); }
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
/* 325 */     public ToStringHelper addValue(char value) { return addHolder(String.valueOf(value)); }
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
/* 337 */     public ToStringHelper addValue(double value) { return addHolder(String.valueOf(value)); }
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
/* 349 */     public ToStringHelper addValue(float value) { return addHolder(String.valueOf(value)); }
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
/* 361 */     public ToStringHelper addValue(int value) { return addHolder(String.valueOf(value)); }
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
/* 373 */     public ToStringHelper addValue(long value) { return addHolder(String.valueOf(value)); }
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
/*     */     public String toString() {
/* 388 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 389 */       String nextSeparator = "";
/* 390 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/*     */       
/* 392 */       for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; 
/* 393 */         valueHolder = valueHolder.next) {
/* 394 */         if (!omitNullValuesSnapshot || valueHolder.value != null) {
/* 395 */           builder.append(nextSeparator);
/* 396 */           nextSeparator = ", ";
/*     */           
/* 398 */           if (valueHolder.name != null) {
/* 399 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 401 */           builder.append(valueHolder.value);
/*     */         } 
/*     */       } 
/* 404 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 408 */       ValueHolder valueHolder = new ValueHolder(null);
/* 409 */       this.holderTail = this.holderTail.next = valueHolder;
/* 410 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(@Nullable Object value) {
/* 414 */       ValueHolder valueHolder = addHolder();
/* 415 */       valueHolder.value = value;
/* 416 */       return this;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(String name, @Nullable Object value) {
/* 420 */       ValueHolder valueHolder = addHolder();
/* 421 */       valueHolder.value = value;
/* 422 */       valueHolder.name = (String)Preconditions.checkNotNull(name);
/* 423 */       return this;
/*     */     }
/*     */     
/*     */     private static final class ValueHolder {
/*     */       String name;
/*     */       Object value;
/*     */       ValueHolder next;
/*     */       
/*     */       private ValueHolder() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Objects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */