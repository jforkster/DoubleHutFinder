package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
public interface Hasher extends PrimitiveSink {
  Hasher putByte(byte paramByte);
  
  Hasher putBytes(byte[] paramArrayOfByte);
  
  Hasher putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  Hasher putShort(short paramShort);
  
  Hasher putInt(int paramInt);
  
  Hasher putLong(long paramLong);
  
  Hasher putFloat(float paramFloat);
  
  Hasher putDouble(double paramDouble);
  
  Hasher putBoolean(boolean paramBoolean);
  
  Hasher putChar(char paramChar);
  
  Hasher putUnencodedChars(CharSequence paramCharSequence);
  
  Hasher putString(CharSequence paramCharSequence, Charset paramCharset);
  
  <T> Hasher putObject(T paramT, Funnel<? super T> paramFunnel);
  
  HashCode hash();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/Hasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */