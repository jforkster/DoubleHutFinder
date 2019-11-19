/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ByteSource
/*     */   extends Object
/*     */   implements InputSupplier<InputStream>
/*     */ {
/*     */   private static final int BUF_SIZE = 4096;
/*     */   
/*  73 */   public CharSource asCharSource(Charset charset) { return new AsCharSource(charset, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  98 */   public final InputStream getInput() throws IOException { return openStream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openBufferedStream() throws IOException {
/* 114 */     InputStream in = openStream();
/* 115 */     return (in instanceof BufferedInputStream) ? (BufferedInputStream)in : new BufferedInputStream(in);
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
/* 127 */   public ByteSource slice(long offset, long length) { return new SlicedByteSource(offset, length, null); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() throws IOException {
/* 138 */     closer = Closer.create();
/*     */     try {
/* 140 */       InputStream in = (InputStream)closer.register(openStream());
/* 141 */       return (in.read() == -1);
/* 142 */     } catch (Throwable e) {
/* 143 */       throw closer.rethrow(e);
/*     */     } finally {
/* 145 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/* 165 */     closer = Closer.create();
/*     */     try {
/* 167 */       InputStream in = (InputStream)closer.register(openStream());
/* 168 */       return countBySkipping(in);
/* 169 */     } catch (IOException e) {
/*     */     
/*     */     } finally {
/* 172 */       closer.close();
/*     */     } 
/*     */     
/* 175 */     closer = Closer.create();
/*     */     try {
/* 177 */       InputStream in = (InputStream)closer.register(openStream());
/* 178 */       return countByReading(in);
/* 179 */     } catch (Throwable e) {
/* 180 */       throw closer.rethrow(e);
/*     */     } finally {
/* 182 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long countBySkipping(InputStream in) throws IOException {
/* 191 */     long count = 0L;
/*     */ 
/*     */     
/*     */     while (true) {
/* 195 */       long skipped = in.skip(Math.min(in.available(), 2147483647));
/* 196 */       if (skipped <= 0L) {
/* 197 */         if (in.read() == -1)
/* 198 */           return count; 
/* 199 */         if (count == 0L && in.available() == 0)
/*     */         {
/*     */           
/* 202 */           throw new IOException();
/*     */         }
/* 204 */         count++; continue;
/*     */       } 
/* 206 */       count += skipped;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 211 */   private static final byte[] countBuffer = new byte[4096];
/*     */   
/*     */   private long countByReading(InputStream in) throws IOException {
/* 214 */     long count = 0L;
/*     */     long read;
/* 216 */     while ((read = in.read(countBuffer)) != -1L) {
/* 217 */       count += read;
/*     */     }
/* 219 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long copyTo(OutputStream output) throws IOException {
/* 230 */     Preconditions.checkNotNull(output);
/*     */     
/* 232 */     closer = Closer.create();
/*     */     try {
/* 234 */       InputStream in = (InputStream)closer.register(openStream());
/* 235 */       return ByteStreams.copy(in, output);
/* 236 */     } catch (Throwable e) {
/* 237 */       throw closer.rethrow(e);
/*     */     } finally {
/* 239 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long copyTo(ByteSink sink) throws IOException {
/* 250 */     Preconditions.checkNotNull(sink);
/*     */     
/* 252 */     closer = Closer.create();
/*     */     try {
/* 254 */       InputStream in = (InputStream)closer.register(openStream());
/* 255 */       OutputStream out = (OutputStream)closer.register(sink.openStream());
/* 256 */       return ByteStreams.copy(in, out);
/* 257 */     } catch (Throwable e) {
/* 258 */       throw closer.rethrow(e);
/*     */     } finally {
/* 260 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] read() throws IOException {
/* 270 */     closer = Closer.create();
/*     */     try {
/* 272 */       InputStream in = (InputStream)closer.register(openStream());
/* 273 */       return ByteStreams.toByteArray(in);
/* 274 */     } catch (Throwable e) {
/* 275 */       throw closer.rethrow(e);
/*     */     } finally {
/* 277 */       closer.close();
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
/*     */   
/*     */   @Beta
/*     */   public <T> T read(ByteProcessor<T> processor) throws IOException {
/* 292 */     Preconditions.checkNotNull(processor);
/*     */     
/* 294 */     closer = Closer.create();
/*     */     
/* 296 */     try { InputStream in = (InputStream)closer.register(openStream());
/* 297 */       object = ByteStreams.readBytes(in, processor);
/*     */ 
/*     */ 
/*     */       
/* 301 */       return (T)object; } catch (Throwable e) { throw closer.rethrow(e); } finally { closer.close(); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash(HashFunction hashFunction) throws IOException {
/* 311 */     Hasher hasher = hashFunction.newHasher();
/* 312 */     copyTo(Funnels.asOutputStream(hasher));
/* 313 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contentEquals(ByteSource other) throws IOException {
/* 324 */     Preconditions.checkNotNull(other);
/*     */     
/* 326 */     byte[] buf1 = new byte[4096];
/* 327 */     byte[] buf2 = new byte[4096];
/*     */     
/* 329 */     closer = Closer.create(); try {
/*     */       int read1;
/* 331 */       InputStream in1 = (InputStream)closer.register(openStream());
/* 332 */       InputStream in2 = (InputStream)closer.register(other.openStream());
/*     */       do {
/* 334 */         read1 = ByteStreams.read(in1, buf1, 0, 4096);
/* 335 */         int read2 = ByteStreams.read(in2, buf2, 0, 4096);
/* 336 */         if (read1 != read2 || !Arrays.equals(buf1, buf2))
/* 337 */           return false; 
/* 338 */       } while (read1 == 4096);
/* 339 */       return true;
/*     */     
/*     */     }
/* 342 */     catch (Throwable e) {
/* 343 */       throw closer.rethrow(e);
/*     */     } finally {
/* 345 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 361 */   public static ByteSource concat(Iterable<? extends ByteSource> sources) { return new ConcatenatedByteSource(sources); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 383 */   public static ByteSource concat(Iterator<? extends ByteSource> sources) { return concat(ImmutableList.copyOf(sources)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 399 */   public static ByteSource concat(ByteSource... sources) { return concat(ImmutableList.copyOf(sources)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 409 */   public static ByteSource wrap(byte[] b) { return new ByteArrayByteSource(b); }
/*     */   public abstract InputStream openStream() throws IOException;
/*     */   private final class AsCharSource extends CharSource {
/*     */     private final Charset charset;
/*     */     private AsCharSource(Charset charset) { this.charset = (Charset)Preconditions.checkNotNull(charset); } public Reader openStream() throws IOException { return new InputStreamReader(ByteSource.this.openStream(), this.charset); } public String toString() { return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")"; } } private final class SlicedByteSource extends ByteSource {
/*     */     private final long offset; private final long length; private SlicedByteSource(long offset, long length) { Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", new Object[] { Long.valueOf(offset) }); Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", new Object[] { Long.valueOf(length) }); this.offset = offset; this.length = length; } public InputStream openStream() throws IOException { return sliceStream(ByteSource.this.openStream()); } public InputStream openBufferedStream() throws IOException { return sliceStream(ByteSource.this.openBufferedStream()); } private InputStream sliceStream(InputStream in) throws IOException { if (this.offset > 0L)
/*     */         try { ByteStreams.skipFully(in, this.offset); } catch (Throwable e) { Closer closer = Closer.create(); closer.register(in); try { throw closer.rethrow(e); }
/*     */           finally { closer.close(); }
/*     */            }
/* 418 */           return ByteStreams.limit(in, this.length); } public ByteSource slice(long offset, long length) { Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", new Object[] { Long.valueOf(offset) }); Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", new Object[] { Long.valueOf(length) }); long maxLength = this.length - offset; return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength)); } public boolean isEmpty() throws IOException { return (this.length == 0L || super.isEmpty()); } public String toString() { return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")"; } } public static ByteSource empty() { return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 564 */       INSTANCE; } private static class ByteArrayByteSource extends ByteSource {
/*     */     protected final byte[] bytes; protected ByteArrayByteSource(byte[] bytes) { this.bytes = (byte[])Preconditions.checkNotNull(bytes); } public InputStream openStream() throws IOException { return new ByteArrayInputStream(this.bytes); } public InputStream openBufferedStream() throws IOException { return openStream(); } public boolean isEmpty() throws IOException { return (this.bytes.length == 0); } public long size() throws IOException { return this.bytes.length; } public byte[] read() throws IOException { return (byte[])this.bytes.clone(); } public long copyTo(OutputStream output) throws IOException { output.write(this.bytes); return this.bytes.length; } public <T> T read(ByteProcessor<T> processor) throws IOException { processor.processBytes(this.bytes, 0, this.bytes.length); return (T)processor.getResult(); } public HashCode hash(HashFunction hashFunction) throws IOException { return hashFunction.hashBytes(this.bytes); } public String toString() { return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "...") + ")"; } } private static final class EmptyByteSource extends ByteArrayByteSource {
/* 566 */     private static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */ 
/*     */     
/* 569 */     private EmptyByteSource() { super(new byte[0]); }
/*     */ 
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 574 */       Preconditions.checkNotNull(charset);
/* 575 */       return CharSource.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 580 */     public byte[] read() throws IOException { return this.bytes; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 585 */     public String toString() { return "ByteSource.empty()"; }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ConcatenatedByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final Iterable<? extends ByteSource> sources;
/*     */     
/* 594 */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) { this.sources = (Iterable)Preconditions.checkNotNull(sources); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 599 */     public InputStream openStream() throws IOException { return new MultiInputStream(this.sources.iterator()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 604 */       for (ByteSource source : this.sources) {
/* 605 */         if (!source.isEmpty()) {
/* 606 */           return false;
/*     */         }
/*     */       } 
/* 609 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 614 */       long result = 0L;
/* 615 */       for (ByteSource source : this.sources) {
/* 616 */         result += source.size();
/*     */       }
/* 618 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 623 */     public String toString() { return "ByteSource.concat(" + this.sources + ")"; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/ByteSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */