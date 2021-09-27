package Breccia.parser.plain;

import Breccia.parser.Privatizer;


final class Privatizer_ extends SimpleCommandPoint<BrecciaCursor> implements Privatizer {


    Privatizer_( BrecciaCursor cursor ) { super( cursor, FlatMarkup.make( cursor, "Command" )); }



    Privatizer_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.privatizer( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements Privatizer.End {


        protected @Override void commit() {
            super.commit();
            cursor.privatizerEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
