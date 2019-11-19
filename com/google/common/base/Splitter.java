/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Splitter
/*     */ {
/*     */   private final CharMatcher trimmer;
/*     */   private final boolean omitEmptyStrings;
/*     */   private final Strategy strategy;
/*     */   private final int limit;
/*     */   
/* 110 */   private Splitter(Strategy strategy) { this(strategy, false, CharMatcher.NONE, 2147483647); }
/*     */ 
/*     */ 
/*     */   
/*     */   private Splitter(Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit) {
/* 115 */     this.strategy = strategy;
/* 116 */     this.omitEmptyStrings = omitEmptyStrings;
/* 117 */     this.trimmer = trimmer;
/* 118 */     this.limit = limit;
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
/* 130 */   public static Splitter on(char separator) { return on(CharMatcher.is(separator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Splitter on(CharMatcher separatorMatcher) {
/* 144 */     Preconditions.checkNotNull(separatorMatcher);
/*     */     
/* 146 */     return new Splitter(new Strategy(separatorMatcher)
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 149 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/* 151 */                 int separatorStart(int start) { return separatorMatcher.indexIn(this.toSplit, start); }
/*     */ 
/*     */ 
/*     */                 
/* 155 */                 int separatorEnd(int separatorPosition) { return separatorPosition + 1; }
/*     */               };
/*     */           }
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
/*     */   public static Splitter on(String separator) {
/* 171 */     Preconditions.checkArgument((separator.length() != 0), "The separator may not be the empty string.");
/*     */ 
/*     */     
/* 174 */     return new Splitter(new Strategy(separator)
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 177 */             return new Splitter.SplittingIterator(splitter, toSplit) {
/*     */                 public int separatorStart(int start) {
/* 179 */                   int separatorLength = separator.length();
/*     */ 
/*     */                   
/* 182 */                   int p = start, last = this.toSplit.length() - separatorLength;
/* 183 */                   for (; p <= last; p++) {
/* 184 */                     int i = 0; while (true) { if (i < separatorLength) {
/* 185 */                         if (this.toSplit.charAt(i + p) != separator.charAt(i))
/*     */                           break;  i++;
/*     */                         continue;
/*     */                       } 
/* 189 */                       return p; }
/*     */                   
/* 191 */                   }  return -1;
/*     */                 }
/*     */ 
/*     */                 
/* 195 */                 public int separatorEnd(int separatorPosition) { return separatorPosition + separator.length(); }
/*     */               };
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.util.regex")
/*     */   public static Splitter on(Pattern separatorPattern) {
/* 216 */     Preconditions.checkNotNull(separatorPattern);
/* 217 */     Preconditions.checkArgument(!separatorPattern.matcher("").matches(), "The pattern may not match the empty string: %s", new Object[] { separatorPattern });
/*     */ 
/*     */     
/* 220 */     return new Splitter(new Strategy(separatorPattern)
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 223 */             final Matcher matcher = separatorPattern.matcher(toSplit);
/* 224 */             return new Splitter.SplittingIterator(splitter, toSplit)
/*     */               {
/* 226 */                 public int separatorStart(int start) { return matcher.find(start) ? matcher.start() : -1; }
/*     */ 
/*     */ 
/*     */                 
/* 230 */                 public int separatorEnd(int separatorPosition) { return matcher.end(); }
/*     */               };
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.util.regex")
/* 254 */   public static Splitter onPattern(String separatorPattern) { return on(Pattern.compile(separatorPattern)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Splitter fixedLength(int length) {
/* 277 */     Preconditions.checkArgument((length > 0), "The length may not be less than 1");
/*     */     
/* 279 */     return new Splitter(new Strategy(length)
/*     */         {
/*     */           public Splitter.SplittingIterator iterator(Splitter splitter, CharSequence toSplit) {
/* 282 */             return new Splitter.SplittingIterator(splitter, toSplit) {
/*     */                 public int separatorStart(int start) {
/* 284 */                   int nextChunkStart = start + length;
/* 285 */                   return (nextChunkStart < this.toSplit.length()) ? nextChunkStart : -1;
/*     */                 }
/*     */ 
/*     */                 
/* 289 */                 public int separatorEnd(int separatorPosition) { return separatorPosition; }
/*     */               };
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/* 316 */   public Splitter omitEmptyStrings() { return new Splitter(this.strategy, true, this.trimmer, this.limit); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public Splitter limit(int limit) {
/* 340 */     Preconditions.checkArgument((limit > 0), "must be greater than zero: %s", new Object[] { Integer.valueOf(limit) });
/* 341 */     return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, limit);
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
/*     */   @CheckReturnValue
/* 356 */   public Splitter trimResults() { return trimResults(CharMatcher.WHITESPACE); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public Splitter trimResults(CharMatcher trimmer) {
/* 373 */     Preconditions.checkNotNull(trimmer);
/* 374 */     return new Splitter(this.strategy, this.omitEmptyStrings, trimmer, this.limit);
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
/*     */   public Iterable<String> split(final CharSequence sequence) {
/* 386 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 388 */     return new Iterable<String>()
/*     */       {
/* 390 */         public Iterator<String> iterator() { return Splitter.this.splittingIterator(sequence); }
/*     */ 
/*     */         
/* 393 */         public String toString() { return Joiner.on(", ").appendTo((new StringBuilder()).append('['), this).append(']').toString(); }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 402 */   private Iterator<String> splittingIterator(CharSequence sequence) { return this.strategy.iterator(this, sequence); }
/*     */ 
/*     */ 
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
/*     */   public List<String> splitToList(CharSequence sequence) {
/* 416 */     Preconditions.checkNotNull(sequence);
/*     */     
/* 418 */     Iterator<String> iterator = splittingIterator(sequence);
/* 419 */     List<String> result = new ArrayList<String>();
/*     */     
/* 421 */     while (iterator.hasNext()) {
/* 422 */       result.add(iterator.next());
/*     */     }
/*     */     
/* 425 */     return Collections.unmodifiableList(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/* 437 */   public MapSplitter withKeyValueSeparator(String separator) { return withKeyValueSeparator(on(separator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/* 449 */   public MapSplitter withKeyValueSeparator(char separator) { return withKeyValueSeparator(on(separator)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @Beta
/* 462 */   public MapSplitter withKeyValueSeparator(Splitter keyValueSplitter) { return new MapSplitter(this, keyValueSplitter, null); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static final class MapSplitter
/*     */   {
/*     */     private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
/*     */ 
/*     */     
/*     */     private final Splitter outerSplitter;
/*     */ 
/*     */     
/*     */     private final Splitter entrySplitter;
/*     */ 
/*     */ 
/*     */     
/*     */     private MapSplitter(Splitter outerSplitter, Splitter entrySplitter) {
/* 480 */       this.outerSplitter = outerSplitter;
/* 481 */       this.entrySplitter = (Splitter)Preconditions.checkNotNull(entrySplitter);
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, String> split(CharSequence sequence) {
/* 500 */       Map<String, String> map = new LinkedHashMap<String, String>();
/* 501 */       for (String entry : this.outerSplitter.split(sequence)) {
/* 502 */         Iterator<String> entryFields = this.entrySplitter.splittingIterator(entry);
/*     */         
/* 504 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", new Object[] { entry });
/* 505 */         String key = (String)entryFields.next();
/* 506 */         Preconditions.checkArgument(!map.containsKey(key), "Duplicate key [%s] found.", new Object[] { key });
/*     */         
/* 508 */         Preconditions.checkArgument(entryFields.hasNext(), "Chunk [%s] is not a valid entry", new Object[] { entry });
/* 509 */         String value = (String)entryFields.next();
/* 510 */         map.put(key, value);
/*     */         
/* 512 */         Preconditions.checkArgument(!entryFields.hasNext(), "Chunk [%s] is not a valid entry", new Object[] { entry });
/*     */       } 
/* 514 */       return Collections.unmodifiableMap(map);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static interface Strategy
/*     */   {
/*     */     Iterator<String> iterator(Splitter param1Splitter, CharSequence param1CharSequence);
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class SplittingIterator
/*     */     extends AbstractIterator<String>
/*     */   {
/*     */     final CharSequence toSplit;
/*     */     
/*     */     final CharMatcher trimmer;
/*     */     
/*     */     final boolean omitEmptyStrings;
/*     */     
/*     */     int offset;
/*     */     
/*     */     int limit;
/*     */ 
/*     */     
/*     */     protected SplittingIterator(Splitter splitter, CharSequence toSplit) {
/* 540 */       this.offset = 0;
/*     */ 
/*     */ 
/*     */       
/* 544 */       this.trimmer = splitter.trimmer;
/* 545 */       this.omitEmptyStrings = splitter.omitEmptyStrings;
/* 546 */       this.limit = splitter.limit;
/* 547 */       this.toSplit = toSplit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String computeNext() {
/* 557 */       int nextStart = this.offset;
/* 558 */       while (this.offset != -1) {
/* 559 */         int end, start = nextStart;
/*     */ 
/*     */         
/* 562 */         int separatorPosition = separatorStart(this.offset);
/* 563 */         if (separatorPosition == -1) {
/* 564 */           end = this.toSplit.length();
/* 565 */           this.offset = -1;
/*     */         } else {
/* 567 */           end = separatorPosition;
/* 568 */           this.offset = separatorEnd(separatorPosition);
/*     */         } 
/* 570 */         if (this.offset == nextStart) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 578 */           this.offset++;
/* 579 */           if (this.offset >= this.toSplit.length()) {
/* 580 */             this.offset = -1;
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 585 */         while (start < end && this.trimmer.matches(this.toSplit.charAt(start))) {
/* 586 */           start++;
/*     */         }
/* 588 */         while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 589 */           end--;
/*     */         }
/*     */         
/* 592 */         if (this.omitEmptyStrings && start == end) {
/*     */           
/* 594 */           nextStart = this.offset;
/*     */           
/*     */           continue;
/*     */         } 
/* 598 */         if (this.limit == 1) {
/*     */ 
/*     */ 
/*     */           
/* 602 */           end = this.toSplit.length();
/* 603 */           this.offset = -1;
/*     */           
/* 605 */           while (end > start && this.trimmer.matches(this.toSplit.charAt(end - 1))) {
/* 606 */             end--;
/*     */           }
/*     */         } else {
/* 609 */           this.limit--;
/*     */         } 
/*     */         
/* 612 */         return this.toSplit.subSequence(start, end).toString();
/*     */       } 
/* 614 */       return (String)endOfData();
/*     */     }
/*     */     
/*     */     abstract int separatorStart(int param1Int);
/*     */     
/*     */     abstract int separatorEnd(int param1Int);
/*     */   }
/*     */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/base/Splitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */