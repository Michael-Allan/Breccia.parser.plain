package Breccia.parser.plain;

import Breccia.parser.BodyFractum;


public abstract class BodyFractum_ extends Fractum_ implements BodyFractum {


    protected BodyFractum_( BrecciaCursor cursor, End_ end ) { super( cursor, end ); }



    public @Override void commit() {
        super.commit();
        cursor.bodyFractum( this ); }



   // ━━━  B o d y   F r a c t u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override PerfectIndent perfectIndent() { return perfectIndent; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static abstract class End_ extends Fractum_.End_ implements BodyFractum.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.bodyFractumEnd( this ); }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final PerfectIndent_ perfectIndent = new PerfectIndent_();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class PerfectIndent_ extends FlatMarkup implements PerfectIndent {


        PerfectIndent_() { super( BodyFractum_.this ); }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int lineNumber() { return BodyFractum_.this.lineNumber(); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
