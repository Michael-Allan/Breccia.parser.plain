package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.CommentAppender;


final class CommentAppender_ extends CommentaryHolder_ implements CommentAppender {


    CommentAppender_( final BrecciaCursor cursor ) {
        super( /*c0_white*/null, cursor );
        initialize( /*c3_commentaryWhenPresent*/FlatGranum.make( cursor, "Commentary" ));
        components.start( 1 ); } // An appender starts without `c0_white`.



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
