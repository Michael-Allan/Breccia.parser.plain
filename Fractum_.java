package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.Fractum;

import static Java.Classes.nameWithoutPackageLeader;


/** @param <C> The type of cursor.
  */
abstract class Fractum_<C extends BrecciaCursor> extends Markup_ implements Fractum {


    Fractum_( final C cursor ) {
        super( cursor );
        this.cursor = cursor; }



    /** Sets this fractum as the present parse state.
      */
    void commit() { cursor.fractum( this ); }



    protected final C cursor;



    /** The corresponding end state of this fractum.
      */
    protected End_ end;



   // ━━━  F r a c t u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override int lineCount() { return cursor.fractumLineEnds.length; }



    public final @Override int xuncLineEnd( final int i ) {
        return cursor.xunc + cursor.fractumLineEnds.array[i]; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @AdjunctSlow int lineNumber() { return cursor.fractumLineNumber(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a fractum.
      */
    abstract class End_ implements Fractum.End {


        void commit() { cursor.fractumEnd( this ); }



        public @Override String toString() { return nameWithoutPackageLeader( getClass() ); }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
