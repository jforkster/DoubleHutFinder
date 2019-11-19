/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubscriberExceptionContext
/*    */ {
/*    */   private final EventBus eventBus;
/*    */   private final Object event;
/*    */   private final Object subscriber;
/*    */   private final Method subscriberMethod;
/*    */   
/*    */   SubscriberExceptionContext(EventBus eventBus, Object event, Object subscriber, Method subscriberMethod) {
/* 42 */     this.eventBus = (EventBus)Preconditions.checkNotNull(eventBus);
/* 43 */     this.event = Preconditions.checkNotNull(event);
/* 44 */     this.subscriber = Preconditions.checkNotNull(subscriber);
/* 45 */     this.subscriberMethod = (Method)Preconditions.checkNotNull(subscriberMethod);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public EventBus getEventBus() { return this.eventBus; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public Object getEvent() { return this.event; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public Object getSubscriber() { return this.subscriber; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public Method getSubscriberMethod() { return this.subscriberMethod; }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/eventbus/SubscriberExceptionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */