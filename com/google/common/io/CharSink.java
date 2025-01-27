/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharSink
/*     */   extends Object
/*     */   implements OutputSupplier<Writer>
/*     */ {
/*     */   @Deprecated
/*  78 */   public final Writer getOutput() throws IOException { return openStream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Writer openBufferedStream() throws IOException {
/*  94 */     Writer writer = openStream();
/*  95 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(CharSequence charSequence) throws IOException {
/* 106 */     Preconditions.checkNotNull(charSequence);
/*     */     
/* 108 */     closer = Closer.create();
/*     */     try {
/* 110 */       Writer out = (Writer)closer.register(openStream());
/* 111 */       out.append(charSequence);
/* 112 */       out.flush();
/* 113 */     } catch (Throwable e) {
/* 114 */       throw closer.rethrow(e);
/*     */     } finally {
/* 116 */       closer.close();
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
/* 128 */   public void writeLines(Iterable<? extends CharSequence> lines) throws IOException { writeLines(lines, System.getProperty("line.separator")); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
/* 139 */     Preconditions.checkNotNull(lines);
/* 140 */     Preconditions.checkNotNull(lineSeparator);
/*     */     
/* 142 */     closer = Closer.create();
/*     */     try {
/* 144 */       Writer out = (Writer)closer.register(openBufferedStream());
/* 145 */       for (CharSequence line : lines) {
/* 146 */         out.append(line).append(lineSeparator);
/*     */       }
/* 148 */       out.flush();
/* 149 */     } catch (Throwable e) {
/* 150 */       throw closer.rethrow(e);
/*     */     } finally {
/* 152 */       closer.close();
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
/*     */   public long writeFrom(Readable readable) throws IOException {
/* 164 */     Preconditions.checkNotNull(readable);
/*     */     
/* 166 */     closer = Closer.create();
/*     */     try {
/* 168 */       Writer out = (Writer)closer.register(openStream());
/* 169 */       long written = CharStreams.copy(readable, out);
/* 170 */       out.flush();
/* 171 */       return written;
/* 172 */     } catch (Throwable e) {
/* 173 */       throw closer.rethrow(e);
/*     */     } finally {
/* 175 */       closer.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract Writer openStream() throws IOException;
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/CharSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */