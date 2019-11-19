package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface OutputSupplier<T> {
  T getOutput() throws IOException;
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/OutputSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */