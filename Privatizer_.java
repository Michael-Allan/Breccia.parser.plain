package Breccia.parser.plain;

import Breccia.parser.Privatizer;


class Privatizer_ extends CommandPoint_<BrecciaCursor> implements Privatizer {


    Privatizer_( BrecciaCursor cursor ) { super( cursor ); }



    Privatizer_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.privatizer( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    class End_ extends CommandPoint_<BrecciaCursor>.End_ implements Privatizer.End {


        protected @Override void commit() {
            super.commit();
            cursor.privatizerEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
