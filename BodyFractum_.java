package Breccia.parser.plain;

import Breccia.parser.BodyFractum;
import Breccia.parser.Granum;


/** @param <C> The type of cursor.
  */
abstract class BodyFractum_<C extends BrecciaCursor> extends Fractum_<C> implements BodyFractum {


    BodyFractum_( C cursor ) { super( cursor ); }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.bodyFractum( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    abstract class End_ extends Fractum_<C>.End_ implements BodyFractum.End {


        @Override void commit() {
            super.commit();
            cursor.bodyFractumEnd( this ); }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
