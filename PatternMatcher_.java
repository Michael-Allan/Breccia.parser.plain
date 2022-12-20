package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.PatternMatcher;
import Breccia.parser.Granum;
import java.util.List;


final class PatternMatcher_ extends Granum_ implements PatternMatcher {


    PatternMatcher_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new DelimitableGranumList(
          patternDelimiterLeft = FlatGranum.make( cursor, "PatternDelimiter"),
          pattern = new Pattern( cursor ),
          patternDelimiterRight = FlatGranum.make( cursor, "PatternDelimiter"),
          matchModifiersWhenPresent = FlatGranum.make( cursor, "MatchModifiers" )); }



    /** A component list of 3 or 4 elements, depending on whether a match-modifier series is present.
      */
    final DelimitableGranumList components;



    FlatGranum matchModifiers;



    final FlatGranum matchModifiersWhenPresent;



    final Pattern pattern;



    final FlatGranum patternDelimiterLeft;



    final FlatGranum patternDelimiterRight;



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Granum> components() { return components; }



    public final @Override @AdjunctSlow int lineNumber() {
        return cursor.bufferLineNumber( text.start() ); }



   // ━━━  P a t t e r n   M a t c h e r ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Granum matchModifiers() { return matchModifiers; }



    public @Override Granum pattern() { return pattern; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class BackslashedSpecial_ extends PatternElement implements BackslashedSpecial {


        BackslashedSpecial_ ( BrecciaCursor cursor ) { super( cursor ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class GroupDelimiter_ extends PatternElement implements GroupDelimiter {


        GroupDelimiter_ ( BrecciaCursor cursor ) { super( cursor ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class Literalizer_ extends PatternElement implements Literalizer {


        Literalizer_ ( BrecciaCursor cursor ) { super( cursor ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class Metacharacter_ extends PatternElement implements Metacharacter {


        Metacharacter_ ( BrecciaCursor cursor ) { super( cursor ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class AnchoredPrefix_ extends PatternElement implements AnchoredPrefix {


        AnchoredPrefix_ ( BrecciaCursor cursor ) { super( cursor ); }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor;



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private static abstract class PatternElement extends FlatGranum {


        PatternElement( BrecciaCursor cursor ) { super( cursor ); }


       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



        public @Override @AdjunctSlow int lineNumber() {
            return cursor.bufferLineNumber( text.start() ); }}}



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
