/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.annotation.Nullable;
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
/*    */ @Beta
/*    */ public final class PatternFilenameFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   private final Pattern pattern;
/*    */   
/* 48 */   public PatternFilenameFilter(String patternStr) { this(Pattern.compile(patternStr)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public PatternFilenameFilter(Pattern pattern) { this.pattern = (Pattern)Preconditions.checkNotNull(pattern); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public boolean accept(@Nullable File dir, String fileName) { return this.pattern.matcher(fileName).matches(); }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/com/google/common/io/PatternFilenameFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */