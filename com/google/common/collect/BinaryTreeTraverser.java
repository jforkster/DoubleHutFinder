/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.BitSet;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class BinaryTreeTraverser<T>
/*     */   extends TreeTraverser<T>
/*     */ {
/*     */   public final Iterable<T> children(final T root) {
/*  59 */     Preconditions.checkNotNull(root);
/*  60 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/*  63 */           return new AbstractIterator<T>()
/*     */             {
/*     */               boolean doneLeft;
/*     */               boolean doneRight;
/*     */               
/*     */               protected T computeNext() {
/*  69 */                 if (!super.doneLeft) {
/*  70 */                   super.doneLeft = true;
/*  71 */                   Optional<T> left = BinaryTreeTraverser.null.this.this$0.leftChild(root);
/*  72 */                   if (left.isPresent()) {
/*  73 */                     return (T)left.get();
/*     */                   }
/*     */                 } 
/*  76 */                 if (!super.doneRight) {
/*  77 */                   super.doneRight = true;
/*  78 */                   Optional<T> right = BinaryTreeTraverser.null.this.this$0.rightChild(root);
/*  79 */                   if (right.isPresent()) {
/*  80 */                     return (T)right.get();
/*     */                   }
/*     */                 } 
/*  83 */                 return (T)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   UnmodifiableIterator<T> preOrderIterator(T root) { return new PreOrderIterator(root); }
/*     */ 
/*     */   
/*     */   private final class PreOrderIterator
/*     */     extends UnmodifiableIterator<T>
/*     */     implements PeekingIterator<T>
/*     */   {
/*     */     private final Deque<T> stack;
/*     */ 
/*     */     
/*     */     PreOrderIterator(T root) {
/* 103 */       this.stack = new ArrayDeque();
/* 104 */       this.stack.addLast(root);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 109 */     public boolean hasNext() { return !this.stack.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public T next() {
/* 114 */       T result = (T)this.stack.removeLast();
/* 115 */       BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(result));
/* 116 */       BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(result));
/* 117 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     public T peek() { return (T)this.stack.getLast(); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   UnmodifiableIterator<T> postOrderIterator(T root) { return new PostOrderIterator(root); }
/*     */ 
/*     */   
/*     */   private final class PostOrderIterator
/*     */     extends UnmodifiableIterator<T>
/*     */   {
/*     */     private final Deque<T> stack;
/*     */     
/*     */     private final BitSet hasExpanded;
/*     */     
/*     */     PostOrderIterator(T root) {
/* 139 */       this.stack = new ArrayDeque();
/* 140 */       this.stack.addLast(root);
/* 141 */       this.hasExpanded = new BitSet();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 146 */     public boolean hasNext() { return !this.stack.isEmpty(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public T next() {
/*     */       while (true) {
/* 152 */         T node = (T)this.stack.getLast();
/* 153 */         boolean expandedNode = this.hasExpanded.get(this.stack.size() - 1);
/* 154 */         if (expandedNode) {
/* 155 */           this.stack.removeLast();
/* 156 */           this.hasExpanded.clear(this.stack.size());
/* 157 */           return node;
/*     */         } 
/* 159 */         this.hasExpanded.set(this.stack.size() - 1);
/* 160 */         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
/* 161 */         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<T> inOrderTraversal(final T root) {
/* 170 */     Preconditions.checkNotNull(root);
/* 171 */     return new FluentIterable<T>()
/*     */       {
/*     */         public UnmodifiableIterator<T> iterator() {
/* 174 */           return new BinaryTreeTraverser.InOrderIterator(super.this$0, root);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private final class InOrderIterator extends AbstractIterator<T> {
/*     */     private final Deque<T> stack;
/*     */     private final BitSet hasExpandedLeft;
/*     */     
/*     */     InOrderIterator(T root) {
/* 184 */       this.stack = new ArrayDeque();
/* 185 */       this.hasExpandedLeft = new BitSet();
/* 186 */       this.stack.addLast(root);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T computeNext() {
/* 191 */       while (!this.stack.isEmpty()) {
/* 192 */         T node = (T)this.stack.getLast();
/* 193 */         if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
/* 194 */           this.stack.removeLast();
/* 195 */           this.hasExpandedLeft.clear(this.stack.size());
/* 196 */           BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(node));
/* 197 */           return node;
/*     */         } 
/* 199 */         this.hasExpandedLeft.set(this.stack.size() - 1);
/* 200 */         BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(node));
/*     */       } 
/*     */       
/* 203 */       return (T)endOfData();
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> void pushIfPresent(Deque<T> stack, Optional<T> node) {
/* 208 */     if (node.isPresent())
/* 209 */       stack.addLast(node.get()); 
/*     */   }
/*     */   
/*     */   public abstract Optional<T> leftChild(T paramT);
/*     */   
/*     */   public abstract Optional<T> rightChild(T paramT);
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/collect/BinaryTreeTraverser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */