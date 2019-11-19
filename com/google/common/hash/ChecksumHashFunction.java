/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Supplier;
/*    */ import java.io.Serializable;
/*    */ import java.util.zip.Checksum;
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
/*    */ final class ChecksumHashFunction
/*    */   extends AbstractStreamingHashFunction
/*    */   implements Serializable
/*    */ {
/*    */   private final Supplier<? extends Checksum> checksumSupplier;
/*    */   private final int bits;
/*    */   private final String toString;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ChecksumHashFunction(Supplier<? extends Checksum> checksumSupplier, int bits, String toString) {
/* 37 */     this.checksumSupplier = (Supplier)Preconditions.checkNotNull(checksumSupplier);
/* 38 */     Preconditions.checkArgument((bits == 32 || bits == 64), "bits (%s) must be either 32 or 64", new Object[] { Integer.valueOf(bits) });
/* 39 */     this.bits = bits;
/* 40 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public int bits() { return this.bits; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public Hasher newHasher() { return new ChecksumHasher((Checksum)this.checksumSupplier.get(), null); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public String toString() { return this.toString; }
/*    */ 
/*    */ 
/*    */   
/*    */   private final class ChecksumHasher
/*    */     extends AbstractByteHasher
/*    */   {
/*    */     private final Checksum checksum;
/*    */ 
/*    */ 
/*    */     
/* 66 */     private ChecksumHasher(Checksum checksum) { this.checksum = (Checksum)Preconditions.checkNotNull(checksum); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 71 */     protected void update(byte b) { this.checksum.update(b); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 76 */     protected void update(byte[] bytes, int off, int len) { this.checksum.update(bytes, off, len); }
/*    */ 
/*    */ 
/*    */     
/*    */     public HashCode hash() {
/* 81 */       long value = this.checksum.getValue();
/* 82 */       if (ChecksumHashFunction.this.bits == 32)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 88 */         return HashCode.fromInt((int)value);
/*    */       }
/* 90 */       return HashCode.fromLong(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/hash/ChecksumHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */