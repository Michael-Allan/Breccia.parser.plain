package Breccia.parser.plain;

import Breccia.parser.CommentaryHolder;
import Breccia.parser.Markup;
import java.util.List;


abstract class CommentaryHolder_ extends Markup_ implements CommentaryHolder {


    /** @see #c0_white
      */
    protected CommentaryHolder_( final FlatMarkup c0_white, final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor;
        components = new DelimitableMarkupList(
          this.c0_white            = c0_white,
          c1_delimiter             = FlatMarkup.make( cursor, "Delimiter" ),
          c2_white                 = FlatMarkup.make( cursor ),
          c3_commentaryWhenPresent = FlatMarkup.make( cursor, "Commentary" ),
          c4_white                 = FlatMarkup.make( cursor ));}



    /** Any whitespace that leads this holder (line of a comment block),
      * or null if none can occur for this type of holder (comment appender).
      */
    final FlatMarkup c0_white;



    final FlatMarkup c1_delimiter;



    final FlatMarkup c2_white;



    FlatMarkup c3_commentary;



    final FlatMarkup c3_commentaryWhenPresent;



    final FlatMarkup c4_white;



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
