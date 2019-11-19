/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   private OutputStream out;
/*     */   private MemoryOutput memory;
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private MemoryOutput() throws IOException {}
/*     */     
/*  54 */     byte[] getBuffer() { return this.buf; }
/*     */ 
/*     */ 
/*     */     
/*  58 */     int getCount() { return this.count; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  64 */   File getFile() { return this.file; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public FileBackedOutputStream(int fileThreshold) { this(fileThreshold, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
/*  91 */     this.fileThreshold = fileThreshold;
/*  92 */     this.resetOnFinalize = resetOnFinalize;
/*  93 */     this.memory = new MemoryOutput(null);
/*  94 */     this.out = this.memory;
/*     */     
/*  96 */     if (resetOnFinalize) {
/*  97 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException {
/* 100 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */           
/*     */           protected void finalize() throws IOException {
/*     */             try {
/* 105 */               FileBackedOutputStream.this.reset();
/* 106 */             } catch (Throwable t) {
/* 107 */               t.printStackTrace(System.err);
/*     */             } 
/*     */           }
/*     */         };
/*     */     } else {
/* 112 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException {
/* 115 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */         };
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
/* 128 */   public ByteSource asByteSource() { return this.source; }
/*     */ 
/*     */   
/*     */   private InputStream openInputStream() throws IOException {
/* 132 */     if (this.file != null) {
/* 133 */       return new FileInputStream(this.file);
/*     */     }
/* 135 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
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
/*     */   public void reset() throws IOException {
/*     */     try {
/* 149 */       close();
/*     */     } finally {
/* 151 */       if (this.memory == null) {
/* 152 */         this.memory = new MemoryOutput(null);
/*     */       } else {
/* 154 */         this.memory.reset();
/*     */       } 
/* 156 */       this.out = this.memory;
/* 157 */       if (this.file != null) {
/* 158 */         File deleteMe = this.file;
/* 159 */         this.file = null;
/* 160 */         if (!deleteMe.delete()) {
/* 161 */           throw new IOException("Could not delete: " + deleteMe);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(int b) {
/* 168 */     update(1);
/* 169 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/* 173 */   public void write(byte[] b) throws IOException { write(b, 0, b.length); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 178 */     update(len);
/* 179 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/* 183 */   public void close() throws IOException { this.out.close(); }
/*     */ 
/*     */ 
/*     */   
/* 187 */   public void flush() throws IOException { this.out.flush(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void update(int len) {
/* 195 */     if (this.file == null && this.memory.getCount() + len > this.fileThreshold) {
/* 196 */       File temp = File.createTempFile("FileBackedOutputStream", null);
/* 197 */       if (this.resetOnFinalize)
/*     */       {
/*     */         
/* 200 */         temp.deleteOnExit();
/*     */       }
/* 202 */       FileOutputStream transfer = new FileOutputStream(temp);
/* 203 */       transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 204 */       transfer.flush();
/*     */ 
/*     */       
/* 207 */       this.out = transfer;
/* 208 */       this.file = temp;
/* 209 */       this.memory = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/FileBackedOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */