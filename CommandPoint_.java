package Breccia.parser.plain;

import Breccia.parser.CommandPoint;
import Java.DelimitableCharSequence;
import java.util.EnumSet;
import java.util.Set;

import static Java.CharBuffers.newDelimitableCharSequence;


/** @param <C> The type of cursor.
  */
public abstract class CommandPoint_<C extends BrecciaCursor> extends Point_<C> implements CommandPoint {


    protected CommandPoint_( C cursor ) { super( cursor ); }



   // ━━━  C o m m a n d   P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Set<Modifier> modifierSet() { return modifierSet; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** @see BrecciaCursor#commandPointKeywords
      */
    final DelimitableCharSequence keyword = newDelimitableCharSequence( cursor.buffer );



    final Set<Modifier> modifierSet = EnumSet.noneOf( Modifier.class );



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
