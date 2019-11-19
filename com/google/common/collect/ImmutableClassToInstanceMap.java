/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ImmutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*     */   private final ImmutableMap<Class<? extends B>, B> delegate;
/*     */   
/*  44 */   public static <B> Builder<B> builder() { return new Builder(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<B>
/*     */     extends Object
/*     */   {
/*  65 */     private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value) {
/*  73 */       this.mapBuilder.put(key, value);
/*  74 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
/*  88 */       for (Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
/*  89 */         Class<? extends T> type = (Class)entry.getKey();
/*  90 */         T value = (T)entry.getValue();
/*  91 */         this.mapBuilder.put(type, cast(type, value));
/*     */       } 
/*  93 */       return this;
/*     */     }
/*     */ 
/*     */     
/*  97 */     private static <B, T extends B> T cast(Class<T> type, B value) { return (T)Primitives.wrap(type).cast(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     public ImmutableClassToInstanceMap<B> build() { return new ImmutableClassToInstanceMap(this.mapBuilder.build(), null); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
/* 126 */     if (map instanceof ImmutableClassToInstanceMap)
/*     */     {
/*     */       
/* 129 */       return (ImmutableClassToInstanceMap)map;
/*     */     }
/*     */     
/* 132 */     return (new Builder()).putAll(map).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate) { this.delegate = delegate; }
/*     */ 
/*     */ 
/*     */   
/* 143 */   protected Map<Class<? extends B>, B> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/* 150 */   public <T extends B> T getInstance(Class<T> type) { return (T)this.delegate.get(Preconditions.checkNotNull(type)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 162 */   public <T extends B> T putInstance(Class<T> type, T value) { throw new UnsupportedOperationException(); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ImmutableClassToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */