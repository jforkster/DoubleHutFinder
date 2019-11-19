/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharSource
/*     */   extends Object
/*     */   implements InputSupplier<Reader>
/*     */ {
/*     */   @Deprecated
/*  94 */   public final Reader getInput() throws IOException { return openStream(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedReader openBufferedStream() throws IOException {
/* 106 */     Reader reader = openStream();
/* 107 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
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
/*     */   public long copyTo(Appendable appendable) throws IOException {
/* 120 */     Preconditions.checkNotNull(appendable);
/*     */     
/* 122 */     closer = Closer.create();
/*     */     try {
/* 124 */       Reader reader = (Reader)closer.register(openStream());
/* 125 */       return CharStreams.copy(reader, appendable);
/* 126 */     } catch (Throwable e) {
/* 127 */       throw closer.rethrow(e);
/*     */     } finally {
/* 129 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long copyTo(CharSink sink) throws IOException {
/* 140 */     Preconditions.checkNotNull(sink);
/*     */     
/* 142 */     closer = Closer.create();
/*     */     try {
/* 144 */       Reader reader = (Reader)closer.register(openStream());
/* 145 */       Writer writer = (Writer)closer.register(sink.openStream());
/* 146 */       return CharStreams.copy(reader, writer);
/* 147 */     } catch (Throwable e) {
/* 148 */       throw closer.rethrow(e);
/*     */     } finally {
/* 150 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String read() throws IOException {
/* 160 */     closer = Closer.create();
/*     */     try {
/* 162 */       Reader reader = (Reader)closer.register(openStream());
/* 163 */       return CharStreams.toString(reader);
/* 164 */     } catch (Throwable e) {
/* 165 */       throw closer.rethrow(e);
/*     */     } finally {
/* 167 */       closer.close();
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
/*     */   @Nullable
/*     */   public String readFirstLine() throws IOException {
/* 181 */     closer = Closer.create();
/*     */     try {
/* 183 */       BufferedReader reader = (BufferedReader)closer.register(openBufferedStream());
/* 184 */       return reader.readLine();
/* 185 */     } catch (Throwable e) {
/* 186 */       throw closer.rethrow(e);
/*     */     } finally {
/* 188 */       closer.close();
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
/*     */   public ImmutableList<String> readLines() throws IOException {
/* 203 */     closer = Closer.create();
/*     */     try {
/* 205 */       BufferedReader reader = (BufferedReader)closer.register(openBufferedStream());
/* 206 */       List<String> result = Lists.newArrayList();
/*     */       String line;
/* 208 */       while ((line = reader.readLine()) != null) {
/* 209 */         result.add(line);
/*     */       }
/* 211 */       return ImmutableList.copyOf(result);
/* 212 */     } catch (Throwable e) {
/* 213 */       throw closer.rethrow(e);
/*     */     } finally {
/* 215 */       closer.close();
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
/*     */   @Beta
/*     */   public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 235 */     Preconditions.checkNotNull(processor);
/*     */     
/* 237 */     closer = Closer.create();
/*     */     
/* 239 */     try { Reader reader = (Reader)closer.register(openStream());
/* 240 */       object = CharStreams.readLines(reader, processor);
/*     */ 
/*     */ 
/*     */       
/* 244 */       return (T)object; } catch (Throwable e) { throw closer.rethrow(e); } finally { closer.close(); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() throws IOException {
/* 256 */     closer = Closer.create();
/*     */     try {
/* 258 */       Reader reader = (Reader)closer.register(openStream());
/* 259 */       return (reader.read() == -1);
/* 260 */     } catch (Throwable e) {
/* 261 */       throw closer.rethrow(e);
/*     */     } finally {
/* 263 */       closer.close();
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
/* 279 */   public static CharSource concat(Iterable<? extends CharSource> sources) { return new ConcatenatedCharSource(sources); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 301 */   public static CharSource concat(Iterator<? extends CharSource> sources) { return concat(ImmutableList.copyOf(sources)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 317 */   public static CharSource concat(CharSource... sources) { return concat(ImmutableList.copyOf(sources)); }
/*     */   public abstract Reader openStream() throws IOException; private static class CharSequenceCharSource extends CharSource {
/*     */     private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r")); private final CharSequence seq; protected CharSequenceCharSource(CharSequence seq) { this.seq = (CharSequence)Preconditions.checkNotNull(seq); } public Reader openStream() throws IOException { return new CharSequenceReader(this.seq); } public String read() throws IOException { return this.seq.toString(); } public boolean isEmpty() throws IOException { return (this.seq.length() == 0); } private Iterable<String> lines() { return new Iterable<String>() { public Iterator<String> iterator() { return new AbstractIterator<String>() {
/*     */                 Iterator<String> lines = LINE_SPLITTER.split(CharSource.CharSequenceCharSource.null.this.this$0.seq).iterator(); protected String computeNext() throws IOException { if (this.lines.hasNext()) { String next = (String)this.lines.next(); if (this.lines.hasNext() || !next.isEmpty())
/*     */                       return next;  }
/*     */                    return (String)endOfData(); }
/*     */               }; } }
/*     */         ; } public String readFirstLine() throws IOException { Iterator<String> lines = lines().iterator(); return lines.hasNext() ? (String)lines.next() : null; } public ImmutableList<String> readLines() throws IOException { return ImmutableList.copyOf(lines()); } public <T> T readLines(LineProcessor<T> processor) throws IOException { for (String line : lines()) {
/*     */         if (!processor.processLine(line))
/*     */           break; 
/*     */       }  return (T)processor.getResult(); } public String toString() throws IOException { return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")"; }
/* 328 */   } public static CharSource wrap(CharSequence charSequence) { return new CharSequenceCharSource(charSequence); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 337 */   public static CharSource empty() { return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 421 */       INSTANCE; }
/*     */   
/* 423 */   private static final class EmptyCharSource extends CharSequenceCharSource { private static final EmptyCharSource INSTANCE = new EmptyCharSource();
/*     */ 
/*     */     
/* 426 */     private EmptyCharSource() { super(""); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     public String toString() throws IOException { return "CharSource.empty()"; } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ConcatenatedCharSource
/*     */     extends CharSource
/*     */   {
/*     */     private final Iterable<? extends CharSource> sources;
/*     */     
/* 440 */     ConcatenatedCharSource(Iterable<? extends CharSource> sources) { this.sources = (Iterable)Preconditions.checkNotNull(sources); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 445 */     public Reader openStream() throws IOException { return new MultiReader(this.sources.iterator()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 450 */       for (CharSource source : this.sources) {
/* 451 */         if (!source.isEmpty()) {
/* 452 */           return false;
/*     */         }
/*     */       } 
/* 455 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 460 */     public String toString() throws IOException { return "CharSource.concat(" + this.sources + ")"; }
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/CharSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */