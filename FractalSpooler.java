package Breccia.parser.plain;

import Breccia.parser.TagName;
import java.util.ArrayList;


/** A spooler of per-fractum resources.  Use them in one fractum, then rewind them for the next.
  *
  *     @see Fractum_
  */
final class FractalSpooler extends Spooler {


    FractalSpooler( final BrecciaCursor c ) {
        final ArrayList<Spool<?>> ss = new ArrayList<>();
        ss.add( flatMarkup          = new Spool<>( () -> FlatMarkup.make( c )));
        ss.add( backslashedSpecial  = new Spool<>( () -> FlatMarkup.make( c, "BackslashedSpecial" )));
        ss.add( containmentOperator = new Spool<>( () -> FlatMarkup.make( c, "ContainmentOperator" )));
        ss.add( divisionLabel       = new Spool<>( () -> FlatMarkup.make( c, "DivisionLabel" )));
        ss.add( groupDelimiter      = new Spool<>( () -> FlatMarkup.make( c, "GroupDelimiter" )));
        ss.add( literalizer         = new Spool<>( () -> FlatMarkup.make( c, "Literalizer" )));
        ss.add( metacharacter       = new Spool<>( () -> FlatMarkup.make( c, "Metacharacter" )));
        ss.add( perfectIndentMarker = new Spool<>( () -> FlatMarkup.make( c, "PerfectIndentMarker" )));
        ss.add( commentAppender     = new Spool<>( () -> new CommentAppender_   ( c )));
        ss.add( commentBlock        = new Spool<>( () -> new CommentBlock_      ( c )));
        ss.add( commentBlockLine    = new Spool<>( () -> new CommentBlock_.Line_( c )));
        ss.add( indentBlind         = new Spool<>( () -> new IndentBlind_       ( c )));
        ss.add( indentBlindLine     = new Spool<>( () -> new IndentBlind_.Line_ ( c )));
        ss.add( patternMatcher      = new Spool<>( () -> new PatternMatcher_    ( c )));
        initialize( ss ); }



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



    /** Spool of flat-markup instances, each reflective of a containment operator.
      */
    final Spool<@TagName("ContainmentOperator") FlatMarkup> containmentOperator;



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
    final Spool<@TagName("PerfectIndentMarker") FlatMarkup> perfectIndentMarker; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
