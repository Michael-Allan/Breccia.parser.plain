package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.CommentaryHolder;
import Breccia.parser.CommentBlock;
import java.util.ArrayList;
import java.util.List;


final class CommentBlock_ extends Granum_ implements CommentBlock {


    CommentBlock_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor; }



    final List<Line> components = new ArrayList<>();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Line> components() { return components; }



    public @Override @AdjunctSlow int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class Line_ extends CommentaryHolder_ implements Line {


        Line_( final BrecciaCursor cursor ) {
            super( /*c0_white*/FlatGranum.make(cursor), cursor );
            initialize( /*c3_commentaryWhenPresent*/new FlatGranum( cursor ) {
                public @Override @AdjunctSlow int column() {
                    return cursor.bufferColumn( text.start() ); }
                public @Override @AdjunctSlow int lineNumber() {
                    return cursor.bufferLineNumber( text.start() ); }
                public @Override String tagName() { return c3_commentaryTagName; }}); }



        private String c3_commentaryTagName;



        /** Sets the tag name for any commentary that occurs in this line.
          */
        void c3_commentaryTagName( String name ) { c3_commentaryTagName = name; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
