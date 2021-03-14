package Breccia.parser.plain;

import Breccia.parser.Privatizer;


public class Privatizer_ extends CommandPoint_ implements Privatizer {


    public Privatizer_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



    public Privatizer_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.privatizer( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static class End_ extends CommandPoint_.End_ implements Privatizer.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.privatizerEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
