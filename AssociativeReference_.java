package Breccia.parser.plain;

import Breccia.parser.AssociativeReference;


public class AssociativeReference_ extends CommandPoint_ implements AssociativeReference {


    public AssociativeReference_( BrecciaCursor cursor ) { super( cursor, new End_( cursor ) ); }



    public AssociativeReference_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.associativeReference( this ); }



    public ImperativeClause imperativeClause() { throw new UnsupportedOperationException(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static class End_ extends CommandPoint_.End_ implements AssociativeReference.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.associativeReferenceEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
