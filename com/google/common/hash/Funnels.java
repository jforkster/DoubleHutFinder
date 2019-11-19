/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
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
/*     */ @Beta
/*     */ public final class Funnels
/*     */ {
/*  40 */   public static Funnel<byte[]> byteArrayFunnel() { return ByteArrayFunnel.INSTANCE; }
/*     */   
/*     */   private enum ByteArrayFunnel
/*     */     implements Funnel<byte[]> {
/*  44 */     INSTANCE;
/*     */ 
/*     */     
/*  47 */     public void funnel(byte[] from, PrimitiveSink into) { into.putBytes(from); }
/*     */ 
/*     */ 
/*     */     
/*  51 */     public String toString() { return "Funnels.byteArrayFunnel()"; }
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
/*  63 */   public static Funnel<CharSequence> unencodedCharsFunnel() { return UnencodedCharsFunnel.INSTANCE; }
/*     */   
/*     */   private enum UnencodedCharsFunnel
/*     */     implements Funnel<CharSequence> {
/*  67 */     INSTANCE;
/*     */ 
/*     */     
/*  70 */     public void funnel(CharSequence from, PrimitiveSink into) { into.putUnencodedChars(from); }
/*     */ 
/*     */ 
/*     */     
/*  74 */     public String toString() { return "Funnels.unencodedCharsFunnel()"; }
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
/*  85 */   public static Funnel<CharSequence> stringFunnel(Charset charset) { return new StringCharsetFunnel(charset); }
/*     */   
/*     */   private static class StringCharsetFunnel
/*     */     extends Object
/*     */     implements Funnel<CharSequence>, Serializable {
/*     */     private final Charset charset;
/*     */     
/*  92 */     StringCharsetFunnel(Charset charset) { this.charset = (Charset)Preconditions.checkNotNull(charset); }
/*     */ 
/*     */ 
/*     */     
/*  96 */     public void funnel(CharSequence from, PrimitiveSink into) { into.putString(from, this.charset); }
/*     */ 
/*     */ 
/*     */     
/* 100 */     public String toString() { return "Funnels.stringFunnel(" + this.charset.name() + ")"; }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 104 */       if (o instanceof StringCharsetFunnel) {
/* 105 */         StringCharsetFunnel funnel = (StringCharsetFunnel)o;
/* 106 */         return this.charset.equals(funnel.charset);
/*     */       } 
/* 108 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 112 */     public int hashCode() { return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 116 */     Object writeReplace() { return new SerializedForm(this.charset); }
/*     */     
/*     */     private static class SerializedForm
/*     */       implements Serializable {
/*     */       private final String charsetCanonicalName;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 123 */       SerializedForm(Charset charset) { this.charsetCanonicalName = charset.name(); }
/*     */ 
/*     */ 
/*     */       
/* 127 */       private Object readResolve() { return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName)); }
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
/* 140 */   public static Funnel<Integer> integerFunnel() { return IntegerFunnel.INSTANCE; }
/*     */   
/*     */   private enum IntegerFunnel
/*     */     implements Funnel<Integer> {
/* 144 */     INSTANCE;
/*     */ 
/*     */     
/* 147 */     public void funnel(Integer from, PrimitiveSink into) { into.putInt(from.intValue()); }
/*     */ 
/*     */ 
/*     */     
/* 151 */     public String toString() { return "Funnels.integerFunnel()"; }
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
/* 162 */   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel) { return new SequentialFunnel(elementFunnel); }
/*     */   
/*     */   private static class SequentialFunnel<E>
/*     */     extends Object
/*     */     implements Funnel<Iterable<? extends E>>, Serializable {
/*     */     private final Funnel<E> elementFunnel;
/*     */     
/* 169 */     SequentialFunnel(Funnel<E> elementFunnel) { this.elementFunnel = (Funnel)Preconditions.checkNotNull(elementFunnel); }
/*     */ 
/*     */     
/*     */     public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
/* 173 */       for (E e : from) {
/* 174 */         this.elementFunnel.funnel(e, into);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 179 */     public String toString() { return "Funnels.sequentialFunnel(" + this.elementFunnel + ")"; }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 183 */       if (o instanceof SequentialFunnel) {
/* 184 */         SequentialFunnel<?> funnel = (SequentialFunnel)o;
/* 185 */         return this.elementFunnel.equals(funnel.elementFunnel);
/*     */       } 
/* 187 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 191 */     public int hashCode() { return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public static Funnel<Long> longFunnel() { return LongFunnel.INSTANCE; }
/*     */   
/*     */   private enum LongFunnel
/*     */     implements Funnel<Long> {
/* 205 */     INSTANCE;
/*     */ 
/*     */     
/* 208 */     public void funnel(Long from, PrimitiveSink into) { into.putLong(from.longValue()); }
/*     */ 
/*     */ 
/*     */     
/* 212 */     public String toString() { return "Funnels.longFunnel()"; }
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
/* 227 */   public static OutputStream asOutputStream(PrimitiveSink sink) { return new SinkAsStream(sink); }
/*     */   
/*     */   private static class SinkAsStream
/*     */     extends OutputStream {
/*     */     final PrimitiveSink sink;
/*     */     
/* 233 */     SinkAsStream(PrimitiveSink sink) { this.sink = (PrimitiveSink)Preconditions.checkNotNull(sink); }
/*     */ 
/*     */ 
/*     */     
/* 237 */     public void write(int b) { this.sink.putByte((byte)b); }
/*     */ 
/*     */ 
/*     */     
/* 241 */     public void write(byte[] bytes) { this.sink.putBytes(bytes); }
/*     */ 
/*     */ 
/*     */     
/* 245 */     public void write(byte[] bytes, int off, int len) { this.sink.putBytes(bytes, off, len); }
/*     */ 
/*     */ 
/*     */     
/* 249 */     public String toString() { return "Funnels.asOutputStream(" + this.sink + ")"; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/Funnels.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */