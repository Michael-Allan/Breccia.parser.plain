package Breccia.parser.plain;

import Breccia.parser.CommandPoint;
import Breccia.parser.Markup;
import Java.DelimitableCharSequence;
import java.util.*;

import static Java.CharBuffers.newDelimitableCharSequence;


/** @param <C> The type of cursor.
  */
public abstract class CommandPoint_<C extends BrecciaCursor> extends Point_<C> implements CommandPoint {


    /** Partly makes an instance for `initialize` to finish.
      */
    CommandPoint_( final C cursor ) { super( cursor ); }



    /** Initializes with a newly made keyword.
      */
    final void initialize() { initialize( newDelimitableCharSequence( cursor.buffer )); }



    /** @see #keyword
      */
    final void initialize( final DelimitableCharSequence keyword ) {
        this.keyword = keyword;
        components = List.of( perfectIndent, bullet, descriptor() ); }



   // ━━━  C o m m a n d   P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Set<Modifier> modifierSet() { return modifierSet; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Do not modify after `initialize`.
      *
      *     @see BrecciaCursor#commandPointKeywords
      */
    private List<Markup> components;



    /** Do not modify after `initialize`.
      *
      *     @see BrecciaCursor#commandPointKeywords
      */
    DelimitableCharSequence keyword;



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
