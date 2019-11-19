/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public class Joiner
/*     */ {
/*     */   private final String separator;
/*     */   
/*  71 */   public static Joiner on(String separator) { return new Joiner(separator); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static Joiner on(char separator) { return new Joiner(String.valueOf(separator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private Joiner(String separator) { this.separator = (String)Preconditions.checkNotNull(separator); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   private Joiner(Joiner prototype) { this.separator = prototype.separator; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException { return (A)appendTo(appendable, parts.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 106 */     Preconditions.checkNotNull(appendable);
/* 107 */     if (parts.hasNext()) {
/* 108 */       appendable.append(toString(parts.next()));
/* 109 */       while (parts.hasNext()) {
/* 110 */         appendable.append(this.separator);
/* 111 */         appendable.append(toString(parts.next()));
/*     */       } 
/*     */     } 
/* 114 */     return appendable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException { return (A)appendTo(appendable, Arrays.asList(parts)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException { return (A)appendTo(appendable, iterable(first, second, rest)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) { return appendTo(builder, parts.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
/*     */     try {
/* 152 */       appendTo(builder, parts);
/* 153 */     } catch (IOException impossible) {
/* 154 */       throw new AssertionError(impossible);
/*     */     } 
/* 156 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 165 */   public final StringBuilder appendTo(StringBuilder builder, Object[] parts) { return appendTo(builder, Arrays.asList(parts)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest) { return appendTo(builder, iterable(first, second, rest)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public final String join(Iterable<?> parts) { return join(parts.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public final String join(Iterator<?> parts) { return appendTo(new StringBuilder(), parts).toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public final String join(Object[] parts) { return join(Arrays.asList(parts)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   public final String join(@Nullable Object first, @Nullable Object second, Object... rest) { return join(iterable(first, second, rest)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public Joiner useForNull(final String nullText) {
/* 218 */     Preconditions.checkNotNull(nullText);
/* 219 */     return new Joiner(this)
/*     */       {
/* 221 */         CharSequence toString(@Nullable Object part) { return (part == null) ? nullText : Joiner.this.toString(part); }
/*     */ 
/*     */ 
/*     */         
/* 225 */         public Joiner useForNull(String nullText) { throw new UnsupportedOperationException("already specified useForNull"); }
/*     */ 
/*     */ 
/*     */         
/* 229 */         public Joiner skipNulls() { throw new UnsupportedOperationException("already specified useForNull"); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public Joiner skipNulls() {
/* 240 */     return new Joiner(this)
/*     */       {
/*     */         public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 243 */           Preconditions.checkNotNull(appendable, "appendable");
/* 244 */           Preconditions.checkNotNull(parts, "parts");
/* 245 */           while (parts.hasNext()) {
/* 246 */             Object part = parts.next();
/* 247 */             if (part != null) {
/* 248 */               appendable.append(Joiner.this.toString(part));
/*     */               break;
/*     */             } 
/*     */           } 
/* 252 */           while (parts.hasNext()) {
/* 253 */             Object part = parts.next();
/* 254 */             if (part != null) {
/* 255 */               appendable.append(Joiner.this.separator);
/* 256 */               appendable.append(Joiner.this.toString(part));
/*     */             } 
/*     */           } 
/* 259 */           return appendable;
/*     */         }
/*     */ 
/*     */         
/* 263 */         public Joiner useForNull(String nullText) { throw new UnsupportedOperationException("already specified skipNulls"); }
/*     */ 
/*     */ 
/*     */         
/* 267 */         public MapJoiner withKeyValueSeparator(String kvs) { throw new UnsupportedOperationException("can't use .skipNulls() with maps"); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 278 */   public MapJoiner withKeyValueSeparator(String keyValueSeparator) { return new MapJoiner(this, keyValueSeparator, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class MapJoiner
/*     */   {
/*     */     private final Joiner joiner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String keyValueSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MapJoiner(Joiner joiner, String keyValueSeparator) {
/* 304 */       this.joiner = joiner;
/* 305 */       this.keyValueSeparator = (String)Preconditions.checkNotNull(keyValueSeparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException { return (A)appendTo(appendable, map.entrySet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) { return appendTo(builder, map.entrySet()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     public String join(Map<?, ?> map) { return join(map.entrySet()); }
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
/*     */     @Beta
/* 342 */     public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException { return (A)appendTo(appendable, entries.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
/* 354 */       Preconditions.checkNotNull(appendable);
/* 355 */       if (parts.hasNext()) {
/* 356 */         Map.Entry<?, ?> entry = (Map.Entry)parts.next();
/* 357 */         appendable.append(this.joiner.toString(entry.getKey()));
/* 358 */         appendable.append(this.keyValueSeparator);
/* 359 */         appendable.append(this.joiner.toString(entry.getValue()));
/* 360 */         while (parts.hasNext()) {
/* 361 */           appendable.append(this.joiner.separator);
/* 362 */           Map.Entry<?, ?> e = (Map.Entry)parts.next();
/* 363 */           appendable.append(this.joiner.toString(e.getKey()));
/* 364 */           appendable.append(this.keyValueSeparator);
/* 365 */           appendable.append(this.joiner.toString(e.getValue()));
/*     */         } 
/*     */       } 
/* 368 */       return appendable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/* 380 */     public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) { return appendTo(builder, entries.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Map.Entry<?, ?>> entries) {
/*     */       try {
/* 393 */         appendTo(builder, entries);
/* 394 */       } catch (IOException impossible) {
/* 395 */         throw new AssertionError(impossible);
/*     */       } 
/* 397 */       return builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/* 408 */     public String join(Iterable<? extends Map.Entry<?, ?>> entries) { return join(entries.iterator()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/* 419 */     public String join(Iterator<? extends Map.Entry<?, ?>> entries) { return appendTo(new StringBuilder(), entries).toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckReturnValue
/* 428 */     public MapJoiner useForNull(String nullText) { return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator); }
/*     */   }
/*     */ 
/*     */   
/*     */   CharSequence toString(Object part) {
/* 433 */     Preconditions.checkNotNull(part);
/* 434 */     return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
/* 439 */     Preconditions.checkNotNull(rest);
/* 440 */     return new AbstractList<Object>()
/*     */       {
/* 442 */         public int size() { return rest.length + 2; }
/*     */ 
/*     */         
/*     */         public Object get(int index) {
/* 446 */           switch (index) {
/*     */             case 0:
/* 448 */               return first;
/*     */             case 1:
/* 450 */               return second;
/*     */           } 
/* 452 */           return rest[index - 2];
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Joiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */