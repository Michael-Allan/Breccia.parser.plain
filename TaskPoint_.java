package Breccia.parser.plain;

import Breccia.parser.TaskPoint;


final class TaskPoint_ extends NonCommandPoint implements TaskPoint {


    TaskPoint_( BrecciaCursor cursor ) { super( cursor ); }



    TaskPoint_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.taskPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends Point_<BrecciaCursor>.End_ implements TaskPoint.End {


        @Override void commit() {
            super.commit();
            cursor.taskPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
