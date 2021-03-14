package Breccia.parser.plain;

import Breccia.parser.CommandPoint;


public abstract class CommandPoint_ extends Point_ implements CommandPoint {


    protected CommandPoint_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.commandPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static abstract class End_ extends Point_.End_ implements CommandPoint.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.commandPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
