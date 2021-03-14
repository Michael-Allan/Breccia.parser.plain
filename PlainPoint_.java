package Breccia.parser.plain;

import Breccia.parser.PlainPoint;


public class PlainPoint_ extends Point_ implements PlainPoint {


    public PlainPoint_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



    public PlainPoint_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.plainPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static class End_ extends Point_.End_ implements PlainPoint.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.plainPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
