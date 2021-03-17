package Breccia.parser.plain;

import Breccia.parser.Fractum;

import static Java.Classes.nameWithoutPackageLeader;


abstract class Fractum_ extends Markup_ implements Fractum {


    Fractum_( final BrecciaCursor cursor, final End_ end ) {
        super( cursor.buffer );
        this.cursor = cursor;
        this.end = end; }



    void commit() { cursor.fractum( this ); }



    final BrecciaCursor cursor;



    /** The corresponding end state of this fractum.
      */
    final End_ end;



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override int lineNumber() { return cursor.fractumLineNumber(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a fractum.
      */
    static abstract class End_ implements Fractum.End {


        End_( BrecciaCursor cursor ) { this.cursor = cursor; }



        void commit() { cursor.fractumEnd( this ); }



        final BrecciaCursor cursor;



       // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override String toString() { return nameWithoutPackageLeader( getClass() ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
