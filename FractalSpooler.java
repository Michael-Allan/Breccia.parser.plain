package Breccia.parser.plain;

import Breccia.parser.TagName;
import java.util.ArrayList;

import static Breccia.parser.plain.PatternMatcher_.*;


/** A spooler of per-fractum resources.  Use them in one fractum, then rewind them for the next.
  *
  *     @see Fractum_
  */
final class FractalSpooler extends Spooler {


    FractalSpooler( final BrecciaCursor c ) {
        final ArrayList<Spool<?>> ss = new ArrayList<>();
        ss.add( flatGranum          = new Spool<>( () -> FlatGranum.make( c )));
        ss.add( containmentOperator = new Spool<>( () -> FlatGranum.make( c, "ContainmentOperator" )));
        ss.add( divisionLabel       = new Spool<>( () -> FlatGranum.make( c, "DivisionLabel" )));
        ss.add( anchoredPrefix      = new Spool<>( () -> new AnchoredPrefix_     ( c )));
        ss.add( backslashedSpecial  = new Spool<>( () -> new BackslashedSpecial_ ( c )));
        ss.add( commentAppender     = new Spool<>( () -> new CommentAppender_    ( c )));
        ss.add( commentBlock        = new Spool<>( () -> new CommentBlock_       ( c )));
        ss.add( commentBlockLine    = new Spool<>( () -> new CommentBlock_.Line_ ( c )));
        ss.add( groupDelimiter      = new Spool<>( () -> new GroupDelimiter_     ( c )));
        ss.add( indentBlind         = new Spool<>( () -> new IndentBlind_        ( c )));
        ss.add( indentBlindLine     = new Spool<>( () -> new IndentBlind_.Line_  ( c )));
        ss.add( literalizer         = new Spool<>( () -> new Literalizer_        ( c )));
        ss.add( metacharacter       = new Spool<>( () -> new Metacharacter_      ( c )));
        ss.add( patternMatcher      = new Spool<>( () -> new PatternMatcher_     ( c )));
        initialize( ss ); }



    /** Spool of regexp anchored prefixes.
      */
    final Spool<AnchoredPrefix_> anchoredPrefix;



    /** Spool of regexp backslashed specials.
      */
    final Spool<BackslashedSpecial_> backslashedSpecial;



    /** Spool of comment appenders.
      */
    final Spool<CommentAppender_> commentAppender;



    /** Spool of comment blocks.
      */
    final Spool<CommentBlock_> commentBlock;



    /** Spool of comment-block lines.
      */
    final Spool<CommentBlock_.Line_> commentBlockLine;



    /** Spool of flat grana each reflective of a containment operator.
      */
    final Spool<@TagName("ContainmentOperator") FlatGranum> containmentOperator;



    /** Spool of flat grana each reflective of a division label.
      */
    final Spool<@TagName("DivisionLabel") FlatGranum> divisionLabel;



    /** Spool of generic flat grana each with a tag name of ‘Granum’.
      *
      *     @see Breccia.parser.Granum#tagName()
      */
    final Spool<FlatGranum> flatGranum;



    /** Spool of regexp group delimiters.
      */
    final Spool<GroupDelimiter_> groupDelimiter;



    /** Spool of indent blinds.
      */
    final Spool<IndentBlind_> indentBlind;



    /** Spool of indent-blind lines.
      */
    final Spool<IndentBlind_.Line_> indentBlindLine;



    /** Spool of regexp literalizers.
      */
    final Spool<Literalizer_> literalizer;



    /** Spool of regexp metacharacters.
      */
    final Spool<Metacharacter_> metacharacter;



    /** Spool of pattern matchers.
      */
    final Spool<PatternMatcher_> patternMatcher; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
