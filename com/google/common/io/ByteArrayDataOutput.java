package com.google.common.io;

import java.io.DataOutput;

public interface ByteArrayDataOutput extends DataOutput {
  void write(int paramInt);
  
  void write(byte[] paramArrayOfByte);
  
  void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  void writeBoolean(boolean paramBoolean);
  
  void writeByte(int paramInt);
  
  void writeShort(int paramInt);
  
  void writeChar(int paramInt);
  
  void writeInt(int paramInt);
  
  void writeLong(long paramLong);
  
  void writeFloat(float paramFloat);
  
  void writeDouble(double paramDouble);
  
  void writeChars(String paramString);
  
  void writeUTF(String paramString);
  
  @Deprecated
  void writeBytes(String paramString);
  
  byte[] toByteArray();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/ByteArrayDataOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */