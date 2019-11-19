/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractNonStreamingHashFunction
/*     */   implements HashFunction
/*     */ {
/*  35 */   public Hasher newHasher() { return new BufferingHasher(32); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  40 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  41 */     return new BufferingHasher(expectedInputSize);
/*     */   }
/*     */ 
/*     */   
/*  45 */   public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) { return newHasher().putObject(instance, funnel).hash(); }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  49 */     int len = input.length();
/*  50 */     Hasher hasher = newHasher(len * 2);
/*  51 */     for (int i = 0; i < len; i++) {
/*  52 */       hasher.putChar(input.charAt(i));
/*     */     }
/*  54 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */   
/*  58 */   public HashCode hashString(CharSequence input, Charset charset) { return hashBytes(input.toString().getBytes(charset)); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public HashCode hashInt(int input) { return newHasher(4).putInt(input).hash(); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public HashCode hashLong(long input) { return newHasher(8).putLong(input).hash(); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public HashCode hashBytes(byte[] input) { return hashBytes(input, 0, input.length); }
/*     */ 
/*     */   
/*     */   private final class BufferingHasher
/*     */     extends AbstractHasher
/*     */   {
/*     */     final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;
/*     */     
/*     */     static final int BOTTOM_BYTE = 255;
/*     */ 
/*     */     
/*  81 */     BufferingHasher(int expectedInputSize) { this.stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize); }
/*     */ 
/*     */ 
/*     */     
/*     */     public Hasher putByte(byte b) {
/*  86 */       this.stream.write(b);
/*  87 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes) {
/*     */       try {
/*  93 */         this.stream.write(bytes);
/*  94 */       } catch (IOException e) {
/*  95 */         throw new RuntimeException(e);
/*     */       } 
/*  97 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/* 102 */       this.stream.write(bytes, off, len);
/* 103 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putShort(short s) {
/* 108 */       this.stream.write(s & 0xFF);
/* 109 */       this.stream.write(s >>> 8 & 0xFF);
/* 110 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putInt(int i) {
/* 115 */       this.stream.write(i & 0xFF);
/* 116 */       this.stream.write(i >>> 8 & 0xFF);
/* 117 */       this.stream.write(i >>> 16 & 0xFF);
/* 118 */       this.stream.write(i >>> 24 & 0xFF);
/* 119 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putLong(long l) {
/* 124 */       for (int i = 0; i < 64; i += 8) {
/* 125 */         this.stream.write((byte)(int)(l >>> i & 0xFFL));
/*     */       }
/* 127 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putChar(char c) {
/* 132 */       this.stream.write(c & 0xFF);
/* 133 */       this.stream.write(c >>> '\b' & 0xFF);
/* 134 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 139 */       funnel.funnel(instance, this);
/* 140 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 145 */     public HashCode hash() { return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length()); }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ExposedByteArrayOutputStream
/*     */     extends ByteArrayOutputStream
/*     */   {
/* 152 */     ExposedByteArrayOutputStream(int expectedInputSize) { super(expectedInputSize); }
/*     */ 
/*     */     
/* 155 */     byte[] byteArray() { return this.buf; }
/*     */ 
/*     */     
/* 158 */     int length() { return this.count; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/AbstractNonStreamingHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */