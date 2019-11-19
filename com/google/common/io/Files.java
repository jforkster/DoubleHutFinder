/*      */ package com.google.common.io;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Splitter;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.TreeTraverser;
/*      */ import com.google.common.hash.HashCode;
/*      */ import com.google.common.hash.HashFunction;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.nio.MappedByteBuffer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Beta
/*      */ public final class Files
/*      */ {
/*      */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*      */   
/*      */   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
/*   84 */     Preconditions.checkNotNull(file);
/*   85 */     Preconditions.checkNotNull(charset);
/*   86 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
/*  101 */     Preconditions.checkNotNull(file);
/*  102 */     Preconditions.checkNotNull(charset);
/*  103 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   public static ByteSource asByteSource(File file) { return new FileByteSource(file, null); }
/*      */ 
/*      */   
/*      */   private static final class FileByteSource
/*      */     extends ByteSource
/*      */   {
/*      */     private final File file;
/*      */     
/*  121 */     private FileByteSource(File file) throws IOException { this.file = (File)Preconditions.checkNotNull(file); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  126 */     public FileInputStream openStream() throws IOException { return new FileInputStream(this.file); }
/*      */ 
/*      */ 
/*      */     
/*      */     public long size() throws IOException {
/*  131 */       if (!this.file.isFile()) {
/*  132 */         throw new FileNotFoundException(this.file.toString());
/*      */       }
/*  134 */       return this.file.length();
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] read() throws IOException {
/*  139 */       closer = Closer.create();
/*      */       try {
/*  141 */         FileInputStream in = (FileInputStream)closer.register(openStream());
/*  142 */         return Files.readFile(in, in.getChannel().size());
/*  143 */       } catch (Throwable e) {
/*  144 */         throw closer.rethrow(e);
/*      */       } finally {
/*  146 */         closer.close();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  152 */     public String toString() { return "Files.asByteSource(" + this.file + ")"; }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] readFile(InputStream in, long expectedSize) throws IOException {
/*  164 */     if (expectedSize > 2147483647L) {
/*  165 */       throw new OutOfMemoryError("file is too large to fit in a byte array: " + expectedSize + " bytes");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  171 */     return (expectedSize == 0L) ? ByteStreams.toByteArray(in) : ByteStreams.toByteArray(in, (int)expectedSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  186 */   public static ByteSink asByteSink(File file, FileWriteMode... modes) { return new FileByteSink(file, modes, null); }
/*      */   
/*      */   private static final class FileByteSink
/*      */     extends ByteSink
/*      */   {
/*      */     private final File file;
/*      */     private final ImmutableSet<FileWriteMode> modes;
/*      */     
/*      */     private FileByteSink(File file, FileWriteMode... modes) {
/*  195 */       this.file = (File)Preconditions.checkNotNull(file);
/*  196 */       this.modes = ImmutableSet.copyOf(modes);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  201 */     public FileOutputStream openStream() throws IOException { return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND)); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  206 */     public String toString() { return "Files.asByteSink(" + this.file + ", " + this.modes + ")"; }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  217 */   public static CharSource asCharSource(File file, Charset charset) { return asByteSource(file).asCharSource(charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  232 */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) { return asByteSink(file, modes).asCharSink(charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  247 */   public static InputSupplier<FileInputStream> newInputStreamSupplier(File file) { return ByteStreams.asInputSupplier(asByteSource(file)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  262 */   public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file) { return newOutputStreamSupplier(file, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  280 */   public static OutputSupplier<FileOutputStream> newOutputStreamSupplier(File file, boolean append) { return ByteStreams.asOutputSupplier(asByteSink(file, modes(append))); }
/*      */ 
/*      */ 
/*      */   
/*  284 */   private static FileWriteMode[] modes(boolean append) { new FileWriteMode[1][0] = FileWriteMode.APPEND; return append ? new FileWriteMode[1] : new FileWriteMode[0]; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  303 */   public static InputSupplier<InputStreamReader> newReaderSupplier(File file, Charset charset) { return CharStreams.asInputSupplier(asCharSource(file, charset)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  320 */   public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset) { return newWriterSupplier(file, charset, false); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  340 */   public static OutputSupplier<OutputStreamWriter> newWriterSupplier(File file, Charset charset, boolean append) { return CharStreams.asOutputSupplier(asCharSink(file, charset, modes(append))); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  353 */   public static byte[] toByteArray(File file) throws IOException { return asByteSource(file).read(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  367 */   public static String toString(File file, Charset charset) throws IOException { return asCharSource(file, charset).read(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  384 */   public static void copy(InputSupplier<? extends InputStream> from, File to) throws IOException { ByteStreams.asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0])); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  395 */   public static void write(byte[] from, File to) throws IOException { asByteSink(to, new FileWriteMode[0]).write(from); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  412 */   public static void copy(File from, OutputSupplier<? extends OutputStream> to) throws IOException { asByteSource(from).copyTo(ByteStreams.asByteSink(to)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  423 */   public static void copy(File from, OutputStream to) throws IOException { asByteSource(from).copyTo(to); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(File from, File to) throws IOException {
/*  440 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
/*      */     
/*  442 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  462 */   public static <R extends Readable & java.io.Closeable> void copy(InputSupplier<R> from, File to, Charset charset) throws IOException { CharStreams.asCharSource(from).copyTo(asCharSink(to, charset, new FileWriteMode[0])); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  477 */   public static void write(CharSequence from, File to, Charset charset) throws IOException { asCharSink(to, charset, new FileWriteMode[0]).write(from); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  492 */   public static void append(CharSequence from, File to, Charset charset) throws IOException { write(from, to, charset, true); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  508 */   private static void write(CharSequence from, File to, Charset charset, boolean append) throws IOException { asCharSink(to, charset, modes(append)).write(from); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  528 */   public static <W extends Appendable & java.io.Closeable> void copy(File from, Charset charset, OutputSupplier<W> to) throws IOException { asCharSource(from, charset).copyTo(CharStreams.asCharSink(to)); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  543 */   public static void copy(File from, Charset charset, Appendable to) throws IOException { asCharSource(from, charset).copyTo(to); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equal(File file1, File file2) throws IOException {
/*  552 */     Preconditions.checkNotNull(file1);
/*  553 */     Preconditions.checkNotNull(file2);
/*  554 */     if (file1 == file2 || file1.equals(file2)) {
/*  555 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  563 */     long len1 = file1.length();
/*  564 */     long len2 = file2.length();
/*  565 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/*  566 */       return false;
/*      */     }
/*  568 */     return asByteSource(file1).contentEquals(asByteSource(file2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File createTempDir() {
/*  591 */     baseDir = new File(System.getProperty("java.io.tmpdir"));
/*  592 */     String baseName = System.currentTimeMillis() + "-";
/*      */     
/*  594 */     for (int counter = 0; counter < 10000; counter++) {
/*  595 */       File tempDir = new File(baseDir, baseName + counter);
/*  596 */       if (tempDir.mkdir()) {
/*  597 */         return tempDir;
/*      */       }
/*      */     } 
/*  600 */     throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + '✏' + ')');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void touch(File file) throws IOException {
/*  613 */     Preconditions.checkNotNull(file);
/*  614 */     if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis()))
/*      */     {
/*  616 */       throw new IOException("Unable to update modification time of " + file);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void createParentDirs(File file) throws IOException {
/*  631 */     Preconditions.checkNotNull(file);
/*  632 */     File parent = file.getCanonicalFile().getParentFile();
/*  633 */     if (parent == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  643 */     parent.mkdirs();
/*  644 */     if (!parent.isDirectory()) {
/*  645 */       throw new IOException("Unable to create parent directories of " + file);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void move(File from, File to) throws IOException {
/*  661 */     Preconditions.checkNotNull(from);
/*  662 */     Preconditions.checkNotNull(to);
/*  663 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", new Object[] { from, to });
/*      */ 
/*      */     
/*  666 */     if (!from.renameTo(to)) {
/*  667 */       copy(from, to);
/*  668 */       if (!from.delete()) {
/*  669 */         if (!to.delete()) {
/*  670 */           throw new IOException("Unable to delete " + to);
/*      */         }
/*  672 */         throw new IOException("Unable to delete " + from);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  690 */   public static String readFirstLine(File file, Charset charset) throws IOException { return asCharSource(file, charset).readFirstLine(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> readLines(File file, Charset charset) throws IOException {
/*  712 */     return (List)readLines(file, charset, new LineProcessor<List<String>>() {
/*  713 */           final List<String> result = Lists.newArrayList();
/*      */ 
/*      */           
/*      */           public boolean processLine(String line) {
/*  717 */             this.result.add(line);
/*  718 */             return true;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  723 */           public List<String> getResult() { return this.result; }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  741 */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException { return (T)CharStreams.readLines(newReaderSupplier(file, charset), callback); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  757 */   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException { return (T)ByteStreams.readBytes(newInputStreamSupplier(file), processor); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  771 */   public static HashCode hash(File file, HashFunction hashFunction) throws IOException { return asByteSource(file).hash(hashFunction); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MappedByteBuffer map(File file) throws IOException {
/*  791 */     Preconditions.checkNotNull(file);
/*  792 */     return map(file, FileChannel.MapMode.READ_ONLY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
/*  815 */     Preconditions.checkNotNull(file);
/*  816 */     Preconditions.checkNotNull(mode);
/*  817 */     if (!file.exists()) {
/*  818 */       throw new FileNotFoundException(file.toString());
/*      */     }
/*  820 */     return map(file, mode, file.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws FileNotFoundException, IOException {
/*  846 */     Preconditions.checkNotNull(file);
/*  847 */     Preconditions.checkNotNull(mode);
/*      */     
/*  849 */     closer = Closer.create();
/*      */     try {
/*  851 */       RandomAccessFile raf = (RandomAccessFile)closer.register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
/*      */       
/*  853 */       return map(raf, mode, size);
/*  854 */     } catch (Throwable e) {
/*  855 */       throw closer.rethrow(e);
/*      */     } finally {
/*  857 */       closer.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static MappedByteBuffer map(RandomAccessFile raf, FileChannel.MapMode mode, long size) throws IOException {
/*  863 */     closer = Closer.create();
/*      */     try {
/*  865 */       FileChannel channel = (FileChannel)closer.register(raf.getChannel());
/*  866 */       return channel.map(mode, 0L, size);
/*  867 */     } catch (Throwable e) {
/*  868 */       throw closer.rethrow(e);
/*      */     } finally {
/*  870 */       closer.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String simplifyPath(String pathname) {
/*  896 */     Preconditions.checkNotNull(pathname);
/*  897 */     if (pathname.length() == 0) {
/*  898 */       return ".";
/*      */     }
/*      */ 
/*      */     
/*  902 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/*      */     
/*  904 */     List<String> path = new ArrayList<String>();
/*      */ 
/*      */     
/*  907 */     for (String component : components) {
/*  908 */       if (component.equals("."))
/*      */         continue; 
/*  910 */       if (component.equals("..")) {
/*  911 */         if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
/*  912 */           path.remove(path.size() - 1); continue;
/*      */         } 
/*  914 */         path.add("..");
/*      */         continue;
/*      */       } 
/*  917 */       path.add(component);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  922 */     String result = Joiner.on('/').join(path);
/*  923 */     if (pathname.charAt(0) == '/') {
/*  924 */       result = "/" + result;
/*      */     }
/*      */     
/*  927 */     while (result.startsWith("/../")) {
/*  928 */       result = result.substring(3);
/*      */     }
/*  930 */     if (result.equals("/..")) {
/*  931 */       result = "/";
/*  932 */     } else if ("".equals(result)) {
/*  933 */       result = ".";
/*      */     } 
/*      */     
/*  936 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getFileExtension(String fullName) {
/*  947 */     Preconditions.checkNotNull(fullName);
/*  948 */     String fileName = (new File(fullName)).getName();
/*  949 */     int dotIndex = fileName.lastIndexOf('.');
/*  950 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getNameWithoutExtension(String file) {
/*  964 */     Preconditions.checkNotNull(file);
/*  965 */     String fileName = (new File(file)).getName();
/*  966 */     int dotIndex = fileName.lastIndexOf('.');
/*  967 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  981 */   public static TreeTraverser<File> fileTreeTraverser() { return FILE_TREE_TRAVERSER; }
/*      */ 
/*      */   
/*  984 */   private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>()
/*      */     {
/*      */       public Iterable<File> children(File file)
/*      */       {
/*  988 */         if (file.isDirectory()) {
/*  989 */           File[] files = file.listFiles();
/*  990 */           if (files != null) {
/*  991 */             return Collections.unmodifiableList(Arrays.asList(files));
/*      */           }
/*      */         } 
/*      */         
/*  995 */         return Collections.emptyList();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1000 */       public String toString() { return "Files.fileTreeTraverser()"; }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1010 */   public static Predicate<File> isDirectory() { return FilePredicate.IS_DIRECTORY; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1019 */   public static Predicate<File> isFile() { return FilePredicate.IS_FILE; }
/*      */   
/*      */   private final abstract enum FilePredicate implements Predicate<File> {
/*      */     IS_DIRECTORY, IS_FILE;
/*      */     
/*      */     static  {
/*      */       // Byte code:
/*      */       //   0: new com/google/common/io/Files$FilePredicate$1
/*      */       //   3: dup
/*      */       //   4: ldc 'IS_DIRECTORY'
/*      */       //   6: iconst_0
/*      */       //   7: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   10: putstatic com/google/common/io/Files$FilePredicate.IS_DIRECTORY : Lcom/google/common/io/Files$FilePredicate;
/*      */       //   13: new com/google/common/io/Files$FilePredicate$2
/*      */       //   16: dup
/*      */       //   17: ldc 'IS_FILE'
/*      */       //   19: iconst_1
/*      */       //   20: invokespecial <init> : (Ljava/lang/String;I)V
/*      */       //   23: putstatic com/google/common/io/Files$FilePredicate.IS_FILE : Lcom/google/common/io/Files$FilePredicate;
/*      */       //   26: iconst_2
/*      */       //   27: anewarray com/google/common/io/Files$FilePredicate
/*      */       //   30: dup
/*      */       //   31: iconst_0
/*      */       //   32: getstatic com/google/common/io/Files$FilePredicate.IS_DIRECTORY : Lcom/google/common/io/Files$FilePredicate;
/*      */       //   35: aastore
/*      */       //   36: dup
/*      */       //   37: iconst_1
/*      */       //   38: getstatic com/google/common/io/Files$FilePredicate.IS_FILE : Lcom/google/common/io/Files$FilePredicate;
/*      */       //   41: aastore
/*      */       //   42: putstatic com/google/common/io/Files$FilePredicate.$VALUES : [Lcom/google/common/io/Files$FilePredicate;
/*      */       //   45: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1023	-> 0
/*      */       //   #1035	-> 13
/*      */       //   #1022	-> 26
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/Files.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */