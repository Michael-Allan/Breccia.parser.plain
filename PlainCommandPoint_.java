package Breccia.parser.plain;

import Breccia.parser.PlainCommandPoint;


class PlainCommandPoint_ extends CommandPoint_ implements PlainCommandPoint {


    PlainCommandPoint_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.plainCommandPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static class End_ extends CommandPoint_.End_ implements PlainCommandPoint.End {


        End_( BrecciaCursor cursor ) { super( cursor ); }



       // ━━━  F r a c t u m _   .   E n d _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        @Override void commit() {
            super.commit();
            cursor.plainCommandPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
