package Breccia.parser.plain;

import Breccia.parser.Fractum;

import static Java.Classes.nameWithoutPackageLeader;


public abstract class Fractum_ extends DeepMarkup implements Fractum {


    protected Fractum_( final BrecciaCursor cursor, final End_ end ) {
        super( cursor.buffer );
        this.cursor = cursor;
        this.end = end; }



    public void commit() { cursor.fractum( this ); }



    /** The corresponding end state of this fractum.
      */
    public final End_ end;



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override int lineNumber() { return cursor.fractumLineNumber(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a fractum.
      */
    public static abstract class End_ implements Fractum.End {


        protected End_( BrecciaCursor cursor ) { this.cursor = cursor; }



        public void commit() { cursor.fractumEnd( this ); }



        protected final BrecciaCursor cursor;



       // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override String toString() { return nameWithoutPackageLeader( getClass() ); }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
