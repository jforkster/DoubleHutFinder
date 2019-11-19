package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Beta
public @interface AllowConcurrentEvents {}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/eventbus/AllowConcurrentEvents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */