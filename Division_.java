package Breccia.parser.plain;

import Breccia.parser.Division;


public class Division_ extends BodyFractum_ implements Division {


    public Division_( BrecciaCursor cursor ) { super( cursor, new End_( cursor )); }



    public Division_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.division( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static class End_ extends BodyFractum_.End_ implements Division.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.divisionEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
