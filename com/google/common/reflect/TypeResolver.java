/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class TypeResolver
/*     */ {
/*     */   private final TypeTable typeTable;
/*     */   
/*  60 */   public TypeResolver() { this.typeTable = new TypeTable(); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   private TypeResolver(TypeTable typeTable) { this.typeTable = typeTable; }
/*     */ 
/*     */ 
/*     */   
/*  68 */   static TypeResolver accordingTo(Type type) { return (new TypeResolver()).where(TypeMappingIntrospector.getTypeMappings(type)); }
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
/*     */   public TypeResolver where(Type formal, Type actual) {
/*  91 */     Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
/*  92 */     populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
/*  93 */     return where(mappings);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) { return new TypeResolver(this.typeTable.where(mappings)); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
/* 103 */     if (from.equals(to)) {
/*     */       return;
/*     */     }
/* 106 */     (new TypeVisitor()
/*     */       {
/* 108 */         void visitTypeVariable(TypeVariable<?> typeVariable) { mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to); }
/*     */         
/*     */         void visitWildcardType(WildcardType fromWildcardType) {
/* 111 */           WildcardType toWildcardType = (WildcardType)TypeResolver.expectArgument(WildcardType.class, to);
/* 112 */           Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
/* 113 */           Type[] toUpperBounds = toWildcardType.getUpperBounds();
/* 114 */           Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
/* 115 */           Type[] toLowerBounds = toWildcardType.getLowerBounds();
/* 116 */           Preconditions.checkArgument((fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length), "Incompatible type: %s vs. %s", new Object[] { fromWildcardType, to });
/*     */ 
/*     */ 
/*     */           
/* 120 */           for (int i = 0; i < fromUpperBounds.length; i++) {
/* 121 */             TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
/*     */           }
/* 123 */           for (int i = 0; i < fromLowerBounds.length; i++)
/* 124 */             TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]); 
/*     */         }
/*     */         
/*     */         void visitParameterizedType(ParameterizedType fromParameterizedType) {
/* 128 */           ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument(ParameterizedType.class, to);
/* 129 */           Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", new Object[] { fromParameterizedType, to });
/*     */           
/* 131 */           Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
/* 132 */           Type[] toArgs = toParameterizedType.getActualTypeArguments();
/* 133 */           Preconditions.checkArgument((fromArgs.length == toArgs.length), "%s not compatible with %s", new Object[] { fromParameterizedType, toParameterizedType });
/*     */           
/* 135 */           for (int i = 0; i < fromArgs.length; i++)
/* 136 */             TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]); 
/*     */         }
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType fromArrayType) {
/* 140 */           Type componentType = Types.getComponentType(to);
/* 141 */           Preconditions.checkArgument((componentType != null), "%s is not an array type.", new Object[] { to });
/* 142 */           TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 148 */         void visitClass(Class<?> fromClass) { throw new IllegalArgumentException("No type mapping from " + fromClass); }
/*     */       }).visit(new Type[] { from });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type resolveType(Type type) {
/* 158 */     Preconditions.checkNotNull(type);
/* 159 */     if (type instanceof TypeVariable)
/* 160 */       return this.typeTable.resolve((TypeVariable)type); 
/* 161 */     if (type instanceof ParameterizedType)
/* 162 */       return resolveParameterizedType((ParameterizedType)type); 
/* 163 */     if (type instanceof GenericArrayType)
/* 164 */       return resolveGenericArrayType((GenericArrayType)type); 
/* 165 */     if (type instanceof WildcardType) {
/* 166 */       return resolveWildcardType((WildcardType)type);
/*     */     }
/*     */     
/* 169 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   private Type[] resolveTypes(Type[] types) {
/* 174 */     Type[] result = new Type[types.length];
/* 175 */     for (int i = 0; i < types.length; i++) {
/* 176 */       result[i] = resolveType(types[i]);
/*     */     }
/* 178 */     return result;
/*     */   }
/*     */   
/*     */   private WildcardType resolveWildcardType(WildcardType type) {
/* 182 */     Type[] lowerBounds = type.getLowerBounds();
/* 183 */     Type[] upperBounds = type.getUpperBounds();
/* 184 */     return new Types.WildcardTypeImpl(resolveTypes(lowerBounds), resolveTypes(upperBounds));
/*     */   }
/*     */ 
/*     */   
/*     */   private Type resolveGenericArrayType(GenericArrayType type) {
/* 189 */     Type componentType = type.getGenericComponentType();
/* 190 */     Type resolvedComponentType = resolveType(componentType);
/* 191 */     return Types.newArrayType(resolvedComponentType);
/*     */   }
/*     */   
/*     */   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
/* 195 */     Type owner = type.getOwnerType();
/* 196 */     Type resolvedOwner = (owner == null) ? null : resolveType(owner);
/* 197 */     Type resolvedRawType = resolveType(type.getRawType());
/*     */     
/* 199 */     Type[] args = type.getActualTypeArguments();
/* 200 */     Type[] resolvedArgs = resolveTypes(args);
/* 201 */     return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T expectArgument(Class<T> type, Object arg) {
/*     */     try {
/* 207 */       return (T)type.cast(arg);
/* 208 */     } catch (ClassCastException e) {
/* 209 */       throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TypeTable
/*     */   {
/*     */     private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;
/*     */     
/* 218 */     TypeTable() { this.map = ImmutableMap.of(); }
/*     */ 
/*     */ 
/*     */     
/* 222 */     private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) { this.map = map; }
/*     */ 
/*     */ 
/*     */     
/*     */     final TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings) {
/* 227 */       ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
/* 228 */       builder.putAll(this.map);
/* 229 */       for (Map.Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
/* 230 */         TypeResolver.TypeVariableKey variable = (TypeResolver.TypeVariableKey)mapping.getKey();
/* 231 */         Type type = (Type)mapping.getValue();
/* 232 */         Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", new Object[] { variable });
/* 233 */         builder.put(variable, type);
/*     */       } 
/* 235 */       return new TypeTable(builder.build());
/*     */     }
/*     */     
/*     */     final Type resolve(final TypeVariable<?> var) {
/* 239 */       final TypeTable unguarded = this;
/* 240 */       TypeTable guarded = new TypeTable()
/*     */         {
/*     */           public Type resolveInternal(TypeVariable<?> intermediateVar, TypeTable forDependent) {
/* 243 */             if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
/* 244 */               return intermediateVar;
/*     */             }
/* 246 */             return unguarded.resolveInternal(intermediateVar, forDependent);
/*     */           }
/*     */         };
/* 249 */       return resolveInternal(var, guarded);
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
/*     */     Type resolveInternal(TypeVariable<?> var, TypeTable forDependants) {
/* 261 */       Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
/* 262 */       if (type == null) {
/* 263 */         Type[] bounds = var.getBounds();
/* 264 */         if (bounds.length == 0) {
/* 265 */           return var;
/*     */         }
/* 267 */         Type[] resolvedBounds = (new TypeResolver(forDependants, null)).resolveTypes(bounds);
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
/* 296 */         if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals(bounds, resolvedBounds))
/*     */         {
/* 298 */           return var;
/*     */         }
/* 300 */         return Types.newArtificialTypeVariable(var.getGenericDeclaration(), var.getName(), resolvedBounds);
/*     */       } 
/*     */ 
/*     */       
/* 304 */       return (new TypeResolver(forDependants, null)).resolveType(type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TypeMappingIntrospector
/*     */     extends TypeVisitor {
/* 310 */     private static final TypeResolver.WildcardCapturer wildcardCapturer = new TypeResolver.WildcardCapturer(null);
/*     */     
/* 312 */     private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType) {
/* 320 */       TypeMappingIntrospector introspector = new TypeMappingIntrospector();
/* 321 */       introspector.visit(new Type[] { wildcardCapturer.capture(contextType) });
/* 322 */       return ImmutableMap.copyOf(introspector.mappings);
/*     */     }
/*     */     
/*     */     void visitClass(Class<?> clazz) {
/* 326 */       visit(new Type[] { clazz.getGenericSuperclass() });
/* 327 */       visit(clazz.getGenericInterfaces());
/*     */     }
/*     */     
/*     */     void visitParameterizedType(ParameterizedType parameterizedType) {
/* 331 */       Class<?> rawClass = (Class)parameterizedType.getRawType();
/* 332 */       TypeVariable[] vars = rawClass.getTypeParameters();
/* 333 */       Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 334 */       Preconditions.checkState((vars.length == typeArgs.length));
/* 335 */       for (int i = 0; i < vars.length; i++) {
/* 336 */         map(new TypeResolver.TypeVariableKey(vars[i]), typeArgs[i]);
/*     */       }
/* 338 */       visit(new Type[] { rawClass });
/* 339 */       visit(new Type[] { parameterizedType.getOwnerType() });
/*     */     }
/*     */ 
/*     */     
/* 343 */     void visitTypeVariable(TypeVariable<?> t) { visit(t.getBounds()); }
/*     */ 
/*     */ 
/*     */     
/* 347 */     void visitWildcardType(WildcardType t) { visit(t.getUpperBounds()); }
/*     */ 
/*     */     
/*     */     private void map(TypeResolver.TypeVariableKey var, Type arg) {
/* 351 */       if (this.mappings.containsKey(var)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 360 */       for (Type t = arg; t != null; t = (Type)this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
/* 361 */         if (var.equalsType(t)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 366 */           for (Type x = arg; x != null; x = (Type)this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x)));
/*     */           return;
/*     */         } 
/*     */       } 
/* 370 */       this.mappings.put(var, arg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class WildcardCapturer
/*     */   {
/* 383 */     private final AtomicInteger id = new AtomicInteger();
/*     */     
/*     */     Type capture(Type type) {
/* 386 */       Preconditions.checkNotNull(type);
/* 387 */       if (type instanceof Class) {
/* 388 */         return type;
/*     */       }
/* 390 */       if (type instanceof TypeVariable) {
/* 391 */         return type;
/*     */       }
/* 393 */       if (type instanceof GenericArrayType) {
/* 394 */         GenericArrayType arrayType = (GenericArrayType)type;
/* 395 */         return Types.newArrayType(capture(arrayType.getGenericComponentType()));
/*     */       } 
/* 397 */       if (type instanceof ParameterizedType) {
/* 398 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 399 */         return Types.newParameterizedTypeWithOwner(captureNullable(parameterizedType.getOwnerType()), (Class)parameterizedType.getRawType(), capture(parameterizedType.getActualTypeArguments()));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 404 */       if (type instanceof WildcardType) {
/* 405 */         WildcardType wildcardType = (WildcardType)type;
/* 406 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 407 */         if (lowerBounds.length == 0) {
/* 408 */           Type[] upperBounds = wildcardType.getUpperBounds();
/* 409 */           String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(upperBounds);
/*     */           
/* 411 */           return Types.newArtificialTypeVariable(WildcardCapturer.class, name, wildcardType.getUpperBounds());
/*     */         } 
/*     */ 
/*     */         
/* 415 */         return type;
/*     */       } 
/*     */       
/* 418 */       throw new AssertionError("must have been one of the known types");
/*     */     }
/*     */     
/*     */     private Type captureNullable(@Nullable Type type) {
/* 422 */       if (type == null) {
/* 423 */         return null;
/*     */       }
/* 425 */       return capture(type);
/*     */     }
/*     */     
/*     */     private Type[] capture(Type[] types) {
/* 429 */       Type[] result = new Type[types.length];
/* 430 */       for (int i = 0; i < types.length; i++) {
/* 431 */         result[i] = capture(types[i]);
/*     */       }
/* 433 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private WildcardCapturer() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class TypeVariableKey
/*     */   {
/*     */     private final TypeVariable<?> var;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 455 */     TypeVariableKey(TypeVariable<?> var) { this.var = (TypeVariable)Preconditions.checkNotNull(var); }
/*     */ 
/*     */ 
/*     */     
/* 459 */     public int hashCode() { return Objects.hashCode(new Object[] { this.var.getGenericDeclaration(), this.var.getName() }); }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 463 */       if (obj instanceof TypeVariableKey) {
/* 464 */         TypeVariableKey that = (TypeVariableKey)obj;
/* 465 */         return equalsTypeVariable(that.var);
/*     */       } 
/* 467 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 472 */     public String toString() { return this.var.toString(); }
/*     */ 
/*     */ 
/*     */     
/*     */     static Object forLookup(Type t) {
/* 477 */       if (t instanceof TypeVariable) {
/* 478 */         return new TypeVariableKey((TypeVariable)t);
/*     */       }
/* 480 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsType(Type type) {
/* 489 */       if (type instanceof TypeVariable) {
/* 490 */         return equalsTypeVariable((TypeVariable)type);
/*     */       }
/* 492 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 497 */     private boolean equalsTypeVariable(TypeVariable<?> that) { return (this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName())); }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/TypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */