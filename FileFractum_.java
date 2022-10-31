package Breccia.parser.plain;

import Breccia.parser.FileFractum;
import Breccia.parser.Granum;
import java.util.List;


final class FileFractum_ extends Fractum_<BrecciaCursor> implements FileFractum {


    FileFractum_( BrecciaCursor cursor ) {
        super( cursor );
        componentsWhenPresent = new CoalescentArrayList( cursor.spooler ); }



    List<Granum> components;



    static final List<Granum> componentsWhenAbsent = List.of(); /* Setting this will be marginally faster
      than would be the alternative of `components.clear`, at least for a `CoalescentArrayList`. */



    final CoalescentGranumList componentsWhenPresent;



    FileFractum_ endSet() {
        end = new End_();
        return this; }



    /** Control flag for late composition, subsequent to `commit`, for sake of efficient parsing.
      */
    boolean isComposed;



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        assert lineNumber() == 1; // As per contract.
        if( cursor.segmentEnd == 0 ) {
            components = FileFractum_.componentsWhenAbsent;
            isComposed = true; }
        else isComposed = false; // Pending demand.
        cursor.fileFractum( this ); }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Granum> components() {
        if( !isComposed ) {
            cursor.composeFileFractum();
            isComposed = true; }
        assert components == componentsWhenPresent ? componentsWhenPresent.isFlush() : true;
        return components; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends Fractum_<BrecciaCursor>.End_ implements FileFractum.End {


        @Override void commit() {
            super.commit();
            cursor.fileFractumEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
