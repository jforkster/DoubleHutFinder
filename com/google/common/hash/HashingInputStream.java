/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class HashingInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final Hasher hasher;
/*     */   
/*     */   public HashingInputStream(HashFunction hashFunction, InputStream in) {
/*  42 */     super((InputStream)Preconditions.checkNotNull(in));
/*  43 */     this.hasher = (Hasher)Preconditions.checkNotNull(hashFunction.newHasher());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  52 */     int b = this.in.read();
/*  53 */     if (b != -1) {
/*  54 */       this.hasher.putByte((byte)b);
/*     */     }
/*  56 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] bytes, int off, int len) throws IOException {
/*  65 */     int numOfBytesRead = this.in.read(bytes, off, len);
/*  66 */     if (numOfBytesRead != -1) {
/*  67 */       this.hasher.putBytes(bytes, off, numOfBytesRead);
/*     */     }
/*  69 */     return numOfBytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public boolean markSupported() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public void reset() throws IOException { throw new IOException("reset not supported"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public HashCode hash() { return this.hasher.hash(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/HashingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */