package Breccia.parser.plain;

import Breccia.parser.Fractum;
import Breccia.parser.Markup;
import Java.DelimitableCharSequence;
import java.util.List;

import static Java.CharBuffers.newDelimitableCharSequence;


public abstract class Fractum_ extends Markup_ implements Fractum {


    protected Fractum_( BrecciaCursor cursor ) {
        this.cursor = cursor;
        text = newDelimitableCharSequence( cursor.buffer ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public final @Override int lineNumber() { return cursor.fractumLineNumber(); }



    public final @Override CharSequence text() { return text; }



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() { return toString( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a fractum.
      */
    public static abstract class End_ implements Fractum.End {


        protected End_() {}}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor;



    final DelimitableCharSequence text; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
