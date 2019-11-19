package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
public interface HashFunction {
  Hasher newHasher();
  
  Hasher newHasher(int paramInt);
  
  HashCode hashInt(int paramInt);
  
  HashCode hashLong(long paramLong);
  
  HashCode hashBytes(byte[] paramArrayOfByte);
  
  HashCode hashBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  HashCode hashUnencodedChars(CharSequence paramCharSequence);
  
  HashCode hashString(CharSequence paramCharSequence, Charset paramCharset);
  
  <T> HashCode hashObject(T paramT, Funnel<? super T> paramFunnel);
  
  int bits();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/HashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */