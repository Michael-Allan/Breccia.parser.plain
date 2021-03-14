package Breccia.parser.plain;

import Breccia.parser.PlainCommandPoint;


public class PlainCommandPoint_ extends CommandPoint_ implements PlainCommandPoint {


    public PlainCommandPoint_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



    public PlainCommandPoint_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.plainCommandPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static class End_ extends CommandPoint_.End_ implements PlainCommandPoint.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.plainCommandPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
