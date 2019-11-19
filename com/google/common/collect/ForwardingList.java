/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingList<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements List<E>
/*     */ {
/*  66 */   public void add(int index, E element) { delegate().add(index, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public boolean addAll(int index, Collection<? extends E> elements) { return delegate().addAll(index, elements); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public E get(int index) { return (E)delegate().get(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public int indexOf(Object element) { return delegate().indexOf(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public int lastIndexOf(Object element) { return delegate().lastIndexOf(element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public ListIterator<E> listIterator() { return delegate().listIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public ListIterator<E> listIterator(int index) { return delegate().listIterator(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public E remove(int index) { return (E)delegate().remove(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public E set(int index, E element) { return (E)delegate().set(index, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public List<E> subList(int fromIndex, int toIndex) { return delegate().subList(fromIndex, toIndex); }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean equals(@Nullable Object object) { return (object == this || delegate().equals(object)); }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public int hashCode() { return delegate().hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAdd(E element) {
/* 131 */     add(size(), element);
/* 132 */     return true;
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
/* 145 */   protected boolean standardAddAll(int index, Iterable<? extends E> elements) { return Lists.addAllImpl(this, index, elements); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   protected int standardIndexOf(@Nullable Object element) { return Lists.indexOfImpl(this, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 168 */   protected int standardLastIndexOf(@Nullable Object element) { return Lists.lastIndexOfImpl(this, element); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   protected Iterator<E> standardIterator() { return listIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   protected ListIterator<E> standardListIterator() { return listIterator(0); }
/*     */ 
/*     */ 
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
/* 204 */   protected ListIterator<E> standardListIterator(int start) { return Lists.listIteratorImpl(this, start); }
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
/* 215 */   protected List<E> standardSubList(int fromIndex, int toIndex) { return Lists.subListImpl(this, fromIndex, toIndex); }
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
/* 226 */   protected boolean standardEquals(@Nullable Object object) { return Lists.equalsImpl(this, object); }
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
/* 237 */   protected int standardHashCode() { return Lists.hashCodeImpl(this); }
/*     */   
/*     */   protected abstract List<E> delegate();
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/ForwardingList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */