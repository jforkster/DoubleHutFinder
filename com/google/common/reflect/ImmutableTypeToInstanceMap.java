/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.collect.ForwardingMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
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
/*     */ @Beta
/*     */ public final class ImmutableTypeToInstanceMap<B>
/*     */   extends ForwardingMap<TypeToken<? extends B>, B>
/*     */   implements TypeToInstanceMap<B>
/*     */ {
/*     */   private final ImmutableMap<TypeToken<? extends B>, B> delegate;
/*     */   
/*  38 */   public static <B> ImmutableTypeToInstanceMap<B> of() { return new ImmutableTypeToInstanceMap(ImmutableMap.of()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static <B> Builder<B> builder() { return new Builder(null); }
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
/*     */   @Beta
/*     */   public static final class Builder<B>
/*     */     extends Object
/*     */   {
/*  64 */     private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value) {
/*  74 */       this.mapBuilder.put(TypeToken.of(key), value);
/*  75 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T extends B> Builder<B> put(TypeToken<T> key, T value) {
/*  83 */       this.mapBuilder.put(key.rejectTypeVariables(), value);
/*  84 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     public ImmutableTypeToInstanceMap<B> build() { return new ImmutableTypeToInstanceMap(this.mapBuilder.build(), null); }
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ 
/*     */   
/* 101 */   private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> delegate) { this.delegate = delegate; }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public <T extends B> T getInstance(TypeToken<T> type) { return (T)trustedGet(type.rejectTypeVariables()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public <T extends B> T putInstance(TypeToken<T> type, T value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public <T extends B> T getInstance(Class<T> type) { return (T)trustedGet(TypeToken.of(type)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public <T extends B> T putInstance(Class<T> type, T value) { throw new UnsupportedOperationException(); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   protected Map<TypeToken<? extends B>, B> delegate() { return this.delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   private <T extends B> T trustedGet(TypeToken<T> type) { return (T)this.delegate.get(type); }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/ImmutableTypeToInstanceMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */