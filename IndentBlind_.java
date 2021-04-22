package Breccia.parser.plain;

import Breccia.parser.IndentBlind;
import Breccia.parser.Markup;
import java.util.ArrayList;
import java.util.List;


final class IndentBlind_ extends Markup_ implements IndentBlind {


    IndentBlind_( final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor; }



    final List<Line> components = new ArrayList<>();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Line> components() { return components; }



    public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class Line_ extends Markup_ implements Line {


        Line_( final BrecciaCursor cursor ) {
            super( cursor.buffer );
            this.cursor = cursor;
            indentWhenPresent    = FlatMarkup.make( cursor );
            delimiter            = FlatMarkup.make( cursor, "Delimiter" );
            substanceWhenPresent = FlatMarkup.make( cursor ); }



        final List<Markup> components = new ArrayList<>( /*initial capacity*/componentsMaximum );



        private static final int componentsMaximum = 4;
          // Indent, delimiter, substance and comment appender.



        private final BrecciaCursor cursor;



        final FlatMarkup delimiter;



        final FlatMarkup indentWhenPresent;



        FlatMarkup substance;



        final FlatMarkup substanceWhenPresent;



       // ━━━  I n d e n t   B l i n d  .  L i n e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override Markup substance() { return substance; }



        public final @Override Markup delimiter() { return delimiter; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Markup> components() {
            assert components.size() <= componentsMaximum; // Within optimal capacity, as initialized.
            return components; }



        public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
