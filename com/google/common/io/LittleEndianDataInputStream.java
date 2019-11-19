/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class LittleEndianDataInputStream
/*     */   extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*  53 */   public LittleEndianDataInputStream(InputStream in) { super((InputStream)Preconditions.checkNotNull(in)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public String readLine() { throw new UnsupportedOperationException("readLine is not supported"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public void readFully(byte[] b) throws IOException { ByteStreams.readFully(this, b); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public void readFully(byte[] b, int off, int len) throws IOException { ByteStreams.readFully(this, b, off, len); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public int skipBytes(int n) throws IOException { return (int)this.in.skip(n); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/*  81 */     int b1 = this.in.read();
/*  82 */     if (0 > b1) {
/*  83 */       throw new EOFException();
/*     */     }
/*     */     
/*  86 */     return b1;
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
/*     */   public int readUnsignedShort() throws IOException {
/* 100 */     byte b1 = readAndCheckByte();
/* 101 */     byte b2 = readAndCheckByte();
/*     */     
/* 103 */     return Ints.fromBytes((byte)0, (byte)0, b2, b1);
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
/*     */   public int readInt() throws IOException {
/* 116 */     byte b1 = readAndCheckByte();
/* 117 */     byte b2 = readAndCheckByte();
/* 118 */     byte b3 = readAndCheckByte();
/* 119 */     byte b4 = readAndCheckByte();
/*     */     
/* 121 */     return Ints.fromBytes(b4, b3, b2, b1);
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
/*     */   public long readLong() throws IOException {
/* 134 */     byte b1 = readAndCheckByte();
/* 135 */     byte b2 = readAndCheckByte();
/* 136 */     byte b3 = readAndCheckByte();
/* 137 */     byte b4 = readAndCheckByte();
/* 138 */     byte b5 = readAndCheckByte();
/* 139 */     byte b6 = readAndCheckByte();
/* 140 */     byte b7 = readAndCheckByte();
/* 141 */     byte b8 = readAndCheckByte();
/*     */     
/* 143 */     return Longs.fromBytes(b8, b7, b6, b5, b4, b3, b2, b1);
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
/* 156 */   public float readFloat() throws IOException { return Float.intBitsToFloat(readInt()); }
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
/* 170 */   public double readDouble() throws IOException { return Double.longBitsToDouble(readLong()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public String readUTF() { return (new DataInputStream(this.in)).readUTF(); }
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
/* 188 */   public short readShort() throws IOException { return (short)readUnsignedShort(); }
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
/* 201 */   public char readChar() throws IOException { return (char)readUnsignedShort(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public byte readByte() throws IOException { return (byte)readUnsignedByte(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public boolean readBoolean() throws IOException { return (readUnsignedByte() != 0); }
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
/*     */   private byte readAndCheckByte() throws IOException {
/* 223 */     int b1 = this.in.read();
/*     */     
/* 225 */     if (-1 == b1) {
/* 226 */       throw new EOFException();
/*     */     }
/*     */     
/* 229 */     return (byte)b1;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/LittleEndianDataInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */