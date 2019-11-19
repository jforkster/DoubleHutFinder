/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public final class CountingOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private long count;
/*    */   
/* 44 */   public CountingOutputStream(@Nullable OutputStream out) { super(out); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public long getCount() { return this.count; }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 53 */     this.out.write(b, off, len);
/* 54 */     this.count += len;
/*    */   }
/*    */   
/*    */   public void write(int b) throws IOException {
/* 58 */     this.out.write(b);
/* 59 */     this.count++;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 66 */   public void close() throws IOException { this.out.close(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/CountingOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */