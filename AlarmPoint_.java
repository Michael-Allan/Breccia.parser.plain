package Breccia.parser.plain;

import Breccia.parser.AlarmPoint;


final class AlarmPoint_ extends NonCommandPoint implements AlarmPoint {


    AlarmPoint_( BrecciaCursor cursor ) { super( cursor ); }



    AlarmPoint_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.alarmPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends Point_<BrecciaCursor>.End_ implements AlarmPoint.End {


        @Override void commit() {
            super.commit();
            cursor.alarmPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
