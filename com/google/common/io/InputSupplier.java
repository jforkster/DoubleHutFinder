package com.google.common.io;

import java.io.IOException;

@Deprecated
public interface InputSupplier<T> {
  T getInput() throws IOException;
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/InputSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */