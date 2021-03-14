package Breccia.parser.plain;

import Breccia.parser.FileFractum;


public class FileFractum_ extends Fractum_ implements FileFractum {


    public FileFractum_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



    protected FileFractum_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.fileFractum( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static class End_ extends Fractum_.End_ implements FileFractum.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.fileFractumEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
