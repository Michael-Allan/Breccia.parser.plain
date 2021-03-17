package Breccia.parser.plain;

import Breccia.parser.PlainPoint;


class PlainPoint_ extends Point_ implements PlainPoint {


    PlainPoint_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.plainPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    static class End_ extends Point_.End_ implements PlainPoint.End {


        End_( BrecciaCursor cursor ) { super( cursor ); }



       // ━━━  F r a c t u m _   .   E n d _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        @Override void commit() {
            super.commit();
            cursor.plainPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
