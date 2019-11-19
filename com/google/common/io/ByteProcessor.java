package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
public interface ByteProcessor<T> {
  boolean processBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;
  
  T getResult();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/ByteProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */