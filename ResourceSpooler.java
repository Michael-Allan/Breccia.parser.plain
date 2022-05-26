package Breccia.parser.plain;

import Breccia.parser.TagName;
import java.util.ArrayList;
import java.util.function.Supplier;


/** A dispensary of reusable resources of various types.
  */
final class ResourceSpooler {


    ResourceSpooler( final BrecciaCursor c ) {
        final ArrayList<Spool<?>> ss = new ArrayList<>();
        ss.add( flatMarkup         = new Spool<>( () -> FlatMarkup.make( c )));
        ss.add( backslashedSpecial = new Spool<>( () -> FlatMarkup.make( c, "BackslashedSpecial" )));
        ss.add( divisionLabel      = new Spool<>( () -> FlatMarkup.make( c, "DivisionLabel" )));
        ss.add( groupDelimiter     = new Spool<>( () -> FlatMarkup.make( c, "GroupDelimiter" )));
        ss.add( literalizer        = new Spool<>( () -> FlatMarkup.make( c, "Literalizer" )));
        ss.add( metacharacter      = new Spool<>( () -> FlatMarkup.make( c, "Metacharacter" )));
        ss.add( perfectIndent      = new Spool<>( () -> FlatMarkup.make( c, "PerfectIndent" )));
        ss.add( commentAppender    = new Spool<>( () -> new CommentAppender_   ( c )));
        ss.add( commentBlock       = new Spool<>( () -> new CommentBlock_      ( c )));
        ss.add( commentBlockLine   = new Spool<>( () -> new CommentBlock_.Line_( c )));
        ss.add( indentBlind        = new Spool<>( () -> new IndentBlind_       ( c )));
        ss.add( indentBlindLine    = new Spool<>( () -> new IndentBlind_.Line_ ( c )));
        ss.add( patternMatcher     = new Spool<>( () -> new PatternMatcher_    ( c )));
        spools = ss.toArray( spoolArrayType ); } /* Bypassing the list interface
          in favour of a bare array, because speed of iteration matters here. */



    /** Spool of flat-markup instances, each reflective of a ‘backslash sequence’,
      * a sequence commencing with a backslash that has special meaning in a regular-expression pattern.
      *
      *     @see <a href='https://perldoc.perl.org/perlrebackslash#The-backslash'>The backslash</a>
      *     @see #literalizer
      */
    final Spool<@TagName("BackslashedSpecial") FlatMarkup> backslashedSpecial;



    /** Spool of comment appenders.
      */
    final Spool<CommentAppender_> commentAppender;



    /** Spool of comment blocks.
      */
    final Spool<CommentBlock_> commentBlock;



    /** Spool of comment-block lines.
      */
    final Spool<CommentBlock_.Line_> commentBlockLine;



    /** Spool of flat-markup instances, each reflective of a division label.
      */
    final Spool<@TagName("DivisionLabel") FlatMarkup> divisionLabel;



    /** Spool of generic flat-markup instances, each with a tag name of ‘Markup’.
      *
      *     @see Breccia.parser.Markup#tagName()
      */
    final Spool<FlatMarkup> flatMarkup;



    /** Spool of flat-markup instances, each reflective of a group delimiter within
      * a regular-expression pattern, one of ‘(’, ‘(?:’ or ‘)’.
      */
    final Spool<@TagName("GroupDelimiter") FlatMarkup> groupDelimiter;



    /** Spool of indent blinds.
      */
    final Spool<IndentBlind_> indentBlind;



    /** Spool of indent-blind lines.
      */
    final Spool<IndentBlind_.Line_> indentBlindLine;



    /** Spool of flat-markup instances, each reflective of a literalizing backslash within
      * a regular-expression pattern, one that ‘takes away [any] special meaning
      * of the character following it’.
      *
      *     @see <a href='https://perldoc.perl.org/perlrebackslash#The-backslash'>The backslash</a>
      *     @see #backslashedSpecial
      */
    final Spool<@TagName("Literalizer") FlatMarkup> literalizer;



    /** Spool of flat-markup instances, each reflective of a metacharacter within
      * a regular-expression pattern.
      *
      *     @see <a href='https://perldoc.perl.org/perlre#Metacharacters'>Metacharacters</a>
      */
    final Spool<@TagName("Metacharacter") FlatMarkup> metacharacter;



    /** Spool of pattern matchers.
      */
    final Spool<PatternMatcher_> patternMatcher;



    /** Spool of flat-markup instances, each reflective of a perfect indent ‘^^’ within
      * a regular-expression pattern.
      */
    final Spool<@TagName("PerfectIndent") FlatMarkup> perfectIndent;



    /** Rewinds all spools, making all resources ready for reuse.
      * Do not call this method if a previously dispensed resource remains in use.
      *
      *     @see Spool#rewind()
      */
    void rewind() { for( Spool<?> s: spools ) s.rewind(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A dispenser of reusable resources of type `R`.
      */
    static final class Spool<R> {


        /** @see #source
          */
        Spool( Supplier<R> source ) { this.source = source; }



        private int r = 0; // Next to unwind.



        private ArrayList<R> resources = new ArrayList<>();



        /** Effectively winds all previously dispensed resources back onto this spool,
          * ready for redispensing and reuse.
          *
          *     @see ResourceSpooler#rewind()
          */
        void rewind() { r = 0; }



        /** Where to get new instances of the resource.
          */
        private final Supplier<R> source;



        /** Dispenses a single instance of the resource.
          */
        R unwind() {
            final R res;
            if( r < resources.size() ) res = resources.get( r );
            else resources.add( res = source.get() );
            ++r;
            return res; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private static final Spool<?>[] spoolArrayType = new Spool<?>[0];



    private final Spool<?>[] spools; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
