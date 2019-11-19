/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CharSequenceReader
/*     */   extends Reader
/*     */ {
/*     */   private CharSequence seq;
/*     */   private int pos;
/*     */   private int mark;
/*     */   
/*  44 */   public CharSequenceReader(CharSequence seq) { this.seq = (CharSequence)Preconditions.checkNotNull(seq); }
/*     */ 
/*     */   
/*     */   private void checkOpen() throws IOException {
/*  48 */     if (this.seq == null) {
/*  49 */       throw new IOException("reader closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  54 */   private boolean hasRemaining() { return (remaining() > 0); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   private int remaining() { return this.seq.length() - this.pos; }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(CharBuffer target) throws IOException {
/*  63 */     Preconditions.checkNotNull(target);
/*  64 */     checkOpen();
/*  65 */     if (!hasRemaining()) {
/*  66 */       return -1;
/*     */     }
/*  68 */     int charsToRead = Math.min(target.remaining(), remaining());
/*  69 */     for (int i = 0; i < charsToRead; i++) {
/*  70 */       target.put(this.seq.charAt(this.pos++));
/*     */     }
/*  72 */     return charsToRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() {
/*  77 */     checkOpen();
/*  78 */     return hasRemaining() ? this.seq.charAt(this.pos++) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/*  83 */     Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*  84 */     checkOpen();
/*  85 */     if (!hasRemaining()) {
/*  86 */       return -1;
/*     */     }
/*  88 */     int charsToRead = Math.min(len, remaining());
/*  89 */     for (int i = 0; i < charsToRead; i++) {
/*  90 */       cbuf[off + i] = this.seq.charAt(this.pos++);
/*     */     }
/*  92 */     return charsToRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*  97 */     Preconditions.checkArgument((n >= 0L), "n (%s) may not be negative", new Object[] { Long.valueOf(n) });
/*  98 */     checkOpen();
/*  99 */     int charsToSkip = (int)Math.min(remaining(), n);
/* 100 */     this.pos += charsToSkip;
/* 101 */     return charsToSkip;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ready() {
/* 106 */     checkOpen();
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public boolean markSupported() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readAheadLimit) throws IOException {
/* 117 */     Preconditions.checkArgument((readAheadLimit >= 0), "readAheadLimit (%s) may not be negative", new Object[] { Integer.valueOf(readAheadLimit) });
/* 118 */     checkOpen();
/* 119 */     this.mark = this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 124 */     checkOpen();
/* 125 */     this.pos = this.mark;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public void close() throws IOException { this.seq = null; }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/CharSequenceReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */