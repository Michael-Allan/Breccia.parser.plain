package Breccia.parser.plain;

import Breccia.parser.BodyFractum;


public abstract class BodyFractum_ extends Fractum_ implements BodyFractum {


    protected BodyFractum_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.bodyFractum( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static abstract class End_ extends Fractum_.End_ implements BodyFractum.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.bodyFractumEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
