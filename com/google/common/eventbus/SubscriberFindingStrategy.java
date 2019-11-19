package com.google.common.eventbus;

import com.google.common.collect.Multimap;

interface SubscriberFindingStrategy {
  Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object paramObject);
}


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/eventbus/SubscriberFindingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */