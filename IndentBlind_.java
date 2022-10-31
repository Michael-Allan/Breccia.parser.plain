package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.IndentBlind;
import Breccia.parser.Granum;
import java.util.ArrayList;
import java.util.List;


final class IndentBlind_ extends Granum_ implements IndentBlind {


    IndentBlind_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor; }



    final List<Line> components = new ArrayList<>();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Line> components() { return components; }



    public @Override @AdjunctSlow int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class Line_ extends Granum_ implements Line {


        Line_( final BrecciaCursor cursor ) {
            super( cursor );
            this.cursor = cursor;
            indentWhenPresent    = FlatGranum.make( cursor );
            delimiter            = FlatGranum.make( cursor, "Delimiter" );
            substanceWhenPresent = FlatGranum.make( cursor ); }



        final List<Granum> components = new ArrayList<>( /*initial capacity*/componentsMaximum );



        private static final int componentsMaximum = 4;
          // Indent, delimiter, substance and comment appender.



        private final BrecciaCursor cursor;



        final FlatGranum delimiter;



        final FlatGranum indentWhenPresent;



        FlatGranum substance;



        final FlatGranum substanceWhenPresent;



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Granum> components() {
            assert components.size() <= componentsMaximum; // Within optimal capacity, as initialized.
            return components; }



        public final @Override @AdjunctSlow int lineNumber() {
            return cursor.bufferLineNumber( text.start() ); }



       // ━━━  I n d e n t   B l i n d  .  L i n e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override Granum substance() { return substance; }



        public final @Override Granum delimiter() { return delimiter; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
