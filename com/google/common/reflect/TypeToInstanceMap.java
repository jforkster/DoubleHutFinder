package com.google.common.reflect;

import com.google.common.annotations.Beta;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
  @Nullable
  <T extends B> T getInstance(Class<T> paramClass);
  
  @Nullable
  <T extends B> T putInstance(Class<T> paramClass, @Nullable T paramT);
  
  @Nullable
  <T extends B> T getInstance(TypeToken<T> paramTypeToken);
  
  @Nullable
  <T extends B> T putInstance(TypeToken<T> paramTypeToken, @Nullable T paramT);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/TypeToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */