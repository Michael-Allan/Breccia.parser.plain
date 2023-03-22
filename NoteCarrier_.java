package Breccia.parser.plain;

import Breccia.parser.NoteCarrier;
import Breccia.parser.PatternMatcher;


final class NoteCarrier_ extends CommandPoint_<BrecciaCursor> implements NoteCarrier {


    NoteCarrier_( BrecciaCursor cursor ) { super( cursor ); }



    NoteCarrier_ endSet() {
        end = new End_();
        return this; }



    final FlatGranum labelWhenPresent = FlatGranum.make( cursor, "Label" );



    PatternMatcher_ patternMatcher;



    final PatternMatcher_ patternMatcherWhenPresent = new PatternMatcher_( cursor );



    final FlatGranum prepositionWhenPresent = FlatGranum.make( cursor, "Preposition" );



   // ━━━  N o t e   C a r r i e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override PatternMatcher patternMatcher() throws MalformedText {
        ensureComposition();
        return patternMatcher; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.noteCarrier( this ); }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Late composition.  Do not call this method directly.  It is called by `ensureComposition` alone,
      * which in turn is called by the access methods of the parts of this note carrier that allow
      * for late composition, namely `patternMatcher`, `appendageClause` and `descriptor.components`.
      *
      *     @see #ensureComposition()
      *     @see #patternMatcher()
      *     @see #appendageClause()
      *     @see Descriptor#components()
      */
    final @Override void compose() throws MalformedText { cursor.composeNoteCarrier(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements NoteCarrier.End {


        protected @Override void commit() {
            super.commit();
            cursor.noteCarrierEnd( this ); }}}



                                                        // Copyright © 2023  Michael Allan.  Licence MIT.
