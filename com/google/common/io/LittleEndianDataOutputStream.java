/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class LittleEndianDataOutputStream
/*     */   extends FilterOutputStream
/*     */   implements DataOutput
/*     */ {
/*  52 */   public LittleEndianDataOutputStream(OutputStream out) { super(new DataOutputStream((OutputStream)Preconditions.checkNotNull(out))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public void write(byte[] b, int off, int len) throws IOException { this.out.write(b, off, len); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public void writeBoolean(boolean v) throws IOException { ((DataOutputStream)this.out).writeBoolean(v); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public void writeByte(int v) throws IOException { ((DataOutputStream)this.out).writeByte(v); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  75 */   public void writeBytes(String s) throws IOException { ((DataOutputStream)this.out).writeBytes(s); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void writeChar(int v) throws IOException { writeShort(v); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeChars(String s) throws IOException {
/*  96 */     for (int i = 0; i < s.length(); i++) {
/*  97 */       writeChar(s.charAt(i));
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
/* 109 */   public void writeDouble(double v) throws IOException { writeLong(Double.doubleToLongBits(v)); }
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
/* 120 */   public void writeFloat(float v) throws IOException { writeInt(Float.floatToIntBits(v)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInt(int v) throws IOException {
/* 131 */     this.out.write(0xFF & v);
/* 132 */     this.out.write(0xFF & v >> 8);
/* 133 */     this.out.write(0xFF & v >> 16);
/* 134 */     this.out.write(0xFF & v >> 24);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLong(long v) throws IOException {
/* 145 */     byte[] bytes = Longs.toByteArray(Long.reverseBytes(v));
/* 146 */     write(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeShort(int v) throws IOException {
/* 157 */     this.out.write(0xFF & v);
/* 158 */     this.out.write(0xFF & v >> 8);
/*     */   }
/*     */ 
/*     */   
/* 162 */   public void writeUTF(String str) throws IOException { ((DataOutputStream)this.out).writeUTF(str); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public void close() throws IOException { this.out.close(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/LittleEndianDataOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */