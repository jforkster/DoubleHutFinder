/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ final class Types
/*     */ {
/*  53 */   private static final Function<Type, String> TYPE_TO_STRING = new Function<Type, String>()
/*     */     {
/*     */       public String apply(Type from) {
/*  56 */         return Types.toString(from);
/*     */       }
/*     */     };
/*     */   
/*  60 */   private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */   
/*     */   static Type newArrayType(Type componentType) {
/*  64 */     if (componentType instanceof WildcardType) {
/*  65 */       WildcardType wildcard = (WildcardType)componentType;
/*  66 */       Type[] lowerBounds = wildcard.getLowerBounds();
/*  67 */       Preconditions.checkArgument((lowerBounds.length <= 1), "Wildcard cannot have more than one lower bounds.");
/*  68 */       if (lowerBounds.length == 1) {
/*  69 */         return supertypeOf(newArrayType(lowerBounds[0]));
/*     */       }
/*  71 */       Type[] upperBounds = wildcard.getUpperBounds();
/*  72 */       Preconditions.checkArgument((upperBounds.length == 1), "Wildcard should have only one upper bound.");
/*  73 */       return subtypeOf(newArrayType(upperBounds[0]));
/*     */     } 
/*     */     
/*  76 */     return JavaVersion.CURRENT.newArrayType(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedTypeWithOwner(@Nullable Type ownerType, Class<?> rawType, Type... arguments) {
/*  85 */     if (ownerType == null) {
/*  86 */       return newParameterizedType(rawType, arguments);
/*     */     }
/*     */     
/*  89 */     Preconditions.checkNotNull(arguments);
/*  90 */     Preconditions.checkArgument((rawType.getEnclosingClass() != null), "Owner type for unenclosed %s", new Object[] { rawType });
/*  91 */     return new ParameterizedTypeImpl(ownerType, rawType, arguments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   static ParameterizedType newParameterizedType(Class<?> rawType, Type... arguments) { return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(rawType), rawType, arguments); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final abstract enum ClassOwnership
/*     */   {
/*     */     OWNED_BY_ENCLOSING_CLASS, LOCAL_CLASS_HAS_NO_OWNER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static final ClassOwnership JVM_BEHAVIOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new com/google/common/reflect/Types$ClassOwnership$1
/*     */       //   3: dup
/*     */       //   4: ldc 'OWNED_BY_ENCLOSING_CLASS'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic com/google/common/reflect/Types$ClassOwnership.OWNED_BY_ENCLOSING_CLASS : Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   13: new com/google/common/reflect/Types$ClassOwnership$2
/*     */       //   16: dup
/*     */       //   17: ldc 'LOCAL_CLASS_HAS_NO_OWNER'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic com/google/common/reflect/Types$ClassOwnership.LOCAL_CLASS_HAS_NO_OWNER : Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   26: iconst_2
/*     */       //   27: anewarray com/google/common/reflect/Types$ClassOwnership
/*     */       //   30: dup
/*     */       //   31: iconst_0
/*     */       //   32: getstatic com/google/common/reflect/Types$ClassOwnership.OWNED_BY_ENCLOSING_CLASS : Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   35: aastore
/*     */       //   36: dup
/*     */       //   37: iconst_1
/*     */       //   38: getstatic com/google/common/reflect/Types$ClassOwnership.LOCAL_CLASS_HAS_NO_OWNER : Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   41: aastore
/*     */       //   42: putstatic com/google/common/reflect/Types$ClassOwnership.$VALUES : [Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   45: invokestatic detectJvmBehavior : ()Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   48: putstatic com/google/common/reflect/Types$ClassOwnership.JVM_BEHAVIOR : Lcom/google/common/reflect/Types$ClassOwnership;
/*     */       //   51: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #106	-> 0
/*     */       //   #113	-> 13
/*     */       //   #104	-> 26
/*     */       //   #127	-> 45
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static ClassOwnership detectJvmBehavior() {
/* 131 */       subclass = (new LocalClass<String>() {  }).getClass();
/* 132 */       ParameterizedType parameterizedType = (ParameterizedType)subclass.getGenericSuperclass();
/*     */       
/* 134 */       for (ClassOwnership behavior : values()) {
/* 135 */         if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
/* 136 */           return behavior;
/*     */         }
/*     */       } 
/* 139 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     abstract Class<?> getOwnerType(Class<?> param1Class);
/*     */   }
/*     */ 
/*     */   
/* 149 */   static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type... bounds) { new Type[1][0] = Object.class; return new TypeVariableImpl(declaration, name, (bounds.length == 0) ? new Type[1] : bounds); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 159 */   static WildcardType subtypeOf(Type upperBound) { return new WildcardTypeImpl(new Type[0], new Type[] { upperBound }); }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 164 */   static WildcardType supertypeOf(Type lowerBound) { return new WildcardTypeImpl(new Type[] { lowerBound }, new Type[] { Object.class }); }
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
/* 177 */   static String toString(Type type) { return (type instanceof Class) ? ((Class)type).getName() : type.toString(); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static Type getComponentType(Type type) {
/* 183 */     Preconditions.checkNotNull(type);
/* 184 */     final AtomicReference<Type> result = new AtomicReference<Type>();
/* 185 */     (new TypeVisitor()
/*     */       {
/* 187 */         void visitTypeVariable(TypeVariable<?> t) { result.set(Types.subtypeOfComponentType(t.getBounds())); }
/*     */ 
/*     */         
/* 190 */         void visitWildcardType(WildcardType t) { result.set(Types.subtypeOfComponentType(t.getUpperBounds())); }
/*     */ 
/*     */         
/* 193 */         void visitGenericArrayType(GenericArrayType t) { result.set(t.getGenericComponentType()); }
/*     */         
/*     */         void visitClass(Class<?> t) {
/* 196 */           result.set(t.getComponentType());
/*     */         }
/*     */       }).visit(new Type[] { type });
/* 199 */     return (Type)result.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Type subtypeOfComponentType(Type[] bounds) {
/* 207 */     for (Type bound : bounds) {
/* 208 */       Type componentType = getComponentType(bound);
/* 209 */       if (componentType != null) {
/*     */ 
/*     */         
/* 212 */         if (componentType instanceof Class) {
/* 213 */           Class<?> componentClass = (Class)componentType;
/* 214 */           if (componentClass.isPrimitive()) {
/* 215 */             return componentClass;
/*     */           }
/*     */         } 
/* 218 */         return subtypeOf(componentType);
/*     */       } 
/*     */     } 
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   private static final class GenericArrayTypeImpl
/*     */     implements GenericArrayType, Serializable
/*     */   {
/*     */     private final Type componentType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 230 */     GenericArrayTypeImpl(Type componentType) { this.componentType = Types.JavaVersion.CURRENT.usedInGenericType(componentType); }
/*     */ 
/*     */ 
/*     */     
/* 234 */     public Type getGenericComponentType() { return this.componentType; }
/*     */ 
/*     */ 
/*     */     
/* 238 */     public String toString() { return Types.toString(this.componentType) + "[]"; }
/*     */ 
/*     */ 
/*     */     
/* 242 */     public int hashCode() { return this.componentType.hashCode(); }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 246 */       if (obj instanceof GenericArrayType) {
/* 247 */         GenericArrayType that = (GenericArrayType)obj;
/* 248 */         return Objects.equal(getGenericComponentType(), that.getGenericComponentType());
/*     */       } 
/*     */       
/* 251 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ParameterizedTypeImpl
/*     */     implements ParameterizedType, Serializable
/*     */   {
/*     */     private final Type ownerType;
/*     */     
/*     */     private final ImmutableList<Type> argumentsList;
/*     */     private final Class<?> rawType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ParameterizedTypeImpl(@Nullable Type ownerType, Class<?> rawType, Type[] typeArguments) {
/* 266 */       Preconditions.checkNotNull(rawType);
/* 267 */       Preconditions.checkArgument((typeArguments.length == rawType.getTypeParameters().length));
/* 268 */       Types.disallowPrimitiveType(typeArguments, "type parameter");
/* 269 */       this.ownerType = ownerType;
/* 270 */       this.rawType = rawType;
/* 271 */       this.argumentsList = Types.JavaVersion.CURRENT.usedInGenericType(typeArguments);
/*     */     }
/*     */ 
/*     */     
/* 275 */     public Type[] getActualTypeArguments() { return Types.toArray(this.argumentsList); }
/*     */ 
/*     */ 
/*     */     
/* 279 */     public Type getRawType() { return this.rawType; }
/*     */ 
/*     */ 
/*     */     
/* 283 */     public Type getOwnerType() { return this.ownerType; }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 287 */       StringBuilder builder = new StringBuilder();
/* 288 */       if (this.ownerType != null) {
/* 289 */         builder.append(Types.toString(this.ownerType)).append('.');
/*     */       }
/* 291 */       builder.append(this.rawType.getName()).append('<').append(COMMA_JOINER.join(Iterables.transform(this.argumentsList, TYPE_TO_STRING))).append('>');
/*     */ 
/*     */ 
/*     */       
/* 295 */       return builder.toString();
/*     */     }
/*     */ 
/*     */     
/* 299 */     public int hashCode() { return ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode() ^ this.rawType.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 304 */       if (!(other instanceof ParameterizedType)) {
/* 305 */         return false;
/*     */       }
/* 307 */       ParameterizedType that = (ParameterizedType)other;
/* 308 */       return (getRawType().equals(that.getRawType()) && Objects.equal(getOwnerType(), that.getOwnerType()) && Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TypeVariableImpl<D extends GenericDeclaration>
/*     */     extends Object
/*     */     implements TypeVariable<D>
/*     */   {
/*     */     private final D genericDeclaration;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final ImmutableList<Type> bounds;
/*     */ 
/*     */     
/*     */     TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 325 */       Types.disallowPrimitiveType(bounds, "bound for type variable");
/* 326 */       this.genericDeclaration = (GenericDeclaration)Preconditions.checkNotNull(genericDeclaration);
/* 327 */       this.name = (String)Preconditions.checkNotNull(name);
/* 328 */       this.bounds = ImmutableList.copyOf(bounds);
/*     */     }
/*     */ 
/*     */     
/* 332 */     public Type[] getBounds() { return Types.toArray(this.bounds); }
/*     */ 
/*     */ 
/*     */     
/* 336 */     public D getGenericDeclaration() { return (D)this.genericDeclaration; }
/*     */ 
/*     */ 
/*     */     
/* 340 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 344 */     public String toString() { return this.name; }
/*     */ 
/*     */ 
/*     */     
/* 348 */     public int hashCode() { return this.genericDeclaration.hashCode() ^ this.name.hashCode(); }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 352 */       if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
/*     */         
/* 354 */         if (obj instanceof TypeVariableImpl) {
/* 355 */           TypeVariableImpl<?> that = (TypeVariableImpl)obj;
/* 356 */           return (this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()) && this.bounds.equals(that.bounds));
/*     */         } 
/*     */ 
/*     */         
/* 360 */         return false;
/*     */       } 
/*     */       
/* 363 */       if (obj instanceof TypeVariable) {
/* 364 */         TypeVariable<?> that = (TypeVariable)obj;
/* 365 */         return (this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()));
/*     */       } 
/*     */       
/* 368 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WildcardTypeImpl
/*     */     implements WildcardType, Serializable {
/*     */     private final ImmutableList<Type> lowerBounds;
/*     */     private final ImmutableList<Type> upperBounds;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
/* 379 */       Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
/* 380 */       Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
/* 381 */       this.lowerBounds = Types.JavaVersion.CURRENT.usedInGenericType(lowerBounds);
/* 382 */       this.upperBounds = Types.JavaVersion.CURRENT.usedInGenericType(upperBounds);
/*     */     }
/*     */ 
/*     */     
/* 386 */     public Type[] getLowerBounds() { return Types.toArray(this.lowerBounds); }
/*     */ 
/*     */ 
/*     */     
/* 390 */     public Type[] getUpperBounds() { return Types.toArray(this.upperBounds); }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 394 */       if (obj instanceof WildcardType) {
/* 395 */         WildcardType that = (WildcardType)obj;
/* 396 */         return (this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(that.getUpperBounds())));
/*     */       } 
/*     */       
/* 399 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 403 */     public int hashCode() { return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode(); }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 407 */       StringBuilder builder = new StringBuilder("?");
/* 408 */       for (Type lowerBound : this.lowerBounds) {
/* 409 */         builder.append(" super ").append(Types.toString(lowerBound));
/*     */       }
/* 411 */       for (Type upperBound : Types.filterUpperBounds(this.upperBounds)) {
/* 412 */         builder.append(" extends ").append(Types.toString(upperBound));
/*     */       }
/* 414 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 421 */   private static Type[] toArray(Collection<Type> types) { return (Type[])types.toArray(new Type[types.size()]); }
/*     */ 
/*     */ 
/*     */   
/* 425 */   private static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) { return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class))); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void disallowPrimitiveType(Type[] types, String usedAs) {
/* 430 */     for (Type type : types) {
/* 431 */       if (type instanceof Class) {
/* 432 */         Class<?> cls = (Class)type;
/* 433 */         Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", new Object[] { cls, usedAs });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 444 */   static Class<?> getArrayClass(Class<?> componentType) { return Array.newInstance(componentType, 0).getClass(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final abstract enum JavaVersion
/*     */   {
/*     */     JAVA6, JAVA7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static final JavaVersion CURRENT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new com/google/common/reflect/Types$JavaVersion$2
/*     */       //   3: dup
/*     */       //   4: ldc 'JAVA6'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic com/google/common/reflect/Types$JavaVersion.JAVA6 : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   13: new com/google/common/reflect/Types$JavaVersion$3
/*     */       //   16: dup
/*     */       //   17: ldc 'JAVA7'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic com/google/common/reflect/Types$JavaVersion.JAVA7 : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   26: iconst_2
/*     */       //   27: anewarray com/google/common/reflect/Types$JavaVersion
/*     */       //   30: dup
/*     */       //   31: iconst_0
/*     */       //   32: getstatic com/google/common/reflect/Types$JavaVersion.JAVA6 : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   35: aastore
/*     */       //   36: dup
/*     */       //   37: iconst_1
/*     */       //   38: getstatic com/google/common/reflect/Types$JavaVersion.JAVA7 : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   41: aastore
/*     */       //   42: putstatic com/google/common/reflect/Types$JavaVersion.$VALUES : [Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   45: new com/google/common/reflect/Types$JavaVersion$1
/*     */       //   48: dup
/*     */       //   49: invokespecial <init> : ()V
/*     */       //   52: invokevirtual capture : ()Ljava/lang/reflect/Type;
/*     */       //   55: instanceof java/lang/Class
/*     */       //   58: ifeq -> 67
/*     */       //   61: getstatic com/google/common/reflect/Types$JavaVersion.JAVA7 : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   64: goto -> 70
/*     */       //   67: getstatic com/google/common/reflect/Types$JavaVersion.JAVA6 : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   70: putstatic com/google/common/reflect/Types$JavaVersion.CURRENT : Lcom/google/common/reflect/Types$JavaVersion;
/*     */       //   73: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #450	-> 0
/*     */       //   #465	-> 13
/*     */       //   #448	-> 26
/*     */       //   #479	-> 45
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final ImmutableList<Type> usedInGenericType(Type[] types) {
/* 486 */       ImmutableList.Builder<Type> builder = ImmutableList.builder();
/* 487 */       for (Type type : types) {
/* 488 */         builder.add(usedInGenericType(type));
/*     */       }
/* 490 */       return builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type newArrayType(Type param1Type);
/*     */ 
/*     */     
/*     */     abstract Type usedInGenericType(Type param1Type);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class NativeTypeVariableEquals<X>
/*     */     extends Object
/*     */   {
/* 505 */     static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/reflect/Types.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */