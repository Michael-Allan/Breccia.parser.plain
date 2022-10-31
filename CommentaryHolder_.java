package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.CommentaryHolder;
import Breccia.parser.Granum;
import java.util.List;


abstract class CommentaryHolder_ extends Granum_ implements CommentaryHolder {


    /** Partly makes an instance for `initialize` to finish.
      *
      *     @see #c0_white
      */
    CommentaryHolder_( final Granum_ c0_white, final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        final Granum[] cc = new Granum_[5];
        cc[0] = this.c0_white = c0_white;
        cc[1] = c1_delimiter = FlatGranum.make( cursor, "Delimiter" );
        cc[2] = c2_white     = FlatGranum.make( cursor );
        cc[4] = c4_white     = FlatGranum.make( cursor );
        components = new DelimitableGranumList( cc ); }



    /** @see #c3_commentaryWhenPresent
      */
    final void initialize( Granum_ c3_commentaryWhenPresent ) {
        components.set( 3, this.c3_commentaryWhenPresent = c3_commentaryWhenPresent ); }



    /** Any whitespace that leads this holder (line of a comment block),
      * or null if none can occur for this type of holder (comment appender).
      */
    final Granum_ c0_white;



    final Granum_ c1_delimiter;



    final Granum_ c2_white;



    Granum c3_commentary;



    /** Do not modify after `initialize`.
      */
    Granum_ c3_commentaryWhenPresent;



    final Granum_ c4_white;



    /** A component list of 2 to 5 elements starting with `c0_white` in case of a comment-block line;
      * or 1 to 4 elements starting with `c1_delimiter` in case of a comment appender.  In both cases,
      * the order of components is fixed according to the ordinal numbers of the namesake fields.
      */
    final DelimitableGranumList components;



    final BrecciaCursor cursor;



   // ━━━  C o m m e n t a r y   H o l d e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Granum commentary() { return c3_commentary; }



    public final @Override Granum delimiter() { return c1_delimiter; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Granum> components() { return components; }



    public final @Override @AdjunctSlow int lineNumber() {
        return cursor.bufferLineNumber( text.start() ); }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
