package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
public interface PrimitiveSink {
  PrimitiveSink putByte(byte paramByte);
  
  PrimitiveSink putBytes(byte[] paramArrayOfByte);
  
  PrimitiveSink putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  PrimitiveSink putShort(short paramShort);
  
  PrimitiveSink putInt(int paramInt);
  
  PrimitiveSink putLong(long paramLong);
  
  PrimitiveSink putFloat(float paramFloat);
  
  PrimitiveSink putDouble(double paramDouble);
  
  PrimitiveSink putBoolean(boolean paramBoolean);
  
  PrimitiveSink putChar(char paramChar);
  
  PrimitiveSink putUnencodedChars(CharSequence paramCharSequence);
  
  PrimitiveSink putString(CharSequence paramCharSequence, Charset paramCharset);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/PrimitiveSink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */