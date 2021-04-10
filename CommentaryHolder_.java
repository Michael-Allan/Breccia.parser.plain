package Breccia.parser.plain;

import Breccia.parser.CommentaryHolder;
import Breccia.parser.Markup;
import java.util.List;


abstract class CommentaryHolder_ extends Markup_ implements CommentaryHolder {


    /** Partly makes an instance for `initialize` to finish.
      *
      *     @see #c0_white
      */
    CommentaryHolder_( final Markup_ c0_white, final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor;
        final Markup[] cc = new Markup_[5];
        cc[0] = this.c0_white = c0_white;
        cc[1] = c1_delimiter = FlatMarkup.make( cursor, "Delimiter" );
        cc[2] = c2_white     = FlatMarkup.make( cursor );
        cc[4] = c4_white     = FlatMarkup.make( cursor );
        components = new DelimitableMarkupList( cc ); }



    /** @see #c3_commentaryWhenPresent
      */
    final void initialize( Markup_ c3_commentaryWhenPresent ) {
        components.set( 3, this.c3_commentaryWhenPresent = c3_commentaryWhenPresent ); }



    /** Any whitespace that leads this holder (line of a comment block),
      * or null if none can occur for this type of holder (comment appender).
      */
    final Markup_ c0_white;



    final Markup_ c1_delimiter;



    final Markup_ c2_white;



    Markup c3_commentary;



    /** Do not modify after `initialize`.
      */
    Markup_ c3_commentaryWhenPresent;



    final Markup_ c4_white;



    /** A component list of 2 to 5 elements starting with `c0_white` in case of a comment-block line;
      * or 1 to 4 elements starting with `c1_delimiter` in case of a comment appender.  In both cases,
      * the order of components is fixed according to the ordinal numbers of the namesake fields.
      */
    final DelimitableMarkupList components;



    final BrecciaCursor cursor;



   // ━━━  C o m m e n t a r y   H o l d e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Markup commentary() { return c3_commentary; }



    public final @Override Markup delimiter() { return c1_delimiter; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



    public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
