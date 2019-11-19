/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Predicates
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*  59 */   public static <T> Predicate<T> alwaysTrue() { return ObjectPredicate.ALWAYS_TRUE.withNarrowedType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*  67 */   public static <T> Predicate<T> alwaysFalse() { return ObjectPredicate.ALWAYS_FALSE.withNarrowedType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*  76 */   public static <T> Predicate<T> isNull() { return ObjectPredicate.IS_NULL.withNarrowedType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*  85 */   public static <T> Predicate<T> notNull() { return ObjectPredicate.NOT_NULL.withNarrowedType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static <T> Predicate<T> not(Predicate<T> predicate) { return new NotPredicate(predicate); }
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
/* 107 */   public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) { return new AndPredicate(defensiveCopy(components), null); }
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
/* 120 */   public static <T> Predicate<T> and(Predicate... components) { return new AndPredicate(defensiveCopy(components), null); }
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
/* 131 */   public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) { return new AndPredicate(asList((Predicate)Preconditions.checkNotNull(first), (Predicate)Preconditions.checkNotNull(second)), null); }
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
/* 146 */   public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) { return new OrPredicate(defensiveCopy(components), null); }
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
/* 159 */   public static <T> Predicate<T> or(Predicate... components) { return new OrPredicate(defensiveCopy(components), null); }
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
/* 170 */   public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) { return new OrPredicate(asList((Predicate)Preconditions.checkNotNull(first), (Predicate)Preconditions.checkNotNull(second)), null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public static <T> Predicate<T> equalTo(@Nullable T target) { return (target == null) ? isNull() : new IsEqualToPredicate(target, null); }
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
/*     */   @GwtIncompatible("Class.isInstance")
/* 201 */   public static Predicate<Object> instanceOf(Class<?> clazz) { return new InstanceOfPredicate(clazz, null); }
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
/*     */   @GwtIncompatible("Class.isAssignableFrom")
/*     */   @Beta
/* 214 */   public static Predicate<Class<?>> assignableFrom(Class<?> clazz) { return new AssignableFromPredicate(clazz, null); }
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
/* 231 */   public static <T> Predicate<T> in(Collection<? extends T> target) { return new InPredicate(target, null); }
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
/* 242 */   public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) { return new CompositionPredicate(predicate, function, null); }
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
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/* 256 */   public static Predicate<CharSequence> containsPattern(String pattern) { return new ContainsPatternFromStringPredicate(pattern); }
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
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/* 269 */   public static Predicate<CharSequence> contains(Pattern pattern) { return new ContainsPatternPredicate(pattern); }
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
/*     */   final abstract enum ObjectPredicate
/*     */     implements Predicate<Object>
/*     */   {
/*     */     ALWAYS_TRUE, ALWAYS_FALSE, IS_NULL, NOT_NULL;
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
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new com/google/common/base/Predicates$ObjectPredicate$1
/*     */       //   3: dup
/*     */       //   4: ldc 'ALWAYS_TRUE'
/*     */       //   6: iconst_0
/*     */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   10: putstatic com/google/common/base/Predicates$ObjectPredicate.ALWAYS_TRUE : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   13: new com/google/common/base/Predicates$ObjectPredicate$2
/*     */       //   16: dup
/*     */       //   17: ldc 'ALWAYS_FALSE'
/*     */       //   19: iconst_1
/*     */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   23: putstatic com/google/common/base/Predicates$ObjectPredicate.ALWAYS_FALSE : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   26: new com/google/common/base/Predicates$ObjectPredicate$3
/*     */       //   29: dup
/*     */       //   30: ldc 'IS_NULL'
/*     */       //   32: iconst_2
/*     */       //   33: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   36: putstatic com/google/common/base/Predicates$ObjectPredicate.IS_NULL : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   39: new com/google/common/base/Predicates$ObjectPredicate$4
/*     */       //   42: dup
/*     */       //   43: ldc 'NOT_NULL'
/*     */       //   45: iconst_3
/*     */       //   46: invokespecial <init> : (Ljava/lang/String;I)V
/*     */       //   49: putstatic com/google/common/base/Predicates$ObjectPredicate.NOT_NULL : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   52: iconst_4
/*     */       //   53: anewarray com/google/common/base/Predicates$ObjectPredicate
/*     */       //   56: dup
/*     */       //   57: iconst_0
/*     */       //   58: getstatic com/google/common/base/Predicates$ObjectPredicate.ALWAYS_TRUE : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   61: aastore
/*     */       //   62: dup
/*     */       //   63: iconst_1
/*     */       //   64: getstatic com/google/common/base/Predicates$ObjectPredicate.ALWAYS_FALSE : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   67: aastore
/*     */       //   68: dup
/*     */       //   69: iconst_2
/*     */       //   70: getstatic com/google/common/base/Predicates$ObjectPredicate.IS_NULL : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   73: aastore
/*     */       //   74: dup
/*     */       //   75: iconst_3
/*     */       //   76: getstatic com/google/common/base/Predicates$ObjectPredicate.NOT_NULL : Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   79: aastore
/*     */       //   80: putstatic com/google/common/base/Predicates$ObjectPredicate.$VALUES : [Lcom/google/common/base/Predicates$ObjectPredicate;
/*     */       //   83: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #277	-> 0
/*     */       //   #286	-> 13
/*     */       //   #295	-> 26
/*     */       //   #304	-> 39
/*     */       //   #275	-> 52
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
/*     */     
/* 315 */     <T> Predicate<T> withNarrowedType() { return this; }
/*     */   }
/*     */   
/*     */   private static class NotPredicate<T>
/*     */     extends Object
/*     */     implements Predicate<T>, Serializable {
/*     */     final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 324 */     NotPredicate(Predicate<T> predicate) { this.predicate = (Predicate)Preconditions.checkNotNull(predicate); }
/*     */ 
/*     */ 
/*     */     
/* 328 */     public boolean apply(@Nullable T t) { return !this.predicate.apply(t); }
/*     */ 
/*     */     
/* 331 */     public int hashCode() { return this.predicate.hashCode() ^ 0xFFFFFFFF; }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 334 */       if (obj instanceof NotPredicate) {
/* 335 */         NotPredicate<?> that = (NotPredicate)obj;
/* 336 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 338 */       return false;
/*     */     }
/*     */     
/* 341 */     public String toString() { return "Predicates.not(" + this.predicate.toString() + ")"; }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 346 */   private static final Joiner COMMA_JOINER = Joiner.on(',');
/*     */   
/*     */   private static class AndPredicate<T>
/*     */     extends Object implements Predicate<T>, Serializable {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 353 */     private AndPredicate(List<? extends Predicate<? super T>> components) { this.components = components; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 358 */       for (int i = 0; i < this.components.size(); i++) {
/* 359 */         if (!((Predicate)this.components.get(i)).apply(t)) {
/* 360 */           return false;
/*     */         }
/*     */       } 
/* 363 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 367 */     public int hashCode() { return this.components.hashCode() + 306654252; }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 370 */       if (obj instanceof AndPredicate) {
/* 371 */         AndPredicate<?> that = (AndPredicate)obj;
/* 372 */         return this.components.equals(that.components);
/*     */       } 
/* 374 */       return false;
/*     */     }
/*     */     
/* 377 */     public String toString() { return "Predicates.and(" + COMMA_JOINER.join(this.components) + ")"; }
/*     */   }
/*     */   
/*     */   private static class OrPredicate<T>
/*     */     extends Object
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 387 */     private OrPredicate(List<? extends Predicate<? super T>> components) { this.components = components; }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/* 392 */       for (int i = 0; i < this.components.size(); i++) {
/* 393 */         if (((Predicate)this.components.get(i)).apply(t)) {
/* 394 */           return true;
/*     */         }
/*     */       } 
/* 397 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 401 */     public int hashCode() { return this.components.hashCode() + 87855567; }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 404 */       if (obj instanceof OrPredicate) {
/* 405 */         OrPredicate<?> that = (OrPredicate)obj;
/* 406 */         return this.components.equals(that.components);
/*     */       } 
/* 408 */       return false;
/*     */     }
/*     */     
/* 411 */     public String toString() { return "Predicates.or(" + COMMA_JOINER.join(this.components) + ")"; }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class IsEqualToPredicate<T>
/*     */     extends Object
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 422 */     private IsEqualToPredicate(T target) { this.target = target; }
/*     */ 
/*     */ 
/*     */     
/* 426 */     public boolean apply(T t) { return this.target.equals(t); }
/*     */ 
/*     */     
/* 429 */     public int hashCode() { return this.target.hashCode(); }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 432 */       if (obj instanceof IsEqualToPredicate) {
/* 433 */         IsEqualToPredicate<?> that = (IsEqualToPredicate)obj;
/* 434 */         return this.target.equals(that.target);
/*     */       } 
/* 436 */       return false;
/*     */     }
/*     */     
/* 439 */     public String toString() { return "Predicates.equalTo(" + this.target + ")"; }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Class.isInstance")
/*     */   private static class InstanceOfPredicate
/*     */     extends Object
/*     */     implements Predicate<Object>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 451 */     private InstanceOfPredicate(Class<?> clazz) { this.clazz = (Class)Preconditions.checkNotNull(clazz); }
/*     */ 
/*     */ 
/*     */     
/* 455 */     public boolean apply(@Nullable Object o) { return this.clazz.isInstance(o); }
/*     */ 
/*     */     
/* 458 */     public int hashCode() { return this.clazz.hashCode(); }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 461 */       if (obj instanceof InstanceOfPredicate) {
/* 462 */         InstanceOfPredicate that = (InstanceOfPredicate)obj;
/* 463 */         return (this.clazz == that.clazz);
/*     */       } 
/* 465 */       return false;
/*     */     }
/*     */     
/* 468 */     public String toString() { return "Predicates.instanceOf(" + this.clazz.getName() + ")"; }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Class.isAssignableFrom")
/*     */   private static class AssignableFromPredicate
/*     */     extends Object
/*     */     implements Predicate<Class<?>>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 480 */     private AssignableFromPredicate(Class<?> clazz) { this.clazz = (Class)Preconditions.checkNotNull(clazz); }
/*     */ 
/*     */ 
/*     */     
/* 484 */     public boolean apply(Class<?> input) { return this.clazz.isAssignableFrom(input); }
/*     */ 
/*     */     
/* 487 */     public int hashCode() { return this.clazz.hashCode(); }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 490 */       if (obj instanceof AssignableFromPredicate) {
/* 491 */         AssignableFromPredicate that = (AssignableFromPredicate)obj;
/* 492 */         return (this.clazz == that.clazz);
/*     */       } 
/* 494 */       return false;
/*     */     }
/*     */     
/* 497 */     public String toString() { return "Predicates.assignableFrom(" + this.clazz.getName() + ")"; }
/*     */   }
/*     */   
/*     */   private static class InPredicate<T>
/*     */     extends Object
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Collection<?> target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 507 */     private InPredicate(Collection<?> target) { this.target = (Collection)Preconditions.checkNotNull(target); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@Nullable T t) {
/*     */       try {
/* 513 */         return this.target.contains(t);
/* 514 */       } catch (NullPointerException e) {
/* 515 */         return false;
/* 516 */       } catch (ClassCastException e) {
/* 517 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 522 */       if (obj instanceof InPredicate) {
/* 523 */         InPredicate<?> that = (InPredicate)obj;
/* 524 */         return this.target.equals(that.target);
/*     */       } 
/* 526 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 530 */     public int hashCode() { return this.target.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 534 */     public String toString() { return "Predicates.in(" + this.target + ")"; }
/*     */   }
/*     */   
/*     */   private static class CompositionPredicate<A, B>
/*     */     extends Object
/*     */     implements Predicate<A>, Serializable
/*     */   {
/*     */     final Predicate<B> p;
/*     */     final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
/* 546 */       this.p = (Predicate)Preconditions.checkNotNull(p);
/* 547 */       this.f = (Function)Preconditions.checkNotNull(f);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 552 */     public boolean apply(@Nullable A a) { return this.p.apply(this.f.apply(a)); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 556 */       if (obj instanceof CompositionPredicate) {
/* 557 */         CompositionPredicate<?, ?> that = (CompositionPredicate)obj;
/* 558 */         return (this.f.equals(that.f) && this.p.equals(that.p));
/*     */       } 
/* 560 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 564 */     public int hashCode() { return this.f.hashCode() ^ this.p.hashCode(); }
/*     */ 
/*     */ 
/*     */     
/* 568 */     public String toString() { return this.p.toString() + "(" + this.f.toString() + ")"; }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Only used by other GWT-incompatible code.")
/*     */   private static class ContainsPatternPredicate
/*     */     extends Object
/*     */     implements Predicate<CharSequence>, Serializable
/*     */   {
/*     */     final Pattern pattern;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 581 */     ContainsPatternPredicate(Pattern pattern) { this.pattern = (Pattern)Preconditions.checkNotNull(pattern); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 586 */     public boolean apply(CharSequence t) { return this.pattern.matcher(t).find(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 593 */     public int hashCode() { return Objects.hashCode(new Object[] { this.pattern.pattern(), Integer.valueOf(this.pattern.flags()) }); }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 597 */       if (obj instanceof ContainsPatternPredicate) {
/* 598 */         ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
/*     */ 
/*     */ 
/*     */         
/* 602 */         return (Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && Objects.equal(Integer.valueOf(this.pattern.flags()), Integer.valueOf(that.pattern.flags())));
/*     */       } 
/*     */       
/* 605 */       return false;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 609 */       String patternString = Objects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
/*     */ 
/*     */ 
/*     */       
/* 613 */       return "Predicates.contains(" + patternString + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible("Only used by other GWT-incompatible code.")
/*     */   private static class ContainsPatternFromStringPredicate
/*     */     extends ContainsPatternPredicate
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/* 625 */     ContainsPatternFromStringPredicate(String string) { super(Pattern.compile(string)); }
/*     */ 
/*     */ 
/*     */     
/* 629 */     public String toString() { return "Predicates.containsPattern(" + this.pattern.pattern() + ")"; }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 638 */   private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) { return Arrays.asList(new Predicate[] { first, second }); }
/*     */ 
/*     */ 
/*     */   
/* 642 */   private static <T> List<T> defensiveCopy(T... array) { return defensiveCopy(Arrays.asList(array)); }
/*     */ 
/*     */   
/*     */   static <T> List<T> defensiveCopy(Iterable<T> iterable) {
/* 646 */     ArrayList<T> list = new ArrayList<T>();
/* 647 */     for (T element : iterable) {
/* 648 */       list.add(Preconditions.checkNotNull(element));
/*     */     }
/* 650 */     return list;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Predicates.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */