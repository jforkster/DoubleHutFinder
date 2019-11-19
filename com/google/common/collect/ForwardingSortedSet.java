/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingSortedSet<E>
/*     */   extends ForwardingSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*  67 */   public Comparator<? super E> comparator() { return delegate().comparator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public E first() { return (E)delegate().first(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public SortedSet<E> headSet(E toElement) { return delegate().headSet(toElement); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public E last() { return (E)delegate().last(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public SortedSet<E> subSet(E fromElement, E toElement) { return delegate().subSet(fromElement, toElement); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public SortedSet<E> tailSet(E fromElement) { return delegate().tailSet(fromElement); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int unsafeCompare(Object o1, Object o2) {
/*  98 */     Comparator<? super E> comparator = comparator();
/*  99 */     return (comparator == null) ? ((Comparable)o1).compareTo(o2) : comparator.compare(o1, o2);
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
/*     */   @Beta
/*     */   protected boolean standardContains(@Nullable Object object) {
/*     */     try {
/* 115 */       SortedSet<Object> self = this;
/* 116 */       Object ceiling = self.tailSet(object).first();
/* 117 */       return (unsafeCompare(ceiling, object) == 0);
/* 118 */     } catch (ClassCastException e) {
/* 119 */       return false;
/* 120 */     } catch (NoSuchElementException e) {
/* 121 */       return false;
/* 122 */     } catch (NullPointerException e) {
/* 123 */       return false;
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
/*     */   
/*     */   @Beta
/*     */   protected boolean standardRemove(@Nullable Object object) {
/*     */     try {
/* 138 */       SortedSet<Object> self = this;
/* 139 */       Iterator<Object> iterator = self.tailSet(object).iterator();
/* 140 */       if (iterator.hasNext()) {
/* 141 */         Object ceiling = iterator.next();
/* 142 */         if (unsafeCompare(ceiling, object) == 0) {
/* 143 */           iterator.remove();
/* 144 */           return true;
/*     */         } 
/*     */       } 
/* 147 */     } catch (ClassCastException e) {
/* 148 */       return false;
/* 149 */     } catch (NullPointerException e) {
/* 150 */       return false;
/*     */     } 
/* 152 */     return false;
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
/*     */   @Beta
/* 164 */   protected SortedSet<E> standardSubSet(E fromElement, E toElement) { return tailSet(fromElement).headSet(toElement); }
/*     */   
/*     */   protected abstract SortedSet<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingSortedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */