package com.google.common.io;

import java.io.DataInput;

public interface ByteArrayDataInput extends DataInput {
  void readFully(byte[] paramArrayOfByte);
  
  void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  int skipBytes(int paramInt);
  
  boolean readBoolean();
  
  byte readByte();
  
  int readUnsignedByte();
  
  short readShort();
  
  int readUnsignedShort();
  
  char readChar();
  
  int readInt();
  
  long readLong();
  
  float readFloat();
  
  double readDouble();
  
  String readLine();
  
  String readUTF();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/ByteArrayDataInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */