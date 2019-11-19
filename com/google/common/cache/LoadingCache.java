package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Beta
@GwtCompatible
public interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
  V get(K paramK) throws ExecutionException;
  
  V getUnchecked(K paramK) throws ExecutionException;
  
  ImmutableMap<K, V> getAll(Iterable<? extends K> paramIterable) throws ExecutionException;
  
  @Deprecated
  V apply(K paramK) throws ExecutionException;
  
  void refresh(K paramK);
  
  ConcurrentMap<K, V> asMap();
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/cache/LoadingCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */