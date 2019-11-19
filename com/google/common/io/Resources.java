/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Resources
/*     */ {
/*     */   @Deprecated
/*  62 */   public static InputSupplier<InputStream> newInputStreamSupplier(URL url) { return ByteStreams.asInputSupplier(asByteSource(url)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static ByteSource asByteSource(URL url) { return new UrlByteSource(url, null); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class UrlByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final URL url;
/*     */ 
/*     */ 
/*     */     
/*  82 */     private UrlByteSource(URL url) { this.url = (URL)Preconditions.checkNotNull(url); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     public InputStream openStream() throws IOException { return this.url.openStream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     public String toString() { return "Resources.asByteSource(" + this.url + ")"; }
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
/*     */   @Deprecated
/* 110 */   public static InputSupplier<InputStreamReader> newReaderSupplier(URL url, Charset charset) { return CharStreams.asInputSupplier(asCharSource(url, charset)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static CharSource asCharSource(URL url, Charset charset) { return asByteSource(url).asCharSource(charset); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static byte[] toByteArray(URL url) throws IOException { return asByteSource(url).read(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static String toString(URL url, Charset charset) throws IOException { return asCharSource(url, charset).read(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public static <T> T readLines(URL url, Charset charset, LineProcessor<T> callback) throws IOException { return (T)CharStreams.readLines(newReaderSupplier(url, charset), callback); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> readLines(URL url, Charset charset) throws IOException {
/* 183 */     return (List)readLines(url, charset, new LineProcessor<List<String>>() {
/* 184 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 188 */             this.result.add(line);
/* 189 */             return true;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 194 */           public List<String> getResult() { return this.result; }
/*     */         });
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
/* 207 */   public static void copy(URL from, OutputStream to) throws IOException { asByteSource(from).copyTo(to); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(String resourceName) {
/* 225 */     ClassLoader loader = (ClassLoader)Objects.firstNonNull(Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
/*     */ 
/*     */     
/* 228 */     URL url = loader.getResource(resourceName);
/* 229 */     Preconditions.checkArgument((url != null), "resource %s not found.", new Object[] { resourceName });
/* 230 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(Class<?> contextClass, String resourceName) {
/* 240 */     URL url = contextClass.getResource(resourceName);
/* 241 */     Preconditions.checkArgument((url != null), "resource %s relative to %s not found.", new Object[] { resourceName, contextClass.getName() });
/*     */     
/* 243 */     return url;
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/Resources.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */