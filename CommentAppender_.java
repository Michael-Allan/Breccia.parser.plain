package Breccia.parser.plain;

import Breccia.parser.CommentAppender;


final class CommentAppender_ extends CommentaryHolder_ implements CommentAppender {


    CommentAppender_( final BrecciaCursor cursor ) {
        super( /*c0_white*/null, cursor );
        components.start( 1 ); } // An appender starts without `c0_white`.



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { return cursor.bufferColumn( text.start() ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
