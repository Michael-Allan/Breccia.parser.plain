package Breccia.parser.plain;

import Breccia.parser.CommentaryHolder;
import Breccia.parser.CommentBlock;
import java.util.ArrayList;
import java.util.List;


final class CommentBlock_ extends Markup_ implements CommentBlock {


    CommentBlock_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor; }



    final List<Line> components = new ArrayList<>();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Line> components() { return components; }



    public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static final class Line_ extends CommentaryHolder_ implements Line {


        Line_( final BrecciaCursor cursor ) {
            super( /*c0_white*/FlatMarkup.make(cursor), cursor );
            initialize( /*c3_commentaryWhenPresent*/new FlatMarkup( cursor ) {
                public @Override int column() { return cursor.bufferColumn( text.start() ); }
                public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }
                public @Override String tagName() { return c3_commentaryTagName; }}); }



        private String c3_commentaryTagName;



        /** Sets the tag name for any commentary that occurs in this line.
          */
        void c3_commentaryTagName( String name ) { c3_commentaryTagName = name; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return 0; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
