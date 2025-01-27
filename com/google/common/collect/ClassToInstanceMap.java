package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B> {
  <T extends B> T getInstance(Class<T> paramClass);
  
  <T extends B> T putInstance(Class<T> paramClass, @Nullable T paramT);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ClassToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */