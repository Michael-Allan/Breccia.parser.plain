package Breccia.parser.plain;

import Breccia.parser.FileFractum;
import Breccia.parser.Markup;
import java.util.List;


final class FileFractum_ extends Fractum_<BrecciaCursor> implements FileFractum {


    FileFractum_( BrecciaCursor cursor ) {
        super( cursor );
        componentsWhenPresent = new CoalescentArrayList( cursor.spooler ); }



    List<Markup> components;



    static final List<Markup> componentsWhenAbsent = List.of(); /* Setting this will be marginally faster
      than would be the alternative of `components.clear`, at least for a `CoalescentArrayList`. */



    final CoalescentMarkupList componentsWhenPresent;



    FileFractum_ endSet() {
        end = new End_();
        return this; }



    /** Late composition control flag.  Cleared on committing a non-empty configuration of this fractum
      * through its `commit` method.  Set on late composition as triggered by a call to `components`.
      *
      *     @see #commit()
      *     @see #components()
      */
    boolean isComposed; /* Justification of late parsing and composition: Use cases exist which care
      nothing for file fracta but their reification, and these may benefit from the time saved
      by leaving the components unparsed, as they contribute nothing to that reification. */



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        if( cursor.segmentEnd == 0 ) {
            components = FileFractum_.componentsWhenAbsent;
            isComposed = true; }
        else isComposed = false; // Pending demand.
        cursor.fileFractum( this ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() {
        if( !isComposed ) {
            cursor.composeFileFractum();
            isComposed = true; }
        assert components == componentsWhenPresent? componentsWhenPresent.isFlush() : true;
        return components; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends Fractum_<BrecciaCursor>.End_ implements FileFractum.End {


        @Override void commit() {
            super.commit();
            cursor.fileFractumEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
