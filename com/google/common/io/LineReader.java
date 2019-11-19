/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.nio.CharBuffer;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ public final class LineReader
/*    */ {
/*    */   private final Readable readable;
/*    */   private final Reader reader;
/*    */   private final char[] buf;
/*    */   private final CharBuffer cbuf;
/*    */   private final Queue<String> lines;
/*    */   private final LineBuffer lineBuf;
/*    */   
/*    */   public LineReader(Readable readable) {
/* 41 */     this.buf = new char[4096];
/* 42 */     this.cbuf = CharBuffer.wrap(this.buf);
/*    */     
/* 44 */     this.lines = new LinkedList();
/* 45 */     this.lineBuf = new LineBuffer() {
/*    */         protected void handleLine(String line, String end) {
/* 47 */           LineReader.this.lines.add(line);
/*    */         }
/*    */       };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 56 */     this.readable = (Readable)Preconditions.checkNotNull(readable);
/* 57 */     this.reader = (readable instanceof Reader) ? (Reader)readable : null;
/*    */   }
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
/*    */   public String readLine() throws IOException {
/* 72 */     while (this.lines.peek() == null) {
/* 73 */       this.cbuf.clear();
/*    */ 
/*    */       
/* 76 */       int read = (this.reader != null) ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
/*    */ 
/*    */       
/* 79 */       if (read == -1) {
/* 80 */         this.lineBuf.finish();
/*    */         break;
/*    */       } 
/* 83 */       this.lineBuf.add(this.buf, 0, read);
/*    */     } 
/* 85 */     return (String)this.lines.poll();
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/LineReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */