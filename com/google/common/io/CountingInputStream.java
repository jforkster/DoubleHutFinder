/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public final class CountingInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private long count;
/* 37 */   private long mark = -1L;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public CountingInputStream(@Nullable InputStream in) { super(in); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public long getCount() { return this.count; }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 54 */     int result = this.in.read();
/* 55 */     if (result != -1) {
/* 56 */       this.count++;
/*    */     }
/* 58 */     return result;
/*    */   }
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 62 */     int result = this.in.read(b, off, len);
/* 63 */     if (result != -1) {
/* 64 */       this.count += result;
/*    */     }
/* 66 */     return result;
/*    */   }
/*    */   
/*    */   public long skip(long n) throws IOException {
/* 70 */     long result = this.in.skip(n);
/* 71 */     this.count += result;
/* 72 */     return result;
/*    */   }
/*    */   
/*    */   public void mark(int readlimit) {
/* 76 */     this.in.mark(readlimit);
/* 77 */     this.mark = this.count;
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() throws IOException {
/* 82 */     if (!this.in.markSupported()) {
/* 83 */       throw new IOException("Mark not supported");
/*    */     }
/* 85 */     if (this.mark == -1L) {
/* 86 */       throw new IOException("Mark not set");
/*    */     }
/*    */     
/* 89 */     this.in.reset();
/* 90 */     this.count = this.mark;
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/CountingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */