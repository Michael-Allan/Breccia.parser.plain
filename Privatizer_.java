package Breccia.parser.plain;

import Breccia.parser.Privatizer;


final class Privatizer_ extends SimpleCommandPoint<BrecciaCursor> implements Privatizer {


    Privatizer_( BrecciaCursor cursor ) { super( cursor ); }



    Privatizer_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        final int xuncParent; {
            for( int h = cursor.hierarchy.size();; ) {
                if( h == 0 ) {
                    xuncParent = -1; // File fractum.
                    break; }
                final var hierarchParent = cursor.hierarchy.get( --h );
                if( hierarchParent != null ) {
                    xuncParent = hierarchParent.xunc();
                    break; }}}
        cursor.xuncPrivatized.add( xuncParent );
        cursor.privatizer( this ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements Privatizer.End {


        protected @Override void commit() {
            super.commit();
            cursor.privatizerEnd( this ); }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
