package Breccia.parser.plain;

import Breccia.parser.CommandPoint;


/** @param <C> The type of cursor.
  */
public abstract class CommandPoint_<C extends BrecciaCursor> extends Point_<C> implements CommandPoint {


    protected CommandPoint_( C cursor ) { super( cursor ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.commandPoint( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    protected abstract class End_ extends Point_<C>.End_ implements CommandPoint.End {


        protected @Override void commit() {
            super.commit();
            cursor.commandPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
